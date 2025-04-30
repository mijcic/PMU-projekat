package com.example

import io.ktor.client.*
import io.ktor.client.call.*
 import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@Serializable
data class GeminiRequest2(
    val prompt: String,
    val tables: Tables
)

@Serializable
data class Tables(
    val zlocinR: ZlocinR,
   // val osobaR: List<OsobaR>,
    //val motivR: List<MotivR>,
    val osumnjicenR: List<OsumnjicenR>,
    val dokazR: List<DokazR>,
    val svedokR: List<SvedokR>,
    val zrtvaR: ZrtvaR,
    val obdukcijaR: ObdukcijaR,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    val dokazOsumnjicenR: List<DokazOsumnjicenR>,
    //val zadatakR: ZadatakR,
    //val alibiR: List<AlibiR>,
    val kontaktKtor: List<KontaktKtor>,
    val porukeKtor: List<PorukeKtor>,
    val poziviKtor: List<PoziviKtor>,
    val galerijaKtor: List<GalerijaKtor>,
    val aplikacijaKtor: List<AplikacijaKtor>,
    val tragKtor: List<TragKtor>,
    val dokazOsumnjicenKtor: List<DokazOsumnjicenKtor>
)


@Serializable
data class ZlocinR(val idZlocin: Int, val tipZlocinaId: Int, val naziv: String, val datum: String?, val mesto: String, val opis: String, val status:String)

@Serializable
data class OsobaR(val idOsoba: Int, val ime: String, val kontakt: String?, val datum: String?, val zanimanje: String,val pol:String,val zlocinId: Int)

@Serializable
data class OsumnjicenR(val idOsumnjicen: Int, val status: Int, val motiv: MotivR?,val tipOsumnjicen:String, val zlocinId:Int,val kriv:Int, val osobaId:OsobaR?)

@Serializable
data class MotivR(val idMotiv:Int, val opis: String)

@Serializable
data class DokazR(val idDokaz: Int, val tipDokaza: String,val opis: String, val zlocinId: Int, val zrtvaId: Int, val status: Int)

//@Serializable
//data class DokazZadatakR(val idDokazZadatak: Int, val tekst: String,val dokazId:DokazR?, val uradjen:Boolean)

@Serializable
data class SvedokR(val idSvedok: Int, val izjava: String, val statusSvedok: String,val statusIspitan:Int,val zlocinId: Int, val osobaId: OsobaR?)

@Serializable
data class ZrtvaR(val idZrtva: Int, val tipZrtve: String, val detalji:String, val statusZrtva:String,val zlocinId: Int, val osobaId: OsobaR?)

@Serializable
data class ObdukcijaR(val idObdukcija:Int, val izvestaj:String, val datum: String, val uzrokSmrti:String, var zrtvaId: Int, val informacije: String )

@Serializable
data class ForenzickiDokazR(val idForenzickiDokaz: Int, val tipForenzickiDokaz: String, val opis: String, val statusS:Int, val veza: String)

@Serializable
data class TelefonR(val idTelefon: Int, val model:String, val os: String, val sifra: String, val informacije: String)

@Serializable
data class DokazOsumnjicenR(var idDokazOsumnjicen: Int, var dokazId: Int, var osumnjicenId: Int)

@Serializable
data class ZadatakR(var idZadatak: Int, var tekst: String, var korak: String, var uradjen: Boolean, var next: ZadatakR?, var zlocinId: Int)

@Serializable
data class AlibiR (var idAlibi: Int, var osumnjicenId: Int, var svedokId: Int?, var opis: String, var statusAlibija: String)

@Serializable
data class KontaktKtor(var idKontakt:Int, val ime: String, val broj: String, val status: Int, var zrtvaId: Int)

@Serializable
data class PorukeKtor(var idPoruke: Int, val tipPoruke: String, val sadrzaj: String, val datumVreme: String, var zrtvaId: Int, var posiljalacId: Int, val statusPoruke: String, val sirovana: Boolean)

@Serializable
data class PoziviKtor (var idPoziv: Int, val tip: Int, val broj: String, val datumVreme: String, val zrtvaId: Int, val status: Int, var kontaktId: Int)

@Serializable
data class GalerijaKtor (var idGalerija: Int, val tip: Int, val putanja: String, var zrtvaId: Int, val datumVreme: String, val lokacija: String)

@Serializable
data class AplikacijaKtor (var idAplikacije: Int, val naziv: String, val tip: Int, val zrtvaId: Int, val aktivna: Boolean, val informacije: String)

@Serializable
data class TragKtor(var idTrag: Int, var forenzickiDokazId: Int, var osumnjicenId: Int)

@Serializable
data class DokazOsumnjicenKtor(var idDokazOsumnjicen: Int, var dokazId: Int, var osumnjicenId: Int)

// --- Konstante ---
// UPOZORENJE: NIKADA NE STAVLJAJ API KLJUČ DIREKTNO U KOD U PRODUKCIJI!
// Koristi environment variable ili configuration fajl.
const val GEMINI_API_KEY = "AIzaSyD0Fssx_oFXYrO4dSoRuSfxhGpn4ziWPQk"

// --- Data Klase za Gemini Zahtev ---
@Serializable
data class GeminiRequest(
    val contents: List<Content>
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String
)

// --- Data Klase za Gemini Odgovor ---
@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    val content: Content? = null,
    val finishReason: String? = null,
    val index: Int? = null
)

@Serializable
data class GeminiResponse2(
    val zlocinR: ZlocinR,
    //val osobaR:  List<OsobaR>,
    //val motivR: List<MotivR>,
    val osumnjicenR:  List<OsumnjicenR>,
    val dokazR:  List<DokazR>,
    val svedokR:  List<SvedokR>,
    val zrtvaR:  ZrtvaR,
    val obdukcijaR: ObdukcijaR,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    val dokazOsumnjicenR: List<DokazOsumnjicenR>,
    //val zadatakR: ZadatakR,
    //val alibiR: List<AlibiR>,
    val kontaktKtor: List<KontaktKtor>,
    val porukeKtor: List<PorukeKtor>,
    val poziviKtor: List<PoziviKtor>,
    val galerijaKtor: List<GalerijaKtor>,
    val aplikacijaKtor: List<AplikacijaKtor>,
    val tragKtor: List<TragKtor>,
    val dokazOsumnjicenKtor: List<DokazOsumnjicenKtor>
)

@Serializable
data class GeminiResponseRetrofit(
    var zlocinRetrofit: ZlocinData?,
    var zrtvaRetrofit: ZrtvaData?,
    var osumnjiceniRetrofit: List<OsumnjicenData>?
)

// http klijent za komunikaciju sa gemini api-jem
val geminiClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    engine {
        requestTimeout = 60_000 // 60 sekundi timeout za API poziv
    }
}

suspend fun queryGemini(prompt: String,tables:String): String {
    val request = GeminiRequest(
        contents = listOf(Content(parts = listOf(Part(text = prompt+tables)))),
    )

    try {
        // Model: gemini-1.5-flash je brz i efikasan. Možeš koristiti i gemini-pro.
        val response: HttpResponse = geminiClient.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$GEMINI_API_KEY") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        println("Gemini - Status: ${response.status}")

        return if (response.status == HttpStatusCode.OK) {
            val geminiResponse: GeminiResponse = response.body()
            println("${geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text}")

            //to insert data into mysql database
            getDataGeminiResponse(geminiResponse)

            geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "No textual response received from Gemini."
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

fun getDataGeminiResponse(geminiResponse:GeminiResponse){
    val json2 =Json {
        ignoreUnknownKeys = true
    }
    val cleanJsonString = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace("`", "")
    val cleanJsonString2 = cleanJsonString?.removePrefix("json")
    val geminiResponse2: GeminiResponse2? =
        cleanJsonString2?.let {
            json2.decodeFromString(
                it
            )
        }
    println(geminiResponse2)

    val geminiResponseRetrofit:GeminiResponseRetrofit=GeminiResponseRetrofit(
        zlocinRetrofit = null,
        zrtvaRetrofit = null,
        osumnjiceniRetrofit = null
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

                /*
                val osobe = geminiResponse2.osobaR

                for(o in osobe){
                    println(o)
                    val datumStr = o.datum
                    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
                    var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

                    if(timestamp2==null){
                        timestamp2=timestamp
                    }
                    val os= timestamp2?.let {
                        OsobaData(
                            idOsoba = o.idOsoba,
                            ime = o.ime,
                            kontakt = o.kontakt,
                            datum = it,
                            zanimanje = o.zanimanje,
                            pol = o.pol,
                            zlocinId = o.zlocinId
                        )
                    }
                    if (os != null) {
                        insertOsobaData(os,zl)
                    }
                }
                */

                //zrtva i osoba
                insertGeminiZrtva(geminiResponse2,geminiResponseRetrofit,timestamp,zl)

                //svedoci
                insertGeminiSvedok(geminiResponse2,timestamp,zl)

                println("\nNA KRAJU\n"+geminiResponseRetrofit.toString())

            }
        }
    }
}

fun insertGeminiZrtva(geminiResponse2: GeminiResponse2,geminiResponseRetrofit: GeminiResponseRetrofit,timestamp:Long,zl:ZlocinData){
    val zrtva = geminiResponse2.zrtvaR
    val datumStr = zrtva.osobaId?.datum
    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
    var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

    if(timestamp2==null){
        timestamp2=timestamp
    }
    val os= timestamp2?.let {
        zrtva.osobaId?.let { it1 ->
            OsobaData(
                idOsoba = it1.idOsoba,
                ime = zrtva.osobaId.ime,
                kontakt = if(zrtva.osobaId.kontakt==null)"" else zrtva.osobaId.kontakt,
                datum = it,
                zanimanje = zrtva.osobaId.zanimanje,
                pol = zrtva.osobaId.pol,
                zlocinId = zrtva.osobaId.zlocinId
            )
        }
    }
    if (os != null) {
        insertOsobaData(os,zl)
    }
    if (os != null) {
        val zr=ZrtvaData(
            idZrtva = zrtva.idZrtva,
            tipZrtve = zrtva.tipZrtve,
            detalji = zrtva.detalji,
            statusZrtva = zrtva.statusZrtva,
            zlocinId = zl.idZlocin,
            osobaId = 1
        )

        // zrtva
        insertZrtva(zr, zl,os)

        geminiResponseRetrofit.zrtvaRetrofit=zr

        // dokazi
        val dokaziLista=insertGeminiDokaz(geminiResponse2,zl,zr)

        //obdukcija
        insertGeminiObdukcija(geminiResponse2,zl,zr,timestamp)

        //forenzicki dokazi
        val forenzickiDokaziLista=insertGeminiForenzickiDokaz(geminiResponse2,zr)

        //telefon
        insertGeminiTelefon(geminiResponse2,zr)

        //kontakt
        val kontaktLista=insertGeminiKontakt(geminiResponse2,zr)

        //poruke
        insertGeminiPoruke(geminiResponse2,zr,kontaktLista,timestamp)

        //pozivi
        insertGeminiPozivi(geminiResponse2,zr,kontaktLista,timestamp)

        //galerija
        insertGeminiGalerija(geminiResponse2,zr,timestamp)

        //aplikacija
        insertGeminiAplikacija(geminiResponse2,zr)

        //osumnjiceni i osobe (izmeniti)
        var osumnjicenLista=insertGeminiOsumnjicen(geminiResponse2,timestamp,zl)
        geminiResponseRetrofit.osumnjiceniRetrofit=osumnjicenLista

        insertGeminiTrag(geminiResponse2,forenzickiDokaziLista,osumnjicenLista)

        insertGeminiDokazOsumnjicen(geminiResponse2,dokaziLista,osumnjicenLista)

    }
}

fun insertGeminiDokaz(geminiResponse2: GeminiResponse2,zl:ZlocinData,zrtva:ZrtvaData): MutableList<DokazData> {
    val dokazi = geminiResponse2.dokazR
    val dokaziLista= mutableListOf<DokazData>()
    for(d in dokazi){
        val prev=d.idDokaz
        val dokaz = DokazData(
            idDokaz = d.idDokaz,
            tipDokaza = d.tipDokaza,
            opis = d.opis,
            zlocinId = d.zlocinId,
            zrtvaId = d.zrtvaId,
            status = d.status
        )
        insertDokazData(dokaz,zl,zrtva)

        val pronadjenDokaz = geminiResponse2.dokazOsumnjicenKtor.find { it.dokazId==prev }
        pronadjenDokaz?.dokazId=dokaz.idDokaz
        dokaziLista.add(dokaz)
    }
    return dokaziLista
}

fun insertGeminiTelefon(geminiResponse2: GeminiResponse2,zrtva:ZrtvaData){
    val telefoni = geminiResponse2.telefonR

    for(telefonR in telefoni){
        val telefon = TelefonData(
            idTelefon = telefonR.idTelefon,
            model = telefonR.model,
            os = telefonR.os,
            sifra = telefonR.sifra,
            informacije = telefonR.informacije
        )

        insertTelefonData(
            telefon = telefon,
            zrtva = zrtva
        )
    }
}

fun insertGeminiForenzickiDokaz(geminiResponse2: GeminiResponse2,zrtva:ZrtvaData): MutableList<ForenzickiDokazData> {
    val dokazi = geminiResponse2.forenzickiDokazR
    var dokaziLista = mutableListOf<ForenzickiDokazData>()
    for(d in dokazi){
        val prev=d.idForenzickiDokaz
        val forenzickiDokaz = ForenzickiDokazData(
            idForenzickiDokaz = d.idForenzickiDokaz,
            tipForenzickiDokaz = d.tipForenzickiDokaz,
            opis = d.opis,
            statusS = d.statusS,
            veza = d.veza
        )
        insertForenzickiDokaz(
            forenzickiDokaz = forenzickiDokaz,
            zrtva = zrtva
        )

        val pronadjenDokaz = geminiResponse2.tragKtor.find { it.forenzickiDokazId==prev }
        pronadjenDokaz?.forenzickiDokazId=forenzickiDokaz.idForenzickiDokaz
        dokaziLista.add(forenzickiDokaz)
    }
    return dokaziLista
}

fun insertGeminiObdukcija(geminiResponse2: GeminiResponse2,zl: ZlocinData,zrtva: ZrtvaData,timestamp: Long){
    val obdukcija =geminiResponse2.obdukcijaR
    val datumStr = obdukcija.datum
    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
    var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

    if(timestamp2==null){
        timestamp2=timestamp
    }

    insertObdukcijaData(
        ObdukcijaData(
            idObdukcija = obdukcija.idObdukcija,
            izvestaj = obdukcija.izvestaj,
            datum = timestamp2,
            uzrokSmrti = obdukcija.uzrokSmrti,
            zrtvaId = obdukcija.zrtvaId,
            informacije = obdukcija.informacije
        ),
        zrtva = zrtva
    )
}

fun insertGeminiOsumnjicen(geminiResponse2: GeminiResponse2,timestamp:Long,zl:ZlocinData): MutableList<OsumnjicenData> {

    val osumnjiceni=geminiResponse2.osumnjicenR
    var osumnjiceniLista = mutableListOf<OsumnjicenData>()

    for(o in osumnjiceni){
        val prev=o.idOsumnjicen
        val datumStr = o.osobaId?.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }
        val os= timestamp2?.let {
            o.osobaId?.let { it1 ->
                OsobaData(
                    idOsoba = it1.idOsoba,
                    ime = o.osobaId.ime,
                    kontakt = if(o.osobaId.kontakt==null)"" else o.osobaId.kontakt,
                    datum = it,
                    zanimanje = o.osobaId.zanimanje,
                    pol = o.osobaId.pol,
                    zlocinId = o.osobaId.zlocinId
                )
            }
        }
        if (os != null) {
            insertOsobaData(os,zl)
        }
        if (os != null) {
            val m= o.motiv?.let { it1 -> MotivData(it1.idMotiv,o.motiv.opis) }
            if (m != null) {
                insertMotivData(m)
            }

            o.motiv?.let { it1 ->
                o.osobaId?.let { it2 ->
                    OsumnjicenData(
                        idOsumnjicen = o.idOsumnjicen,
                        status = o.status,
                        tipOsumnjicen = o.tipOsumnjicen,
                        motiv = it1.idMotiv,
                        zlocinId = o.zlocinId,
                        kriv = o.kriv,
                        osobaId = os.idOsoba
                    )
                }
            }?.let { it2 ->
                if (m != null) {
                    insertOsumnjicenData(
                        it2,
                        zlocin = zl,
                        motiv = m,
                        zrtva = ZrtvaData(0,"","","",1,1)
                    )
                }
                val pronadjenOsumnjicen = geminiResponse2.tragKtor.find { it.osumnjicenId==prev }
                pronadjenOsumnjicen?.osumnjicenId=it2.idOsumnjicen

                val pronadjen = geminiResponse2.dokazOsumnjicenKtor.find { it.osumnjicenId==prev }
                pronadjen?.osumnjicenId=it2.idOsumnjicen
                osumnjiceniLista.add(it2)
            }
        }
    }
    return osumnjiceniLista
}

fun insertGeminiSvedok(geminiResponse2: GeminiResponse2,timestamp:Long,zl:ZlocinData){
    val svedoci=geminiResponse2.svedokR

    for(s in svedoci){
        val datumStr = s.osobaId?.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }
        val os= timestamp2?.let {
            s.osobaId?.let { it1 ->
                OsobaData(
                    idOsoba = it1.idOsoba,
                    ime = s.osobaId.ime,
                    kontakt = if(s.osobaId.kontakt==null)"" else s.osobaId.kontakt,
                    datum = it,
                    zanimanje = s.osobaId.zanimanje,
                    pol = s.osobaId.pol,
                    zlocinId = s.osobaId.zlocinId
                )
            }
        }
        if (os != null) {
            insertOsobaData(os,zl)
        }
        if (os != null) {
            insertSvedokData(
                SvedokData(
                    idSvedok = s.idSvedok,
                    izjava = s.izjava,
                    statusSvedok = s.statusSvedok,
                    statusIspitan = s.statusIspitan,
                    zlocinId = s.zlocinId,
                    osobaId = os.idOsoba
                ),
                zlocin = zl
            )
        }
    }

}

fun insertGeminiKontakt(geminiResponse2: GeminiResponse2,zrtva: ZrtvaData):MutableList<KontaktData>{
    val kontakti =geminiResponse2.kontaktKtor
    var kontaktiLista = mutableListOf<KontaktData>()

    for(k in kontakti){
        val prev=k.idKontakt
        val kont=KontaktData(
            idKontakt =k.idKontakt,
            ime = k.ime,
            broj = k.broj,
            status = k.status,
            zrtvaId =zrtva
        )
        insertKontaktData(
            kontakt = kont,
            zrtva = zrtva
        )
        val pronadjenaPoruka = geminiResponse2.porukeKtor.find { it.posiljalacId == prev }
        pronadjenaPoruka?.posiljalacId = kont.idKontakt

        val pronadjenPoziv = geminiResponse2.poziviKtor.find { it.kontaktId==prev }
        pronadjenPoziv?.kontaktId=kont.idKontakt
        kontaktiLista.add(kont)
    }
    return kontaktiLista
}

fun insertGeminiPoruke(geminiResponse2: GeminiResponse2,zrtva: ZrtvaData,kontaktLista: MutableList<KontaktData>,timestamp: Long){
    val poruke =geminiResponse2.porukeKtor

    for(p in poruke){
        val kontakt=kontaktLista.find { it.idKontakt==p.posiljalacId }

        val datumStr = p.datumVreme
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(datumStr, formatter)
        var timestamp2 = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        if(timestamp2==null){
            timestamp2=timestamp
        }
        kontakt?.let {
            PorukeData(
                idPoruke = p.idPoruke,
                tipPoruke = p.tipPoruke,
                sadrzaj = p.sadrzaj,
                datumVreme = timestamp2,
                zrtvaId = zrtva,
                posiljalacId = it,
                statusPoruke = p.statusPoruke,
                sifrovana = p.sirovana
            )
        }?.let {
            insertPorukeData(
                poruke = it,
                zrtva = zrtva,
                kontakt = kontakt
            )
        }

    }
}

fun insertGeminiPozivi(geminiResponse2: GeminiResponse2,zrtva: ZrtvaData,kontaktLista: MutableList<KontaktData>,timestamp: Long){
    val pozivi =geminiResponse2.poziviKtor

    for(p in pozivi){
        val kontakt=kontaktLista.find { it.idKontakt == p.kontaktId }

        val datumStr = p.datumVreme
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(datumStr, formatter)
        var timestamp2 = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        if(timestamp2==null){
            timestamp2=timestamp
        }

        if (kontakt != null) {
            insertPoziviData(
                pozivi = PoziviData(
                    idPoziv = p.idPoziv,
                    tip = p.tip,
                    broj = p.broj,
                    datumVreme = timestamp2,
                    zrtvaId = zrtva,
                    status = p.status,
                    kontaktId = kontakt
                ),
                zrtva = zrtva,
                kontakt = kontakt
            )
        }
    }
}


fun insertGeminiGalerija(geminiResponse2: GeminiResponse2,zrtva: ZrtvaData,timestamp: Long){
    val galerija =geminiResponse2.galerijaKtor

    for(g in galerija){
        val datumStr = g.datumVreme
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(datumStr, formatter)
        var timestamp2 = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        if(timestamp2==null){
            timestamp2=timestamp
        }


        insertGalerijaData(
            galerija = GalerijaData(
                idGalerija = g.idGalerija,
                tip = g.tip,
                putanja = g.putanja,
                zrtvaId = zrtva,
                datumVreme = timestamp2,
                lokacija = g.lokacija
            ),
            zrtva = zrtva
        )

    }
}

fun insertGeminiAplikacija(geminiResponse2: GeminiResponse2,zrtva: ZrtvaData){
    val aplikacija =geminiResponse2.aplikacijaKtor

    for(a in aplikacija){

        insertAplikacijaData(
            aplikacija = AplikacijaData(
                idAplikacije = a.idAplikacije,
                naziv = a.naziv,
                tip = a.tip,
                zrtvaId = zrtva,
                aktivna = a.aktivna,
                informacije = a.informacije
            ),
            zrtva = zrtva
        )
    }
}

fun insertGeminiTrag(geminiResponse2: GeminiResponse2, forenzickiDokaz: MutableList<ForenzickiDokazData>, osumnjicen: MutableList<OsumnjicenData>){
    val tragovi =geminiResponse2.tragKtor

    for(t in tragovi){
        val fz=forenzickiDokaz.find { it.idForenzickiDokaz==t.forenzickiDokazId }
        val os=osumnjicen.find { it.idOsumnjicen==t.osumnjicenId }

        if (fz!=null && os!=null){
            insertTragData(
                trag = TragData(
                    idTrag = t.idTrag,
                    forenzickiDokazId = fz,
                    osumnjicenId = os
                ),
                forenzickiDokaz = fz,
                osumnjicen = os
            )
        }
    }
}

fun insertGeminiDokazOsumnjicen(geminiResponse2: GeminiResponse2, dokazi: MutableList<DokazData>, osumnjicen: MutableList<OsumnjicenData>){
    val dokaziOsumnjiceni =geminiResponse2.dokazOsumnjicenKtor

    for(dok in dokaziOsumnjiceni){
        val d=dokazi.find { it.idDokaz==dok.dokazId }
        val os=osumnjicen.find { it.idOsumnjicen==dok.osumnjicenId }

        if (d!=null && os!=null){
            insertDokazOsumnjicenData(
                dokazOsumnjicen = DokazOsumnjicenData(
                    idDokazOsumnjicen = dok.idDokazOsumnjicen,
                    dokazId = d,
                    osumnjicenId = os
                ),
                dokaz = d,
                osumnjicen = os
            )
        }

    }
}

suspend fun queryGeminiRetrofit(prompt: String,tables:String): Any? {
    val request = GeminiRequest(
        contents = listOf(Content(parts = listOf(Part(text = prompt+tables)))),
    )

    try {
        // Model: gemini-1.5-flash je brz i efikasan. Možeš koristiti i gemini-pro.
        val response: HttpResponse = geminiClient.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$GEMINI_API_KEY") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        println("Gemini - Status: ${response.status}")


        return if (response.status == HttpStatusCode.OK) {
            val geminiResponse: GeminiResponse = response.body()
            println("${geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text}")

            val json2 = Json {
                ignoreUnknownKeys = true
            }
            val cleanJsonString =
                geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace("`", "")
            val cleanJsonString2 = cleanJsonString?.removePrefix("json")
            val geminiResponse2: GeminiResponse2? =
                cleanJsonString2?.let {
                    json2.decodeFromString(
                        it
                    )
                }
            geminiResponse2


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
