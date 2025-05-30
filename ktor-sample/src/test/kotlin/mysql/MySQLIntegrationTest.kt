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

            stmt.execute("""
                CREATE TABLE whatsappkontakt (
                    idWhatsAppKontakt INT AUTO_INCREMENT PRIMARY KEY,
                    zlocinId INT NOT NULL,
                    ime VARCHAR(100) NOT NULL,
                    broj VARCHAR(100) NOT NULL,
                    slika INT,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE whatsappporuka (
                	idWhatsAppPoruka INT AUTO_INCREMENT PRIMARY KEY,
                    kontaktKoSalje INT NOT NULL,
                    kontaktKomeSalje INT NOT NULL,
                    tekst VARCHAR(1000) NOT NULL,
                    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    procitana TINYINT(0),
                    FOREIGN KEY (kontaktKoSalje) REFERENCES whatsappkontakt(idWhatsAppKontakt),
                    FOREIGN KEY (kontaktKomeSalje) REFERENCES whatsappkontakt(idWhatsAppKontakt)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE telefon (
                    idTelefon INT AUTO_INCREMENT PRIMARY KEY,
                    model VARCHAR(50) NOT NULL,
                    os ENUM('IOS', 'Android') NOT NULL,
                    zrtvaId INT NOT NULL,
                    sifra VARCHAR(100) NOT NULL,
                    informacije VARCHAR(255) NOT NULL,
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE kontakt (
                    idKontakt INT AUTO_INCREMENT PRIMARY KEY,
                    ime varchar(255) NOT NULL,
                    broj varchar(50) NOT NULL,
                    statusS INT NOT NULL,
                    zrtvaId INT NOT NULL,
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
                );
            """.trimIndent())


            stmt.execute("""
                CREATE TABLE poruke (
                    idPoruke INT AUTO_INCREMENT PRIMARY KEY,
                    tipPoruke ENUM('SMS', 'WhatsApp', 'email') NOT NULL,
                    sadrzaj varchar(255) NOT NULL,
                    datumVreme DATETIME DEFAULT CURRENT_TIMESTAMP,
                    zrtvaId INT NOT NULL,
                    posiljalacId INT NOT NULL,
                    statusPoruke ENUM('sent', 'read', 'delete') NOT NULL,
                    sifrovana boolean NOT NULL,
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva),
                    FOREIGN KEY (posiljalacId) REFERENCES kontakt(idKontakt)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE pozivi (
                    idPoziv INT AUTO_INCREMENT PRIMARY KEY,
                    tip INT NOT NULL,
                    broj varchar(100) NOT NULL,
                    datumVreme DATETIME DEFAULT CURRENT_TIMESTAMP,
                    zrtvaId INT NOT NULL,
                    statusS INT NOT NULL,
                    kontaktId INT NOT NULL,
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva),
                    FOREIGN KEY (kontaktId) REFERENCES kontakt(idKontakt)
                );
            """.trimIndent())


            stmt.execute("""
                CREATE TABLE galerija (
                    idGalerija INT AUTO_INCREMENT PRIMARY KEY,
                    tip INT NOT NULL,
                    putanja varchar(100) NOT NULL,
                    zrtvaId INT NOT NULL,
                    datumVreme DATETIME DEFAULT CURRENT_TIMESTAMP,
                    lokacija varchar(100) NOT NULL,
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE aplikacija (
                    idAplikacije INT AUTO_INCREMENT PRIMARY KEY,
                    naziv varchar(100) NOT NULL,
                    tip INT NOT NULL,
                    zrtvaId INT NOT NULL,
                    aktivna boolean NOT NULL,
                    informacije varchar(100) NOT NULL,
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE trag (
                    idTrag INT AUTO_INCREMENT PRIMARY KEY,
                    forenzickiDokazId INT NOT NULL,
                    osumnjicenId INT NOT NULL,
                    FOREIGN KEY (forenzickiDokazId) REFERENCES forenzickiDokaz(idForenzickiDokaz),
                    FOREIGN KEY (osumnjicenId) REFERENCES osumnjicen(idOsumnjicen)
                );
            """.trimIndent())


            stmt.execute("""
                CREATE TABLE dokazOsumnjicen (
                    idDokazOsumnjicen INT AUTO_INCREMENT PRIMARY KEY,
                    dokazId INT NOT NULL,
                    osumnjicenId INT NOT NULL,
                    FOREIGN KEY (dokazId) REFERENCES dokaz(idDokaz),
                    FOREIGN KEY (osumnjicenId) REFERENCES osumnjicen(idOsumnjicen)
                );
            """.trimIndent())


            stmt.execute("""
                CREATE TABLE oneContact (
                    idOneContact INT AUTO_INCREMENT PRIMARY KEY,
                    zlocinId INT NOT NULL,
                    ime VARCHAR(100) NOT NULL,
                    broj VARCHAR(100) NOT NULL,
                    slika INT,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin)
                );
            """.trimIndent())


            stmt.execute("""
                CREATE TABLE beleska (
                    idBeleska INT AUTO_INCREMENT PRIMARY KEY,
                    zlocinId INT NOT NULL,
                    tekst VARCHAR(1000) NOT NULL,
                    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE whatsappkontakt (
                    idWhatsAppKontakt INT AUTO_INCREMENT PRIMARY KEY,
                    zlocinId INT NOT NULL,
                    ime VARCHAR(100) NOT NULL,
                    broj VARCHAR(100) NOT NULL,
                    slika INT,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE usedzlocin(
                    idUsedZlocin INT AUTO_INCREMENT PRIMARY KEY,
                    zlocinId INT NOT NULL,
                    used BOOLEAN NOT NULL,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin)
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

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = m,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba2
        )

        repo.insertOsumnjicenData(osumnjicen,zlocin,m)
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
        repo.insertZrtva(zr,zlocin,osoba)

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
        repo.insertZrtva(zr,zlocin,osoba)

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



    @Test
    fun testInserTelefonData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo na svadbi",
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
        repo.insertZrtva(zr,zlocin, osoba)

        val tel = TelefonData(
            idTelefon = 1,
            model = "Samsung Galaxy S22",
            os = "Android",
            sifra = "1234",
            informacije = "Pronađene su poruke sa pretnjama.",
            zrtvaId = zr.idZrtva
        )

        repo.insertTelefonData(tel,zr)
        val stmt = connection.prepareStatement("SELECT * FROM telefon WHERE zrtvaId=?")
        stmt.setInt(1, zr.idZrtva)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji telefon sa prosledjenim zrtvaId-om")
        assertEquals(tel.idTelefon, rs.getInt("idTelefon"))
        assertEquals(tel.zrtvaId, rs.getInt("zrtvaId"))
        assertEquals("Samsung Galaxy S22", rs.getString("model"))
        assertEquals("Android", rs.getString("os"))
        assertEquals("1234", rs.getString("sifra"))
        assertEquals("Pronađene su poruke sa pretnjama.", rs.getString("informacije"))
    }

    @Test
    fun testInsertKontaktData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val kontakt = KontaktData(
            idKontakt = 1,
            ime = "Elenora",
            broj = "+442123412312",
            status = 1,
            zrtvaId = zr
        )

        repo.insertKontaktData(kontakt,zr)
        val stmt = connection.prepareStatement("SELECT * FROM kontakt WHERE zrtvaId=?")
        stmt.setInt(1, zr.idZrtva)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji kontakt sa prosledjenim zrtvaId-om")
        assertEquals(kontakt.idKontakt, rs.getInt("idKontakt"))
        assertEquals("Elenora", rs.getString("ime"))
        assertEquals("+442123412312", rs.getString("broj"))
        assertEquals(1, rs.getInt("statusS"))
        assertEquals(zr.idZrtva, rs.getInt("zrtvaId"))
    }


    @Test
    fun testInsertPorukeData(){
        println("poruketest")
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val kontakt = KontaktData(
            idKontakt = 1,
            ime = "Alex",
            broj = "+472123412312",
            status = 1,
            zrtvaId = zr
        )
        repo.insertKontaktData(kontakt,zr)

        val poruka = PorukeData(
            idPoruke = 1,
            tipPoruke = "SMS",
            sadrzaj = ":(",
            datumVreme = d,
            zrtvaId = zr,
            posiljalacId = kontakt,
            statusPoruke = "sent",
            sifrovana = false
        )

        repo.insertPorukeData(poruka,zr,kontakt)
        val stmt = connection.prepareStatement("SELECT * FROM poruke WHERE zrtvaId=?")
        stmt.setInt(1, zr.idZrtva)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji kontakt sa prosledjenim zrtvaId-om")
        assertEquals(poruka.idPoruke, rs.getInt("idPoruke"))
        assertEquals("SMS", rs.getString("tipPoruke"))
        assertEquals(":(", rs.getString("sadrzaj"))
        assertEquals(kontakt.idKontakt, rs.getInt("posiljalacId"))
        assertEquals("sent", rs.getString("statusPoruke"))
        assertEquals(zr.idZrtva, rs.getInt("zrtvaId"))
        assertEquals(false, rs.getBoolean("sifrovana"))
        val storedTimestamp = rs.getTimestamp("datumVreme").time
        assertTrue(abs(storedTimestamp - d) < 1000)
    }

    @Test
    fun testInsertPoziviData(){
        println("pozivitest")
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u starom hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val kontakt = KontaktData(
            idKontakt = 1,
            ime = "Alex",
            broj = "+472123412312",
            status = 1,
            zrtvaId = zr
        )
        repo.insertKontaktData(kontakt,zr)

        val poziv = PoziviData(
            idPoziv = 1,
            tip = 0,
            broj = "+432635647547",
            datumVreme = d,
            zrtvaId = zr,
            status = 0,
            kontaktId = kontakt
        )

        repo.insertPoziviData(poziv, zr,kontakt)
        val stmt = connection.prepareStatement("SELECT * FROM pozivi WHERE zrtvaId=?")
        stmt.setInt(1, zr.idZrtva)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji poziv sa prosledjenim zrtvaId-om")
        assertEquals(poziv.idPoziv, rs.getInt("idPoziv"))
        assertEquals(0, rs.getInt("tip"))
        assertEquals("+432635647547", rs.getString("broj"))
        assertEquals(kontakt.idKontakt, rs.getInt("kontaktId"))
        assertEquals(0, rs.getInt("statusS"))
        assertEquals(zr.idZrtva, rs.getInt("zrtvaId"))
        val storedTimestamp = rs.getTimestamp("datumVreme").time
        assertTrue(abs(storedTimestamp - d) < 1000)
    }

    @Test
    fun testInsertGalerijaData(){
        println("galerijaTest")
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u novom hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val galerija = GalerijaData(
            idGalerija = 1,
            tip = 1,
            putanja = "/mojeSlike",
            zrtvaId = zr,
            datumVreme = d,
            lokacija = "hotel"
        )


        repo.insertGalerijaData(galerija,zr)
        val stmt = connection.prepareStatement("SELECT * FROM galerija WHERE zrtvaId=?")
        stmt.setInt(1, zr.idZrtva)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji galerija sa prosledjenim zrtvaId-om")
        assertEquals(galerija.idGalerija, rs.getInt("idGalerija"))
        assertEquals(1, rs.getInt("tip"))
        assertEquals("/mojeSlike", rs.getString("putanja"))
        assertEquals("hotel", rs.getString("lokacija"))
        assertEquals(zr.idZrtva, rs.getInt("zrtvaId"))
        val storedTimestamp = rs.getTimestamp("datumVreme").time
        assertTrue(abs(storedTimestamp - d) < 1000)
    }


    @Test
    fun testInsertAplikacijaData(){
        println("aplikacijaTest")
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u ukletom hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val aplikacija = AplikacijaData(
            idAplikacije = 1,
            naziv = "Instagram",
            tip = 0,
            zrtvaId = zr,
            aktivna = true,
            informacije = "Poslednja aktivnost na Instagram profilu žrtve."
        )

        repo.insertAplikacijaData(aplikacija,zr)
        val stmt = connection.prepareStatement("SELECT * FROM aplikacija WHERE zrtvaId=?")
        stmt.setInt(1, zr.idZrtva)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji aplikacija sa prosledjenim zrtvaId-om")
        assertEquals(aplikacija.idAplikacije, rs.getInt("idAplikacije"))
        assertEquals(0, rs.getInt("tip"))
        assertEquals("Instagram", rs.getString("naziv"))
        assertEquals("Poslednja aktivnost na Instagram profilu žrtve.", rs.getString("informacije"))
        assertEquals(zr.idZrtva, rs.getInt("zrtvaId"))
        assertEquals(true, rs.getBoolean("aktivna"))
    }

    @Test
    fun testInsertTragData(){
        println("tragTest")
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u luksuznom hotelu",
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

        val m = MotivData(
            idMotiv = 1,
            opis = "Ljubomora"
        )
        repo.insertMotivData(m)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = m,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )

        repo.insertOsumnjicenData(osumnjicen,zlocin,m)

        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val dokaz = ForenzickiDokazData(
            idForenzickiDokaz = 1,
            tipForenzickiDokaz = "DNK",
            opis = "DNK tragovi pronađeni na pištolju.",
            statusS = 0,
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
        )
        repo.insertForenzickiDokaz(dokaz,zr)

        val trag = TragData(
            idTrag = 1,
            forenzickiDokazId = dokaz,
            osumnjicenId = osumnjicen
        )
        repo.insertTragData(trag,dokaz,osumnjicen)

        val stmt = connection.prepareStatement("SELECT * FROM trag WHERE idTrag=?")
        stmt.setInt(1, trag.idTrag)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji trag sa prosledjenim zlocinId-om")
        assertEquals(trag.idTrag, rs.getInt("idTrag"))
        assertEquals(dokaz.idForenzickiDokaz, rs.getInt("forenzickiDokazId"))
        assertEquals(osumnjicen.idOsumnjicen, rs.getInt("osumnjicenId"))

    }

    @Test
    fun testInsertDokazOsumnjicenData(){
        println("dokazOsumnjicenTest")
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u hotelu sa 3 zvezdice",
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

        val m = MotivData(
            idMotiv = 1,
            opis = "Ljubomora"
        )
        repo.insertMotivData(m)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = m,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )

        repo.insertOsumnjicenData(osumnjicen,zlocin,m)

        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val dokaz = DokazData(
            idDokaz = 1,
            tipDokaza = "fizicki",
            opis = "Pistolj pronadjen na mestu zlocina.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zr.idZrtva,
            status = 0
        )
        repo.insertDokazData(dokaz,zlocin,zr)

        val dokazOsumnjicen = DokazOsumnjicenData(
            idDokazOsumnjicen = 1,
            dokazId = dokaz,
            osumnjicenId = osumnjicen
        )
        repo.insertDokazOsumnjicenData(dokazOsumnjicen, dokaz, osumnjicen)

        val stmt = connection.prepareStatement("SELECT * FROM dokazOsumnjicen WHERE idDokazOsumnjicen=?")
        stmt.setInt(1, dokazOsumnjicen.idDokazOsumnjicen)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji dokazOsumnjicen")
        assertEquals(dokazOsumnjicen.idDokazOsumnjicen, rs.getInt("idDokazOsumnjicen"))
        assertEquals(dokaz.idDokaz, rs.getInt("dokazId"))
        assertEquals(osumnjicen.idOsumnjicen, rs.getInt("osumnjicenId"))
    }

    @Test
    fun testInsertOneContactData(){
        println("oneContactTest")
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u tudjoj kuci",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mlade zene",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)
        println(zlocin.idZlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val oneContact =OneContactData(
            idOneContact = 1,
            zlocinId = zlocin.idZlocin,
            ime = "John",
            broj = "+54533465645",
            slika = 1,
        )
        repo.insertOneContactData(oneContact,zlocin)

        val stmt = connection.prepareStatement("SELECT * FROM oneContact WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji oneContact sa prosledjenim zlocinId-om")
        assertEquals(oneContact.idOneContact, rs.getInt("idOneContact"))
        assertEquals("John", rs.getString("ime"))
        assertEquals("+54533465645", rs.getString("broj"))
        assertEquals(1, rs.getInt("slika"))
        assertEquals(zlocin.idZlocin, rs.getInt("zlocinId"))
    }

    @Test
    fun testInsertBeleskaData(){
        println("beleskaTest")
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u necijoj kuci",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mlade zene",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)
        println(zlocin.idZlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val beleska = BeleskaData(
            idBeleska = 1,
            zlocinId = zlocin.idZlocin,
            tekst = "Moram da stignem pre njega",
            datum = d
        )
        repo.insertBeleskaData(beleska,zlocin)

        val stmt = connection.prepareStatement("SELECT * FROM beleska WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji beleska sa prosledjenim zlocinId-om")
        assertEquals(beleska.idBeleska, rs.getInt("idBeleska"))
        assertEquals("Moram da stignem pre njega", rs.getString("tekst"))
        assertEquals(zlocin.idZlocin, rs.getInt("zlocinId"))
        val storedTimestamp = rs.getTimestamp("datum").time
        assertTrue(abs(storedTimestamp - d) < 1000)
    }

    @Test
    fun testInsertWhatsAppKontaktData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u tudjoj kuci",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mlade zene",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)
        println(zlocin.idZlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val whatsAppKontakt =WhatsAppKontaktData(
            idWhatsAppKontakt = 1,
            zlocinId = zlocin.idZlocin,
            ime = "Tom",
            broj = "+1231423152",
            slika = 13
        )
        repo.insertWhatsAppKontaktData(whatsAppKontakt,zlocin)

        val stmt = connection.prepareStatement("SELECT * FROM whatsappkontakt WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji whatsappkontakt sa prosledjenim zlocinId-om")
        assertEquals(whatsAppKontakt.idWhatsAppKontakt, rs.getInt("idWhatsAppKontakt"))
        assertEquals("Tom", rs.getString("ime"))
        assertEquals("+1231423152", rs.getString("broj"))
        assertEquals(13, rs.getInt("slika"))
        assertEquals(zlocin.idZlocin, rs.getInt("zlocinId"))
    }

    @Test
    fun testInsertUsedZlocinData(){
        println("usedZlocinTest")
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u staroj porodicnoj kuci",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mladog coveka",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val usedZlocin= UsedZlocinData(
            idUsedZlocin = 1,
            zlocinId = zlocin,
            used = false
        )

        repo.insertUsedZlocinData(usedZlocin)

        val stmt = connection.prepareStatement("SELECT * FROM usedzlocin WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji usedZlocin sa prosledjenim zlocinId-om")
        assertEquals(usedZlocin.idUsedZlocin, rs.getInt("idUsedZlocin"))
        assertEquals(zlocin.idZlocin, rs.getInt("zlocinId"))
        assertEquals(usedZlocin.used, rs.getBoolean("used"))
    }

    @Test
    fun testGetZlocin(){
        println("getZlocinTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u staroj porodicnoj kuci",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mladog coveka",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)

        val zlocinGet =repoGet.getZlocin(zlocin.idZlocin)
        val zlocinNePostoji =repoGet.getZlocin(zlocin.idZlocin+11)

        assertTrue(zlocinGet!=null, "Treba da postoji zlocin sa prosledjenim id-om")
        assertTrue(zlocinNePostoji==null, "Treba da ne postoji zlocin sa prosledjenim id-om")
        assertEquals(zlocinGet.idZlocin, zlocin.idZlocin)
        assertEquals(zlocinGet.tipZlocinaId, zlocin.tipZlocinaId)
        assertEquals(zlocinGet.naziv, zlocin.naziv)
        assertTrue(abs(zlocinGet.datum - zlocin.datum) < 1000)
        assertEquals(zlocinGet.mesto, zlocin.mesto)
        assertEquals(zlocinGet.opis, zlocin.opis)
        assertEquals(zlocinGet.status, zlocin.status)
    }

    @Test
    fun testGetZrtva(){
        println("getZrtvaTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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
        println(zlocin.idZlocin)

        val zrtvaGet = repoGet.getZrtva(zlocin.idZlocin)
        val zrtvaNePostoji =repoGet.getZrtva(zlocin.idZlocin+11)

        assertTrue(zrtvaGet!=null, "Treba da postoji zrtva sa prosledjenim id-om zlocina")
        assertTrue(zrtvaNePostoji==null, "Treba da ne postoji zrtva sa prosledjenim id-om zlocina")
        assertEquals(zrtvaGet.idZrtva, zr.idZrtva)
        assertEquals(zrtvaGet.statusZrtva, zr.statusZrtva)
        assertEquals(zrtvaGet.zlocinId, zr.zlocinId)
        assertEquals(zrtvaGet.osobaId.idOsoba, zr.osobaId.idOsoba)
        assertEquals(zrtvaGet.osobaId.ime, zr.osobaId.ime)
        assertEquals(zrtvaGet.osobaId.zlocinId, zr.osobaId.zlocinId)
        assertEquals(zrtvaGet.osobaId.kontakt, zr.osobaId.kontakt)
        assertEquals(zrtvaGet.osobaId.pol, zr.osobaId.pol)
        assertEquals(zrtvaGet.osobaId.zanimanje, zr.osobaId.zanimanje)
        assertTrue(abs(zrtvaGet.osobaId.datum-zr.osobaId.datum)<1000)
        assertEquals(zrtvaGet.detalji, zr.detalji)
        assertEquals(zrtvaGet.tipZrtve, zr.tipZrtve)
    }

    @Test
    fun testGetOsumnjicen(){
        println("getOsumnjicenTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val osoba2= OsobaData(
            idOsoba = 2,
            ime = "Tomas Black",
            kontakt = "+4433337888999",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba2,zlocin)

        val m = MotivData(
            idMotiv = 1,
            opis = "Ljubomora"
        )
        repo.insertMotivData(m)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = m,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba2
        )
        repo.insertOsumnjicenData(osumnjicen,zlocin,m)

        val osumnjicenGet = repoGet.getOsumnjiceni(zlocin.idZlocin)
        val osumnjicenNePostoji =repoGet.getOsumnjiceni(zlocin.idZlocin+11)

        assertTrue(osumnjicenGet!= emptyList<OsumnjicenData>(), "Treba da postoji osumnjicen sa prosledjenim id-om zlocina")
        assertTrue(osumnjicenNePostoji==emptyList<OsumnjicenData>(), "Treba da ne postoji osumnjicen sa prosledjenim id-om zlocina")

        for(o in osumnjicenGet){
            if(o.idOsumnjicen == osumnjicen.idOsumnjicen){
                assertEquals(o.idOsumnjicen, osumnjicen.idOsumnjicen)
                assertEquals(o.status, osumnjicen.status)
                assertEquals(o.zlocinId, osumnjicen.zlocinId)
                assertEquals(o.osobaId.idOsoba, osumnjicen.osobaId.idOsoba)
                assertEquals(o.osobaId.ime, osumnjicen.osobaId.ime)
                assertEquals(o.osobaId.zlocinId, osumnjicen.osobaId.zlocinId)
                assertEquals(o.osobaId.kontakt, osumnjicen.osobaId.kontakt)
                assertEquals(o.osobaId.pol, osumnjicen.osobaId.pol)
                assertEquals(o.osobaId.zanimanje, osumnjicen.osobaId.zanimanje)
                assertTrue(abs(o.osobaId.datum-osumnjicen.osobaId.datum)<1000)
                assertEquals(o.tipOsumnjicen, osumnjicen.tipOsumnjicen)
                assertEquals(o.kriv, osumnjicen.kriv)
                assertEquals(o.motiv, osumnjicen.motiv)
            }

        }
    }

    @Test
    fun testGetDokazi(){
        println("getDokaziTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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
        repo.insertZrtva(zr,zlocin,osoba)

        val dokaz = DokazData(
            idDokaz = 1,
            tipDokaza = "fizicki",
            opis = "Pistolj pronadjen na mestu zlocina.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zr.idZrtva,
            status = 0
        )
        repo.insertDokazData(dokaz,zlocin,zr)

        val dokaziGet = repoGet.getDokazi(zlocin.idZlocin,zr)
        val dokaziNePostoji =repoGet.getDokazi(zlocin.idZlocin+11,zr)

        assertTrue(dokaziGet!= emptyList<DokazData>(), "Treba da postoje dokazi sa prosledjenim id-om zlocina")
        assertTrue(dokaziNePostoji==emptyList<DokazData>(), "Treba da ne postoje dokazi sa prosledjenim id-om zlocina")

        if (dokaziGet != null) {
            for(o in dokaziGet){
                if(o.idDokaz == dokaz.idDokaz){
                    assertEquals(o.idDokaz, dokaz.idDokaz)
                    assertEquals(o.status, dokaz.status)
                    assertEquals(o.zlocinId, dokaz.zlocinId)
                    assertEquals(o.opis, dokaz.opis)
                    assertEquals(o.tipDokaza, dokaz.tipDokaza)
                    assertEquals(o.zrtvaId, dokaz.zrtvaId)
                }
            }
        }
    }

    @Test
    fun testGetTelefon(){
        println("getTelefonTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo na svadbi",
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
        repo.insertZrtva(zr,zlocin, osoba)

        val tel = TelefonData(
            idTelefon = 1,
            model = "Samsung Galaxy S22",
            os = "Android",
            sifra = "1234",
            informacije = "Pronađene su poruke sa pretnjama.",
            zrtvaId = zr.idZrtva
        )

        repo.insertTelefonData(tel,zr)

        val telefonGet = repoGet.getTelefon(zr.idZrtva)
        val telefonNePostoji =repoGet.getTelefon(zr.idZrtva+11)

        assertTrue(telefonGet!= emptyList<TelefonData>(), "Treba da postoje telefoni sa prosledjenim id-om zlocina")
        assertTrue(telefonNePostoji==emptyList<TelefonData>(), "Treba da ne postoje telefonisa prosledjenim id-om zlocina")

        if (telefonGet != null) {
            for(o in telefonGet){
                if(o.idTelefon == tel.idTelefon){
                    assertEquals(o.idTelefon, tel.idTelefon)
                    assertEquals(o.os, tel.os)
                    assertEquals(o.sifra, tel.sifra)
                    assertEquals(o.model, tel.model)
                    assertEquals(o.zrtvaId, tel.zrtvaId)
                    assertEquals(o.informacije, tel.informacije)
                }
            }
        }
    }

    @Test
    fun testGetForenzickiDokazi(){
        println("getForenzickiDokaziTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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
        repo.insertZrtva(zr,zlocin,osoba)

        val dokaz = ForenzickiDokazData(
            idForenzickiDokaz = 1,
            tipForenzickiDokaz = "DNK",
            opis = "DNK tragovi pronađeni na pištolju.",
            statusS = 0,
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
        )
        repo.insertForenzickiDokaz(dokaz,zr)


        val dokaziGet = repoGet.getForenzickiDokazi(zr.idZrtva)
        val dokaziNePostoji =repoGet.getForenzickiDokazi(zr.idZrtva+11)

        assertTrue(dokaziGet!= emptyList<ForenzickiDokazData>(), "Treba da postoje dokazi sa prosledjenim id-om zlocina")
        assertTrue(dokaziNePostoji==emptyList<ForenzickiDokazData>(), "Treba da ne postoje dokazi sa prosledjenim id-om zlocina")

        if (dokaziGet != null) {
            for(o in dokaziGet){
                if(o.idForenzickiDokaz == dokaz.idForenzickiDokaz){
                    assertEquals(o.idForenzickiDokaz, dokaz.idForenzickiDokaz)
                    assertEquals(o.tipForenzickiDokaz, dokaz.tipForenzickiDokaz)
                    assertEquals(o.veza, dokaz.veza)
                    assertEquals(o.opis, dokaz.opis)
                    assertEquals(o.statusS, dokaz.statusS)
                }
            }
        }
    }

    @Test
    fun testGetObdukcija(){
        println("getObdukcijaTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val obdukcijaGet = repoGet.getObdukcija(zr.idZrtva)
        val obdukcijaNePostoji =repoGet.getZrtva(zr.idZrtva+11)

        assertTrue(obdukcijaGet!=null, "Treba da postoji obdukcija sa prosledjenim id-om zrtve")
        assertTrue(obdukcijaNePostoji==null, "Treba da ne postoji obdukcija sa prosledjenim id-om zrtve")
        assertEquals(obdukcijaGet.idObdukcija, obdukcija.idObdukcija)
        assertEquals(obdukcijaGet.uzrokSmrti, obdukcija.uzrokSmrti)
        assertEquals(obdukcijaGet.zrtvaId, obdukcija.zrtvaId)
        assertEquals(obdukcijaGet.informacije, obdukcija.informacije)
        assertEquals(obdukcijaGet.izvestaj, obdukcija.izvestaj)
        assertTrue(abs(obdukcijaGet.datum-obdukcija.datum)<1000)
    }

    @Test
    fun testGetSvedoci(){
        println("getSvedociTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val svedokGet = repoGet.getSvedoci(zlocin.idZlocin)
        val svedokNePostoji =repoGet.getSvedoci(zlocin.idZlocin+11)

        assertTrue(svedokGet!= emptyList<SvedokData>(), "Treba da postoje svedoci sa prosledjenim id-om zlocina")
        assertTrue(svedokNePostoji==emptyList<SvedokData>(), "Treba da ne postoje svedoci sa prosledjenim id-om zlocina")

        if (svedokGet != null) {
            for(o in svedokGet){
                if(o.idSvedok == svedok.idSvedok){
                    assertEquals(o.idSvedok, svedok.idSvedok)
                    assertEquals(o.statusIspitan, svedok.statusIspitan)
                    assertEquals(o.statusSvedok, svedok.statusSvedok)
                    assertEquals(o.izjava, svedok.izjava)
                    assertEquals(o.zlocinId, svedok.zlocinId)
                    assertEquals(o.osobaId.idOsoba, svedok.osobaId.idOsoba)
                    assertEquals(o.osobaId.ime, svedok.osobaId.ime)
                    assertEquals(o.osobaId.zlocinId, svedok.osobaId.zlocinId)
                    assertEquals(o.osobaId.kontakt, svedok.osobaId.kontakt)
                    assertEquals(o.osobaId.pol, svedok.osobaId.pol)
                    assertEquals(o.osobaId.zanimanje, svedok.osobaId.zanimanje)
                    assertTrue(abs(o.osobaId.datum-svedok.osobaId.datum)<1000)
                }
            }
        }
    }

    @Test
    fun testGetOneContact(){
        println("getOneContactTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u tudjoj kuci",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mlade zene",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val oneContact =OneContactData(
            idOneContact = 1,
            zlocinId = zlocin.idZlocin,
            ime = "John",
            broj = "+54533465645",
            slika = 1,
        )
        repo.insertOneContactData(oneContact,zlocin)

        val oneContactGet = repoGet.getOneContact(zlocin.idZlocin)
        val oneContactNePostoji =repoGet.getOneContact(zlocin.idZlocin+11)

        assertTrue(oneContactGet!= emptyList<OneContactData>(), "Treba da postoje oneContact sa prosledjenim id-om zlocina")
        assertTrue(oneContactNePostoji==emptyList<OneContactData>(), "Treba da ne postoje oneContact sa prosledjenim id-om zlocina")

        if (oneContactGet != null) {
            for(o in oneContactGet){
                if(o.idOneContact == oneContact.idOneContact){
                    assertEquals(o.idOneContact, oneContact.idOneContact)
                    assertEquals(o.zlocinId, oneContact.zlocinId)
                    assertEquals(o.ime, oneContact.ime)
                    assertEquals(o.broj, oneContact.broj)
                    assertEquals(o.slika, oneContact.slika)
                }
            }
        }
    }

    @Test
    fun testGetKontakti(){
        println("getKontaktiTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()
        connection.autoCommit = true

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zr,zlocin,osoba)

        println(zr)
        val kontakt = KontaktData(
            idKontakt = 1,
            ime = "Elenora",
            broj = "+442123412312",
            status = 1,
            zrtvaId = zr
        )
        repo.insertKontaktData(kontakt,zr)

        val kontaktGet = repoGet.getKontakti(zr.idZrtva,zr)
        val kontaktNePostoji =repoGet.getKontakti(zr.idZrtva+11,ZrtvaData(zr.idZrtva+11,"","","",zlocin.idZlocin,osoba))

        println(kontaktGet)
        assertTrue(kontaktGet!= emptyList<KontaktData>(), "Treba da postoje kontakti sa prosledjenim id-om zlocina")
        assertTrue(kontaktNePostoji==emptyList<KontaktData>(), "Treba da ne postoje kontakti sa prosledjenim id-om zlocina")

        if (kontaktGet != null) {
            for(o in kontaktGet){
                if(o.idKontakt == kontakt.idKontakt){
                    assertEquals(o.idKontakt, kontakt.idKontakt)
                    assertEquals(o.status, kontakt.status)
                    assertEquals(o.ime, kontakt.ime)
                    assertEquals(o.broj, kontakt.broj)
                    assertEquals(o.zrtvaId, kontakt.zrtvaId)
                }
            }
        }
    }

    @Test
    fun testGetPoruke(){
        println("getPorukeTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zr,zlocin,osoba)

        val kontakt = KontaktData(
            idKontakt = 1,
            ime = "Alex",
            broj = "+472123412312",
            status = 1,
            zrtvaId = zr
        )
        repo.insertKontaktData(kontakt,zr)

        val poruka = PorukeData(
            idPoruke = 1,
            tipPoruke = "SMS",
            sadrzaj = ":(",
            datumVreme = d,
            zrtvaId = zr,
            posiljalacId = kontakt,
            statusPoruke = "sent",
            sifrovana = false
        )
        repo.insertPorukeData(poruka,zr,kontakt)

        val porukeGet = repoGet.getPoruke(zr.idZrtva,zr, listOf(kontakt))
        val porukeNePostoji =repoGet.getPoruke(zr.idZrtva+11,ZrtvaData(zr.idZrtva+11,"","","",zlocin.idZlocin,osoba),
            listOf(kontakt)
        )

        assertTrue(porukeGet!= emptyList<PorukeData>(), "Treba da postoje poruke sa prosledjenim id-om zlocina")
        assertTrue(porukeNePostoji==emptyList<PorukeData>(), "Treba da ne postoje poruke sa prosledjenim id-om zlocina")

        if (porukeGet != null) {
            for(o in porukeGet){
                if(o.idPoruke == poruka.idPoruke){
                    assertEquals(o.idPoruke, poruka.idPoruke)
                    assertEquals(o.tipPoruke, poruka.tipPoruke)
                    assertEquals(o.statusPoruke, poruka.statusPoruke)
                    assertEquals(o.sifrovana, poruka.sifrovana)
                    assertEquals(o.zrtvaId.idZrtva, poruka.zrtvaId.idZrtva)
                    assertEquals(o.zrtvaId.zlocinId, poruka.zrtvaId.zlocinId)
                    assertEquals(o.zrtvaId.statusZrtva, poruka.zrtvaId.statusZrtva)
                    assertEquals(o.zrtvaId.detalji, poruka.zrtvaId.detalji)
                    assertEquals(o.zrtvaId.tipZrtve, poruka.zrtvaId.tipZrtve)
                    assertEquals(o.zrtvaId.osobaId.idOsoba, poruka.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.zrtvaId.osobaId.idOsoba, poruka.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.zrtvaId.osobaId.ime, poruka.zrtvaId.osobaId.ime)
                    assertEquals(o.zrtvaId.osobaId.zlocinId, poruka.zrtvaId.osobaId.zlocinId)
                    assertEquals(o.zrtvaId.osobaId.kontakt, poruka.zrtvaId.osobaId.kontakt)
                    assertEquals(o.zrtvaId.osobaId.pol, poruka.zrtvaId.osobaId.pol)
                    assertEquals(o.zrtvaId.osobaId.zanimanje, poruka.zrtvaId.osobaId.zanimanje)
                    assertTrue(abs(o.zrtvaId.osobaId.datum-poruka.zrtvaId.osobaId.datum)<1000)
                    assertTrue(abs(o.datumVreme-poruka.datumVreme)<1000)
                    assertEquals(o.sadrzaj, poruka.sadrzaj)

                    assertEquals(o.posiljalacId.ime, poruka.posiljalacId.ime)
                    assertEquals(o.posiljalacId.status, poruka.posiljalacId.status)
                    assertEquals(o.posiljalacId.idKontakt, poruka.posiljalacId.idKontakt)
                    assertEquals(o.posiljalacId.broj, poruka.posiljalacId.broj)
                    assertEquals(o.posiljalacId.zrtvaId.idZrtva, poruka.posiljalacId.zrtvaId.idZrtva)
                    assertEquals(o.posiljalacId.zrtvaId.zlocinId, poruka.posiljalacId.zrtvaId.zlocinId)
                    assertEquals(o.posiljalacId.zrtvaId.tipZrtve, poruka.posiljalacId.zrtvaId.tipZrtve)
                    assertEquals(o.posiljalacId.zrtvaId.detalji, poruka.posiljalacId.zrtvaId.detalji)
                    assertEquals(o.posiljalacId.zrtvaId.statusZrtva, poruka.posiljalacId.zrtvaId.statusZrtva)

                    assertEquals(o.posiljalacId.zrtvaId.osobaId.idOsoba, poruka.posiljalacId.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.posiljalacId.zrtvaId.osobaId.idOsoba, poruka.posiljalacId.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.posiljalacId.zrtvaId.osobaId.ime, poruka.posiljalacId.zrtvaId.osobaId.ime)
                    assertEquals(o.posiljalacId.zrtvaId.osobaId.zlocinId, poruka.posiljalacId.zrtvaId.osobaId.zlocinId)
                    assertEquals(o.posiljalacId.zrtvaId.osobaId.kontakt, poruka.posiljalacId.zrtvaId.osobaId.kontakt)
                    assertEquals(o.posiljalacId.zrtvaId.osobaId.pol, poruka.posiljalacId.zrtvaId.osobaId.pol)
                    assertEquals(o.posiljalacId.zrtvaId.osobaId.zanimanje, poruka.posiljalacId.zrtvaId.osobaId.zanimanje)
                    assertTrue(abs(o.posiljalacId.zrtvaId.osobaId.datum-poruka.posiljalacId.zrtvaId.osobaId.datum)<1000)
                }
            }
        }
    }

    @Test
    fun testGetPozivi(){
        println("getPoziviTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u starom hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val kontakt = KontaktData(
            idKontakt = 1,
            ime = "Alex",
            broj = "+472123412312",
            status = 1,
            zrtvaId = zr
        )
        repo.insertKontaktData(kontakt,zr)

        val poziv = PoziviData(
            idPoziv = 1,
            tip = 0,
            broj = "+432635647547",
            datumVreme = d,
            zrtvaId = zr,
            status = 0,
            kontaktId = kontakt
        )
        repo.insertPoziviData(poziv, zr,kontakt)

        val poziviGet = repoGet.getPozivi(zr.idZrtva,zr, listOf(kontakt))
        val poziviNePostoji =repoGet.getPozivi(zr.idZrtva+11,ZrtvaData(zr.idZrtva+11,"","","",zlocin.idZlocin,osoba),
            listOf(kontakt)
        )

        assertTrue(poziviGet!= emptyList<PoziviData>(), "Treba da postoje pozivi sa prosledjenim id-om zlocina")
        assertTrue(poziviNePostoji==emptyList<PoziviData>(), "Treba da ne postoje pozivi sa prosledjenim id-om zlocina")

        if (poziviGet != null) {
            for(o in poziviGet){
                if(o.idPoziv == poziv.idPoziv){
                    assertEquals(o.idPoziv, poziv.idPoziv)
                    assertEquals(o.status, poziv.status)
                    assertEquals(o.tip, poziv.tip)
                    assertEquals(o.broj, poziv.broj)
                    assertEquals(o.zrtvaId.idZrtva, poziv.zrtvaId.idZrtva)
                    assertEquals(o.zrtvaId.zlocinId, poziv.zrtvaId.zlocinId)
                    assertEquals(o.zrtvaId.statusZrtva, poziv.zrtvaId.statusZrtva)
                    assertEquals(o.zrtvaId.detalji, poziv.zrtvaId.detalji)
                    assertEquals(o.zrtvaId.tipZrtve, poziv.zrtvaId.tipZrtve)
                    assertEquals(o.zrtvaId.osobaId.idOsoba, poziv.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.zrtvaId.osobaId.idOsoba, poziv.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.zrtvaId.osobaId.ime, poziv.zrtvaId.osobaId.ime)
                    assertEquals(o.zrtvaId.osobaId.zlocinId, poziv.zrtvaId.osobaId.zlocinId)
                    assertEquals(o.zrtvaId.osobaId.kontakt, poziv.zrtvaId.osobaId.kontakt)
                    assertEquals(o.zrtvaId.osobaId.pol, poziv.zrtvaId.osobaId.pol)
                    assertEquals(o.zrtvaId.osobaId.zanimanje, poziv.zrtvaId.osobaId.zanimanje)
                    assertTrue(abs(o.zrtvaId.osobaId.datum-poziv.zrtvaId.osobaId.datum)<1000)

                    assertTrue(abs(o.datumVreme-poziv.datumVreme)<1000)
                    assertEquals(o.kontaktId.idKontakt, poziv.kontaktId.idKontakt)
                    assertEquals(o.kontaktId.ime, poziv.kontaktId.ime)
                    assertEquals(o.kontaktId.status, poziv.kontaktId.status)
                    assertEquals(o.kontaktId.idKontakt, poziv.kontaktId.idKontakt)
                    assertEquals(o.kontaktId.broj, poziv.kontaktId.broj)
                    assertEquals(o.kontaktId.zrtvaId.idZrtva, poziv.kontaktId.zrtvaId.idZrtva)
                    assertEquals(o.kontaktId.zrtvaId.zlocinId, poziv.kontaktId.zrtvaId.zlocinId)
                    assertEquals(o.kontaktId.zrtvaId.tipZrtve, poziv.kontaktId.zrtvaId.tipZrtve)
                    assertEquals(o.kontaktId.zrtvaId.detalji, poziv.kontaktId.zrtvaId.detalji)
                    assertEquals(o.kontaktId.zrtvaId.statusZrtva, poziv.kontaktId.zrtvaId.statusZrtva)

                    assertEquals(o.kontaktId.zrtvaId.osobaId.idOsoba, poziv.kontaktId.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.kontaktId.zrtvaId.osobaId.idOsoba, poziv.kontaktId.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.kontaktId.zrtvaId.osobaId.ime, poziv.kontaktId.zrtvaId.osobaId.ime)
                    assertEquals(o.kontaktId.zrtvaId.osobaId.zlocinId, poziv.kontaktId.zrtvaId.osobaId.zlocinId)
                    assertEquals(o.kontaktId.zrtvaId.osobaId.kontakt, poziv.kontaktId.zrtvaId.osobaId.kontakt)
                    assertEquals(o.kontaktId.zrtvaId.osobaId.pol, poziv.kontaktId.zrtvaId.osobaId.pol)
                    assertEquals(o.kontaktId.zrtvaId.osobaId.zanimanje, poziv.kontaktId.zrtvaId.osobaId.zanimanje)
                    assertTrue(abs(o.kontaktId.zrtvaId.osobaId.datum-poziv.kontaktId.zrtvaId.osobaId.datum)<1000)
                }
            }
        }
    }


    @Test
    fun testGetGalerija(){
        println("getGalerijaTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()
        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u novom hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val galerija = GalerijaData(
            idGalerija = 11,
            tip = 0,
            putanja = "/mojeSlike2",
            zrtvaId = zr,
            datumVreme = d,
            lokacija = "hotel"
        )
        repo.insertGalerijaData(galerija,zr)

        val galerijaGet = repoGet.getGalerija(zr.idZrtva,zr)
        val galerijaNePostoji =repoGet.getGalerija(zr.idZrtva+11,ZrtvaData(zr.idZrtva+11,"","","",zlocin.idZlocin,osoba),)

        assertTrue(galerijaGet!= emptyList<GalerijaData>(), "Treba da postoje galerija sa prosledjenim id-om zlocina")
        assertTrue(galerijaNePostoji==emptyList<GalerijaData>(), "Treba da ne postoje galerija sa prosledjenim id-om zlocina")

        if (galerijaGet != null) {
            for(o in galerijaGet){
                if(o.idGalerija == galerija.idGalerija){
                    assertEquals(o.idGalerija, galerija.idGalerija)
                    assertEquals(o.tip, galerija.tip)
                    assertEquals(o.putanja, galerija.putanja)
                    assertEquals(o.lokacija, galerija.lokacija)

                    assertEquals(o.zrtvaId.idZrtva, galerija.zrtvaId.idZrtva)
                    assertEquals(o.zrtvaId.zlocinId, galerija.zrtvaId.zlocinId)
                    assertEquals(o.zrtvaId.statusZrtva, galerija.zrtvaId.statusZrtva)
                    assertEquals(o.zrtvaId.detalji, galerija.zrtvaId.detalji)
                    assertEquals(o.zrtvaId.tipZrtve, galerija.zrtvaId.tipZrtve)
                    assertEquals(o.zrtvaId.osobaId.idOsoba, galerija.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.zrtvaId.osobaId.idOsoba, galerija.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.zrtvaId.osobaId.ime, galerija.zrtvaId.osobaId.ime)
                    assertEquals(o.zrtvaId.osobaId.zlocinId, galerija.zrtvaId.osobaId.zlocinId)
                    assertEquals(o.zrtvaId.osobaId.kontakt, galerija.zrtvaId.osobaId.kontakt)
                    assertEquals(o.zrtvaId.osobaId.pol, galerija.zrtvaId.osobaId.pol)
                    assertEquals(o.zrtvaId.osobaId.zanimanje, galerija.zrtvaId.osobaId.zanimanje)
                    assertTrue(abs(o.zrtvaId.osobaId.datum-galerija.zrtvaId.osobaId.datum)<1000)

                    assertTrue(abs(o.datumVreme-galerija.datumVreme)<1000)
                }
            }
        }
    }


    @Test
    fun testGetAplikacije(){
        println("getAplikacijeTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u ukletom hotelu",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val aplikacija = AplikacijaData(
            idAplikacije = 1,
            naziv = "Instagram",
            tip = 0,
            zrtvaId = zr,
            aktivna = true,
            informacije = "Poslednja aktivnost na Instagram profilu žrtve."
        )
        repo.insertAplikacijaData(aplikacija,zr)

        val aplikacijaGet = repoGet.getAplikacije(zr.idZrtva,zr)
        val aplikacijaNePostoji =repoGet.getAplikacije(zr.idZrtva+11,ZrtvaData(zr.idZrtva+11,"","","",zlocin.idZlocin,osoba),)

        assertTrue(aplikacijaGet!= emptyList<AplikacijaData>(), "Treba da postoje aplikacije sa prosledjenim id-om zlocina")
        assertTrue(aplikacijaNePostoji==emptyList<AplikacijaData>(), "Treba da ne postoje aplikacije sa prosledjenim id-om zlocina")

        if (aplikacijaGet != null) {
            for(o in aplikacijaGet){
                if(o.idAplikacije == aplikacija.idAplikacije){
                    assertEquals(o.idAplikacije, aplikacija.idAplikacije)
                    assertEquals(o.tip, aplikacija.tip)
                    assertEquals(o.naziv, aplikacija.naziv)
                    assertEquals(o.aktivna, aplikacija.aktivna)
                    assertEquals(o.informacije, aplikacija.informacije)

                    assertEquals(o.zrtvaId.idZrtva, aplikacija.zrtvaId.idZrtva)
                    assertEquals(o.zrtvaId.zlocinId, aplikacija.zrtvaId.zlocinId)
                    assertEquals(o.zrtvaId.statusZrtva, aplikacija.zrtvaId.statusZrtva)
                    assertEquals(o.zrtvaId.detalji, aplikacija.zrtvaId.detalji)
                    assertEquals(o.zrtvaId.tipZrtve, aplikacija.zrtvaId.tipZrtve)
                    assertEquals(o.zrtvaId.osobaId.idOsoba, aplikacija.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.zrtvaId.osobaId.idOsoba, aplikacija.zrtvaId.osobaId.idOsoba)
                    assertEquals(o.zrtvaId.osobaId.ime, aplikacija.zrtvaId.osobaId.ime)
                    assertEquals(o.zrtvaId.osobaId.zlocinId, aplikacija.zrtvaId.osobaId.zlocinId)
                    assertEquals(o.zrtvaId.osobaId.kontakt, aplikacija.zrtvaId.osobaId.kontakt)
                    assertEquals(o.zrtvaId.osobaId.pol, aplikacija.zrtvaId.osobaId.pol)
                    assertEquals(o.zrtvaId.osobaId.zanimanje, aplikacija.zrtvaId.osobaId.zanimanje)
                    assertTrue(abs(o.zrtvaId.osobaId.datum-aplikacija.zrtvaId.osobaId.datum)<1000)

                }
            }
        }
    }


    @Test
    fun testGetTragovi(){
        println("getTragoviTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u luksuznom hotelu",
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

        val m = MotivData(
            idMotiv = 1,
            opis = "Ljubomora"
        )
        repo.insertMotivData(m)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = m,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )

        repo.insertOsumnjicenData(osumnjicen,zlocin,m)

        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val dokaz = ForenzickiDokazData(
            idForenzickiDokaz = 1,
            tipForenzickiDokaz = "DNK",
            opis = "DNK tragovi pronađeni na pištolju.",
            statusS = 0,
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
        )
        repo.insertForenzickiDokaz(dokaz,zr)

        val dokaz2 = ForenzickiDokazData(
            idForenzickiDokaz = 2,
            tipForenzickiDokaz = "DNK",
            opis = "DNK tragovi pronađeni na nozu",
            statusS = 0,
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
        )

        val trag = TragData(
            idTrag = 1,
            forenzickiDokazId = dokaz,
            osumnjicenId = osumnjicen
        )
        repo.insertTragData(trag,dokaz,osumnjicen)


        val tragoviGet = repoGet.getTragovi(listOf(dokaz), listOf(osumnjicen))
        val tragoviNePostoji =repoGet.getTragovi(listOf(dokaz2), listOf(osumnjicen))

        assertTrue(tragoviGet!= emptyList<TragData>(), "Treba da postoje tragovi.")
        assertTrue(tragoviNePostoji==emptyList<TragData>(), "Treba da ne postoje tragovi a")

        if (tragoviGet != null) {
            for(o in tragoviGet){
                if(o.idTrag == trag.idTrag){
                    assertEquals(o.idTrag, trag.idTrag)
                    assertEquals(o.forenzickiDokazId.idForenzickiDokaz, trag.forenzickiDokazId.idForenzickiDokaz)
                    assertEquals(o.forenzickiDokazId.tipForenzickiDokaz, trag.forenzickiDokazId.tipForenzickiDokaz)
                    assertEquals(o.forenzickiDokazId.veza, trag.forenzickiDokazId.veza)
                    assertEquals(o.forenzickiDokazId.opis, trag.forenzickiDokazId.opis)
                    assertEquals(o.forenzickiDokazId.statusS, trag.forenzickiDokazId.statusS)

                    assertEquals(o.osumnjicenId.idOsumnjicen, trag.osumnjicenId.idOsumnjicen)
                    assertEquals(o.osumnjicenId.status, trag.osumnjicenId.status)
                    assertEquals(o.osumnjicenId.zlocinId, trag.osumnjicenId.zlocinId)
                    assertEquals(o.osumnjicenId.osobaId.idOsoba, trag.osumnjicenId.osobaId.idOsoba)
                    assertEquals(o.osumnjicenId.osobaId.ime, trag.osumnjicenId.osobaId.ime)
                    assertEquals(o.osumnjicenId.osobaId.zlocinId, trag.osumnjicenId.osobaId.zlocinId)
                    assertEquals(o.osumnjicenId.osobaId.kontakt, trag.osumnjicenId.osobaId.kontakt)
                    assertEquals(o.osumnjicenId.osobaId.pol, trag.osumnjicenId.osobaId.pol)
                    assertEquals(o.osumnjicenId.osobaId.zanimanje, trag.osumnjicenId.osobaId.zanimanje)
                    assertTrue(abs(o.osumnjicenId.osobaId.datum-trag.osumnjicenId.osobaId.datum)<1000)
                    assertEquals(o.osumnjicenId.tipOsumnjicen, trag.osumnjicenId.tipOsumnjicen)
                    assertEquals(o.osumnjicenId.kriv, trag.osumnjicenId.kriv)
                    assertEquals(o.osumnjicenId.motiv, trag.osumnjicenId.motiv)

                }
            }
        }
    }


    @Test
    fun testGetDokazOsumnjicen(){
        println("getDokazOsumnjicenTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u hotelu sa 3 zvezdice",
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

        val m = MotivData(
            idMotiv = 1,
            opis = "Ljubomora"
        )
        repo.insertMotivData(m)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = m,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )

        repo.insertOsumnjicenData(osumnjicen,zlocin,m)

        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )

        repo.insertZrtva(zr,zlocin,osoba)

        val dokaz = DokazData(
            idDokaz = 1,
            tipDokaza = "fizicki",
            opis = "Pistolj pronadjen na mestu zlocina.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zr.idZrtva,
            status = 0
        )
        repo.insertDokazData(dokaz,zlocin,zr)

        val dokaz2 = DokazData(
            idDokaz = 2,
            tipDokaza = "fizicki",
            opis = "Noz pronadjen na mestu zlocina.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zr.idZrtva,
            status = 0
        )

        val dokazOsumnjicen = DokazOsumnjicenData(
            idDokazOsumnjicen = 1,
            dokazId = dokaz,
            osumnjicenId = osumnjicen
        )
        repo.insertDokazOsumnjicenData(dokazOsumnjicen, dokaz, osumnjicen)

        val dokazOsumnjicenGet = repoGet.getDokaziOsumnjiceni(listOf(dokaz), listOf(osumnjicen))
        val dokazNePostoji =repoGet.getDokaziOsumnjiceni(listOf(dokaz2), listOf(osumnjicen))

        assertTrue(dokazOsumnjicenGet!= emptyList<DokazOsumnjicenData>(), "Treba da postoje dokazOsumnjicen.")
        assertTrue(dokazNePostoji==emptyList<DokazOsumnjicenData>(), "Treba da ne postoje dokazOsumnjicen")

        if (dokazOsumnjicenGet != null) {
            for(o in dokazOsumnjicenGet){
                if(o.idDokazOsumnjicen == dokazOsumnjicen.idDokazOsumnjicen){
                    assertEquals(o.idDokazOsumnjicen, dokazOsumnjicen.idDokazOsumnjicen)

                    assertEquals(o.dokazId.idDokaz, dokazOsumnjicen.dokazId.idDokaz)
                    assertEquals(o.dokazId.opis, dokazOsumnjicen.dokazId.opis)
                    assertEquals(o.dokazId.tipDokaza, dokazOsumnjicen.dokazId.tipDokaza)
                    assertEquals(o.dokazId.status, dokazOsumnjicen.dokazId.status)
                    assertEquals(o.dokazId.zlocinId, dokazOsumnjicen.dokazId.zlocinId)
                    assertEquals(o.dokazId.zrtvaId, dokazOsumnjicen.dokazId.zrtvaId)

                    assertEquals(o.osumnjicenId.idOsumnjicen, dokazOsumnjicen.osumnjicenId.idOsumnjicen)
                    assertEquals(o.osumnjicenId.status, dokazOsumnjicen.osumnjicenId.status)
                    assertEquals(o.osumnjicenId.zlocinId, dokazOsumnjicen.osumnjicenId.zlocinId)
                    assertEquals(o.osumnjicenId.osobaId.idOsoba, dokazOsumnjicen.osumnjicenId.osobaId.idOsoba)
                    assertEquals(o.osumnjicenId.osobaId.ime, dokazOsumnjicen.osumnjicenId.osobaId.ime)
                    assertEquals(o.osumnjicenId.osobaId.zlocinId, dokazOsumnjicen.osumnjicenId.osobaId.zlocinId)
                    assertEquals(o.osumnjicenId.osobaId.kontakt, dokazOsumnjicen.osumnjicenId.osobaId.kontakt)
                    assertEquals(o.osumnjicenId.osobaId.pol, dokazOsumnjicen.osumnjicenId.osobaId.pol)
                    assertEquals(o.osumnjicenId.osobaId.zanimanje, dokazOsumnjicen.osumnjicenId.osobaId.zanimanje)
                    assertTrue(abs(o.osumnjicenId.osobaId.datum-dokazOsumnjicen.osumnjicenId.osobaId.datum)<1000)
                    assertEquals(o.osumnjicenId.tipOsumnjicen, dokazOsumnjicen.osumnjicenId.tipOsumnjicen)
                    assertEquals(o.osumnjicenId.kriv, dokazOsumnjicen.osumnjicenId.kriv)
                    assertEquals(o.osumnjicenId.motiv, dokazOsumnjicen.osumnjicenId.motiv)

                }
            }
        }
    }


    @Test
    fun testGetBeleske(){
        println("getBeleskeTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u necijoj kuci",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mlade zene",
            status = "u_istrazi",
            idZlocin = 0
        )
        repo.insertZlocinData(zlocin)
        println(zlocin.idZlocin)


        val beleska = BeleskaData(
            idBeleska = 1,
            zlocinId = zlocin.idZlocin,
            tekst = "Moram da stignem pre njega",
            datum = d
        )
        repo.insertBeleskaData(beleska,zlocin)


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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zr,zlocin, osoba)

        val beleskeGet = repoGet.getBeleske(zlocin.idZlocin, zr)
        val beleskeNePostoji =repoGet.getBeleske(zlocin.idZlocin+11, zr)

        assertTrue(beleskeGet!= emptyList<BeleskaData>(), "Treba da postoje beleske.")
        assertTrue(beleskeNePostoji==emptyList<BeleskaData>(), "Treba da ne postoje beleske")

        if (beleskeGet != null) {
            for(o in beleskeGet){
                if(o.idBeleska == beleska.idBeleska){
                    assertEquals(o.idBeleska, beleska.idBeleska)
                    assertEquals(o.tekst, beleska.tekst)
                    assertEquals(o.zlocinId, beleska.zlocinId)

                    assertTrue(abs(o.datum-beleska.datum)<1000)
                }
            }
        }
    }


    @Test
    fun testGetWhatsAppKontakt(){
        println("getWhatsAppKontaktTest")
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u tudjoj kuci",
            datum = d,
            mesto = "Pariz",
            opis = "Ubistvo mlade zene",
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
            detalji = "Korumpirana advokatica pronadjena mrtva.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zr,zlocin, osoba)

        val whatsAppKontakt =WhatsAppKontaktData(
            idWhatsAppKontakt = 1,
            zlocinId = zlocin.idZlocin,
            ime = "Tom",
            broj = "+1231423152",
            slika = 13
        )
        repo.insertWhatsAppKontaktData(whatsAppKontakt,zlocin)


        val whatsAppKontaktGet = repoGet.getWhatsAppKontakt(zlocin.idZlocin, zr)
        val whatsAppKontaktNePostoji =repoGet.getBeleske(zlocin.idZlocin+11, zr)

        assertTrue(whatsAppKontaktGet!= emptyList<WhatsAppKontaktData>(), "Treba da postoje WhatsAppKontaktData.")
        assertTrue(whatsAppKontaktNePostoji==emptyList<WhatsAppKontaktData>(), "Treba da ne postoje WhatsAppKontaktData")

        if (whatsAppKontaktGet != null) {
            for(o in whatsAppKontaktGet){
                if(o.idWhatsAppKontakt == whatsAppKontakt.idWhatsAppKontakt){
                    assertEquals(o.idWhatsAppKontakt, whatsAppKontakt.idWhatsAppKontakt)
                    assertEquals(o.slika, whatsAppKontakt.slika)
                    assertEquals(o.zlocinId, whatsAppKontakt.zlocinId)
                    assertEquals(o.ime, whatsAppKontakt.ime)
                    assertEquals(o.broj, whatsAppKontakt.broj)
                }
            }
        }
    }

    @Test
    fun testInsertWhatsAppKontaktData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u hotelskoj sobi",
            datum = d,
            mesto = "Amsterdam",
            opis = "Telo muskarca iz Italije pronadjeno u hotelskoj sobi. Na telu tragovi gusenja. Nema znakova borbe, ali sigurnosne kamere pokazale su nepoznatu zenu kako ulazi noc ranije.",
            status = "u_istrazi",
            idZlocin = 1
        )
        repo.insertZlocinData(zlocin)

        val whatsappKontakt = WhatsAppKontaktData(
            idWhatsAppKontakt = 1,
            zlocinId = zlocin.idZlocin,
            ime = "Carlos Martinez",
            broj = "+34612345678",
            slika = 1
        )
        repo.insertWhatsAppKontaktData(whatsappKontakt, zlocin)

        val stmt = connection.prepareStatement("SELECT * FROM whatsappkontakt WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji whatsappkontakt sa prosledjenim id-jem zlocina")
        assertEquals(whatsappKontakt.idWhatsAppKontakt, rs.getInt("idWhatsAppKontakt"))
        assertEquals(whatsappKontakt.zlocinId, rs.getInt("zlocinId"))
        assertEquals(whatsappKontakt.ime, rs.getString("ime"))
        assertEquals(whatsappKontakt.broj, rs.getString("broj"))
        assertEquals(whatsappKontakt.slika, rs.getInt("slika"))
    }

    @Test
    fun testInsertWhatsAppPorukaData(){
        val repo = RepositoryInsert(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u hotelskoj sobi",
            datum = d,
            mesto = "Amsterdam",
            opis = "Telo muskarca iz Italije pronadjeno u hotelskoj sobi. Na telu tragovi gusenja. Nema znakova borbe, ali sigurnosne kamere pokazale su nepoznatu zenu kako ulazi noc ranije.",
            status = "u_istrazi",
            idZlocin = 1
        )
        repo.insertZlocinData(zlocin)

        val kontaktKoSalje = WhatsAppKontaktData(
            idWhatsAppKontakt = 1,
            zlocinId = zlocin.idZlocin,
            ime = "Alessandro Moretti",
            broj = "+393312223344",
            slika = 1
        )
        repo.insertWhatsAppKontaktData(kontaktKoSalje, zlocin)

        val kontaktKomeSalje = WhatsAppKontaktData(
            idWhatsAppKontakt = 2,
            zlocinId = zlocin.idZlocin,
            ime = "Giulia Romano",
            broj = "+393316665555",
            slika = 2
        )
        repo.insertWhatsAppKontaktData(kontaktKomeSalje, zlocin)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val whatsAppPoruka = WhatsAppPorukaData(
            idWhatsAppPoruka = 1,
            kontaktKoSalje = kontaktKoSalje.idWhatsAppKontakt,
            kontaktKomeSalje = kontaktKomeSalje.idWhatsAppKontakt,
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.",
            datum = timestamp2,
            procitana = false
        )
        repo.insertWhatsAppPorukaData(whatsAppPoruka, kontaktKoSalje, kontaktKomeSalje)

        val stmt = connection.prepareStatement("SELECT * FROM whatsappporuka WHERE idWhatsAppPoruka=?")
        stmt.setInt(1, 1)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji whatsappporuka sa prosledjenim id-jem")
        assertEquals(whatsAppPoruka.idWhatsAppPoruka, rs.getInt("idWhatsAppPoruka"))
        assertEquals(whatsAppPoruka.kontaktKoSalje, rs.getInt("kontaktKoSalje"))
        assertEquals(whatsAppPoruka.kontaktKomeSalje, rs.getInt("kontaktKomeSalje"))
        assertEquals(whatsAppPoruka.tekst, rs.getString("tekst"))
        val storedTimestamp = rs.getTimestamp("datum").time
        assertTrue(abs(storedTimestamp - whatsAppPoruka.datum) < 1000)
        assertEquals(whatsAppPoruka.procitana, rs.getBoolean("procitana"))
    }
}