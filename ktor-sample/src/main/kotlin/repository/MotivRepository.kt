package com.example.repository

import com.example.closeResources
import com.example.data.remote.tables.MotivData
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class MotivRepository(private val conn: Connection): MotivRepositoryInterface {

    override fun insertMotivData(motiv: MotivData){
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
}