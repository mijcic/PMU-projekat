package com.example

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Serializable
data class GeminiRequest2MysteriousSymptoms(
    val prompt: String,
    val tables: TablesMysteriousSymptoms
)


@Serializable
data class TablesMysteriousSymptoms(
    val zlocinR: ZlocinR,
    val osobaR: List<OsobaR>,
    val dokazR: List<DokazR>,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    val aplikacijaKtor: List<AplikacijaKtor>,
    val oneContactR: List<OneContactR>,
    val beleskaR: List<BeleskaR>,
    val whatsAppKontaktR: List<WhatsAppKontaktR>,
    val whatsAppPorukaR: List<WhatsAppPorukaR>,
    val oneCallR: List<OneCallR>,
    val galleryR: List<GalleryR>,
    val obicnaPorukaR: List<ObicnaPorukaR>,
    val pitanjeR: List<PitanjeR>,
    val odgovorR: List<OdgovorR>,
    val zadatakR: List<ZadatakR>,
    val dokazZadatakR: List<DokazZadatakR>,
    val telefonZadatakR: List<TelefonZadatakR>,
    val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,
    val pacijentR: PacijentR,
    val medicinskiIzvestajR: MedicinskiIzvestajR,
    val lekarskiTestR: LekarskiTestR,
    val lokacijeIstrageR: List<LokacijeIstrageR>,
    val izjavaZaPacijentaR: IzjavaZaPacijentaR
)

@Serializable
data class PacijentR (var idPacijent: Int, val simptomi: String, val statusPacijenta: String, val datumPrijave: String, var prijavio:OsobaR?, var zlocinId: Int, var zrtvaId: ZrtvaR?)

@Serializable
data class MedicinskiIzvestajR (var idMedicinskiIzvestaj: Int, val rezime: String, val CTnalaz: String, val MRInalaz: String, val krvnaSlika: String, val toksikoloskeAnalize: String, val zakljucak: String, var pacijentId: Int)

@Serializable
data class LekarskiTestR (var idLekarskiTest: Int, var pacijentId: Int, val izvestaj: String)

@Serializable
data class LokacijeIstrageR (var idLokacijeIstrage: Int, val mesto: String, val naziv: String, val opis: String,val zlocinId:Int,var geoTackaALatitude:Double, var geoTackaALongitude:Double)

@Serializable
open class IzjavaZaPacijentaR (var idIzjavaZaPacijenta: Int, var izjava: String, var pacijentId: Int, var osobaId: Int)

@Serializable
data class GeminiResponse2MysteriousSymptoms(
    val zlocinR: ZlocinR,
    override val osobaR: List<OsobaR>,
    override val dokazR: List<DokazR>,
    override val forenzickiDokazR: List<ForenzickiDokazR>,
    override val telefonR: List<TelefonR>,
    override val aplikacijaKtor: List<AplikacijaKtor>,

    override val oneContactR: List<OneContactR>,
    override val beleskaR: List<BeleskaR>,
    override val whatsAppKontaktR: List<WhatsAppKontaktR>,
    override val whatsAppPorukaR: List<WhatsAppPorukaR>,
    override val oneCallR: List<OneCallR>,
    override val galleryR: List<GalleryR>,
    override val obicnaPorukaR: List<ObicnaPorukaR>,
    override val pitanjeR: List<PitanjeR>,
    override val odgovorR: List<OdgovorR>,
    override val zadatakR: List<ZadatakR>,

    override val dokazZadatakR: List<DokazZadatakR>,
    override val telefonZadatakR: List<TelefonZadatakR>,
    override val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,

    val pacijentR: PacijentR,
    val medicinskiIzvestajR: MedicinskiIzvestajR,
    val lekarskiTestR: LekarskiTestR,
    val lokacijeIstrageR: List<LokacijeIstrageR>,
    val izjavaZaPacijentaR: IzjavaZaPacijentaR
) : GeminiResponseCommon2


@Serializable
data class GeminiResponseRetrofitMysteriousSymptoms(
    var zlocinRetrofit: ZlocinData?,
    override var dokaziRetrofit: List<DokazData>?,
    override var telefoniRetrofit: List<TelefonData>?,
    override var forenzickiDokazRetrofit: List<ForenzickiDokazData>?,
    override var oneContactRetrofit: List<OneContactData>?,
    override var aplikacijeRetrofit: List<AplikacijaData>?,
    override var beleskeRetrofit: List<BeleskaData>?,
    override var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?,
    override var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?,
    override var oneCallRetrofit: List<OneCallData>?,
    override var galleryRetrofit: List<GalleryData>?,
    override var obicnePorukeRetrofit: List<ObicnaPorukaData>?,
    override var pitanjaRetrofit: List<PitanjeData>?,
    override var odgovoriRetrofit: List<OdgovorData>?,
    override var osobeRetrofit: List<OsobaData>?,
    override var zadaciRetrofit: List<ZadatakData>?,
    override var dokaziZadaciRetrofit: List<DokazZadatakData>?,
    override var telefonZadaciRetrofit: List<TelefonZadatakData>?,
    override var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?,

    var pacijentRetrofit: PacijentData?,
    var medicinskiIzvestajRetrofit: MedicinskiIzvestajData?,
    var lekarskiTestRetrofit: LekarskiTestData?,
    var lokacijeIstrageRetrofit: List<LokacijeIstrageData>?,
    var izjavaZaPacijentaRetrofit: IzjavaZaPacijentaData?
):GeminiResponseRetrofitCommon

fun insertGeminiPacijent(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms,zl:ZlocinData): PacijentData? {
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

    var prijavio:OsobaData? = null
    pacijent.prijavio?.let {
        it.kontakt?.let { it1 ->
            if (timestampPrijavio != null) {
                prijavio=OsobaData(
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

    var osoba:OsobaData? =null
    pacijent.zrtvaId?.osobaId?.let {
        if (timestampOsoba != null) {
            it.kontakt?.let { it1 ->
                osoba=OsobaData(
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

    var zrtva:ZrtvaData? = null
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

    prijavio?.let { insertOsobaData(it, zl) }
    osoba?.let { insertOsobaData(it, zl) }
    zrtva?.let { osoba?.let { it1 -> insertZrtva(zrtva!!,zl, it1) } }

    var pac:PacijentData?= null
    prijavio?.let {
        if (timestamp != null) {
            zrtva?.let { it1 ->
                pac=PacijentData(
                    idPacijent = pacijent.idPacijent,
                    simptomi = pacijent.simptomi,
                    statusPacijenta = pacijent.statusPacijenta,
                    datumPrijave = timestamp,
                    prijavio = it,
                    zlocinId = zl,
                    zrtvaId = it1
                )

                insertPacijentData(pac!!)
                geminiResponseRetrofit.pacijentRetrofit=pac
            }
        }
    }
    return pac
}

fun insertGeminiMedicinskiIzvestaj(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms,pacijent:PacijentData) {
    val medicinskiIzvestaj = geminiResponse2.medicinskiIzvestajR

    var medIzv=MedicinskiIzvestajData(
        idMedicinskiIzvestaj = medicinskiIzvestaj.idMedicinskiIzvestaj,
        rezime = medicinskiIzvestaj.rezime,
        CTnalaz = medicinskiIzvestaj.CTnalaz,
        MRInalaz = medicinskiIzvestaj.MRInalaz,
        krvnaSlika = medicinskiIzvestaj.krvnaSlika,
        toksikoloskeAnalize = medicinskiIzvestaj.toksikoloskeAnalize,
        zakljucak = medicinskiIzvestaj.zakljucak,
        pacijentId = pacijent
    )

    insertMedicinskiIzvestajData(medicinskiIzvestaj = medIzv)
    geminiResponseRetrofit.medicinskiIzvestajRetrofit=medIzv
}

fun insertGeminiIzjavaZaPacijenta(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData,zl: ZlocinData) {
    val izjave = geminiResponse2.izjavaZaPacijentaR

    val datumString = "2025-11-12"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val datum = datumString?.let { LocalDate.parse(it, formatter) }
    val timestamp = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()


    var osoba:OsobaData? =null
    /*
    izjave.osobaId?.kontakt?.let {
        if (timestamp != null) {
            osoba= izjave.osobaId?.let { it1 ->
                OsobaData(
                    idOsoba = it1.idOsoba,
                    ime = izjave.osobaId!!.ime,
                    kontakt = it,
                    datum = timestamp,
                    zanimanje = izjave.osobaId!!.zanimanje,
                    pol = izjave.osobaId!!.pol,
                    zlocinId = zl.idZlocin
                )
            }
        }
    }*/
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
        insertOsobaData(osoba,zl)
    }

    if (osoba != null) {
        val izjava = IzjavaZaPacijentaData(
            idIzjavaZaPacijenta = izjave.idIzjavaZaPacijenta,
            izjava = izjave.izjava,
            pacijentId = pacijent,
            osobaId = osoba!!
        )

        insertIzjavaZaPacijentaData(izjava, pacijent, osoba!!)
        geminiResponseRetrofit.izjavaZaPacijentaRetrofit = izjava
    }

}

fun insertGeminiLekarskiTest(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData) {
    val test = geminiResponse2.lekarskiTestR

    val lekarskiTest = LekarskiTestData(
        idLekarskiTest = test.idLekarskiTest,
        pacijentId = pacijent,
        izvestaj = test.izvestaj
    )

    insertLekarskiTestData(lekarskiTest)

    geminiResponseRetrofit.lekarskiTestRetrofit = lekarskiTest
}

fun insertGeminiLokacijeIstrage(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms,zl:ZlocinData) {
    val lokacijeIstrage = geminiResponse2.lokacijeIstrageR
    var lokacijeLista= mutableListOf<LokacijeIstrageData>()

    for(l in lokacijeIstrage){
        var lok=LokacijeIstrageData(
            idLokacijeIstrage = l.idLokacijeIstrage,
            mesto = l.mesto,
            naziv = l.naziv,
            opis = l.naziv,
            zlocinId = zl.idZlocin,
            geoTackaALatitude = l.geoTackaALatitude,
            geoTackaALongitude = l.geoTackaALongitude
        )
        insertLokacijeIstrageData(
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

    if (geminiResponse2 != null) {
        val datumString = geminiResponse2.zlocinR.datum
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val datum = datumString?.let { LocalDate.parse(it, formatter) }
        val timestamp = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        geminiResponse2.zlocinR.datum?.let {
            if (timestamp != null) {

                val zl =ZlocinData(
                    idZlocin = geminiResponse2.zlocinR.idZlocin,
                    tipZlocinaId = 1,
                    naziv = geminiResponse2.zlocinR.naziv,
                    datum = timestamp,
                    mesto = geminiResponse2.zlocinR.mesto,
                    opis = geminiResponse2.zlocinR.opis,
                    status = geminiResponse2.zlocinR.status,
                )
                insertZlocinData(zl)
                geminiResponseRetrofit.zlocinRetrofit=zl

                var pacijent=insertGeminiPacijent(geminiResponse2,geminiResponseRetrofit,zl)

                var dokaziLista: MutableList<DokazData> = mutableListOf()
                var telefoniLista: MutableList<TelefonData> = mutableListOf()
                var forenzickiDokaziLista: MutableList<ForenzickiDokazData> = mutableListOf()

                if (pacijent != null) {
                    insertGeminiMedicinskiIzvestaj(geminiResponse2,geminiResponseRetrofit,pacijent)
                    insertGeminiIzjavaZaPacijenta(geminiResponse2, geminiResponseRetrofit, pacijent,zl)
                    insertGeminiLekarskiTest(geminiResponse2,geminiResponseRetrofit,pacijent)

                    // dokazi
                    dokaziLista = insertGeminiDokaz(geminiResponse2,geminiResponseRetrofit,zl,pacijent.zrtvaId)

                    //forenzicki dokazi
                    forenzickiDokaziLista = insertGeminiForenzickiDokaz(geminiResponse2,geminiResponseRetrofit,pacijent.zrtvaId)

                    //telefon
                    telefoniLista = insertGeminiTelefon(geminiResponse2,geminiResponseRetrofit,pacijent.zrtvaId)

                    //aplikacija
                    insertGeminiAplikacija(geminiResponse2,geminiResponseRetrofit,pacijent.zrtvaId)
                }

                insertGeminiLokacijeIstrage(geminiResponse2,geminiResponseRetrofit,zl)

                //osoba
                insertGeminiOsoba(geminiResponse2, geminiResponseRetrofit, zl, timestamp)

                // beleske
                insertGeminiBeleska(geminiResponse2, geminiResponseRetrofit, zl, timestamp)

                // whatsAppKontakt
                val whatsAppKontaktiLista = insertGeminiWhatsAppKontakt(geminiResponse2, geminiResponseRetrofit,zl)

                //  whatsAppPoruka
                insertGeminiWhatsAppPoruka(geminiResponse2, geminiResponseRetrofit, whatsAppKontaktiLista, timestamp)

                // kontakti - One Contact
                val kontaktiLista = insertGeminiOneContact(geminiResponse2, geminiResponseRetrofit, zl)

                // one call
                insertGeminiOneCall(geminiResponse2, geminiResponseRetrofit,kontaktiLista, timestamp)

                // gallery
                insertGeminiGallery(geminiResponse2, geminiResponseRetrofit,zl, timestamp)

                //  obicnaPoruka
                insertGeminiObicnaPoruka(geminiResponse2,geminiResponseRetrofit, kontaktiLista, timestamp)

                // pitanje
                val pitanjaLista = insertGeminiPitanje(geminiResponse2, geminiResponseRetrofit,zl)

                //odgovor
                insertGeminiOdgovor(geminiResponse2, geminiResponseRetrofit,pitanjaLista)

                // zadatak

                val zadaciLista = insertGeminiZadatakPacijent(geminiResponse2, zl)
                updateGeminiZadatakListPacijent(geminiResponse2,geminiResponseRetrofit, zl)
                geminiResponseRetrofit.zadaciRetrofit = zadaciLista

                // dokazZadatak
                insertGeminiDokazZadatak(geminiResponse2, geminiResponseRetrofit,dokaziLista, zadaciLista)

                // telefonZadatak
                insertGeminiTelefonZadatak(geminiResponse2, geminiResponseRetrofit,telefoniLista, zadaciLista)

                // forenzickiDokazZadatak
                insertGeminiForenzickiDokazZadatak(geminiResponse2, geminiResponseRetrofit,forenzickiDokaziLista, zadaciLista)

            }
        }
    }
    return geminiResponseRetrofit
}



fun insertGeminiZadatakPacijent(geminiResponse2: GeminiResponseCommon2, zlocin: ZlocinData): MutableList<ZadatakData> {
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

        insertZadatakData(zad, zlocin)


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

fun updateGeminiZadatakListPacijent(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,zlocin: ZlocinData) {
    val zadaci = geminiResponse2.zadatakR
    var lista: List<ZadatakData> = emptyList()
    lista = getZadatakListaData()
    updateZadatakListData(lista,zlocin)
    //geminiResponseRetrofit.zadaciRetrofit =lista
}
