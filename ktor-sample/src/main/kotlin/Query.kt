package com.example

import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

fun insertZlocin(zlocin: Zlocin) {
    val query = """
        INSERT INTO zlocin (tipZlocinaId, naziv, datum, mesto, opis, statusS)
        VALUES (?, ?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query)

        statement?.setInt(1,zlocin.idTipZlocina)
        statement?.setString(2, zlocin.naziv)
        statement?.setTimestamp(3, java.sql.Timestamp(zlocin.datum))
        statement?.setString(4, zlocin.mesto)
        statement?.setString(5, zlocin.opis)
        statement?.setString(6, zlocin.status)

        statement?.executeUpdate()
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertZlocinData(zlocin: ZlocinData) {
    val query = """
        INSERT INTO zlocin (tipZlocinaId, naziv, datum, mesto, opis, statusS)
        VALUES (?, ?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1,zlocin.idTipZlocina)
        statement?.setString(2, zlocin.naziv)
        statement?.setTimestamp(3, java.sql.Timestamp(zlocin.datum))
        statement?.setString(4, zlocin.mesto)
        statement?.setString(5, zlocin.opis)
        statement?.setString(6, zlocin.status)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            // Vraca generisani ID
            zlocin.id=resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertZrtva(zrtvaData: ZrtvaData,zlocin: ZlocinData){
    val query = """
        INSERT INTO Zrtva (tipZrtve, ime, detalji, statusZrtva, zlocinId)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, zrtvaData.tipZrtve)       // tipZrtve
        statement?.setString(2, zrtvaData.ime)            // ime
        statement?.setString(3, zrtvaData.detalji)        // detalji
        statement?.setString(4, zrtvaData.statusZrtva)    // statusZrtva
        statement?.setInt(5, zlocin.id)          // zlocinId

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            // Vraca generisani ID
            zrtvaData.id=resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertMotivData(motiv: MotivData){
    val query = """
        INSERT INTO Motiv (opis) 
        VALUES (?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, motiv.opis)
        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            // Vraca generisani ID
            motiv.idMotiv=resultSet.getInt(1)
        }

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertOsumnjicenData(osumnjicen: OsumnjicenData,zlocin: ZlocinData, motiv: MotivData, zrtva: ZrtvaData) {
    val query = """
        INSERT INTO osumnjicen (ime, statusS, tipOsumnjicen, motiv, idZlocin, kriv)
        VALUES (?, ?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, osumnjicen.ime)
        statement?.setInt(2, osumnjicen.status)
        statement?.setInt(3, osumnjicen.tipOsumnjicen)
        statement?.setInt(4, motiv.idMotiv)
        statement?.setInt(5, zlocin.id)
        statement?.setInt(6, osumnjicen.kriv)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            // Vraca generisani ID
            osumnjicen.id=resultSet.getInt(1)
        }

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
    insertOdnosOsumnjicenZrtvaData(osumnjicen,zrtva)
}

fun insertOdnosOsumnjicenZrtvaData(osumnjicen: OsumnjicenData, zrtva: ZrtvaData) {
    val query = """
        INSERT INTO odnosOsumnjicenZrtva (osumnjicenId, zrtvaId, tipOdnosa)
        VALUES (?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, osumnjicen.id)
        statement?.setInt(2, zrtva.id)
        statement?.setString(3, osumnjicen.odnosZrtva)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            osumnjicen.id=resultSet.getInt(1)
        }

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertDokazData(dokaz: DokazData, zlocin: ZlocinData, zrtva: ZrtvaData, osumnjiceni: List<OsumnjicenData>){
    val query = """
        INSERT INTO dokaz (tipDokaza, opis, statusS, zlocinId, zrtvaId)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, dokaz.tipDokaza)
        statement?.setString(2, dokaz.opis)
        statement?.setInt(3, dokaz.status)  // status osumnjičenog
        statement?.setInt(4, zlocin.id)  // tip osumnjičenog (pojedinac, organizacija itd.)
        statement?.setInt(5, zrtva.id)  // motiv osumnjičenog (ID motiva)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            dokaz.id=resultSet.getInt(1)
        }

        val osumnjicen = osumnjiceni.find { it.ime == dokaz.osumnjicen.ime }

        if (osumnjicen != null) {
            dokaz.osumnjicen = osumnjicen
        }

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
    insertDokazOsumnjicenData(dokaz)
}

fun insertDokazOsumnjicenData(dokaz: DokazData){
    val query = """
        INSERT INTO dokazOsumnjicen (dokazId, osumnjicenId)
        VALUES (?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, dokaz.id)
        statement?.setInt(2, dokaz.osumnjicen.id)

        statement?.executeUpdate()

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertSvedokData(svedok: SvedokData, zlocin: ZlocinData){
    val query = """
        INSERT INTO svedok (ime, kontakt, zlocinId, izjava, statusSvedok, statusS)
        VALUES (?, ?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, svedok.ime)
        statement?.setString(2, svedok.kontakt)
        statement?.setInt(3, zlocin.id)
        statement?.setString(4, svedok.izjava)
        statement?.setString(5, svedok.statusSvedok)
        statement?.setInt(6, svedok.status)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            svedok.id=resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}
fun insertAlibiData(alibi: AlibiData, zlocin: ZlocinData, osumnjiceni: List<OsumnjicenData>, svedoci: List<SvedokData>){
    val query = """
        INSERT INTO alibi (osumnjicenId, svedokId, opis, statusAlibija)
        VALUES (?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    val osumnjicen = osumnjiceni.find { it.ime == alibi.osumnjicen.ime }
    if (osumnjicen != null) {
        alibi.osumnjicen = osumnjicen
    }

    if (alibi.svedok?.ime == "") {
        alibi.svedok = null
    }

    if (alibi.svedok != null) {
        val svedok = svedoci.find { it.ime == alibi.svedok?.ime }
        if (svedok != null) {
            alibi.svedok = svedok
        }
    }

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, alibi.osumnjicen.id)

        if (alibi.svedok != null) {
            alibi.svedok?.id?.let { statement?.setInt(2, it) }
        } else {
            statement?.setNull(2, java.sql.Types.INTEGER)  // Correctly set null for svedokId
        }
        statement?.setString(3, alibi.opis)
        statement?.setString(4, alibi.statusAlibija)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            alibi.id = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertObdukcijaData(obdukcija: ObdukcijaData, zrtva: ZrtvaData){
    val query = """
        INSERT INTO obdukcija (izvestaj, datum, uzrokSmrti, zrtvaId,informacije)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, obdukcija.izvestaj)
        statement?.setDate(2, Date(obdukcija.datum))
        statement?.setString(3,  obdukcija.uzrokSmrti)
        statement?.setInt(4,  zrtva.id)
        statement?.setString(5, obdukcija.informacije)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            obdukcija.id = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertForenzickiDokaz(forenzickiDokaz: ForenzickiDokazData, zrtva: ZrtvaData){
    val query = """
        INSERT INTO forenzickiDokaz (tipForenzickiDokaz, opis, statusS, zrtvaId,veza)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, forenzickiDokaz.tipForenzickiDokaz)
        statement?.setString(2, forenzickiDokaz.opis)
        statement?.setInt(3,  forenzickiDokaz.statusS)
        statement?.setInt(4,  zrtva.id)
        statement?.setString(5, forenzickiDokaz.veza)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            forenzickiDokaz.id = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertTelefonData(telefon: TelefonData, zrtva: ZrtvaData){
    val query = """
        INSERT INTO telefon (model, os, zrtvaId, sifra, informacije)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, telefon.model)
        statement?.setString(2, telefon.os)
        statement?.setInt(3,  zrtva.id)
        statement?.setString(4,  telefon.sifra)
        statement?.setString(5, telefon.informacije)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            telefon.id = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}


fun insertMisijaPorukaData(misijaPoruka: MisijaPorukaData, zlocin: ZlocinData){
    val query = """
        INSERT INTO misijaPoruka (zlocinId, naziv, statusS, posiljalac, poruka)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, zlocin.id)
        statement?.setString(2, misijaPoruka.naziv)
        statement?.setInt(3,  misijaPoruka.statusS)
        statement?.setString(4,  misijaPoruka.posiljalac)
        statement?.setString(5, misijaPoruka.poruka)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            misijaPoruka.id = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// sign up

fun signUpKorisnik(korisnik: KorisnikRequest) {

    val query = """
        INSERT INTO korisnik (korisnickoIme, ime, prezime, sifra, email, nacinPrijave, poeni, poslednjaAktivnost)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, korisnik.korisnickoIme)
        statement?.setString(2, korisnik.ime)
        statement?.setString(3, korisnik.prezime)
        statement?.setString(4, korisnik.sifra)
        statement?.setString(5, korisnik.email)
        statement?.setString(6, "rucno")
        statement?.setInt(7, 0)
        statement?.setDate(8, Date(System.currentTimeMillis()))

        statement?.executeUpdate()

        //resultSet = statement?.generatedKeys
        //if (resultSet?.next() == true) {
            //korisnik.id=resultSet.getInt(1)
        //}

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}


fun checkKorisnik(korisnik: KorisnikRequest): Boolean {
    val query = """
        SELECT COUNT(*) FROM korisnik WHERE korisnickoIme=? OR email=?
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()  // Assuming this method returns a valid DB connection
        statement = conn?.prepareStatement(query)

        // Set the parameters for both username and email
        statement?.setString(1, korisnik.korisnickoIme)
        statement?.setString(2, korisnik.email)

        resultSet = statement?.executeQuery()

        // If COUNT(*) > 0, it means either the username or email already exists
        if (resultSet?.next() == true) {
            val count = resultSet.getInt(1)
            return count > 0
        }

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, resultSet)  // Make sure to close DB resources
    }

    return false  // No matching user found
}

// log in

fun logIn(korisnik: KorisnikRequest): Boolean {
    val query = """
        SELECT * FROM korisnik WHERE korisnickoIme = ? AND sifra = ?
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query)

        statement?.setString(1, korisnik.korisnickoIme)
        statement?.setString(2, korisnik.sifra)

        resultSet = statement?.executeQuery()

        if (resultSet?.next() == true) {
            return true
        }

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, resultSet)
    }

    return false
}
