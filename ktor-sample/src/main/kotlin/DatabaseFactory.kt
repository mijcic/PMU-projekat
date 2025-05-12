package com.example

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.time.LocalDate
import java.time.LocalDateTime

object DatabaseFactory {
    fun init() {
        try {
            Database.connect(
                url = "jdbc:mysql://localhost:3306/whodunit",
                driver = "com.mysql.cj.jdbc.Driver",
                user = "root",
                password = "mia123"
            )
            println("Uspesno povezan sa bazom.")
            println("uspesno izvrsena transakcija")
        } catch (e: Exception) {
            println("Greska prilikom inicijalizacije baze: ${e.message}")
        }
    }
}