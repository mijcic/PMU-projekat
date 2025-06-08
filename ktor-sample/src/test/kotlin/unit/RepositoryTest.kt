package com.example.unit

import com.example.closeResources
import com.example.models.dto.UsedZlocinData
import com.example.models.dto.ZlocinData
import com.example.repository.Repository
import com.example.repository.RepositoryInsert
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.sql.*
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class RepositoryTest {
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var resultSet: ResultSet
    private lateinit var repository: Repository

    private fun returnZlocinData(): ZlocinData {
        return ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u tramvaju",
            datum = System.currentTimeMillis(),
            mesto = "Pariz",
            opis = "Ubistvo zene",
            status = "u_istrazi",
            idZlocin = 0
        )
    }

    private fun returnTimeStamp():Long{
        val datumStr = "2024-11-11"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it, formatter2) }
        return dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    @BeforeEach
    fun setup() {
        connection = mockk()
        preparedStatement = mockk()
        resultSet = mockk(relaxed = true)
        every { resultSet.close() } just Runs
        repository = Repository(connection)
    }

    @AfterEach
    fun teardown() {
        clearMocks(connection, preparedStatement, resultSet)
        unmockkStatic("com.example.ApplicationKt") // pravi package gde je closeResources
    }

    //UsedZlocinData

    @Test
    fun `should return null when resultSet is null in getUsedZlocinMurder`() {
        val statement = mockk<Statement>()
        every { connection.createStatement() } returns statement
        every { statement.executeQuery(any()) } returns null

        repository = Repository(connection)

        val result = repository.getUsedZlocinMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when resultSet is empty in getUsedZlocinMurder`() {
        val statement = mockk<Statement>()
        every { connection.createStatement() } returns statement
        every { statement.executeQuery(any()) } returns resultSet
        every { resultSet.next() } returns false

        repository = Repository(connection)

        val result = repository.getUsedZlocinMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when zlocinId is SQL NULL in getUsedZlocinMurder`() {
        val statement = mockk<Statement>()
        every { connection.createStatement() } returns statement
        every { statement.executeQuery(any()) } returns resultSet
        every { resultSet.next() } returns true
        every { resultSet.getInt("zlocinId") } returns 0
        every { resultSet.wasNull() } returns true

        repository = Repository(connection)

        val result = repository.getUsedZlocinMurder()

        assertNull(result)
    }

    //ZlocinData

    @Test
    fun `should return null when resultSet is null in getZlocin`() {
        val statement = mockk<Statement>()
        every { connection.createStatement() } returns statement
        every { statement.executeQuery(any()) } returns null

        repository = Repository(connection)

        val result = repository.getZlocin(1)

        assertNull(result)
    }

    //TipZlocinaData

    @Test
    fun `should return null when resultSet is null in getTipZlocina`() {
        val statement = mockk<Statement>()
        every { connection.createStatement() } returns statement
        every { statement.executeQuery(any()) } returns null

        repository = Repository(connection)

        val result = repository.getTipZlocina(1)

        assertNull(result)
    }

    @Test
    fun `should return null when resultSet is empty in getTipZlocina`() {
        val statement = mockk<Statement>()
        every { connection.createStatement() } returns statement
        every { statement.executeQuery(any()) } returns resultSet
        every { resultSet.next() } returns false

        repository = Repository(connection)

        val result =  repository.getTipZlocina(1)

        assertNull(result)
    }

    //
}
