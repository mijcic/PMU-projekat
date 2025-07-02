package com.example.repository

import com.example.closeResources
import com.example.data.remote.tables.*
import java.sql.*

class RepositoryInsert(private val conn: Connection){

    //insert into UsedZlocin Table in mysql
    fun insertUsedZlocinData(usedZlocin: UsedZlocinData) {
        val query = """
            INSERT INTO usedzlocin (zlocinId, used)
            VALUES (?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1,usedZlocin.zlocinId.idZlocin)
            statement.setBoolean(2, usedZlocin.used)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                // Vraca generisani ID
                usedZlocin.idUsedZlocin=resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }

    // insert into Zlocin Table in mySql
    fun insertZlocinData(zlocin: ZlocinData) {
        val query = """
            INSERT INTO zlocin (tipZlocinaId, naziv, datum, mesto, opis, statusS)
            VALUES (?, ?, ?, ?, ?, ?)
        """

        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1,zlocin.tipZlocinaId)
            statement.setString(2, zlocin.naziv)
            statement.setTimestamp(3, java.sql.Timestamp(zlocin.datum))
            statement.setString(4, zlocin.mesto)
            statement.setString(5, zlocin.opis)
            statement.setString(6, zlocin.status)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
    fun insertOsobaData(osobaData: OsobaData, zlocin: ZlocinData){
        val query = """
            INSERT INTO Osoba (ime, kontakt, datum, zanimanje, pol,zlocinId)
            VALUES (?, ?, ?, ?, ?, ?)
        """

        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, osobaData.ime)
            statement.setString(2, osobaData.kontakt)
            statement.setTimestamp(3, java.sql.Timestamp(osobaData.datum))
            statement.setString(4, osobaData.zanimanje)
            statement.setString(5, osobaData.pol)
            statement.setInt(6, zlocin.idZlocin)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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

    fun insertZrtva(zrtvaData: ZrtvaData, zlocin: ZlocinData, osoba: OsobaData){
        val query = """
            INSERT INTO Zrtva (tipZrtve, detalji, statusZrtva, zlocinId, osobaId)
            VALUES (?, ?, ?, ?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, zrtvaData.tipZrtve)
            statement.setString(2, zrtvaData.detalji)
            statement.setString(3, zrtvaData.statusZrtva)
            statement.setInt(4, zrtvaData.zlocinId)
            statement.setInt(5, osoba.idOsoba)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                // Vraca generisani ID
                zrtvaData.idZrtva=resultSet.getInt(1)
                zrtvaData.osobaId=osoba
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

        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, motiv.opis)
            statement.executeUpdate()
            resultSet = statement.generatedKeys
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

    fun insertOsumnjicenData(osumnjicen: OsumnjicenData, zlocin: ZlocinData, motiv: MotivData) {
        val query = """
            INSERT INTO osumnjicen (statusS, tipOsumnjicen, motiv, zlocinId, kriv, osobaId)
            VALUES (?, ?, ?, ?, ?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, osumnjicen.status)
            statement.setString(2, osumnjicen.tipOsumnjicen)
            statement.setInt(3, motiv.idMotiv)
            statement.setInt(4, zlocin.idZlocin)
            statement.setInt(5, osumnjicen.kriv)
            statement.setInt(6, osumnjicen.osobaId.idOsoba)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                // Vraca generisani ID
                osumnjicen.idOsumnjicen=resultSet.getInt(1)
                osumnjicen.motiv=motiv
                osumnjicen.zlocinId=zlocin.idZlocin
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
        val query = """
            INSERT INTO dokaz (tipDokaza, opis, statusS, zlocinId, zrtvaId)
            VALUES (?, ?, ?, ?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            if (dokaz.tipDokaza != "digitalni" && dokaz.tipDokaza != "fizicki") dokaz.tipDokaza = "fizicki"

            statement.setString(1, dokaz.tipDokaza)
            statement.setString(2, dokaz.opis)
            statement.setInt(3, dokaz.status)
            statement.setInt(4, zlocin.idZlocin)
            statement.setInt(5, zrtva.idZrtva)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                dokaz.idDokaz=resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
        //insertDokazOsumnjicenData(dokaz)
    }

    fun insertSvedokData(svedok: SvedokData, zlocin: ZlocinData){
        val query = """
            INSERT INTO svedok (izjava, statusSvedok, statusIspitan, zlocinId, osobaId)
            VALUES (?, ?, ?, ?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, svedok.izjava)
            statement.setString(2, svedok.statusSvedok)
            statement.setInt(3, svedok.statusIspitan)
            statement.setInt(4, zlocin.idZlocin)
            statement.setInt(5, svedok.osobaId.idOsoba)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
            //conn = getDatabaseConnection()
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

        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, obdukcija.izvestaj)
            statement.setTimestamp(2, Timestamp(obdukcija.datum))
            statement.setString(3,  obdukcija.uzrokSmrti)
            statement.setInt(4,  zrtva.idZrtva)
            statement.setString(5, obdukcija.informacije)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, forenzickiDokaz.tipForenzickiDokaz)
            statement.setString(2, forenzickiDokaz.opis)
            statement.setInt(3,  forenzickiDokaz.statusS)
            statement.setInt(4,  zrtva.idZrtva)
            statement.setString(5, forenzickiDokaz.veza)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, telefon.model)
            statement.setString(2, telefon.os)
            statement.setInt(3,  zrtva.idZrtva)
            statement.setString(4,  telefon.sifra)
            statement.setString(5, telefon.informacije)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                telefon.idTelefon = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }


    fun insertKontaktData(kontakt: KontaktData, zrtva: ZrtvaData){
        conn.autoCommit = true
        val query = """
        INSERT INTO kontakt (ime, broj, statusS, zrtvaId)
        VALUES (?, ?, ?, ?)
    """
        //var conn: Connection? = null
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            //conn = getDatabaseConnection()
            statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

            statement?.setString(1, kontakt.ime)
            statement?.setString(2, kontakt.broj)
            statement?.setInt(3,  kontakt.status)
            statement?.setInt(4,  zrtva.idZrtva)

            statement?.executeUpdate()

            resultSet = statement?.generatedKeys
            if (resultSet?.next() == true) {
                kontakt.idKontakt = resultSet.getInt(1)
                kontakt.zrtvaId=zrtva
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }

    }

    fun insertPorukeData(poruke: PorukeData, zrtva: ZrtvaData, kontakt: KontaktData){

        val query = """
        INSERT INTO poruke (tipPoruke, sadrzaj, datumVreme, zrtvaId,posiljalacId,statusPoruke,sifrovana)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """
        //var conn: Connection? = null
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null


        try {
            //conn = getDatabaseConnection()
            statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

            statement?.setString(1, poruke.tipPoruke)
            statement?.setString(2, poruke.sadrzaj)
            statement?.setTimestamp(3, Timestamp(poruke.datumVreme))
            statement?.setInt(4,  zrtva.idZrtva)
            statement?.setInt(5,  kontakt.idKontakt)
            statement?.setString(6,  poruke.statusPoruke)
            statement?.setBoolean(7,  poruke.sifrovana)

            statement?.executeUpdate()

            resultSet = statement?.generatedKeys
            if (resultSet?.next() == true) {
                poruke.idPoruke = resultSet.getInt(1)
                poruke.zrtvaId=zrtva
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }

    fun insertPoziviData(pozivi: PoziviData, zrtva: ZrtvaData, kontakt: KontaktData){

        val query = """
        INSERT INTO pozivi (tip, broj, datumVreme, zrtvaId,statusS,kontaktId)
        VALUES (?, ?, ?, ?, ?, ?)
    """
        //var conn: Connection? = null
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            //conn = getDatabaseConnection()
            statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

            statement?.setInt(1, pozivi.tip)
            statement?.setString(2, pozivi.broj)
            statement?.setTimestamp(3, Timestamp(pozivi.datumVreme))
            statement?.setInt(4,  zrtva.idZrtva)
            statement?.setInt(5,  pozivi.status)
            statement?.setInt(6,  kontakt.idKontakt)


            statement?.executeUpdate()

            resultSet = statement?.generatedKeys
            if (resultSet?.next() == true) {
                pozivi.idPoziv = resultSet.getInt(1)
                pozivi.zrtvaId=zrtva
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }

    fun insertGalerijaData(galerija: GalerijaData, zrtva: ZrtvaData){

        val query = """
        INSERT INTO galerija (tip, putanja, zrtvaId,datumVreme,lokacija)
        VALUES (?, ?, ?, ?, ?)
    """
        //var conn: Connection? = null
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            //conn = getDatabaseConnection()
            statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

            statement?.setInt(1, galerija.tip)
            statement?.setString(2, galerija.putanja)
            statement?.setInt(3,  zrtva.idZrtva)
            statement?.setTimestamp(4, Timestamp(galerija.datumVreme))
            statement?.setString(5,  galerija.lokacija)

            statement?.executeUpdate()

            resultSet = statement?.generatedKeys
            if (resultSet?.next() == true) {
                galerija.idGalerija = resultSet.getInt(1)
                galerija.zrtvaId=zrtva
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }

    fun insertAplikacijaData(aplikacija: AplikacijaData, zrtva: ZrtvaData){
        val query = """
            INSERT INTO aplikacija (naziv, tip, zrtvaId,aktivna,informacije)
            VALUES (?, ?, ?, ?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, aplikacija.naziv)
            statement.setInt(2, aplikacija.tip)
            statement.setInt(3,  zrtva.idZrtva)
            statement.setBoolean(4, aplikacija.aktivna)
            statement.setString(5,  aplikacija.informacije)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                aplikacija.idAplikacije = resultSet.getInt(1)
                aplikacija.zrtvaId=zrtva
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }


    fun insertTragData(trag: TragData, forenzickiDokaz: ForenzickiDokazData, osumnjicen: OsumnjicenData){
        val query = """
            INSERT INTO trag (forenzickiDokazId, osumnjicenId)
            VALUES (?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, forenzickiDokaz.idForenzickiDokaz)
            statement.setInt(2, osumnjicen.idOsumnjicen)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                trag.idTrag = resultSet.getInt(1)
                trag.osumnjicenId=osumnjicen
                trag.forenzickiDokazId=forenzickiDokaz
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }

    fun insertDokazOsumnjicenData(dokazOsumnjicen: DokazOsumnjicenData, dokaz: DokazData, osumnjicen: OsumnjicenData){
        val query = """
            INSERT INTO dokazOsumnjicen (dokazId, osumnjicenId)
            VALUES (?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, dokaz.idDokaz)
            statement.setInt(2, osumnjicen.idOsumnjicen)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                dokazOsumnjicen.idDokazOsumnjicen = resultSet.getInt(1)
                dokazOsumnjicen.osumnjicenId=osumnjicen
                dokazOsumnjicen.dokazId=dokaz
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
        INSERT INTO korisnik (korisnickoIme, ime, prezime, sifra, email, nacinPrijave, idToken, poeni, poslednjaAktivnost)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """
        // var conn: Connection? = null
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            //conn = getDatabaseConnection()
            statement = conn?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

            statement?.setString(1, korisnik.korisnickoIme)
            statement?.setString(2, korisnik.ime)
            statement?.setString(3, korisnik.prezime)
            statement?.setString(4, korisnik.sifra)
            statement?.setString(5, korisnik.email)
            statement?.setString(6, korisnik.nacinPrijave)
            statement?.setString(7, korisnik.idToken)
            statement?.setInt(8, 0)
            statement?.setDate(9, Date(System.currentTimeMillis()))

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
        // var conn: Connection? = null
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        println("Statement pripremljen: ${statement != null}")

        try {
            //conn = getDatabaseConnection()  // Assuming this method returns a valid DB connection
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
            //conn = getDatabaseConnection()
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        println("Statement pripremljen: ${statement != null}")

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, zlocin.idZlocin)
            statement.setString(2, oneContactData.ime)
            statement.setString(3, oneContactData.broj)
            statement.setInt(4,  oneContactData.slika)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, zlocin.idZlocin)
            statement.setString(2, beleskaData.tekst)
            statement.setTimestamp(3, Timestamp(beleskaData.datum))
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, zlocin.idZlocin)
            statement.setString(2, whatsAppKontaktDataData.ime)
            statement.setString(3, whatsAppKontaktDataData.broj)
            if (whatsAppKontaktDataData.slika != null) {
                statement.setInt(4, whatsAppKontaktDataData.slika)
            }
            else {
                statement.setNull(4, Types.INTEGER)  // Correctly set null for slika
            }
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, kontaktKoSalje.idWhatsAppKontakt)
            statement.setInt(2, kontaktKomeSalje.idWhatsAppKontakt)
            statement.setString(3, whatsAppPorukaData.tekst)
            statement.setTimestamp(4, java.sql.Timestamp(whatsAppPorukaData.datum))
            statement.setBoolean(5, whatsAppPorukaData.procitana)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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

    fun insertOneCallData(oneCallData: OneCallData) {
        val query = """
            INSERT INTO onecall (kontakt, datum, propusten, dolazni, zrtvaId)
            VALUES (?, ?, ?, ?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, oneCallData.kontakt)
            statement.setTimestamp(2, Timestamp(oneCallData.datum))
            statement.setBoolean(3, oneCallData.propusten)
            statement.setBoolean(4, oneCallData.dolazni)
            statement.setInt(5, oneCallData.zrtvaId)

            statement.executeUpdate()
            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, zlocin.idZlocin)
            if (galleryData.slika != null) {
                statement.setInt(2, galleryData.slika)
            }
            else {
                statement.setNull(2, Types.INTEGER)  // Correctly set null for slika
            }
            statement.setTimestamp(3, Timestamp(galleryData.datum))
            statement.setString(4, galleryData.mesto)

            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, kontaktKoSalje.idOneContact)
            statement.setInt(2, kontaktKomeSalje.idOneContact)
            statement.setString(3, obicnaPorukaData.tekst)
            statement.setTimestamp(4, Timestamp(obicnaPorukaData.datum))
            statement.setBoolean(5, obicnaPorukaData.procitana)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, osumnjicenData.idOsumnjicen)
            statement.setInt(2, zrtvaData.idZrtva)
            statement.setString(3, odnosOsumnjicenZrtvaData.tipOdnosa)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, prijavljeniKorisnikData.korisnickoIme)
            statement.setString(2, prijavljeniKorisnikData.sifra)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, zlocin.idZlocin)
            statement.setString(2, pitanjeData.tekst)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, pitanje.idPitanje)
            statement.setString(2, odgovorData.tekstOdgovora)
            statement.setBoolean(3, odgovorData.tacan)
            statement.setInt(4, odgovorData.bodovi)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, pitanjeIspitivanjeOsumnjicenogData.kategorija)
            statement.setString(2, pitanjeIspitivanjeOsumnjicenogData.tekst)
            statement.setString(3, pitanjeIspitivanjeOsumnjicenogData.odgovor)
            statement.setString(4, pitanjeIspitivanjeOsumnjicenogData.komentar)
            statement.setInt(5, osumnjicen.idOsumnjicen)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }
            statement.setString(1, pitanjeIspitivanjeSvedokaData.tekst)
            statement.setString(2, pitanjeIspitivanjeSvedokaData.odgovor)
            statement.setInt(3, svedok.idSvedok)
            statement.setInt(4, pitanjeIspitivanjeSvedokaData.nextPitanje)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                pitanjeIspitivanjeSvedokaData.idPitanjeIspitivanjeSvedoka = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }

    fun getPitanjeIspitivanjeSvedokaListData(): List<PitanjeIspitivanjeSvedokaData> {
        val query = "SELECT * FROM pitanjeispitivanjesvedoka"
        val list = mutableListOf<PitanjeIspitivanjeSvedokaData>()

        // var conn: Connection? = null
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            //conn = getDatabaseConnection()
            statement = conn?.prepareStatement(query)
            resultSet = statement?.executeQuery()

            while (resultSet?.next() == true) {
                val ispitivanje = PitanjeIspitivanjeSvedokaData(
                    idPitanjeIspitivanjeSvedoka = resultSet.getInt("idPitanjeIspitivanjeSvedoka"),
                    tekst = resultSet.getString("tekst"),
                    odgovor = resultSet.getString("odgovor"),
                    svedokId = resultSet.getInt("svedokId"),
                    nextPitanje = resultSet.getInt("nextPitanje")
                )
                list.add(ispitivanje)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, resultSet)
        }

        return list
    }


    fun updatePitanjeIspitivanjeSvedokaListData(list: List<PitanjeIspitivanjeSvedokaData>, svedok: SvedokData) {
        val query = """
        UPDATE pitanjeispitivanjesvedoka
        SET nextPitanje = ?
        WHERE idPitanjeIspitivanjeSvedoka = ? AND svedokId = ?
    """

        // var conn: Connection? = null
        var statement: PreparedStatement? = null

        try {
            //conn = getDatabaseConnection()
            statement = conn?.prepareStatement(query)

            for (i in 0 until list.size - 1) {
                val currentPitanje = list[i]
                val nextPitanje = list[i + 1]

                statement?.setInt(1, nextPitanje.idPitanjeIspitivanjeSvedoka)
                statement?.setInt(2, currentPitanje.idPitanjeIspitivanjeSvedoka)
                statement?.setInt(3, svedok.idSvedok)
                statement?.addBatch()
            }

            statement?.executeBatch()
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, zadatakData.tekst)
            statement.setString(2, zadatakData.korak)
            statement.setBoolean(3, zadatakData.uradjen)
            statement.setNull(4,  Types.INTEGER)
            statement.setInt(5, zlocin.idZlocin)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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

        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query)
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

    fun updateZadatakListData(zadatakList: List<ZadatakData>, zlocin: ZlocinData) {
        val query = """
            UPDATE zadatak
            SET nextZadatak = ?
            WHERE idZadatak = ? AND zlocinId = ?
        """
        var statement: PreparedStatement? = null

        try {
            statement = conn.prepareStatement(query)

            for (i in 0 until zadatakList.size - 1) {
                val currentZadatak = zadatakList[i]
                val nextZadatak = zadatakList[i + 1]

                statement?.setInt(1, nextZadatak.idZadatak)
                statement?.setInt(2, currentZadatak.idZadatak)
                statement?.setInt(3, zlocin.idZlocin)
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

        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, dokazZadatakData.tekst)
            statement.setInt(2, dokazData.idDokaz)
            statement.setBoolean(3, dokazZadatakData.uradjen)
            statement.setInt(4, zadatakData.idZadatak)

            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }
            statement.setInt(1, osumnjicenData.idOsumnjicen)
            statement.setInt(2, zadatakData.idZadatak)
            statement.setBoolean(3, ispitivanjeOsumnjicenogZadatakData.uradjen)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, svedokData.idSvedok)
            statement.setInt(2, zadatakData.idZadatak)
            statement.setBoolean(3, false)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, telefonData.idTelefon)
            statement.setInt(2, zadatakData.idZadatak)
            statement.setBoolean(3, telefonZadatakData.uradjen)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, forenzickiDokazZadatakData.tekst)
            statement.setInt(2, forenzickiDokazData.idForenzickiDokaz)
            statement.setBoolean(3, forenzickiDokazZadatakData.uradjen)
            statement.setInt(4, zadatakData.idZadatak)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
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



    //mysterious symptoms

    fun insertPacijentData(pacijent: PacijentData) {
        if (pacijent.statusPacijenta!="ziva" || pacijent.statusPacijenta!="mrtva"){
            pacijent.statusPacijenta="ziva"
        }
        val query = """
            INSERT INTO pacijent (simptomi, statusPacijenta, datumPrijave, prijavio, zlocinId,zrtvaId)
            VALUES (?, ?, ?, ?, ?,?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, pacijent.simptomi)
            statement.setString(2, pacijent.statusPacijenta)
            statement.setTimestamp(3, Timestamp(pacijent.datumPrijave))
            statement.setInt(4, pacijent.prijavio.idOsoba)
            statement.setInt(5, pacijent.zlocinId.idZlocin)
            statement.setInt(6, pacijent.zrtvaId.idZrtva)

            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                pacijent.idPacijent = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }


    fun insertMedicinskiIzvestajData(medicinskiIzvestaj: MedicinskiIzvestajData) {
        val query = """
            INSERT INTO medicinskiizvestaj (rezime, CTnalaz, MRInalaz, krvnaSlika, toksikoloskeAnalize, zakljucak,pacijentId )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, medicinskiIzvestaj.rezime)
            statement.setString(2, medicinskiIzvestaj.CTnalaz)
            statement.setString(3, medicinskiIzvestaj.MRInalaz)
            statement.setString(4, medicinskiIzvestaj.krvnaSlika)
            statement.setString(5, medicinskiIzvestaj.toksikoloskeAnalize)
            statement.setString(6, medicinskiIzvestaj.zakljucak)
            statement.setInt(7, medicinskiIzvestaj.pacijentId.idPacijent)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                medicinskiIzvestaj.idMedicinskiIzvestaj = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }


    fun insertLekarskiTestData(lekarskiTest: LekarskiTestData) {
        val query = """
           INSERT INTO lekarskitest (pacijentId,izjava)
           VALUES (?,?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setInt(1, lekarskiTest.pacijentId.idPacijent)
            statement.setString(2, lekarskiTest.izvestaj)
            statement.executeUpdate()
            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                lekarskiTest.idLekarskiTest = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }


    fun insertLokacijeIstrageData(lokacijeIstrage: LokacijeIstrageData) {
        val query = """
            INSERT INTO lokacijeistrage (mesto, naziv, opis, zlocinId, geoTackaALatitude, geoTackaALongitude)
            VALUES (?, ?, ?, ?, ?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, lokacijeIstrage.mesto)
            statement.setString(2, lokacijeIstrage.naziv)
            statement.setString(3, lokacijeIstrage.opis)
            statement.setInt(4, lokacijeIstrage.zlocinId)
            statement.setDouble(5, lokacijeIstrage.geoTackaALatitude)
            statement.setDouble(6, lokacijeIstrage.geoTackaALongitude)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                lokacijeIstrage.idLokacijeIstrage = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }

    fun insertIzjavaZaPacijentaData(izjavaZaPacijenta: IzjavaZaPacijentaData, pacijentData: PacijentData, osobaData: OsobaData) {
        val query = """
            INSERT INTO izjavazapacijenta (izjava, pacijentId, osobaId)
            VALUES (?, ?, ?)
        """
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            if (statement == null) {
                println("Prepare statement failed: statement is null")
                return
            }

            statement.setString(1, izjavaZaPacijenta.izjava)
            statement.setInt(2, pacijentData.idPacijent)
            statement.setInt(3, osobaData.idOsoba)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                izjavaZaPacijenta.idIzjavaZaPacijenta = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }
}