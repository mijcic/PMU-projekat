package com.example.repository

import com.example.closeResources
import com.example.data.remote.tables.MotivData
import com.example.data.remote.tables.OdnosOsumnjicenZrtvaData
import com.example.data.remote.tables.OsobaData
import com.example.data.remote.tables.OsumnjicenData
import com.example.data.remote.tables.PrijavljeniKorisnikData
import com.example.data.remote.tables.SvedokData
import com.example.data.remote.tables.ZlocinData
import com.example.data.remote.tables.ZrtvaData
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class OsobaRepository(private val conn: Connection): OsobaRepositoryInterface {

    //insertOsobaData
    override fun insertOsobaData(osobaData: OsobaData, zlocin: ZlocinData){
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

    override fun insertZrtva(zrtvaData: ZrtvaData, zlocin: ZlocinData, osoba: OsobaData){
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

    override fun insertOsumnjicenData(osumnjicen: OsumnjicenData, zlocin: ZlocinData, motiv: MotivData) {
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

    override fun insertSvedokData(svedok: SvedokData, zlocin: ZlocinData){
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

}