package rs.ac.bg.etf.projekat.data.model

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR

class CrimeRepositoryImpl(private val realm: Realm) : CrimeRepository {
    override suspend fun selectLokacijeIstrageR(): List<LokacijeIstrageR> {
        return realm.query<LokacijeIstrageR>().find()
    }
}
