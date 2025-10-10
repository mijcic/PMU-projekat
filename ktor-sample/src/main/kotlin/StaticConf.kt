package com.example

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureStaticContent() {
    routing {
        staticResources("/admin", "static")
        staticResources("/admin_korisnici", "static")
        staticResources("/admin_addJson", "static")
        staticResources("/admin_addFormsMurder", "static")
        staticResources("/admin_story", "static")
        staticResources("/admin_delete", "static")

        staticResources("/admin_stats", "static")
        staticResources("/admin_login", "static")
        staticResources("/admin_addGemini", "static")
    }
}
