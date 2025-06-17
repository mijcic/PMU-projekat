package com.example.repository

import com.example.models.dto.*
import com.example.models.dto.gemini.retrofit.GeminiResponse2MysteriousSymptoms
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofitMysteriousSymptoms
import com.example.models.interfaces.GeminiResponseCommon2
import com.example.models.interfaces.GeminiResponseRetrofitCommon
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class GeminiProMysteriousSymptomsRepositoryImpl : GeminiProMysteriousSymptomsRepository {

    override fun insertGeminiPacijent(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, zl: ZlocinData, repo: RepositoryInsert): PacijentData? {
        val pacijent = geminiResponse2.pacijentR

        val datumString = geminiResponse2.pacijentR.datumPrijave
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val datum = datumString?.let { LocalDate.parse(it, formatter) }
        val timestamp = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()


        val datumStringPrijavio = geminiResponse2.pacijentR.prijavio?.datum
        val datumPrijavio = datumStringPrijavio?.let { LocalDate.parse(it, formatter) }
        val timestampPrijavio = datumPrijavio?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        val datumStringOsoba = geminiResponse2.pacijentR.zrtvaId?.osobaId?.datum
        val datumOsoba = datumStringOsoba?.let { LocalDate.parse(it, formatter) }
        val timestampOsoba = datumOsoba?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        var prijavio: OsobaData? = null
        pacijent.prijavio?.let {
            it.kontakt?.let { it1 ->
                if (timestampPrijavio != null) {
                    prijavio= OsobaData(
                        idOsoba = it.idOsoba,
                        ime = it.ime,
                        kontakt = it1,
                        datum = timestampPrijavio,
                        zanimanje = it.zanimanje,
                        pol = it.pol,
                        zlocinId = it.zlocinId
                    )
                }
            }
        }

        var osoba: OsobaData? =null
        pacijent.zrtvaId?.osobaId?.let {
            if (timestampOsoba != null) {
                it.kontakt?.let { it1 ->
                    osoba= OsobaData(
                        idOsoba = it.idOsoba,
                        ime = it.ime,
                        kontakt = it1,
                        datum = timestampOsoba,
                        zanimanje = it.zanimanje,
                        pol = it.pol,
                        zlocinId = it.zlocinId
                    )
                }
            }
        }

        var zrtva: ZrtvaData? = null
        pacijent.zrtvaId?.let {
            zrtva= osoba?.let { it1 ->
                ZrtvaData(
                    idZrtva = it.idZrtva,
                    tipZrtve = it.tipZrtve,
                    detalji = it.detalji,
                    statusZrtva = it.statusZrtva,
                    zlocinId = zl.idZlocin,
                    osobaId = it1
                )
            }
        }

        prijavio?.let { repo.insertOsobaData(it, zl) }
        osoba?.let { repo.insertOsobaData(it, zl) }
        zrtva?.let { osoba?.let { it1 -> repo.insertZrtva(zrtva!!,zl, it1) } }

        var pac: PacijentData?= null
        prijavio?.let {
            if (timestamp != null) {
                zrtva?.let { it1 ->
                    pac= PacijentData(
                        idPacijent = pacijent.idPacijent,
                        simptomi = pacijent.simptomi,
                        statusPacijenta = pacijent.statusPacijenta,
                        datumPrijave = timestamp,
                        prijavio = it,
                        zlocinId = zl,
                        zrtvaId = it1
                    )

                    repo.insertPacijentData(pac!!)
                    geminiResponseRetrofit.pacijentRetrofit=pac
                }
            }
        }
        return pac
    }

    override fun insertGeminiMedicinskiIzvestaj(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData, repo: RepositoryInsert) {
        val medicinskiIzvestaj = geminiResponse2.medicinskiIzvestajR

        var medIzv= MedicinskiIzvestajData(
            idMedicinskiIzvestaj = medicinskiIzvestaj.idMedicinskiIzvestaj,
            rezime = medicinskiIzvestaj.rezime,
            CTnalaz = medicinskiIzvestaj.CTnalaz,
            MRInalaz = medicinskiIzvestaj.MRInalaz,
            krvnaSlika = medicinskiIzvestaj.krvnaSlika,
            toksikoloskeAnalize = medicinskiIzvestaj.toksikoloskeAnalize,
            zakljucak = medicinskiIzvestaj.zakljucak,
            pacijentId = pacijent
        )

        repo.insertMedicinskiIzvestajData(medicinskiIzvestaj = medIzv)
        geminiResponseRetrofit.medicinskiIzvestajRetrofit=medIzv
    }

    override fun insertGeminiIzjavaZaPacijenta(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData, zl: ZlocinData, repo: RepositoryInsert) {
        val izjave = geminiResponse2.izjavaZaPacijentaR

        val datumString = "2025-11-12"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val datum = datumString?.let { LocalDate.parse(it, formatter) }
        val timestamp = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()


        var osoba: OsobaData? =null

        osoba= timestamp?.let {
            OsobaData(
                idOsoba = 1,
                ime = "dsa",
                kontakt = "adf",
                datum = it,
                zanimanje = "asdf",
                pol = "adf",
                zlocinId = zl.idZlocin
            )
        }
        if (osoba != null) {
            repo.insertOsobaData(osoba,zl)
        }

        if (osoba != null) {
            val izjava = IzjavaZaPacijentaData(
                idIzjavaZaPacijenta = izjave.idIzjavaZaPacijenta,
                izjava = izjave.izjava,
                pacijentId = pacijent,
                osobaId = osoba!!
            )

            repo.insertIzjavaZaPacijentaData(izjava, pacijent, osoba!!)
            geminiResponseRetrofit.izjavaZaPacijentaRetrofit = izjava
        }
    }

    override fun insertGeminiLekarskiTest(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData, repo: RepositoryInsert) {
        val test = geminiResponse2.lekarskiTestR

        val lekarskiTest = LekarskiTestData(
            idLekarskiTest = test.idLekarskiTest,
            pacijentId = pacijent,
            izvestaj = test.izvestaj
        )

        repo.insertLekarskiTestData(lekarskiTest)
        geminiResponseRetrofit.lekarskiTestRetrofit = lekarskiTest
    }

    override fun insertGeminiLokacijeIstrage(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, zl: ZlocinData, repo: RepositoryInsert) {
        val lokacijeIstrage = geminiResponse2.lokacijeIstrageR
        var lokacijeLista= mutableListOf<LokacijeIstrageData>()

        for(l in lokacijeIstrage){
            var lok= LokacijeIstrageData(
                idLokacijeIstrage = l.idLokacijeIstrage,
                mesto = l.mesto,
                naziv = l.naziv,
                opis = l.naziv,
                zlocinId = zl.idZlocin,
                geoTackaALatitude = l.geoTackaALatitude,
                geoTackaALongitude = l.geoTackaALongitude
            )
            repo.insertLokacijeIstrageData(
                lokacijeIstrage = lok
            )
            lokacijeLista.add(lok)
        }

        geminiResponseRetrofit.lokacijeIstrageRetrofit=lokacijeLista
    }

    override fun insertGeminiZadatakPacijent(geminiResponse2: GeminiResponseCommon2, zlocin: ZlocinData,repo: RepositoryInsert): MutableList<ZadatakData> {
        val zadaci = geminiResponse2.zadatakR
        var zadaciLista = mutableListOf<ZadatakData>()

        for(z in zadaci){
            val prev = z.idZadatak
            val zad = ZadatakData(
                idZadatak = z.idZadatak,
                tekst = z.tekst,
                korak = z.korak,
                uradjen = z.uradjen,
                nextZadatak = z.nextZadatak,
                zlocinId = zlocin.idZlocin
            )

            repo.insertZadatakData(zad, zlocin)


            val dokazZadatak = geminiResponse2.dokazZadatakR.find { it.zadatakId == prev }
            dokazZadatak?.zadatakId = zad.idZadatak

            val telefonZadatak = geminiResponse2.telefonZadatakR.find { it.zadatakId == prev }
            telefonZadatak?.zadatakId = zad.idZadatak

            val forenzickiDokazZadatak = geminiResponse2.forenzickiDokazZadatakR.find { it.zadatakId == prev }
            forenzickiDokazZadatak?.zadatakId = zad.idZadatak

            zadaciLista.add(zad)
        }

        return zadaciLista
    }

    override fun updateGeminiZadatakListPacijent(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,zlocin: ZlocinData,repo:RepositoryInsert) {
        val zadaci = geminiResponse2.zadatakR
        var lista: List<ZadatakData> = emptyList()
        lista = repo.getZadatakListaData()
        repo.updateZadatakListData(lista,zlocin)
        //geminiResponseRetrofit.zadaciRetrofit =lista
    }
}