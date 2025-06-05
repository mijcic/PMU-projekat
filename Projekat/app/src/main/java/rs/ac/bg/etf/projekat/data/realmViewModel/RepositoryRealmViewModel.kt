package rs.ac.bg.etf.projekat.data.realmViewModel

import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR

interface RepositoryRealmViewModel {
    suspend fun insertTipZlocina(nazivTZ: String): TipZlocinaR?
}