package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
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
            call.respond(organizacije)
        }
        get("/dokaz") {
            val dokazi = fetchDokazFromDatabase()
            call.respond(dokazi)
        }
        get("/forenzickidokaz") {
            val dokazi = fetchForenzickiDokazFromDatabase()
            call.respond(dokazi)
        }

        //post zahtevi

        post("/postZlocin"){
            try {
                println("Post zlocin")
                println("call $call")
                val zlocin = call.receive<Zlocin>()
                println("Received Zlocin: $zlocin")
                insertZlocin(zlocin) // Insert data into the database
                call.respond("Zlocin inserted successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, MessageResponse("Failed to insert Zlocin"))
            }
        }

        post("/insertData"){
            try{
                val zlocin = call.receive<ZlocinRequest>()
                //println("$zlocin")

                insertZlocinData(zlocin.zlocin)
                insertZrtva(zlocin.zrtva,zlocin.zlocin)
                insertObdukcijaData(zlocin.obdukcija, zlocin.zrtva)
                insertTelefonData(zlocin.telefon, zlocin.zrtva)

                for (i in zlocin.motivi.indices) {
                    val motiv = zlocin.motivi[i]
                    val osumnjicen = zlocin.osumnjicen[i] // pretpostavljamo da su 'motivi' i 'osumnjicen' iste duzine

                    insertMotivData(motiv)
                    insertOsumnjicenData(osumnjicen, zlocin.zlocin, motiv, zlocin.zrtva)
                }
                for(dokaz in zlocin.dokazi){
                    insertDokazData(dokaz, zlocin.zlocin, zlocin.zrtva, zlocin.osumnjicen)
                }
                for(svedok in zlocin.svedok){
                    insertSvedokData(svedok, zlocin.zlocin)
                }
                for(alibi in zlocin.alibi){
                    insertAlibiData(alibi,zlocin.zlocin,zlocin.osumnjicen,zlocin.svedok)
                }
                for(forenzickiDokaz in zlocin.forenzickiDokazi){
                    insertForenzickiDokaz(forenzickiDokaz, zlocin.zrtva)
                }
                for (misijaPoruka in zlocin.misijaPoruka){
                    insertMisijaPorukaData(misijaPoruka, zlocin.zlocin)
                }

                call.respond("Zlocin inserted successfully")
            }
            catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, MessageResponse("Failed to insert Zlocin"))
            }
        }

        post("/signUp"){
            try{
                println("signUp")
                val korisnik = call.receive<KorisnikRequest>()
                val b= checkKorisnik(korisnik)
                if(b){
                    call.respond("Korisnik already exists.")
                }
                else{
                    signUpKorisnik(korisnik)
                    call.respond("Korisnik inserted successfully")
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, MessageResponse("Failed to insert Korisnik"))
            }
        }

        post("/logIn"){
            try{
                println("logIn")
                val korisnik = call.receive<KorisnikRequest>()
                val result = logIn(korisnik)
                if (result) {
                    call.respond(MessageResponse("TRUE"))
                }
                else {
                    call.respond(MessageResponse("FALSE"))
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, MessageResponse("Failed to log in Korisnik"))
            }
        }

        staticResources("/static", "static")
    }
}

fun getDatabaseConnection(): Connection? {
    return DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/whodunit?useSSL=false&allowPublicKeyRetrieval=true",
        "root",
        //"1234"
        "mia123"
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
        val mesto = resultSet.getString("mesto")
        val status = resultSet.getString("status")
        Zlocin(id, naziv, opis, idTipZlocina, datum,mesto, status)
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