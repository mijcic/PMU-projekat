package rs.ac.bg.etf.projekat

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR

@OptIn(ExperimentalCoroutinesApi::class)
class CrimeRepositoryTest {

    private lateinit var repository: FakeCrimeRepository

    @Before
    fun setup() {
        repository = FakeCrimeRepository()
    }

    @Test
    fun `selectLokacijeIstrageR returns correct data`() = runTest {
        val expected = listOf(
            LokacijeIstrageR().apply { idLokacijeIstrage = 1; naziv = "Mesto 1" },
            LokacijeIstrageR().apply { idLokacijeIstrage = 2; naziv = "Mesto 2" }
        )

        repository.fakeData = expected

        val result = repository.selectLokacijeIstrageR()

        assertEquals(2, result.size)
        assertEquals("Mesto 1", result[0].naziv)
    }
}
