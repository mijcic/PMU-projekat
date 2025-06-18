package com.example.unit

import com.example.data.remote.tables.*
import com.example.repository.RepoInterface
import com.example.service.get.GeminiMysteriousSymptomsService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class GeminiMysteriousSymptomsServiceTest {
    private lateinit var repository: RepoInterface
    private lateinit var service: GeminiMysteriousSymptomsService

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        service = GeminiMysteriousSymptomsService(repository)
    }

    private fun returnZlocinData(): ZlocinData {
        return ZlocinData(
            tipZlocinaId = 1,
            naziv = "Ubistvo u tramvaju",
            datum = System.currentTimeMillis(),
            mesto = "Pariz",
            opis = "Ubistvo zene",
            status = "u_istrazi",
            idZlocin = 1
        )
    }

    private fun returnTipZlocinaData(): TipZlocinaDC {
        return TipZlocinaDC(1,"mysterious symptoms")
    }

    private fun returnZrtvaData(): ZrtvaData {
        return ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica se zali da ne moze da dise i da je udahnula veliku kolicinu nekog gasa u tramvaju.",
            statusZrtva = "ziva",
            zlocinId = returnZlocinData().idZlocin,
            osobaId = returnOsobaData().first()
        )
    }

    private fun returnTimeStamp():Long{
        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it, formatter2) }
        return dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    private fun returnZadaciData(): List<ZadatakData>{
        val zlocin = returnZlocinData()

        return listOf(
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
    }

    private fun returnOsobaData(): List<OsobaData>{
        return listOf(
            OsobaData(
            idOsoba = 2,
            ime = "Tomas Black",
            kontakt = "+4433337888999",
            datum = returnTimeStamp(),
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = returnZlocinData().idZlocin
        )
        )
    }

    private fun returnDokaziData():List<DokazData>{
        return listOf(
            DokazData(
            idDokaz = 1,
            tipDokaza = "fizicki",
            opis = "Pistolj pronadjen na mestu zlocina.",
            zlocinId = returnZlocinData().idZlocin,
            zrtvaId = returnZrtvaData().idZrtva,
            status = 0
        )
        )
    }

    private fun returnTelefonData():List<TelefonData>{
        return listOf(
            TelefonData(
            idTelefon = 1,
            model = "Samsung Galaxy S22",
            os = "Android",
            sifra = "1234",
            informacije = "Pronađene su poruke sa pretnjama.",
            zrtvaId = returnZrtvaData().idZrtva
        )
        )
    }

    private fun returnForenzickiDokaziData():List<ForenzickiDokazData>{
        return listOf(
            ForenzickiDokazData(
            idForenzickiDokaz = 1,
            tipForenzickiDokaz = "DNK",
            opis = "DNK tragovi pronađeni na pištolju.",
            statusS = 0,
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.",
            zrtvaId = returnZrtvaData().idZrtva
        )
        )
    }

    private fun returnOneContact(): List<OneContactData>{
        return listOf(
            OneContactData(
                idOneContact = 1,
                zlocinId = returnZlocinData().idZlocin,
                ime = "John",
                broj = "+54533465645",
                slika = 1,
            ),
            OneContactData(
                idOneContact = 2,
                zlocinId = returnZlocinData().idZlocin,
                ime = "Tom",
                broj = "+54524565611",
                slika = 1,
            )
        )
    }

    private fun returnAplikacijeData(): List<AplikacijaData>{
        return listOf(
            AplikacijaData(
            idAplikacije = 1,
            naziv = "Instagram",
            tip = 0,
            zrtvaId = returnZrtvaData(),
            aktivna = true,
            informacije = "Poslednja aktivnost na Instagram profilu žrtve."
        )
        )
    }

    private fun returnBeleskeData(): List<BeleskaData>{
        return listOf(
            BeleskaData(
            idBeleska = 1,
            zlocinId = returnZlocinData().idZlocin,
            tekst = "Moram da stignem pre njega",
            datum = returnTimeStamp()
        )
        )
    }

    private fun returnWhatsAppKontaktiData(): List<WhatsAppKontaktData>{
        return listOf(
            WhatsAppKontaktData(
                idWhatsAppKontakt = 1,
                zlocinId = returnZlocinData().idZlocin,
                ime = "Tom",
                broj = "+1231423152",
                slika = 13
            ),
            WhatsAppKontaktData(
                idWhatsAppKontakt = 2,
                zlocinId = returnZlocinData().idZlocin,
                ime = "John",
                broj = "+1431423152",
                slika = 13
            )
        )
    }

    private fun returnWhatsAppPorukeData(): List<WhatsAppPorukaData>{
        return listOf(
            WhatsAppPorukaData(
            idWhatsAppPoruka = 1,
            kontaktKoSalje = returnWhatsAppKontaktiData().first().idWhatsAppKontakt,
            kontaktKomeSalje = returnWhatsAppKontaktiData()[1].idWhatsAppKontakt,
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.",
            datum = returnTimeStamp(),
            procitana = false
        )
        )
    }

    private fun returnOneCallData(): List<OneCallData>{
        return listOf(
            OneCallData(
            idOneCall = 1,
            kontakt = returnOneContact().first().idOneContact,
            datum = returnTimeStamp(),
            propusten = false,
            dolazni = true,
            zrtvaId = returnZrtvaData().idZrtva
        )
        )
    }

    private fun returnObicnaPorukaData(): List<ObicnaPorukaData>{
        return listOf(
            ObicnaPorukaData(
            idObicnaPoruka = 1,
            kontaktKoSalje = returnOneContact().first().idOneContact,
            kontaktKomeSalje = returnOneContact()[1].idOneContact,
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.",
            datum = returnTimeStamp(),
            procitana = false
        )
        )
    }

    private fun returnDokaziZadaciData(): List<DokazZadatakData>{
        return listOf(
            DokazZadatakData(
            idDokazZadatak = 1,
            tekst = "Posalji dokaz na forenzicku analizu",
            dokazId = returnDokaziData().first().idDokaz,
            uradjen = false,
            zadatakId = returnZadaciData().first().idZadatak
        )
        )
    }

    private fun returnTelefonZadaciData(): List<TelefonZadatakData>{
        return listOf(
            TelefonZadatakData(
            idTelefonZadatak = 1,
            telefonId = returnTelefonData().first().idTelefon,
            zadatakId = returnZadaciData().first().idZadatak,
            uradjen = true
        )
        )
    }

    private fun returnForenzickiDokazZadatakData(): List<ForenzickiDokazZadatakData>{
        return listOf(
            ForenzickiDokazZadatakData(
            idForenzickiDokazZadatak = 1,
            tekst = "Otkrij kojoj zeni pripada DNK.",
            forenzickiDokazId = returnForenzickiDokaziData().first().idForenzickiDokaz,
            uradjen = true,
            zadatakId = returnZadaciData().first().idZadatak
        )
        )
    }

    private fun returnGalleryData(): List<GalleryData>{
        return listOf(
            GalleryData(
            idPhoto = 1,
            zlocinId = returnZlocinData().idZlocin,
            slika = 1,
            datum = returnTimeStamp(),
            mesto = "Amsterdam"
        )
        )
    }

    private fun returnPitanjaData(): List<PitanjeData>{
        return listOf(
            PitanjeData(
            idPitanje = 1,
            zlocinId = returnZlocinData().idZlocin,
            tekst = "Da li si otisao do njene sobe?"
        )
        )
    }

    private fun returnOdgovorData(): List<OdgovorData>{
        return listOf(
            OdgovorData(
            idOdogovor = 1,
            pitanjeId = returnPitanjaData().first().idPitanje,
            tekstOdgovora = "Jesam, kaze da nije umesana.",
            tacan = false,
            bodovi = 10
        )
        )
    }

    private fun returnPacijentData(): PacijentData {
        return PacijentData(
            idPacijent = 1,
            simptomi = "Visoka temperatura, glavobolja",
            statusPacijenta = "Kritično",
            datumPrijave = returnTimeStamp(),
            prijavio = returnOsobaData().get(0),
            zlocinId = returnZlocinData(),
            zrtvaId = returnZrtvaData()
        )
    }

    private fun returnMedicinskiIzvestajData(): MedicinskiIzvestajData {
        return MedicinskiIzvestajData(
            idMedicinskiIzvestaj = 1,
            rezime = "Pacijent primljen sa simptomima visoke temperature i dezorijentacije.",
            CTnalaz = "CT pokazuje blago uvećanje limfnih čvorova u abdomenu.",
            MRInalaz = "MRI nalaz ukazuje na upalu moždanih ovojnica.",
            krvnaSlika = "Povišen broj leukocita, smanjen nivo hemoglobina.",
            toksikoloskeAnalize = "Prisustvo tragova opijata u uzorcima krvi.",
            zakljucak = "Postoje indikacije trovanja nepoznatom supstancom, potrebno dalje testiranje.",
            pacijentId = returnPacijentData()
        )
    }

    private fun returnLekarskiTest(): LekarskiTestData {
        return LekarskiTestData(
            idLekarskiTest = 1,
            pacijentId = returnPacijentData(),
            izvestaj = "Testovi ukazuju na prisustvo toksina u krvotoku. Nema znakova fizičke traume. Potrebna dodatna toksikološka analiza."
        )
    }

    private fun returnLokacijeIstrage(): List<LokacijeIstrageData>? {
        return listOf(
            LokacijeIstrageData(
                idLokacijeIstrage = 1,
                mesto = "Beograd",
                naziv = "Napustena fabrika",
                opis = "Mesto zločina pronađeno u napuštenoj fabrici na periferiji grada. Prisutan trag krvi i tragovi borbe.",
                zlocinId = returnZlocinData().idZlocin,
                geoTackaALatitude = 44.7866,
                geoTackaALongitude = 20.4489
            )
        )
    }

    private fun returnIzjavaZaPacijenta(): IzjavaZaPacijentaData {
        return IzjavaZaPacijentaData(
            idIzjavaZaPacijenta = 1,
            izjava = "Pacijent je poslednjih nekoliko dana pokazivao neobično ponašanje i žalio se na jake glavobolje.",
            pacijentId = returnPacijentData(),
            osobaId = returnOsobaData().get(0)
        )
    }

    @Test
    fun `should return null when getUsedZlocinMysteriousSymptoms returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns null
        //Kad god neko pozove getUsedZlocinMurder(), vrati null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getZlocin returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        //mockujemo sve prethodne zavisnosti koje vode do metode koju zelimo da testiramo
        every { repository.getZlocin(1) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getTipZlocina returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getZlocin(1) } returns returnZlocinData()
        every { repository.getTipZlocina(1) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getZrtva returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getZrtva(1) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getDokazi returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getDokazi(1, zrtva) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getTelefon returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getTelefon(zrtva.idZrtva) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getForenzickiDokazi returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getForenzickiDokazi(zrtva.idZrtva) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getOneContact returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getOneContact(1) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getGalerija returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        val zrtva= returnZrtvaData()
        every { repository.getZlocin(1) } returns returnZlocinData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getGalerija(1,zrtva) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getAplikacije returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getAplikacije(1, zrtva) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getBeleske returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getBeleske(1,zrtva) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getWhatsAppKontakt returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getWhatsAppKontakt(1,zrtva) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getWhatsAppPoruka returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getWhatsAppPoruka(1, listOf()) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getOneCall returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getOneCall(1, listOf()) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getObicnaPoruka returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getObicnaPoruka(1, listOf()) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getZadaci returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        var zlocin = returnZlocinData()
        val zrtva = returnZrtvaData()
        every { repository.getZlocin(1) } returns zlocin
        every { repository.getZrtva(zlocin.idZlocin) } returns zrtva
        every { repository.getZadaci(zlocin.idZlocin) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getDokaziZadaci returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        var zlocin = returnZlocinData()
        val zrtva = returnZrtvaData()
        every { repository.getZlocin(1) } returns zlocin
        every { repository.getZrtva(zlocin.idZlocin) } returns zrtva
        every { repository.getZadaci(zlocin.idZlocin) } returns returnZadaciData()
        every { repository.getDokaziZadaci(zlocin.idZlocin, any()) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getTelefonZadaci returns null`() {
        val zlocin = returnZlocinData()
        val zrtva = returnZrtvaData()
        val zadaci = returnZadaciData()

        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getZlocin(1) } returns zlocin
        every { repository.getZrtva(zlocin.idZlocin) } returns zrtva
        every { repository.getZadaci(zlocin.idZlocin) } returns zadaci

        every { repository.getTelefonZadaci(zlocin.idZlocin, zadaci) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getForenzickiDokazZadatak returns null`() {
        val zlocin = returnZlocinData()
        val zrtva = returnZrtvaData()
        val zadaci = returnZadaciData()

        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getZlocin(1) } returns zlocin
        every { repository.getZrtva(zlocin.idZlocin) } returns zrtva
        every { repository.getZadaci(zlocin.idZlocin) } returns zadaci

        every { repository.getForenzickiDokazZadatak(zlocin.idZlocin, zadaci) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getGallery returns null`() {
        val zlocin = returnZlocinData()
        val zrtva = returnZrtvaData()
        val zadaci = returnZadaciData()
        val dokazi = returnDokaziZadaciData()

        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getZlocin(1) } returns zlocin
        every { repository.getZrtva(zlocin.idZlocin) } returns zrtva
        every { repository.getZadaci(zlocin.idZlocin) } returns zadaci
        every { repository.getDokaziZadaci(zlocin.idZlocin, zadaci) } returns dokazi
        every { repository.getGallery(zlocin.idZlocin) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getPitanja returns null`() {
        val zlocin = returnZlocinData()
        val zrtva = returnZrtvaData()
        val zadaci = returnZadaciData()
        val dokazi = returnDokaziZadaciData()

        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getZlocin(1) } returns zlocin
        every { repository.getZrtva(zlocin.idZlocin) } returns zrtva
        every { repository.getZadaci(zlocin.idZlocin) } returns zadaci
        every { repository.getDokaziZadaci(zlocin.idZlocin, zadaci) } returns dokazi
        every { repository.getPitanja(1) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getOdgovor returns null`() {
        val zlocin = returnZlocinData()
        val zadaci = returnZadaciData()

        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getZlocin(1) } returns zlocin
        every { repository.getZadaci(zlocin.idZlocin) } returns zadaci

        every { repository.getGallery(1) } returns returnGalleryData()
        every { repository.getOsobe(1) } returns returnOsobaData()
        every { repository.getPitanja(1) } returns returnPitanjaData()

        every { repository.getOdgovor(1) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getOsobe returns null`() {
        val zlocin = returnZlocinData()
        val zadaci = returnZadaciData()
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getZlocin(1) } returns zlocin
        every { repository.getZadaci(zlocin.idZlocin) } returns zadaci
        every { repository.getGallery(1) } returns returnGalleryData()
        every { repository.getPitanja(1) } returns returnPitanjaData()
        every { repository.getOdgovor(1) } returns returnOdgovorData()
        every { repository.getOsobe(1) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getPacijent returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        var zlocin = returnZlocinData()
        every { repository.getZlocin(1) } returns zlocin
        val osobe = returnOsobaData()
        every { repository.getOsobe(1) } returns osobe
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getPacijent(1, zlocin, zrtva, osobe) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getMedicinskiIzvetaj returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        var zlocin = returnZlocinData()
        every { repository.getZlocin(1) } returns zlocin
        val osobe = returnOsobaData()
        every { repository.getOsobe(1) } returns osobe
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        val pacijent = returnPacijentData()
        every { repository.getPacijent(1, zlocin, zrtva, osobe) } returns pacijent
        every { repository.getMedicinskiIzvetaj(pacijent) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getLekarskiTest returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        var zlocin = returnZlocinData()
        every { repository.getZlocin(1) } returns zlocin
        val osobe = returnOsobaData()
        every { repository.getOsobe(1) } returns osobe
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        val pacijent = returnPacijentData()
        every { repository.getPacijent(1, zlocin, zrtva, osobe) } returns pacijent
        every { repository.getLekarskiTest(pacijent) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getLokacijeIstrage returns null`() {
        val zlocin = returnZlocinData()
        val osobe = returnOsobaData()
        val zrtva = returnZrtvaData()
        val pacijent = returnPacijentData()

        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        every { repository.getZlocin(1) } returns zlocin
        every { repository.getOsobe(1) } returns osobe
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getPacijent(1, zlocin, zrtva, osobe) } returns pacijent
        every { repository.getMedicinskiIzvetaj(pacijent) } returns returnMedicinskiIzvestajData()
        every { repository.getLekarskiTest(pacijent) } returns returnLekarskiTest()

        every { repository.getLokacijeIstrage(1) } returns null

        every { repository.getIzjavaZaPacijenta(pacijent, osobe) } returns returnIzjavaZaPacijenta()

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return null when getIzjavaZaPacijenta returns null`() {
        every { repository.getUsedZlocinMysteriousSymptoms() } returns 1
        var zlocin = returnZlocinData()
        every { repository.getZlocin(1) } returns zlocin
        val osobe = returnOsobaData()
        every { repository.getOsobe(1) } returns osobe
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        val pacijent = returnPacijentData()
        every { repository.getPacijent(1, zlocin, zrtva, osobe) } returns pacijent
        every { repository.getIzjavaZaPacijenta(pacijent, osobe) } returns null

        val result = service.getGeminiMysteriousSymtoms()

        assertNull(result)
    }

    @Test
    fun `should return GeminiResponseRetrofit when all data is present`() {
        val id = 1
        val zlocin = returnZlocinData()
        val tipZlocina = returnTipZlocinaData()
        val zrtva = returnZrtvaData()
        val osobe = returnOsobaData()
        val pacijent = returnPacijentData()

        every { repository.getUsedZlocinMysteriousSymptoms() } returns id
        every { repository.getZlocin(id) } returns zlocin
        every { repository.getTipZlocina(zlocin.tipZlocinaId) } returns tipZlocina
        every { repository.getZrtva(id) } returns zrtva
        every { repository.getDokazi(id, zrtva) } returns returnDokaziData()
        every { repository.getZadaci(id) } returns returnZadaciData()
        every { repository.getOsobe(id) } returns osobe
        every { repository.getTelefon(zrtva.idZrtva) } returns returnTelefonData()
        every { repository.getForenzickiDokazi(zrtva.idZrtva) } returns returnForenzickiDokaziData()
        every { repository.getOneContact(id) } returns returnOneContact()
        every { repository.getAplikacije(id, zrtva) } returns returnAplikacijeData()
        every { repository.getBeleske(id, zrtva) } returns returnBeleskeData()
        every { repository.getWhatsAppKontakt(id, zrtva) } returns returnWhatsAppKontaktiData()
        every { repository.getWhatsAppPoruka(id, any()) } returns returnWhatsAppPorukeData()
        every { repository.getOneCall(id, any()) } returns returnOneCallData()
        every { repository.getObicnaPoruka(id, any()) } returns returnObicnaPorukaData()
        every { repository.getDokaziZadaci(id, any()) } returns returnDokaziZadaciData()
        every { repository.getTelefonZadaci(id, any()) } returns returnTelefonZadaciData()
        every { repository.getForenzickiDokazZadatak(id, any()) } returns returnForenzickiDokazZadatakData()
        every { repository.getGallery(id) } returns returnGalleryData()
        every { repository.getPitanja(id) } returns returnPitanjaData()
        every { repository.getOdgovor(id) } returns returnOdgovorData()
        every { repository.getPacijent(id, zlocin, zrtva, osobe) } returns pacijent
        every { repository.getMedicinskiIzvetaj(pacijent) } returns returnMedicinskiIzvestajData()
        every { repository.getLekarskiTest(pacijent) } returns returnLekarskiTest()
        every { repository.getLokacijeIstrage(id) } returns returnLokacijeIstrage()
        every { repository.getIzjavaZaPacijenta(pacijent, osobe) } returns returnIzjavaZaPacijenta()

        val result = service.getGeminiMysteriousSymtoms()

        assertNotNull(result)
    }
}