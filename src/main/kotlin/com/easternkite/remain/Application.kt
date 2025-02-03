package com.easternkite.remain

import com.easternkite.remain.features.fortune.fortuneRoute
import com.easternkite.remain.features.time.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 2424, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    fortuneRoute()
}
