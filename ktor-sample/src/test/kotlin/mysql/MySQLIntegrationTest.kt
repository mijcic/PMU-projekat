package com.example.mysql

import com.example.models.dto.*
import com.example.repository.RepositoryInsert
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.MySQLContainer
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MySQLIntegrationTest {

    private val mysql = MySQLContainer("mysql:8.0").apply {
        withDatabaseName("testdb")
        withUsername("test")
        withPassword("test")
        start()
    }

    private lateinit var connection: Connection

    @BeforeAll
    fun setupDatabase() {
        connection = DriverManager.getConnection(mysql.jdbcUrl, mysql.username, mysql.password)
        connection.autoCommit = false
        connection.createStatement().use { stmt ->

            // Drop ako postoji
            stmt.execute("DROP TABLE IF EXISTS Zlocin")
            stmt.execute("DROP TABLE IF EXISTS TipZlocina")
            stmt.execute("DROP TABLE IF EXISTS Osoba")

            // Kreiranje tabele TipZlocina
            stmt.execute("""
                CREATE TABLE TipZlocina (
                    idTipZlocina INT AUTO_INCREMENT PRIMARY KEY,
                    naziv VARCHAR(255) NOT NULL
                );
            """.trimIndent())

            stmt.execute("""
                INSERT INTO TipZlocina (naziv) VALUES
                ('murder'), ('disappearance'), ('robbery'), ('kidnappingAndBlackmail'),
                ('FamilySecrets'), ('Abuse'), ('GangConflicts'), ('Corruption'),
                ('MysteriousSymptoms'), ('MafiaCrimesOfPassion'), ('FalseIdentities'), ('CultsAndSecrets');
            """.trimIndent())


            stmt.execute("""
                CREATE TABLE zlocin (
                    idZlocin INT AUTO_INCREMENT PRIMARY KEY,
                    tipZlocinaId INT NOT NULL,
                    naziv VARCHAR(255) NOT NULL,
                    datum DATETIME DEFAULT CURRENT_TIMESTAMP,
                    mesto VARCHAR(255) NOT NULL,
                    opis TEXT NOT NULL,
                    statusS ENUM('u_istrazi', 'resen') NOT NULL,
                    FOREIGN KEY (tipZlocinaId) REFERENCES TipZlocina(idTipZlocina)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE Osoba (
                    idOsoba INT AUTO_INCREMENT PRIMARY KEY,  -- Primarni ključ
                    ime VARCHAR(50) NOT NULL,          
                    kontakt VARCHAR(255) NOT NULL,               
                    datum DATETIME DEFAULT CURRENT_TIMESTAMP,
                    zanimanje VARCHAR(100) NOT NULL,
                    pol VARCHAR(50) NOT NULL,       
                    zlocinId INT NOT NULL,                   -- Spoljašnji ključ na Zlocin
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin)  -- Veza sa Zlocin tabelom
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE Zrtva (
                    idZrtva INT AUTO_INCREMENT PRIMARY KEY,  -- Primarni ključ
                    tipZrtve VARCHAR(50) NOT NULL,           -- Tip žrtve (osoba, objekat, fenomen...)
                    detalji VARCHAR(150) NOT NULL,            -- Detalji o žrtvi
                    statusZrtva VARCHAR(50) NOT NULL,        -- Status žrtve
                    zlocinId INT NOT NULL,                   -- Spoljašnji ključ na Zlocin
                    osobaId INT NOT NULL,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin),  -- Veza sa Zlocin tabelom
                    FOREIGN KEY (osobaId) REFERENCES Osoba(idOsoba) 
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE Motiv (
                    idMotiv INT AUTO_INCREMENT PRIMARY KEY,
                    opis TEXT NOT NULL 
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE osumnjicen (
                    idOsumnjicen INT AUTO_INCREMENT PRIMARY KEY,
                    statusS INT NOT NULL,
                    tipOsumnjicen ENUM('pojedinac', 'organizacija') NOT NULL, 
                    motiv INT NOT NULL,
                    zlocinId INT NOT NULL,
                    kriv INT NOT NULL,
                    osobaId INT NOT NULL,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin),
                    FOREIGN KEY (osobaId) REFERENCES Osoba(idOsoba),
                    FOREIGN KEY (motiv) REFERENCES Motiv(idMotiv)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE dokaz (
                    idDokaz INT AUTO_INCREMENT PRIMARY KEY,
                    tipDokaza ENUM('fizicki', 'digitalni', 'svedok') NOT NULL,
                    opis TEXT NOT NULL,
                    zlocinId INT NOT NULL,
                    zrtvaId INT NOT NULL,
                    statusS INT NOT NULL,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin),
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE svedok (
                    idSvedok INT AUTO_INCREMENT PRIMARY KEY,
                    izjava VARCHAR(255) NOT NULL,
                    statusSvedok ENUM('aktivno', 'zasticen', 'nesaradnja') NOT NULL,
                    statusIspitan INT NOT NULL,
                    zlocinId INT NOT NULL,
                    osobaId INT NOT NULL,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin),
                    FOREIGN KEY (osobaId) REFERENCES Osoba(idOsoba)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE obdukcija (
                    idObdukcija INT AUTO_INCREMENT PRIMARY KEY,
                    izvestaj TEXT NOT NULL,
                    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    uzrokSmrti VARCHAR(255) NOT NULL,
                    zrtvaId INT NOT NULL,
                    informacije VARCHAR(255) NOT NULL,
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE forenzickiDokaz (
                    idForenzickiDokaz INT AUTO_INCREMENT PRIMARY KEY,
                    tipForenzickiDokaz ENUM('otisak', 'DNK', 'dokument') NOT NULL,
                    opis TEXT NOT NULL,
                    statusS INT NOT NULL,
                    zrtvaId INT NOT NULL,
                    veza VARCHAR(255) NOT NULL,
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
                );
            """.trimIndent())

            println("Tables created successfully.")
        }

        connection.commit()
    }


    @Test
    fun testInsertZlocinData() {
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u vozu",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mladog coveka",
            status = "u_istrazi", // validna ENUM vrednost
            idZlocin = 0
        )

        repo.insertZlocinData(zlocin)

        val stmt = connection.prepareStatement("SELECT * FROM zlocin WHERE naziv=?")
        stmt.setString(1, zlocin.naziv)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji red sa nazivom 'Ubistvo u vozu'")
        assertEquals("Ubistvo u vozu", rs.getString("naziv"))
        assertEquals("Pariz", rs.getString("mesto"))
        assertEquals("Ubistvo mladog coveka", rs.getString("opis"))
        assertEquals("u_istrazi", rs.getString("statusS"))
        val storedTimestamp = rs.getTimestamp("datum").time
        assertTrue(abs(storedTimestamp - d) < 1000, "Datum u bazi i očekivani datum nisu dovoljno blizu")
    }

    @Test
    fun testInsertOsobaData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u kuci",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mladog coveka",
            status = "u_istrazi", // validna ENUM vrednost
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)
        println(zlocin.idZlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba= OsobaData(
            idOsoba = 1,
            ime = "Amelia Black",
            kontakt = "+447777888999",
            datum = timestamp2,
            zanimanje = "advokatica",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )

        repo.insertOsobaData(osoba,zlocin)

        val stmt = connection.prepareStatement("SELECT * FROM Osoba WHERE zlocinId=?")
        stmt.setInt(1, osoba.zlocinId)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji osoba sa prosledjenim zlocinId-om")
        assertEquals("Amelia Black", rs.getString("ime"))
        assertEquals("+447777888999", rs.getString("kontakt"))
        assertEquals("advokatica", rs.getString("zanimanje"))
        assertEquals("zenski", rs.getString("pol"))
        val storedTimestamp = rs.getTimestamp("datum").time
        assertTrue(abs(storedTimestamp - timestamp2) < 1000, "Datum u bazi i očekivani datum nisu dovoljno blizu")
    }

    @Test
    fun testInsertZrtvaData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u trznom centru",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mlade advokatice",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba= OsobaData(
            idOsoba = 1,
            ime = "Amelia Black",
            kontakt = "+447777888999",
            datum = timestamp2,
            zanimanje = "advokatica",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )

        repo.insertOsobaData(osoba,zlocin)

        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zr,zlocin,osoba)
        val stmt = connection.prepareStatement("SELECT * FROM Zrtva WHERE zlocinId=?")
        stmt.setInt(1, zr.zlocinId)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji zrtva sa prosledjenim zlocinId-om")
        assertEquals("zena", rs.getString("tipZrtve"))
        assertEquals("Korumpirana advokatica pronadjena mrtva u vozu.", rs.getString("detalji"))
        assertEquals("mrtav", rs.getString("statusZrtva"))
        assertEquals(osoba.idOsoba, rs.getInt("osobaId"))
        assertEquals(zlocin.idZlocin, rs.getInt("zlocinId"))
    }

    @Test
    fun testInsertMotivData(){
        val repo = RepositoryInsert(connection)

        val m = MotivData(
            idMotiv = 1,
            opis = "Ljubomora"
        )

        repo.insertMotivData(m)

        val stmt = connection.prepareStatement("SELECT * FROM Motiv WHERE opis=? and idMotiv=?")
        stmt.setString(1, m.opis)
        stmt.setInt(2, m.idMotiv)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji motiv")
        assertEquals(m.idMotiv, rs.getInt("idMotiv"))
        assertEquals("Ljubomora", rs.getString("opis"))
    }

    @Test
    fun testInsertOsumnjicenData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()
        
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u podzemnom",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mladica",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba= OsobaData(
            idOsoba = 1,
            ime = "Amelia Black",
            kontakt = "+447777888999",
            datum = timestamp2,
            zanimanje = "advokatica",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )

        val osoba2= OsobaData(
            idOsoba = 2,
            ime = "Tomas Black",
            kontakt = "+4433337888999",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )

        repo.insertOsobaData(osoba,zlocin)
        repo.insertOsobaData(osoba2,zlocin)

        val m = MotivData(
            idMotiv = 1,
            opis = "Ljubomora"
        )

        repo.insertMotivData(m)

        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = m,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba2
        )

        repo.insertOsumnjicenData(osumnjicen,zlocin,m,zr)
        val stmt = connection.prepareStatement("SELECT * FROM osumnjicen WHERE zlocinId=?")
        stmt.setInt(1, osumnjicen.zlocinId)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji osumnjicen sa prosledjenim zlocinId-om")
        assertEquals(osumnjicen.idOsumnjicen, rs.getInt("idOsumnjicen"))
        assertEquals(0, rs.getInt("statusS"))
        assertEquals("pojedinac", rs.getString("tipOsumnjicen"))
        assertEquals(m.idMotiv, rs.getInt("motiv"))
        assertEquals(zlocin.idZlocin, rs.getInt("zlocinId"))
        assertEquals(osoba2.idOsoba, rs.getInt("osobaId"))
    }
    
    @Test
    fun testInsertDokazData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u autobusu",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mladica",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba= OsobaData(
            idOsoba = 1,
            ime = "Amelia Black",
            kontakt = "+447777888999",
            datum = timestamp2,
            zanimanje = "advokatica",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )

        repo.insertOsobaData(osoba,zlocin)


        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        val dokaz = DokazData(
            idDokaz = 1,
            tipDokaza = "fizicki",
            opis = "Pistolj pronadjen na mestu zlocina.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zr.idZrtva,
            status = 0
        )

        repo.insertDokazData(dokaz,zlocin,zr)
        val stmt = connection.prepareStatement("SELECT * FROM dokaz WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji dokaz sa prosledjenim zlocinId-om")
        assertEquals(dokaz.idDokaz, rs.getInt("idDokaz"))
        assertEquals("fizicki", rs.getString("tipDokaza"))
        assertEquals("Pistolj pronadjen na mestu zlocina.", rs.getString("opis"))
        assertEquals(zr.idZrtva, rs.getInt("zrtvaId"))
        assertEquals(zlocin.idZlocin, rs.getInt("zlocinId"))
        assertEquals(0, rs.getInt("statusS"))
    }

    @Test
    fun testInsertSvedokData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u stanu",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mladica",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba= OsobaData(
            idOsoba = 1,
            ime = "Amelia Black",
            kontakt = "+447777888999",
            datum = timestamp2,
            zanimanje = "advokatica",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )

        repo.insertOsobaData(osoba,zlocin)


        val svedok = SvedokData(
            idSvedok = 1,
            izjava = "Cula sam pucanj i videla zenu kako bezi.",
            statusSvedok = "aktivno",
            statusIspitan = 0,
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertSvedokData(svedok,zlocin)
        val stmt = connection.prepareStatement("SELECT * FROM svedok WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji svedok sa prosledjenim zlocinId-om")
        assertEquals(svedok.idSvedok, rs.getInt("idSvedok"))
        assertEquals("Cula sam pucanj i videla zenu kako bezi.", rs.getString("izjava"))
        assertEquals("aktivno", rs.getString("statusSvedok"))
        assertEquals(0, rs.getInt("statusIspitan"))
        assertEquals(zlocin.idZlocin, rs.getInt("zlocinId"))
        assertEquals(osoba.idOsoba, rs.getInt("osobaId"))
    }

    @Test
    fun testInsertObdukcijaData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u tramvaju",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo zene",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba= OsobaData(
            idOsoba = 1,
            ime = "Amelia Black",
            kontakt = "+447777888999",
            datum = timestamp2,
            zanimanje = "advokatica",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )

        repo.insertOsobaData(osoba,zlocin)

        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zr,zlocin,osoba)
        val d2 = System.currentTimeMillis()

        val obdukcija = ObdukcijaData(
            idObdukcija = 1,
            izvestaj = "Zrtva je preminula od rane od metka u grudima. Nema znakova borbe.",
            datum = d2,
            uzrokSmrti = "Rana od metka u grudima",
            zrtvaId =zr.idZrtva,
            informacije = "Nema znakova seksualnog napada."
        )

        repo.insertObdukcijaData(obdukcija,zr)
        val stmt = connection.prepareStatement("SELECT * FROM obdukcija WHERE zrtvaId=?")
        stmt.setInt(1, zr.idZrtva)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji obdukcija sa prosledjenim zrtvaId-om")
        assertEquals(obdukcija.idObdukcija, rs.getInt("idObdukcija"))
        assertEquals("Zrtva je preminula od rane od metka u grudima. Nema znakova borbe.", rs.getString("izvestaj"))
        assertEquals(zr.idZrtva, rs.getInt("zrtvaId"))
        assertEquals("Rana od metka u grudima", rs.getString("uzrokSmrti"))
        assertEquals("Nema znakova seksualnog napada.", rs.getString("informacije"))
        val storedTimestamp = rs.getTimestamp("datum").time
        assertTrue(abs(storedTimestamp - obdukcija.datum) < 1000)
    }


    @Test
    fun testInsertForenzickiDokazData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u tunelu",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mladica",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba= OsobaData(
            idOsoba = 1,
            ime = "Amelia Black",
            kontakt = "+447777888999",
            datum = timestamp2,
            zanimanje = "advokatica",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )

        repo.insertOsobaData(osoba,zlocin)

        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        val dokaz = ForenzickiDokazData(
            idForenzickiDokaz = 1,
            tipForenzickiDokaz = "DNK",
            opis = "DNK tragovi pronađeni na pištolju.",
            statusS = 0,
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
        )

        repo.insertForenzickiDokaz(dokaz,zr)
        val stmt = connection.prepareStatement("SELECT * FROM forenzickiDokaz WHERE zrtvaId=?")
        stmt.setInt(1, zr.idZrtva)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji dokaz sa prosledjenim zlocinId-om")
        assertEquals(dokaz.idForenzickiDokaz, rs.getInt("idForenzickiDokaz"))
        assertEquals("DNK", rs.getString("tipForenzickiDokaz"))
        assertEquals("DNK tragovi pronađeni na pištolju.", rs.getString("opis"))
        assertEquals(0, rs.getInt("statusS"))
        assertEquals("DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.", rs.getString("veza"))
    }
}