package com.example.repository

import com.example.closeResources
import com.example.data.remote.tables.AlibiData
import com.example.data.remote.tables.ObdukcijaData
import com.example.data.remote.tables.OsumnjicenData
import com.example.data.remote.tables.SvedokData
import com.example.data.remote.tables.ZlocinData
import com.example.data.remote.tables.ZrtvaData
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.Timestamp

class AlibiRepository(private val conn: Connection): AlibiRepositoryInterface {
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

}