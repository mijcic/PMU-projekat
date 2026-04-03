package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

/**
 * Entry point of the server application.
 * Starts an embedded Ktor server on port 8080 using Netty.
 */
fun main() {
    embeddedServer(Netty, port = 8080,module = Application::module).start(wait = true)
}

/**
 * Configures the Ktor application module.
 * Sets up serialization and routing.
 */
fun Application.module() {
    configureSerialization()
    configureRouting()
    configureStaticContent()
}