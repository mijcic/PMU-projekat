package com.example

import com.example.data.remote.*
import com.example.models.dto.*
import com.example.models.dto.gemini.request.GeminiRequest
import com.example.models.dto.gemini.response.Content
import com.example.models.dto.gemini.response.GeminiResponse
import com.example.models.dto.gemini.response.Part
import com.example.models.dto.gemini.retrofit.GeminiResponse2
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit
import com.example.models.interfaces.*
import com.example.repository.RepositoryInsert
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
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
data class Story(
    val story:String
)

data class SviDokaziOdZrtve(
    val dokaziLista: MutableList<DokazData>,
    val telefoniLista: MutableList<TelefonData>,
    val forenzickiDokaziLista: MutableList<ForenzickiDokazData>,
    val zrtva: ZrtvaData?
)

fun insertGeminiZrtva(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, timestamp:Long, zl: ZlocinData, repo:RepositoryInsert): SviDokaziOdZrtve {
    val zrtva = geminiResponse2.zrtvaR
    val datumStr = zrtva.osobaId?.datum
    var timestamp2: Long? = null
    try {
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
        timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
    } catch (e: Exception) {
        try {
            val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dat = datumStr?.let { LocalDateTime.parse(it, formatter2) }
            timestamp2 = dat?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
        } catch (ex: Exception) {
            println("Greska pri parsiranju datuma: ${ex.message}")
        }
    }

    var dokaziLista: MutableList<DokazData> = mutableListOf()
    var telefoniLista: MutableList<TelefonData> = mutableListOf()
    var forenzickiDokaziLista: MutableList<ForenzickiDokazData> = mutableListOf()

    var zr: ZrtvaData? = null

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
        repo.insertOsobaData(os,zl)
    }
    if (os != null) {
        zr= ZrtvaData(
            idZrtva = zrtva.idZrtva,
            tipZrtve = zrtva.tipZrtve,
            detalji = zrtva.detalji,
            statusZrtva = zrtva.statusZrtva,
            zlocinId = zl.idZlocin,
            osobaId = os
        )

        repo.insertZrtva(zr, zl,os)
        geminiResponseRetrofit.zrtvaRetrofit=zr

        // dokazi
        dokaziLista = insertGeminiDokaz(geminiResponse2,geminiResponseRetrofit,zl,zr,repo)

        //obdukcija
        insertGeminiObdukcija(geminiResponse2,geminiResponseRetrofit,zl,zr,timestamp,repo)

        //forenzicki dokazi
        forenzickiDokaziLista = insertGeminiForenzickiDokaz(geminiResponse2,geminiResponseRetrofit,zr,repo)

        //telefon
        telefoniLista = insertGeminiTelefon(geminiResponse2,geminiResponseRetrofit,zr,repo)
        //insertGeminiTelefon(geminiResponse2,geminiResponseRetrofit,zr)

        //kontakt
        val kontaktLista=insertGeminiKontakt(geminiResponse2, geminiResponseRetrofit,zr,repo)

        //poruke
        insertGeminiPoruke(geminiResponse2,geminiResponseRetrofit,zr,kontaktLista,timestamp,repo)

        //pozivi
        insertGeminiPozivi(geminiResponse2,geminiResponseRetrofit,zr,kontaktLista,timestamp, repo)

        //galerija
        insertGeminiGalerija(geminiResponse2, geminiResponseRetrofit,zr,timestamp, repo)

        //aplikacija
        insertGeminiAplikacija(geminiResponse2,geminiResponseRetrofit,zr,repo)

        //osumnjiceni i osobe (izmeniti)
        var osumnjicenLista=insertGeminiOsumnjicen(geminiResponse2,geminiResponseRetrofit,timestamp,zl,repo)
        //geminiResponseRetrofit.osumnjiceniRetrofit=osumnjicenLista

        insertGeminiTrag(geminiResponse2,geminiResponseRetrofit,forenzickiDokaziLista,osumnjicenLista,repo)

        insertGeminiDokazOsumnjicen(geminiResponse2,geminiResponseRetrofit,dokaziLista,osumnjicenLista,repo)

    }
    return SviDokaziOdZrtve(dokaziLista, telefoniLista, forenzickiDokaziLista, zr)
}

fun insertGeminiDokaz(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData, zrtva: ZrtvaData,repo:RepositoryInsert): MutableList<DokazData> {
    val dokazi = geminiResponse2.dokazR
    var dokaziLista = mutableListOf<DokazData>()

    for(d in dokazi){
        val prev = d.idDokaz
        var dokaz: DokazData?=null
        if(zrtva!=null){
             dokaz = DokazData(
                idDokaz = d.idDokaz,
                tipDokaza = d.tipDokaza,
                opis = d.opis,
                zlocinId = zl.idZlocin,
                zrtvaId = zrtva.idZrtva,
                status = d.status
            )
        }


        if (dokaz != null) {
            repo.insertDokazData(dokaz, zl, zrtva)
        }

        val dokazZadatak = geminiResponse2.dokazZadatakR.find { it.dokazId == prev }
        if (dokaz != null) {
            dokazZadatak?.dokazId = dokaz.idDokaz
            dokaziLista.add(dokaz)
        }

        geminiResponseRetrofit.dokaziRetrofit=dokaziLista
    }
    return dokaziLista
}

fun insertGeminiTelefon(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zrtva: ZrtvaData,repo: RepositoryInsert): MutableList<TelefonData> {
    val telefoni = geminiResponse2.telefonR
    var telefoniLista = mutableListOf<TelefonData>()

    for(t in telefoni){
        val prev = t.idTelefon
        val tel = TelefonData(
            idTelefon = t.idTelefon,
            model = t.model,
            os = t.os,
            sifra = t.sifra,
            informacije = t.informacije,
            zrtvaId = zrtva.idZrtva
        )

        repo.insertTelefonData(tel, zrtva)


        val telefonZadatak = geminiResponse2.telefonZadatakR.find { it.telefonId == prev }
        telefonZadatak?.telefonId = tel.idTelefon

        telefoniLista.add(tel)

    }
    geminiResponseRetrofit.telefoniRetrofit=telefoniLista
    return telefoniLista
}

fun insertGeminiForenzickiDokaz(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zrtva: ZrtvaData,repo: RepositoryInsert): MutableList<ForenzickiDokazData> {
    val dokazi = geminiResponse2.forenzickiDokazR
    val dokaziLista = mutableListOf<ForenzickiDokazData>()

    for(d in dokazi){
        val prev = d.idForenzickiDokaz
        val dokaz = ForenzickiDokazData(
            idForenzickiDokaz = d.idForenzickiDokaz,
            tipForenzickiDokaz = d.tipForenzickiDokaz,
            opis = d.opis,
            statusS = d.statusS,
            veza = d.veza,
            zrtvaId = zrtva.idZrtva
        )

        if (zrtva!=null){
            repo.insertForenzickiDokaz(dokaz, zrtva)
        }


        val forenzickiDokazZadatak = geminiResponse2.forenzickiDokazZadatakR.find { it.forenzickiDokazId == prev }
        forenzickiDokazZadatak?.forenzickiDokazId = dokaz.idForenzickiDokaz

        dokaziLista.add(dokaz)
    }
    geminiResponseRetrofit.forenzickiDokazRetrofit=dokaziLista
    return dokaziLista
}

fun insertGeminiObdukcija(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zl: ZlocinData, zrtva: ZrtvaData, timestamp: Long, repo: RepositoryInsert){
    val obdukcija =geminiResponse2.obdukcijaR
    val datumStr = obdukcija.datum

    var timestamp2: Long? = null

    try {
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
        timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
    } catch (e: Exception) {
        try {
            val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dat = datumStr?.let { LocalDateTime.parse(it, formatter2) }
            timestamp2 = dat?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
        } catch (ex: Exception) {
            println("Greska pri parsiranju datuma: ${ex.message}")
        }
    }

    if(timestamp2==null){
        timestamp2=timestamp
    }

    var ob= ObdukcijaData(
        idObdukcija = obdukcija.idObdukcija,
        izvestaj = obdukcija.izvestaj,
        datum = timestamp2!!,
        uzrokSmrti = obdukcija.uzrokSmrti,
        zrtvaId = obdukcija.zrtvaId,
        informacije = obdukcija.informacije
    )
    repo.insertObdukcijaData(
        obdukcija = ob,
        zrtva = zrtva
    )

    geminiResponseRetrofit.obdukcijaRetrofit=ob
}

fun insertGeminiOsumnjicen(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, timestamp:Long, zl: ZlocinData, repo: RepositoryInsert): MutableList<OsumnjicenData> {
    val osumnjiceni=geminiResponse2.osumnjicenR
    val osumnjiceniLista = mutableListOf<OsumnjicenData>()

    for(o in osumnjiceni){
        val prev = o.idOsumnjicen
        val datumStr = o.osobaId?.datum
        var timestamp2: Long? = null

        try {
            val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
            timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
        } catch (e: Exception) {
            try {
                val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val dat = datumStr?.let { LocalDateTime.parse(it, formatter2) }
                timestamp2 = dat?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
            } catch (ex: Exception) {
                println("Greska pri parsiranju datuma: ${ex.message}")
            }
        }

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
            repo.insertOsobaData(os,zl)
        }
        if (os != null) {
            val m= o.motiv?.let { it1 -> MotivData(it1.idMotiv,o.motiv.opis) }
            if (m != null) {
                repo.insertMotivData(m)
            }

            if (m != null) {
                val osum= OsumnjicenData(
                    idOsumnjicen = o.idOsumnjicen,
                    status = o.status,
                    tipOsumnjicen = o.tipOsumnjicen,
                    motiv = m,
                    zlocinId = o.zlocinId,
                    kriv = o.kriv,
                    osobaId = os
                )
                repo.insertOsumnjicenData(
                    osum,
                    zlocin = zl,
                    motiv = m
                )

                val ispitivanjeOsumnjicenogZadatak = geminiResponse2.ispitivanjeOsumnjicenogZadatakR.find { it.osumnjicenId == prev }
                ispitivanjeOsumnjicenogZadatak?.osumnjicenId = osum.idOsumnjicen

                val odnosOsumnjicenZrtva = geminiResponse2.odnosOsumnjicenZrtvaR.find { it.osumnjicenId == prev }
                odnosOsumnjicenZrtva?.osumnjicenId = osum.idOsumnjicen

                val pitanjeIspitivanjeOsumnjicenog = geminiResponse2.pitanjeIspitivanjeOsumnjicenogR.find { it.osumnjicenId == prev }
                pitanjeIspitivanjeOsumnjicenog?.osumnjicenId = osum.idOsumnjicen

                osumnjiceniLista.add(osum)

                val pronadjenOsumnjicen = geminiResponse2.tragKtor.find { it.osumnjicenId==prev }
                pronadjenOsumnjicen?.osumnjicenId=osum.idOsumnjicen

                val pronadjen = geminiResponse2.dokazOsumnjicenKtor.find { it.osumnjicenId==prev }
                pronadjen?.osumnjicenId=osum.idOsumnjicen
                osumnjiceniLista.add(osum)
            }
        }
    }
    geminiResponseRetrofit.osumnjiceniRetrofit=osumnjiceniLista
    return osumnjiceniLista
}

fun insertGeminiSvedok(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, timestamp:Long, zl: ZlocinData, repo: RepositoryInsert): MutableList<SvedokData>{
    val svedoci=geminiResponse2.svedokR
    val svedociLista = mutableListOf<SvedokData>()

    for(s in svedoci){
        val prev = s.idSvedok
        val datumStr = s.osobaId?.datum

        var timestamp2: Long? = null

        try {
            val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
            timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
        } catch (e: Exception) {
            try {
                val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val dat = datumStr?.let { LocalDateTime.parse(it, formatter2) }
                timestamp2 = dat?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
            } catch (ex: Exception) {
                println("Greska pri parsiranju datuma: ${ex.message}")
            }
        }

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
            repo.insertOsobaData(os,zl)
        }
        if (os != null) {
            val svedok = SvedokData(
                idSvedok = s.idSvedok,
                izjava = s.izjava,
                statusSvedok = s.statusSvedok,
                statusIspitan = s.statusIspitan,
                zlocinId = s.zlocinId,
                osobaId = os
            )
            repo.insertSvedokData(
                svedok = svedok,
                zlocin = zl
            )

            val pronadjenoIspitivanjeSvedokaZadatak = geminiResponse2.ispitivanjeSvedokaZadatakR.find { it.svedokId == prev }
            pronadjenoIspitivanjeSvedokaZadatak?.svedokId = svedok.idSvedok

            val pitanjeIspitivanjeSvedoka = geminiResponse2.pitanjeIspitivanjeSvedokaR.find { it.svedokId == prev }
            pitanjeIspitivanjeSvedoka?.svedokId = svedok.idSvedok

            svedociLista.add(svedok)
        }
    }
    geminiResponseRetrofit.svedociRetrofit = svedociLista
    return svedociLista
}

fun insertGeminiOneContact(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData,repo: RepositoryInsert): MutableList<OneContactData> {
    val kontakti = geminiResponse2.oneContactR
    val kontaktiLista = mutableListOf<OneContactData>()

    for(k in kontakti){
        val prev = k.idOneContact

        if (k.slika==null){
            k.slika=0
        }

        val kontakt = OneContactData(
            idOneContact = k.idOneContact,
            zlocinId = k.zlocinId,
            ime = k.ime,
            broj = k.broj,
            slika = k.slika!!,
        )

        repo.insertOneContactData(kontakt, zl)

        val oneCall = geminiResponse2.oneCallR.find { it.kontakt == prev }
        oneCall?.kontakt = kontakt.idOneContact

        val kontaktKoSalje = geminiResponse2.obicnaPorukaR.find { it.kontaktKoSalje == prev }
        kontaktKoSalje?.kontaktKoSalje = kontakt.idOneContact

        val kontaktKomeSalje = geminiResponse2.obicnaPorukaR.find { it.kontaktKomeSalje == prev }
        kontaktKomeSalje?.kontaktKomeSalje = kontakt.idOneContact

        kontaktiLista.add(kontakt)
    }
    geminiResponseRetrofit.oneContactRetrofit = kontaktiLista
    return kontaktiLista
}

fun insertGeminiKontakt(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zrtva: ZrtvaData, repo: RepositoryInsert):MutableList<KontaktData>{
    val kontakti =geminiResponse2.kontaktKtor
    var kontaktiLista = mutableListOf<KontaktData>()

    for(k in kontakti){
        val prev=k.idKontakt
        val kont= KontaktData(
            idKontakt =k.idKontakt,
            ime = k.ime,
            broj = k.broj,
            status = k.status,
            zrtvaId =zrtva
        )
        repo.insertKontaktData(
            kontakt = kont,
            zrtva = zrtva
        )
        val pronadjenaPoruka = geminiResponse2.porukeKtor.find { it.posiljalacId == prev }
        pronadjenaPoruka?.posiljalacId = kont.idKontakt

        val pronadjenPoziv = geminiResponse2.poziviKtor.find { it.kontaktId==prev }
        pronadjenPoziv?.kontaktId=kont.idKontakt
        kontaktiLista.add(kont)
    }
    geminiResponseRetrofit.kontaktiRetrofit = kontaktiLista
    return kontaktiLista
}

fun insertGeminiPoruke(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zrtva: ZrtvaData, kontaktLista: MutableList<KontaktData>, timestamp: Long, repo: RepositoryInsert){
    val poruke =geminiResponse2.porukeKtor
    var porukeLista = mutableListOf<PorukeData>()

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
            repo.insertPorukeData(
                poruke = it,
                zrtva = zrtva,
                kontakt = kontakt
            )
            porukeLista.add(it)
        }

    }
    geminiResponseRetrofit.porukeRetrofit = porukeLista
}

fun insertGeminiPozivi(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zrtva: ZrtvaData, kontaktLista: MutableList<KontaktData>, timestamp: Long, repo: RepositoryInsert){
    val pozivi =geminiResponse2.poziviKtor
    var poziviLista = mutableListOf<PoziviData>()

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
            val poz= PoziviData(
                idPoziv = p.idPoziv,
                tip = p.tip,
                broj = p.broj,
                datumVreme = timestamp2,
                zrtvaId = zrtva,
                status = p.status,
                kontaktId = kontakt
            )
            repo.insertPoziviData(
                pozivi = poz,
                zrtva = zrtva,
                kontakt = kontakt
            )

            poziviLista.add(poz)
        }
    }
    geminiResponseRetrofit.poziviRetrofit = poziviLista
}


fun insertGeminiGalerija(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zrtva: ZrtvaData, timestamp: Long, repo: RepositoryInsert){
    val galerija =geminiResponse2.galerijaKtor
    val galerijaLista = mutableListOf<GalerijaData>()

    for(g in galerija){
        val datumStr = g.datumVreme

        var timestamp2: Long? = null

        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val datum = datumStr?.let { LocalDate.parse(it, formatter) }
            timestamp2 = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
        } catch (e: Exception) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val datum = datumStr?.let { LocalDateTime.parse(it, formatter) }
                timestamp2 = datum?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
            } catch (ex: Exception) {
                try {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH)
                    val dateTime = LocalDateTime.parse(datumStr, formatter)
                    timestamp2 = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
                }catch (exx:Exception){
                }
                println("Greska pri parsiranju datuma: ${ex.message}")
            }
        }


        if(timestamp2==null){
            timestamp2=timestamp
        }

        val gal= GalerijaData(
            idGalerija = g.idGalerija,
            tip = g.tip,
            putanja = g.putanja,
            zrtvaId = zrtva,
            datumVreme = timestamp2,
            lokacija = g.lokacija
        )
        repo.insertGalerijaData(
            galerija = gal,
            zrtva = zrtva
        )
        galerijaLista.add(gal)
    }
    geminiResponseRetrofit.galerijaRetrofit = galerijaLista
}

fun insertGeminiAplikacija(geminiResponse2: GeminiResponseCommon2,geminiResponseRetrofit: GeminiResponseRetrofitCommon,zrtva: ZrtvaData, repo: RepositoryInsert){
    val aplikacija =geminiResponse2.aplikacijaKtor
    val aplikacijaLista = mutableListOf<AplikacijaData>()

    for(a in aplikacija){
        val ap= AplikacijaData(
            idAplikacije = a.idAplikacije,
            naziv = a.naziv,
            tip = a.tip,
            zrtvaId = zrtva,
            aktivna = a.aktivna,
            informacije = a.informacije
        )

        repo.insertAplikacijaData(
            aplikacija = ap,
            zrtva = zrtva
        )
        aplikacijaLista.add(ap)
    }
    geminiResponseRetrofit.aplikacijeRetrofit = aplikacijaLista
}

fun insertGeminiTrag(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, forenzickiDokaz: MutableList<ForenzickiDokazData>, osumnjicen: MutableList<OsumnjicenData>, repo: RepositoryInsert){
    val tragovi =geminiResponse2.tragKtor
    val tragLista = mutableListOf<TragData>()

    for(t in tragovi){
        val fz=forenzickiDokaz.find { it.idForenzickiDokaz==t.forenzickiDokazId }
        val os=osumnjicen.find { it.idOsumnjicen==t.osumnjicenId }

        if (fz!=null && os!=null){
            val tr = TragData(
                idTrag = t.idTrag,
                forenzickiDokazId = fz,
                osumnjicenId = os
            )
            repo.insertTragData(
                trag = tr ,
                forenzickiDokaz = fz,
                osumnjicen = os
            )
            tragLista.add(tr)
        }
    }
    geminiResponseRetrofit.tragoviRetrofit = tragLista
}

fun insertGeminiDokazOsumnjicen(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, dokazi: MutableList<DokazData>, osumnjicen: MutableList<OsumnjicenData>, repo: RepositoryInsert){
    val dokaziOsumnjiceni =geminiResponse2.dokazOsumnjicenKtor
    val dokaziOsumnjiceniLista = mutableListOf<DokazOsumnjicenData>()

    for(dok in dokaziOsumnjiceni){
        val d=dokazi.find { it.idDokaz==dok.dokazId }
        val os=osumnjicen.find { it.idOsumnjicen==dok.osumnjicenId }

        if (d!=null && os!=null){
            val dokOs= DokazOsumnjicenData(
                idDokazOsumnjicen = dok.idDokazOsumnjicen,
                dokazId = d,
                osumnjicenId = os
            )
            repo.insertDokazOsumnjicenData(
                dokazOsumnjicen =dokOs ,
                dokaz = d,
                osumnjicen = os
            )
            dokaziOsumnjiceniLista.add(dokOs)
        }
    }
    geminiResponseRetrofit.dokaziOsumnjiceniRetrofit = dokaziOsumnjiceniLista
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




fun insertGeminiBeleska(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData, timestamp: Long, repo: RepositoryInsert) {
    val beleske = geminiResponse2.beleskaR
    val beleskaLista = mutableListOf<BeleskaData>()

    for(b in beleske){
        val datumStr = b.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        val beleska = BeleskaData(
            idBeleska = b.idBeleska,
            zlocinId = b.zlocinId,
            tekst = b.tekst,
            datum = timestamp2,
        )

        repo.insertBeleskaData(beleska, zl)
        beleskaLista.add(beleska)
    }
    geminiResponseRetrofit.beleskeRetrofit = beleskaLista
}

fun insertGeminiWhatsAppKontakt(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,zl: ZlocinData, repo: RepositoryInsert): MutableList<WhatsAppKontaktData> {
    val whatsAppKontakti = geminiResponse2.whatsAppKontaktR
    val whatsAppLista = mutableListOf<WhatsAppKontaktData>()

    for(w in whatsAppKontakti){
        val prev = w.idWhatsAppKontakt

        val whatsAppKontakt = WhatsAppKontaktData(
            idWhatsAppKontakt = w.idWhatsAppKontakt,
            zlocinId = w.zlocinId,
            ime = w.ime,
            broj = w.broj,
            slika = w.slika
        )

        repo.insertWhatsAppKontaktData(whatsAppKontakt, zl)

        val waKontaktKoSalje = geminiResponse2.whatsAppPorukaR.find { it.kontaktKoSalje == prev }
        waKontaktKoSalje?.kontaktKoSalje = whatsAppKontakt.idWhatsAppKontakt

        val waKontaktKomeSalje = geminiResponse2.whatsAppPorukaR.find { it.kontaktKomeSalje == prev }
        waKontaktKomeSalje?.kontaktKomeSalje = whatsAppKontakt.idWhatsAppKontakt

        whatsAppLista.add(whatsAppKontakt)
    }
    geminiResponseRetrofit.whatsappKontaktRetrofit = whatsAppLista
    return whatsAppLista
}

fun insertGeminiWhatsAppPoruka(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, kontaktiLista: MutableList<WhatsAppKontaktData>, timestamp: Long,repo: RepositoryInsert) {
    val whatsAppPoruke = geminiResponse2.whatsAppPorukaR
    val whatsAppPorukaLista = mutableListOf<WhatsAppPorukaData>()


    for(w in whatsAppPoruke){
        val waKontaktKoSalje = kontaktiLista.find { it.idWhatsAppKontakt == w.kontaktKoSalje }
        val waKontaktKomeSalje = kontaktiLista.find { it.idWhatsAppKontakt == w.kontaktKomeSalje }

        val datumStr = w.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        if (waKontaktKoSalje != null && waKontaktKomeSalje != null) {
            val whatsAppPoruka = WhatsAppPorukaData(
                idWhatsAppPoruka = w.idWhatsAppPoruka,
                kontaktKoSalje = waKontaktKoSalje.idWhatsAppKontakt,
                kontaktKomeSalje = waKontaktKomeSalje.idWhatsAppKontakt,
                tekst = w.tekst,
                datum = timestamp2,
                procitana = w.procitana
            )

            repo.insertWhatsAppPorukaData(whatsAppPoruka, waKontaktKoSalje, waKontaktKomeSalje)
            whatsAppPorukaLista.add(whatsAppPoruka)
        }
    }
    geminiResponseRetrofit.whatsappPorukaRetrofit = whatsAppPorukaLista
}

fun insertGeminiOneCall(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zrtvaData: ZrtvaData, kontaktiLista: MutableList<OneContactData>, timestamp: Long,repo: RepositoryInsert) {
    val pozivi = geminiResponse2.oneCallR
    val poziviLista = mutableListOf<OneCallData>()

    for(p in pozivi){
        val kontakt = kontaktiLista.find { it.idOneContact == p.kontakt }

        val datumStr = p.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        if (kontakt != null) {
            val poziv = OneCallData(
                idOneCall = p.idOneCall,
                kontakt = kontakt.idOneContact,
                datum = timestamp2,
                propusten = p.propusten,
                dolazni = p.dolazni,
                zrtvaId = zrtvaData.idZrtva
            )

            repo.insertOneCallData(poziv)
            poziviLista.add(poziv)
        }
    }
    geminiResponseRetrofit.oneCallRetrofit = poziviLista
}

fun insertGeminiGallery(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData, timestamp: Long,repo: RepositoryInsert) {
    val galerija = geminiResponse2.galleryR
    val galleryLista = mutableListOf<GalleryData>()

    for(g in galerija){
        val datumStr = g.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        val slika = GalleryData(
            idPhoto = g.idPhoto,
            zlocinId = zl.idZlocin,
            slika = g.slika,
            datum = timestamp2,
            mesto = g.mesto
        )

        repo.insertGalleryData(slika, zl)
        galleryLista.add(slika)
    }
    geminiResponseRetrofit.galleryRetrofit= galleryLista
}

fun insertGeminiObicnaPoruka(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, kontaktiLista: MutableList<OneContactData>, timestamp: Long,repo: RepositoryInsert) {
    val obicnePoruke = geminiResponse2.obicnaPorukaR
    val obicnePorukeLista = mutableListOf<ObicnaPorukaData>()

    for(p in obicnePoruke){
        val kontaktKoSalje = kontaktiLista.find { it.idOneContact == p.kontaktKoSalje }
        val kontaktKomeSalje = kontaktiLista.find { it.idOneContact == p.kontaktKomeSalje }

        val datumStr = p.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        if (kontaktKoSalje != null && kontaktKomeSalje != null) {
            val poruka = ObicnaPorukaData(
                idObicnaPoruka = p.idObicnaPoruka,
                kontaktKoSalje = kontaktKoSalje.idOneContact,
                kontaktKomeSalje = kontaktKomeSalje.idOneContact,
                tekst = p.tekst,
                datum = timestamp2,
                procitana = p.procitana
            )

            repo.insertObicnaPorukaData(poruka, kontaktKoSalje, kontaktKomeSalje)
            obicnePorukeLista.add(poruka)
        }
    }
    geminiResponseRetrofit.obicnePorukeRetrofit = obicnePorukeLista
}

fun insertGeminiOdnosOsumnjicenZrtva(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, osumnjicenLista: MutableList<OsumnjicenData>, zrtva: ZrtvaData, repo: RepositoryInsert) {
    val odnosi = geminiResponse2.odnosOsumnjicenZrtvaR
    val odnosOsumnjicenZrtvaLista = mutableListOf<OdnosOsumnjicenZrtvaData>()
    val validValues = listOf("poslovni", "licni", "porodicni", "rivalski", "slucajni", "ljubavni", "kolege")

    for(o in odnosi){
        val osumnjiceni = osumnjicenLista.find { it.idOsumnjicen == o.osumnjicenId }
        if (o.tipOdnosa !in validValues) {
            o.tipOdnosa = "slucajni"
        }

        if (osumnjiceni != null) {
            val odnos = OdnosOsumnjicenZrtvaData(
                idOdnos = o.idOdnos,
                osumnjicenId = osumnjiceni.idOsumnjicen,
                zrtvaId = zrtva.idZrtva,
                tipOdnosa = o.tipOdnosa
            )

            repo.insertOdnosOsumnjicenZrtvaData(odnos, osumnjiceni, zrtva)
            odnosOsumnjicenZrtvaLista.add(odnos)
        }
    }
    geminiResponseRetrofit.odnosiOsumnjiceniZrtvaRetrofit = odnosOsumnjicenZrtvaLista
}

fun insertGeminiPrijavljeniKorisnik(geminiResponse2: GeminiResponse2, repo: RepositoryInsert) {
    val korisnici = geminiResponse2.prijavljeniKorisnikR

    for(k in korisnici){
        val korisnik = PrijavljeniKorisnikData(
            idKorisnik = k.idKorisnik,
            korisnickoIme = k.korisnickoIme,
            sifra = k.sifra
        )

        repo.insertPrijavljeniKorisnikData(korisnik)
    }
}

fun insertGeminiPitanje(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData,repo: RepositoryInsert): MutableList<PitanjeData> {
    val pitanja = geminiResponse2.pitanjeR
    val pitanjeList = mutableListOf<PitanjeData>()

    for(p in pitanja){
        val prev = p.idPitanje
        val pitanje = PitanjeData(
            idPitanje = p.idPitanje,
            zlocinId = zl.idZlocin,
            tekst = p.tekst
        )

        repo.insertPitanjeData(pitanje, zl)

        val pronadjeniOdgovor = geminiResponse2.odgovorR.find { it.pitanjeId == prev }
        pronadjeniOdgovor?.pitanjeId = pitanje.idPitanje

        pitanjeList.add(pitanje)
    }
    geminiResponseRetrofit.pitanjaRetrofit = pitanjeList
    return pitanjeList
}

fun insertGeminiOdgovor(geminiResponse2: GeminiResponseCommon2,geminiResponseRetrofit: GeminiResponseRetrofitCommon, pitanjeLista: MutableList<PitanjeData>,repo: RepositoryInsert) {
    val odgovori = geminiResponse2.odgovorR
    val odgovoriLista = mutableListOf<OdgovorData>()

    for(o in odgovori){
        val pitanje = pitanjeLista.find { it.idPitanje == o.pitanjeId }

        if (pitanje != null) {
            val odgovor = OdgovorData(
                idOdogovor = o.idOdogovor,
                pitanjeId = pitanje.idPitanje,
                tekstOdgovora = o.tekstOdgovora,
                tacan = o.tacan,
                bodovi = o.bodovi
            )

            repo.insertOdgovorData(odgovor, pitanje)
            odgovoriLista.add(odgovor)
        }
    }
    geminiResponseRetrofit.odgovoriRetrofit = odgovoriLista
}

fun insertGeminiPitanjeIspitivanjeOsumnjicenog(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, osumnjiceniLista: MutableList<OsumnjicenData>, repo: RepositoryInsert) {
    val pitanja = geminiResponse2.pitanjeIspitivanjeOsumnjicenogR
    val pitanjaIspitivanjeOsumnjicenogLista = mutableListOf<PitanjeIspitivanjeOsumnjicenogData>()
    val validValues = listOf("opsta", "alibi", "dokaz", "kontradikcija")

    for(p in pitanja){
        val osumnjiceni = osumnjiceniLista.find { it.idOsumnjicen == p.osumnjicenId }
        if (p.kategorija !in validValues) {
            p.kategorija = "opsta"
        }

        if (osumnjiceni != null) {
            val pitanje = PitanjeIspitivanjeOsumnjicenogData(
                idPitanjeIspitivanjeOsumnjicenog = p.idPitanjeIspitivanjeOsumnjicenog,
                kategorija = p.kategorija,
                tekst = p.tekst,
                odgovor = p.odgovor,
                komentar = p.komentar,
                osumnjicenId = osumnjiceni.idOsumnjicen
            )

            repo.insertPitanjeIspitivanjeOsumnjicenogData(pitanje, osumnjiceni)
            pitanjaIspitivanjeOsumnjicenogLista.add(pitanje)
        }
    }
    geminiResponseRetrofit.pitanjeIspitivanjeOsumnjicenogRetrofit = pitanjaIspitivanjeOsumnjicenogLista
}

fun insertGeminiPitanjeIspitivanjeSvedoka(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, svedociLista: MutableList<SvedokData>, repo: RepositoryInsert) {
    val pitanja = geminiResponse2.pitanjeIspitivanjeSvedokaR
    val pitanjeIspitivanjeSvedokaLista = mutableListOf<PitanjeIspitivanjeSvedokaData>()

    for(p in pitanja){
        var svedok = svedociLista.find { it.idSvedok == p.svedokId }

        if (svedok != null) {
            val pitanje = PitanjeIspitivanjeSvedokaData(
                idPitanjeIspitivanjeSvedoka = p.idPitanjeIspitivanjeSvedoka,
                tekst = p.tekst,
                odgovor = p.odgovor,
                svedokId = svedok.idSvedok,
                nextPitanje = p.nextPitanje
            )

            repo.insertPitanjeIspitivanjeSvedokaData(pitanje, svedok)
            pitanjeIspitivanjeSvedokaLista.add(pitanje)
        }
    }
    geminiResponseRetrofit.pitanjeIspitivanjeSvedokaRetrofit = pitanjeIspitivanjeSvedokaLista
}

fun insertGeminiOsoba(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zlocin: ZlocinData, timestamp: Long,repo: RepositoryInsert) {
    val osobe = geminiResponse2.osobaR
    val osobeLista = mutableListOf<OsobaData>()

    for(o in osobe){
        val datumStr = o.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        val osoba = OsobaData(
            idOsoba = o.idOsoba,
            ime = o.ime,
            kontakt = o.kontakt?.toString() ?: "",
            datum = timestamp2,
            zanimanje = o.zanimanje,
            pol = o.pol,
            zlocinId = zlocin.idZlocin
        )

        repo.insertOsobaData(osoba, zlocin)
        osobeLista.add(osoba)
    }
    geminiResponseRetrofit.osobeRetrofit = osobeLista
}

fun insertGeminiZadatak(geminiResponse2: GeminiResponse2, zlocin: ZlocinData, repo: RepositoryInsert): MutableList<ZadatakData> {
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

        val pronadjenoIspitivanjeSvedokaZadatak = geminiResponse2.ispitivanjeSvedokaZadatakR.find { it.zadatakId == prev }
        pronadjenoIspitivanjeSvedokaZadatak?.zadatakId = zad.idZadatak

        val dokazZadatak = geminiResponse2.dokazZadatakR.find { it.zadatakId == prev }
        dokazZadatak?.zadatakId = zad.idZadatak

        val ispitivanjeOsumnjicenogZadatak = geminiResponse2.ispitivanjeOsumnjicenogZadatakR.find { it.zadatakId == prev }
        ispitivanjeOsumnjicenogZadatak?.zadatakId = zad.idZadatak

        val telefonZadatak = geminiResponse2.telefonZadatakR.find { it.zadatakId == prev }
        telefonZadatak?.zadatakId = zad.idZadatak

        val forenzickiDokazZadatak = geminiResponse2.forenzickiDokazZadatakR.find { it.zadatakId == prev }
        forenzickiDokazZadatak?.zadatakId = zad.idZadatak

        zadaciLista.add(zad)
    }

    return zadaciLista
}

fun updateGeminiZadatakList(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zlocin: ZlocinData, repo: RepositoryInsert) {
    val zadaci = geminiResponse2.zadatakR
    var lista: List<ZadatakData> = emptyList()
    lista = repo.getZadatakListaData()
    repo.updateZadatakListData(lista,zlocin)
    //geminiResponseRetrofit.zadaciRetrofit =lista
}

fun insertGeminiDokazZadatak(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, dokazList: MutableList<DokazData>, zadatakList: MutableList<ZadatakData>,repo: RepositoryInsert) {
    val dokazi = geminiResponse2.dokazZadatakR
    val dokaziLista = mutableListOf<DokazZadatakData>()

    for(d in dokazi) {
        val dokaz = dokazList.find { it.idDokaz == d.dokazId }
        val zadatak = zadatakList.find { it.idZadatak == d.zadatakId }

        if (dokaz != null && zadatak != null) {
            val dokazZadatak = DokazZadatakData(
                idDokazZadatak = d.idDokazZadatak,
                tekst = d.tekst,
                dokazId = dokaz.idDokaz,
                uradjen = d.uradjen,
                zadatakId = zadatak.idZadatak
            )

            repo.insertDokazZadatakData(dokazZadatak, dokaz, zadatak)
            dokaziLista.add(dokazZadatak)
        }
    }
    geminiResponseRetrofit.dokaziZadaciRetrofit = dokaziLista
}

fun insertGeminiIspitivanjeOsumnjicenogZadatak(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, osumnjicenList: MutableList<OsumnjicenData>, zadatakList: MutableList<ZadatakData>, repo: RepositoryInsert) {
    val ispitivanja = geminiResponse2.ispitivanjeOsumnjicenogZadatakR
    val ispitivanjeOsumnjicenogZadatakLista = mutableListOf<IspitivanjeOsumnjicenogZadatakData>()

    for(i in ispitivanja) {
        val osumnjicen = osumnjicenList.find { it.idOsumnjicen == i.osumnjicenId }
        val zadatak = zadatakList.find { it.idZadatak == i.zadatakId }

        if (osumnjicen != null && zadatak != null) {
            val ispitivanjeOsumnjicenogZadatak = IspitivanjeOsumnjicenogZadatakData(
                idIspitivanjeOsumnjicenogZadatak = i.idIspitivanjeOsumnjicenogZadatak,
                osumnjicenId = osumnjicen.idOsumnjicen,
                zadatakId = zadatak.idZadatak,
                uradjen = i.uradjen
            )

            repo.insertIspitivanjeOsumnjicenogZadatakData(ispitivanjeOsumnjicenogZadatak, osumnjicen, zadatak)
            ispitivanjeOsumnjicenogZadatakLista.add(ispitivanjeOsumnjicenogZadatak)
        }
    }
    geminiResponseRetrofit.ispitivanjeOsumnjicenogZadaciRetrofit = ispitivanjeOsumnjicenogZadatakLista
}

fun insertGeminiIspitivanjeSvedokaZadatak(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, svedokList: MutableList<SvedokData>, zadatakList: MutableList<ZadatakData>, repo: RepositoryInsert) {
    val ispitivanja = geminiResponse2.ispitivanjeSvedokaZadatakR
    val ispitivanjeSvedokaZadatakLista = mutableListOf<IspitivanjeSvedokaZadatakData>()

    for(i in ispitivanja) {
        val zadatak = zadatakList.find { it.idZadatak == i.zadatakId }
        val svedok = svedokList.find { it.idSvedok == i.svedokId }

        if (svedok != null && zadatak != null) {
            val ispitivanjeSvedokaZadatak = IspitivanjeSvedokaZadatakData(
                idIspitivanjeSvedokaZadatak = i.idIspitivanjeSvedokaZadatak,
                svedokId = svedok.idSvedok,
                zadatakId = zadatak.idZadatak,
                uradjen = i.uradjen
            )

            repo.insertIspitivanjeSvedokaZadatakData(ispitivanjeSvedokaZadatak, svedok, zadatak)
            ispitivanjeSvedokaZadatakLista.add(ispitivanjeSvedokaZadatak)
        }
    }
    geminiResponseRetrofit.ispitivanjeSvedokaZadaciRetrofit = ispitivanjeSvedokaZadatakLista
}

fun insertGeminiTelefonZadatak(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, telefonList: MutableList<TelefonData>, zadatakList: MutableList<ZadatakData>,repo: RepositoryInsert) {
    val zadaci = geminiResponse2.telefonZadatakR
    val telefonZadatakLista = mutableListOf<TelefonZadatakData>()

    for(z in zadaci) {
        val zadatak = zadatakList.find { it.idZadatak == z.zadatakId }
        val telefon = telefonList.find { it.idTelefon == z.telefonId }

        if (telefon != null && zadatak != null) {
            val telefonZadatak = TelefonZadatakData(
                idTelefonZadatak = z.idTelefonZadatak,
                telefonId = telefon.idTelefon,
                zadatakId = zadatak.idZadatak,
                uradjen = z.uradjen
            )

            repo.insertTelefonZadatakData(telefonZadatak, telefon, zadatak)
            telefonZadatakLista.add(telefonZadatak)
        }
    }
    geminiResponseRetrofit.telefonZadaciRetrofit = telefonZadatakLista
}

fun insertGeminiForenzickiDokazZadatak(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, forenzickiDokazList: MutableList<ForenzickiDokazData>, zadatakList: MutableList<ZadatakData>,repo: RepositoryInsert) {
    val zadaci = geminiResponse2.forenzickiDokazZadatakR
    val forenzickiDokazZadaciLista = mutableListOf<ForenzickiDokazZadatakData>()

    for(z in zadaci) {
        val zadatak = zadatakList.find { it.idZadatak == z.zadatakId }
        val forenzickiDokaz = forenzickiDokazList.find { it.idForenzickiDokaz == z.forenzickiDokazId }

        if (forenzickiDokaz != null && zadatak != null) {
            val forenzickiDokazZadatak = ForenzickiDokazZadatakData(
                idForenzickiDokazZadatak = z.idForenzickiDokazZadatak,
                tekst = z.tekst,
                forenzickiDokazId = forenzickiDokaz.idForenzickiDokaz,
                uradjen = z.uradjen,
                zadatakId = zadatak.idZadatak
            )

            repo.insertForenzickiDokazZadatakData(forenzickiDokazZadatak, forenzickiDokaz, zadatak)
            forenzickiDokazZadaciLista.add(forenzickiDokazZadatak)
        }
    }
    geminiResponseRetrofit.forenzickiDokazZadaciRetrofit = forenzickiDokazZadaciLista
}

//fun insertGeminiPorukeZadatak(geminiResponse2: GeminiResponse2, porukeList: MutableList<PorukeData>, zadatakList: MutableList<ZadatakData>) {
//    val zadaci = geminiResponse2.porukeZadatakR
//
//    for(z in zadaci){
//        val zadatak = zadatakList.find { it.idZadatak == z.zadatakId }
//        val poruka = porukeList.find { it.idPoruke == z.porukeId }
//
//        if (poruka != null && zadatak != null) {
//            val porukaZadatak = PorukeZadatakData(
//                idPorukeZadatak = z.idPorukeZadatak,
//                porukeId = poruka.idPoruke,
//                zadatakId = zadatak.idZadatak,
//                uradjen = z.uradjen
//            )
//
//            insertPorukeZadatakData(porukaZadatak, poruka, zadatak)
//        }
//    }
//}

suspend fun suspendInsertKontakti(
    whatsAppKontaktiLista: MutableList<WhatsAppKontaktData>,
    geminiResponse2: GeminiResponse2,
    geminiResponseRetrofit: GeminiResponseRetrofit,
    timestamp: Long,
    kontaktiLista: MutableList<OneContactData>,
    zl: ZlocinData,
    zrtva: ZrtvaData,
    repo: RepositoryInsert
) = coroutineScope {

    //  whatsAppPoruka
    val whatsAppPorukaDeferred = async(Dispatchers.IO) {
        insertGeminiWhatsAppPoruka(geminiResponse2, geminiResponseRetrofit, whatsAppKontaktiLista, timestamp, repo)
    }
    // one call
    val oneCallDeferred = async(Dispatchers.IO) {
        insertGeminiOneCall(geminiResponse2, geminiResponseRetrofit, zrtva, kontaktiLista, timestamp, repo)
    }
    // gallery
    val galleryDeferred = async(Dispatchers.IO) {
        insertGeminiGallery(geminiResponse2, geminiResponseRetrofit,zl, timestamp,repo)
    }
    //  obicnaPoruka
    val obicnaPorukaDeferred = async(Dispatchers.IO) {
        insertGeminiObicnaPoruka(geminiResponse2,geminiResponseRetrofit, kontaktiLista, timestamp,repo)
    }
    // cekanje rezultata
    whatsAppPorukaDeferred.await()
    oneCallDeferred.await()
    galleryDeferred.await()
    obicnaPorukaDeferred.await()
}



suspend fun suspendInsertPitanja(
    pitanjaLista: MutableList<PitanjeData>,
    geminiResponse2: GeminiResponse2,
    geminiResponseRetrofit: GeminiResponseRetrofit,
    timestamp: Long,
    osumnjiceniLista: MutableList<OsumnjicenData>,
    svedociLista: MutableList<SvedokData>,
    zl: ZlocinData,
    repo: RepositoryInsert
) = coroutineScope {

    // odgovor
    val odgovoriDeferred = async(Dispatchers.IO) {
        insertGeminiOdgovor(geminiResponse2, geminiResponseRetrofit,pitanjaLista, repo)
    }
    // pitanjeIspitivanjeOsumnjicenog
    val pitanjeIspitivanjeOsumnjicenogDeferred = async(Dispatchers.IO) {
        insertGeminiPitanjeIspitivanjeOsumnjicenog(geminiResponse2, geminiResponseRetrofit,osumnjiceniLista, repo)
    }
    // pitanjeIspitivanjeSvedoka
    val pitanjeIspitivanjeSvedokaDeferred = async(Dispatchers.IO) {
        insertGeminiPitanjeIspitivanjeSvedoka(geminiResponse2, geminiResponseRetrofit,svedociLista, repo)
    }
    //  osoba
    val osobaDeferred = async(Dispatchers.IO) {
        insertGeminiOsoba(geminiResponse2, geminiResponseRetrofit, zl, timestamp, repo)
    }
    // cekanje rezultata
    odgovoriDeferred.await()
    pitanjeIspitivanjeOsumnjicenogDeferred.await()
    pitanjeIspitivanjeSvedokaDeferred.await()
    osobaDeferred.await()
}


suspend fun suspendInsertZadaci(
    zadaciLista: MutableList<ZadatakData>,
    geminiResponse2: GeminiResponse2,
    geminiResponseRetrofit: GeminiResponseRetrofit,
    osumnjiceniLista: MutableList<OsumnjicenData>,
    svedociLista: MutableList<SvedokData>,
    sviDokaziZrtva:SviDokaziOdZrtve,
    repo: RepositoryInsert
) = coroutineScope {
    // dokazZadatak
    val dokazZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiDokazZadatak(geminiResponse2, geminiResponseRetrofit,sviDokaziZrtva.dokaziLista, zadaciLista, repo)
    }
    // ispitivanjeOsumnjicenogZadatak
    val ispitivanjeOsumnjicenogZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiIspitivanjeOsumnjicenogZadatak(geminiResponse2, geminiResponseRetrofit, osumnjiceniLista, zadaciLista, repo)
    }
    // ispitivanjeSvedokaZadatak
    val ispitivanjeSvedokaZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiIspitivanjeSvedokaZadatak(geminiResponse2, geminiResponseRetrofit, svedociLista, zadaciLista, repo)
    }
    // telefonZadatak
    val telefonZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiTelefonZadatak(geminiResponse2, geminiResponseRetrofit,sviDokaziZrtva.telefoniLista, zadaciLista, repo)
    }

    // forenzickiDokazZadatak
    val forenzickiDokazZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiForenzickiDokazZadatak(geminiResponse2, geminiResponseRetrofit,sviDokaziZrtva.forenzickiDokaziLista, zadaciLista, repo)

    }
    // cekanje rezultata
    dokazZadatakDeferred.await()
    ispitivanjeOsumnjicenogZadatakDeferred.await()
    ispitivanjeSvedokaZadatakDeferred.await()
    telefonZadatakDeferred.await()
    forenzickiDokazZadatakDeferred.await()
}