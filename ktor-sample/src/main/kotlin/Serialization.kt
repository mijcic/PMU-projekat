package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Configures content negotiation and JSON serialization for the Ktor application.
 *
 * This function installs the [ContentNegotiation] plugin using `kotlinx.serialization` for JSON handling.
 *
 * Usage:
 * - Automatically serializes and deserializes JSON in request/response bodies.
 * - Useful for APIs expecting or returning JSON-formatted data.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}