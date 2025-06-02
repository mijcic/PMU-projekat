package com.example.unit

import com.example.models.dto.*
import com.example.repository.RepoInterface
import com.example.repository.Repository
import com.example.service.get.GeminiMurderService
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/*
MockK ne može da mockuje klase koje nisu open, ili koje imaju neke specifične konstrukcije Kover "pokvari".
🔍 Šta se dešava:
Kada pokreneš test normalno → sve radi

Kada pokreneš sa Kover → Kover menja bytecode (dodaje "hookove" za praćenje linija koda)
MockK ne prepoznaje više Repository kao "čistu" klasu i baca MockKException
 */

class GeminiMurderServiceTest {

    private lateinit var repository: RepoInterface
    private lateinit var service: GeminiMurderService

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        service = GeminiMurderService(repository)
    }


    fun returnZlocinData():ZlocinData{
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

    fun returnTipZlocinaData():TipZlocinaDC{
        return TipZlocinaDC(1,"murder")
    }

    fun returnZrtvaData():ZrtvaData{
        val zlocin = returnZlocinData()

        val datumStr = "1997-11-11"
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

        return ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = osoba
        )
    }

    fun returnObdukcijaData():ObdukcijaData{
        val zrtva = returnZrtvaData()
        return ObdukcijaData(
            idObdukcija = 1,
            izvestaj = "Zrtva je preminula od rane od metka u grudima. Nema znakova borbe.",
            datum = System.currentTimeMillis(),
            uzrokSmrti = "Rana od metka u grudima",
            zrtvaId =zrtva.idZrtva,
            informacije = "Nema znakova seksualnog napada."
        )
    }

    fun returnTimeStamp():Long{
        val datumStr = "2024-10-10"
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr.let { LocalDate.parse(it.toString(), formatter2) }
        return dat.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    fun returnOsumnjiceniData(): List<OsumnjicenData>{
        val zlocin = returnZlocinData()
        val osoba2= OsobaData(
            idOsoba = 2,
            ime = "Tomas Black",
            kontakt = "+4433337888999",
            datum = returnTimeStamp(),
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = zlocin.idZlocin
        )

        val m = MotivData(
            idMotiv = 1,
            opis = "Ljubomora"
        )

        return listOf(OsumnjicenData(
            idOsumnjicen = 1,
            status = 0,
            tipOsumnjicen = "pojedinac",
            motiv = m,
            zlocinId = zlocin.idZlocin,
            kriv = 0,
            osobaId = osoba2
        ))
    }

    fun returnZadaciData(): List<ZadatakData>{
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

    fun returnOsobaData(): List<OsobaData>{
        return listOf(OsobaData(
            idOsoba = 2,
            ime = "Tomas Black",
            kontakt = "+4433337888999",
            datum = returnTimeStamp(),
            zanimanje = "advokat",
            pol = "muski",
            zlocinId = returnZlocinData().idZlocin
        ))
    }

    fun returnDokaziData():List<DokazData>{
        val zlocin =returnZlocinData()
        val osoba = returnOsobaData()
        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = returnOsobaData().first()
        )

        return listOf(DokazData(
            idDokaz = 1,
            tipDokaza = "fizicki",
            opis = "Pistolj pronadjen na mestu zlocina.",
            zlocinId = zlocin.idZlocin,
            zrtvaId = zr.idZrtva,
            status = 0
        ))
    }

    fun returnTelefonData():List<TelefonData>{
        val zlocin =returnZlocinData()
        val zr= ZrtvaData(
            idZrtva = 1,
            tipZrtve = "zena",
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu.",
            statusZrtva = "mrtav",
            zlocinId = zlocin.idZlocin,
            osobaId = returnOsobaData().first()
        )

        return listOf(TelefonData(
            idTelefon = 1,
            model = "Samsung Galaxy S22",
            os = "Android",
            sifra = "1234",
            informacije = "Pronađene su poruke sa pretnjama.",
            zrtvaId = zr.idZrtva
        ))
    }

    fun returnForenzickiDokaziData():List<ForenzickiDokazData>{
        return listOf(ForenzickiDokazData(
            idForenzickiDokaz = 1,
            tipForenzickiDokaz = "DNK",
            opis = "DNK tragovi pronađeni na pištolju.",
            statusS = 0,
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
        ))
    }

    fun returnSvedociData(): List<SvedokData>{
        return listOf(SvedokData(
            idSvedok = 1,
            izjava = "Cula sam pucanj i videla zenu kako bezi.",
            statusSvedok = "aktivno",
            statusIspitan = 0,
            zlocinId = returnZlocinData().idZlocin,
            osobaId = returnOsobaData().first()
        ))
    }

    fun returnOneContact(): List<OneContactData>{
        return listOf(OneContactData(
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

    fun returnKontaktiData(): List<KontaktData>{
        return listOf(KontaktData(
                idKontakt = 1,
                ime = "Elenora",
                broj = "+442123412312",
                status = 1,
                zrtvaId = returnZrtvaData()
        ))
    }

    fun returnPorukeData(): List<PorukeData>{
        return listOf(PorukeData(
            idPoruke = 1,
            tipPoruke = "SMS",
            sadrzaj = ":(",
            datumVreme = System.currentTimeMillis(),
            zrtvaId = returnZrtvaData(),
            posiljalacId = returnKontaktiData().first(),
            statusPoruke = "sent",
            sifrovana = false
        ))
    }

    fun returnPoziviData(): List<PoziviData>{
        return listOf(PoziviData(
            idPoziv = 1,
            tip = 0,
            broj = "+432635647547",
            datumVreme = System.currentTimeMillis(),
            zrtvaId = returnZrtvaData(),
            status = 0,
            kontaktId = returnKontaktiData().first()
        ))
    }

    fun returnGalerijaData(): List<GalerijaData>{
        return listOf(GalerijaData(
            idGalerija = 11,
            tip = 0,
            putanja = "/mojeSlike2",
            zrtvaId = returnZrtvaData(),
            datumVreme = returnTimeStamp(),
            lokacija = "hotel"
        ))
    }

    fun returnAplikacijeData(): List<AplikacijaData>{
        return listOf(AplikacijaData(
            idAplikacije = 1,
            naziv = "Instagram",
            tip = 0,
            zrtvaId = returnZrtvaData(),
            aktivna = true,
            informacije = "Poslednja aktivnost na Instagram profilu žrtve."
        ))
    }

    fun returnBeleskeData(): List<BeleskaData>{
        return listOf(BeleskaData(
            idBeleska = 1,
            zlocinId = returnZlocinData().idZlocin,
            tekst = "Moram da stignem pre njega",
            datum = returnTimeStamp()
        ))
    }

    fun returnWhatsAppKontaktiData(): List<WhatsAppKontaktData>{
        return listOf(WhatsAppKontaktData(
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

    fun returnWhatsAppPorukeData(): List<WhatsAppPorukaData>{
        return listOf(WhatsAppPorukaData(
            idWhatsAppPoruka = 1,
            kontaktKoSalje = returnWhatsAppKontaktiData().first().idWhatsAppKontakt,
            kontaktKomeSalje = returnWhatsAppKontaktiData()[1].idWhatsAppKontakt,
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.",
            datum = returnTimeStamp(),
            procitana = false
        ))
    }

    fun returnOneCallData(): List<OneCallData>{
        return listOf(OneCallData(
            idOneCall = 1,
            kontakt = returnOneContact().first().idOneContact,
            datum = returnTimeStamp(),
            propusten = false,
            dolazni = true
        ))
    }

    fun returnObicnaPorukaData(): List<ObicnaPorukaData>{
        return listOf(ObicnaPorukaData(
            idObicnaPoruka = 1,
            kontaktKoSalje = returnOneContact().first().idOneContact,
            kontaktKomeSalje = returnOneContact()[1].idOneContact,
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.",
            datum = returnTimeStamp(),
            procitana = false
        ))
    }

    @Test
    fun `should return null when getUsedZlocinMurder returns null`() {
        every { repository.getUsedZlocinMurder() } returns null
        //Kad god neko pozove getUsedZlocinMurder(), vrati null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getZlocin returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        //mockujemo sve prethodne zavisnosti koje vode do metode koju zelimo da testiramo
        every { repository.getZlocin(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getTipZlocina returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getZlocin(1) } returns returnZlocinData()
        every { repository.getTipZlocina(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getZrtva returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getZrtva(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getDokazi returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getDokazi(1,zrtva) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getTelefon returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getTelefon(zrtva.idZrtva) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getForenzickiDokazi returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getForenzickiDokazi(zrtva.idZrtva) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getObdukcija returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getObdukcija(zrtva.idZrtva) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getSvedoci returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getSvedoci(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getOneContact returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getOneContact(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getKontakti returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1,) } returns zrtva
        every { repository.getKontakti(1,zrtva) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getPoruke returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1,) } returns zrtva
        every { repository.getPoruke(1,zrtva, listOf()) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getPozivi returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1,) } returns zrtva
        every { repository.getPozivi(1,zrtva, listOf()) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getGalerija returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1,) } returns zrtva
        every { repository.getGalerija(1,zrtva) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getAplikacije returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1,) } returns zrtva
        every { repository.getAplikacije(1,zrtva) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getBeleske returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1,) } returns zrtva
        every { repository.getBeleske(1,zrtva) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getWhatsAppKontakt returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1,) } returns zrtva
        every { repository.getWhatsAppKontakt(1,zrtva) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getWhatsAppPoruka returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getWhatsAppPoruka(1, listOf()) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getOneCall returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getOneCall(1, listOf()) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getObicnaPoruka returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getObicnaPoruka(1, listOf()) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getOsumnjiceni returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getOsumnjiceni(1) } returns emptyList()

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getTragovi returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1,) } returns zrtva
        every { repository.getTragovi(repository.getForenzickiDokazi(zrtva.idZrtva),repository.getOsumnjiceni(1)) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getDokaziOsumnjiceni returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        every { repository.getZrtva(1,) } returns zrtva
        every { repository.getDokaziOsumnjiceni(repository.getDokazi(1,zrtva),repository.getOsumnjiceni(1)) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getZadaci returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getZadaci(1,) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getDokaziZadaci returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getDokaziZadaci(1, repository.getZadaci(1)) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getIspitivanjeOsumnjicenogZadatak returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getIspitivanjeOsumnjicenogZadatak(1, repository.getZadaci(1)) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getIspitivanjeSvedokaZadatak returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getIspitivanjeSvedokaZadatak(1, repository.getZadaci(1)) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getTelefonZadaci returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getTelefonZadaci(1, repository.getZadaci(1)) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getForenzickiDokazZadatak returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getForenzickiDokazZadatak(1, repository.getZadaci(1)) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getGallery returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getGallery(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getOdnosOsumnjicenZrtva returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getOdnosOsumnjicenZrtva(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getPitanja returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getPitanja(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getOdgovor returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getOdgovor(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getPitanjeIspitivanjeOsumnjicenog returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getPitanjeIspitivanjeOsumnjicenog(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getPitanjeIspitivanjeSvedoka returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getPitanjeIspitivanjeSvedoka(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return null when getOsobe returns null`() {
        every { repository.getUsedZlocinMurder() } returns 1
        every { repository.getOsobe(1) } returns null

        val result = service.getGeminiMurder()

        assertNull(result)
    }

    @Test
    fun `should return GeminiResponseRetrofit when all data is present`() {
        val id = 1
        val zlocin = returnZlocinData()
        val tipZlocina = returnTipZlocinaData()
        val zrtva= returnZrtvaData()

        every { repository.getUsedZlocinMurder() } returns id
        every { repository.getZlocin(id) } returns zlocin
        every { repository.getTipZlocina(zlocin.tipZlocinaId) } returns tipZlocina
        every { repository.getZrtva(id) } returns zrtva
        every { repository.getDokazi(id, zrtva) } returns returnDokaziData()
        every { repository.getZadaci(id) } returns returnZadaciData()
        every { repository.getOsobe(id) } returns returnOsobaData()
        every { repository.getTelefon(zrtva.idZrtva) } returns returnTelefonData()
        every { repository.getForenzickiDokazi(zrtva.idZrtva) } returns returnForenzickiDokaziData()
        every { repository.getObdukcija(zrtva.idZrtva) } returns returnObdukcijaData()
        every { repository.getSvedoci(id) } returns returnSvedociData()
        every { repository.getOneContact(id) } returns returnOneContact()
        every { repository.getKontakti(id, zrtva) } returns returnKontaktiData()
        every { repository.getPoruke(id, zrtva, any()) } returns returnPorukeData()
        every { repository.getPozivi(id, zrtva, any()) } returns returnPoziviData()
        every { repository.getGalerija(id, zrtva) } returns returnGalerijaData()
        every { repository.getAplikacije(id, zrtva) } returns returnAplikacijeData()
        every { repository.getBeleske(id, zrtva) } returns returnBeleskeData()
        every { repository.getWhatsAppKontakt(id, zrtva) } returns returnWhatsAppKontaktiData()
        every { repository.getWhatsAppPoruka(id, any()) } returns returnWhatsAppPorukeData()
        every { repository.getOneCall(id, any()) } returns returnOneCallData()
        every { repository.getOsumnjiceni(id) } returns returnOsumnjiceniData()
        every { repository.getObicnaPoruka(id, any()) } returns returnObicnaPorukaData()

        /*
        every { repository.getTragovi(any(), any()) } returns listOf()
        every { repository.getDokaziOsumnjiceni(any(), any()) } returns listOf()
        every { repository.getDokaziZadaci(id, any()) } returns listOf()
        every { repository.getIspitivanjeOsumnjicenogZadatak(id, any()) } returns listOf()
        every { repository.getIspitivanjeSvedokaZadatak(id, any()) } returns listOf()
        every { repository.getTelefonZadaci(id, any()) } returns listOf()
        every { repository.getForenzickiDokazZadatak(id, any()) } returns listOf()
        every { repository.getGallery(id) } returns listOf()
        every { repository.getOdnosOsumnjicenZrtva(id) } returns listOf()
        every { repository.getPitanja(id) } returns listOf()
        every { repository.getOdgovor(id) } returns listOf()
        every { repository.getPitanjeIspitivanjeOsumnjicenog(id) } returns listOf()
        every { repository.getPitanjeIspitivanjeSvedoka(id) } returns listOf()*/

        val result = service.getGeminiMurder()

        assertNotNull(result)
    }

    //Dupli pozivi

    @Test
    fun `should call getZrtva only once inside loadZrtvaDataGeminiRetrofit`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva= returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getDokazi(1, zrtva) } returns emptyList()
        every { repository.getKontakti(1, zrtva) } returns emptyList()
        every { repository.getTelefon(zrtva.idZrtva) } returns emptyList()
        every { repository.getForenzickiDokazi(zrtva.idZrtva) } returns emptyList()
        every { repository.getObdukcija(zrtva.idZrtva) } returns returnObdukcijaData()

        every { repository.getSvedoci(1) } returns returnSvedociData()
        every { repository.getOneContact(1) } returns returnOneContact()
        every { repository.getPoruke(1, zrtva, emptyList()) } returns returnPorukeData()
        every { repository.getPozivi(1, zrtva, emptyList()) } returns returnPoziviData()
        every { repository.getGalerija(1, zrtva) } returns returnGalerijaData()
        every { repository.getAplikacije(1, zrtva) } returns returnAplikacijeData()
        every { repository.getBeleske(1, zrtva) } returns returnBeleskeData()
        every { repository.getWhatsAppKontakt(1, zrtva) } returns returnWhatsAppKontaktiData()
        every { repository.getWhatsAppPoruka(1, emptyList()) } returns returnWhatsAppPorukeData()
        every { repository.getOneCall(1, emptyList()) } returns returnOneCallData()
        every { repository.getObicnaPoruka(1, emptyList()) } returns returnObicnaPorukaData()

        service.getGeminiMurder()

        verify(exactly = 1) { repository.getZrtva(1) }
    }

    @Test
    fun `should call getKontakti only once inside loadZrtvaDataGeminiRetrofit`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val validZrtva= returnZrtvaData()
        every { repository.getZrtva(1) } returns validZrtva
        val kontakti = listOf(KontaktData(1,"","+435365564",1,validZrtva))
        every { repository.getKontakti(1, validZrtva) } returns kontakti
        every { repository.getPoruke(1, validZrtva, kontakti) } returns returnPorukeData()
        every { repository.getPozivi(1, validZrtva, kontakti) } returns returnPoziviData()

        service.getGeminiMurder()

        verify(exactly = 1) { repository.getKontakti(1, validZrtva) }
    }

    @Test
    fun `should call getWhatsAppPoruka only once inside loadZrtvaDataGeminiRetrofit`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva= returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getWhatsAppKontakt(1, zrtva) } returns returnWhatsAppKontaktiData()
        every { repository.getWhatsAppPoruka(1, emptyList()) } returns returnWhatsAppPorukeData()

        service.getGeminiMurder()

        verify(exactly = 1) { repository.getWhatsAppKontakt(1,zrtva) }
    }

    @Test
    fun `should call getOneContact only once inside loadZrtvaDataGeminiRetrofit`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva= returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        every { repository.getOneCall(1, emptyList()) } returns returnOneCallData()
        every { repository.getObicnaPoruka(1, emptyList()) } returns returnObicnaPorukaData()

        service.getGeminiMurder()

        verify(exactly = 1) { repository.getOneContact(1) }
    }

    /*
    @Test
    fun `should call getOsumnjiceni only once inside loadOsumnjiceniDataGeminiRetrofit`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva= returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        val forenzika = returnForenzickiDokaziData()
        val dokazi = returnDokaziData()
        every { repository.getOsumnjiceni(1) } returns returnOsumnjiceniData()
        every { repository.getTragovi(forenzika, emptyList()) } returns emptyList()
        every { repository.getDokaziOsumnjiceni(dokazi, emptyList()) } returns emptyList()

        service.getGeminiMurder()

        verify(exactly = 1) { repository.getOsumnjiceni(1) }
    }
    */


    //ok odavde
    @Test
    fun `should call getZrtva only once inside loadOsumnjiceniDataGeminiRetrofit`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva= returnZrtvaData()
        every { repository.getZrtva(1) } returns zrtva
        val forenzika = emptyList<ForenzickiDokazData>()
        val dokazi = emptyList<DokazData>()
        val osumnjiceni = returnOsumnjiceniData()
        every { repository.getOsumnjiceni(1) } returns osumnjiceni
        every { repository.getTragovi(forenzika, osumnjiceni) } returns emptyList()
        every { repository.getDokaziOsumnjiceni(dokazi, osumnjiceni) } returns emptyList()

        service.getGeminiMurder()

        verify(exactly = 1) { repository.getZrtva(1) }
    }

    @Test
    fun `should call getDokazi only once inside loadOsumnjiceniDataGeminiRetrofit`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zrtva = returnZrtvaData()
        val dokazi = emptyList<DokazData>()
        val osumnjiceni = returnOsumnjiceniData()

        every { repository.getZrtva(1) } returns zrtva
        every { repository.getDokazi(1, zrtva) } returns dokazi
        every { repository.getOsumnjiceni(1) } returns osumnjiceni
        every { repository.getTragovi(any(), any()) } returns emptyList()
        every { repository.getDokaziOsumnjiceni(dokazi, osumnjiceni) } returns emptyList()

        service.getGeminiMurder()

        verify(exactly = 1) { repository.getDokazi(1, zrtva) }
    }
    /*

    @Test
    fun `should call getZadaci only once inside loadZadaciData`() {
        every { repository.getUsedZlocinMurder() } returns 1
        val zadaci = returnZadaciData()

        every { repository.getZadaci(1) } returns zadaci
        every { repository.getDokaziZadaci(1, zadaci) } returns emptyList()
        every { repository.getIspitivanjeOsumnjicenogZadatak(1, zadaci)} returns emptyList()
        every { repository.getIspitivanjeSvedokaZadatak(1, zadaci) } returns emptyList()
        every { repository.getTelefonZadaci(1, zadaci) } returns emptyList()
        every { repository.getForenzickiDokazZadatak(1, zadaci) } returns emptyList()

        service.getGeminiMurder()

        verify(exactly = 1) { repository.getZadaci(1) }
    }

     */
}
