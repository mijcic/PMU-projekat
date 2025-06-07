package com.example.service.get

import com.example.*
import com.example.models.dto.PacijentData
import com.example.models.dto.gemini.GeminiResponseRetrofitMysteriousSymptoms
import com.example.repository.Repository

class GeminiMysteriousSymptomsService(private val repository: Repository) {
    fun getGeminiMysteriousSymtoms(): GeminiResponseRetrofitMysteriousSymptoms? {
        println("MysteriousSymptoms")
        val id = repository.getUsedZlocinMysteriousSymptoms()
        val zl = id?.let { repository.getZlocin(it) }
        val zrtva = id?.let { repository.getZrtva(it) }
        val dokazi = zrtva?.let { repository.getDokazi(id, it) }
        val telefoni = zrtva?.let { repository.getTelefon(it.idZrtva) }
        val forenzickiDokazi = zrtva?.let { repository.getForenzickiDokazi(it.idZrtva) }
        val oneContact = id?.let { repository.getOneContact(it) }
        val aplikacija = zrtva?.let { repository.getAplikacije(id, it) }
        val beleske = zrtva?.let { repository.getBeleske(id, it) }
        val whatsAppKontakti = zrtva?.let { repository.getWhatsAppKontakt(id, it) }
        val whatsAppPoruke = id?.let { repository.getWhatsAppPoruka(it,whatsAppKontakti) }
        val gallery = id?.let { repository.getGallery(it) }
        val pitanja = id?.let { repository.getPitanja(it) }
        val odgovori = id?.let { repository.getOdgovor(it) }
        val osobe = id?.let { repository.getOsobe(it) }
        val zadaci = id?.let { repository.getZadaci(it) }
        val dokazZadatak = id?.let { repository.getDokaziZadaci(it,zadaci) }
        val telefonZadaci = id?.let { repository.getTelefonZadaci(it, zadaci) }
        val forenzickiDokazZadaci = id?.let { repository.getForenzickiDokazZadatak(it,zadaci) }
        val oneCall = id?.let { repository.getOneCall(it,oneContact) }
        val obicnaPoruka = id?.let { repository.getObicnaPoruka(it,oneContact) }
        var pacijent: PacijentData? = null
        if (zrtva != null && osobe != null && zl!=null) {
            pacijent= repository.getPacijent(id, zl,zrtva,osobe)
        }
        val medicinskiIzvestaj = id?.let { repository.getMedicinskiIzvetaj(pacijent) }
        val lekarskiTest = id?.let { repository.getLekarskiTest(pacijent) }
        val lokacijeIstrage = id?.let { repository.getLokacijeIstrage(it) }
        val izjavaZaPacijenta = pacijent?.let { repository.getIzjavaZaPacijenta(it,osobe) }

        val allData = listOf(
            id, zl, zrtva, dokazi, telefoni, forenzickiDokazi, oneContact, aplikacija,
            beleske, whatsAppKontakti, whatsAppPoruke, gallery, pitanja, odgovori,
            osobe, zadaci, dokazZadatak, telefonZadaci, forenzickiDokazZadaci,
            oneCall, obicnaPoruka, pacijent, medicinskiIzvestaj, lekarskiTest,
            lokacijeIstrage, izjavaZaPacijenta
        )

        if (allData.any { it == null }) {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }
        else {
            val geminiResponseRetrofit:GeminiResponseRetrofitMysteriousSymptoms= GeminiResponseRetrofitMysteriousSymptoms(
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
}