package com.example.service

import com.example.*
import com.example.repository.Repository

class GeminiMysteriousSymptomsService(repository: Repository) {
    fun getGeminiMysteriousSymtoms(): GeminiResponseRetrofitMysteriousSymptoms {
        val id = getUsedZlocinMysteriousSymptoms()
        val zl = id?.let { getZlocin(it) }
        val zrtva = id?.let { getZrtva(it) }
        val dokazi = zrtva?.let { getDokazi(id, it) }
        val telefoni = zrtva?.let { getTelefon(it.idZrtva) }
        val forenzickiDokazi = zrtva?.let { getForenzickiDokazi(it.idZrtva) }
        val oneContact = id?.let { getOneContact(it) }
        val aplikacija = zrtva?.let { getAplikacije(id, it) }
        val beleske = zrtva?.let { getBeleske(id, it) }
        val whatsAppKontakti = zrtva?.let { getWhatsAppKontakt(id, it) }
        val whatsAppPoruke = id?.let { getWhatsAppPoruka(it,whatsAppKontakti) }
        val gallery = id?.let { getGallery(it) }
        val pitanja = id?.let { getPitanja(it) }
        val odgovori = id?.let { getOdgovor(it) }
        val osobe = id?.let { getOsobe(it) }
        val zadaci = id?.let { getZadaci(it) }
        val dokazZadatak = id?.let { getDokaziZadaci(it,zadaci) }
        val telefonZadaci = id?.let { getTelefonZadaci(it, zadaci) }
        val forenzickiDokazZadaci = id?.let { getForenzickiDokazZadatak(it,zadaci) }
        val oneCall = id?.let { getOneCall(it,oneContact) }
        val obicnaPoruka = id?.let { getObicnaPoruka(it,oneContact) }
        var pacijent: PacijentData? = null
        if (zrtva != null && osobe != null && zl!=null) {
            pacijent= getPacijent(id, zl,zrtva,osobe)
        }
        val medicinskiIzvestaj = id?.let { getMedicinskiIzvetaj(it,pacijent) }
        val lekarskiTest = id?.let { getLekarskiTest(it,pacijent) }
        val lokacijeIstrage = id?.let { getLokacijeIstrage(it) }
        val izjavaZaPacijenta = pacijent?.let { getIzjavaZaPacijenta(it,osobe) }

        val geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms = GeminiResponseRetrofitMysteriousSymptoms(
            zlocinRetrofit = zl,
            dokaziRetrofit = dokazi,
            telefoniRetrofit = telefoni,
            forenzickiDokazRetrofit = forenzickiDokazi,
            oneContactRetrofit = oneContact,
            aplikacijeRetrofit = aplikacija,
            beleskeRetrofit = beleske,
            whatsappKontaktRetrofit = whatsAppKontakti,
            whatsappPorukaRetrofit = whatsAppPoruke,
            oneCallRetrofit = oneCall,
            galleryRetrofit = gallery,
            obicnePorukeRetrofit = obicnaPoruka,
            pitanjaRetrofit = pitanja,
            odgovoriRetrofit = odgovori,
            osobeRetrofit = osobe,
            zadaciRetrofit = zadaci,
            dokaziZadaciRetrofit = dokazZadatak,
            telefonZadaciRetrofit = telefonZadaci,
            forenzickiDokazZadaciRetrofit = forenzickiDokazZadaci,
            pacijentRetrofit = pacijent,
            medicinskiIzvestajRetrofit = medicinskiIzvestaj,
            lekarskiTestRetrofit = lekarskiTest,
            lokacijeIstrageRetrofit = lokacijeIstrage,
            izjavaZaPacijentaRetrofit = izjavaZaPacijenta
        )

        println("\nGEMINI MS\n")
        return geminiResponseRetrofit
    }
}