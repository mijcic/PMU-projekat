package rs.ac.bg.etf.projekat


import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import rs.ac.bg.etf.projekat.data.CommonRepository
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.Repository

@ExperimentalCoroutinesApi
class MyViewModelTest {

    // 1. Mockovanje zavisnosti
    private val mockMyRepository: Repository = mockk()
    private val mockCommonRepository: CommonRepository = mockk()

    // ViewModel koji ćemo testirati
    private lateinit var viewModel: MyViewModel

    // Dispatcher za testiranje korutina
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Postavi Main Dispatcher da koristi naš testDispatcher
        Dispatchers.setMain(testDispatcher)

        // Inicijalizuj ViewModel sa mock zavisnostima
        viewModel = MyViewModel(mockMyRepository, mockCommonRepository)
    }

    @After
    fun tearDown() {
        // Resetuj Main Dispatcher nakon svakog testa
        Dispatchers.resetMain()
    }

    /*
    // --- Testovi za getAllDataZlocin funkciju ---
    @Test
    fun `getAllDataZlocin successfully updates uiStateZlocinData with data`() = runTest {
        val expectedSuspects = listOf(Osumnjiceni("Suspect1"), Osumnjiceni("Suspect2"))
        val expectedWitnesses = listOf(Svedok("Witness1"), Svedok("Witness2"))

        coEvery { mockCommonRepository.selectAllOsumnjiceni() } returns expectedSuspects
        coEvery { mockCommonRepository.selectAllSvedoci() } returns expectedWitnesses

        viewModel.getAllDataZlocin()

        val finalState = viewModel.uiStateZlocinData.first()

        assertEquals(expectedSuspects, finalState.suspects)
        assertEquals(expectedWitnesses, finalState.witnesses)
    }

    @Test
    fun `getAllDataZlocin handles error and updates uiStateZlocinData with empty lists`() = runTest {
        val errorMessage = "Data fetch error"
        val expectedException = Exception(errorMessage)

        coEvery { mockCommonRepository.selectAllOsumnjiceni() } throws expectedException
        coEvery { mockCommonRepository.selectAllSvedoci() } throws expectedException // Mock obe ako obe mogu baciti

        viewModel.getAllDataZlocin()

        val finalState = viewModel.uiStateZlocinData.first()

        assertEquals(emptyList<Osumnjiceni>(), finalState.suspects)
        assertEquals(emptyList<Svedok>(), finalState.witnesses)
    }


    // --- Testovi za getPitanjaZaOsumnjicenog funkciju ---
    @Test
    fun `getPitanjaZaOsumnjicenog successfully updates uiStatePitanjaZaOsumnjicenog with data`() = runTest {
        val osumnjicen = "John Doe"
        val opstaPitanja = listOf(Pitanje("Opste 1"), Pitanje("Opste 2"))
        val alibiPitanja = listOf(Pitanje("Alibi 1"))
        val dokazPitanja = listOf(Pitanje("Dokaz 1"))
        val kontradikcijaPitanja = listOf(Pitanje("Kontradikcija 1"))

        coEvery { mockCommonRepository.selectPitanjaByOsumnjicenAndCategory(osumnjicen, "opsta") } returns opstaPitanja
        coEvery { mockCommonRepository.selectPitanjaByOsumnjicenAndCategory(osumnjicen, "alibi") } returns alibiPitanja
        coEvery { mockCommonRepository.selectPitanjaByOsumnjicenAndCategory(osumnjicen, "dokaz") } returns dokazPitanja
        coEvery { mockCommonRepository.selectPitanjaByOsumnjicenAndCategory(osumnjicen, "kontradikcija") } returns kontradikcijaPitanja

        viewModel.getPitanjaZaOsumnjicenog(osumnjicen)

        val finalState = viewModel.uiStatePitanjaZaOsumnjicenog.first()

        assertEquals(opstaPitanja, finalState.opstaPitanja)
        assertEquals(alibiPitanja, finalState.alibiPitanja)
        assertEquals(dokazPitanja, finalState.dokazPitanja)
        assertEquals(kontradikcijaPitanja, finalState.kontradikcijaPitanja)
    }

    @Test
    fun `getPitanjaZaOsumnjicenog handles error and updates uiStatePitanjaZaOsumnjicenog with empty lists`() = runTest {
        val osumnjicen = "Jane Doe"
        val errorMessage = "Error fetching questions"
        val expectedException = Exception(errorMessage)

        // Mockiramo da SVI pozivi bacaju izuzetak
        coEvery { mockCommonRepository.selectPitanjaByOsumnjicenAndCategory(any(), any()) } throws expectedException

        viewModel.getPitanjaZaOsumnjicenog(osumnjicen)

        val finalState = viewModel.uiStatePitanjaZaOsumnjicenog.first()

        assertEquals(emptyList<Pitanje>(), finalState.opstaPitanja)
        assertEquals(emptyList<Pitanje>(), finalState.alibiPitanja)
        assertEquals(emptyList<Pitanje>(), finalState.dokazPitanja)
        assertEquals(emptyList<Pitanje>(), finalState.kontradikcijaPitanja)
    }

    // --- Testovi za getPitanjaZaSvedoka funkciju ---
    @Test
    fun `getPitanjaZaSvedoka successfully updates uiStatePitanjaZaSvedoka with data`() = runTest {
        val svedok = "Alice Smith"
        val expectedPitanja = listOf(Pitanje("Svedok 1"), Pitanje("Svedok 2"))

        coEvery { mockCommonRepository.selectPitanjaBySvedok(svedok) } returns expectedPitanja

        viewModel.getPitanjaZaSvedoka(svedok)

        val finalState = viewModel.uiStatePitanjaZaSvedoka.first()

        assertEquals(expectedPitanja, finalState.pitanja)
    }

    @Test
    fun `getPitanjaZaSvedoka handles error and updates uiStatePitanjaZaSvedoka with empty list`() = runTest {
        val svedok = "Bob Johnson"
        val errorMessage = "Error fetching witness questions"
        val expectedException = Exception(errorMessage)

        coEvery { mockCommonRepository.selectPitanjaBySvedok(svedok) } throws expectedException

        viewModel.getPitanjaZaSvedoka(svedok)

        val finalState = viewModel.uiStatePitanjaZaSvedoka.first()

        assertEquals(emptyList<Pitanje>(), finalState.pitanja)
    }*/
}