package com.easternkite.remain.features.shopping.mcp

import com.easternkite.remain.ai.Gemini
import com.easternkite.remain.ai.startChat
import com.easternkite.remain.features.shopping.client.NaverShoppingClient
import dev.shreyaspatil.ai.client.generativeai.type.content
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val PROMPT_MCP_CONTEXT = """
You are a converter that transforms user natural language queries into structured parameters for the Naver Shopping Search API.

Use the following specifications to generate valid JSON:

- query (string): Extract the precise product name or keyword from the user query. This should focus only on the core item (e.g., "iPhone 15 Pro Max", "Galaxy Buds 2").
- display (integer): Number of items to display (1~10, default:null).
    - If the query includes an explicit number of desired results (e.g., "show me 5 options"), extract and use it.
    - Otherwise, default to 1.
- sort (string): Sort order of results.
  - "sim": by relevance (default)
  - "date": by recency
  - "asc": by lowest price
  - "dsc": by highest price
- filter (string, optional): Product type filter.
  - "naverpay": only show products with Naver Pay
- exclude (string, optional): Product types to exclude (colon-separated).
  - "used": exclude second-hand items
  - "rental": exclude rental items
  - "cbshop": exclude overseas purchase or resell
- brand (string, optional): Extract the expected brand name, if possible (e.g., Apple, Samsung, etc.).
- extra (string, optional): Include any additional user preferences or constraints that do not fit into other structured fields. This field should preserve original nuances like "기능 좋은 거", "애플 상품은 제외", etc.
- Respond with a JSON object only. Do NOT wrap it in triple backticks or use a markdown code block.

Example Input:
"Find me the cheapest wireless earbuds that are not second-hand or from overseas."

Expected Output:
```json
{
  "query": "wireless earbuds",
  "display": 100,
  "brand": "Apple"
}
"""

const val PROMPT_MCP_RESPONSE = """
You are a shopping assistant that selects only the main product the user is looking for, based on structured query parameters. You will receive a JSON object containing the user's search query and parameters.

**Input:**

The input is a JSON object with the following keys:

*   `query`: A natural language query string from the user (e.g., "wireless earbuds").
*   `display`: The number of search results initially retrieved. This information is for context, not direct filtering.
*   `brand`: (Optional) The desired brand of the product. If present, only products from this brand should be considered.

**Task:**

1.  Understand the exact intent of the user based on the `query`. If the query includes a product category (e.g., "wireless earbuds"), assume the user is searching for the **main product itself**, not accessories.
2.  Utilize the `brand` parameter to filter results. If `brand` is specified, only return products from that brand. If `brand` is not specified, consider products from any brand.
3.  Exclude irrelevant items such as cases, screen protectors, stands, or accessories unless the user specifically asks for them in the `query`.
4.  Select **only and exactly** the number of products specified by the `display` field (e.g., if `display` is 1, return only 1 product — never more, never less). This is a strict requirement.
4-1. Select Product that most closely match the main item described in the `query` and, if provided, from the specified `brand`
5.  Return only the following information for each selected product:
    *   `title`: product name
    *   `price`: lowest price
    *   `url`: product page link
    *   `imageUrl`: product image link
    *   `reason`: A brief explanation why it's a good match, considering the `query` and `brand` (if specified) using Korean Language.
6. Respond with a JSON object only. Do NOT wrap it in triple backticks or use a markdown code block.

- Output Format:

[
   {
    "title": "...",
    "price": 0,
    "url": "...",
    "imageUrl": "...",
    "reason": "..."
   }
]

- Example Input JSON:

{
  "query": "wireless earbuds",
  "sort": "asc",
  "filter": "naverpay",
  "display": 1,
  "brand": "Apple"
}
"""

class ShoppingMCP {
    val chat = Gemini.startChat()
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = NaverShoppingClient(
        clientId = System.getenv("NAVER_CLIENT_ID") ?: throw IllegalStateException("NAVER_CLIENT_ID is not set"),
        clientSecret = System.getenv("NAVER_CLIENT_SECRET") ?: throw IllegalStateException("NAVER_CLIENT_SECRET is not set")
    )

    suspend fun sendUserQuery(prompt: String): ShoppingContext {
        chat.history.clear()
        chat.history.add(
            content {
                text(PROMPT_MCP_CONTEXT)
            }
        )

        val llmResponse = chat.sendMessage(prompt).text ?: throw McpGenerationException("LLM response is null")
        println("llmResponse: ${llmResponse.withMarkDownRemoved()} before $llmResponse")
        return json.decodeFromString<ShoppingContext>(llmResponse.withMarkDownRemoved())
    }


    suspend fun createRecommendsByContext(context: ShoppingContext): List<ShoppingRecommended> {
        chat.history.clear()
        val shopResponse = client.searchProducts(
            query = context.query,
            sort = context.sort,
            filter = context.filter,
        ).bodyAsText()

        println("ctx = $context")
        chat.history.add(
            content {
                text(PROMPT_MCP_RESPONSE)
                text(Json.encodeToString(context))
            }
        )

        val llmSelectedProducts = chat.sendMessage(shopResponse).text ?: throw McpGenerationException("LLM response is null")
        println("llmSelectedProducts: $llmSelectedProducts")
        return json.decodeFromString<List<ShoppingRecommended>>(llmSelectedProducts.withMarkDownRemoved())
    }
}

fun String.withMarkDownRemoved(): String {
    return this.replace("```json", "").replace("```", "")
}