package rs.ac.bg.etf.projekat.data

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeOsumnjicenogZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeSvedokaZadatakR
import rs.ac.bg.etf.projekat.data.realm.IzjavaZaPacijentaR
import rs.ac.bg.etf.projekat.data.realm.LekarskiTestR
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR
import rs.ac.bg.etf.projekat.data.realm.MedicinskiIzvestajR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.PacijentR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeOsumnjicenogR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR
import rs.ac.bg.etf.projekat.data.realm.PorukeZadatakR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonZadatakR
import rs.ac.bg.etf.projekat.data.realm.ZadatakR
import javax.inject.Inject

class CommonRepository @Inject constructor(
    private val realm: Realm, // ili neki RealmHelper
) {
    suspend fun selectAllOsumnjiceni(): List<OsumnjicenR> {
        val osumnjiceni: List<OsumnjicenR>

        osumnjiceni = realm.query<OsumnjicenR>().find()

        return osumnjiceni
    }

    suspend fun selectAllSvedoci(): List<SvedokR> {
        val svedoci: List<SvedokR>

        svedoci = realm.query<SvedokR>().find()

        return svedoci
    }

    suspend fun selectAllPitanjaIspitivanjeOsumnjicenog(): List<PitanjeIspitivanjeOsumnjicenogR> {
        val pitanja: List<PitanjeIspitivanjeOsumnjicenogR>

        pitanja = realm.query<PitanjeIspitivanjeOsumnjicenogR>().find()
        return pitanja
    }

    suspend fun selectPitanjaByOsumnjicenAndCategory(osumnjicenId: String, category: String): List<PitanjeIspitivanjeOsumnjicenogR> {
        val pitanja: List<PitanjeIspitivanjeOsumnjicenogR>

        pitanja = realm.query<PitanjeIspitivanjeOsumnjicenogR>(
            "osumnjicenId.osobaId.ime == $0 AND kategorija == $1",
            osumnjicenId,
            category
        ).find()

        return pitanja
    }

    suspend fun selectPitanjaBySvedok(svedokId: String): List<PitanjeIspitivanjeSvedokaR> {
        val pitanja: List<PitanjeIspitivanjeSvedokaR>

        pitanja = realm.query<PitanjeIspitivanjeSvedokaR>(
            "svedokId.osobaId.ime == $0",
            svedokId,
        ).find()

        return pitanja
    }

    suspend fun selectTasks(): List<ZadatakR> {
        val zadaci: List<ZadatakR>

        zadaci = realm.query<ZadatakR>().find()

        return zadaci.reversed()
    }

    suspend fun selectEvidences(): List<DokazR>{
        val dokazi: List<DokazR>

        dokazi = realm.query<DokazR>().find()

        return dokazi
    }

    suspend fun selectEvidencesTasks(evidences: List<DokazR>): List<DokazZadatakR> {
        val allDokazZadatak: List<DokazZadatakR> = realm.query<DokazZadatakR>().find()

        val evidenceIds: List<Int> = evidences.map { it.idDokaz }

        val filteredDokazZadatak = allDokazZadatak.filter { task ->
            evidenceIds.contains(task.dokazId?.idDokaz)
        }

        return filteredDokazZadatak
    }

    suspend fun selectForensicEvidences(): List<ForenzickiDokazR>{
        val dokazi: List<ForenzickiDokazR>

        dokazi = realm.query<ForenzickiDokazR>().find()

        return dokazi
    }

    suspend fun selectForensicEvidencesTasks(evidences: List<ForenzickiDokazR>): List<ForenzickiDokazZadatakR> {
        val allDokazZadatak: List<ForenzickiDokazZadatakR> = realm.query<ForenzickiDokazZadatakR>().find()

        val evidenceIds: List<Int> = evidences.map { it.idForenzickiDokaz }

        val filteredDokazZadatak = allDokazZadatak.filter { task ->
            evidenceIds.contains(task.forenzickiDokazId?.idForenzickiDokaz)
        }

        return filteredDokazZadatak
    }

    private fun evidenceIds(evidences: List<DokazR>): List<Int> {
        return evidences.map { it.idDokaz } // Pretpostavljamo da DokazR ima polje 'idDokaz'
    }

    suspend fun updateDokazZadatakAndZadatak(zadatakId: Int,dokazZadatakId: Int ) {
        val realm = realm
        realm.write {
            val dokazZadaci = query(DokazZadatakR::class).find()

            val dokazZadatak = dokazZadaci.firstOrNull { it.idDokazZadatak == dokazZadatakId }

            if (dokazZadatak != null) {
                dokazZadatak.uradjen = true

                val zadaci = query(ZadatakR::class).find()

                val zadatak = zadaci.firstOrNull { it.idZadatak == zadatakId }

                if (zadatak != null) {
                    zadatak.uradjen = true
                }
            }
        }
    }

    suspend fun updateForenzickiDokazZadatakAndZadatak(zadatakId: Int,dokazZadatakId: Int ) {
        val realm = realm
        realm.write {
            val dokazZadaci = query(ForenzickiDokazZadatakR::class).find()

            val dokazZadatak = dokazZadaci.firstOrNull { it.idForenzickiDokazZadatak == dokazZadatakId }

            if (dokazZadatak != null) {
                dokazZadatak.uradjen = true

                val zadaci = query(ZadatakR::class).find()

                val zadatak = zadaci.firstOrNull { it.idZadatak == zadatakId }

                if (zadatak != null) {
                    zadatak.uradjen = true
                }
            }
        }
    }

    suspend fun updateIspitivanjeOsumnjicenogZadatak(ispitivanjeOsumnjicenogZadatak: Int,zadatakId:Int ) {
        val realm = realm

        realm.write {
            val ispitivanje = query(IspitivanjeOsumnjicenogZadatakR::class).find()

            val ispitivanjeZ =
                ispitivanje.firstOrNull { it.idIspitivanjeOsumnjicenogZadatak == ispitivanjeOsumnjicenogZadatak }

            if (ispitivanjeZ != null) {
                ispitivanjeZ.uradjen = true

                val zadaci = query(ZadatakR::class).find()

                val zadatak = zadaci.firstOrNull { it.idZadatak == zadatakId }

                if (zadatak != null) {
                    zadatak.uradjen = true
                }
            }
        }
    }

    fun selectIspitivanjeOsumnjicenogZadatak(osumnjicenZ: OsumnjicenR?): IspitivanjeOsumnjicenogZadatakR? {
        Log.d("UPO",osumnjicenZ.toString())
        val zadaci = realm.query<IspitivanjeOsumnjicenogZadatakR>(
            "osumnjicenId == $0 AND uradjen == $1",
            osumnjicenZ, false
        ).find()
        Log.d("UPO",zadaci.toString())
        return zadaci.firstOrNull()
    }



    suspend fun updateIspitivanjeSvedokaZadatak(ispitivanjeSvedokaZadatak: Int,zadatakId:Int ) {
        val realm = realm

        realm.write {
            val ispitivanje = query(IspitivanjeSvedokaZadatakR::class).find()

            val ispitivanjeZ =
                ispitivanje.firstOrNull { it.idIspitivanjeSvedokaZadatak == ispitivanjeSvedokaZadatak }

            if (ispitivanjeZ != null) {
                ispitivanjeZ.uradjen = true

                val zadaci = query(ZadatakR::class).find()

                val zadatak = zadaci.firstOrNull { it.idZadatak == zadatakId }

                if (zadatak != null) {
                    zadatak.uradjen = true
                }
            }
        }
    }

    fun selectIspitivanjeSvedokaZadatak(svedokZ: SvedokR?): IspitivanjeSvedokaZadatakR? {
        val zadaci = realm.query<IspitivanjeSvedokaZadatakR>(
            "svedokId == $0 AND uradjen == $1",
            svedokZ, false
        ).find()

        return zadaci.firstOrNull()
    }


    suspend fun updateTelefonZadatak(telefonZadatak: Int,zadatakId:Int ) {
        val realm = realm

        realm.write {
            val telefon = query(TelefonZadatakR::class).find()

            val telefonZ =
                telefon.firstOrNull { it.idTelefonZadatak == telefonZadatak }

            if (telefonZ != null) {
                telefonZ.uradjen = true

                val zadaci = query(ZadatakR::class).find()

                val zadatak = zadaci.firstOrNull { it.idZadatak == zadatakId }

                if (zadatak != null) {
                    zadatak.uradjen = true
                }
            }
        }
    }


    suspend fun updatePorukeZadatak(porukeZadatak: Int,zadatakId:Int ) {
        val realm = realm

        realm.write {
            val poruke = query(PorukeZadatakR::class).find()

            val porukeZ =
                poruke.firstOrNull { it.idPorukeZadatak == porukeZadatak }

            if (porukeZ != null) {
                porukeZ.uradjen = true

                val zadaci = query(ZadatakR::class).find()
                val zadatak = zadaci.firstOrNull { it.idZadatak == zadatakId }

                if (zadatak != null) {
                    zadatak.uradjen = true
                }
            }
        }
    }

    //izmeniti -> dodati Telefon kao parametar
    fun selectTelefonZadatak(): TelefonZadatakR? {
        val zadaci = realm.query<TelefonZadatakR>(
            "uradjen == $0",
            false
        ).find()

        return zadaci.firstOrNull()
    }

    //izmeniti -> dodati poruke kao parametar
    fun selectPorukeZadatak(): PorukeZadatakR? {
        val zadaci = realm.query<PorukeZadatakR>(
            "uradjen == $0",
            false
        ).find()

        return zadaci.firstOrNull()
    }


//Mysterious Symptoms

    suspend fun selectPacijent(): PacijentR {
        val pacijent: PacijentR
        pacijent = realm.query<PacijentR>().find().first()
        return pacijent
    }

    suspend fun selectMedicinskiIzvestaj(): MedicinskiIzvestajR {
        val med: MedicinskiIzvestajR
        med = realm.query<MedicinskiIzvestajR>().find().first()
        return med
    }

    suspend fun selectLekarskiTest(): LekarskiTestR {
        val lek: LekarskiTestR
        lek = realm.query<LekarskiTestR>().find().first()
        return lek
    }

    suspend fun selectIzjavaZaPacijenta(): IzjavaZaPacijentaR {
        val izj: IzjavaZaPacijentaR
        izj = realm.query<IzjavaZaPacijentaR>().find().first()
        return izj
    }

    suspend fun selectLokacijeIstrageR(): List<LokacijeIstrageR> {
        val lokacije: List<LokacijeIstrageR>
        lokacije = realm.query<LokacijeIstrageR>().find()
        return lokacije
    }
}
