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
import rs.ac.bg.etf.projekat.data.realm.ZrtvaR
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

    suspend fun selectVictim(): ZrtvaR {
        val zrtva : List<ZrtvaR>

        zrtva = realm.query<ZrtvaR>().find()

        return zrtva.first()
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

    suspend fun updatePitanjaZaOsumnjicenogPitanjaEmpty(osumnjicen: String){
        realm.write {
            val ispitivanjeZ = query(
                IspitivanjeOsumnjicenogZadatakR::class,
                "osumnjicenId.osobaId.ime == $0",
                osumnjicen
            ).first().find()

            if (ispitivanjeZ != null) {
                ispitivanjeZ.uradjen = true

                val zadatak = query(
                    ZadatakR::class,
                    "idZadatak == $0",
                    ispitivanjeZ.zadatakId?.idZadatak
                ).first().find()

                if (zadatak != null) {
                    zadatak.uradjen = true
                    println("Zadatak  označen kao uradjen.")
                } else {
                    println("Zadatak  nije pronađen.")
                }
            } else {
                println("Ispitivanje  nije pronađeno.")
            }
        }
    }


    suspend fun updatePitanjaZaSvedokaPitanjaEmpty(svedok: String){
        Log.d("SVEDOK ",svedok)
        realm.write {
            val ispitivanjeZ = query(
                IspitivanjeSvedokaZadatakR::class,
                "svedokId.osobaId.ime == $0",
                svedok
            ).first().find()

            Log.d("SVEDOK ",ispitivanjeZ?.idIspitivanjeSvedokaZadatak.toString())
            Log.d("SVEDOK ",ispitivanjeZ?.zadatakId.toString())

            if (ispitivanjeZ != null) {
                ispitivanjeZ.uradjen = true

                val zadatak = query(
                    ZadatakR::class,
                    "idZadatak == $0",
                    ispitivanjeZ.zadatakId?.idZadatak
                ).first().find()

                Log.d("SVEDOK ",zadatak?.idZadatak.toString())

                if (zadatak != null) {
                    zadatak.uradjen = true
                    println("Zadatak  označen kao uradjen.")
                } else {
                    println("Zadatak  nije pronađen.")
                }
            } else {
                println("Ispitivanje  nije pronađeno.")
            }
        }
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
        realm.write {
            val dokazZadatak = query(DokazZadatakR::class, "idDokazZadatak == $0", dokazZadatakId)
                .first().find()

            if (dokazZadatak != null) {
                dokazZadatak.uradjen = true
                println("Označen DokazZadatak $dokazZadatakId kao uradjen.")

                val zadatak = query(ZadatakR::class, "idZadatak == $0", zadatakId)
                    .first().find()

                if (zadatak != null) {
                    zadatak.uradjen = true
                    println("Označen Zadatak $zadatakId kao uradjen.")
                } else {
                    println("Zadatak $zadatakId nije pronađen.")
                }
            } else {
                println("DokazZadatak $dokazZadatakId nije pronađen.")
            }
        }
    }

    suspend fun updateForenzickiDokazZadatakAndZadatak(zadatakId: Int,dokazZadatakId: Int ) {
        realm.write {
            val dokazZadatak = query(
                ForenzickiDokazZadatakR::class,
                "idForenzickiDokazZadatak == $0",
                dokazZadatakId
            ).first().find()

            if (dokazZadatak != null) {
                dokazZadatak.uradjen = true
                println("Forenzički dokaz $dokazZadatakId označen kao uradjen.")

                val zadatak = query(
                    ZadatakR::class,
                    "idZadatak == $0",
                    zadatakId
                ).first().find()

                if (zadatak != null) {
                    zadatak.uradjen = true
                    println("Zadatak $zadatakId označen kao uradjen.")
                } else {
                    println("Zadatak $zadatakId nije pronađen.")
                }
            } else {
                println("Forenzički dokaz $dokazZadatakId nije pronađen.")
            }
        }
    }

    suspend fun updateIspitivanjeOsumnjicenogZadatak(ispitivanjeOsumnjicenogZadatakId: Int,zadatakId:Int ) {
        realm.write {
            val ispitivanjeZ = query(
                IspitivanjeOsumnjicenogZadatakR::class,
                "idIspitivanjeOsumnjicenogZadatak == $0",
                ispitivanjeOsumnjicenogZadatakId
            ).first().find()

            if (ispitivanjeZ != null) {
                ispitivanjeZ.uradjen = true
                println("Ispitivanje osumnjičenog $ispitivanjeOsumnjicenogZadatakId označeno kao uradjeno.")

                val zadatak = query(
                    ZadatakR::class,
                    "idZadatak == $0",
                    zadatakId
                ).first().find()

                if (zadatak != null) {
                    zadatak.uradjen = true
                    println("Zadatak $zadatakId označen kao uradjen.")
                } else {
                    println("Zadatak $zadatakId nije pronađen.")
                }
            } else {
                println("Ispitivanje $ispitivanjeOsumnjicenogZadatakId nije pronađeno.")
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



    suspend fun updateIspitivanjeSvedokaZadatak(ispitivanjeSvedokaZadatakId: Int,zadatakId:Int ) {
        realm.write {
            val ispitivanjeZ = query(
                IspitivanjeSvedokaZadatakR::class,
                "idIspitivanjeSvedokaZadatak == $0",
                ispitivanjeSvedokaZadatakId
            ).first().find()

            if (ispitivanjeZ != null) {
                ispitivanjeZ.uradjen = true
                println("Ispitivanje svedoka $ispitivanjeSvedokaZadatakId označeno kao uradjeno.")

                val zadatak = query(
                    ZadatakR::class,
                    "idZadatak == $0",
                    zadatakId
                ).first().find()

                if (zadatak != null) {
                    zadatak.uradjen = true
                    println("Zadatak $zadatakId označen kao uradjen.")
                } else {
                    println("Zadatak $zadatakId nije pronađen.")
                }
            } else {
                println("Ispitivanje svedoka $ispitivanjeSvedokaZadatakId nije pronađeno.")
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


    suspend fun updateTelefonZadatak(telefonZadatakId: Int,zadatakId:Int ) {
        realm.write {
            val telefonZ = query(
                TelefonZadatakR::class,
                "idTelefonZadatak == $0",
                telefonZadatakId
            ).first().find()

            if (telefonZ != null) {
                telefonZ.uradjen = true
                println("TelefonZadatak $telefonZadatakId označen kao uradjen.")

                val zadatak = query(
                    ZadatakR::class,
                    "idZadatak == $0",
                    zadatakId
                ).first().find()

                if (zadatak != null) {
                    zadatak.uradjen = true
                    println("Zadatak $zadatakId označen kao uradjen.")
                } else {
                    println("Zadatak $zadatakId nije pronađen.")
                }
            } else {
                println("TelefonZadatak $telefonZadatakId nije pronađen.")
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

        Log.d("Telefon select",zadaci.firstOrNull().toString())
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

    suspend fun selectPacijent(): PacijentR? {
        val pacijent: PacijentR?
        pacijent = realm.query<PacijentR>().find().firstOrNull()

        return pacijent
    }

    suspend fun selectMedicinskiIzvestaj(): MedicinskiIzvestajR? {
        val med: MedicinskiIzvestajR?
        med = realm.query<MedicinskiIzvestajR>().find().firstOrNull()
        return med
    }

    suspend fun selectLekarskiTest(): LekarskiTestR? {
        val lek: LekarskiTestR?
        lek = realm.query<LekarskiTestR>().find().firstOrNull()
        return lek
    }

    suspend fun selectIzjavaZaPacijenta(): IzjavaZaPacijentaR? {
        val izj: IzjavaZaPacijentaR?
        izj = realm.query<IzjavaZaPacijentaR>().find().firstOrNull()
        return izj
    }

    suspend fun selectLokacijeIstrageR(): List<LokacijeIstrageR> {
        val lokacije: List<LokacijeIstrageR>
        lokacije = realm.query<LokacijeIstrageR>().find()
        return lokacije
    }

}
