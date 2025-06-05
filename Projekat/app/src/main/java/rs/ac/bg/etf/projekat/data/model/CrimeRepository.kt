package rs.ac.bg.etf.projekat.data.model

import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR

interface CrimeRepository {
    suspend fun selectLokacijeIstrageR(): List<LokacijeIstrageR>
}