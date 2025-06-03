package com.example.service.get

import com.example.models.dto.OsumnjicenData
import com.example.models.dto.gemini.GeminiResponseRetrofit
import com.example.models.dto.service.*
import com.example.repository.RepoInterface
import com.example.repository.Repository

class GeminiMurderService(private val repository: RepoInterface) {
    fun getGeminiMurder(): GeminiResponseRetrofit? {
        println("\nGEMINI MURDER\n")

        val id = repository.getUsedZlocinMurder() ?: run {
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

        val osumnjiceniData = loadOsumnjiceniDataGeminiRetrofit(id,zrtvaData) ?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }
        val zadaciData = loadZadaciData(id) ?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }

        val otherData = loadOtherData(id) ?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }

        val geminiResponseRetrofit:GeminiResponseRetrofit=GeminiResponseRetrofit(
            zlocinRetrofit = zlocinData.zlocin,
            zrtvaRetrofit = zrtvaData.zrtva,
            osumnjiceniRetrofit = osumnjiceniData.osumnjiceni,
            dokaziRetrofit =zrtvaData.dokazi,
            telefoniRetrofit = zrtvaData.telefoni,
            forenzickiDokazRetrofit = zrtvaData.forenzika,
            obdukcijaRetrofit = zrtvaData.obdukcija,
            svedociRetrofit =zrtvaData.svedoci,
            oneContactRetrofit = zrtvaData.oneContact,
            kontaktiRetrofit = zrtvaData.kontakti,
            porukeRetrofit = zrtvaData.poruke,
            poziviRetrofit = zrtvaData.pozivi,
            galerijaRetrofit = zrtvaData.galerija,
            aplikacijeRetrofit = zrtvaData.aplikacije,
            tragoviRetrofit = osumnjiceniData.tragovi,
            dokaziOsumnjiceniRetrofit = osumnjiceniData.dokaziOsumnjiceni,
            beleskeRetrofit = zrtvaData.beleske,
            whatsappKontaktRetrofit = zrtvaData.whatsappKontakti,
            whatsappPorukaRetrofit = zrtvaData.whatsappPoruke,
            oneCallRetrofit = zrtvaData.oneCall,
            galleryRetrofit = otherData.gallery,
            obicnePorukeRetrofit = zrtvaData.obicnaPoruka,
            odnosiOsumnjiceniZrtvaRetrofit = otherData.odnosi,
            pitanjaRetrofit = otherData.pitanja,
            odgovoriRetrofit = otherData.odgovori,
            pitanjeIspitivanjeOsumnjicenogRetrofit = otherData.pitanjaIspitivanjeOsumnjicenog,
            pitanjeIspitivanjeSvedokaRetrofit = otherData.pitanjaIspitivanjeSvedoka,
            osobeRetrofit = otherData.osobe,
            zadaciRetrofit = zadaciData.zadaci,
            dokaziZadaciRetrofit = zadaciData.dokaziZadaci,
            ispitivanjeOsumnjicenogZadaciRetrofit = zadaciData.ispitivanjeOsumnjicenogZadaci,
            ispitivanjeSvedokaZadaciRetrofit = zadaciData.ispitivanjeSvedokaZadaci,
            telefonZadaciRetrofit = zadaciData.telefonZadaci,
            forenzickiDokazZadaciRetrofit = zadaciData.forenzickiDokazZadaci
        )
        println("GEMINI MURDER")
        return geminiResponseRetrofit
    }


    private fun loadZlocinDataGeminiRetrofit(id: Int): ZlocinDataGeminiRetrofit? {
        val zl = repository.getZlocin(id) ?: return null
        val tip = repository.getTipZlocina(zl.tipZlocinaId) ?: return null
        return ZlocinDataGeminiRetrofit(zl, tip)
    }

    private fun loadZrtvaDataGeminiRetrofit(id: Int): ZrtvaDataGeminiRetrofit? {
        val zrtva = repository.getZrtva(id) ?: return null
        val kontakti = repository.getKontakti(id, zrtva) ?: return null
        val whatsappKontakti = repository.getWhatsAppKontakt(id, zrtva) ?: return null
        val oneContact =repository.getOneContact(id) ?: return null
        return ZrtvaDataGeminiRetrofit(
            zrtva = zrtva,
            dokazi = repository.getDokazi(id, zrtva) ?: return null,
            telefoni = repository.getTelefon(zrtva.idZrtva) ?: return null,
            forenzika = repository.getForenzickiDokazi(zrtva.idZrtva) ?: return null,
            obdukcija = repository.getObdukcija(zrtva.idZrtva) ?: return null,
            svedoci = repository.getSvedoci(id) ?: return null,
            oneContact = oneContact,
            kontakti = kontakti,
            poruke = repository.getPoruke(id, zrtva, kontakti) ?: return null,
            pozivi = repository.getPozivi(id, zrtva, kontakti) ?: return null,
            galerija = repository.getGalerija(id, zrtva) ?: return null,
            aplikacije = repository.getAplikacije(id, zrtva) ?: return null,
            beleske = repository.getBeleske(id, zrtva) ?: return null,
            whatsappKontakti = whatsappKontakti,
            whatsappPoruke = repository.getWhatsAppPoruka(id, whatsappKontakti) ?: return null,
            oneCall = repository.getOneCall(id, oneContact) ?: return null,
            obicnaPoruka = repository.getObicnaPoruka(id, oneContact) ?: return null
        )
    }

    private fun loadOsumnjiceniDataGeminiRetrofit(id: Int,zrtvaData:ZrtvaDataGeminiRetrofit): OsumnjiceniDataGeminiRetrofit? {
        val osumnjiceni = repository.getOsumnjiceni(id) ?: return null
        //if (osumnjiceni.isEmpty()) return null

        val forenzika = zrtvaData.forenzika
        return OsumnjiceniDataGeminiRetrofit(
            osumnjiceni = osumnjiceni ?:return null,
            tragovi = repository.getTragovi(forenzika, osumnjiceni) ?: return null,
            dokaziOsumnjiceni = repository.getDokaziOsumnjiceni(zrtvaData.dokazi, osumnjiceni) ?: return null
        )
    }

    private fun loadZadaciData(id: Int): ZadaciDataGeminiRetrofit? {
        val zadaci = repository.getZadaci(id) ?: return null
        return ZadaciDataGeminiRetrofit(
            zadaci = zadaci,
            dokaziZadaci = repository.getDokaziZadaci(id, zadaci) ?: return null,
            ispitivanjeOsumnjicenogZadaci = repository.getIspitivanjeOsumnjicenogZadatak(id, zadaci) ?: return null,
            ispitivanjeSvedokaZadaci = repository.getIspitivanjeSvedokaZadatak(id, zadaci) ?: return null,
            telefonZadaci = repository.getTelefonZadaci(id, zadaci) ?: return null,
            forenzickiDokazZadaci = repository.getForenzickiDokazZadatak(id, zadaci) ?: return null
        )
    }

    private fun loadOtherData(id: Int): OtherDataGeminiRetrofit? {
        return OtherDataGeminiRetrofit(
            gallery = repository.getGallery(id)?: return null,
            odnosi = repository.getOdnosOsumnjicenZrtva(id)?: return null,
            pitanja = repository.getPitanja(id)?: return null,
            odgovori = repository.getOdgovor(id)?: return null,
            pitanjaIspitivanjeOsumnjicenog = repository.getPitanjeIspitivanjeOsumnjicenog(id)?: return null,
            pitanjaIspitivanjeSvedoka = repository.getPitanjeIspitivanjeSvedoka(id)?: return null,
            osobe = repository.getOsobe(id)?: return null
        )
    }
}