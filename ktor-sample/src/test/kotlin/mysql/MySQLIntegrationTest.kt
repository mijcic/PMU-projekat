package com.example.mysql

import com.example.models.dto.*
import com.example.repository.Repository
import com.example.repository.RepositoryInsert
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertNotNull
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
                CREATE TABLE usedzlocin(
                    idUsedZlocin INT AUTO_INCREMENT PRIMARY KEY,
                    zlocinId INT NOT NULL,
                    used BOOLEAN NOT NULL,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE onecall (
                    idOneCall INT AUTO_INCREMENT PRIMARY KEY,
                    kontakt INT NOT NULL,
                    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    propusten TINYINT(0),
                    dolazni TINYINT(0),
                    zrtvaId INT NOT NULL,
                    FOREIGN KEY (kontakt) REFERENCES oneContact(idOneContact),
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE gallery (
                    idPhoto INT AUTO_INCREMENT PRIMARY KEY,
                    zlocinId INT NOT NULL,
                    slika INT,
                    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    mesto VARCHAR(100) NOT NULL,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE obicnaporuka (
                    idObicnaPoruka INT AUTO_INCREMENT PRIMARY KEY,
                    kontaktKoSalje INT NOT NULL,
                    kontaktKomeSalje INT NOT NULL,
                    tekst VARCHAR(1000) NOT NULL,
                    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    procitana TINYINT(0),
                    FOREIGN KEY (kontaktKoSalje) REFERENCES oneContact(idOneContact),
                    FOREIGN KEY (kontaktKomeSalje) REFERENCES oneContact(idOneContact)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE odnososumnjicenzrtva (
                    idOdnos INT AUTO_INCREMENT PRIMARY KEY,
                    osumnjicenId INT NOT NULL,
                    zrtvaId INT NOT NULL,
                    tipOdnosa ENUM('poslovni', 'licni','porodicni','rivalski','slucajni','ljubavni') NOT NULL, 
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva),
                    FOREIGN KEY (osumnjicenId) REFERENCES osumnjicen(idOsumnjicen)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE prijavljenikorisnik (
                    idKorisnik INT AUTO_INCREMENT PRIMARY KEY,
                    korisnickoIme VARCHAR(100) NOT NULL,
                    sifra VARCHAR(100) NOT NULL
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE pitanje (
                    idPitanje INT AUTO_INCREMENT PRIMARY KEY,
                    zlocinId INT,
                    tekst VARCHAR(1000) NOT NULL
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE odgovor (
                    idOdogovor INT AUTO_INCREMENT PRIMARY KEY,
                    pitanjeId INT,
                    tekstOdgovora VARCHAR(1000) NOT NULL,
                    tacan TINYINT(0),
                    bodovi INT NOT NULL,
                    FOREIGN KEY (pitanjeId) REFERENCES pitanje(idPitanje)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE pitanjeispitivanjeosumnjicenog (
                    idPitanjeIspitivanjeOsumnjicenog INT AUTO_INCREMENT PRIMARY KEY,
                    kategorija ENUM('opsta', 'alibi', 'dokaz', 'kontradikcija') NOT NULL,
                    tekst VARCHAR(1000) NOT NULL,
                    odgovor VARCHAR(1000) NOT NULL,
                    komentar VARCHAR(1000) NOT NULL,
                    osumnjicenId INT NOT NULL,
                    FOREIGN KEY (osumnjicenId) REFERENCES osumnjicen(idOsumnjicen)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE pitanjeispitivanjesvedoka (
                    idPitanjeIspitivanjeSvedoka INT AUTO_INCREMENT PRIMARY KEY,
                    tekst VARCHAR(1000) NOT NULL,
                    odgovor VARCHAR(1000) NOT NULL,
                    svedokId INT NOT NULL,
                    nextPitanje INT NOT NULL,
                    FOREIGN KEY (svedokId) REFERENCES svedok(idSvedok)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE zadatak (
                    idZadatak INT AUTO_INCREMENT PRIMARY KEY,
                    tekst VARCHAR(1000) NOT NULL,
                    korak VARCHAR(1000) NOT NULL,
                    uradjen TINYINT(0),
                    nextZadatak INT,
                    zlocinId INT NOT NULL,
                    FOREIGN KEY (nextZadatak) REFERENCES zadatak(idZadatak)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE dokazzadatak (
                    idDokazZadatak INT AUTO_INCREMENT PRIMARY KEY,
                    tekst VARCHAR(1000) NOT NULL,
                    dokazId INT NOT NULL,
                    uradjen TINYINT(0),
                    zadatakId INT NOT NULL,
                    FOREIGN KEY (dokazId) REFERENCES dokaz(idDokaz),
                    FOREIGN KEY (zadatakId) REFERENCES zadatak(idZadatak)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE ispitivanjeosumnjicenogzadatak (
                    idIspitivanjeOsumnjicenogZadatak INT AUTO_INCREMENT PRIMARY KEY,
                    osumnjicenId INT NOT NULL,
                    zadatakId INT NOT NULL,
                    uradjen TINYINT(0),
                    FOREIGN KEY (osumnjicenId) REFERENCES osumnjicen(idOsumnjicen),
                    FOREIGN KEY (zadatakId) REFERENCES zadatak(idZadatak)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE ispitivanjesvedokazadatak (
                    idIspitivanjeSvedokaZadatak INT AUTO_INCREMENT PRIMARY KEY,
                    svedokId INT NOT NULL,
                    zadatakId INT NOT NULL,
                    uradjen TINYINT(0),
                    FOREIGN KEY (svedokId) REFERENCES svedok(idSvedok),
                    FOREIGN KEY (zadatakId) REFERENCES zadatak(idZadatak)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE telefonzadatak (
                    idTelefonZadatak INT AUTO_INCREMENT PRIMARY KEY,
                    telefonId INT NOT NULL,
                    zadatakId INT NOT NULL,
                    uradjen TINYINT(0),
                    FOREIGN KEY (telefonId) REFERENCES telefon(idTelefon),
                    FOREIGN KEY (zadatakId) REFERENCES zadatak(idZadatak)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE forenzickidokazzadatak (
                    idForenzickiDokazZadatak INT AUTO_INCREMENT PRIMARY KEY,
                    tekst VARCHAR(1000) NOT NULL,
                    forenzickiDokazId INT NOT NULL,
                    uradjen TINYINT(0),
                    zadatakId INT NOT NULL,
                    FOREIGN KEY (forenzickiDokazId) REFERENCES forenzickiDokaz(idForenzickiDokaz),
                    FOREIGN KEY (zadatakId) REFERENCES zadatak(idZadatak)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE pacijent (
                    idPacijent INT AUTO_INCREMENT PRIMARY KEY,
                    simptomi varchar(255) NOT NULL,
                    statusPacijenta ENUM('ziva', 'mrtva') NOT NULL,
                    datumPrijave DATETIME DEFAULT CURRENT_TIMESTAMP,
                    prijavio INT NOT NULL,
                    zlocinId INT NOT NULL,
                    zrtvaId INT NOT NULL,
                    FOREIGN KEY (prijavio) REFERENCES Osoba(idOsoba),
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin),
                    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE medicinskiizvestaj (
                    idMedicinskiIzvestaj INT AUTO_INCREMENT PRIMARY KEY,
                    rezime varchar(255) NOT NULL,
                    CTnalaz varchar(255) NOT NULL,
                    MRInalaz varchar(255) NOT NULL,
                    krvnaSlika varchar(255) NOT NULL,
                    toksikoloskeAnalize varchar(255) NOT NULL,
                    zakljucak varchar(255) NOT NULL,
                    pacijentId INT NOT NULL,
                    FOREIGN KEY (pacijentId) REFERENCES pacijent(idPacijent)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE lekarskitest (
                    idLekarskiTest INT AUTO_INCREMENT PRIMARY KEY,
                    pacijentId INT NOT NULL,
                    izjava varchar(255) NOT NULL,
                    FOREIGN KEY (pacijentId) REFERENCES pacijent(idPacijent)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE lokacijeistrage (
                    idLokacijeIstrage INT AUTO_INCREMENT PRIMARY KEY,
                    mesto varchar(100) NOT NULL,
                    naziv varchar(100) NOT NULL,
                    opis varchar(100) NOT NULL,
                    zlocinId INT NOT NULL,
                    geoTackaALatitude DOUBLE NOT NULL,
                    geoTackaALongitude DOUBLE NOT NULL,
                    FOREIGN KEY (zlocinId) REFERENCES zlocin(idZlocin)
                );
            """.trimIndent())

            stmt.execute("""
                CREATE TABLE izjavazapacijenta (
                    idIzjavaZaPacijenta INT AUTO_INCREMENT PRIMARY KEY,
                    izjava varchar(255) NOT NULL,
                    pacijentId INT NOT NULL,
                    osobaId INT NOT NULL,
                    FOREIGN KEY (pacijentId) REFERENCES pacijent(idPacijent),
                    FOREIGN KEY (osobaId) REFERENCES Osoba(idOsoba)
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
            tipDokaza = "digitalni",
            opis = "Pistolj pronadjen na mestu zlocina.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zr.idZrtva,
            status = 0
        )

        repo.insertDokazData(dokaz,zlocin,zr)

        val dokaz2 = DokazData(
            idDokaz = 1,
            tipDokaza = "noviTio",
            opis = "Noz pronadjen na mestu zlocina.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zr.idZrtva,
            status = 0
        )

        repo.insertDokazData(dokaz2,zlocin,zr)
        val stmt = connection.prepareStatement("SELECT * FROM dokaz WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji dokaz sa prosledjenim zlocinId-om")
        assertEquals(dokaz.idDokaz, rs.getInt("idDokaz"))
        assertEquals("digitalni", rs.getString("tipDokaza"))
        assertEquals("Pistolj pronadjen na mestu zlocina.", rs.getString("opis"))
        assertEquals(zr.idZrtva, rs.getInt("zrtvaId"))
        assertEquals(zlocin.idZlocin, rs.getInt("zlocinId"))
        assertEquals(0, rs.getInt("statusS"))

        assertTrue(rs.next(), "Treba da postoji jos jedan dokaz sa prosledjenim zlocinId-om")
        assertEquals(dokaz2.idDokaz, rs.getInt("idDokaz"))
        assertEquals("fizicki", rs.getString("tipDokaza"))
        assertEquals("Noz pronadjen na mestu zlocina.", rs.getString("opis"))
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
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.",
            zrtvaId = zr.idZrtva
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
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.",
            zrtvaId = zr.idZrtva
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

        if (osumnjicenGet != null) {
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
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.",
            zrtvaId = zr.idZrtva
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
        val obdukcijaNePostoji =repoGet.getObdukcija(zr.idZrtva+11)

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
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.",
            zrtvaId = zr.idZrtva
        )
        repo.insertForenzickiDokaz(dokaz,zr)

        val dokaz2 = ForenzickiDokazData(
            idForenzickiDokaz = 2,
            tipForenzickiDokaz = "DNK",
            opis = "DNK tragovi pronađeni na nozu",
            statusS = 0,
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.",
            zrtvaId = zr.idZrtva
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
            ime = "Giulio Romano",
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

    @Test
    fun testInsertOneCallData(){
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

        val oneContact = OneContactData(
            idOneContact = 1,
            zlocinId = zlocin.idZlocin,
            ime = "Alessandro Moretti",
            broj = "+393312223344",
            slika = 1
        )
        repo.insertOneContactData(oneContact, zlocin)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val oneCall = OneCallData(
            idOneCall = 1,
            kontakt = oneContact.idOneContact,
            datum = timestamp2,
            propusten = false,
            dolazni = true,
            zrtvaId = zrtva.idZrtva
        )
        repo.insertOneCallData(oneCall)

        val stmt = connection.prepareStatement("SELECT * FROM onecall WHERE idOneCall=?")
        stmt.setInt(1, oneCall.idOneCall)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji oneCall sa prosledjenim id-jem")
        assertEquals(oneCall.idOneCall, rs.getInt("idOneCall"))
        assertEquals(oneCall.kontakt, rs.getInt("kontakt"))
        val storedTimestamp = rs.getTimestamp("datum").time
        assertTrue(abs(storedTimestamp - oneCall.datum) < 1000)
        assertEquals(oneCall.propusten, rs.getBoolean("propusten"))
        assertEquals(oneCall.dolazni, rs.getBoolean("dolazni"))
        assertEquals(oneCall.zrtvaId, rs.getInt("zrtvaId"))
    }

    @Test
    fun testInsertGalleryData(){
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

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val gallery = GalleryData(
            idPhoto = 1,
            zlocinId = zlocin.idZlocin,
            slika = 1,
            datum = timestamp2,
            mesto = "Amsterdam"
        )
        repo.insertGalleryData(gallery, zlocin)

        val stmt = connection.prepareStatement("SELECT * FROM gallery WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji gallery sa prosledjenim id-jem zlocina")
        assertEquals(gallery.idPhoto, rs.getInt("idPhoto"))
        assertEquals(gallery.zlocinId, rs.getInt("zlocinId"))
        assertEquals(gallery.slika, rs.getInt("slika"))
        val storedTimestamp = rs.getTimestamp("datum").time
        assertTrue(abs(storedTimestamp - gallery.datum) < 1000)
        assertEquals(gallery.mesto, rs.getString("mesto"))
    }

    @Test
    fun testInsertObicnaPorukaData(){
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

        val kontaktKoSalje = OneContactData(
            idOneContact = 1,
            zlocinId = zlocin.idZlocin,
            ime = "Alessandro Moretti",
            broj = "+393312223344",
            slika = 1
        )
        repo.insertOneContactData(kontaktKoSalje, zlocin)

        val kontaktKomeSalje = OneContactData(
            idOneContact = 2,
            zlocinId = zlocin.idZlocin,
            ime = "Giulio Romano",
            broj = "+393316665555",
            slika = 2
        )
        repo.insertOneContactData(kontaktKomeSalje, zlocin)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val obicnaPoruka = ObicnaPorukaData(
            idObicnaPoruka = 1,
            kontaktKoSalje = kontaktKoSalje.idOneContact,
            kontaktKomeSalje = kontaktKomeSalje.idOneContact,
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.",
            datum = timestamp2,
            procitana = false
        )
        repo.insertObicnaPorukaData(obicnaPoruka, kontaktKoSalje, kontaktKomeSalje)

        val stmt = connection.prepareStatement("SELECT * FROM obicnaporuka WHERE idObicnaPoruka=?")
        stmt.setInt(1, 1)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji obicnaporuka sa prosledjenim id-jem")
        assertEquals(obicnaPoruka.idObicnaPoruka, rs.getInt("idObicnaPoruka"))
        assertEquals(obicnaPoruka.kontaktKoSalje, rs.getInt("kontaktKoSalje"))
        assertEquals(obicnaPoruka.kontaktKomeSalje, rs.getInt("kontaktKomeSalje"))
        assertEquals(obicnaPoruka.tekst, rs.getString("tekst"))
        val storedTimestamp = rs.getTimestamp("datum").time
        assertTrue(abs(storedTimestamp - obicnaPoruka.datum) < 1000)
        assertEquals(obicnaPoruka.procitana, rs.getBoolean("procitana"))
    }

    @Test
    fun testInsertOdnosOsumnjicenZrtvaData(){
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

        val motiv = MotivData(
            idMotiv = 1,
            opis = "Rivalstvo"
        )
        repo.insertMotivData(motiv)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = motiv,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )
        repo.insertOsumnjicenData(osumnjicen, zlocin, motiv)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba1
        )
        repo.insertZrtva(zrtva, zlocin, osoba1)
        
        val odnosOsumnjicenZrtva = OdnosOsumnjicenZrtvaData(
            idOdnos = 1,
            osumnjicenId = osumnjicen.idOsumnjicen,
            zrtvaId = zrtva.idZrtva,
            tipOdnosa = "rivalski"
        )
        repo.insertOdnosOsumnjicenZrtvaData(odnosOsumnjicenZrtva, osumnjicen, zrtva)

        val stmt = connection.prepareStatement("SELECT * FROM odnososumnjicenzrtva WHERE osumnjicenId=? AND zrtvaId=?")
        stmt.setInt(1, osumnjicen.idOsumnjicen)
        stmt.setInt(2, zrtva.idZrtva)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji odnososumnjicenzrtva sa prosledjenim id-jem osumnjicenog i zrtve")
        assertEquals(odnosOsumnjicenZrtva.idOdnos, rs.getInt("idOdnos"))
        assertEquals(odnosOsumnjicenZrtva.osumnjicenId, rs.getInt("osumnjicenId"))
        assertEquals(odnosOsumnjicenZrtva.zrtvaId, rs.getInt("zrtvaId"))
        assertEquals(odnosOsumnjicenZrtva.tipOdnosa, rs.getString("tipOdnosa"))
    }

    @Test
    fun testInsertPrijavljeniKorisnikData(){
        val repo = RepositoryInsert(connection)

        val prijavljeniKorisnik = PrijavljeniKorisnikData(
            idKorisnik = 1,
            korisnickoIme = "korisnickoIme",
            sifra = "sifra"
        )
        repo.insertPrijavljeniKorisnikData(prijavljeniKorisnik)

        val stmt = connection.prepareStatement("SELECT * FROM prijavljenikorisnik WHERE idKorisnik=?")
        stmt.setInt(1, prijavljeniKorisnik.idKorisnik)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji prijavljenikorisnik sa prosledjenim id-jem")
        assertEquals(prijavljeniKorisnik.idKorisnik, rs.getInt("idKorisnik"))
        assertEquals(prijavljeniKorisnik.korisnickoIme, rs.getString("korisnickoIme"))
        assertEquals(prijavljeniKorisnik.sifra, rs.getString("sifra"))
    }

    @Test
    fun testInsertPitanjeData(){
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

        val pitanje = PitanjeData(
            idPitanje = 1,
            zlocinId = zlocin.idZlocin,
            tekst = "Da li si otisao do njene sobe?"
        )
        repo.insertPitanjeData(pitanje, zlocin)

        val stmt = connection.prepareStatement("SELECT * FROM pitanje WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji pitanje sa prosledjenim id-jem zlocina")
        assertEquals(pitanje.idPitanje, rs.getInt("idPitanje"))
        assertEquals(pitanje.zlocinId, rs.getInt("zlocinId"))
        assertEquals(pitanje.tekst, rs.getString("tekst"))
    }

    @Test
    fun testInsertOdgovorData(){
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

        val pitanje = PitanjeData(
            idPitanje = 1,
            zlocinId = zlocin.idZlocin,
            tekst = "Da li si stvarno otisao do njene sobe?"
        )
        repo.insertPitanjeData(pitanje, zlocin)

        val odgovor = OdgovorData(
            idOdogovor = 1,
            pitanjeId = pitanje.idPitanje,
            tekstOdgovora = "Jesam, kaze da nije umesana.",
            tacan = false,
            bodovi = 10
        )
        repo.insertOdgovorData(odgovor, pitanje)

        val stmt = connection.prepareStatement("SELECT * FROM odgovor WHERE idOdogovor=?")
        stmt.setInt(1, odgovor.idOdogovor)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji odgovor sa prosledjenim id-jem")
        assertEquals(odgovor.idOdogovor, rs.getInt("idOdogovor"))
        assertEquals(odgovor.pitanjeId, rs.getInt("pitanjeId"))
        assertEquals(odgovor.tekstOdgovora, rs.getString("tekstOdgovora"))
        assertEquals(odgovor.tacan, rs.getBoolean("tacan"))
        assertEquals(odgovor.bodovi, rs.getInt("bodovi"))
    }

    @Test
    fun testInsertPitanjeIspitivanjeOsumnjicenogData(){
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

        val motiv = MotivData(
            idMotiv = 1,
            opis = "Rivalstvo"
        )
        repo.insertMotivData(motiv)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = motiv,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )
        repo.insertOsumnjicenData(osumnjicen, zlocin, motiv)
        
        val pitanjeIspitivanjeOsumnjicenog = PitanjeIspitivanjeOsumnjicenogData(
            idPitanjeIspitivanjeOsumnjicenog = 1,
            kategorija = "alibi",
            tekst = "Zasto ste bili u sobi zrtve?",
            odgovor = "Samo sam mu doneo kofer. Otisao sam odmah.",
            komentar = "Nije pomenuo sadrzaj kofera ni zasto bas on donosi. Moguce da prikriva pravi razlog dolaska.",
            osumnjicenId = osumnjicen.idOsumnjicen
        )
        repo.insertPitanjeIspitivanjeOsumnjicenogData(pitanjeIspitivanjeOsumnjicenog, osumnjicen)

        val stmt = connection.prepareStatement("SELECT * FROM pitanjeispitivanjeosumnjicenog WHERE osumnjicenId=?")
        stmt.setInt(1, osumnjicen.idOsumnjicen)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji pitanjeispitivanjeosumnjicenog sa prosledjenim id-jem osumnjicenog")
        assertEquals(pitanjeIspitivanjeOsumnjicenog.idPitanjeIspitivanjeOsumnjicenog, rs.getInt("idPitanjeIspitivanjeOsumnjicenog"))
        assertEquals(pitanjeIspitivanjeOsumnjicenog.kategorija, rs.getString("kategorija"))
        assertEquals(pitanjeIspitivanjeOsumnjicenog.tekst, rs.getString("tekst"))
        assertEquals(pitanjeIspitivanjeOsumnjicenog.odgovor, rs.getString("odgovor"))
        assertEquals(pitanjeIspitivanjeOsumnjicenog.komentar, rs.getString("komentar"))
        assertEquals(pitanjeIspitivanjeOsumnjicenog.osumnjicenId, rs.getInt("osumnjicenId"))
    }

    @Test
    fun testInsertGetUpdatePitanjeIspitivanjeSvedokaData() {
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val svedok = SvedokData(
            idSvedok = 1,
            izjava = "Video sam tog muskarca kako izlazi iz sobe oko pola dva ujutru. Delovao je nervozno i stalno se osvrtao.",
            statusSvedok = "aktivno",
            statusIspitan = 0,
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertSvedokData(svedok,zlocin)

        val pitanja = listOf(
            PitanjeIspitivanjeSvedokaData(
                idPitanjeIspitivanjeSvedoka = 1,
                tekst = "Jeste li sigurni da je to bio bas taj muskarac?",
                odgovor = "Da, prepoznao sam ga – imao je crvenu jaknu i hodao je sepajući.",
                svedokId = svedok.idSvedok,
                nextPitanje = 0
            ),
            PitanjeIspitivanjeSvedokaData(
                idPitanjeIspitivanjeSvedoka = 2,
                tekst = "Da li ste primetili još nešto neobično u tom trenutku?",
                odgovor = "Da, mislim da je u ruci držao nešto poput kožne torbe.",
                svedokId = svedok.idSvedok,
                nextPitanje = 0
            ),
            PitanjeIspitivanjeSvedokaData(
                idPitanjeIspitivanjeSvedoka = 3,
                tekst = "Da li ste videli da li je neko drugi ulazio ili izlazio iz sobe te noći?",
                odgovor = "Pre toga sam video ženu sa šeširom kako ulazi, ali nisam video da je izašla.",
                svedokId = svedok.idSvedok,
                nextPitanje = 0
            ),
        )

        for (pitanje in pitanja) {
            repo.insertPitanjeIspitivanjeSvedokaData(pitanje, svedok)
        }

        // da li su sva pitanja ucitana
        val ucitanaPitanja = repo.getPitanjeIspitivanjeSvedokaListData().filter { it.svedokId == svedok.idSvedok }
        assertEquals(3, ucitanaPitanja.size)

        // azuriranje nextZadatak polja

        repo.updatePitanjeIspitivanjeSvedokaListData(pitanja, svedok)

        // provera da li je nextZadatak dobro ucitan
        val updatedPitanja = repo.getPitanjeIspitivanjeSvedokaListData().filter { it.svedokId == svedok.idSvedok }
        assertEquals(updatedPitanja[0].nextPitanje, updatedPitanja[1].idPitanjeIspitivanjeSvedoka)
        assertEquals(updatedPitanja[1].nextPitanje, updatedPitanja[2].idPitanjeIspitivanjeSvedoka)
        assertEquals(updatedPitanja[2].nextPitanje, 0)

        val stmt = connection.prepareStatement("SELECT * FROM pitanjeispitivanjesvedoka WHERE svedokId=? ORDER BY idPitanjeIspitivanjeSvedoka")
        stmt.setInt(1, svedok.idSvedok)
        val rs = stmt.executeQuery()

        var count = 0
        while (rs.next()) {
            count++
            if (count == 1) {
                assertEquals(updatedPitanja[0].idPitanjeIspitivanjeSvedoka, rs.getInt("idPitanjeIspitivanjeSvedoka"))
                assertEquals(updatedPitanja[0].tekst, rs.getString("tekst"))
                assertEquals(updatedPitanja[0].odgovor, rs.getString("odgovor"))
                assertEquals(updatedPitanja[0].svedokId, rs.getInt("svedokId"))
                assertEquals(updatedPitanja[1].idPitanjeIspitivanjeSvedoka, rs.getInt("nextPitanje"))
            }
            if (count == 2) {
                assertEquals(updatedPitanja[1].idPitanjeIspitivanjeSvedoka, rs.getInt("idPitanjeIspitivanjeSvedoka"))
                assertEquals(updatedPitanja[1].tekst, rs.getString("tekst"))
                assertEquals(updatedPitanja[1].odgovor, rs.getString("odgovor"))
                assertEquals(updatedPitanja[1].svedokId, rs.getInt("svedokId"))
                assertEquals(updatedPitanja[2].idPitanjeIspitivanjeSvedoka, rs.getInt("nextPitanje"))
            }
            if (count == 3) {
                assertEquals(updatedPitanja[2].idPitanjeIspitivanjeSvedoka, rs.getInt("idPitanjeIspitivanjeSvedoka"))
                assertEquals(updatedPitanja[2].tekst, rs.getString("tekst"))
                assertEquals(updatedPitanja[2].odgovor, rs.getString("odgovor"))
                assertEquals(updatedPitanja[2].svedokId, rs.getInt("svedokId"))
                assertEquals(0, rs.getInt("nextPitanje"))
            }
        }
        assertEquals(3, count)
    }

    @Test
    fun testInsertGetUpdateZadatakData() {
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

        val zadaci = listOf(
            ZadatakData(
                idZadatak = 1,
                tekst = "Pronadji karticu gosta",
                korak = "korak_1",
                uradjen = false,
                nextZadatak = null,
                zlocinId = zlocin.idZlocin
            ),
            ZadatakData(
                idZadatak = 2,
                tekst = "Pregledaj snimke kamera",
                korak = "korak_2",
                uradjen = false,
                nextZadatak = null,
                zlocinId = zlocin.idZlocin
            ),
            ZadatakData(
                idZadatak = 2,
                tekst = "Ispitaj osoblje",
                korak = "korak_3",
                uradjen = false,
                nextZadatak = null,
                zlocinId = zlocin.idZlocin
            )
        )

        for (zadatak in zadaci) {
            repo.insertZadatakData(zadatak, zlocin)
        }

        // da li su svi zadaci ucitani
        val ucitaniZadaci = repo.getZadatakListaData().filter { it.zlocinId == zlocin.idZlocin }
        assertEquals(3, ucitaniZadaci.size)

        // azuriranje nextZadatak polja
        repo.updateZadatakListData(ucitaniZadaci, zlocin)

        // provera da li je nextZadatak dobro ucitan
        val updatedZadaci = repo.getZadatakListaData().filter { it.zlocinId == zlocin.idZlocin }
        assertEquals(updatedZadaci[0].nextZadatak, updatedZadaci[1].idZadatak)
        assertEquals(updatedZadaci[1].nextZadatak, updatedZadaci[2].idZadatak)
        assertEquals(updatedZadaci[2].nextZadatak, 0)

        val stmt = connection.prepareStatement("SELECT * FROM zadatak WHERE zlocinId=? ORDER BY idZadatak")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        var count = 0
        while (rs.next()) {
            count++
            if (count == 1) {
                assertEquals(updatedZadaci[0].idZadatak, rs.getInt("idZadatak"))
                assertEquals(updatedZadaci[0].tekst, rs.getString("tekst"))
                assertEquals(updatedZadaci[0].korak, rs.getString("korak"))
                assertEquals(updatedZadaci[0].uradjen, rs.getBoolean("uradjen"))
                assertEquals(updatedZadaci[1].idZadatak, rs.getInt("nextZadatak"))
                assertEquals(updatedZadaci[0].zlocinId, rs.getInt("zlocinId"))
            }
            if (count == 2) {
                assertEquals(updatedZadaci[1].idZadatak, rs.getInt("idZadatak"))
                assertEquals(updatedZadaci[1].tekst, rs.getString("tekst"))
                assertEquals(updatedZadaci[1].korak, rs.getString("korak"))
                assertEquals(updatedZadaci[1].uradjen, rs.getBoolean("uradjen"))
                assertEquals(updatedZadaci[2].idZadatak, rs.getInt("nextZadatak"))
                assertEquals(updatedZadaci[1].zlocinId, rs.getInt("zlocinId"))
            }
            if (count == 3) {
                assertEquals(updatedZadaci[2].idZadatak, rs.getInt("idZadatak"))
                assertEquals(updatedZadaci[2].tekst, rs.getString("tekst"))
                assertEquals(updatedZadaci[2].korak, rs.getString("korak"))
                assertEquals(updatedZadaci[2].uradjen, rs.getBoolean("uradjen"))
                assertEquals(0, rs.getInt("nextZadatak"))
                assertEquals(updatedZadaci[2].zlocinId, rs.getInt("zlocinId"))
            }
        }
        assertEquals(3, count)
    }

    @Test
    fun testInsertDokazZadatakData(){
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val dokaz = DokazData(
            idDokaz = 1,
            tipDokaza = "fizicki",
            opis = "Crna kozna rukavica pronadjena ispod kreveta u hotelskoj sobi. Na unutrasnjem delu pronadjeni tragovi pudera i duga svetla vlas. Ne pripada zrtvi. DNK analiza u toku.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zrtva.idZrtva,
            status = 0
        )
        repo.insertDokazData(dokaz, zlocin, zrtva)

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Posalji koznu rukavicu na forenzicku analizu radi identifikacije DNK i porekla vlasi.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)

        val dokazZadatak = DokazZadatakData(
            idDokazZadatak = 1,
            tekst = "Posalji dokaz na forenzicku analizu",
            dokazId = dokaz.idDokaz,
            uradjen = false,
            zadatakId = zadatak.idZadatak
        )
        repo.insertDokazZadatakData(dokazZadatak, dokaz, zadatak)

        val stmt = connection.prepareStatement("SELECT * FROM dokazzadatak WHERE dokazId=?")
        stmt.setInt(1, dokaz.idDokaz)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji dokazzadatak sa prosledjenim id-jem dokaza")
        assertEquals(dokazZadatak.idDokazZadatak, rs.getInt("idDokazZadatak"))
        assertEquals(dokazZadatak.tekst, rs.getString("tekst"))
        assertEquals(dokazZadatak.dokazId, rs.getInt("dokazId"))
        assertEquals(dokazZadatak.uradjen, rs.getBoolean("uradjen"))
        assertEquals(dokazZadatak.zadatakId, rs.getInt("zadatakId"))
    }

    @Test
    fun testInsertIspitivanjeOsumnjicenogZadatakData(){
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

        val motiv = MotivData(
            idMotiv = 1,
            opis = "Rivalstvo"
        )
        repo.insertMotivData(motiv)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = motiv,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )
        repo.insertOsumnjicenData(osumnjicen, zlocin, motiv)

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Ispitaj osumnjicenog gde je bio za vreme ubistva.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)

        val ispitivanjeOsumnjicenogZadatak = IspitivanjeOsumnjicenogZadatakData(
            idIspitivanjeOsumnjicenogZadatak = 1,
            osumnjicenId = osumnjicen.idOsumnjicen,
            zadatakId = zadatak.idZadatak,
            uradjen = true
        )
        repo.insertIspitivanjeOsumnjicenogZadatakData(ispitivanjeOsumnjicenogZadatak, osumnjicen, zadatak)

        val stmt = connection.prepareStatement("SELECT * FROM ispitivanjeosumnjicenogzadatak WHERE osumnjicenId=?")
        stmt.setInt(1, osumnjicen.idOsumnjicen)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji ispitivanjeosumnjicenogzadatak sa prosledjenim id-jem osumnjicenog")
        assertEquals(ispitivanjeOsumnjicenogZadatak.idIspitivanjeOsumnjicenogZadatak, rs.getInt("idIspitivanjeOsumnjicenogZadatak"))
        assertEquals(ispitivanjeOsumnjicenogZadatak.osumnjicenId, rs.getInt("osumnjicenId"))
        assertEquals(ispitivanjeOsumnjicenogZadatak.zadatakId, rs.getInt("zadatakId"))
        assertEquals(ispitivanjeOsumnjicenogZadatak.uradjen, rs.getBoolean("uradjen"))
    }

    @Test
    fun testInsertIspitivanjeSvedokaZadatakData(){
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

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val svedok = SvedokData(
            idSvedok = 1,
            izjava = "Video sam tog muskarca kako izlazi iz sobe oko pola dva ujutru. Delovao je nervozno i stalno se osvrtao.",
            statusSvedok = "aktivno",
            statusIspitan = 0,
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertSvedokData(svedok,zlocin)

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Ispitaj sta kaze svedok.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)

        val ispitivanjeSvedokaZadatak = IspitivanjeSvedokaZadatakData(
            idIspitivanjeSvedokaZadatak = 1,
            svedokId = svedok.idSvedok,
            zadatakId = zadatak.idZadatak,
            uradjen = false
        )
        repo.insertIspitivanjeSvedokaZadatakData(ispitivanjeSvedokaZadatak, svedok, zadatak)

        val stmt = connection.prepareStatement("SELECT * FROM ispitivanjesvedokazadatak WHERE svedokId=?")
        stmt.setInt(1, svedok.idSvedok)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji ispitivanjesvedokazadatak sa prosledjenim id-jem svedoka")
        assertEquals(ispitivanjeSvedokaZadatak.idIspitivanjeSvedokaZadatak, rs.getInt("idIspitivanjeSvedokaZadatak"))
        assertEquals(ispitivanjeSvedokaZadatak.svedokId, rs.getInt("svedokId"))
        assertEquals(ispitivanjeSvedokaZadatak.zadatakId, rs.getInt("zadatakId"))
        assertEquals(ispitivanjeSvedokaZadatak.uradjen, rs.getBoolean("uradjen"))
    }

    @Test
    fun testInsertTelefonZadatakData(){
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)
        
        val telefon = TelefonData(
            idTelefon = 1,
            model = "iPhone 13 Pro",
            os = "IOS",
            sifra = "1234",
            informacije = "Telefon je pronadjen na nocnom stociću pored tela. Nije bio zakljucan. Poslednji poziv upućen zeni ciji identitet još nije potvrdjen. Poruka „Stizes li?” poslata u 01:08h, 20 minuta pre procenjenog vremena smrti.",
            zrtvaId = zrtva.idZrtva
        )
        repo.insertTelefonData(telefon, zrtva)

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Otkrij ko je misteriozna zena.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)

        val telefonZadatak = TelefonZadatakData(
            idTelefonZadatak = 1,
            telefonId = telefon.idTelefon,
            zadatakId = zadatak.idZadatak,
            uradjen = true
        )
        repo.insertTelefonZadatakData(telefonZadatak, telefon, zadatak)

        val stmt = connection.prepareStatement("SELECT * FROM telefonzadatak WHERE telefonId=?")
        stmt.setInt(1, telefon.idTelefon)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji telefonzadatak sa prosledjenim id-jem telefona")
        assertEquals(telefonZadatak.idTelefonZadatak, rs.getInt("idTelefonZadatak"))
        assertEquals(telefonZadatak.telefonId, rs.getInt("telefonId"))
        assertEquals(telefonZadatak.zadatakId, rs.getInt("zadatakId"))
        assertEquals(telefonZadatak.uradjen, rs.getBoolean("uradjen"))
    }

    @Test
    fun testInsertForenzickiDokazZadatakData(){
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)
        
        val forenzickiDokaz = ForenzickiDokazData(
            idForenzickiDokaz = 1,
            tipForenzickiDokaz = "DNK",
            opis = "Analiza DNK uzorka sa krvavog noza",
            statusS = 0,
            veza = "DNK tragovi na nozu pripadaju zenskoj osobi.",
            zrtvaId = zrtva.idZrtva
        )
        repo.insertForenzickiDokaz(forenzickiDokaz, zrtva)

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Otkrij kojoj zeni pripada DNK.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)

        val forenzickiDokazZadatak = ForenzickiDokazZadatakData(
            idForenzickiDokazZadatak = 1,
            tekst = "Otkrij kojoj zeni pripada DNK.",
            forenzickiDokazId = forenzickiDokaz.idForenzickiDokaz,
            uradjen = true,
            zadatakId = zadatak.idZadatak
        )
        repo.insertForenzickiDokazZadatakData(forenzickiDokazZadatak, forenzickiDokaz, zadatak)

        val stmt = connection.prepareStatement("SELECT * FROM forenzickidokazzadatak WHERE forenzickiDokazId=?")
        stmt.setInt(1, forenzickiDokaz.idForenzickiDokaz)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji forenzickidokazzadatak sa prosledjenim id-jem forenzickog dokaza")
        assertEquals(forenzickiDokazZadatak.idForenzickiDokazZadatak, rs.getInt("idForenzickiDokazZadatak"))
        assertEquals(forenzickiDokazZadatak.tekst, rs.getString("tekst"))
        assertEquals(forenzickiDokazZadatak.forenzickiDokazId, rs.getInt("forenzickiDokazId"))
        assertEquals(forenzickiDokazZadatak.zadatakId, rs.getInt("zadatakId"))
        assertEquals(forenzickiDokazZadatak.uradjen, rs.getBoolean("uradjen"))
    }

    @Test
    fun testInsertPacijentData(){
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)
        
        val pacijent = PacijentData(
            idPacijent = 1,
            simptomi = "Pacijent se zali na povisenu temperaturu, mucninu i povracanje, uz primetno bledilo i ubrzan puls. Takodje pokazuje znakove mentalne konfuzije i teskoce sa koncentracijom od jutra nakon prijema.",
            statusPacijenta = "ziva",
            datumPrijave = timestamp2,
            prijavio = osoba1,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(pacijent)

        val stmt = connection.prepareStatement("SELECT * FROM pacijent WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji pacijent sa prosledjenim id-jem zlocina")
        assertEquals(pacijent.idPacijent, rs.getInt("idPacijent"))
        assertEquals(pacijent.simptomi, rs.getString("simptomi"))
        assertEquals(pacijent.statusPacijenta, rs.getString("statusPacijenta"))
        val storedTimestamp = rs.getTimestamp("datumPrijave").time
        assertTrue(abs(storedTimestamp - pacijent.datumPrijave) < 1000)
        assertEquals(pacijent.prijavio.idOsoba, rs.getInt("prijavio"))
        assertEquals(pacijent.zlocinId.idZlocin, rs.getInt("zlocinId"))
        assertEquals(pacijent.zrtvaId.idZrtva, rs.getInt("zrtvaId"))
    }

    @Test
    fun testInsertMedicinskiIzvestajData(){
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)

        val pacijent = PacijentData(
            idPacijent = 1,
            simptomi = "Pacijent se zali na povisenu temperaturu, mucninu i povracanje, uz primetno bledilo i ubrzan puls. Takodje pokazuje znakove mentalne konfuzije i teskoce sa koncentracijom od jutra nakon prijema.",
            statusPacijenta = "ziva",
            datumPrijave = timestamp2,
            prijavio = osoba1,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(pacijent)
        
        val medicinskiIzvestaj = MedicinskiIzvestajData(
            idMedicinskiIzvestaj = 1,
            rezime = "Pacijent primljen sa visokom temperaturom, mucninom, povracanjem i znacima mentalne konfuzije.",
            CTnalaz = "Bez znakova intrakranijalnog krvarenja. Blaga cerebralna edematoznost.",
            MRInalaz = "Ne prikazuje strukturalne abnormalnosti. Promene u hipokampusu moguce uzrokovane toksinima.",
            krvnaSlika = "Poviseni leukociti, smanjeni eritrociti. Znaci upalnog odgovora organizma.",
            toksikoloskeAnalize = "Detektovani tragovi pesticida – mogucnost trovanja.",
            zakljucak = "Simptomi i analize ukazuju na moguce akutno trovanje. Preporucena hospitalizacija i dalja toksikoloska ispitivanja.",
            pacijentId = pacijent
        )
        repo.insertMedicinskiIzvestajData(medicinskiIzvestaj)

        val stmt = connection.prepareStatement("SELECT * FROM medicinskiizvestaj WHERE pacijentId=?")
        stmt.setInt(1, pacijent.idPacijent)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji medicinskiizvestaj sa prosledjenim id-jem pacijenta")
        assertEquals(medicinskiIzvestaj.idMedicinskiIzvestaj, rs.getInt("idMedicinskiIzvestaj"))
        assertEquals(medicinskiIzvestaj.rezime, rs.getString("rezime"))
        assertEquals(medicinskiIzvestaj.CTnalaz, rs.getString("CTnalaz"))
        assertEquals(medicinskiIzvestaj.MRInalaz, rs.getString("MRInalaz"))
        assertEquals(medicinskiIzvestaj.krvnaSlika, rs.getString("krvnaSlika"))
        assertEquals(medicinskiIzvestaj.toksikoloskeAnalize, rs.getString("toksikoloskeAnalize"))
        assertEquals(medicinskiIzvestaj.zakljucak, rs.getString("zakljucak"))
        assertEquals(medicinskiIzvestaj.pacijentId.idPacijent, rs.getInt("pacijentId"))
    }

    @Test
    fun testInsertLekarskiTestData(){
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)

        val pacijent = PacijentData(
            idPacijent = 1,
            simptomi = "Pacijent se zali na povisenu temperaturu, mucninu i povracanje, uz primetno bledilo i ubrzan puls. Takodje pokazuje znakove mentalne konfuzije i teskoce sa koncentracijom od jutra nakon prijema.",
            statusPacijenta = "ziva",
            datumPrijave = timestamp2,
            prijavio = osoba1,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(pacijent)

        val lekarskiTest = LekarskiTestData(
            idLekarskiTest = 1,
            pacijentId = pacijent,
            izvestaj = "Obavljen je inicijalni neuroloski pregled koji je pokazao usporene reflekse i dezorijentaciju u vremenu. Takodje je uradjen test koordinacije pokreta sa slabim rezultatima."
        )
        repo.insertLekarskiTestData(lekarskiTest)

        val stmt = connection.prepareStatement("SELECT * FROM lekarskitest WHERE pacijentId=?")
        stmt.setInt(1, pacijent.idPacijent)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji lekarskitest sa prosledjenim id-jem pacijenta")
        assertEquals(lekarskiTest.idLekarskiTest, rs.getInt("idLekarskiTest"))
        assertEquals(lekarskiTest.pacijentId.idPacijent, rs.getInt("pacijentId"))
        assertEquals(lekarskiTest.izvestaj, rs.getString("izjava"))
    }

    @Test
    fun testInsertLokacijeIstrageData(){
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

        val lokacijeIstrage = LokacijeIstrageData(
            idLokacijeIstrage = 1,
            mesto = "Amsterdam",
            naziv = "De Wallen",
            opis = "Potencijalno mesto incidenta zbog svedocenja prolaznika.",
            zlocinId = zlocin.idZlocin,
            geoTackaALatitude = 52.3740,
            geoTackaALongitude = 4.9000
        )
        repo.insertLokacijeIstrageData(lokacijeIstrage)

        val stmt = connection.prepareStatement("SELECT * FROM lokacijeistrage WHERE zlocinId=?")
        stmt.setInt(1, zlocin.idZlocin)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji lokacijeistrage sa prosledjenim id-jem zlocina")
        assertEquals(lokacijeIstrage.idLokacijeIstrage, rs.getInt("idLokacijeIstrage"))
        assertEquals(lokacijeIstrage.mesto, rs.getString("mesto"))
        assertEquals(lokacijeIstrage.naziv, rs.getString("naziv"))
        assertEquals(lokacijeIstrage.opis, rs.getString("opis"))
        assertEquals(lokacijeIstrage.zlocinId, rs.getInt("zlocinId"))
        assertEquals(lokacijeIstrage.geoTackaALatitude, rs.getDouble("geoTackaALatitude"))
        assertEquals(lokacijeIstrage.geoTackaALongitude, rs.getDouble("geoTackaALongitude"))
    }

    @Test
    fun testInsertIzjavaZaPacijentaData(){
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)

        val pacijent = PacijentData(
            idPacijent = 1,
            simptomi = "Pacijent se zali na povisenu temperaturu, mucninu i povracanje, uz primetno bledilo i ubrzan puls. Takodje pokazuje znakove mentalne konfuzije i teskoce sa koncentracijom od jutra nakon prijema.",
            statusPacijenta = "ziva",
            datumPrijave = timestamp2,
            prijavio = osoba1,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(pacijent)

        val osoba2 = OsobaData(
            idOsoba = 3,
            ime = "Sophie van Dijk",
            kontakt = "+31644556677",
            datum = timestamp2,
            zanimanje = "recepcionarka",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba2, zlocin)

        val izjavaZaPacijenta = IzjavaZaPacijentaData(
            idIzjavaZaPacijenta = 1,
            izjava = "Ujutru nakon incidenta, Giulio se ponasao cudno – delovao je dezorijentisano i zbunjeno. Rekao je da se ne seca šta se desilo prethodne noci, ali se zalio na mucninu i vrtoglavicu. Pomenuo je da je popio pice koje mu je donela nepoznata zena.",
            pacijentId = pacijent,
            osobaId = osoba2
        )
        repo.insertIzjavaZaPacijentaData(izjavaZaPacijenta, pacijent, osoba2)

        val stmt = connection.prepareStatement("SELECT * FROM izjavazapacijenta WHERE pacijentId=?")
        stmt.setInt(1, pacijent.idPacijent)
        val rs = stmt.executeQuery()

        assertTrue(rs.next(), "Treba da postoji izjavazapacijenta sa prosledjenim id-jem pacijenta")
        assertEquals(izjavaZaPacijenta.idIzjavaZaPacijenta, rs.getInt("idIzjavaZaPacijenta"))
        assertEquals(izjavaZaPacijenta.izjava, rs.getString("izjava"))
        assertEquals(izjavaZaPacijenta.pacijentId.idPacijent, rs.getInt("pacijentId"))
        assertEquals(izjavaZaPacijenta.osobaId.idOsoba, rs.getInt("osobaId"))
    }

    @Test
    fun testGetWhatsAppPoruka(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        var listaKontakata = mutableListOf<WhatsAppKontaktData>()

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
        listaKontakata.add(kontaktKoSalje)

        val kontaktKomeSalje = WhatsAppKontaktData(
            idWhatsAppKontakt = 2,
            zlocinId = zlocin.idZlocin,
            ime = "Giulio Romano",
            broj = "+393316665555",
            slika = 2
        )
        repo.insertWhatsAppKontaktData(kontaktKomeSalje, zlocin)
        listaKontakata.add(kontaktKomeSalje)

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

        val whatsAppPorukaGet = repoGet.getWhatsAppPoruka(zlocin.idZlocin, listaKontakata)
        val whatsAppPorukaNePostoji = repoGet.getWhatsAppPoruka(zlocin.idZlocin + 11, listaKontakata)

        assertTrue(whatsAppPorukaGet != emptyList<WhatsAppPorukaData>(), "Treba da postoje WhatsAppPorukaData.")
        assertTrue(whatsAppPorukaNePostoji == emptyList<WhatsAppPorukaData>(), "Treba da ne postoje WhatsAppPorukaData")

        if (whatsAppPorukaGet != null) {
            for(p in whatsAppPorukaGet){
                if (p.idWhatsAppPoruka == whatsAppPoruka.idWhatsAppPoruka){
                    assertEquals(p.idWhatsAppPoruka, whatsAppPoruka.idWhatsAppPoruka)
                    assertEquals(p.kontaktKoSalje, whatsAppPoruka.kontaktKoSalje)
                    assertEquals(p.kontaktKomeSalje, whatsAppPoruka.kontaktKomeSalje)
                    assertEquals(p.tekst, whatsAppPoruka.tekst)
                    assertEquals(p.datum, whatsAppPoruka.datum)
                    assertEquals(p.procitana, whatsAppPoruka.procitana)
                }
            }
        }
    }

    @Test
    fun testGetGallery(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val gallery = GalleryData(
            idPhoto = 1,
            zlocinId = zlocin.idZlocin,
            slika = 1,
            datum = timestamp2,
            mesto = "Amsterdam"
        )
        repo.insertGalleryData(gallery, zlocin)

        val galleryGet = repoGet.getGallery(zlocin.idZlocin)
        val galleryNePostoji = repoGet.getGallery(zlocin.idZlocin + 11)

        assertTrue(galleryGet != emptyList<GalleryData>(), "Treba da postoje GalleryData.")
        assertTrue(galleryNePostoji == emptyList<GalleryData>(), "Treba da ne postoje GalleryData")

        if (galleryGet != null) {
            for(g in galleryGet){
                if (g.idPhoto == gallery.idPhoto){
                    assertEquals(g.idPhoto, gallery.idPhoto)
                    assertEquals(g.zlocinId, gallery.zlocinId)
                    assertEquals(g.slika, gallery.slika)
                    assertTrue(abs(g.datum - gallery.datum) < 1000)
                    assertEquals(g.mesto, gallery.mesto)
                }
            }
        }
    }

    @Test
    fun testGetOdnosOsumnjicenZrtva(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val motiv = MotivData(
            idMotiv = 1,
            opis = "Rivalstvo"
        )
        repo.insertMotivData(motiv)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = motiv,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )
        repo.insertOsumnjicenData(osumnjicen, zlocin, motiv)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba1
        )
        repo.insertZrtva(zrtva, zlocin, osoba1)

        val odnosOsumnjicenZrtva = OdnosOsumnjicenZrtvaData(
            idOdnos = 1,
            osumnjicenId = osumnjicen.idOsumnjicen,
            zrtvaId = zrtva.idZrtva,
            tipOdnosa = "rivalski"
        )
        repo.insertOdnosOsumnjicenZrtvaData(odnosOsumnjicenZrtva, osumnjicen, zrtva)

        val odnosGet = repoGet.getOdnosOsumnjicenZrtva(zrtva.idZrtva)
        val odnosNePostoji = repoGet.getOdnosOsumnjicenZrtva(zrtva.idZrtva + 11)

        assertTrue(odnosGet != emptyList<OdnosOsumnjicenZrtvaData>(), "Treba da postoje OdnosOsumnjicenZrtvaData.")
        assertTrue(odnosNePostoji == emptyList<OdnosOsumnjicenZrtvaData>(), "Treba da ne postoje OdnosOsumnjicenZrtvaData")

        if (odnosGet != null) {
            for(o in odnosGet){
                if (o.idOdnos == odnosOsumnjicenZrtva.idOdnos){
                    assertEquals(o.idOdnos, odnosOsumnjicenZrtva.idOdnos)
                    assertEquals(o.osumnjicenId, odnosOsumnjicenZrtva.osumnjicenId)
                    assertEquals(o.zrtvaId, odnosOsumnjicenZrtva.zrtvaId)
                    assertEquals(o.tipOdnosa, odnosOsumnjicenZrtva.tipOdnosa)
                }
            }
        }
    }

    @Test
    fun testGetPitanja(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val pitanje = PitanjeData(
            idPitanje = 1,
            zlocinId = zlocin.idZlocin,
            tekst = "Da li si otisao do njene sobe?"
        )
        repo.insertPitanjeData(pitanje, zlocin)

        val pitanjeGet = repoGet.getPitanja(zlocin.idZlocin)
        val pitanjeNePostoji = repoGet.getPitanja(zlocin.idZlocin + 11)

        assertTrue(pitanjeGet != emptyList<PitanjeData>(), "Treba da postoje PitanjeData.")
        assertTrue(pitanjeNePostoji == emptyList<PitanjeData>(), "Treba da ne postoje PitanjeData")

        if (pitanjeGet != null) {
            for(p in pitanjeGet){
                if (p.idPitanje == pitanje.idPitanje){
                    assertEquals(pitanje.idPitanje, p.idPitanje)
                    assertEquals(pitanje.zlocinId, p.zlocinId)
                    assertEquals(pitanje.tekst, p.tekst)
                }
            }
        }
    }

    @Test
    fun testGetOdgovor(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val pitanje = PitanjeData(
            idPitanje = 1,
            zlocinId = zlocin.idZlocin,
            tekst = "Da li si stvarno otisao do njene sobe?"
        )
        repo.insertPitanjeData(pitanje, zlocin)

        val odgovor = OdgovorData(
            idOdogovor = 1,
            pitanjeId = pitanje.idPitanje,
            tekstOdgovora = "Jesam, kaze da nije umesana.",
            tacan = false,
            bodovi = 10
        )
        repo.insertOdgovorData(odgovor, pitanje)

        val odgovorGet = repoGet.getOdgovor(zlocin.idZlocin)
        val odgovorNePostoji = repoGet.getOdgovor(zlocin.idZlocin + 11)

        assertTrue(odgovorGet != emptyList<OdgovorData>(), "Treba da postoje OdgovorData.")
        assertTrue(odgovorNePostoji == emptyList<OdgovorData>(), "Treba da ne postoje OdgovorData")

        if (odgovorGet != null) {
            for(o in odgovorGet){
                if (o.idOdogovor == odgovor.idOdogovor){
                    assertEquals(odgovor.idOdogovor, o.idOdogovor)
                    assertEquals(odgovor.pitanjeId, o.pitanjeId)
                    assertEquals(odgovor.tekstOdgovora, o.tekstOdgovora)
                    assertEquals(odgovor.tacan, o.tacan)
                    assertEquals(odgovor.bodovi, o.bodovi)
                }
            }
        }
    }

    @Test
    fun testGetPitanjeIspitivanjeOsumnjicenog(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val motiv = MotivData(
            idMotiv = 1,
            opis = "Rivalstvo"
        )
        repo.insertMotivData(motiv)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = motiv,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )
        repo.insertOsumnjicenData(osumnjicen, zlocin, motiv)

        val pitanjeIspitivanjeOsumnjicenog = PitanjeIspitivanjeOsumnjicenogData(
            idPitanjeIspitivanjeOsumnjicenog = 1,
            kategorija = "alibi",
            tekst = "Zasto ste bili u sobi zrtve?",
            odgovor = "Samo sam mu doneo kofer. Otisao sam odmah.",
            komentar = "Nije pomenuo sadrzaj kofera ni zasto bas on donosi. Moguce da prikriva pravi razlog dolaska.",
            osumnjicenId = osumnjicen.idOsumnjicen
        )
        repo.insertPitanjeIspitivanjeOsumnjicenogData(pitanjeIspitivanjeOsumnjicenog, osumnjicen)

        val ispitivanjeGet = repoGet.getPitanjeIspitivanjeOsumnjicenog(osumnjicen.idOsumnjicen)
        val ispitivanjeNePostoji = repoGet.getPitanjeIspitivanjeOsumnjicenog(osumnjicen.idOsumnjicen + 11)

        assertTrue(ispitivanjeGet != emptyList<PitanjeIspitivanjeOsumnjicenogData>(), "Treba da postoje PitanjeIspitivanjeOsumnjicenogData.")
        assertTrue(ispitivanjeNePostoji == emptyList<PitanjeIspitivanjeOsumnjicenogData>(), "Treba da ne postoje PitanjeIspitivanjeOsumnjicenogData")

        if (ispitivanjeGet != null) {
            for(i in ispitivanjeGet){
                if (i.idPitanjeIspitivanjeOsumnjicenog == pitanjeIspitivanjeOsumnjicenog.idPitanjeIspitivanjeOsumnjicenog){
                    assertEquals(pitanjeIspitivanjeOsumnjicenog.idPitanjeIspitivanjeOsumnjicenog, i.idPitanjeIspitivanjeOsumnjicenog)
                    assertEquals(pitanjeIspitivanjeOsumnjicenog.kategorija, i.kategorija)
                    assertEquals(pitanjeIspitivanjeOsumnjicenog.tekst, i.tekst)
                    assertEquals(pitanjeIspitivanjeOsumnjicenog.odgovor, i.odgovor)
                    assertEquals(pitanjeIspitivanjeOsumnjicenog.komentar, i.komentar)
                    assertEquals(pitanjeIspitivanjeOsumnjicenog.osumnjicenId, i.osumnjicenId)
                }
            }
        }
    }

    @Test
    fun testGetPitanjeIspitivanjeSvedoka(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val svedok = SvedokData(
            idSvedok = 1,
            izjava = "Video sam tog muskarca kako izlazi iz sobe oko pola dva ujutru. Delovao je nervozno i stalno se osvrtao.",
            statusSvedok = "aktivno",
            statusIspitan = 0,
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertSvedokData(svedok,zlocin)

        val pitanjeIspitivanjeSvedoka = PitanjeIspitivanjeSvedokaData(
            idPitanjeIspitivanjeSvedoka = 1,
            tekst = "Jeste li sigurni da je to bio bas taj muskarac?",
            odgovor = "Da, prepoznao sam ga – imao je crvenu jaknu i hodao je sepajući.",
            svedokId = svedok.idSvedok,
            nextPitanje = 0
        )
        repo.insertPitanjeIspitivanjeSvedokaData(pitanjeIspitivanjeSvedoka, svedok)

        val ispitivanjeGet = repoGet.getPitanjeIspitivanjeSvedoka(svedok.idSvedok)
        val ispitivanjeNePostoji = repoGet.getPitanjeIspitivanjeSvedoka(svedok.idSvedok + 11)

        assertTrue(ispitivanjeGet != emptyList<PitanjeIspitivanjeSvedokaData>(), "Treba da postoje PitanjeIspitivanjeSvedokaData.")
        assertTrue(ispitivanjeNePostoji == emptyList<PitanjeIspitivanjeSvedokaData>(), "Treba da ne postoje PitanjeIspitivanjeSvedokaData")

        if (ispitivanjeGet != null) {
            for(i in ispitivanjeGet){
                if (i.idPitanjeIspitivanjeSvedoka == pitanjeIspitivanjeSvedoka.idPitanjeIspitivanjeSvedoka){
                    assertEquals(pitanjeIspitivanjeSvedoka.idPitanjeIspitivanjeSvedoka, i.idPitanjeIspitivanjeSvedoka)
                    assertEquals(pitanjeIspitivanjeSvedoka.tekst, i.tekst)
                    assertEquals(pitanjeIspitivanjeSvedoka.odgovor, i.odgovor)
                    assertEquals(pitanjeIspitivanjeSvedoka.svedokId, i.svedokId)
                    assertEquals(pitanjeIspitivanjeSvedoka.nextPitanje, i.nextPitanje)
                }
            }
        }
    }

    @Test
    fun testGetOsobe(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zlocin = ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u kuci",
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

        val osobaGet = repoGet.getOsobe(zlocin.idZlocin)
        val osobaNePostoji = repoGet.getOsobe(zlocin.idZlocin + 11)

        assertTrue(osobaGet != emptyList<OsobaData>(), "Treba da postoje OsobaData.")
        assertTrue(osobaNePostoji == emptyList<OsobaData>(), "Treba da ne postoje OsobaData")

        if (osobaGet != null) {
            for(o in osobaGet){
                if (o.idOsoba == osoba.idOsoba){
                    assertEquals(o.idOsoba, osoba.idOsoba)
                    assertEquals(o.ime, osoba.ime)
                    assertEquals(o.kontakt, osoba.kontakt)
                    assertTrue(abs(o.datum - osoba.datum) < 1000)
                    assertEquals(o.zanimanje, osoba.zanimanje)
                    assertEquals(o.pol, osoba.pol)
                    assertEquals(o.zlocinId, osoba.zlocinId)
                }
            }
        }
    }

    @Test
    fun testGetZadaci(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Pronadji karticu gosta",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = null,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)

        val zadaciGet = repoGet.getZadaci(zadatak.zlocinId)
        val zadaciNePostoji = repoGet.getZadaci(zadatak.zlocinId + 11)

        assertTrue(zadaciGet != emptyList<ZadatakData>(), "Treba da postoje ZadatakData.")
        assertTrue(zadaciNePostoji == emptyList<ZadatakData>(), "Treba da ne postoje ZadatakData")

        if (zadaciGet != null) {
            for(z in zadaciGet){
                if (z.idZadatak == zadatak.idZadatak){
                    assertEquals(z.idZadatak, zadatak.idZadatak)
                    assertEquals(z.tekst, zadatak.tekst)
                    assertEquals(z.korak, zadatak.korak)
                    assertEquals(z.uradjen, zadatak.uradjen)
                    assertEquals(z.nextZadatak, zadatak.nextZadatak)
                    assertEquals(z.zlocinId, zadatak.zlocinId)
                }
            }
        }
    }

    @Test
    fun testGetDokaziZadaci(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val dokaz = DokazData(
            idDokaz = 1,
            tipDokaza = "fizicki",
            opis = "Crna kozna rukavica pronadjena ispod kreveta u hotelskoj sobi. Na unutrasnjem delu pronadjeni tragovi pudera i duga svetla vlas. Ne pripada zrtvi. DNK analiza u toku.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zrtva.idZrtva,
            status = 0
        )
        repo.insertDokazData(dokaz, zlocin, zrtva)

        val zadaciList = mutableListOf<ZadatakData>()

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Posalji koznu rukavicu na forenzicku analizu radi identifikacije DNK i porekla vlasi.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 2,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)
        zadaciList.add(zadatak)

        val dokazZadatak = DokazZadatakData(
            idDokazZadatak = 1,
            tekst = "Posalji dokaz na forenzicku analizu",
            dokazId = dokaz.idDokaz,
            uradjen = false,
            zadatakId = zadatak.idZadatak
        )
        repo.insertDokazZadatakData(dokazZadatak, dokaz, zadatak)

        val dokaz2 = DokazData(
            idDokaz = 2,
            tipDokaza = "bioloski",
            opis = "DNK uzorak pronadjen na rukavici odgovara nepoznatoj zeni.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zrtva.idZrtva,
            status = 0
        )
        repo.insertDokazData(dokaz2, zlocin, zrtva)

        val zadatak2 = ZadatakData(
            idZadatak = 2,
            tekst = "Ispitaj osobu koja je poslednja vidjena sa zrtvom.",
            korak = "korak_2",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak2, zlocin)
        zadaciList.add(zadatak2)

        val dokazZadatak2 = DokazZadatakData(
            idDokazZadatak = 2,
            tekst = "Uporedi DNK sa bazom podataka osumnjicenih.",
            dokazId = dokaz2.idDokaz,
            uradjen = false,
            zadatakId = zadatak2.idZadatak
        )
        repo.insertDokazZadatakData(dokazZadatak2, dokaz2, zadatak2)

        val dokazZadatakGet = repoGet.getDokaziZadaci(zlocin.idZlocin, zadaciList)
        val dokazZadatakNePostoji = repoGet.getDokaziZadaci(zlocin.idZlocin + 11, zadaciList)

        assertTrue(dokazZadatakGet != emptyList<DokazZadatakData>(), "Treba da postoje DokazZadatakData.")
        assertTrue(dokazZadatakNePostoji == emptyList<DokazZadatakData>(), "Treba da ne postoje DokazZadatakData")

        val validniZadatakIdjevi = zadaciList.map { it.idZadatak }

        val ocekivaniDokazZadaci = listOf(dokazZadatak, dokazZadatak2)

        if (dokazZadatakGet != null) {
            for (d in dokazZadatakGet) {
                assertTrue(
                    d.zadatakId in validniZadatakIdjevi,
                    "DokazZadatakData sa zadatakId=${d.zadatakId} nije deo zadaciList — ne bi smeo biti u rezultatu."
                )
            }

            for (ocekivani in ocekivaniDokazZadaci) {
                val pronadjen = dokazZadatakGet.find { it.idDokazZadatak == ocekivani.idDokazZadatak }
                assertNotNull(pronadjen, "DokazZadatakData sa id=${ocekivani.idDokazZadatak} nije pronađen u rezultatu")

                pronadjen?.let {
                    assertEquals(ocekivani.idDokazZadatak, it.idDokazZadatak)
                    assertEquals(ocekivani.tekst, it.tekst)
                    assertEquals(ocekivani.dokazId, it.dokazId)
                    assertEquals(ocekivani.uradjen, it.uradjen)
                    assertEquals(ocekivani.zadatakId, it.zadatakId)
                }
            }
        }
    }

    @Test
    fun testGetIspitivanjeOsumnjicenogZadatak(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zadaciList = mutableListOf<ZadatakData>()

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

        val motiv = MotivData(
            idMotiv = 1,
            opis = "Rivalstvo"
        )
        repo.insertMotivData(motiv)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val osumnjicen = OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = motiv,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba
        )
        repo.insertOsumnjicenData(osumnjicen, zlocin, motiv)

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Ispitaj osumnjicenog gde je bio za vreme ubistva.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 2,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)
        zadaciList.add(zadatak)

        val ispitivanjeOsumnjicenogZadatak = IspitivanjeOsumnjicenogZadatakData(
            idIspitivanjeOsumnjicenogZadatak = 1,
            osumnjicenId = osumnjicen.idOsumnjicen,
            zadatakId = zadatak.idZadatak,
            uradjen = true
        )
        repo.insertIspitivanjeOsumnjicenogZadatakData(ispitivanjeOsumnjicenogZadatak, osumnjicen, zadatak)

        val motiv2 = MotivData(
            idMotiv = 2,
            opis = "Ljubomora"
        )
        repo.insertMotivData(motiv2)

        val osoba2 = OsobaData(
            idOsoba = 2,
            ime = "Isabella Rossi",
            kontakt = "+31655443322",
            datum = timestamp2,
            zanimanje = "fotograf",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba2, zlocin)

        val osumnjicen2 = OsumnjicenData(
            idOsumnjicen = 2,
            status = 1,
            tipOsumnjicen = "pojedinac",
            motiv = motiv2,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba2
        )
        repo.insertOsumnjicenData(osumnjicen2, zlocin, motiv2)

        val zadatak2 = ZadatakData(
            idZadatak = 2,
            tekst = "Proveri alibi osumnjičene i njene poruke sa žrtvom.",
            korak = "korak_2",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak2, zlocin)
        zadaciList.add(zadatak2)

        val ispitivanjeOsumnjicenogZadatak2 = IspitivanjeOsumnjicenogZadatakData(
            idIspitivanjeOsumnjicenogZadatak = 2,
            osumnjicenId = osumnjicen2.idOsumnjicen,
            zadatakId = zadatak2.idZadatak,
            uradjen = false
        )
        repo.insertIspitivanjeOsumnjicenogZadatakData(ispitivanjeOsumnjicenogZadatak2, osumnjicen2, zadatak2)

        val ispitivanjeGet = repoGet.getIspitivanjeOsumnjicenogZadatak(zlocin.idZlocin, zadaciList)
        val ispitivanjeNePostoji = repoGet.getIspitivanjeOsumnjicenogZadatak(zlocin.idZlocin + 11, zadaciList)

        assertTrue(ispitivanjeGet != emptyList<IspitivanjeOsumnjicenogZadatakData>(), "Treba da postoje IspitivanjeOsumnjicenogZadatakData.")
        assertTrue(ispitivanjeNePostoji == emptyList<IspitivanjeOsumnjicenogZadatakData>(), "Treba da ne postoje IspitivanjeOsumnjicenogZadatakData")

        val validniZadatakIdjevi = zadaciList.map { it.idZadatak }

        val ocekivaniZadaci = listOf(ispitivanjeOsumnjicenogZadatak, ispitivanjeOsumnjicenogZadatak2)

        if (ispitivanjeGet != null) {
            for (i in ispitivanjeGet) {
                assertTrue(
                    i.zadatakId in validniZadatakIdjevi,
                    "IspitivanjeOsumnjicenogZadatakData sa zadatakId=${i.zadatakId} nije deo zadaciList — ne bi smeo biti u rezultatu."
                )
            }

            for (ocekivani in ocekivaniZadaci) {
                val pronadjen = ispitivanjeGet.find { it.idIspitivanjeOsumnjicenogZadatak == ocekivani.idIspitivanjeOsumnjicenogZadatak }
                assertNotNull(pronadjen, "IspitivanjeOsumnjicenogZadatakData sa id=${ocekivani.idIspitivanjeOsumnjicenogZadatak} nije pronađen u rezultatu")

                pronadjen?.let {
                    assertEquals(ocekivani.idIspitivanjeOsumnjicenogZadatak, it.idIspitivanjeOsumnjicenogZadatak)
                    assertEquals(ocekivani.osumnjicenId, it.osumnjicenId)
                    assertEquals(ocekivani.uradjen, it.uradjen)
                    assertEquals(ocekivani.zadatakId, it.zadatakId)
                }
            }
        }
    }

    @Test
    fun testGetIspitivanjeSvedokaZadatak(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zadaciList = mutableListOf<ZadatakData>()

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

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val svedok = SvedokData(
            idSvedok = 1,
            izjava = "Video sam tog muskarca kako izlazi iz sobe oko pola dva ujutru. Delovao je nervozno i stalno se osvrtao.",
            statusSvedok = "aktivno",
            statusIspitan = 0,
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertSvedokData(svedok,zlocin)

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Ispitaj sta kaze svedok.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 2,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)
        zadaciList.add(zadatak)

        val ispitivanjeSvedokaZadatak = IspitivanjeSvedokaZadatakData(
            idIspitivanjeSvedokaZadatak = 1,
            svedokId = svedok.idSvedok,
            zadatakId = zadatak.idZadatak,
            uradjen = false
        )
        repo.insertIspitivanjeSvedokaZadatakData(ispitivanjeSvedokaZadatak, svedok, zadatak)

        val osoba2 = OsobaData(
            idOsoba = 2,
            ime = "Sophie van Dijk",
            kontakt = "+31699887766",
            datum = timestamp2,
            zanimanje = "recepcionerka",
            pol = "zenski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba2, zlocin)

        val svedok2 = SvedokData(
            idSvedok = 2,
            izjava = "Radim u hotelu. Te noci sam cula viku iz sobe, ali nisam mogla da vidim ko je unutra. Ujutru je soba bila prazna.",
            statusSvedok = "aktivno",
            statusIspitan = 0,
            zlocinId = zlocin.idZlocin,
            osobaId = osoba2
        )
        repo.insertSvedokData(svedok2, zlocin)

        val zadatak2 = ZadatakData(
            idZadatak = 2,
            tekst = "Razgovaraj sa recepcionerkom hotela o dogadjaju.",
            korak = "korak_2",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak2, zlocin)
        zadaciList.add(zadatak2)

        val ispitivanjeSvedokaZadatak2 = IspitivanjeSvedokaZadatakData(
            idIspitivanjeSvedokaZadatak = 2,
            svedokId = svedok2.idSvedok,
            zadatakId = zadatak2.idZadatak,
            uradjen = false
        )
        repo.insertIspitivanjeSvedokaZadatakData(ispitivanjeSvedokaZadatak2, svedok2, zadatak2)

        val ispitivanjeGet = repoGet.getIspitivanjeSvedokaZadatak(zlocin.idZlocin, zadaciList)
        val ispitivanjeNePostoji = repoGet.getIspitivanjeSvedokaZadatak(zlocin.idZlocin + 11, zadaciList)

        assertTrue(ispitivanjeGet != emptyList<IspitivanjeSvedokaZadatakData>(), "Treba da postoje IspitivanjeSvedokaZadatakData.")
        assertTrue(ispitivanjeNePostoji == emptyList<IspitivanjeSvedokaZadatakData>(), "Treba da ne postoje IspitivanjeSvedokaZadatakData")

        val validniZadatakIdjevi = zadaciList.map { it.idZadatak }

        val ocekivaniZadaci = listOf(ispitivanjeSvedokaZadatak, ispitivanjeSvedokaZadatak2)

        if (ispitivanjeGet != null) {
            for (i in ispitivanjeGet) {
                assertTrue(
                    i.zadatakId in validniZadatakIdjevi,
                    "IspitivanjeSvedokaZadatakData sa zadatakId=${i.zadatakId} nije deo zadaciList — ne bi smeo biti u rezultatu."
                )
            }

            for (ocekivani in ocekivaniZadaci) {
                val pronadjen = ispitivanjeGet.find { it.idIspitivanjeSvedokaZadatak == ocekivani.idIspitivanjeSvedokaZadatak }
                assertNotNull(pronadjen, "IspitivanjeSvedokaZadatakData sa id=${ocekivani.idIspitivanjeSvedokaZadatak} nije pronađen u rezultatu")

                pronadjen?.let {
                    assertEquals(ocekivani.idIspitivanjeSvedokaZadatak, it.idIspitivanjeSvedokaZadatak)
                    assertEquals(ocekivani.svedokId, it.svedokId)
                    assertEquals(ocekivani.uradjen, it.uradjen)
                    assertEquals(ocekivani.zadatakId, it.zadatakId)
                }
            }
        }
    }

    @Test
    fun testGetTelefonZadaci(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zadaciList = mutableListOf<ZadatakData>()

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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val telefon = TelefonData(
            idTelefon = 1,
            model = "iPhone 13 Pro",
            os = "IOS",
            sifra = "1234",
            informacije = "Telefon je pronadjen na nocnom stociću pored tela. Nije bio zakljucan. Poslednji poziv upućen zeni ciji identitet još nije potvrdjen. Poruka „Stizes li?” poslata u 01:08h, 20 minuta pre procenjenog vremena smrti.",
            zrtvaId = zrtva.idZrtva
        )
        repo.insertTelefonData(telefon, zrtva)

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Otkrij ko je misteriozna zena.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 2,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)
        zadaciList.add(zadatak)

        val telefonZadatak = TelefonZadatakData(
            idTelefonZadatak = 1,
            telefonId = telefon.idTelefon,
            zadatakId = zadatak.idZadatak,
            uradjen = true
        )
        repo.insertTelefonZadatakData(telefonZadatak, telefon, zadatak)

        val zadatak2 = ZadatakData(
            idZadatak = 2,
            tekst = "Analiziraj pozive sa telefona žrtve.",
            korak = "korak_2",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak2, zlocin)
        zadaciList.add(zadatak2)

        val telefonZadatak2 = TelefonZadatakData(
            idTelefonZadatak = 2,
            telefonId = telefon.idTelefon,
            zadatakId = zadatak2.idZadatak,
            uradjen = false
        )
        repo.insertTelefonZadatakData(telefonZadatak2, telefon, zadatak2)

        val telefonGet = repoGet.getTelefonZadaci(zrtva.idZrtva, zadaciList)
        val telefonNePostoji = repoGet.getTelefonZadaci(zrtva.idZrtva + 11, zadaciList)

        assertTrue(telefonGet != emptyList<TelefonZadatakData>(), "Treba da postoje TelefonZadatakData.")
        assertTrue(telefonNePostoji == emptyList<TelefonZadatakData>(), "Treba da ne postoje TelefonZadatakData")

        val validniZadatakIdjevi = zadaciList.map { it.idZadatak }

        val ocekivaniZadaci = listOf(telefonZadatak, telefonZadatak2)

        if (telefonGet != null) {
            for (t in telefonGet) {
                assertTrue(
                    t.zadatakId in validniZadatakIdjevi,
                    "TelefonZadatakData sa zadatakId=${t.zadatakId} nije deo zadaciList — ne bi smeo biti u rezultatu."
                )
            }

            for (ocekivani in ocekivaniZadaci) {
                val pronadjen = telefonGet.find { it.idTelefonZadatak == ocekivani.idTelefonZadatak }
                assertNotNull(pronadjen, "TelefonZadatakData sa id=${ocekivani.idTelefonZadatak} nije pronađen u rezultatu")

                pronadjen?.let {
                    assertEquals(ocekivani.idTelefonZadatak, it.idTelefonZadatak)
                    assertEquals(ocekivani.telefonId, it.telefonId)
                    assertEquals(ocekivani.uradjen, it.uradjen)
                    assertEquals(ocekivani.zadatakId, it.zadatakId)
                }
            }
        }
    }

    @Test
    fun testGetForenzickiDokazZadatak(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val zadaciList = mutableListOf<ZadatakData>()

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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val forenzickiDokaz = ForenzickiDokazData(
            idForenzickiDokaz = 1,
            tipForenzickiDokaz = "DNK",
            opis = "Analiza DNK uzorka sa krvavog noza",
            statusS = 0,
            veza = "DNK tragovi na nozu pripadaju zenskoj osobi.",
            zrtvaId = zrtva.idZrtva
        )
        repo.insertForenzickiDokaz(forenzickiDokaz, zrtva)

        val zadatak = ZadatakData(
            idZadatak = 1,
            tekst = "Otkrij kojoj zeni pripada DNK.",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak, zlocin)
        zadaciList.add(zadatak)

        val forenzickiDokazZadatak = ForenzickiDokazZadatakData(
            idForenzickiDokazZadatak = 1,
            tekst = "Otkrij kojoj zeni pripada DNK.",
            forenzickiDokazId = forenzickiDokaz.idForenzickiDokaz,
            uradjen = true,
            zadatakId = zadatak.idZadatak
        )
        repo.insertForenzickiDokazZadatakData(forenzickiDokazZadatak, forenzickiDokaz, zadatak)

        val forenzickiDokaz2 = ForenzickiDokazData(
            idForenzickiDokaz = 2,
            tipForenzickiDokaz = "otisak",
            opis = "Otisak prsta pronađen na kvaki hotelske sobe",
            statusS = 0,
            veza = "Otisak prsta ne pripada žrtvi – moguće da je u pitanju uljez.",
            zrtvaId = zrtva.idZrtva
        )
        repo.insertForenzickiDokaz(forenzickiDokaz2, zrtva)

        val zadatak2 = ZadatakData(
            idZadatak = 2,
            tekst = "Identifikuj osobu kojoj pripada otisak prsta.",
            korak = "korak_2",
            uradjen = false,
            nextZadatak = 0,
            zlocinId = zlocin.idZlocin
        )
        repo.insertZadatakData(zadatak2, zlocin)
        zadaciList.add(zadatak2)

        val forenzickiDokazZadatak2 = ForenzickiDokazZadatakData(
            idForenzickiDokazZadatak = 2,
            tekst = "Identifikuj osobu kojoj pripada otisak prsta.",
            forenzickiDokazId = forenzickiDokaz2.idForenzickiDokaz,
            uradjen = false,
            zadatakId = zadatak2.idZadatak
        )
        repo.insertForenzickiDokazZadatakData(forenzickiDokazZadatak2, forenzickiDokaz2, zadatak2)

        val forenzickiDokazZadatakGet = repoGet.getForenzickiDokazZadatak(zrtva.idZrtva, zadaciList)
        val forenzickiDokazZadatakNePostoji = repoGet.getForenzickiDokazZadatak(zrtva.idZrtva + 11, zadaciList)

        assertTrue(forenzickiDokazZadatakGet != emptyList<ForenzickiDokazZadatakData>(), "Treba da postoje ForenzickiDokazZadatakData.")
        assertTrue(forenzickiDokazZadatakNePostoji == emptyList<ForenzickiDokazZadatakData>(), "Treba da ne postoje ForenzickiDokazZadatakData")

        val validniZadatakIdjevi = zadaciList.map { it.idZadatak }

        val ocekivaniZadaci = listOf(forenzickiDokazZadatak, forenzickiDokazZadatak2)

        if (forenzickiDokazZadatakGet != null) {
            for (f in forenzickiDokazZadatakGet) {
                assertTrue(
                    f.zadatakId in validniZadatakIdjevi,
                    "ForenzickiDokazZadatakData sa zadatakId=${f.zadatakId} nije deo zadaciList — ne bi smeo biti u rezultatu."
                )
            }

            for (ocekivani in ocekivaniZadaci) {
                val pronadjen = forenzickiDokazZadatakGet.find { it.idForenzickiDokazZadatak == ocekivani.idForenzickiDokazZadatak }
                assertNotNull(pronadjen, "ForenzickiDokazZadatakData sa id=${ocekivani.idForenzickiDokazZadatak} nije pronađen u rezultatu")

                pronadjen?.let {
                    assertEquals(ocekivani.idForenzickiDokazZadatak, it.idForenzickiDokazZadatak)
                    assertEquals(ocekivani.tekst, it.tekst)
                    assertEquals(ocekivani.forenzickiDokazId, it.forenzickiDokazId)
                    assertEquals(ocekivani.uradjen, it.uradjen)
                    assertEquals(ocekivani.zadatakId, it.zadatakId)
                }
            }
        }
    }

    @Test
    fun testGetOneCall(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val kontaktiList = mutableListOf<OneContactData>()

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

        val oneContact = OneContactData(
            idOneContact = 1,
            zlocinId = zlocin.idZlocin,
            ime = "Alessandro Moretti",
            broj = "+393312223344",
            slika = 1
        )
        repo.insertOneContactData(oneContact, zlocin)
        kontaktiList.add(oneContact)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val oneCall = OneCallData(
            idOneCall = 1,
            kontakt = oneContact.idOneContact,
            datum = timestamp2,
            propusten = false,
            dolazni = true,
            zrtvaId = zrtva.idZrtva
        )
        repo.insertOneCallData(oneCall)

        val oneCallGet = repoGet.getOneCall(zlocin.idZlocin, kontaktiList)
        val oneCallNePostoji = repoGet.getOneCall(zlocin.idZlocin + 11, kontaktiList)

        assertTrue(oneCallGet != emptyList<OneCallData>(), "Treba da postoje OneCallData.")
        assertTrue(oneCallNePostoji == emptyList<OneCallData>(), "Treba da ne postoje OneCallData")

        if (oneCallGet != null) {
            for(o in oneCallGet){
                if (o.idOneCall == oneCall.idOneCall){
                    assertEquals(o.idOneCall, oneCall.idOneCall)
                    assertEquals(o.kontakt, oneCall.kontakt)
                    assertEquals(o.datum, oneCall.datum)
                    assertEquals(o.propusten, oneCall.propusten)
                    assertEquals(o.dolazni, oneCall.dolazni)
                    assertEquals(o.zrtvaId, oneCall.zrtvaId)
                }
            }
        }
    }

    @Test
    fun testGetObicnaPoruka(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val kontaktiList = mutableListOf<OneContactData>()

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

        val kontaktKoSalje = OneContactData(
            idOneContact = 1,
            zlocinId = zlocin.idZlocin,
            ime = "Alessandro Moretti",
            broj = "+393312223344",
            slika = 1
        )
        repo.insertOneContactData(kontaktKoSalje, zlocin)
        kontaktiList.add(kontaktKoSalje)

        val kontaktKomeSalje = OneContactData(
            idOneContact = 2,
            zlocinId = zlocin.idZlocin,
            ime = "Giulio Romano",
            broj = "+393316665555",
            slika = 2
        )
        repo.insertOneContactData(kontaktKomeSalje, zlocin)
        kontaktiList.add(kontaktKomeSalje)

        val datumStr = "2024-03-04"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val obicnaPoruka = ObicnaPorukaData(
            idObicnaPoruka = 1,
            kontaktKoSalje = kontaktKoSalje.idOneContact,
            kontaktKomeSalje = kontaktKomeSalje.idOneContact,
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.",
            datum = timestamp2,
            procitana = false
        )
        repo.insertObicnaPorukaData(obicnaPoruka, kontaktKoSalje, kontaktKomeSalje)

        val obicnePorukeGet = repoGet.getObicnaPoruka(zlocin.idZlocin, kontaktiList)
        val obicnePorukeNePostoji = repoGet.getObicnaPoruka(zlocin.idZlocin + 11, kontaktiList)

        assertTrue(obicnePorukeGet != emptyList<ObicnaPorukaData>(), "Treba da postoje ObicnaPorukaData.")
        assertTrue(obicnePorukeNePostoji == emptyList<ObicnaPorukaData>(), "Treba da ne postoje ObicnaPorukaData")

        if (obicnePorukeGet != null) {
            for(o in obicnePorukeGet){
                if (o.idObicnaPoruka == obicnaPoruka.idObicnaPoruka){
                    assertEquals(o.idObicnaPoruka, obicnaPoruka.idObicnaPoruka)
                    assertEquals(o.kontaktKoSalje, obicnaPoruka.kontaktKoSalje)
                    assertEquals(o.kontaktKomeSalje, obicnaPoruka.kontaktKomeSalje)
                    assertEquals(o.tekst, obicnaPoruka.tekst)
                    assertEquals(o.datum, obicnaPoruka.datum)
                    assertEquals(o.procitana, obicnaPoruka.procitana)
                }
            }
        }
    }

    @Test
    fun testGetPacijent(){
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val osobeList = mutableListOf<OsobaData>()

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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)
        osobeList.add(osoba)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)
        osobeList.add(osoba1)

        val pacijent = PacijentData(
            idPacijent = 1,
            simptomi = "Pacijent se zali na povisenu temperaturu, mucninu i povracanje, uz primetno bledilo i ubrzan puls. Takodje pokazuje znakove mentalne konfuzije i teskoce sa koncentracijom od jutra nakon prijema.",
            statusPacijenta = "ziva",
            datumPrijave = timestamp2,
            prijavio = osoba1,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(pacijent)

        val pacijentiGet = repoGet.getPacijent(zlocin.idZlocin, zlocin, zrtva, osobeList)
        val pacijentiNePostoji = repoGet.getPacijent(zlocin.idZlocin + 11, zlocin, zrtva, osobeList)

        assertTrue(pacijentiGet != null, "Treba da postoje PacijentData.")
        assertTrue(pacijentiNePostoji == null, "Treba da ne postoje PacijentData")

        if (pacijentiGet != null) {
            if (pacijentiGet.idPacijent == pacijent.idPacijent){
                assertEquals(pacijentiGet.idPacijent, pacijent.idPacijent)
                assertEquals(pacijentiGet.simptomi, pacijent.simptomi)
                assertEquals(pacijentiGet.statusPacijenta, pacijent.statusPacijenta)
                assertEquals(pacijentiGet.datumPrijave, pacijent.datumPrijave)
                assertEquals(pacijentiGet.prijavio, pacijent.prijavio)
                assertEquals(pacijentiGet.zlocinId, pacijent.zlocinId)
                assertEquals(pacijentiGet.zrtvaId, pacijent.zrtvaId)
            }
        }
    }

    @Test
    fun testGetMedicinskiIzvetaj() {
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)

        val pacijent = PacijentData(
            idPacijent = 1,
            simptomi = "Pacijent se zali na povisenu temperaturu, mucninu i povracanje, uz primetno bledilo i ubrzan puls. Takodje pokazuje znakove mentalne konfuzije i teskoce sa koncentracijom od jutra nakon prijema.",
            statusPacijenta = "ziva",
            datumPrijave = timestamp2,
            prijavio = osoba1,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(pacijent)

        val medicinskiIzvestaj = MedicinskiIzvestajData(
            idMedicinskiIzvestaj = 1,
            rezime = "Pacijent primljen sa visokom temperaturom, mucninom, povracanjem i znacima mentalne konfuzije.",
            CTnalaz = "Bez znakova intrakranijalnog krvarenja. Blaga cerebralna edematoznost.",
            MRInalaz = "Ne prikazuje strukturalne abnormalnosti. Promene u hipokampusu moguce uzrokovane toksinima.",
            krvnaSlika = "Poviseni leukociti, smanjeni eritrociti. Znaci upalnog odgovora organizma.",
            toksikoloskeAnalize = "Detektovani tragovi pesticida – mogucnost trovanja.",
            zakljucak = "Simptomi i analize ukazuju na moguce akutno trovanje. Preporucena hospitalizacija i dalja toksikoloska ispitivanja.",
            pacijentId = pacijent
        )
        repo.insertMedicinskiIzvestajData(medicinskiIzvestaj)

        // lazni pacijent

        val osoba2 = OsobaData(
            idOsoba = 3,
            ime = "Luca Bianchi",
            kontakt = "+393300112233",
            datum = timestamp2,
            zanimanje = "glumac",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba2, zlocin)

        val lazniPacijent = PacijentData(
            idPacijent = 2,
            simptomi = "Navodi jake bolove u stomaku i vrtoglavicu, ali medicinski nalazi ne potvrđuju nikakve fizičke simptome. Ispitanik deluje uznemireno kada se spomene hotelska soba.",
            statusPacijenta = "lazno_prijavljen",
            datumPrijave = timestamp2,
            prijavio = osoba2,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(lazniPacijent)

        val medicinskiIzvestajGet = repoGet.getMedicinskiIzvetaj(pacijent)
        val medicinskiIzvestajNePostoji = repoGet.getMedicinskiIzvetaj(lazniPacijent)

        assertTrue(medicinskiIzvestajGet != null, "Treba da postoje MedicinskiIzvestajData.")
        assertTrue(medicinskiIzvestajNePostoji == null, "Treba da ne postoje MedicinskiIzvestajData")

        if (medicinskiIzvestajGet != null) {
            if (medicinskiIzvestajGet.idMedicinskiIzvestaj == medicinskiIzvestaj.idMedicinskiIzvestaj){
                assertEquals(medicinskiIzvestajGet.idMedicinskiIzvestaj, medicinskiIzvestaj.idMedicinskiIzvestaj)
                assertEquals(medicinskiIzvestajGet.rezime, medicinskiIzvestaj.rezime)
                assertEquals(medicinskiIzvestajGet.CTnalaz, medicinskiIzvestaj.CTnalaz)
                assertEquals(medicinskiIzvestajGet.MRInalaz, medicinskiIzvestaj.MRInalaz)
                assertEquals(medicinskiIzvestajGet.krvnaSlika, medicinskiIzvestaj.krvnaSlika)
                assertEquals(medicinskiIzvestajGet.toksikoloskeAnalize, medicinskiIzvestaj.toksikoloskeAnalize)
                assertEquals(medicinskiIzvestajGet.zakljucak, medicinskiIzvestaj.zakljucak)
                assertEquals(medicinskiIzvestajGet.pacijentId, medicinskiIzvestaj.pacijentId)
            }
        }
    }

    @Test
    fun testGetLekarskiTest() {
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)

        val pacijent = PacijentData(
            idPacijent = 1,
            simptomi = "Pacijent se zali na povisenu temperaturu, mucninu i povracanje, uz primetno bledilo i ubrzan puls. Takodje pokazuje znakove mentalne konfuzije i teskoce sa koncentracijom od jutra nakon prijema.",
            statusPacijenta = "ziva",
            datumPrijave = timestamp2,
            prijavio = osoba1,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(pacijent)

        val lekarskiTest = LekarskiTestData(
            idLekarskiTest = 1,
            pacijentId = pacijent,
            izvestaj = "Obavljen je inicijalni neuroloski pregled koji je pokazao usporene reflekse i dezorijentaciju u vremenu. Takodje je uradjen test koordinacije pokreta sa slabim rezultatima."
        )
        repo.insertLekarskiTestData(lekarskiTest)

        // lazni pacijent

        val osoba2 = OsobaData(
            idOsoba = 3,
            ime = "Luca Bianchi",
            kontakt = "+393300112233",
            datum = timestamp2,
            zanimanje = "glumac",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba2, zlocin)

        val lazniPacijent = PacijentData(
            idPacijent = 2,
            simptomi = "Navodi jake bolove u stomaku i vrtoglavicu, ali medicinski nalazi ne potvrđuju nikakve fizičke simptome. Ispitanik deluje uznemireno kada se spomene hotelska soba.",
            statusPacijenta = "lazno_prijavljen",
            datumPrijave = timestamp2,
            prijavio = osoba2,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(lazniPacijent)

        val lekarskiTestGet = repoGet.getLekarskiTest(pacijent)
        val lekarskiTestNePostoji = repoGet.getLekarskiTest(lazniPacijent)

        assertTrue(lekarskiTestGet != null, "Treba da postoje LekarskiTestData.")
        assertTrue(lekarskiTestNePostoji == null, "Treba da ne postoje LekarskiTestData")

        if (lekarskiTestGet != null) {
            if (lekarskiTestGet.idLekarskiTest == lekarskiTest.idLekarskiTest){
                assertEquals(lekarskiTestGet.idLekarskiTest, lekarskiTest.idLekarskiTest)
                assertEquals(lekarskiTestGet.pacijentId, lekarskiTest.pacijentId)
                assertEquals(lekarskiTestGet.izvestaj, lekarskiTest.izvestaj)
            }
        }
    }

    @Test
    fun testGetLokacijeIstrage() {
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
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

        val lokacijeIstrage = LokacijeIstrageData(
            idLokacijeIstrage = 1,
            mesto = "Amsterdam",
            naziv = "De Wallen",
            opis = "Potencijalno mesto incidenta zbog svedocenja prolaznika.",
            zlocinId = zlocin.idZlocin,
            geoTackaALatitude = 52.3740,
            geoTackaALongitude = 4.9000
        )
        repo.insertLokacijeIstrageData(lokacijeIstrage)

        val lokacijeIstrageGet = repoGet.getLokacijeIstrage(zlocin.idZlocin)
        val lokacijeIstrageNePostoji = repoGet.getLokacijeIstrage(zlocin.idZlocin + 11)

        assertTrue(lokacijeIstrageGet != emptyList<LokacijeIstrageData>(), "Treba da postoje LokacijeIstrageData.")
        assertTrue(lokacijeIstrageNePostoji == emptyList<LokacijeIstrageData>(), "Treba da ne postoje LokacijeIstrageData")

        if (lokacijeIstrageGet != null) {
            for (l in lokacijeIstrageGet) {
                if (l.idLokacijeIstrage == lokacijeIstrage.idLokacijeIstrage){
                    assertEquals(l.idLokacijeIstrage, lokacijeIstrage.idLokacijeIstrage)
                    assertEquals(l.mesto, lokacijeIstrage.mesto)
                    assertEquals(l.naziv, lokacijeIstrage.naziv)
                    assertEquals(l.opis, lokacijeIstrage.opis)
                    assertEquals(l.zlocinId, lokacijeIstrage.zlocinId)
                    assertEquals(l.geoTackaALatitude, lokacijeIstrage.geoTackaALatitude)
                    assertEquals(l.geoTackaALongitude, lokacijeIstrage.geoTackaALongitude)
                }
            }
        }
    }

    @Test
    fun testGetIzjavaZaPacijenta() {
        val repo = RepositoryInsert(connection)
        var repoGet = Repository(connection)
        val d = System.currentTimeMillis()

        val osobeList = mutableListOf<OsobaData>()

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

        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        val timestamp2 = dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

        val osoba = OsobaData(
            idOsoba = 1,
            ime = "Giulio Romano",
            kontakt = "+393316665555",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba, zlocin)
        osobeList.add(osoba)

        val zrtva = ZrtvaData(
            idZrtva = 1,
            tipZrtve = "osoba",
            detalji = "Na telu tragovi gusenja.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
        repo.insertZrtva(zrtva, zlocin, osoba)

        val osoba1 = OsobaData(
            idOsoba = 2,
            ime = "Alessandro Moretti",
            kontakt = "+393312223344",
            datum = timestamp2,
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )
        repo.insertOsobaData(osoba1, zlocin)
        osobeList.add(osoba1)

        val pacijent = PacijentData(
            idPacijent = 1,
            simptomi = "Pacijent se zali na povisenu temperaturu, mucninu i povracanje, uz primetno bledilo i ubrzan puls. Takodje pokazuje znakove mentalne konfuzije i teskoce sa koncentracijom od jutra nakon prijema.",
            statusPacijenta = "ziva",
            datumPrijave = timestamp2,
            prijavio = osoba1,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(pacijent)

        val izjavaZaPacijenta = IzjavaZaPacijentaData(
            idIzjavaZaPacijenta = 1,
            izjava = "Ujutru nakon incidenta, Giulio se ponasao cudno – delovao je dezorijentisano i zbunjeno. Rekao je da se ne seca šta se desilo prethodne noci, ali se zalio na mucninu i vrtoglavicu. Pomenuo je da je popio pice koje mu je donela nepoznata zena.",
            pacijentId = pacijent,
            osobaId = osoba1
        )
        repo.insertIzjavaZaPacijentaData(izjavaZaPacijenta, pacijent, osoba1)

        val pacijentGreska = PacijentData(
            idPacijent = 2,
            simptomi = "Pacijent se zali na povisenu temperaturu, mucninu i povracanje, uz primetno bledilo i ubrzan puls. Takodje pokazuje znakove mentalne konfuzije i teskoce sa koncentracijom od jutra nakon prijema.",
            statusPacijenta = "ziva",
            datumPrijave = timestamp2,
            prijavio = osoba1,
            zlocinId = zlocin,
            zrtvaId = zrtva
        )
        repo.insertPacijentData(pacijentGreska)

        val izjaveGet = repoGet.getIzjavaZaPacijenta(pacijent, osobeList)
        val izjaveNePostoji = repoGet.getIzjavaZaPacijenta(pacijentGreska, osobeList)

        assertTrue(izjaveGet != null, "Treba da postoje IzjavaZaPacijentaData.")
        assertTrue(izjaveNePostoji == null, "Treba da ne postoje IzjavaZaPacijentaData")

        if (izjaveGet != null) {
            if (izjaveGet.idIzjavaZaPacijenta == izjavaZaPacijenta.idIzjavaZaPacijenta){
                assertEquals(izjaveGet.idIzjavaZaPacijenta, izjavaZaPacijenta.idIzjavaZaPacijenta)
                assertEquals(izjaveGet.izjava, izjavaZaPacijenta.izjava)
                assertEquals(izjaveGet.pacijentId, izjavaZaPacijenta.pacijentId)
                assertEquals(izjaveGet.osobaId, izjavaZaPacijenta.osobaId)
            }
        }
    }


}