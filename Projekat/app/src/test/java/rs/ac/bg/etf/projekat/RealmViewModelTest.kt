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
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR
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

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}