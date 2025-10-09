package com.example

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureStaticContent() {
    routing {
        staticResources("/admin", "static")
        staticResources("/admin_korisnici", "static")
    }
}
