package com.example.service.get

import com.example.*
import com.example.models.dto.gemini.GeminiResponseRetrofitMysteriousSymptoms
import com.example.models.dto.service.*
import com.example.repository.RepoInterface

class GeminiMysteriousSymptomsService(private val repository: RepoInterface) {
    fun getGeminiMysteriousSymtoms(): GeminiResponseRetrofitMysteriousSymptoms? {
        val id = repository.getUsedZlocinMysteriousSymptoms() ?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }

        val zlocinData = loadZlocinDataGeminiRetrofit(id) ?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }

        val zrtvaData = loadZrtvaDataGeminiRetrofit(id)?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }

        val pacijentData = loadPacijentDataGeminiRetrofit(id,zlocinData,zrtvaData)?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }

        val zadaciData = loadZadaciMSData(id) ?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }

        val otherData = loadMSOtherData(id) ?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }

        val geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms = GeminiResponseRetrofitMysteriousSymptoms(
            zlocinRetrofit = zlocinData.zlocin,
            dokaziRetrofit = zrtvaData.dokazi,
            telefoniRetrofit = zrtvaData.telefoni,
            forenzickiDokazRetrofit = zrtvaData.forenzika,
            oneContactRetrofit = zrtvaData.oneContact,
            aplikacijeRetrofit = zrtvaData.aplikacije,
            beleskeRetrofit = zrtvaData.beleske,
            whatsappKontaktRetrofit = zrtvaData.whatsappKontakti,
            whatsappPorukaRetrofit = zrtvaData.whatsappPoruke,
            oneCallRetrofit = zrtvaData.oneCall,
            galleryRetrofit = otherData.gallery,
            obicnePorukeRetrofit = zrtvaData.obicnaPoruka,
            pitanjaRetrofit = otherData.pitanja,
            odgovoriRetrofit = otherData.odgovori,
            osobeRetrofit = pacijentData.osobe,
            zadaciRetrofit = zadaciData.zadaci,
            dokaziZadaciRetrofit = zadaciData.dokaziZadaci,
            telefonZadaciRetrofit = zadaciData.telefonZadaci,
            forenzickiDokazZadaciRetrofit = zadaciData.forenzickiDokazZadaci,
            pacijentRetrofit = pacijentData.pacijent,
            medicinskiIzvestajRetrofit = pacijentData.medicinskiIzvestaj,
            lekarskiTestRetrofit = pacijentData.lekarskiTest,
            lokacijeIstrageRetrofit = pacijentData.lokacijeIstrage,
            izjavaZaPacijentaRetrofit = pacijentData.izjavaZaPacijenta
        )

        return geminiResponseRetrofit
    }

    private fun loadZlocinDataGeminiRetrofit(id: Int): ZlocinDataGeminiRetrofit? {
        val zl = repository.getZlocin(id) ?: return null
        val tip = repository.getTipZlocina(zl.tipZlocinaId) ?: return null
        return ZlocinDataGeminiRetrofit(zl, tip)
    }

    private fun loadZrtvaDataGeminiRetrofit(id: Int): ZrtvaMSDataGeminiRetrofit? {
        val zrtva = repository.getZrtva(id) ?: return null
        val whatsappKontakti = repository.getWhatsAppKontakt(id, zrtva) ?: return null
        val oneContact = repository.getOneContact(id) ?: return null


        return ZrtvaMSDataGeminiRetrofit(
            zrtva = zrtva,
            dokazi = repository.getDokazi(id, zrtva) ?: return null,
            telefoni = repository.getTelefon(zrtva.idZrtva) ?: return null,
            forenzika = repository.getForenzickiDokazi(zrtva.idZrtva) ?: return null,
            oneContact = oneContact,
            galerija = repository.getGalerija(id, zrtva) ?: return null,
            aplikacije = repository.getAplikacije(id, zrtva) ?: return null,
            beleske = repository.getBeleske(id, zrtva) ?: return null,
            whatsappKontakti = whatsappKontakti,
            whatsappPoruke = repository.getWhatsAppPoruka(id, whatsappKontakti) ?: return null,
            oneCall = repository.getOneCall(id, oneContact) ?: return null,
            obicnaPoruka = repository.getObicnaPoruka(id, oneContact) ?: return null,
        )
    }

    private fun loadPacijentDataGeminiRetrofit(id: Int,zlocin:ZlocinDataGeminiRetrofit,zrtva:ZrtvaMSDataGeminiRetrofit): PacijentMSDataGeminiRetrofit? {
        val osobe = repository.getOsobe(id)?: return null
        val pacijent = repository.getPacijent(id, zlocin.zlocin, zrtva.zrtva, osobe) ?: return null

        return PacijentMSDataGeminiRetrofit(
            pacijent = pacijent,
            medicinskiIzvestaj = repository.getMedicinskiIzvetaj(pacijent) ?: return null,
            lekarskiTest = repository.getLekarskiTest(pacijent) ?: return null,
            lokacijeIstrage = repository.getLokacijeIstrage(id) ?: return null,
            izjavaZaPacijenta = repository.getIzjavaZaPacijenta(pacijent, osobe) ?: return null,
            osobe = osobe
        )
    }

    private fun loadZadaciMSData(id: Int): ZadaciMSDataGeminiRetrofit? {
        val zadaci = repository.getZadaci(id) ?: return null
        return ZadaciMSDataGeminiRetrofit(
            zadaci = zadaci,
            dokaziZadaci = repository.getDokaziZadaci(id, zadaci) ?: return null,
            telefonZadaci = repository.getTelefonZadaci(id, zadaci) ?: return null,
            forenzickiDokazZadaci = repository.getForenzickiDokazZadatak(id, zadaci) ?: return null
        )
    }

    private fun loadMSOtherData(id: Int): OtherMSDataGeminiRetrofit? {
        return OtherMSDataGeminiRetrofit(
            gallery = repository.getGallery(id)?: return null,
            pitanja = repository.getPitanja(id)?: return null,
            odgovori = repository.getOdgovor(id)?: return null,
        )
    }
}