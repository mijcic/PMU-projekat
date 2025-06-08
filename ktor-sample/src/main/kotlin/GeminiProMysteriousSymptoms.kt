package com.example

import com.example.data.remote.*
import com.example.models.dto.*
import com.example.models.dto.gemini.*
import com.example.models.interfaces.*
import com.example.repository.RepositoryInsert
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Serializable
data class GeminiRequest2MysteriousSymptoms(
    val prompt: String,
    val tables: TablesMysteriousSymptoms
)

fun insertGeminiPacijent(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms,zl: ZlocinData,repo: RepositoryInsert): PacijentData? {
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

fun insertGeminiMedicinskiIzvestaj(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms,pacijent: PacijentData,repo: RepositoryInsert) {
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

fun insertGeminiIzjavaZaPacijenta(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData, zl: ZlocinData,repo: RepositoryInsert) {
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

fun insertGeminiLekarskiTest(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData,repo: RepositoryInsert) {
    val test = geminiResponse2.lekarskiTestR

    val lekarskiTest = LekarskiTestData(
        idLekarskiTest = test.idLekarskiTest,
        pacijentId = pacijent,
        izvestaj = test.izvestaj
    )

    repo.insertLekarskiTestData(lekarskiTest)
    geminiResponseRetrofit.lekarskiTestRetrofit = lekarskiTest
}

fun insertGeminiLokacijeIstrage(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms,zl: ZlocinData,repo: RepositoryInsert) {
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

suspend fun queryGeminiMysteriousSymptoms(prompt: String,tables:String): Any {
    val request = GeminiRequest(
        contents = listOf(Content(parts = listOf(Part(text = prompt+tables)))),
    )

    try {
        val t0 = System.currentTimeMillis()
        val response: HttpResponse = geminiClient.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$GEMINI_API_KEY") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        val t1 = System.currentTimeMillis()
        println("Gemini API trajanje: ${t1 - t0}ms")

        return if (response.status == HttpStatusCode.OK) {
            val geminiResponse: GeminiResponse = response.body()
            val t2 = System.currentTimeMillis()
            println("Parsiranje trajanje: ${t2 - t1}ms")


            //to insert data into mysql database
            println(geminiResponse)
            val geminiResponseRetrofit = getDataGeminiResponseMysteriousSymptoms(geminiResponse)

            val t3 = System.currentTimeMillis()
            println("Insert trajanje: ${t3 - t2}ms")

            geminiResponseRetrofit
            //geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            //  ?: "No textual response received from Gemini."
        } else {
            val errorBody = response.bodyAsText()
            println("Error from Gemini: ${response.status} - $errorBody")
            "Error during communication with the Gemini API: ${response.status}"
        }

    } catch (e: Exception) {
        println("Exception during the Gemini API call: ${e.message}")
        e.printStackTrace()
        return "Internal error during communication with the AI service."
    }
}


suspend fun queryGeminiMysteriousSymptomsStream(prompt: String, tables: String): Flow<String> {
    val request = GeminiRequest(
        contents = listOf(Content(parts = listOf(Part(text = prompt + tables))))
    )

    return flow {
        val response: HttpResponse = try {
            println("saljem zahtev na Gemini API...")
            geminiClient.post(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:streamGenerateContent?key=$GEMINI_API_KEY"
            ) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        } catch (e: Exception) {
            println("Greška prilikom slanja zahteva Gemini API-ju: ${e.message}")
            throw e // Važno je da ponovo bacite izuzetak da bi se `catch` blok u ruti izvršio
        }

        val channel = response.bodyAsChannel()
        val buffer = ByteArray(1024)

        val textBuilder = StringBuilder()
        println("Uspesno poslat zahtev, pocinjem da citam strim.")
        while (!channel.isClosedForRead) {
            println("Citam podatke sa strima...")
            val bytesRead = channel.readAvailable(buffer)

            if (bytesRead > 0) {
                val chunk = buffer.decodeToString(0, bytesRead)

                // Emit chunk
                emit(chunk)

                // Optional: accumulate full response
                textBuilder.append(chunk)
                println("Primljen chunk: $chunk")
            }
        }

        println("Full streamed response:\n${textBuilder.toString()}")

    }.catch { e ->
        println("Greška tokom stream-a: ${e.message}")
        // Samo propagiraj grešku – ne emituj ništa!
        throw e
    }
}



fun getDataGeminiResponseMysteriousSymptoms(geminiResponse:GeminiResponse): GeminiResponseRetrofitMysteriousSymptoms {

    val json2 = Json {
        ignoreUnknownKeys = true
    }
    val cleanJsonString = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace("`", "")
    val cleanJsonString2 = cleanJsonString?.removePrefix("json")
    val geminiResponse2: GeminiResponse2MysteriousSymptoms? =
        cleanJsonString2?.let {
            json2.decodeFromString(
                it
            )
        }

    val geminiResponseRetrofit:GeminiResponseRetrofitMysteriousSymptoms= GeminiResponseRetrofitMysteriousSymptoms(
        zlocinRetrofit = null,
        dokaziRetrofit = null,
        telefoniRetrofit = null,
        forenzickiDokazRetrofit = null,
        oneContactRetrofit = null,
        aplikacijeRetrofit = null,
        beleskeRetrofit = null,
        whatsappKontaktRetrofit = null,
        whatsappPorukaRetrofit = null,
        oneCallRetrofit = null,
        galleryRetrofit = null,
        obicnePorukeRetrofit = null,
        pitanjaRetrofit = null,
        odgovoriRetrofit = null,
        osobeRetrofit = null,
        zadaciRetrofit = null,
        dokaziZadaciRetrofit = null,
        telefonZadaciRetrofit = null,
        forenzickiDokazZadaciRetrofit = null,
        pacijentRetrofit = null,
        medicinskiIzvestajRetrofit = null,
        lekarskiTestRetrofit = null,
        lokacijeIstrageRetrofit = null,
        izjavaZaPacijentaRetrofit = null
    )

    val conn = getDatabaseConnection()
    if(conn==null){
        return geminiResponseRetrofit
    }
    val repo = RepositoryInsert(conn)

    if (geminiResponse2 != null) {
        val datumString = geminiResponse2.zlocinR.datum
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val datum = datumString?.let { LocalDate.parse(it, formatter) }
        val timestamp = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        geminiResponse2.zlocinR.datum?.let {
            if (timestamp != null) {

                val zl = ZlocinData(
                    idZlocin = geminiResponse2.zlocinR.idZlocin,
                    tipZlocinaId = 1,
                    naziv = geminiResponse2.zlocinR.naziv,
                    datum = timestamp,
                    mesto = geminiResponse2.zlocinR.mesto,
                    opis = geminiResponse2.zlocinR.opis,
                    status = geminiResponse2.zlocinR.status,
                )
                repo.insertZlocinData(zl)
                geminiResponseRetrofit.zlocinRetrofit=zl

                val datumStr = geminiResponse2.pacijentR.zrtvaId?.osobaId?.datum
                val datumLong = datumStr?.let {
                    LocalDate.parse(it)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                } ?: timestamp
                val osoba = OsobaData(
                    idOsoba = geminiResponse2.pacijentR.zrtvaId?.osobaId?.idOsoba ?: -1,
                    ime = geminiResponse2.pacijentR.zrtvaId?.osobaId?.ime ?: "Nepoznato",
                    kontakt = geminiResponse2.pacijentR.zrtvaId?.osobaId?.kontakt ?: "Nepoznato",
                    datum = datumLong ?: timestamp,
                    zanimanje = geminiResponse2.pacijentR.zrtvaId?.osobaId?.zanimanje ?: "Nepoznato",
                    pol = geminiResponse2.pacijentR.zrtvaId?.osobaId?.pol ?: "M",
                    zlocinId = geminiResponse2.pacijentR.zrtvaId?.osobaId?.zlocinId ?: -1
                )
                repo.insertOsobaData(osoba, zl)

                val zrtva = ZrtvaData(
                    idZrtva = geminiResponse2.pacijentR.zrtvaId?.idZrtva ?: -1,
                    tipZrtve = geminiResponse2.pacijentR.zrtvaId?.tipZrtve ?: "osoba",
                    detalji = geminiResponse2.pacijentR.zrtvaId?.detalji ?: "",
                    statusZrtva = geminiResponse2.pacijentR.zrtvaId?.statusZrtva ?: "ziva",
                    zlocinId = geminiResponse2.pacijentR.zrtvaId?.zlocinId ?: -1,
                    osobaId = osoba
                )
                repo.insertZlocinData(zl)
                geminiResponseRetrofit.zlocinRetrofit=zl

                var pacijent=insertGeminiPacijent(geminiResponse2,geminiResponseRetrofit,zl,repo)

                var dokaziLista: MutableList<DokazData> = mutableListOf()
                var telefoniLista: MutableList<TelefonData> = mutableListOf()
                var forenzickiDokaziLista: MutableList<ForenzickiDokazData> = mutableListOf()

                if (pacijent != null) {
                    insertGeminiMedicinskiIzvestaj(geminiResponse2,geminiResponseRetrofit,pacijent, repo)
                    insertGeminiIzjavaZaPacijenta(geminiResponse2, geminiResponseRetrofit, pacijent,zl, repo)
                    insertGeminiLekarskiTest(geminiResponse2,geminiResponseRetrofit,pacijent, repo)

                    // dokazi
                    dokaziLista = insertGeminiDokaz(geminiResponse2,geminiResponseRetrofit,zl,pacijent.zrtvaId, repo)

                    //forenzicki dokazi
                    forenzickiDokaziLista = insertGeminiForenzickiDokaz(geminiResponse2,geminiResponseRetrofit,pacijent.zrtvaId, repo)

                    //telefon
                    telefoniLista = insertGeminiTelefon(geminiResponse2,geminiResponseRetrofit,pacijent.zrtvaId, repo)

                    //aplikacija
                    insertGeminiAplikacija(geminiResponse2,geminiResponseRetrofit,pacijent.zrtvaId, repo)
                }

                insertGeminiLokacijeIstrage(geminiResponse2,geminiResponseRetrofit,zl, repo)

                //osoba
                insertGeminiOsoba(geminiResponse2, geminiResponseRetrofit, zl, timestamp, repo)

                // beleske
                insertGeminiBeleska(geminiResponse2, geminiResponseRetrofit, zl, timestamp, repo)

                // whatsAppKontakt
                val whatsAppKontaktiLista = insertGeminiWhatsAppKontakt(geminiResponse2, geminiResponseRetrofit,zl, repo)

                //  whatsAppPoruka
                insertGeminiWhatsAppPoruka(geminiResponse2, geminiResponseRetrofit, whatsAppKontaktiLista, timestamp, repo)

                // kontakti - One Contact
                val kontaktiLista = insertGeminiOneContact(geminiResponse2, geminiResponseRetrofit, zl, repo)

                // one call
                insertGeminiOneCall(geminiResponse2, geminiResponseRetrofit, zrtva, kontaktiLista, timestamp, repo)

                // gallery
                insertGeminiGallery(geminiResponse2, geminiResponseRetrofit,zl, timestamp, repo)

                //  obicnaPoruka
                insertGeminiObicnaPoruka(geminiResponse2,geminiResponseRetrofit, kontaktiLista, timestamp, repo)

                // pitanje
                val pitanjaLista = insertGeminiPitanje(geminiResponse2, geminiResponseRetrofit,zl, repo)

                //odgovor
                insertGeminiOdgovor(geminiResponse2, geminiResponseRetrofit,pitanjaLista, repo)

                // zadatak

                val zadaciLista = insertGeminiZadatakPacijent(geminiResponse2, zl, repo)
                updateGeminiZadatakListPacijent(geminiResponse2,geminiResponseRetrofit, zl, repo)
                geminiResponseRetrofit.zadaciRetrofit = zadaciLista

                // dokazZadatak
                insertGeminiDokazZadatak(geminiResponse2, geminiResponseRetrofit,dokaziLista, zadaciLista, repo)

                // telefonZadatak
                insertGeminiTelefonZadatak(geminiResponse2, geminiResponseRetrofit,telefoniLista, zadaciLista, repo)

                // forenzickiDokazZadatak
                insertGeminiForenzickiDokazZadatak(geminiResponse2, geminiResponseRetrofit,forenzickiDokaziLista, zadaciLista, repo)

            }
        }
    }
    return geminiResponseRetrofit
}



fun insertGeminiZadatakPacijent(geminiResponse2: GeminiResponseCommon2, zlocin: ZlocinData,repo: RepositoryInsert): MutableList<ZadatakData> {
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

fun updateGeminiZadatakListPacijent(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,zlocin: ZlocinData,repo:RepositoryInsert) {
    val zadaci = geminiResponse2.zadatakR
    var lista: List<ZadatakData> = emptyList()
    lista = repo.getZadatakListaData()
    repo.updateZadatakListData(lista,zlocin)
    //geminiResponseRetrofit.zadaciRetrofit =lista
}