package rs.ac.bg.etf.projekat

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR

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
            LokacijeIstrageR().apply {
                idLokacijeIstrage = 1;
                mesto = "London";
                naziv = "Bolnica Sv.Petra" ;
                opis = "Univerzitetska bolnica";
                //zlocinId
                geoTackaALatitude = 2.3;
                geoTackaALongitude = 5.5;
            },
            LokacijeIstrageR().apply { idLokacijeIstrage = 2; naziv = "Mesto 2" }
        )
        repository.fakeData = expected

        val result = repository.selectLokacijeIstrageR()

        assertEquals(2, result.size)
        assertEquals(1, result[0].idLokacijeIstrage)
        assertEquals("Bolnica Sv.Petra", result[0].naziv)
        assertEquals("London", result[0].mesto)
        assertEquals(2.3, result[0].geoTackaALatitude)
        assertEquals(5.5, result[0].geoTackaALongitude)

        assertEquals("Mesto 2", result[1].naziv)
    }
}
