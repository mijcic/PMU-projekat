package com.example.unit

import com.example.closeResources
import com.example.models.dto.*
import com.example.repository.RepositoryInsert
import io.mockk.*
import junit.framework.TestCase.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.sql.*
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class RepositoryInsertTest {
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var resultSet: ResultSet
    private lateinit var repositoryInsert: RepositoryInsert

    private fun returnZlocinData():ZlocinData{
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

    private fun returnOsobaData(): OsobaData{
        return OsobaData(
            idOsoba = 0,
            ime = "Petar",
            kontakt = "petar@example.com",
            datum = System.currentTimeMillis(),
            zanimanje = "inženjer",
            pol = "M",
            zlocinId = returnZlocinData().idZlocin
        )
    }

    private fun returnZrtvaData(): ZrtvaData {
        return ZrtvaData(
            idZrtva = 0,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu.",
            statusZrtva = "mrtav",
            zlocinId = returnZlocinData().idZlocin,
            osobaId = returnOsobaData()
        )
    }

    private fun returnMotivData(): MotivData {
        return MotivData(
            idMotiv = 1,
            opis = "Ljubomora"
        )
    }

    private fun returnOsumnjiceniData(): OsumnjicenData{
        val m = returnMotivData()

        return OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = m,
            zlocinId = returnZlocinData().idZlocin,
            kriv = 0,
            osobaId = returnOsobaData()
        )
    }

    private fun returnDokaziData():DokazData{
        return DokazData(
            idDokaz = 0,
            tipDokaza = "fizicki",
            opis = "Pistolj pronadjen na mestu zlocina.",
            zlocinId = returnZlocinData().idZlocin,
            zrtvaId = returnZrtvaData().idZrtva,
            status = 0
        )
    }

    private fun returnSvedokData(): SvedokData{
        return SvedokData(
            idSvedok = 0,
            izjava = "Cula sam pucanj i videla zenu kako bezi.",
            statusSvedok = "aktivno",
            statusIspitan = 0,
            zlocinId = returnZlocinData().idZlocin,
            osobaId = returnOsobaData()
        )
    }

    private fun returnObdukcijaData():ObdukcijaData{
        return ObdukcijaData(
            idObdukcija = 0,
            izvestaj = "Zrtva je preminula od rane od metka u grudima. Nema znakova borbe.",
            datum = System.currentTimeMillis(),
            uzrokSmrti = "Rana od metka u grudima",
            zrtvaId =returnZrtvaData().idZrtva,
            informacije = "Nema znakova seksualnog napada."
        )
    }

    private fun returnForenzickiDokazData():ForenzickiDokazData{
        return ForenzickiDokazData(
            idForenzickiDokaz = 0,
            tipForenzickiDokaz = "DNK",
            opis = "DNK tragovi pronađeni na pištolju.",
            statusS = 0,
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
        )
    }

    private fun returnTelefonData():TelefonData{
        return TelefonData(
            idTelefon = 0,
            model = "Samsung Galaxy S22",
            os = "Android",
            sifra = "1234",
            informacije = "Pronađene su poruke sa pretnjama.",
            zrtvaId = returnZrtvaData().idZrtva
        )
    }

    private fun returnAplikacijaData(): AplikacijaData{
        return AplikacijaData(
            idAplikacije = 0,
            naziv = "Instagram",
            tip = 0,
            zrtvaId = returnZrtvaData(),
            aktivna = true,
            informacije = "Poslednja aktivnost na Instagram profilu žrtve."
        )
    }

    private fun returnTragData(): TragData{
        return TragData(
            idTrag = 0,
            forenzickiDokazId = returnForenzickiDokazData(),
            osumnjicenId = returnOsumnjiceniData()
        )
    }

    private fun returnDokaziOsumnjiceniData(): DokazOsumnjicenData{
        return DokazOsumnjicenData(
            idDokazOsumnjicen = 0,
            dokazId = returnDokaziData(),
            osumnjicenId = returnOsumnjiceniData()
        )
    }

    private fun returnOneContact(): OneContactData{
        return OneContactData(
            idOneContact = 0,
            zlocinId = returnZlocinData().idZlocin,
            ime = "John",
            broj = "+54533465645",
            slika = 1,
        )
    }

    private fun returnBeleskeData(): BeleskaData{
        return BeleskaData(
            idBeleska = 0,
            zlocinId = returnZlocinData().idZlocin,
            tekst = "Moram da stignem pre njega",
            datum = returnTimeStamp()
        )
    }

    private fun returnWhatsAppKontaktiData(): WhatsAppKontaktData{
        return WhatsAppKontaktData(
            idWhatsAppKontakt = 0,
            zlocinId = returnZlocinData().idZlocin,
            ime = "Tom",
            broj = "+1231423152",
            slika = 13
        )
    }

    private fun returnWhatsAppPorukeData(): WhatsAppPorukaData{
        return WhatsAppPorukaData(
            idWhatsAppPoruka = 0,
            kontaktKoSalje = returnWhatsAppKontaktiData().idWhatsAppKontakt,
            kontaktKomeSalje = returnWhatsAppKontaktiData().idWhatsAppKontakt,
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.",
            datum = returnTimeStamp(),
            procitana = false
        )
    }

    private fun returnOneCallData(): OneCallData{
        return OneCallData(
            idOneCall =0,
            kontakt = returnOneContact().idOneContact,
            datum = returnTimeStamp(),
            propusten = false,
            dolazni = true
        )
    }

    private fun returnGalleryData(): GalleryData{
        return GalleryData(
            idPhoto = 0,
            zlocinId = returnZlocinData().idZlocin,
            slika = 1,
            datum = returnTimeStamp(),
            mesto = "Amsterdam"
        )
    }

    private fun returnObicnaPorukaData(): ObicnaPorukaData{
        return ObicnaPorukaData(
            idObicnaPoruka = 0,
            kontaktKoSalje = returnOneContact().idOneContact,
            kontaktKomeSalje = returnOneContact().idOneContact,
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.",
            datum = returnTimeStamp(),
            procitana = false
        )
    }

    private fun returnOdnosOsumnjicenZrtvaData(): OdnosOsumnjicenZrtvaData{
        return OdnosOsumnjicenZrtvaData(
            idOdnos = 0,
            osumnjicenId = returnOsumnjiceniData().idOsumnjicen,
            zrtvaId = returnZrtvaData().idZrtva,
            tipOdnosa = "rivalski"
        )
    }

    private fun returnPitanjaData(): PitanjeData{
        return PitanjeData(
            idPitanje = 0,
            zlocinId = returnZlocinData().idZlocin,
            tekst = "Da li si otisao do njene sobe?"
        )
    }

    private fun returnOdgovorData(): OdgovorData{
        return OdgovorData(
            idOdogovor = 0,
            pitanjeId = returnPitanjaData().idPitanje,
            tekstOdgovora = "Jesam, kaze da nije umesana.",
            tacan = false,
            bodovi = 10
        )
    }

    private fun returnPitanjeIspitivanjeOsumnjicenogData(): PitanjeIspitivanjeOsumnjicenogData{
        return PitanjeIspitivanjeOsumnjicenogData(
            idPitanjeIspitivanjeOsumnjicenog = 0,
            kategorija = "alibi",
            tekst = "Zasto ste bili u sobi zrtve?",
            odgovor = "Samo sam mu doneo kofer. Otisao sam odmah.",
            komentar = "Nije pomenuo sadrzaj kofera ni zasto bas on donosi. Moguce da prikriva pravi razlog dolaska.",
            osumnjicenId = returnOsumnjiceniData().idOsumnjicen
        )
    }

    private fun returnPitanjeIspitivanjeSvedokaData(): PitanjeIspitivanjeSvedokaData{
        return PitanjeIspitivanjeSvedokaData(
            idPitanjeIspitivanjeSvedoka = 0,
            tekst = "Jeste li sigurni da je to bio bas taj muskarac?",
            odgovor = "Da, prepoznao sam ga – imao je crvenu jaknu i hodao je sepajući.",
            svedokId = returnSvedokData().idSvedok,
            nextPitanje = 0
        )
    }

    private fun returnZadatakData(): ZadatakData{
        return ZadatakData(
            idZadatak = 0,
            tekst = "Pronadji karticu gosta",
            korak = "korak_1",
            uradjen = false,
            nextZadatak = null,
            zlocinId = returnZlocinData().idZlocin
        )
    }

    private fun returnDokazZadatakData(): DokazZadatakData{
        return DokazZadatakData(
            idDokazZadatak = 0,
            tekst = "Posalji dokaz na forenzicku analizu",
            dokazId = returnDokaziData().idDokaz,
            uradjen = false,
            zadatakId = returnZadatakData().idZadatak
        )
    }

    private fun returnIspitivanjeOsumnjicenogZadatakData(): IspitivanjeOsumnjicenogZadatakData{
        return IspitivanjeOsumnjicenogZadatakData(
            idIspitivanjeOsumnjicenogZadatak = 0,
            osumnjicenId = returnOsumnjiceniData().idOsumnjicen,
            zadatakId = returnZadatakData().idZadatak,
            uradjen = true
        )
    }

    private fun returnIspitivanjeSvedokaZadatakData(): IspitivanjeSvedokaZadatakData{
        return IspitivanjeSvedokaZadatakData(
            idIspitivanjeSvedokaZadatak = 0,
            svedokId = returnSvedokData().idSvedok,
            zadatakId = returnZadatakData().idZadatak,
            uradjen = false
        )
    }

    private fun returnTelefonZadatakData(): TelefonZadatakData{
        return TelefonZadatakData(
            idTelefonZadatak = 0,
            telefonId = returnTelefonData().idTelefon,
            zadatakId = returnZadatakData().idZadatak,
            uradjen = true
        )
    }

    private fun returnForenzickiDokazZadatakData(): ForenzickiDokazZadatakData{
        return ForenzickiDokazZadatakData(
            idForenzickiDokazZadatak = 0,
            tekst = "Otkrij kojoj zeni pripada DNK.",
            forenzickiDokazId = returnForenzickiDokazData().idForenzickiDokaz,
            uradjen = true,
            zadatakId = returnZadatakData().idZadatak
        )
    }

    private fun returnPacijentData(): PacijentData{
        return PacijentData(
            idPacijent = 0,
            simptomi = "Temperatura",
            statusPacijenta = "ziva", //ziva,mrtva
            datumPrijave = returnTimeStamp(),
            prijavio = returnOsobaData(),
            zlocinId = returnZlocinData(),
            zrtvaId = returnZrtvaData()
        )
    }

    private fun returnMedicinskiIzvestajData(): MedicinskiIzvestajData{
        return MedicinskiIzvestajData (
            idMedicinskiIzvestaj = 0 ,
            rezime = "u redu",
            CTnalaz = "uredan CTnalaz",
            MRInalaz = "uredan MRInalaz",
            krvnaSlika = "dobra",
            toksikoloskeAnalize = "nema",
            zakljucak = "u redu",
            pacijentId = returnPacijentData()
        )
    }

    private fun returnLekarskiTestData(): LekarskiTestData{
        return LekarskiTestData (
            idLekarskiTest = 0,
            pacijentId = returnPacijentData(),
            izvestaj = "Lekarski test okej",
        )
    }

    private fun returnLokacijeIstrageData(): LokacijeIstrageData{
        return LokacijeIstrageData (
            idLokacijeIstrage = 0,
            mesto = "London",
            naziv = "Bolnica Sveti Luka",
            opis = "Bolnica",
            zlocinId = returnZlocinData().idZlocin,
            geoTackaALatitude = 2.3,
            geoTackaALongitude = 4.3
        )
    }

    private fun returnIzjavaZaPacijentaData(): IzjavaZaPacijentaData{
        return IzjavaZaPacijentaData(
            idIzjavaZaPacijenta = 0,
            izjava = "Pacijent je u losem stanju",
            pacijentId = returnPacijentData(),
            osobaId = returnOsobaData()
        )
    }

    @BeforeEach
    fun setup() {
        connection = mockk()
        preparedStatement = mockk()
        resultSet = mockk(relaxed = true)
        every { resultSet.close() } just Runs
        repositoryInsert = RepositoryInsert(connection)
    }

    @AfterEach
    fun teardown() {
        clearMocks(connection, preparedStatement, resultSet)
        unmockkStatic("com.example.ApplicationKt") // pravi package gde je closeResources
    }

    @Test
    fun `should handle SQLException during executeUpdate in method insertUsedZlocinData`() {
        val usedZlocinData = UsedZlocinData(
            zlocinId = ZlocinData(
                tipZlocinaId = 1,
                naziv = "Ubistvo u tramvaju",
                datum = System.currentTimeMillis(),
                mesto = "Pariz",
                opis = "Ubistvo zene",
                status = "u_istrazi",
                idZlocin = 7
            ),
            used = false,
            idUsedZlocin = 0
        )

        every {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
        } returns preparedStatement


        every { preparedStatement.setInt(1, 7) } just Runs
        every { preparedStatement.setBoolean(2, false) } just Runs

        every { preparedStatement.executeUpdate() } throws SQLException("DB insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        repositoryInsert.insertUsedZlocinData(usedZlocinData)

        assertEquals(0, usedZlocinData.idUsedZlocin)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, 7)
            preparedStatement.setBoolean(2, false)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }

    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertUsedZlocinData`() {
        val usedZlocin = UsedZlocinData(
            zlocinId = ZlocinData(
                tipZlocinaId = 1,
                naziv = "Test",
                datum = System.currentTimeMillis(),
                mesto = "Mesto",
                opis = "Opis",
                status = "status",
                idZlocin = 5
            ),
            used = false,
            idUsedZlocin = 0
        )

        val connection = mockk<Connection>()
        val repositoryInsert = RepositoryInsert(connection)

        every {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
        } returns null

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        repositoryInsert.insertUsedZlocinData(usedZlocin)
        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
    }

    //zlocinData

    @Test
    fun `should handle SQLException during executeUpdate in method insertZlocinData`() {
        val zlocin = returnZlocinData()

        every {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
        } returns preparedStatement

        every { preparedStatement.setInt(1, zlocin.tipZlocinaId) } just Runs
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setTimestamp(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("DB insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        repositoryInsert.insertZlocinData(zlocin)

        assertEquals(0, zlocin.idZlocin)


        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            //preparedStatement.setInt(1, zlocin.tipZlocinaId)
            //preparedStatement.setString(any(), any())
            //preparedStatement.setTimestamp(any(), any())
           // preparedStatement.executeUpdate()
         //   closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertZlocinData`() {
        val zlocin = returnZlocinData()

        val connection = mockk<Connection>()
        val repositoryInsert = RepositoryInsert(connection)

        every {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
        } returns null

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        repositoryInsert.insertZlocinData(zlocin)
        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
    }


    //osobaData

    @Test
    fun `should handle SQLException during executeUpdate in method insertOsobaData`() {
        val osoba = returnOsobaData()
        val zlocin = ZlocinData(
            idZlocin = 1,
            tipZlocinaId = 1,
            naziv = "Kradja",
            datum = System.currentTimeMillis(),
            mesto = "Beograd",
            opis = "Opis zločina",
            status = "u_istrazi",
        )

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setTimestamp(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("DB insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertOsobaData(osoba, zlocin)

        assertEquals(0, osoba.idOsoba)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, osoba.ime)
            preparedStatement.setString(2, osoba.kontakt)
            preparedStatement.setTimestamp(3, any())
            preparedStatement.setString(4, osoba.zanimanje)
            preparedStatement.setString(5, osoba.pol)
            preparedStatement.setInt(6, zlocin.idZlocin)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }

    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertOsobaData`() {
        val osoba = returnOsobaData()
        val zlocin = returnZlocinData()

        val repositoryInsert = RepositoryInsert(connection)

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertOsobaData(osoba, zlocin)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, osoba.idOsoba)
    }

    //ZrtvaData

    @Test
    fun `should handle SQLException during executeUpdate in method insertZrtva`() {
        val zrtva = returnZrtvaData()
        val zlocin = returnZlocinData().apply { idZlocin = 1 }
        val osoba = returnOsobaData().apply { idOsoba = 1 }

        val originalOsoba = zrtva.osobaId

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertZrtva(zrtva, zlocin, osoba)

        assertEquals(0, zrtva.idZrtva)
        assertSame(originalOsoba, zrtva.osobaId)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, zrtva.tipZrtve)
            preparedStatement.setString(2, zrtva.detalji)
            preparedStatement.setString(3, zrtva.statusZrtva)
            preparedStatement.setInt(4, zrtva.zlocinId)
            preparedStatement.setInt(5, osoba.idOsoba)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertZrtva`() {
        val osoba = returnOsobaData()
        val zlocin = returnZlocinData()
        val zrtva = returnZrtvaData().apply {
            osobaId = OsobaData(idOsoba = -1, ime = "", kontakt = "", datum = 0L, zanimanje = "", pol = "", zlocinId = 0)
        }

        val initialOsoba = zrtva.osobaId

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertZrtva(zrtva, zlocin, osoba)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, zrtva.idZrtva)
        assertSame(initialOsoba, zrtva.osobaId) // potvrđuje da se osobaId nije promenio
    }

    //MotivData

    @Test
    fun `should handle SQLException during executeUpdate in method insertMotivData`() {
        val motiv = MotivData(idMotiv = 0, opis = "Test motiv")

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(1, motiv.opis) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertMotivData(motiv)

        assertEquals(0, motiv.idMotiv)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, motiv.opis)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertMotivData`() {
        val motiv = MotivData(idMotiv = 0, opis = "Test motiv")

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertMotivData(motiv)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, motiv.idMotiv)
    }

    //OsumnjiceniData

    @Test
    fun `should handle SQLException during executeUpdate in method insertOsumnjicenData`() {
        val osumnjicen = returnOsumnjiceniData()
        val zlocin = returnZlocinData().apply { idZlocin = 1 }
        val motiv = returnMotivData().apply { idMotiv = 1 }

        val originalId = osumnjicen.idOsumnjicen
        val originalMotiv = osumnjicen.motiv
        val originalZlocinId = osumnjicen.zlocinId

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertOsumnjicenData(osumnjicen, zlocin, motiv)

        assertEquals(originalId, osumnjicen.idOsumnjicen)
        assertEquals(originalMotiv, osumnjicen.motiv)
        assertEquals(originalZlocinId, osumnjicen.zlocinId)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, osumnjicen.status)
            preparedStatement.setString(2, osumnjicen.tipOsumnjicen)
            preparedStatement.setInt(3, motiv.idMotiv)
            preparedStatement.setInt(4, zlocin.idZlocin)
            preparedStatement.setInt(5, osumnjicen.kriv)
            preparedStatement.setInt(6, osumnjicen.osobaId.idOsoba)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertOsumnjicenData`() {
        val osumnjicen = returnOsumnjiceniData().apply {
            idOsumnjicen = 0
            zlocinId = 0
            motiv = returnMotivData().copy(idMotiv = 99, opis = "Originalni motiv")
        }
        val originalMotiv = osumnjicen.motiv
        val originalZlocinId = osumnjicen.zlocinId

        val zlocin = returnZlocinData().apply { idZlocin = 123 }
        val motiv = returnMotivData().apply { idMotiv = 456 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertOsumnjicenData(osumnjicen, zlocin, motiv)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, osumnjicen.idOsumnjicen)
        assertEquals(originalZlocinId, osumnjicen.zlocinId)
        assertEquals(originalMotiv, osumnjicen.motiv)
    }

    //DokazData

    @Test
    fun `should handle SQLException during executeUpdate in method insertDokazData`() {
        val dokaz = returnDokaziData()
        val zlocin = returnZlocinData().apply { idZlocin = 1 }
        val zrtva = returnZrtvaData().apply { idZrtva = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertDokazData(dokaz, zlocin, zrtva)

        assertEquals(0, dokaz.idDokaz)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, dokaz.tipDokaza)
            preparedStatement.setString(2, dokaz.opis)
            preparedStatement.setInt(3, dokaz.status)
            preparedStatement.setInt(4, zlocin.idZlocin)
            preparedStatement.setInt(5, zrtva.idZrtva)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertDokazData`() {
        val dokaz = returnDokaziData().apply {
            idDokaz = 0
            tipDokaza = "nepoznato" // da testira i konverziju
        }
        val zlocin = returnZlocinData()
        val zrtva = returnZrtvaData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertDokazData(dokaz, zlocin, zrtva)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, dokaz.idDokaz)
    }

    //SvedokData

    @Test
    fun `should handle SQLException during executeUpdate in method insertSvedokData`() {
        val svedok = returnSvedokData()
        val zlocin = returnZlocinData().apply { idZlocin = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertSvedokData(svedok, zlocin)

        assertEquals(0, svedok.idSvedok)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, svedok.izjava)
            preparedStatement.setString(2, svedok.statusSvedok)
            preparedStatement.setInt(3, svedok.statusIspitan)
            preparedStatement.setInt(4, zlocin.idZlocin)
            preparedStatement.setInt(5, svedok.osobaId.idOsoba)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertSvedokData`() {
        val svedok = returnSvedokData().apply {
            idSvedok = 0
        }
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertSvedokData(svedok, zlocin)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, svedok.idSvedok)
    }

    //ObdukcijaData

    @Test
    fun `should handle SQLException during executeUpdate in method insertObdukcijaData`() {
        val obdukcija = returnObdukcijaData()
        val zrtva = returnZrtvaData().apply { idZrtva = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setTimestamp(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertObdukcijaData(obdukcija, zrtva)

        assertEquals(0, obdukcija.idObdukcija)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, obdukcija.izvestaj)
            preparedStatement.setTimestamp(2, Timestamp(obdukcija.datum))
            preparedStatement.setString(3, obdukcija.uzrokSmrti)
            preparedStatement.setInt(4, zrtva.idZrtva)
            preparedStatement.setString(5, obdukcija.informacije)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertObdukcijaData`() {
        val obdukcija = returnObdukcijaData().apply {
            idObdukcija = 0
        }
        val zrtva = returnZrtvaData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertObdukcijaData(obdukcija, zrtva)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, obdukcija.idObdukcija)
    }

    //ForenzickiDokazData

    @Test
    fun `should handle SQLException during executeUpdate in method insertForenzickiDokaz`() {
        val dokaz = returnForenzickiDokazData()
        val zrtva = returnZrtvaData().apply { idZrtva = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertForenzickiDokaz(dokaz, zrtva)

        assertEquals(0, dokaz.idForenzickiDokaz)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, dokaz.tipForenzickiDokaz)
            preparedStatement.setString(2, dokaz.opis)
            preparedStatement.setInt(3, dokaz.statusS)
            preparedStatement.setInt(4, zrtva.idZrtva)
            preparedStatement.setString(5, dokaz.veza)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertForenzickiDokaz`() {
        val dokaz = returnForenzickiDokazData().apply {
            idForenzickiDokaz = 0
        }
        val zrtva = returnZrtvaData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertForenzickiDokaz(dokaz, zrtva)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, dokaz.idForenzickiDokaz)
    }

    //TelefonData

    @Test
    fun `should handle SQLException during executeUpdate in method insertTelefonData`() {
        val telefon = returnTelefonData()
        val zrtva = returnZrtvaData().apply { idZrtva = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertTelefonData(telefon, zrtva)

        assertEquals(0, telefon.idTelefon)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, telefon.model)
            preparedStatement.setString(2, telefon.os)
            preparedStatement.setInt(3, zrtva.idZrtva)
            preparedStatement.setString(4, telefon.sifra)
            preparedStatement.setString(5, telefon.informacije)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertTelefonData`() {
        val telefon = returnTelefonData().apply { idTelefon = 0 }
        val zrtva = returnZrtvaData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertTelefonData(telefon, zrtva)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, telefon.idTelefon)
    }

    //AplikacijaData

    @Test
    fun `should handle SQLException during executeUpdate in method insertAplikacijaData`() {
        val aplikacija = returnAplikacijaData()
        val zrtva = returnZrtvaData().apply { idZrtva = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.setBoolean(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertAplikacijaData(aplikacija, zrtva)

        assertEquals(0, aplikacija.idAplikacije)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, aplikacija.naziv)
            preparedStatement.setInt(2, aplikacija.tip)
            preparedStatement.setInt(3, zrtva.idZrtva)
            preparedStatement.setBoolean(4, aplikacija.aktivna)
            preparedStatement.setString(5, aplikacija.informacije)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertAplikacijaData`() {
        val aplikacija = returnAplikacijaData().apply { idAplikacije = 0 }
        val zrtva = returnZrtvaData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertAplikacijaData(aplikacija, zrtva)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, aplikacija.idAplikacije)
    }

    //TragData

    @Test
    fun `should handle SQLException during executeUpdate in method insertTragData`() {
        val trag = returnTragData()
        val forenzickiDokaz = returnForenzickiDokazData().apply { idForenzickiDokaz = 1 }
        val osumnjicen = returnOsumnjiceniData().apply { idOsumnjicen = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertTragData(trag, forenzickiDokaz, osumnjicen)

        assertEquals(0, trag.idTrag)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, forenzickiDokaz.idForenzickiDokaz)
            preparedStatement.setInt(2, osumnjicen.idOsumnjicen)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertTragData`() {
        val trag = returnTragData().apply { idTrag = 0 }
        val forenzickiDokaz = returnForenzickiDokazData().apply { idForenzickiDokaz = 1 }
        val osumnjicen = returnOsumnjiceniData().apply { idOsumnjicen = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertTragData(trag, forenzickiDokaz, osumnjicen)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, trag.idTrag)
    }

    //DokazOsumnjiceniData

    @Test
    fun `should handle SQLException during executeUpdate in method insertDokazOsumnjicenData`() {
        val dokazOsumnjicen = returnDokaziOsumnjiceniData()
        val dokaz = returnDokaziData().apply { idDokaz = 1 }
        val osumnjicen = returnOsumnjiceniData().apply { idOsumnjicen = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertDokazOsumnjicenData(dokazOsumnjicen, dokaz, osumnjicen)

        assertEquals(0, dokazOsumnjicen.idDokazOsumnjicen)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, dokaz.idDokaz)
            preparedStatement.setInt(2, osumnjicen.idOsumnjicen)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertDokazOsumnjicenData`() {
        val dokazOsumnjicen = returnDokaziOsumnjiceniData()
        val dokaz = returnDokaziData().apply { idDokaz = 1 }
        val osumnjicen = returnOsumnjiceniData().apply { idOsumnjicen = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertDokazOsumnjicenData(dokazOsumnjicen, dokaz, osumnjicen)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, dokazOsumnjicen.idDokazOsumnjicen)
    }

    //OneContactData

    @Test
    fun `should handle SQLException during executeUpdate in method insertOneContactData`() {
        val oneContactData = returnOneContact()
        val zlocin = returnZlocinData().apply { idZlocin = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertOneContactData(oneContactData, zlocin)

        assertEquals(0, oneContactData.idOneContact)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, zlocin.idZlocin)
            preparedStatement.setString(2, oneContactData.ime)
            preparedStatement.setString(3, oneContactData.broj)
            preparedStatement.setInt(4, oneContactData.slika)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertOneContactData`() {
        val oneContactData = returnOneContact().apply { idOneContact = 0 }
        val zlocin = returnZlocinData().apply { idZlocin = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertOneContactData(oneContactData, zlocin)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, oneContactData.idOneContact)
    }

    //BeleskaData

    @Test
    fun `should handle SQLException during executeUpdate in method insertBeleskaData`() {
        val beleska = returnBeleskeData()
        val zlocin = returnZlocinData().apply { idZlocin = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setTimestamp(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertBeleskaData(beleska, zlocin)

        assertEquals(0, beleska.idBeleska)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, zlocin.idZlocin)
            preparedStatement.setString(2, beleska.tekst)
            preparedStatement.setTimestamp(3, Timestamp(beleska.datum))
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertBeleskaData`() {
        val beleska = returnBeleskeData().apply { idBeleska = 0 }
        val zlocin = returnZlocinData().apply { idZlocin = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertBeleskaData(beleska, zlocin)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, beleska.idBeleska)
    }

    //WhatsAppKontakt

    @Test
    fun `should handle SQLException during executeUpdate in method insertWhatsAppKontaktData`() {
        val kontakt = returnWhatsAppKontaktiData()
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, zlocin.idZlocin) } just Runs
        every { preparedStatement.setString(2, kontakt.ime) } just Runs
        every { preparedStatement.setString(3, kontakt.broj) } just Runs
        every { preparedStatement.setInt(4, kontakt.slika!!) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertWhatsAppKontaktData(kontakt, zlocin)

        assertEquals(0, kontakt.idWhatsAppKontakt)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, zlocin.idZlocin)
            preparedStatement.setString(2, kontakt.ime)
            preparedStatement.setString(3, kontakt.broj)
            preparedStatement.setInt(4, kontakt.slika!!)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertWhatsAppKontaktData`() {
        val kontakt = returnWhatsAppKontaktiData()
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertWhatsAppKontaktData(kontakt, zlocin)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, kontakt.idWhatsAppKontakt)
    }

    @Test
    fun `should call setNull when slika is null in method insertWhatsAppKontaktData`() {
        val kontakt =  WhatsAppKontaktData(
            idWhatsAppKontakt = 0,
            zlocinId = returnZlocinData().idZlocin,
            ime = "Tom",
            broj = "+1231423152",
            slika = null
        )
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, zlocin.idZlocin) } just Runs
        every { preparedStatement.setString(2, kontakt.ime) } just Runs
        every { preparedStatement.setString(3, kontakt.broj) } just Runs
        every { preparedStatement.setNull(4, Types.INTEGER) } just Runs
        every { preparedStatement.executeUpdate() } returns 1
        every { preparedStatement.generatedKeys } returns resultSet
        every { resultSet.next() } returns true
        every { resultSet.getInt(1) } returns 999

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertWhatsAppKontaktData(kontakt, zlocin)

        assertEquals(999, kontakt.idWhatsAppKontakt)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, zlocin.idZlocin)
            preparedStatement.setString(2, kontakt.ime)
            preparedStatement.setString(3, kontakt.broj)
            preparedStatement.setNull(4, Types.INTEGER)
            preparedStatement.executeUpdate()
            preparedStatement.generatedKeys
            resultSet.next()
            resultSet.getInt(1)
            closeResources(connection, preparedStatement, null)
        }
    }

    //WhatsAppPoruke

    @Test
    fun `should handle SQLException during executeUpdate in method insertWhatsAppPorukaData`() {
        val poruka = returnWhatsAppPorukeData()
        val kontaktKoSalje = returnWhatsAppKontaktiData().apply { idWhatsAppKontakt = 1 }
        val kontaktKomeSalje = returnWhatsAppKontaktiData().apply { idWhatsAppKontakt = 2 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, kontaktKoSalje.idWhatsAppKontakt) } just Runs
        every { preparedStatement.setInt(2, kontaktKomeSalje.idWhatsAppKontakt) } just Runs
        every { preparedStatement.setString(3, poruka.tekst) } just Runs
        every { preparedStatement.setTimestamp(4, any()) } just Runs
        every { preparedStatement.setBoolean(5, poruka.procitana) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertWhatsAppPorukaData(poruka, kontaktKoSalje, kontaktKomeSalje)

        assertEquals(0, poruka.idWhatsAppPoruka)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, kontaktKoSalje.idWhatsAppKontakt)
            preparedStatement.setInt(2, kontaktKomeSalje.idWhatsAppKontakt)
            preparedStatement.setString(3, poruka.tekst)
            preparedStatement.setTimestamp(4, any())
            preparedStatement.setBoolean(5, poruka.procitana)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertWhatsAppPorukaData`() {
        val poruka = returnWhatsAppPorukeData().apply { idWhatsAppPoruka = 0 }
        val kontaktKoSalje = returnWhatsAppKontaktiData().apply { idWhatsAppKontakt = 1 }
        val kontaktKomeSalje = returnWhatsAppKontaktiData().apply { idWhatsAppKontakt = 2 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertWhatsAppPorukaData(poruka, kontaktKoSalje, kontaktKomeSalje)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, poruka.idWhatsAppPoruka)
    }

    //OneCall

    @Test
    fun `should handle SQLException during executeUpdate in method insertOneCallData`() {
        val oneCall = returnOneCallData()
        val kontakt = returnOneContact()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, oneCall.kontakt) } just Runs
        every { preparedStatement.setTimestamp(2, any()) } just Runs
        every { preparedStatement.setBoolean(3, oneCall.propusten) } just Runs
        every { preparedStatement.setBoolean(4, oneCall.dolazni) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repositoryInsert = RepositoryInsert(connection)
        repositoryInsert.insertOneCallData(oneCall, kontakt)

        assertEquals(0, oneCall.idOneCall)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, oneCall.kontakt)
            preparedStatement.setTimestamp(2, any())
            preparedStatement.setBoolean(3, oneCall.propusten)
            preparedStatement.setBoolean(4, oneCall.dolazni)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertOneCallData`() {
        val oneCall = returnOneCallData().apply { idOneCall = 0 }
        val kontakt = returnOneContact()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repositoryInsert = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repositoryInsert.insertOneCallData(oneCall, kontakt)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, oneCall.idOneCall)
    }

    //GalleryData

    @Test
    fun `should call setNull when slika is null in method insertGalleryData`() {
        val gallery = GalleryData(
            idPhoto = 0,
            zlocinId = returnZlocinData().idZlocin,
            slika = null,
            datum = returnTimeStamp(),
            mesto = "Amsterdam"
        )
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, zlocin.idZlocin) } just Runs
        every { preparedStatement.setNull(2, Types.INTEGER) } just Runs
        every { preparedStatement.setTimestamp(3, any()) } just Runs
        every { preparedStatement.setString(4, gallery.mesto) } just Runs
        every { preparedStatement.executeUpdate() } returns 1
        every { preparedStatement.generatedKeys } returns resultSet
        every { resultSet.next() } returns true
        every { resultSet.getInt(1) } returns 123

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertGalleryData(gallery, zlocin)

        assertEquals(123, gallery.idPhoto)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, zlocin.idZlocin)
            preparedStatement.setNull(2, Types.INTEGER)
            preparedStatement.setTimestamp(3, any())
            preparedStatement.setString(4, gallery.mesto)
            preparedStatement.executeUpdate()
            preparedStatement.generatedKeys
            resultSet.next()
            resultSet.getInt(1)
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should handle SQLException during executeUpdate in method insertGalleryData`() {
        val gallery = returnGalleryData()
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, zlocin.idZlocin) } just Runs
        if (gallery.slika != null) {
            every { preparedStatement.setInt(2, gallery.slika!!) } just Runs
        } else {
            every { preparedStatement.setNull(2, Types.INTEGER) } just Runs
        }
        every { preparedStatement.setTimestamp(3, any()) } just Runs
        every { preparedStatement.setString(4, gallery.mesto) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertGalleryData(gallery, zlocin)

        assertEquals(0, gallery.idPhoto)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, zlocin.idZlocin)
            if (gallery.slika != null) {
                preparedStatement.setInt(2, gallery.slika!!)
            } else {
                preparedStatement.setNull(2, Types.INTEGER)
            }
            preparedStatement.setTimestamp(3, any())
            preparedStatement.setString(4, gallery.mesto)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertGalleryData`() {
        val gallery = returnGalleryData().apply { idPhoto = 0 }
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertGalleryData(gallery, zlocin)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, gallery.idPhoto)
    }

    //ObicnaPorukaData

    @Test
    fun `should handle SQLException during executeUpdate in method insertObicnaPorukaData`() {
        val poruka = returnObicnaPorukaData()
        val kontaktKoSalje = returnOneContact().apply { idOneContact = 1 }
        val kontaktKomeSalje = returnOneContact().apply { idOneContact = 2 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, kontaktKoSalje.idOneContact) } just Runs
        every { preparedStatement.setInt(2, kontaktKomeSalje.idOneContact) } just Runs
        every { preparedStatement.setString(3, poruka.tekst) } just Runs
        every { preparedStatement.setTimestamp(4, any()) } just Runs
        every { preparedStatement.setBoolean(5, poruka.procitana) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertObicnaPorukaData(poruka, kontaktKoSalje, kontaktKomeSalje)

        assertEquals(0, poruka.idObicnaPoruka)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, kontaktKoSalje.idOneContact)
            preparedStatement.setInt(2, kontaktKomeSalje.idOneContact)
            preparedStatement.setString(3, poruka.tekst)
            preparedStatement.setTimestamp(4, any())
            preparedStatement.setBoolean(5, poruka.procitana)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertObicnaPorukaData`() {
        val poruka = returnObicnaPorukaData().apply { idObicnaPoruka = 0 }
        val kontaktKoSalje = returnOneContact()
        val kontaktKomeSalje = returnOneContact()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertObicnaPorukaData(poruka, kontaktKoSalje, kontaktKomeSalje)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, poruka.idObicnaPoruka)
    }

    //OdnosOsumnjicenZrtvaData

    @Test
    fun `should handle SQLException during executeUpdate in method insertOdnosOsumnjicenZrtvaData`() {
        val odnos = returnOdnosOsumnjicenZrtvaData()
        val osumnjicen = returnOsumnjiceniData().apply { idOsumnjicen = 1 }
        val zrtva = returnZrtvaData().apply { idZrtva = 2 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, osumnjicen.idOsumnjicen) } just Runs
        every { preparedStatement.setInt(2, zrtva.idZrtva) } just Runs
        every { preparedStatement.setString(3, odnos.tipOdnosa) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Simulirana greska")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertOdnosOsumnjicenZrtvaData(odnos, osumnjicen, zrtva)

        assertEquals(0, odnos.idOdnos)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, osumnjicen.idOsumnjicen)
            preparedStatement.setInt(2, zrtva.idZrtva)
            preparedStatement.setString(3, odnos.tipOdnosa)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertOdnosOsumnjicenZrtvaData`() {
        val odnos = returnOdnosOsumnjicenZrtvaData().apply { idOdnos = 0 }
        val osumnjicen = returnOsumnjiceniData().apply { idOsumnjicen = 1 }
        val zrtva = returnZrtvaData().apply { idZrtva = 2 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertOdnosOsumnjicenZrtvaData(odnos, osumnjicen, zrtva)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, odnos.idOdnos)
    }

    //PitanjeData

    @Test
    fun `should handle SQLException during executeUpdate in method insertPitanjeData`() {
        val pitanje = returnPitanjaData()
        val zlocin = returnZlocinData().apply { idZlocin = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, zlocin.idZlocin) } just Runs
        every { preparedStatement.setString(2, pitanje.tekst) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertPitanjeData(pitanje, zlocin)

        assertEquals(0, pitanje.idPitanje)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, zlocin.idZlocin)
            preparedStatement.setString(2, pitanje.tekst)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertPitanjeData`() {
        val pitanje = returnPitanjaData().apply { idPitanje = 0 }
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertPitanjeData(pitanje, zlocin)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, pitanje.idPitanje)
    }

    //OdgovorData

    @Test
    fun `should handle SQLException during executeUpdate in method insertOdgovorData`() {
        val odgovor = returnOdgovorData()
        val pitanje = returnPitanjaData().apply { idPitanje = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, pitanje.idPitanje) } just Runs
        every { preparedStatement.setString(2, odgovor.tekstOdgovora) } just Runs
        every { preparedStatement.setBoolean(3, odgovor.tacan) } just Runs
        every { preparedStatement.setInt(4, odgovor.bodovi) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertOdgovorData(odgovor, pitanje)

        assertEquals(0, odgovor.idOdogovor)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, pitanje.idPitanje)
            preparedStatement.setString(2, odgovor.tekstOdgovora)
            preparedStatement.setBoolean(3, odgovor.tacan)
            preparedStatement.setInt(4, odgovor.bodovi)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertOdgovorData`() {
        val odgovor = returnOdgovorData().apply { idOdogovor = 0 }
        val pitanje = returnPitanjaData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertOdgovorData(odgovor, pitanje)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, odgovor.idOdogovor)
    }

    //PitanjeIspitivanjeOsumnjicenogData

    @Test
    fun `should handle SQLException during executeUpdate in method insertPitanjeIspitivanjeOsumnjicenogData`() {
        val pitanje = returnPitanjeIspitivanjeOsumnjicenogData()
        val osumnjicen = returnOsumnjiceniData().apply { idOsumnjicen = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(1, pitanje.kategorija) } just Runs
        every { preparedStatement.setString(2, pitanje.tekst) } just Runs
        every { preparedStatement.setString(3, pitanje.odgovor) } just Runs
        every { preparedStatement.setString(4, pitanje.komentar) } just Runs
        every { preparedStatement.setInt(5, osumnjicen.idOsumnjicen) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertPitanjeIspitivanjeOsumnjicenogData(pitanje, osumnjicen)

        assertEquals(0, pitanje.idPitanjeIspitivanjeOsumnjicenog)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, pitanje.kategorija)
            preparedStatement.setString(2, pitanje.tekst)
            preparedStatement.setString(3, pitanje.odgovor)
            preparedStatement.setString(4, pitanje.komentar)
            preparedStatement.setInt(5, osumnjicen.idOsumnjicen)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertPitanjeIspitivanjeOsumnjicenogData`() {
        val pitanje = returnPitanjeIspitivanjeOsumnjicenogData().apply { idPitanjeIspitivanjeOsumnjicenog = 0 }
        val osumnjicen = returnOsumnjiceniData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertPitanjeIspitivanjeOsumnjicenogData(pitanje, osumnjicen)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, pitanje.idPitanjeIspitivanjeOsumnjicenog)
    }

    // PitanjeIspitivanjeSvedoka

    @Test
    fun `should handle SQLException during executeUpdate in method insertPitanjeIspitivanjeSvedokaData`() {
        val pitanje = returnPitanjeIspitivanjeSvedokaData()
        val svedok = returnSvedokData().apply { idSvedok = 1 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(1, pitanje.tekst) } just Runs
        every { preparedStatement.setString(2, pitanje.odgovor) } just Runs
        every { preparedStatement.setInt(3, svedok.idSvedok) } just Runs
        every { preparedStatement.setInt(4, pitanje.nextPitanje) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertPitanjeIspitivanjeSvedokaData(pitanje, svedok)

        assertEquals(0, pitanje.idPitanjeIspitivanjeSvedoka)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, pitanje.tekst)
            preparedStatement.setString(2, pitanje.odgovor)
            preparedStatement.setInt(3, svedok.idSvedok)
            preparedStatement.setInt(4, pitanje.nextPitanje)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }
    @Test
    fun `should print message and return if prepareStatement returns null in method insertPitanjeIspitivanjeSvedokaData`() {
        val pitanje = returnPitanjeIspitivanjeSvedokaData().apply { idPitanjeIspitivanjeSvedoka = 0 }
        val svedok = returnSvedokData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertPitanjeIspitivanjeSvedokaData(pitanje, svedok)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, pitanje.idPitanjeIspitivanjeSvedoka)
    }

    // Zadatak

    @Test
    fun `should handle SQLException during executeUpdate in method insertZadatakData`() {
        val zadatak = returnZadatakData()
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(1, zadatak.tekst) } just Runs
        every { preparedStatement.setString(2, zadatak.korak) } just Runs
        every { preparedStatement.setBoolean(3, zadatak.uradjen) } just Runs
        every { preparedStatement.setNull(4, Types.INTEGER) } just Runs
        every { preparedStatement.setInt(5, zlocin.idZlocin) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertZadatakData(zadatak, zlocin)

        assertEquals(0, zadatak.idZadatak)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, zadatak.tekst)
            preparedStatement.setString(2, zadatak.korak)
            preparedStatement.setBoolean(3, zadatak.uradjen)
            preparedStatement.setNull(4, Types.INTEGER)
            preparedStatement.setInt(5, zlocin.idZlocin)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertZadatakData`() {
        val zadatak = returnZadatakData().apply { idZadatak = 0 }
        val zlocin = returnZlocinData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertZadatakData(zadatak, zlocin)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, zadatak.idZadatak)
    }

    //DokazZadatakData

    @Test
    fun `should handle SQLException during executeUpdate in method insertDokazZadatakData`() {
        val dokazZadatak = returnDokazZadatakData()
        val dokaz = returnDokaziData()
        val zadatak = returnZadatakData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(1, dokazZadatak.tekst) } just Runs
        every { preparedStatement.setInt(2, dokaz.idDokaz) } just Runs
        every { preparedStatement.setBoolean(3, dokazZadatak.uradjen) } just Runs
        every { preparedStatement.setInt(4, zadatak.idZadatak) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertDokazZadatakData(dokazZadatak, dokaz, zadatak)

        assertEquals(0, dokazZadatak.idDokazZadatak)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, dokazZadatak.tekst)
            preparedStatement.setInt(2, dokaz.idDokaz)
            preparedStatement.setBoolean(3, dokazZadatak.uradjen)
            preparedStatement.setInt(4, zadatak.idZadatak)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertDokazZadatakData`() {
        val dokazZadatak = returnDokazZadatakData().apply { idDokazZadatak = 0 }
        val dokaz = returnDokaziData()
        val zadatak = returnZadatakData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertDokazZadatakData(dokazZadatak, dokaz, zadatak)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, dokazZadatak.idDokazZadatak)
    }

    //IspitivanjeOsumnjicenogZadatakData

    @Test
    fun `should handle SQLException during executeUpdate in method insertIspitivanjeOsumnjicenogZadatakData`() {
        val ispitivanjeZadatak = returnIspitivanjeOsumnjicenogZadatakData()
        val osumnjicen = returnOsumnjiceniData()
        val zadatak = returnZadatakData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, osumnjicen.idOsumnjicen) } just Runs
        every { preparedStatement.setInt(2, zadatak.idZadatak) } just Runs
        every { preparedStatement.setBoolean(3, ispitivanjeZadatak.uradjen) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertIspitivanjeOsumnjicenogZadatakData(ispitivanjeZadatak, osumnjicen, zadatak)

        assertEquals(0, ispitivanjeZadatak.idIspitivanjeOsumnjicenogZadatak)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, osumnjicen.idOsumnjicen)
            preparedStatement.setInt(2, zadatak.idZadatak)
            preparedStatement.setBoolean(3, ispitivanjeZadatak.uradjen)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertIspitivanjeOsumnjicenogZadatakData`() {
        val ispitivanjeZadatak = returnIspitivanjeOsumnjicenogZadatakData().apply { idIspitivanjeOsumnjicenogZadatak = 0 }
        val osumnjicen = returnOsumnjiceniData()
        val zadatak = returnZadatakData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertIspitivanjeOsumnjicenogZadatakData(ispitivanjeZadatak, osumnjicen, zadatak)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, ispitivanjeZadatak.idIspitivanjeOsumnjicenogZadatak)
    }

    //IspitivanjeSvedokaZadatakData

    @Test
    fun `should handle SQLException during executeUpdate in method insertIspitivanjeSvedokaZadatakData`() {
        val ispitivanjeZadatak = returnIspitivanjeSvedokaZadatakData()
        val svedok = returnSvedokData()
        val zadatak = returnZadatakData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, svedok.idSvedok) } just Runs
        every { preparedStatement.setInt(2, zadatak.idZadatak) } just Runs
        every { preparedStatement.setBoolean(3, ispitivanjeZadatak.uradjen) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertIspitivanjeSvedokaZadatakData(ispitivanjeZadatak, svedok, zadatak)

        assertEquals(0, ispitivanjeZadatak.idIspitivanjeSvedokaZadatak)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, svedok.idSvedok)
            preparedStatement.setInt(2, zadatak.idZadatak)
            preparedStatement.setBoolean(3, ispitivanjeZadatak.uradjen)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertIspitivanjeSvedokaZadatakData`() {
        val ispitivanjeZadatak = returnIspitivanjeSvedokaZadatakData().apply { idIspitivanjeSvedokaZadatak = 0 }
        val svedok = returnSvedokData()
        val zadatak = returnZadatakData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertIspitivanjeSvedokaZadatakData(ispitivanjeZadatak, svedok, zadatak)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, ispitivanjeZadatak.idIspitivanjeSvedokaZadatak)
    }

    //TelefonZadatakData

    @Test
    fun `should handle SQLException during executeUpdate in method insertTelefonZadatakData`() {
        val telefonZadatakData = returnTelefonZadatakData()
        val telefonData = returnTelefonData()
        val zadatakData = returnZadatakData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, telefonData.idTelefon) } just Runs
        every { preparedStatement.setInt(2, zadatakData.idZadatak) } just Runs
        every { preparedStatement.setBoolean(3, telefonZadatakData.uradjen) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertTelefonZadatakData(telefonZadatakData, telefonData, zadatakData)

        assertEquals(0, telefonZadatakData.idTelefonZadatak)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, telefonData.idTelefon)
            preparedStatement.setInt(2, zadatakData.idZadatak)
            preparedStatement.setBoolean(3, telefonZadatakData.uradjen)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in method insertTelefonZadatakData`() {
        val telefonZadatakData = returnTelefonZadatakData().apply { idTelefonZadatak = 0 }
        val telefonData = returnTelefonData()
        val zadatakData = returnZadatakData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val repository = RepositoryInsert(connection)

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        repository.insertTelefonZadatakData(telefonZadatakData, telefonData, zadatakData)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, telefonZadatakData.idTelefonZadatak)
    }

    //ForenzickiDokazZadatakData

    @Test
    fun `should handle SQLException during executeUpdate in insertForenzickiDokazZadatakData`() {
        val zadatak = returnZadatakData()
        val forenzickiDokaz = returnForenzickiDokazData()
        val data = returnForenzickiDokazZadatakData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(1, data.tekst) } just Runs
        every { preparedStatement.setInt(2, forenzickiDokaz.idForenzickiDokaz) } just Runs
        every { preparedStatement.setBoolean(3, data.uradjen) } just Runs
        every { preparedStatement.setInt(4, zadatak.idZadatak) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repository = RepositoryInsert(connection)
        repository.insertForenzickiDokazZadatakData(data, forenzickiDokaz, zadatak)

        assertEquals(0, data.idForenzickiDokazZadatak)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, data.tekst)
            preparedStatement.setInt(2, forenzickiDokaz.idForenzickiDokaz)
            preparedStatement.setBoolean(3, data.uradjen)
            preparedStatement.setInt(4, zadatak.idZadatak)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in insertForenzickiDokazZadatakData`() {
        val zadatak = returnZadatakData()
        val forenzickiDokaz = returnForenzickiDokazData()
        val data = returnForenzickiDokazZadatakData().apply { idForenzickiDokazZadatak = 0 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val repository = RepositoryInsert(connection)
        repository.insertForenzickiDokazZadatakData(data, forenzickiDokaz, zadatak)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, data.idForenzickiDokazZadatak)
    }

    //PacijentData

    @Test
    fun `should handle SQLException during executeUpdate in insertPacijentData`() {
        val pacijent = returnPacijentData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(1, pacijent.simptomi) } just Runs
        every { preparedStatement.setString(2, any()) } just Runs
        every { preparedStatement.setTimestamp(3, any()) } just Runs
        every { preparedStatement.setInt(4, pacijent.prijavio.idOsoba) } just Runs
        every { preparedStatement.setInt(5, pacijent.zlocinId.idZlocin) } just Runs
        every { preparedStatement.setInt(6, pacijent.zrtvaId.idZrtva) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repo = RepositoryInsert(connection)
        repo.insertPacijentData(pacijent)

        assertEquals(0, pacijent.idPacijent)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, pacijent.simptomi)
            preparedStatement.setString(2, "ziva")
            preparedStatement.setTimestamp(3, any())
            preparedStatement.setInt(4, pacijent.prijavio.idOsoba)
            preparedStatement.setInt(5, pacijent.zlocinId.idZlocin)
            preparedStatement.setInt(6, pacijent.zrtvaId.idZrtva)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in insertPacijentData`() {
        val pacijent = returnPacijentData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val repo = RepositoryInsert(connection)
        repo.insertPacijentData(pacijent)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, pacijent.idPacijent)
    }

    //MedicinskiIzvestajData

    @Test
    fun `should handle SQLException during executeUpdate in insertMedicinskiIzvestajData`() {
        val medicinskiIzvestaj = returnMedicinskiIzvestajData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repo = RepositoryInsert(connection)
        repo.insertMedicinskiIzvestajData(medicinskiIzvestaj)

        assertEquals(0, medicinskiIzvestaj.idMedicinskiIzvestaj)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(any(), any())
            preparedStatement.setString(any(), any())
            preparedStatement.setString(any(), any())
            preparedStatement.setString(any(), any())
            preparedStatement.setString(any(), any())
            preparedStatement.setString(any(), any())
            preparedStatement.setInt(any(), any())
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    @Test
    fun `should print message and return if prepareStatement returns null in insertMedicinskiIzvestajData`() {
        val medicinskiIzvestaj = returnMedicinskiIzvestajData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val repo = RepositoryInsert(connection)
        repo.insertMedicinskiIzvestajData(medicinskiIzvestaj)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, medicinskiIzvestaj.idMedicinskiIzvestaj)
    }

    //LekarskiTestData

    @Test
    fun `should handle prepareStatement returning null in insertLekarskiTestData`() {
        val lekarskiTest = returnLekarskiTestData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val repo = RepositoryInsert(connection)
        repo.insertLekarskiTestData(lekarskiTest)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, lekarskiTest.idLekarskiTest)
    }

    @Test
    fun `should handle SQLException during executeUpdate in insertLekarskiTestData`() {
        val lekarskiTest = returnLekarskiTestData().apply { idLekarskiTest = 0 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setInt(1, lekarskiTest.pacijentId.idPacijent) } just Runs
        every { preparedStatement.setString(2, lekarskiTest.izvestaj) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repo = RepositoryInsert(connection)
        repo.insertLekarskiTestData(lekarskiTest)

        assertEquals(0, lekarskiTest.idLekarskiTest)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setInt(1, lekarskiTest.pacijentId.idPacijent)
            preparedStatement.setString(2, lekarskiTest.izvestaj)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    //LokacijeIstrageData

    @Test
    fun `should print message and return if prepareStatement returns null in insertLokacijeIstrageData`() {
        val lokacija = returnLokacijeIstrageData().apply { idLokacijeIstrage = 0 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val repo = RepositoryInsert(connection)
        repo.insertLokacijeIstrageData(lokacija)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, lokacija.idLokacijeIstrage)
    }

    @Test
    fun `should handle SQLException during executeUpdate in insertLokacijeIstrageData`() {
        val lokacija = returnLokacijeIstrageData().apply { idLokacijeIstrage = 0 }

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(any(), any()) } just Runs
        every { preparedStatement.setInt(any(), any()) } just Runs
        every { preparedStatement.setDouble(any(), any()) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repo = RepositoryInsert(connection)
        repo.insertLokacijeIstrageData(lokacija)

        assertEquals(0, lokacija.idLokacijeIstrage)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, lokacija.mesto)
            preparedStatement.setString(2, lokacija.naziv)
            preparedStatement.setString(3, lokacija.opis)
            preparedStatement.setInt(4, lokacija.zlocinId)
            preparedStatement.setDouble(5, lokacija.geoTackaALatitude)
            preparedStatement.setDouble(6, lokacija.geoTackaALongitude)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }

    //IzjavaZaPacijentaData

    @Test
    fun `should print message and return if prepareStatement returns null in insertIzjavaZaPacijentaData`() {
        val izjava = returnIzjavaZaPacijentaData().apply { idIzjavaZaPacijenta = 0 }
        val pacijent = returnPacijentData()
        val osoba = returnOsobaData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns null

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val repo = RepositoryInsert(connection)
        repo.insertIzjavaZaPacijentaData(izjava, pacijent, osoba)

        System.setOut(System.out)
        val output = outputStream.toString().trim()

        assertTrue(output.contains("Prepare statement failed: statement is null"))
        assertEquals(0, izjava.idIzjavaZaPacijenta)
    }

    @Test
    fun `should handle SQLException during executeUpdate in insertIzjavaZaPacijentaData`() {
        val izjava = returnIzjavaZaPacijentaData().apply { idIzjavaZaPacijenta = 0 }
        val pacijent = returnPacijentData()
        val osoba = returnOsobaData()

        every { connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS) } returns preparedStatement
        every { preparedStatement.setString(1, izjava.izjava) } just Runs
        every { preparedStatement.setInt(2, pacijent.idPacijent) } just Runs
        every { preparedStatement.setInt(3, osoba.idOsoba) } just Runs
        every { preparedStatement.executeUpdate() } throws SQLException("Insert failed")

        mockkStatic("com.example.ApplicationKt")
        every { closeResources(connection, preparedStatement, null) } just Runs

        val repo = RepositoryInsert(connection)
        repo.insertIzjavaZaPacijentaData(izjava, pacijent, osoba)

        assertEquals(0, izjava.idIzjavaZaPacijenta)

        verifySequence {
            connection.prepareStatement(any(), Statement.RETURN_GENERATED_KEYS)
            preparedStatement.setString(1, izjava.izjava)
            preparedStatement.setInt(2, pacijent.idPacijent)
            preparedStatement.setInt(3, osoba.idOsoba)
            preparedStatement.executeUpdate()
            closeResources(connection, preparedStatement, null)
        }
    }
}