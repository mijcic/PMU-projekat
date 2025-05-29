package com.example.service.get

import com.example.models.dto.gemini.GeminiResponseRetrofit
import com.example.repository.Repository

class GeminiMurderService(private val repository: Repository) {
    fun getGeminiMurder(): GeminiResponseRetrofit? {
        println("\nGEMINI MURDER\n")
        val id = repository.getUsedZlocinMurder()
        val zl = id?.let { repository.getZlocin(it) }
        val tipZl= zl?.let { repository.getTipZlocina(it.tipZlocinaId) }
        val zrtva = id?.let { repository.getZrtva(it) }
        val osumnjiceni = id?.let { repository.getOsumnjiceni(it) }
        val dokazi = zrtva?.let { repository.getDokazi(id, it) }
        val telefoni = zrtva?.let { repository.getTelefon(it.idZrtva) }
        val forenzickiDokazi = zrtva?.let { repository.getForenzickiDokazi(it.idZrtva) }
        val obdukcija = zrtva?.let { repository.getObdukcija(it.idZrtva) }
        val svedoci = id?.let { repository.getSvedoci(it) }
        val oneContact = id?.let { repository.getOneContact(it) }
        val kontakti = zrtva?.let { repository.getKontakti(id, it) }
        val poruke = zrtva?.let { repository.getPoruke(id, it,kontakti) }
        val pozivi = zrtva?.let { repository.getPozivi(id, it,kontakti) }
        val galerija = zrtva?.let { repository.getGalerija(id, it) }
        val aplikacija = zrtva?.let { repository.getAplikacije(id, it) }
        val tragovi = zrtva?.let { repository.getTragovi(id, it,forenzickiDokazi,osumnjiceni) }
        val dokazOsumnjiceni = zrtva?.let { repository.getDokaziOsumnjiceni(id, it,dokazi,osumnjiceni) }
        val beleske = zrtva?.let { repository.getBeleske(id, it) }
        val whatsAppKontakti = zrtva?.let { repository.getWhatsAppKontakt(id, it) }
        val whatsAppPoruke = id?.let { repository.getWhatsAppPoruka(it,whatsAppKontakti) }
        val gallery = id?.let { repository.getGallery(it) }
        val odnosOsumnjicenZrtva = id?.let { repository.getOdnosOsumnjicenZrtva(it) }
        val pitanja = id?.let { repository.getPitanja(it) }
        val odgovori = id?.let { repository.getOdgovor(it) }
        val pitanjaIspitivanjeOsumnjicenog = id?.let { repository.getPitanjeIspitivanjeOsumnjicenog(it) }
        val pitanjaIspitivanjeSvedoka = id?.let { repository.getPitanjeIspitivanjeSvedoka(it) }
        val osobe = id?.let { repository.getOsobe(it) }
        val zadaci = id?.let { repository.getZadaci(it) }
        val dokazZadatak = id?.let { repository.getDokaziZadaci(it,zadaci) }
        val ispitivanjeOsumnjicenogZadatak = id?.let { repository.getIspitivanjeOsumnjicenogZadatak(it,zadaci) }
        val ispitivanjeSvedokaZadatak = id?.let { repository.getIspitivanjeSvedokaZadatak(it,zadaci) }
        val telefonZadaci = id?.let { repository.getTelefonZadaci(it, zadaci) }
        val forenzickiDokazZadaci = id?.let { repository.getForenzickiDokazZadatak(it,zadaci) }
        val oneCall = id?.let { repository.getOneCall(it,oneContact) }
        val obicnaPoruka = id?.let { repository.getObicnaPoruka(it,oneContact) }

        val allData = listOf(
            zl, tipZl, zrtva, osumnjiceni, dokazi, telefoni, forenzickiDokazi, obdukcija,
            svedoci, oneContact, kontakti, poruke, pozivi, galerija, aplikacija,
            tragovi, dokazOsumnjiceni, beleske, whatsAppKontakti, whatsAppPoruke,
            gallery, odnosOsumnjicenZrtva, pitanja, odgovori,
            pitanjaIspitivanjeOsumnjicenog, pitanjaIspitivanjeSvedoka, osobe,
            zadaci, dokazZadatak, ispitivanjeOsumnjicenogZadatak,
            ispitivanjeSvedokaZadatak, telefonZadaci, forenzickiDokazZadaci,
            oneCall, obicnaPoruka
        )

        if (allData.any { it == null }) {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }
        else {
            val geminiResponseRetrofit:GeminiResponseRetrofit=GeminiResponseRetrofit(
                zlocinRetrofit = zl,
                zrtvaRetrofit = zrtva,
                osumnjiceniRetrofit = osumnjiceni,
                dokaziRetrofit =dokazi,
                telefoniRetrofit = telefoni,
                forenzickiDokazRetrofit = forenzickiDokazi,
                obdukcijaRetrofit = obdukcija,
                svedociRetrofit =svedoci,
                oneContactRetrofit = oneContact,
                kontaktiRetrofit = kontakti,
                porukeRetrofit = poruke,
                poziviRetrofit = pozivi,
                galerijaRetrofit = galerija,
                aplikacijeRetrofit = aplikacija,
                tragoviRetrofit = tragovi,
                dokaziOsumnjiceniRetrofit = dokazOsumnjiceni,
                beleskeRetrofit = beleske,
                whatsappKontaktRetrofit = whatsAppKontakti,
                whatsappPorukaRetrofit = whatsAppPoruke,
                oneCallRetrofit = oneCall,
                galleryRetrofit = gallery,
                obicnePorukeRetrofit = obicnaPoruka,
                odnosiOsumnjiceniZrtvaRetrofit = odnosOsumnjicenZrtva,
                pitanjaRetrofit = pitanja,
                odgovoriRetrofit = odgovori,
                pitanjeIspitivanjeOsumnjicenogRetrofit = pitanjaIspitivanjeOsumnjicenog,
                pitanjeIspitivanjeSvedokaRetrofit = pitanjaIspitivanjeSvedoka,
                osobeRetrofit = osobe,
                zadaciRetrofit = zadaci,
                dokaziZadaciRetrofit = dokazZadatak,
                ispitivanjeOsumnjicenogZadaciRetrofit = ispitivanjeOsumnjicenogZadatak,
                ispitivanjeSvedokaZadaciRetrofit = ispitivanjeSvedokaZadatak,
                telefonZadaciRetrofit = telefonZadaci,
                forenzickiDokazZadaciRetrofit = forenzickiDokazZadaci
            )
            println("GEMINI MURDER")
            return geminiResponseRetrofit
        }
    }
}