package com.example.service

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

/**
 * Service class responsible for managing the database connection and executing SQL queries.
 *
 * @property dbUrl The JDBC URL of the database.
 * @property user The username used to connect to the database.
 * @property password The password used to connect to the database.
 */
class DatabaseService(private val dbUrl: String, private val user: String, private val password: String) {

    /**
     * Establishes and returns a connection to the database.
     *
     * @return A [Connection] instance connected to the database.
     * @throws java.sql.SQLException If a database access error occurs.
     */
    fun getDatabaseConnection(): Connection {
        return DriverManager.getConnection(
            dbUrl, user, password
        )
    }

    /**
     * Executes a SQL query and maps each result row using the provided [rowMapper] function.
     *
     * @param query The SQL query string to be executed.
     * @param rowMapper A lambda that maps a [ResultSet] row to a result object of type [T].
     * @return A list of objects of type [T] mapped from the result set.
     */
    fun <T> executeQuery(query: String, rowMapper: (ResultSet) -> T): List<T> {
        val results = mutableListOf<T>()
        getDatabaseConnection().use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        results.add(rowMapper(rs))
                    }
                }
            }
        }
        return results
    }

    /**
     * Checks whether the 'zlocin' table has any murder entries (tipZlocinaId = 1).
     *
     * @param connection An open [Connection] to the database.
     * @return `true` if no murder entries exist, otherwise `false`.
     */
    fun isMurderTableEmpty(connection: Connection): Boolean {
        println("isMurderTableEmpty")
        val query = "SELECT COUNT(*) FROM zlocin WHERE tipZlocinaId=1"
        connection.prepareStatement(query).use { statement ->
            statement.executeQuery().use { rs ->
                return if (rs.next()) rs.getInt(1) == 0 else true
            }
        }
    }

    /**
     * Checks whether the 'zlocin' table has any mysterious symptom entries (tipZlocinaId = 9).
     *
     * @param connection An open [Connection] to the database.
     * @return `true` if no such entries exist, otherwise `false`.
     */
    fun isMysteriousSymptomsTableEmpty(connection: Connection): Boolean {
        val query = "SELECT COUNT(*) FROM zlocin WHERE tipZlocinaId=9"
        connection.prepareStatement(query).use { statement ->
            statement.executeQuery().use { rs ->
                return if (rs.next()) rs.getInt(1) == 0 else true
            }
        }
    }
}