package com.example.repository

import com.example.closeResources
import com.example.data.remote.tables.UsedZlocinData
import com.example.data.remote.tables.ZlocinData
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class ZlocinRepository(private val conn: Connection): ZlocinRepositoryInterface {

    //insert into UsedZlocin Table in mysql
    override fun insertUsedZlocinData(usedZlocin: UsedZlocinData) {
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

            statement.setInt(1, usedZlocin.zlocinId.idZlocin)
            statement.setBoolean(2, usedZlocin.used)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                // Vraca generisani ID
                usedZlocin.idUsedZlocin = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }

    // insert into Zlocin Table in mySql
    override fun insertZlocinData(zlocin: ZlocinData) {
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

            statement.setInt(1, zlocin.tipZlocinaId)
            statement.setString(2, zlocin.naziv)
            statement.setTimestamp(3, java.sql.Timestamp(zlocin.datum))
            statement.setString(4, zlocin.mesto)
            statement.setString(5, zlocin.opis)
            statement.setString(6, zlocin.status)
            statement.executeUpdate()

            resultSet = statement.generatedKeys
            if (resultSet?.next() == true) {
                // Vraca generisani ID
                zlocin.idZlocin = resultSet.getInt(1)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeResources(conn, statement, null)
        }
    }
}