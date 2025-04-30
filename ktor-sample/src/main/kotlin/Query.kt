package com.example

import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

// insert into Zlocin Table in mySql
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

        statement?.setInt(1,zlocin.tipZlocinaId)
        statement?.setString(2, zlocin.naziv)
        statement?.setTimestamp(3, java.sql.Timestamp(zlocin.datum))
        statement?.setString(4, zlocin.mesto)
        statement?.setString(5, zlocin.opis)
        statement?.setString(6, zlocin.status)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            // Vraca generisani ID
            zlocin.idZlocin=resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

//insertOsobaData


fun insertOsobaData(osobaData: OsobaData,zlocin: ZlocinData){
    val query = """
        INSERT INTO Osoba (ime, kontakt, datum, zanimanje, pol,zlocinId)
        VALUES (?, ?, ?, ?, ?, ?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, osobaData.ime)
        statement?.setString(2, osobaData.kontakt)
        statement?.setTimestamp(3, java.sql.Timestamp(osobaData.datum))
        statement?.setString(4, osobaData.zanimanje)
        statement?.setString(5, osobaData.pol)
        statement?.setInt(6, zlocin.idZlocin)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            // Vraca generisani ID
            osobaData.idOsoba=resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}


fun insertZrtva(zrtvaData: ZrtvaData,zlocin: ZlocinData,osoba:OsobaData){
    val query = """
        INSERT INTO Zrtva (tipZrtve, detalji, statusZrtva, zlocinId, osobaId)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, zrtvaData.tipZrtve)
        statement?.setString(2, zrtvaData.detalji)
        statement?.setString(3, zrtvaData.statusZrtva)
        statement?.setInt(4, zrtvaData.zlocinId)
        statement?.setInt(5, zlocin.idZlocin)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            // Vraca generisani ID
            zrtvaData.idZrtva=resultSet.getInt(1)
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
        INSERT INTO osumnjicen (statusS, tipOsumnjicen, motiv, zlocinId, kriv, osobaId)
        VALUES (?, ?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, osumnjicen.status)
        statement?.setString(2, osumnjicen.tipOsumnjicen)
        statement?.setInt(3, motiv.idMotiv)
        statement?.setInt(4, zlocin.idZlocin)
        statement?.setInt(5, osumnjicen.kriv)
        statement?.setInt(6, osumnjicen.osobaId)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            // Vraca generisani ID
            osumnjicen.idOsumnjicen=resultSet.getInt(1)
        }

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
    //insertOdnosOsumnjicenZrtvaData(osumnjicen,zrtva)
}

//fun insertOdnosOsumnjicenZrtvaData(osumnjicen: OsumnjicenData, zrtva: ZrtvaData) {
//    val query = """
//        INSERT INTO odnosOsumnjicenZrtva (osumnjicenId, zrtvaId, tipOdnosa)
//        VALUES (?, ?, ?)
//    """
//    var conn: Connection? = null
//    var statement: PreparedStatement? = null
//    var resultSet: ResultSet? = null
//
//    try {
//        conn = getDatabaseConnection()
//        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
//
//        statement?.setInt(1, osumnjicen.idOsumnjicen)
//        statement?.setInt(2, zrtva.idZrtva)
//        //statement?.setString(3, osumnjicen.odnosZrtva)
//
//        statement?.executeUpdate()
//
//        resultSet = statement?.generatedKeys
//        if (resultSet?.next() == true) {
//            osumnjicen.idOsumnjicen=resultSet.getInt(1)
//        }
//
//    } catch (e: SQLException) {
//        e.printStackTrace()
//    } finally {
//        closeResources(conn, statement, null)
//    }
//}

fun insertDokazData(dokaz: DokazData, zlocin: ZlocinData, zrtva: ZrtvaData){
                    //, osumnjiceni: List<OsumnjicenData>){
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
        statement?.setInt(4, zlocin.idZlocin)  // tip osumnjičenog (pojedinac, organizacija itd.)
        statement?.setInt(5, zrtva.idZrtva)  // motiv osumnjičenog (ID motiva)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            dokaz.idDokaz=resultSet.getInt(1)
        }

       // val osumnjicen = osumnjiceni.find { it.ime == dokaz.osumnjicen.ime }

        //if (osumnjicen != null) {
          //  dokaz.osumnjicen = osumnjicen
        //}

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
    //insertDokazOsumnjicenData(dokaz)
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

        statement?.setInt(1, dokaz.idDokaz)
        //statement?.setInt(2, dokaz.osumnjicen.idOsumnjicen)

        statement?.executeUpdate()

    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun insertSvedokData(svedok: SvedokData, zlocin: ZlocinData){
    val query = """
        INSERT INTO svedok (izjava, statusSvedok, statusIspitan, zlocinId, osobaId)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, svedok.izjava)
        statement?.setString(2, svedok.statusSvedok)
        statement?.setInt(3, svedok.statusIspitan)
        statement?.setInt(4, zlocin.idZlocin)
        statement?.setInt(5, svedok.osobaId)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            svedok.idSvedok=resultSet.getInt(1)
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

    //val osumnjicen = osumnjiceni.find { it.ime == alibi.osumnjicen.ime }
    //if (osumnjicen != null) {
      //  alibi.osumnjicen = osumnjicen
    //}

    //if (alibi.svedok?.osobaId == "") {
      //  alibi.svedok = null
    //}

    if (alibi.svedok != null) {
    //    val svedok = svedoci.find { it.ime == alibi.svedok?.ime }
      //  if (svedok != null) {
        //    alibi.svedok = svedok
        //}
    }

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, alibi.osumnjicen.idOsumnjicen)

        if (alibi.svedok != null) {
            alibi.svedok?.idSvedok?.let { statement?.setInt(2, it) }
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
        statement?.setInt(4,  zrtva.idZrtva)
        statement?.setString(5, obdukcija.informacije)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            obdukcija.idObdukcija = resultSet.getInt(1)
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
        statement?.setInt(4,  zrtva.idZrtva)
        statement?.setString(5, forenzickiDokaz.veza)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            forenzickiDokaz.idForenzickiDokaz = resultSet.getInt(1)
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
        statement?.setInt(3,  zrtva.idZrtva)
        statement?.setString(4,  telefon.sifra)
        statement?.setString(5, telefon.informacije)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            telefon.idTelefon = resultSet.getInt(1)
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

        statement?.setInt(1, zlocin.idZlocin)
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

// one contact

fun insertOneContactData(oneContactData: OneContactData, zlocin: ZlocinData) {
    val query = """
        INSERT INTO oneContact (zlocinId, ime, broj, slika)
        VALUES (?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, zlocin.idZlocin)
        statement?.setString(2, oneContactData.ime)
        statement?.setString(3, oneContactData.broj)
        statement?.setInt(4,  oneContactData.slika)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            oneContactData.idOneContact = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// beleska

fun insertBeleskaData(beleskaData: BeleskaData, zlocin: ZlocinData) {
    val query = """
        INSERT INTO beleska (zlocinId, tekst, datum)
        VALUES (?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, zlocin.idZlocin)
        statement?.setString(2, beleskaData.tekst)
        statement?.setTimestamp(3, java.sql.Timestamp(beleskaData.datum))

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            beleskaData.idBeleska = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// WhatsAppKontakt

fun insertWhatsAppKontaktData(whatsAppKontaktDataData: WhatsAppKontaktData, zlocin: ZlocinData) {
    val query = """
        INSERT INTO whatsappkontakt (zlocinId, ime, broj, slika)
        VALUES (?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, zlocin.idZlocin)
        statement?.setString(2, whatsAppKontaktDataData.ime)
        statement?.setString(3, whatsAppKontaktDataData.broj)
        if (whatsAppKontaktDataData.slika != null) {
            statement?.setInt(4, whatsAppKontaktDataData.slika)
        }
        else {
            statement?.setNull(4, java.sql.Types.INTEGER)  // Correctly set null for slika
        }

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            whatsAppKontaktDataData.idWhatsAppKontakt = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// WhatsAppPoruka

fun insertWhatsAppPorukaData(whatsAppPorukaData: WhatsAppPorukaData, kontaktKoSalje: WhatsAppKontaktData, kontaktKomeSalje: WhatsAppKontaktData) {
    val query = """
        INSERT INTO whatsappporuka (kontaktKoSalje, kontaktKomeSalje, tekst, datum, procitana)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, kontaktKoSalje.idWhatsAppKontakt)
        statement?.setInt(2, kontaktKomeSalje.idWhatsAppKontakt)
        statement?.setString(3, whatsAppPorukaData.tekst)
        statement?.setTimestamp(4, java.sql.Timestamp(whatsAppPorukaData.datum))
        statement?.setBoolean(5, whatsAppPorukaData.procitana)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            whatsAppPorukaData.idWhatsAppPoruka = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// OneCall

fun insertOneCallData(oneCallData: OneCallData, kontakt: OneContactData) {
    val query = """
        INSERT INTO onecall (kontakt, datum, propusten, dolazni)
        VALUES (?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, oneCallData.kontakt)
        statement?.setTimestamp(2, java.sql.Timestamp(oneCallData.datum))
        statement?.setBoolean(3, oneCallData.propusten)
        statement?.setBoolean(4, oneCallData.dolazni)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            oneCallData.idOneCall = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// Gallery

fun insertGalleryData(galleryData: GalleryData, zlocin: ZlocinData) {
    val query = """
        INSERT INTO gallery (zlocinId, slika, datum, mesto)
        VALUES (?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, zlocin.idZlocin)
        if (galleryData.slika != null) {
            statement?.setInt(2, galleryData.slika)
        }
        else {
            statement?.setNull(2, java.sql.Types.INTEGER)  // Correctly set null for slika
        }
        statement?.setTimestamp(3, java.sql.Timestamp(galleryData.datum))
        statement?.setString(4, galleryData.mesto)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            galleryData.idPhoto = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// ObicnaPoruka

fun insertObicnaPorukaData(obicnaPorukaData: ObicnaPorukaData, kontaktKoSalje: OneContactData, kontaktKomeSalje: OneContactData) {
    val query = """
        INSERT INTO obicnaporuka (kontaktKoSalje, kontaktKomeSalje, tekst, datum, procitana)
        VALUES (?, ?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, kontaktKoSalje.idOneContact)
        statement?.setInt(2, kontaktKomeSalje.idOneContact)
        statement?.setString(3, obicnaPorukaData.tekst)
        statement?.setTimestamp(4, java.sql.Timestamp(obicnaPorukaData.datum))

        if (obicnaPorukaData.procitana != null) {
            statement?.setBoolean(5, obicnaPorukaData.procitana)
        } else {
            statement?.setNull(5, java.sql.Types.BOOLEAN)
        }

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            obicnaPorukaData.idObicnaPoruka = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// OdnosOsumnjicenZrtva

fun insertOdnosOsumnjicenZrtvaData(odnosOsumnjicenZrtvaData: OdnosOsumnjicenZrtvaData, osumnjicenData: OsumnjicenData, zrtvaData: ZrtvaData) {
    val query = """
        INSERT INTO odnososumnjicenzrtva (osumnjicenId, zrtvaId, tipOdnosa)
        VALUES (?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, osumnjicenData.idOsumnjicen)
        statement?.setInt(2, zrtvaData.idZrtva)
        statement?.setString(3, odnosOsumnjicenZrtvaData.tipOdnosa)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            odnosOsumnjicenZrtvaData.idOdnos = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// PrijavljeniKorisnik

fun insertPrijavljeniKorisnikData(prijavljeniKorisnikData: PrijavljeniKorisnikData) {
    val query = """
        INSERT INTO prijavljenikorisnik (korisnickoIme, sifra)
        VALUES (?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, prijavljeniKorisnikData.korisnickoIme)
        statement?.setString(2, prijavljeniKorisnikData.sifra)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            prijavljeniKorisnikData.idKorisnik = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// Pitanje

fun insertPitanjeData(pitanjeData: PitanjeData, zlocin: ZlocinData) {
    val query = """
        INSERT INTO pitanje (zlocinId, tekst)
        VALUES (?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, zlocin.idZlocin)
        statement?.setString(2, pitanjeData.tekst)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            pitanjeData.idPitanje = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// Odgovor

fun insertOdgovorData(odgovorData: OdgovorData, pitanje: PitanjeData) {
    val query = """
        INSERT INTO odgovor (pitanjeId, tekstOdgovora, tacan, bodovi)
        VALUES (?, ?, ?, ?)
    """
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, pitanje.idPitanje)
        statement?.setString(2, odgovorData.tekstOdgovora)
        statement?.setBoolean(3, odgovorData.tacan)
        statement?.setInt(4, odgovorData.bodovi)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            odgovorData.idOdogovor = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// PitanjeIspitivanjeOsumnjicenog

fun insertPitanjeIspitivanjeOsumnjicenogData(pitanjeIspitivanjeOsumnjicenogData: PitanjeIspitivanjeOsumnjicenogData, osumnjicen: OsumnjicenData) {
    val query = """
        INSERT INTO pitanjeispitivanjeosumnjicenog (kategorija, tekst, odgovor, komentar, osumnjicenId)
        VALUES (?, ?, ?, ?, ?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, pitanjeIspitivanjeOsumnjicenogData.kategorija)
        statement?.setString(2, pitanjeIspitivanjeOsumnjicenogData.tekst)
        statement?.setString(3, pitanjeIspitivanjeOsumnjicenogData.odgovor)
        statement?.setString(4, pitanjeIspitivanjeOsumnjicenogData.komentar)
        statement?.setInt(5, osumnjicen.idOsumnjicen)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            pitanjeIspitivanjeOsumnjicenogData.idPitanjeIspitivanjeOsumnjicenog = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// PitanjeIspitivanjeSvedoka

fun insertPitanjeIspitivanjeSvedokaData(pitanjeIspitivanjeSvedokaData: PitanjeIspitivanjeSvedokaData, svedok: SvedokData) {
    val query = """
        INSERT INTO pitanjeispitivanjesvedoka (tekst, odgovor, svedokId, nextPitanje)
        VALUES (?, ?, ?, ?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, pitanjeIspitivanjeSvedokaData.tekst)
        statement?.setString(2, pitanjeIspitivanjeSvedokaData.odgovor)
        statement?.setInt(3, svedok.idSvedok)
        statement?.setInt(4, pitanjeIspitivanjeSvedokaData.nextPitanje)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            pitanjeIspitivanjeSvedokaData.idPitanjeIspitivanjeSvedoka = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// Zadatak

fun insertZadatakData(zadatakData: ZadatakData, zlocin: ZlocinData) {
    val query = """
        INSERT INTO zadatak (tekst, korak, uradjen, nextZadatak, zlocinId)
        VALUES (?, ?, ?, ?, ?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, zadatakData.tekst)
        statement?.setString(2, zadatakData.korak)
        statement?.setBoolean(3, zadatakData.uradjen)
        statement?.setNull(4,  java.sql.Types.INTEGER)
        statement?.setInt(5, zlocin.idZlocin)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            zadatakData.idZadatak = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

fun getZadatakListaData(): List<ZadatakData> {
    val query = "SELECT idZadatak, tekst, korak, uradjen, nextZadatak, zlocinId FROM zadatak"
    val zadatakList = mutableListOf<ZadatakData>()

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query)
        resultSet = statement?.executeQuery()

        while (resultSet?.next() == true) {
            val zadatak = ZadatakData(
                idZadatak = resultSet.getInt("idZadatak"),
                tekst = resultSet.getString("tekst"),
                korak = resultSet.getString("korak"),
                uradjen = resultSet.getBoolean("uradjen"),
                nextZadatak = resultSet.getInt("nextZadatak"),
                zlocinId = resultSet.getInt("zlocinId")
            )
            zadatakList.add(zadatak)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, resultSet)
    }

    return zadatakList
}

fun updateZadatakListData(zadatakList: List<ZadatakData>) {
    val query = """
        UPDATE zadatak
        SET nextZadatak = ?
        WHERE idZadatak = ?
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query)

        for (i in 0 until zadatakList.size - 1) {
            val currentZadatak = zadatakList[i]
            val nextZadatak = zadatakList[i + 1]

            statement?.setInt(1, nextZadatak.idZadatak)
            statement?.setInt(2, currentZadatak.idZadatak)
            statement?.addBatch()
        }

        statement?.executeBatch()
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// DokazZadatak

fun insertDokazZadatakData(dokazZadatakData: DokazZadatakData, dokazData: DokazData, zadatakData: ZadatakData) {
    val query = """
        INSERT INTO dokazzadatak (tekst, dokazId, uradjen, zadatakId)
        VALUES (?, ?, ?, ?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, dokazZadatakData.tekst)
        statement?.setInt(2, dokazData.idDokaz)
        statement?.setBoolean(3, dokazZadatakData.uradjen)
        statement?.setInt(4, zadatakData.idZadatak)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            dokazZadatakData.idDokazZadatak = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// IspitivanjeOsumnjicenogZadatak

fun insertIspitivanjeOsumnjicenogZadatakData(ispitivanjeOsumnjicenogZadatakData: IspitivanjeOsumnjicenogZadatakData, osumnjicenData: OsumnjicenData, zadatakData: ZadatakData) {
    val query = """
        INSERT INTO ispitivanjeosumnjicenogzadatak (osumnjicenId, zadatakId, uradjen)
        VALUES (?, ?, ?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, osumnjicenData.idOsumnjicen)
        statement?.setInt(2, zadatakData.idZadatak)
        statement?.setBoolean(3, ispitivanjeOsumnjicenogZadatakData.uradjen)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            ispitivanjeOsumnjicenogZadatakData.idIspitivanjeOsumnjicenogZadatak = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// IspitivanjeSvedokaZadatak

fun insertIspitivanjeSvedokaZadatakData(ispitivanjeSvedokaZadatakData: IspitivanjeSvedokaZadatakData, svedokData: SvedokData, zadatakData: ZadatakData) {
    val query = """
        INSERT INTO ispitivanjesvedokazadatak (svedokId, zadatakId, uradjen)
        VALUES (?, ?, ?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, svedokData.idSvedok)
        statement?.setInt(2, zadatakData.idZadatak)
        statement?.setBoolean(3, ispitivanjeSvedokaZadatakData.uradjen)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            ispitivanjeSvedokaZadatakData.idIspitivanjeSvedokaZadatak = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// TelefonZadatak

fun insertTelefonZadatakData(telefonZadatakData: TelefonZadatakData, telefonData: TelefonData, zadatakData: ZadatakData) {
    val query = """
        INSERT INTO telefonzadatak (telefonId, zadatakId, uradjen)
        VALUES (?, ?, ?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setInt(1, telefonData.idTelefon)
        statement?.setInt(2, zadatakData.idZadatak)
        statement?.setBoolean(3, telefonZadatakData.uradjen)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            telefonZadatakData.idTelefonZadatak = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// ForenzickiDokazZadatak

fun insertForenzickiDokazZadatakData(forenzickiDokazZadatakData: ForenzickiDokazZadatakData, forenzickiDokazData: ForenzickiDokazData, zadatakData: ZadatakData) {
    val query = """
        INSERT INTO forenzickidokazzadatak (tekst, forenzickiDokazId, uradjen, zadatakId)
        VALUES (?, ?, ?, ?)
    """

    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

        statement?.setString(1, forenzickiDokazZadatakData.tekst)
        statement?.setInt(2, forenzickiDokazData.idForenzickiDokaz)
        statement?.setBoolean(3, forenzickiDokazZadatakData.uradjen)
        statement?.setInt(4, zadatakData.idZadatak)

        statement?.executeUpdate()

        resultSet = statement?.generatedKeys
        if (resultSet?.next() == true) {
            forenzickiDokazZadatakData.idForenzickiDokazZadatak = resultSet.getInt(1)
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, null)
    }
}

// PorukeZadatak

//fun insertPorukeZadatakData(porukeZadatakData: PorukeZadatakData, porukeData: PorukeData, zadatakData: ZadatakData) {
//    val query = """
//        INSERT INTO porukezadatak (porukeId, zadatakId, uradjen)
//        VALUES (?, ?, ?)
//    """
//
//    var conn: Connection? = null
//    var statement: PreparedStatement? = null
//    var resultSet: ResultSet? = null
//
//    try {
//        conn = getDatabaseConnection()
//        statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
//
//        statement?.setInt(1, porukeData.idPoruke)
//        statement?.setInt(2, zadatakData.idZadatak)
//        statement?.setBoolean(3, porukeZadatakData.uradjen)
//
//        statement?.executeUpdate()
//
//        resultSet = statement?.generatedKeys
//        if (resultSet?.next() == true) {
//            porukeZadatakData.idPorukeZadatak = resultSet.getInt(1)
//        }
//    } catch (e: SQLException) {
//        e.printStackTrace()
//    } finally {
//        closeResources(conn, statement, null)
//    }
//}