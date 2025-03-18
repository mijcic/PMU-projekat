package com.example

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/tipzlocin") {
            val zlocini = fetchTipZlocinaFromDatabase()
            call.respond(zlocini)  // vraca JSON obj
        }
        get("/zlocin") {
            val zlocini = fetchZlocinFromDatabase()
            call.respond(zlocini)  // vraca JSON obj
        }
        get("/clanorganizacije") {
            val organizacije = fetchClanOrganizacijeFromDatabase()
            call.respond(organizacije)  // vraca JSON obj
        }
        get("/dokaz") {
            val dokazi = fetchDokazFromDatabase()
            call.respond(dokazi)  // vraca JSON obj
        }
        get("/forenzickidokaz") {
            val dokazi = fetchForenzickiDokazFromDatabase()
            call.respond(dokazi)  // vraca JSON obj
        }

        staticResources("/static", "static")
    }
}

fun getDatabaseConnection(): Connection? {
    return DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/whodunit?useSSL=false&allowPublicKeyRetrieval=true",
        "root",
        "1234"
    )
}

fun closeResources(conn: Connection?, statement: PreparedStatement?, resultSet: ResultSet?) {
    resultSet?.close()
    statement?.close()
    conn?.close()
}

fun <T> executeQuery(query: String, rowMapper: (ResultSet) -> T): List<T> {
    val resultList = mutableListOf<T>()
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query)
        resultSet = statement?.executeQuery()

        while (resultSet?.next() == true) {
            resultList.add(rowMapper(resultSet))
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, resultSet)
    }

    return resultList
}

fun fetchTipZlocinaFromDatabase(): List<TipZlocinaDC> {
    val query = "SELECT * FROM tipzlocina"
    return executeQuery(query) { resultSet ->
        val id = resultSet.getInt("idTipZlocina")
        val naziv = resultSet.getString("naziv")
        TipZlocinaDC(id, naziv)
    }
}

fun fetchZlocinFromDatabase(): List<Zlocin> {
    val query = "SELECT * FROM zlocin"
    return executeQuery(query) { resultSet ->
        val id = resultSet.getInt("idZlocin")
        val naziv = resultSet.getString("naziv")
        val opis = resultSet.getString("opis")
        val idTipZlocina = resultSet.getInt("idTipZlocina")
        val datum = resultSet.getTimestamp("datum").time
        Zlocin(id, naziv, opis, idTipZlocina, datum)
    }
}

fun fetchClanOrganizacijeFromDatabase(): List<ClanOrganizacije> {
    val query = "SELECT * FROM clanorganizacije"
    return executeQuery(query) { resultSet ->
        val idClan = resultSet.getInt("idClan")
        val idOsumnjicen = resultSet.getInt("idOsumnjicen")
        val idOrganizacija = resultSet.getInt("idOrganizacija")
        ClanOrganizacije(idClan, idOsumnjicen, idOrganizacija)
    }
}

fun fetchDokazFromDatabase(): List<Dokaz> {
    val query = "SELECT * FROM dokaz"
    return executeQuery(query) { resultSet ->
        val idDokaz = resultSet.getInt("idDokaz")
        val opis = resultSet.getString("opis")
        val lokacija = resultSet.getString("lokacija")
        val idZlocin = resultSet.getInt("idZlocin")
        Dokaz(idDokaz, opis, lokacija, idZlocin)
    }
}

fun fetchForenzickiDokazFromDatabase(): List<ForenzickiDokaz> {
    val query = "SELECT * FROM forenzickidokaz"
    return executeQuery(query) { resultSet ->
        val idForenzickiDokaz = resultSet.getInt("idForenzickiDokaz")
        val opis = resultSet.getString("opis")
        val tip = resultSet.getString("tip")
        val idZlocin = resultSet.getInt("idZlocin")
        ForenzickiDokaz(idForenzickiDokaz, opis, tip, idZlocin)
    }
}