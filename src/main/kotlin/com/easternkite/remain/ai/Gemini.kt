package com.easternkite.remain.ai

import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.BlockThreshold
import dev.shreyaspatil.ai.client.generativeai.type.Content
import dev.shreyaspatil.ai.client.generativeai.type.GenerationConfig
import dev.shreyaspatil.ai.client.generativeai.type.HarmCategory
import dev.shreyaspatil.ai.client.generativeai.type.SafetySetting

/**
 * `Gemini` object provides a singleton instance of the Gemini GenerativeModel.
 *
 * This object encapsulates the configuration and initialization of the Gemini 1.5 Flash model,
 * ensuring consistent access and setup throughout the application.
 *
 * **Key Features:**
 *
 * - **Singleton Instance:** Provides a single, readily accessible `GenerativeModel` instance.
 * - **Model Specification:** Configures the `GenerativeModel` to use the "gemini-1.5-flash" model.
 * - **API Key:** Uses the `KEY_GEMINI` constant for authentication with the Gemini API.
 * - **Safety Settings:** Configures safety settings to block content based on specified harm categories,
 *   excluding the `UNKNOWN` category and setting the threshold to `UNSPECIFIED`.
 *
 * **Usage:**
 *
 * ```kotlin
 * val geminiModel = Gemini.model
 * // Use geminiModel for text generation, image analysis, etc.
 * ```
 *
 * **Note:**
 *
 * - The `KEY_GEMINI` constant, representing your API key, should be defined elsewhere in your project.
 * - The safety settings are configured to block content from all known HarmCategories with an unspecified threshold.
 *   You can modify them as needed.
 *
 * @property model A `GenerativeModel` instance configured with the "gemini-1.5-flash" model, API key, and safety settings.
 */
object Gemini {
    val model = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = KEY_GEMINI,
        generationConfig = GenerationConfig.builder()
            .apply {
                temperature = 2.0f
            }.build(),
        safetySettings = HarmCategory.entries
            .filter { it != HarmCategory.UNKNOWN }
            .map { SafetySetting(it, BlockThreshold.UNSPECIFIED) }
    )
}

/**
 * Starts a new chat session with the Gemini model.
 *
 * This function initiates a chat session, optionally providing a history of previous interactions.
 * The returned [Chat] object can then be used to send further messages to the model and receive responses.
 *
 * @param history An optional list of [Content] objects representing the chat history.
 *                Each [Content] in the list represents a turn in the conversation.
 *                If not provided, a new chat session with no history is started.
 *                Defaults to an empty list.
 * @return A [Chat] object representing the active chat session.
 */
fun Gemini.startChat(history: List<Content> = emptyList()) = model.startChat(history)
