package rs.ac.bg.etf.projekat

import io.mockk.coEvery
import io.mockk.mockk
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
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RepositoryRealmViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class RealmViewModelTest {

    private val mockRepository: RepositoryRealmViewModel = mockk()
    private lateinit var viewModel: RealmViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        //viewModel = RealmViewModel(mockRepository)
    }

    @Test
    fun `insertTipZlocina returns existing TipZlocinaR`() = runTest {
        val tipZlocina = TipZlocinaR().apply {
            idTipZlocina = 1
            nazivTipaZlocina = "Kradja"
        }

        coEvery { mockRepository.insertTipZlocina("Kradja") } returns tipZlocina

        val result = viewModel.inserTipZlocina("Kradja")

        assert(result != null)
        assert(result?.nazivTipaZlocina == "Kradja")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
