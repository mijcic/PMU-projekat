package rs.ac.bg.etf.projekat.phone

import io.realm.kotlin.types.RealmInstant
import rs.ac.bg.etf.projekat.data.realm.ObicnaPorukaR
import rs.ac.bg.etf.projekat.data.realm.OneContactR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppKontaktR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppPorukaR
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class navDestination(
    val route: String,
    val label: String,
    val icon: Int
)

data class WhatsAppPreviewItem(
    val kontakt: WhatsAppKontaktR,
    val lastMessage: WhatsAppPorukaR?
)

data class OneContactPreviewItem(
    val kontakt: OneContactR,
    val lastMessage: ObicnaPorukaR?
)

fun realmInstantToTimeString(realmInstant: RealmInstant?): String {
    if (realmInstant == null) return ""

    val instant = Instant.ofEpochSecond(
        realmInstant.epochSeconds,
        realmInstant.nanosecondsOfSecond.toLong()
    )

    val zoneId = ZoneId.systemDefault()
    val zonedDateTime = instant.atZone(zoneId)
    val localDate = zonedDateTime.toLocalDate()
    val today = LocalDate.now(zoneId)
    val yesterday = today.minusDays(1)

    return when (localDate) {
        today -> {
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            timeFormatter.format(zonedDateTime)
        }
        yesterday -> "Yesterday"
        else -> {
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            dateFormatter.format(localDate)
        }
    }
}

fun realmInstantToDateString(realmInstant: RealmInstant?): String {
    if (realmInstant == null) return ""

    val instant = Instant.ofEpochSecond(
        realmInstant.epochSeconds,
        realmInstant.nanosecondsOfSecond.toLong()
    )

    val zoneId = ZoneId.systemDefault()
    val localDate = instant.atZone(zoneId).toLocalDate()
    val today = LocalDate.now(zoneId)
    val yesterday = today.minusDays(1)

    return when (localDate) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            formatter.format(localDate) + "."
        }
    }
}

fun realmInstantForWA(realmInstant: RealmInstant?): String {
    if (realmInstant == null) return ""

    val instant = Instant.ofEpochSecond(
        realmInstant.epochSeconds,
        realmInstant.nanosecondsOfSecond.toLong()
    )

    val zoneId = ZoneId.systemDefault()
    val zonedDateTime = instant.atZone(zoneId)
    val localDate = zonedDateTime.toLocalDate()
    val today = LocalDate.now(zoneId)
    val yesterday = today.minusDays(1)

    return when (localDate) {
        today -> {
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            timeFormatter.format(zonedDateTime.toLocalTime())
        }
        yesterday -> "Yesterday"
        else -> {
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
            dateFormatter.format(localDate)
        }
    }
}