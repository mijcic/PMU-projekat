package com.example.repository

import com.example.closeResources
import com.example.data.remote.tables.DokazData
import com.example.data.remote.tables.DokazOsumnjicenData
import com.example.data.remote.tables.ForenzickiDokazData
import com.example.data.remote.tables.OsumnjicenData
import com.example.data.remote.tables.TragData
import com.example.data.remote.tables.ZlocinData
import com.example.data.remote.tables.ZrtvaData
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement


class DokazRepository(private val conn: Connection): DokazRepositoryInterface {
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

}