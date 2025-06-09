package rs.ac.bg.etf.projekat.data.realmViewModel

import io.realm.kotlin.Realm
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import javax.inject.Inject

class RepositoryImplRealmViewModel @Inject constructor(
    private val realm: Realm
): RepositoryRealmViewModel {
    override suspend fun insertTipZlocina(nazivTZ: String): TipZlocinaR? {
        var tipZlocina: TipZlocinaR? = null
        realm.write {
            tipZlocina = query<TipZlocinaR>("nazivTipaZlocina == $0", nazivTZ).find().firstOrNull()

            if (tipZlocina == null) {
                val maxId = query<TipZlocinaR>().find().maxOfOrNull { it.idTipZlocina } ?: 0
                tipZlocina = TipZlocinaR().apply {
                    idTipZlocina = maxId + 1
                    nazivTipaZlocina = nazivTZ
                }
                copyToRealm(tipZlocina!!)
            }
        }
        return tipZlocina
    }
}
