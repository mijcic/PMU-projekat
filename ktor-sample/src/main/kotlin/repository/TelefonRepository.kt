package com.example.repository

import com.example.closeResources
import com.example.data.remote.tables.AplikacijaData
import com.example.data.remote.tables.BeleskaData
import com.example.data.remote.tables.GalerijaData
import com.example.data.remote.tables.GalleryData
import com.example.data.remote.tables.KontaktData
import com.example.data.remote.tables.ObicnaPorukaData
import com.example.data.remote.tables.OneCallData
import com.example.data.remote.tables.OneContactData
import com.example.data.remote.tables.PorukeData
import com.example.data.remote.tables.PoziviData
import com.example.data.remote.tables.TelefonData
import com.example.data.remote.tables.WhatsAppKontaktData
import com.example.data.remote.tables.WhatsAppPorukaData
import com.example.data.remote.tables.ZlocinData
import com.example.data.remote.tables.ZrtvaData
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.Timestamp
import java.sql.Types

class TelefonRepository(private val conn: Connection): TelefonRepositoryInterface {
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

}