package rs.ac.bg.etf.projekat

import rs.ac.bg.etf.projekat.data.model.CrimeRepository
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR

class FakeCrimeRepository : CrimeRepository {
    var fakeData: List<LokacijeIstrageR> = emptyList()

    override suspend fun selectLokacijeIstrageR(): List<LokacijeIstrageR> {
        return fakeData
    }
}
