package com.example

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureStaticContent() {
    routing {
        staticResources("/admin", "static/templates")
        staticResources("/admin_korisnici", "static/templates")
        staticResources("/admin_addJson", "static/templates")
        staticResources("/admin_addFormsMurder", "static/templates")
        staticResources("/admin_story", "static/templates")
        staticResources("/admin_delete", "static/templates")

        staticResources("/admin_stats", "static/templates")
        staticResources("/admin_login", "static/templates")
        staticResources("/admin_addGemini", "static/templates")

        staticResources("/css", "static/css")
        staticResources("/fonts", "static/fonts")
        staticResources("/img", "static/img")
        staticResources("/js", "static/js")
    }
}
