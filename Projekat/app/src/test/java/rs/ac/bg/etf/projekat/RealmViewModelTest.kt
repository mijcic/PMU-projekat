package rs.ac.bg.etf.projekat

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.realm.kotlin.types.RealmInstant
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import rs.ac.bg.etf.projekat.data.realm.AlibiR
import rs.ac.bg.etf.projekat.data.realm.AplikacijaR
import rs.ac.bg.etf.projekat.data.realm.BeleskaR
import rs.ac.bg.etf.projekat.data.realm.DokazOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.GalleryR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeOsumnjicenogZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeSvedokaZadatakR
import rs.ac.bg.etf.projekat.data.realm.IzjavaZaPacijentaR
import rs.ac.bg.etf.projekat.data.realm.KontaktR
import rs.ac.bg.etf.projekat.data.realm.LekarskiTestR
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR
import rs.ac.bg.etf.projekat.data.realm.MedicinskiIzvestajR
import rs.ac.bg.etf.projekat.data.realm.MisijaPorukaR
import rs.ac.bg.etf.projekat.data.realm.MisijaR
import rs.ac.bg.etf.projekat.data.realm.MotivR
import rs.ac.bg.etf.projekat.data.realm.ObdukcijaR
import rs.ac.bg.etf.projekat.data.realm.ObicnaPorukaR
import rs.ac.bg.etf.projekat.data.realm.OdgovorR
import rs.ac.bg.etf.projekat.data.realm.OdnosOsumnjicenZrtvaR
import rs.ac.bg.etf.projekat.data.realm.OneCallR
import rs.ac.bg.etf.projekat.data.realm.OneContactR
import rs.ac.bg.etf.projekat.data.realm.OsobaR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.PacijentR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeOsumnjicenogR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR
import rs.ac.bg.etf.projekat.data.realm.PitanjeR
import rs.ac.bg.etf.projekat.data.realm.PorukeR
import rs.ac.bg.etf.projekat.data.realm.PorukeZadatakR
import rs.ac.bg.etf.projekat.data.realm.StatusAlibijaR
import rs.ac.bg.etf.projekat.data.realm.StatusPorukeR
import rs.ac.bg.etf.projekat.data.realm.StatusZrtvaR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonR
import rs.ac.bg.etf.projekat.data.realm.TelefonZadatakR
import rs.ac.bg.etf.projekat.data.realm.TipOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import rs.ac.bg.etf.projekat.data.realm.TragR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppKontaktR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppPorukaR
import rs.ac.bg.etf.projekat.data.realm.ZadatakR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR
import rs.ac.bg.etf.projekat.data.realm.ZrtvaR
import rs.ac.bg.etf.projekat.data.realm.stZlocinR
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RepositoryImplRealmViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class RealmViewModelTest {

    // Mockiramo SAMO RepositoryImplRealmViewModel, ne i Realm direktno u ViewModel testu.
    // ViewModel treba da komunicira samo sa Repository-jem.
    private val mockRepository: RepositoryImplRealmViewModel = mockk()
    private lateinit var viewModel: RealmViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        every { mockRepository.getRealm() } returns mockk()
        viewModel = RealmViewModel(mockRepository)
    }

    @Test
    fun `insertTipZlocina returns existing TipZlocinaR`() = runTest {
        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina

        val result = viewModel.inserTipZlocina("Kradja")

        assertNotNull(result)
        assertEquals("Kradja", result?.nazivTipaZlocina)
        println(tipZlocina.nazivTipaZlocina)
        println(tipZlocina.idTipZlocina)
        println(result?.nazivTipaZlocina)
        println(result?.idTipZlocina)
    }

    @Test
    fun `insertZlocin returns existing ZlocinaR`() = runTest {
        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        var datumM = RealmInstant.now()
        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum= datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1,tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika",stZlocinR.u_istrazi.name) } returns zlocin

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1,tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika",stZlocinR.u_istrazi.name)

        assertNotNull(result)
        assertNotNull(result2)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals("Ubistvo u starom hotelu", result2?.naziv)
        assertEquals(datumM, result2?.datum)
        assertEquals("Ubistvo u mladog preduzetnika", result2?.opis)
        assertEquals(stZlocinR.u_istrazi.name, result2?.status)
        assertEquals(datumM, result2?.datum)
    }

    @Test
    fun `insertOsoba returns existing OsobaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum= datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1,tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika",stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(1, result3?.idOsoba)
        assertEquals("Petar", result3?.ime)
        assertEquals("petar@example.com", result3?.kontakt)
        assertEquals(datumM, result3?.datum)
        assertEquals("inženjer", result3?.zanimanje)
        assertEquals("M", result3?.pol)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(datumM, result3?.datum)
    }

    @Test
    fun `insertZrtva returns existing ZrtvaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum= datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1,tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika",stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(1, result4?.idZrtva)
        assertEquals("zena", result4?.tipZrtve)
        assertEquals("Korumpirana advokatica pronadjena mrtva u vozu.", result4?.detalji)
        assertEquals(StatusZrtvaR.mrtva.name, result4?.statusZrtva)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(osoba, result4?.osobaId)
    }

    @Test
    fun `insertMotiv returns existing MotivR`() = runTest {
        val motiv = MotivR().apply {
            idMotiv = 1
            opis = "Ljubomora"
        }

        coEvery { mockRepository.insertMotiv("Ljubomora") } returns motiv

        val result = viewModel.insertMotiv("Ljubomora")

        assertNotNull(result)
        assertEquals(1, result?.idMotiv)
        assertEquals("Ljubomora", result?.opis)
    }

    @Test
    fun `insertOsumnjiceni returns existing OsumnjicenR`() = runTest {
        var datumM = RealmInstant.now()

        var motivO = MotivR().apply {
            idMotiv = 1
            opis = "Ljubomora"
        }

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum= datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val osumnjiceni = OsumnjicenR().apply {
            idOsumnjicen = 1
            status = 0
            tipOsumnjicen = "pojedinac"
            motiv = motivO
            zlocinId = zlocin
            kriv = 0
            osobaId = osoba
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M") } returns osumnjiceni
        coEvery { mockRepository.insertMotiv("Ljubomora") } returns motivO


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1,tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika",stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertMotiv("Ljubomora")
        val result5 = viewModel.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(motivO, result5?.motiv)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(1, result5?.idOsumnjicen)
        assertEquals("Petar", result5?.osobaId?.ime)
        assertEquals(0, result5?.status)
        assertEquals("pojedinac", result5?.tipOsumnjicen)
        assertEquals(0, result5?.kriv)
        assertEquals("petar@example.com", result5?.osobaId?.kontakt)
        assertEquals(datumM, result5?.osobaId?.datum)
        assertEquals("inženjer", result5?.osobaId?.zanimanje)
        assertEquals("M", result5?.osobaId?.pol)
    }

    @Test
    fun `insertDokaz returns existing DokazR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        var dokaz = DokazR().apply {
            idDokaz = 0
            tipDokaza = "fizicki"
            opis = "Pistolj pronadjen na mestu zlocina."
            zlocinId = zlocin
            zrtvaId = zrtva
            status = 0
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertDokaz(0, "fizicki", "Pistolj pronadjen na mestu zlocina.", zlocin, zrtva, 0) } returns dokaz


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertDokaz(0, "fizicki", "Pistolj pronadjen na mestu zlocina.", zlocin, zrtva, 0)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(0, result5?.idDokaz)
        assertEquals("fizicki", result5?.tipDokaza)
        assertEquals("Pistolj pronadjen na mestu zlocina.", result5?.opis)
        assertEquals(0, result5?.status)
    }

    @Test
    fun `insertDokazOsumnjicenog returns existing DokazOsumnjicenR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        var dokaz = DokazR().apply {
            idDokaz = 0
            tipDokaza = "fizicki"
            opis = "Pistolj pronadjen na mestu zlocina."
            zlocinId = zlocin
            zrtvaId = zrtva
            status = 0
        }

        var motivO = MotivR().apply {
            idMotiv = 1
            opis = "Ljubomora"
        }

        val osumnjiceni = OsumnjicenR().apply {
            idOsumnjicen = 1
            status = 0
            tipOsumnjicen = "pojedinac"
            motiv = motivO
            zlocinId = zlocin
            kriv = 0
            osobaId = osoba
        }

        val dokazOsumnjicen = DokazOsumnjicenR().apply {
            idDokazOsumnjicen = 0
            dokazId = dokaz
            osumnjicenId = osumnjiceni
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertDokaz(0, "fizicki", "Pistolj pronadjen na mestu zlocina.", zlocin, zrtva, 0) } returns dokaz
        coEvery { mockRepository.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M") } returns osumnjiceni
        coEvery { mockRepository.insertMotiv("Ljubomora") } returns motivO
        coEvery { mockRepository.insertDokazOsumnjicenog(0, dokaz, osumnjiceni) } returns dokazOsumnjicen

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertDokaz(0, "fizicki", "Pistolj pronadjen na mestu zlocina.", zlocin, zrtva, 0)
        val result6 = viewModel.insertMotiv("Ljubomora")
        val result7 = viewModel.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M")
        val result8 = viewModel.insertDokazOsumnjicenog(0, dokaz, osumnjiceni)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertNotNull(result8)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(motivO, result7?.motiv)
        assertEquals(zlocin, result7?.zlocinId)
        assertEquals(dokaz, result8?.dokazId)
        assertEquals(osumnjiceni, result8?.osumnjicenId)
        assertEquals(0, result8?.idDokazOsumnjicen)
    }

    @Test
    fun `insertSvedok returns existing SvedokR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val svedok = SvedokR().apply {
            idSvedok = 0
            izjava = "Cula sam pucanj i videla zenu kako bezi."
            statusSvedok = "aktivno"
            statusIspitan = 0
            zlocinId = zlocin
            osobaId = osoba
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertSvedok(0, "Petar", "petar@example.com", "Cula sam pucanj i videla zenu kako bezi.", zlocin, "aktivno", 0, datumM, "inženjer", "M") } returns svedok


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertSvedok(0, "Petar", "petar@example.com", "Cula sam pucanj i videla zenu kako bezi.", zlocin, "aktivno", 0, datumM, "inženjer", "M")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(0, result4?.idSvedok)
        assertEquals("Petar", result4?.osobaId?.ime)
        assertEquals("petar@example.com", result4?.osobaId?.kontakt)
        assertEquals("Cula sam pucanj i videla zenu kako bezi.", result4?.izjava)
        assertEquals("aktivno", result4?.statusSvedok)
        assertEquals(0, result4?.statusIspitan)
        assertEquals("inženjer", result4?.osobaId?.zanimanje)
        assertEquals("M", result4?.osobaId?.pol)
    }

    @Test
    fun `insertAlibi returns existing AlibiR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        var dokaz = DokazR().apply {
            idDokaz = 0
            tipDokaza = "fizicki"
            opis = "Pistolj pronadjen na mestu zlocina."
            zlocinId = zlocin
            zrtvaId = zrtva
            status = 0
        }

        var motivO = MotivR().apply {
            idMotiv = 1
            opis = "Ljubomora"
        }

        val osumnjiceni = OsumnjicenR().apply {
            idOsumnjicen = 1
            status = 0
            tipOsumnjicen = "pojedinac"
            motiv = motivO
            zlocinId = zlocin
            kriv = 0
            osobaId = osoba
        }

        val svedok = SvedokR().apply {
            idSvedok = 0
            izjava = "Cula sam pucanj i videla zenu kako bezi."
            statusSvedok = "aktivno"
            statusIspitan = 0
            zlocinId = zlocin
            osobaId = osoba
        }

        val alibi = AlibiR().apply {
            idAlibi = 0
            osumnjicenId = osumnjiceni
            svedokId = svedok
            opis = "U to vreme je bio u restoranu s prijateljima, što potvrđuje račun iz restorana i snimak sa sigurnosne kamere."
            statusAlibija = StatusAlibijaR.lažan.name
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertDokaz(0, "fizicki", "Pistolj pronadjen na mestu zlocina.", zlocin, zrtva, 0) } returns dokaz
        coEvery { mockRepository.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M") } returns osumnjiceni
        coEvery { mockRepository.insertMotiv("Ljubomora") } returns motivO
        coEvery { mockRepository.insertSvedok(0, "Petar", "petar@example.com", "Cula sam pucanj i videla zenu kako bezi.", zlocin, "aktivno", 0, datumM, "inženjer", "M") } returns svedok
        coEvery { mockRepository.insertAlibi(osumnjiceni, svedok,  "U to vreme je bio u restoranu s prijateljima, što potvrđuje račun iz restorana i snimak sa sigurnosne kamere.", StatusAlibijaR.lažan.name) } returns alibi

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertDokaz(0, "fizicki", "Pistolj pronadjen na mestu zlocina.", zlocin, zrtva, 0)
        val result6 = viewModel.insertMotiv("Ljubomora")
        val result7 = viewModel.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M")
        val result8 = viewModel.insertSvedok(0, "Petar", "petar@example.com", "Cula sam pucanj i videla zenu kako bezi.", zlocin, "aktivno", 0, datumM, "inženjer", "M")
        val result9 = viewModel.insertAlibi(osumnjiceni, svedok,  "U to vreme je bio u restoranu s prijateljima, što potvrđuje račun iz restorana i snimak sa sigurnosne kamere.", StatusAlibijaR.lažan.name)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertNotNull(result8)
        assertNotNull(result9)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(motivO, result7?.motiv)
        assertEquals(zlocin, result7?.zlocinId)
        assertEquals(zlocin, result8?.zlocinId)
        assertEquals(osumnjiceni, result9?.osumnjicenId)
        assertEquals(svedok, result9?.svedokId)
        assertEquals("U to vreme je bio u restoranu s prijateljima, što potvrđuje račun iz restorana i snimak sa sigurnosne kamere.", result9?.opis)
        assertEquals(StatusAlibijaR.lažan.name, result9?.statusAlibija)
    }

    @Test
    fun `insertMisija returns existing MisijaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val misija = MisijaR().apply {
            idMisija = 0
            zlocinId = zlocin
            naziv = "Misija_1"
            opis = "Pronađi ključne dokaze"
            status = 0
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertMisija(zlocin, "Misija_1", "Pronađi ključne dokaze", 0) } returns misija

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertMisija(zlocin, "Misija_1", "Pronađi ključne dokaze", 0)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals("Misija_1", result3?.naziv)
        assertEquals("Pronađi ključne dokaze", result3?.opis)
        assertEquals(0, result3?.status)
    }

    @Test
    fun `insertKontakt returns existing KontaktR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val kontakt = KontaktR().apply {
            idKontakt = 0
            ime = "Petar"
            broj = "+381111222333"
            status = 0
            zrtvaId = zrtva
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertKontakt(0, "Petar", "+381111222333", 0, zrtva) } returns kontakt

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertKontakt(0, "Petar", "+381111222333", 0, zrtva)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(0, result5?.idKontakt)
        assertEquals("Petar", result5?.ime)
        assertEquals("+381111222333", result5?.broj)
        assertEquals(0, result5?.status)
    }

    @Test
    fun `insertPoruka returns existing PorukeR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val kontakt = KontaktR().apply {
            idKontakt = 0
            ime = "Petar"
            broj = "+381111222333"
            status = 0
            zrtvaId = zrtva
        }

        val poruka = PorukeR().apply {
            idPoruke = 0
            tipPoruke = "SMS"
            sadrzaj = "Hej! Samo da javim da sam stigla kući."
            datumVreme = datumM
            zrtvaId = zrtva
            posiljalacId = kontakt
            statusPoruke = StatusPorukeR.read.name
            sifrovana = false
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertKontakt(0, "Petar", "+381111222333", 0, zrtva) } returns kontakt
        coEvery { mockRepository.insertPoruka("SMS", "Hej! Samo da javim da sam stigla kući.", datumM, zrtva, kontakt, StatusPorukeR.read.name, false) } returns poruka

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertKontakt(0, "Petar", "+381111222333", 0, zrtva)
        val result6 = viewModel.insertPoruka("SMS", "Hej! Samo da javim da sam stigla kući.", datumM, zrtva, kontakt, StatusPorukeR.read.name, false)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(zrtva, result6?.zrtvaId)
        assertEquals(kontakt, result6?.posiljalacId)
        assertEquals("SMS", result6?.tipPoruke)
        assertEquals("Hej! Samo da javim da sam stigla kući.", result6?.sadrzaj)
        assertEquals(StatusPorukeR.read.name, result6?.statusPoruke)
        assertEquals(false, result6?.sifrovana)
        assertEquals(datumM, result6?.datumVreme)
    }

    @Test
    fun `insertMisijaPoruka returns existing MisijaPorukaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val kontakt = KontaktR().apply {
            idKontakt = 0
            ime = "Petar"
            broj = "+381111222333"
            status = 0
            zrtvaId = zrtva
        }

        val porukaMP = PorukeR().apply {
            idPoruke = 0
            tipPoruke = "SMS"
            sadrzaj = "Hej! Samo da javim da sam stigla kući."
            datumVreme = datumM
            zrtvaId = zrtva
            posiljalacId = kontakt
            statusPoruke = StatusPorukeR.read.name
            sifrovana = false
        }

        val misijaPoruka = MisijaPorukaR().apply {
            idMisija = 0
            zlocinId = zlocin
            naziv = "Misija_poruka_1"
            poruka = porukaMP
            status = 0
            posiljalac = "Petar"
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertKontakt(0, "Petar", "+381111222333", 0, zrtva) } returns kontakt
        coEvery { mockRepository.insertPoruka("SMS", "Hej! Samo da javim da sam stigla kući.", datumM, zrtva, kontakt, StatusPorukeR.read.name, false) } returns porukaMP
        coEvery { mockRepository.insertMisijaPoruka(zlocin, "Misija_poruka_1", porukaMP, 0, "Petar") } returns misijaPoruka

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertKontakt(0, "Petar", "+381111222333", 0, zrtva)
        val result6 = viewModel.insertPoruka("SMS", "Hej! Samo da javim da sam stigla kući.", datumM, zrtva, kontakt, StatusPorukeR.read.name, false)
        val result7 = viewModel.insertMisijaPoruka(zlocin, "Misija_poruka_1", porukaMP, 0, "Petar")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(zrtva, result6?.zrtvaId)
        assertEquals(kontakt, result6?.posiljalacId)
        assertEquals(zlocin, result7?.zlocinId)
        assertEquals(porukaMP, result7?.poruka)
        assertEquals("Misija_poruka_1", result7?.naziv)
        assertEquals(0, result7?.status)
        assertEquals("Petar", result7?.posiljalac)
    }

    @Test
    fun `insertObdukcija returns existing ObdukcijaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val obdukcija = ObdukcijaR().apply {
            idObdukcija = 0
            izvestaj = "Zrtva je preminula od rane od metka u grudima. Nema znakova borbe."
            datum = datumM
            uzrokSmrti = "Rana od metka u grudima"
            zrtvaId = zrtva
            informacije = "Nema znakova seksualnog napada."
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertObdukcija(0, "Zrtva je preminula od rane od metka u grudima. Nema znakova borbe.", datumM.toString(), "Rana od metka u grudima", zrtva, "Nema znakova seksualnog napada.") } returns obdukcija


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertObdukcija(0, "Zrtva je preminula od rane od metka u grudima. Nema znakova borbe.", datumM.toString(), "Rana od metka u grudima", zrtva, "Nema znakova seksualnog napada.")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(0, result5?.idObdukcija)
        assertEquals("Zrtva je preminula od rane od metka u grudima. Nema znakova borbe.", result5?.izvestaj)
        assertEquals("Rana od metka u grudima", result5?.uzrokSmrti)
        assertEquals("Nema znakova seksualnog napada.", result5?.informacije)
        assertEquals(datumM, result5?.datum)
    }

    @Test
    fun `insertForenzickiDokaz returns existing ForenzickiDokazR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val forenzickiDokaz = ForenzickiDokazR().apply {
            idForenzickiDokaz = 0
            tipForenzickiDokaz = "DNK"
            opis = "DNK tragovi pronađeni na pištolju."
            status = 0
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
            zrtvaId = zrtva
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertForenzickiDokaz(0, "DNK", "DNK tragovi pronađeni na pištolju.", 0, zrtva, "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.") } returns forenzickiDokaz


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertForenzickiDokaz(0, "DNK", "DNK tragovi pronađeni na pištolju.", 0, zrtva, "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(0, result5?.idForenzickiDokaz)
        assertEquals("DNK", result5?.tipForenzickiDokaz)
        assertEquals("DNK tragovi pronađeni na pištolju.", result5?.opis)
        assertEquals(0, result5?.status)
        assertEquals("DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.", result5?.veza)
    }

    @Test
    fun `insertTelefon returns existing TelefonR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val telefon = TelefonR().apply {
            idTelefon = 0
            model = "Samsung Galaxy S22"
            os = "Android"
            sifra = "1234"
            zrtvaId = zrtva
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertTelefon(0, "Samsung Galaxy S22", "Android", zrtva, "1234") } returns telefon


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertTelefon(0, "Samsung Galaxy S22", "Android", zrtva, "1234")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(0, result5?.idTelefon)
        assertEquals("Samsung Galaxy S22", result5?.model)
        assertEquals("Android", result5?.os)
        assertEquals("1234", result5?.sifra)
    }

    @Test
    fun `insertOdnosOsumnjicenZrtva returns existing OdnosOsumnjicenZrtvaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        var motivO = MotivR().apply {
            idMotiv = 1
            opis = "Ljubomora"
        }

        val osumnjiceni = OsumnjicenR().apply {
            idOsumnjicen = 1
            status = 0
            tipOsumnjicen = "pojedinac"
            motiv = motivO
            zlocinId = zlocin
            kriv = 0
            osobaId = osoba
        }

        val odnos = OdnosOsumnjicenZrtvaR().apply {
            idOdnos = 0
            osumnjicenId = osumnjiceni
            zrtvaId = zrtva
            tipOdnosa = "rivalski"
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M") } returns osumnjiceni
        coEvery { mockRepository.insertMotiv("Ljubomora") } returns motivO
        coEvery { mockRepository.insertOdnosOsumnjicenZrtva(0, osumnjiceni, zrtva, "rivalski") } returns odnos

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertMotiv("Ljubomora")
        val result6 = viewModel.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M")
        val result7 = viewModel.insertOdnosOsumnjicenZrtva(0, osumnjiceni, zrtva, "rivalski")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result6?.zlocinId)
        assertEquals(osumnjiceni, result7?.osumnjicenId)
        assertEquals(zrtva, result7?.zrtvaId)
        assertEquals(0, result7?.idOdnos)
        assertEquals("rivalski", result7?.tipOdnosa)
    }

    @Test
    fun `insertPrijavljeniKorisnik returns existing PrijavljeniKorisnikR`() = runTest {
        val prijavljeniKorisnik = PrijavljeniKorisnikR().apply {
            idKorisnik = 0
            korisnickoIme = "korisnickoIme"
            sifra = "sifra"
        }

        coEvery { mockRepository.insertPrijavljeniKorisnik("korisnickoIme", "sifra") } returns prijavljeniKorisnik

        val result = viewModel.insertPrijavljeniKorisnik("korisnickoIme", "sifra")

        assertNotNull(result)
        assertEquals(0, result?.idKorisnik)
        assertEquals("korisnickoIme", result?.korisnickoIme)
        assertEquals("sifra", result?.sifra)
    }

    @Test
    fun `insertPitanjeIspitivanjeOsumnjicenog returns existing PitanjeIspitivanjeOsumnjicenogR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        var motivO = MotivR().apply {
            idMotiv = 1
            opis = "Ljubomora"
        }

        val osumnjiceni = OsumnjicenR().apply {
            idOsumnjicen = 1
            status = 0
            tipOsumnjicen = "pojedinac"
            motiv = motivO
            zlocinId = zlocin
            kriv = 0
            osobaId = osoba
        }

        val pitanje = PitanjeIspitivanjeOsumnjicenogR().apply {
            idPitanjeIspitivanjeOsumnjicenog = 0
            kategorija = "alibi"
            tekst = "Zasto ste bili u sobi zrtve?"
            odgovor = "Samo sam mu doneo kofer. Otisao sam odmah."
            komentar = "Nije pomenuo sadrzaj kofera ni zasto bas on donosi. Moguce da prikriva pravi razlog dolaska."
            osumnjicenId = osumnjiceni
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M") } returns osumnjiceni
        coEvery { mockRepository.insertMotiv("Ljubomora") } returns motivO
        coEvery { mockRepository.insertPitanjeIspitivanjeOsumnjicenog(0, osumnjiceni.idOsumnjicen, "alibi", "Zasto ste bili u sobi zrtve?", "Samo sam mu doneo kofer. Otisao sam odmah.", "Nije pomenuo sadrzaj kofera ni zasto bas on donosi. Moguce da prikriva pravi razlog dolaska.") } returns pitanje

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertMotiv("Ljubomora")
        val result6 = viewModel.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M")
        val result7 = viewModel.insertPitanjeIspitivanjeOsumnjicenog(0, osumnjiceni.idOsumnjicen, "alibi", "Zasto ste bili u sobi zrtve?", "Samo sam mu doneo kofer. Otisao sam odmah.", "Nije pomenuo sadrzaj kofera ni zasto bas on donosi. Moguce da prikriva pravi razlog dolaska.")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result6?.zlocinId)
        assertEquals(osumnjiceni, result7?.osumnjicenId)
        assertEquals(0, result7?.idPitanjeIspitivanjeOsumnjicenog)
        assertEquals("alibi", result7?.kategorija)
        assertEquals("Zasto ste bili u sobi zrtve?", result7?.tekst)
        assertEquals("Samo sam mu doneo kofer. Otisao sam odmah.", result7?.odgovor)
        assertEquals("Nije pomenuo sadrzaj kofera ni zasto bas on donosi. Moguce da prikriva pravi razlog dolaska.", result7?.komentar)
    }

    @Test
    fun `insertPitanjeIspitivanjeSvedoka returns existing PitanjeIspitivanjeSvedokaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        var motivO = MotivR().apply {
            idMotiv = 1
            opis = "Ljubomora"
        }

        val svedok = SvedokR().apply {
            idSvedok = 0
            izjava = "Cula sam pucanj i videla zenu kako bezi."
            statusSvedok = "aktivno"
            statusIspitan = 0
            zlocinId = zlocin
            osobaId = osoba
        }

        val pitanje = PitanjeIspitivanjeSvedokaR().apply {
            idPitanjeIspitivanjeSvedoka = 0
            tekst = "Jeste li sigurni da je to bio bas taj muskarac?"
            odgovor = "Da, prepoznao sam ga – imao je crvenu jaknu i hodao je sepajući."
            svedokId = svedok
            next = 0
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertSvedok(0, "Petar", "petar@example.com", "Cula sam pucanj i videla zenu kako bezi.", zlocin, "aktivno", 0, datumM, "inženjer", "M") } returns svedok
        coEvery { mockRepository.insertMotiv("Ljubomora") } returns motivO
        coEvery { mockRepository.insertPitanjeIspitivanjeSvedoka(0, svedok, "Jeste li sigurni da je to bio bas taj muskarac?", "Da, prepoznao sam ga – imao je crvenu jaknu i hodao je sepajući.") } returns pitanje

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertMotiv("Ljubomora")
        val result6 = viewModel.insertSvedok(0, "Petar", "petar@example.com", "Cula sam pucanj i videla zenu kako bezi.", zlocin, "aktivno", 0, datumM, "inženjer", "M")
        val result7 = viewModel.insertPitanjeIspitivanjeSvedoka( 0, svedok, "Jeste li sigurni da je to bio bas taj muskarac?", "Da, prepoznao sam ga – imao je crvenu jaknu i hodao je sepajući.")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result6?.zlocinId)
        assertEquals(svedok, result7?.svedokId)
        assertEquals(0, result7?.idPitanjeIspitivanjeSvedoka)
        assertEquals("Jeste li sigurni da je to bio bas taj muskarac?", result7?.tekst)
        assertEquals("Da, prepoznao sam ga – imao je crvenu jaknu i hodao je sepajući.", result7?.odgovor)
    }

    @Test
    fun `insertZadatak returns existing ZadatakR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val zadatak = ZadatakR().apply {
            idZadatak = 0
            tekst = "Pronadji karticu gosta"
            korak = "korak_1"
            uradjen = false
            next = null
            zlocinId = zlocin
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin) } returns zadatak

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(0, result3?.idZadatak)
        assertEquals("Pronadji karticu gosta", result3?.tekst)
        assertEquals("korak_1", result3?.korak)
        assertEquals(false, result3?.uradjen)
        assertEquals(null, result3?.next)
    }

    @Test
    fun `updateZadatak sets next task correctly`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val zadatak1 = ZadatakR().apply {
            idZadatak = 1
            tekst = "Ispitaj svedoka"
            korak = "korak_1"
            uradjen = false
            next = null
            zlocinId = zlocin
        }

        val zadatak2 = ZadatakR().apply {
            idZadatak = 2
            tekst = "Nadji otiske"
            korak = "korak_2"
            uradjen = false
            next = null
            zlocinId = zlocin
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertZadatak(1, "Ispitaj svedoka", "korak_1", false, null, zlocin) } returns zadatak1
        coEvery { mockRepository.insertZadatak(2, "Nadji otiske", "korak_2", false, null, zlocin) } returns zadatak2
        coEvery { mockRepository.updateZadatak(1, 2) } answers { zadatak1.next = zadatak2 }

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val inserted1 = viewModel.insertZadatak(1, "Ispitaj svedoka", "korak_1", false, null, zlocin)
        val inserted2 = viewModel.insertZadatak(2, "Nadji otiske", "korak_2", false, null, zlocin)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(inserted1)
        assertNotNull(inserted2)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, inserted1?.zlocinId)
        assertEquals(zlocin, inserted2?.zlocinId)
        assertEquals(1, inserted1?.idZadatak)
        assertEquals("Ispitaj svedoka", inserted1?.tekst)
        assertEquals("korak_1", inserted1?.korak)
        assertEquals(false, inserted1?.uradjen)
        assertEquals(null, inserted1?.next)
        assertEquals(2, inserted2?.idZadatak)
        assertEquals("Nadji otiske", inserted2?.tekst)
        assertEquals("korak_2", inserted2?.korak)
        assertEquals(false, inserted2?.uradjen)
        assertEquals(null, inserted2?.next)

        viewModel.updateZadatak(1, 2)

        assertEquals(zadatak2, zadatak1.next)
        assertEquals(null, zadatak2.next)
    }

    @Test
    fun `insertDokazZadatak returns existing DokazZadatakR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        var dokaz = DokazR().apply {
            idDokaz = 0
            tipDokaza = "fizicki"
            opis = "Pistolj pronadjen na mestu zlocina."
            zlocinId = zlocin
            zrtvaId = zrtva
            status = 0
        }

        val zadatak = ZadatakR().apply {
            idZadatak = 0
            tekst = "Pronadji karticu gosta"
            korak = "korak_1"
            uradjen = false
            next = null
            zlocinId = zlocin
        }

        val dokazZadatak = DokazZadatakR().apply {
            idDokazZadatak = 0
            tekst = "Posalji dokaz na forenzicku analizu"
            dokazId = dokaz
            uradjen = false
            zadatakId = zadatak
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertDokaz(0, "fizicki", "Pistolj pronadjen na mestu zlocina.", zlocin, zrtva, 0) } returns dokaz
        coEvery { mockRepository.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin) } returns zadatak
        coEvery { mockRepository.insertDokazZadatak(0, "Posalji dokaz na forenzicku analizu", dokaz, false, zadatak) } returns dokazZadatak

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertDokaz(0, "fizicki", "Pistolj pronadjen na mestu zlocina.", zlocin, zrtva, 0)
        val result6 = viewModel.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin)
        val result7 = viewModel.insertDokazZadatak(0, "Posalji dokaz na forenzicku analizu", dokaz, false, zadatak)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(zlocin, result6?.zlocinId)
        assertEquals(dokaz, result7?.dokazId)
        assertEquals(zadatak, result7?.zadatakId)
        assertEquals(0, result7?.idDokazZadatak)
        assertEquals("Posalji dokaz na forenzicku analizu", result7?.tekst)
        assertEquals(false, result7?.uradjen)
    }

    @Test
    fun `insertForenzickiDokazZadatak returns existing ForenzickiDokazZadatakR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val forenzickiDokaz = ForenzickiDokazR().apply {
            idForenzickiDokaz = 0
            tipForenzickiDokaz = "DNK"
            opis = "DNK tragovi pronađeni na pištolju."
            status = 0
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
            zrtvaId = zrtva
        }

        val zadatak = ZadatakR().apply {
            idZadatak = 0
            tekst = "Pronadji karticu gosta"
            korak = "korak_1"
            uradjen = false
            next = null
            zlocinId = zlocin
        }

        val forenzickiDokazZadatak = ForenzickiDokazZadatakR().apply {
            idForenzickiDokazZadatak = 0
            tekst = "Otkrij kojoj zeni pripada DNK."
            forenzickiDokazId = forenzickiDokaz
            uradjen = true
            zadatakId = zadatak
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertForenzickiDokaz(0, "DNK", "DNK tragovi pronađeni na pištolju.", 0, zrtva, "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.") } returns forenzickiDokaz
        coEvery { mockRepository.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin) } returns zadatak
        coEvery { mockRepository.insertForenzickiDokazZadatak(0, "Otkrij kojoj zeni pripada DNK.", forenzickiDokaz, true, zadatak) } returns forenzickiDokazZadatak

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertForenzickiDokaz(0, "DNK", "DNK tragovi pronađeni na pištolju.", 0, zrtva, "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.")
        val result6 = viewModel.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin)
        val result7 = viewModel.insertForenzickiDokazZadatak(0, "Otkrij kojoj zeni pripada DNK.", forenzickiDokaz, true, zadatak)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(zlocin, result6?.zlocinId)
        assertEquals(forenzickiDokaz, result7?.forenzickiDokazId)
        assertEquals(zadatak, result7?.zadatakId)
        assertEquals(0, result7?.idForenzickiDokazZadatak)
        assertEquals("Otkrij kojoj zeni pripada DNK.", result7?.tekst)
        assertEquals(true, result7?.uradjen)
    }

    @Test
    fun `insertIspitivanjeOsumnjicenogZadatak returns existing IspitivanjeOsumnjicenogZadatakR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        var motivO = MotivR().apply {
            idMotiv = 1
            opis = "Ljubomora"
        }

        val osumnjiceni = OsumnjicenR().apply {
            idOsumnjicen = 1
            status = 0
            tipOsumnjicen = "pojedinac"
            motiv = motivO
            zlocinId = zlocin
            kriv = 0
            osobaId = osoba
        }

        val zadatak = ZadatakR().apply {
            idZadatak = 0
            tekst = "Pronadji karticu gosta"
            korak = "korak_1"
            uradjen = false
            next = null
            zlocinId = zlocin
        }

        val ispitivanjeZadatak = IspitivanjeOsumnjicenogZadatakR().apply {
            idIspitivanjeOsumnjicenogZadatak = 0
            osumnjicenId = osumnjiceni
            zadatakId = zadatak
            uradjen = true
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M") } returns osumnjiceni
        coEvery { mockRepository.insertMotiv("Ljubomora") } returns motivO
        coEvery { mockRepository.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin) } returns zadatak
        coEvery { mockRepository.insertIspitivanjeOsumnjicenogZadatak(0, osumnjiceni, zadatak, true) } returns ispitivanjeZadatak

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertMotiv("Ljubomora")
        val result6 = viewModel.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M")
        val result7 = viewModel.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin)
        val result8 = viewModel.insertIspitivanjeOsumnjicenogZadatak(0, osumnjiceni, zadatak, true)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertNotNull(result8)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(motivO, result6?.motiv)
        assertEquals(zlocin, result6?.zlocinId)
        assertEquals(zlocin, result7?.zlocinId)
        assertEquals(osumnjiceni, result8?.osumnjicenId)
        assertEquals(zadatak, result8?.zadatakId)
        assertEquals(0, result8?.idIspitivanjeOsumnjicenogZadatak)
        assertEquals(true, result8?.uradjen)
    }

    @Test
    fun `insertIspitivanjeSvedokaZadatak returns existing IspitivanjeSvedokaZadatakR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val svedok = SvedokR().apply {
            idSvedok = 0
            izjava = "Cula sam pucanj i videla zenu kako bezi."
            statusSvedok = "aktivno"
            statusIspitan = 0
            zlocinId = zlocin
            osobaId = osoba
        }

        val zadatak = ZadatakR().apply {
            idZadatak = 0
            tekst = "Pronadji karticu gosta"
            korak = "korak_1"
            uradjen = false
            next = null
            zlocinId = zlocin
        }

        val ispitivanjeZadatak = IspitivanjeSvedokaZadatakR().apply {
            idIspitivanjeSvedokaZadatak = 0
            svedokId = svedok
            zadatakId = zadatak
            uradjen = false
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertSvedok(0, "Petar", "petar@example.com", "Cula sam pucanj i videla zenu kako bezi.", zlocin, "aktivno", 0, datumM, "inženjer", "M") } returns svedok
        coEvery { mockRepository.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin) } returns zadatak
        coEvery { mockRepository.insertIspitivanjeSvedokaZadatak(0, svedok, zadatak, false) } returns ispitivanjeZadatak

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertSvedok(0, "Petar", "petar@example.com", "Cula sam pucanj i videla zenu kako bezi.", zlocin, "aktivno", 0, datumM, "inženjer", "M")
        val result6 = viewModel.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin)
        val result7 = viewModel.insertIspitivanjeSvedokaZadatak(0, svedok, zadatak, false)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(zlocin, result6?.zlocinId)
        assertEquals(svedok, result7?.svedokId)
        assertEquals(zadatak, result7?.zadatakId)
        assertEquals(0, result7?.idIspitivanjeSvedokaZadatak)
        assertEquals(false, result7?.uradjen)
    }

    @Test
    fun `insertTelefonZadatak returns existing TelefonZadatakR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val telefon = TelefonR().apply {
            idTelefon = 0
            model = "Samsung Galaxy S22"
            os = "Android"
            sifra = "1234"
            zrtvaId = zrtva
        }

        val zadatak = ZadatakR().apply {
            idZadatak = 0
            tekst = "Pronadji karticu gosta"
            korak = "korak_1"
            uradjen = false
            next = null
            zlocinId = zlocin
        }

        val telefonZadatak = TelefonZadatakR().apply {
            idTelefonZadatak = 0
            telefonId = telefon
            zadatakId = zadatak
            uradjen = true
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertTelefon(0, "Samsung Galaxy S22", "Android", zrtva, "1234") } returns telefon
        coEvery { mockRepository.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin) } returns zadatak
        coEvery { mockRepository.insertTelefonZadatak(0, telefon, zadatak, true) } returns telefonZadatak

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertTelefon(0, "Samsung Galaxy S22", "Android", zrtva, "1234")
        val result6 = viewModel.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin)
        val result7 = viewModel.insertTelefonZadatak(0, telefon, zadatak, true)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(zlocin, result6?.zlocinId)
        assertEquals(telefon, result7?.telefonId)
        assertEquals(zadatak, result7?.zadatakId)
        assertEquals(0, result7?.idTelefonZadatak)
        assertEquals(true, result7?.uradjen)
    }

    @Test
    fun `insertPorukeZadatak returns existing PorukeZadatakR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val kontakt = KontaktR().apply {
            idKontakt = 0
            ime = "Petar"
            broj = "+381111222333"
            status = 0
            zrtvaId = zrtva
        }

        val poruka = PorukeR().apply {
            idPoruke = 0
            tipPoruke = "SMS"
            sadrzaj = "Hej! Samo da javim da sam stigla kući."
            datumVreme = datumM
            zrtvaId = zrtva
            posiljalacId = kontakt
            statusPoruke = StatusPorukeR.read.name
            sifrovana = false
        }

        val zadatak = ZadatakR().apply {
            idZadatak = 0
            tekst = "Pronadji karticu gosta"
            korak = "korak_1"
            uradjen = false
            next = null
            zlocinId = zlocin
        }

        val porukeZadatak = PorukeZadatakR().apply {
            idPorukeZadatak = 0
            porukeId = poruka
            zadatakId = zadatak
            uradjen = false
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertKontakt(0, "Petar", "+381111222333", 0, zrtva) } returns kontakt
        coEvery { mockRepository.insertPoruka("SMS", "Hej! Samo da javim da sam stigla kući.", datumM, zrtva, kontakt, StatusPorukeR.read.name, false) } returns poruka
        coEvery { mockRepository.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin) } returns zadatak
        coEvery { mockRepository.insertPorukeZadatak(poruka, zadatak, false) } returns porukeZadatak

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertKontakt(0, "Petar", "+381111222333", 0, zrtva)
        val result6 = viewModel.insertPoruka("SMS", "Hej! Samo da javim da sam stigla kući.", datumM, zrtva, kontakt, StatusPorukeR.read.name, false)
        val result7 = viewModel.insertZadatak(0, "Pronadji karticu gosta", "korak_1", false, null, zlocin)
        val result8 = viewModel.insertPorukeZadatak(poruka, zadatak, false)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertNotNull(result8)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(zrtva, result6?.zrtvaId)
        assertEquals(kontakt, result6?.posiljalacId)
        assertEquals(zlocin, result7?.zlocinId)
        assertEquals(poruka, result8?.porukeId)
        assertEquals(zadatak, result8?.zadatakId)
        assertEquals(false, result8?.uradjen)
    }

    @Test
    fun `insertPacijent returns existing PacijentR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val pacijent = PacijentR().apply {
            idPacijent = 0
            simptomi = "Temperatura"
            statusPacijenta = "ziva"
            datumPrijave = datumM
            prijavio = "Petar"
            zlocinId = zlocin
            zrtvaId = zrtva
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertPacijent(0, "Temperatura", "ziva", datumM, "Petar", zlocin, zrtva) } returns pacijent

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertPacijent(0, "Temperatura", "ziva", datumM, "Petar", zlocin, zrtva)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(0, result5?.idPacijent)
        assertEquals("Temperatura", result5?.simptomi)
        assertEquals("ziva", result5?.statusPacijenta)
        assertEquals("Petar", result5?.prijavio)
        assertEquals(datumM, result5?.datumPrijave)
    }

    @Test
    fun `insertIzjavaZaPacijenta returns existing IzjavaZaPacijentaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val pacijent = PacijentR().apply {
            idPacijent = 0
            simptomi = "Temperatura"
            statusPacijenta = "ziva"
            datumPrijave = datumM
            prijavio = "Petar"
            zlocinId = zlocin
            zrtvaId = zrtva
        }

        val izjava = IzjavaZaPacijentaR().apply {
            idIzjavaZaPacijenta = 0
            izjava = "Pacijent je u losem stanju"
            pacijentId = pacijent
            osobaId = osoba
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertPacijent(0, "Temperatura", "ziva", datumM, "Petar", zlocin, zrtva) } returns pacijent
        coEvery { mockRepository.insertIzjavaZaPacijenta(0, "Pacijent je u losem stanju", pacijent, osoba) } returns izjava


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertPacijent(0, "Temperatura", "ziva", datumM, "Petar", zlocin, zrtva)
        val result6 = viewModel.insertIzjavaZaPacijenta(0, "Pacijent je u losem stanju", pacijent, osoba)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(pacijent, result6?.pacijentId)
        assertEquals(osoba, result6?.osobaId)
        assertEquals(0, result6?.idIzjavaZaPacijenta)
        assertEquals("Pacijent je u losem stanju", result6?.izjava)
    }

    @Test
    fun `insertLekarskiTest returns existing LekarskiTestR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val pacijent = PacijentR().apply {
            idPacijent = 0
            simptomi = "Temperatura"
            statusPacijenta = "ziva"
            datumPrijave = datumM
            prijavio = "Petar"
            zlocinId = zlocin
            zrtvaId = zrtva
        }

        val lekarskiTest = LekarskiTestR().apply {
            idLekarskiTest = 0
            pacijentId = pacijent
            izvestaj = "Lekarski test okej"
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertPacijent(0, "Temperatura", "ziva", datumM, "Petar", zlocin, zrtva) } returns pacijent
        coEvery { mockRepository.insertLekarskiTest(0, pacijent, "Lekarski test okej") } returns lekarskiTest


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertPacijent(0, "Temperatura", "ziva", datumM, "Petar", zlocin, zrtva)
        val result6 = viewModel.insertLekarskiTest(0, pacijent, "Lekarski test okej")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(pacijent, result6?.pacijentId)
        assertEquals(0, result6?.idLekarskiTest)
        assertEquals("Lekarski test okej", result6?.izvestaj)
    }

    @Test
    fun `insertLokacijeIstrage returns existing LokacijeIstrageR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val lokacijeIstrage = LokacijeIstrageR().apply {
            idLokacijeIstrage = 0
            mesto = "London"
            naziv = "Bolnica Sveti Luka"
            opis = "Bolnica"
            zlocinId = zlocin
            geoTackaALatitude = 2.3
            geoTackaALongitude = 4.3
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertLokacijeIstrage(0, "London", "Bolnica Sveti Luka", "Bolnica", zlocin, 2.3, 4.3) } returns lokacijeIstrage

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertLokacijeIstrage(0, "London", "Bolnica Sveti Luka", "Bolnica", zlocin, 2.3, 4.3)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(0, result3?.idLokacijeIstrage)
        assertEquals("London", result3?.mesto)
        assertEquals("Bolnica Sveti Luka", result3?.naziv)
        assertEquals("Bolnica", result3?.opis)
        assertEquals(2.3, result3?.geoTackaALatitude)
        assertEquals(4.3, result3?.geoTackaALongitude)
    }

    @Test
    fun `insertMedicinskiIzvestaj returns existing MedicinskiIzvestajR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val pacijent = PacijentR().apply {
            idPacijent = 0
            simptomi = "Temperatura"
            statusPacijenta = "ziva"
            datumPrijave = datumM
            prijavio = "Petar"
            zlocinId = zlocin
            zrtvaId = zrtva
        }

        val medicinskiIzvestaj = MedicinskiIzvestajR().apply {
            idMedicinskiIzvestaj = 0
            rezime = "u redu"
            CTnalaz = "uredan CTnalaz"
            MRInalaz = "uredan MRInalaz"
            krvnaSlika = "dobra"
            toksikoloskeAnalize = "nema"
            zakljucak = "u redu"
            pacijentId = pacijent
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertPacijent(0, "Temperatura", "ziva", datumM, "Petar", zlocin, zrtva) } returns pacijent
        coEvery { mockRepository.insertMedicinskiIzvestaj(0, "u redu", "uredan CTnalaz", "uredan MRInalaz", "dobra", "nema", "u redu", pacijent) } returns medicinskiIzvestaj


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertPacijent(0, "Temperatura", "ziva", datumM, "Petar", zlocin, zrtva)
        val result6 = viewModel.insertMedicinskiIzvestaj(0, "u redu", "uredan CTnalaz", "uredan MRInalaz", "dobra", "nema", "u redu", pacijent)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(pacijent, result6?.pacijentId)
        assertEquals(0, result6?.idMedicinskiIzvestaj)
        assertEquals("u redu", result6?.rezime)
        assertEquals("uredan CTnalaz", result6?.CTnalaz)
        assertEquals("uredan MRInalaz", result6?.MRInalaz)
        assertEquals("dobra", result6?.krvnaSlika)
        assertEquals("nema", result6?.toksikoloskeAnalize)
        assertEquals("u redu", result6?.zakljucak)
    }

    @Test
    fun `insertPitanje returns existing PitanjeR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val pitanje = PitanjeR().apply {
            idPitanje = 0
            zlocinId = zlocin
            tekst = "Da li si otisao do njene sobe?"
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertPitanje(0, zlocin, "Da li si otisao do njene sobe?") } returns pitanje

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertPitanje(0, zlocin, "Da li si otisao do njene sobe?")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(0, result3?.idPitanje)
        assertEquals("Da li si otisao do njene sobe?", result3?.tekst)
    }

    @Test
    fun `insertOdogovor returns existing OdgovorR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val pitanje = PitanjeR().apply {
            idPitanje = 0
            zlocinId = zlocin
            tekst = "Da li si otisao do njene sobe?"
        }

        val odgovor = OdgovorR().apply {
            idOdogovor = 0
            pitanjeId = pitanje
            tekstOdgovora = "Jesam, kaze da nije umesana."
            tacan = false
            bodovi = 10
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertPitanje(0, zlocin, "Da li si otisao do njene sobe?") } returns pitanje
        coEvery { mockRepository.insertOdogovor(0, pitanje, "Jesam, kaze da nije umesana.", false, 10) } returns odgovor

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertPitanje(0, zlocin, "Da li si otisao do njene sobe?")
        val result4 = viewModel.insertOdogovor(0, pitanje, "Jesam, kaze da nije umesana.", false, 10)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(pitanje, result4?.pitanjeId)
        assertEquals(0, result4?.idOdogovor)
        assertEquals("Jesam, kaze da nije umesana.", result4?.tekstOdgovora)
        assertEquals(false, result4?.tacan)
        assertEquals(10, result4?.bodovi)
    }

    @Test
    fun `getAllOdgovorForPitanje returns mocked answers list`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val pitanje = PitanjeR().apply {
            idPitanje = 0
            zlocinId = zlocin
            tekst = "Da li si otisao do njene sobe?"
        }

        val odgovori = listOf(
            OdgovorR().apply {
                idOdogovor = 1
                pitanjeId = pitanje
                tekstOdgovora = "Ne"
                tacan = true
                bodovi = 5
            },
            OdgovorR().apply {
                idOdogovor = 2
                pitanjeId = pitanje
                tekstOdgovora = "Da, neko je bežao"
                tacan = false
                bodovi = 0
            }
        )

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertPitanje(0, zlocin, "Da li si otisao do njene sobe?") } returns pitanje
        coEvery { mockRepository.getAllOdgovorForPitanje(pitanje) } returns odgovori

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertPitanje(0, zlocin, "Da li si otisao do njene sobe?")
        val result4 = viewModel.getAllOdgovorForPitanje(pitanje)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(2, result4?.size)
        assertEquals("Ne", result4?.get(0)?.tekstOdgovora)
        assertEquals("Da, neko je bežao", result4?.get(1)?.tekstOdgovora)
    }

    @Test
    fun `insertBeleska returns existing BeleskaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val beleska = BeleskaR().apply {
            idBeleska = 0
            zlocinId = zlocin
            tekst = "Moram da stignem pre njega"
            datum = datumM
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertBeleska(0, zlocin, "Moram da stignem pre njega", datumM) } returns beleska

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertBeleska(0, zlocin, "Moram da stignem pre njega", datumM)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(0, result3?.idBeleska)
        assertEquals("Moram da stignem pre njega", result3?.tekst)
        assertEquals(datumM, result3?.datum)
    }

    @Test
    fun `insertAplikacija returns existing AplikacijaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val aplikacija = AplikacijaR().apply {
            idAplikacije = 0
            naziv = "Instagram"
            tip = 0
            zrtvaId = zrtva
            aktivna = true
            informacije = "Poslednja aktivnost na Instagram profilu žrtve."
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertAplikacija(0, zrtva, "Instagram", 0, true, "Poslednja aktivnost na Instagram profilu žrtve.") } returns aplikacija


        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertAplikacija(0, zrtva, "Instagram", 0, true, "Poslednja aktivnost na Instagram profilu žrtve.")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(0, result5?.idAplikacije)
        assertEquals("Instagram", result5?.naziv)
        assertEquals(0, result5?.tip)
        assertEquals(true, result5?.aktivna)
        assertEquals("Poslednja aktivnost na Instagram profilu žrtve.", result5?.informacije)
    }

    @Test
    fun `insertTrag returns existing TragR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val forenzickiDokaz = ForenzickiDokazR().apply {
            idForenzickiDokaz = 0
            tipForenzickiDokaz = "DNK"
            opis = "DNK tragovi pronađeni na pištolju."
            status = 0
            veza = "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed."
            zrtvaId = zrtva
        }

        var motivO = MotivR().apply {
            idMotiv = 1
            opis = "Ljubomora"
        }

        val osumnjiceni = OsumnjicenR().apply {
            idOsumnjicen = 1
            status = 0
            tipOsumnjicen = "pojedinac"
            motiv = motivO
            zlocinId = zlocin
            kriv = 0
            osobaId = osoba
        }

        val trag = TragR().apply {
            idTrag = 0
            forenzickiDokazId = forenzickiDokaz
            osumnjicenId = osumnjiceni
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertForenzickiDokaz(0, "DNK", "DNK tragovi pronađeni na pištolju.", 0, zrtva, "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.") } returns forenzickiDokaz
        coEvery { mockRepository.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M") } returns osumnjiceni
        coEvery { mockRepository.insertMotiv("Ljubomora") } returns motivO
        coEvery { mockRepository.insertTrag(0, forenzickiDokaz, osumnjiceni) } returns trag

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertForenzickiDokaz(0, "DNK", "DNK tragovi pronađeni na pištolju.", 0, zrtva, "DNK tragovi na pištolju se poklapaju sa DNK Olivije Reed.")
        val result6 = viewModel.insertMotiv("Ljubomora")
        val result7 = viewModel.insertOsumnjiceni(1, "Petar", 0, "pojedinac", motivO, zlocin, 0, "petar@example.com", datumM, "inženjer", "M")
        val result8 = viewModel.insertTrag(0, forenzickiDokaz, osumnjiceni)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertNotNull(result7)
        assertNotNull(result8)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zrtva, result5?.zrtvaId)
        assertEquals(motivO, result7?.motiv)
        assertEquals(zlocin, result7?.zlocinId)
        assertEquals(forenzickiDokaz, result8?.forenzickiDokazId)
        assertEquals(osumnjiceni, result8?.osumnjicenId)
        assertEquals(0, result8?.idTrag)
    }

    @Test
    fun `insertWhatsAppKontakt returns existing WhatsAppKontaktR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val waKontakt = WhatsAppKontaktR().apply {
            idWhatsAppKontakt = 0
            zlocinId = zlocin
            ime = "Tom"
            broj = "+1231423152"
            slika = 13
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertWhatsAppKontakt(0, zlocin, "Tom", "+1231423152", 13) } returns waKontakt

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertWhatsAppKontakt(0, zlocin, "Tom", "+1231423152", 13)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(0, result3?.idWhatsAppKontakt)
        assertEquals("Tom", result3?.ime)
        assertEquals("+1231423152", result3?.broj)
        assertEquals(13, result3?.slika)
    }

    @Test
    fun `insertWhatsAppPoruka returns existing WhatsAppPorukaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val waKontakt1 = WhatsAppKontaktR().apply {
            idWhatsAppKontakt = 0
            zlocinId = zlocin
            ime = "Tom"
            broj = "+1231423152"
            slika = 13
        }

        val waKontakt2 = WhatsAppKontaktR().apply {
            idWhatsAppKontakt = 1
            zlocinId = zlocin
            ime = "Peter"
            broj = "+1231423153"
            slika = 13
        }

        val waPoruka = WhatsAppPorukaR().apply {
            idWhatsAppPoruka = 0
            kontaktKoSalje = waKontakt1
            kontaktKomeSalje = waKontakt2
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije."
            datum = datumM
            procitana = false
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertWhatsAppKontakt(0, zlocin, "Tom", "+1231423152", 13) } returns waKontakt1
        coEvery { mockRepository.insertWhatsAppKontakt(1, zlocin, "Peter", "+1231423153", 13) } returns waKontakt2
        coEvery { mockRepository.insertWhatsAppPoruka(0, waKontakt1, waKontakt2, "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.", datumM, false) } returns waPoruka

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertWhatsAppKontakt(0, zlocin, "Tom", "+1231423152", 13)
        val result4 = viewModel.insertWhatsAppKontakt(1, zlocin, "Peter", "+1231423153", 13)
        val result5 = viewModel.insertWhatsAppPoruka(0, waKontakt1, waKontakt2, "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.", datumM, false)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(waKontakt1, result5?.kontaktKoSalje)
        assertEquals(waKontakt2, result5?.kontaktKomeSalje)
        assertEquals(0, result5?.idWhatsAppPoruka)
        assertEquals("Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.", result5?.tekst)
        assertEquals(false, result5?.procitana)
        assertEquals(datumM, result5?.datum)
    }

    @Test
    fun `insertOneContact returns existing OneContactR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val kontakt = OneContactR().apply {
            idOneContact = 0
            zlocinId = zlocin
            ime = "John"
            broj = "+54533465645"
            slika = 1
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOneContact(0, zlocin, "John", "+54533465645", 1) } returns kontakt

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOneContact(0, zlocin, "John", "+54533465645", 1)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(0, result3?.idOneContact)
        assertEquals("John", result3?.ime)
        assertEquals("+54533465645", result3?.broj)
        assertEquals(1, result3?.slika)
    }

    @Test
    fun `insertOneCall returns existing OneCallR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val osoba = OsobaR().apply {
            idOsoba = 1
            ime = "Petar"
            kontakt = "petar@example.com"
            datum = datumM
            zanimanje = "inženjer"
            pol = "M"
            zlocinId = zlocin
        }

        val zrtva = ZrtvaR().apply {
            idZrtva = 1
            tipZrtve = "zena"
            detalji = "Korumpirana advokatica pronadjena mrtva u vozu."
            statusZrtva = StatusZrtvaR.mrtva.name
            zlocinId = zlocin
            osobaId = osoba
        }

        val kontaktOC = OneContactR().apply {
            idOneContact = 0
            zlocinId = zlocin
            ime = "John"
            broj = "+54533465645"
            slika = 1
        }

        val oneCall = OneCallR().apply {
            idOneCall = 0
            kontakt = kontaktOC
            datum = datumM
            propusten = false
            dolazni = true
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin) } returns osoba
        coEvery { mockRepository.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol) } returns zrtva
        coEvery { mockRepository.insertOneContact(0, zlocin, "John", "+54533465645", 1) } returns kontaktOC
        coEvery { mockRepository.insertOneCall(0, kontaktOC, datumM, false, true) } returns oneCall

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOsoba(1, "Petar", "petar@example.com", datumM, "inženjer", "M", zlocin)
        val result4 = viewModel.insertZrtva(1, "zena", osoba.ime, "Korumpirana advokatica pronadjena mrtva u vozu.", StatusZrtvaR.mrtva.name, zlocin, osoba.kontakt, datumM, osoba.zanimanje, osoba.pol)
        val result5 = viewModel.insertOneContact(0, zlocin, "John", "+54533465645", 1)
        val result6 = viewModel.insertOneCall(0, kontaktOC, datumM, false, true)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertNotNull(result6)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(zlocin, result5?.zlocinId)
        assertEquals(kontaktOC, result6?.kontakt)
        assertEquals(0, result6?.idOneCall)
        assertEquals(false, result6?.propusten)
        assertEquals(true, result6?.dolazni)
        assertEquals(datumM, result6?.datum)
    }

    @Test
    fun `insertGalleryPhoto returns existing GalleryR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val gallery = GalleryR().apply {
            idPhoto = 0
            zlocinId = zlocin
            slika = 1
            datum = datumM
            mesto = "Amsterdam"
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertGalleryPhoto(0, zlocin, 1, datumM, "Amsterdam") } returns gallery

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertGalleryPhoto(0, zlocin, 1, datumM, "Amsterdam")

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(0, result3?.idPhoto)
        assertEquals(1, result3?.slika)
        assertEquals("Amsterdam", result3?.mesto)
        assertEquals(datumM, result3?.datum)
    }

    @Test
    fun `insertObicnaPoruka returns existing ObicnaPorukaR`() = runTest {
        var datumM = RealmInstant.now()

        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        val zlocin = ZlocinR().apply {
            idZlocin = 1
            tipZlocinaId = tipZlocina
            naziv = "Ubistvo u starom hotelu"
            datum = datumM
            mesto = "Pariz"
            opis = "Ubistvo u mladog preduzetnika"
            status = stZlocinR.u_istrazi.name
        }

        val kontakt1 = OneContactR().apply {
            idOneContact = 0
            zlocinId = zlocin
            ime = "John"
            broj = "+54533465645"
            slika = 1
        }

        val kontakt2 = OneContactR().apply {
            idOneContact = 1
            zlocinId = zlocin
            ime = "Peter"
            broj = "+54533465646"
            slika = 1
        }

        val poruka = ObicnaPorukaR().apply {
            idObicnaPoruka = 0
            kontaktKoSalje = kontakt1
            kontaktKomeSalje = kontakt2
            tekst = "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije."
            datum = datumM
            procitana = false
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina
        coEvery { mockRepository.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name) } returns zlocin
        coEvery { mockRepository.insertOneContact(0, zlocin, "John", "+54533465645", 1) } returns kontakt1
        coEvery { mockRepository.insertOneContact(1, zlocin, "Peter", "+54533465646", 1) } returns kontakt2
        coEvery { mockRepository.insertObicnaPoruka(kontakt1, kontakt2, "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.", datumM, false) } returns poruka

        val result = viewModel.inserTipZlocina("Kradja")
        val result2 = viewModel.insertZlocin(1, tipZlocina,"Ubistvo u starom hotelu","2024-11-11","Pariz","Ubistvo u mladog preduzetnika", stZlocinR.u_istrazi.name)
        val result3 = viewModel.insertOneContact(0, zlocin, "John", "+54533465645", 1)
        val result4 = viewModel.insertOneContact(1, zlocin, "Peter", "+54533465646", 1)
        val result5 = viewModel.insertObicnaPoruka(kontakt1, kontakt2, "Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.", datumM, false)

        assertNotNull(result)
        assertNotNull(result2)
        assertNotNull(result3)
        assertNotNull(result4)
        assertNotNull(result5)
        assertEquals(tipZlocina, result2?.tipZlocinaId)
        assertEquals(zlocin, result3?.zlocinId)
        assertEquals(zlocin, result4?.zlocinId)
        assertEquals(kontakt1, result5?.kontaktKoSalje)
        assertEquals(kontakt2, result5?.kontaktKomeSalje)
        assertEquals("Upoznao sam je u baru. Delovala je cudno, ali idem do njene sobe. Javljam se kasnije.", result5?.tekst)
        assertEquals(false, result5?.procitana)
        assertEquals(datumM, result5?.datum)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}