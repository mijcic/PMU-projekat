package com.example.service.get

import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofit
import com.example.data.remote.generic.PitanjeIspitivanjeOsumnjicenogR
import com.example.data.remote.service.*
import com.example.data.remote.tables.PitanjeIspitivanjeOsumnjicenogData
import com.example.data.remote.tables.PitanjeIspitivanjeSvedokaData
import com.example.repository.RepoInterface

/**
 * Service responsible for collecting and assembling all necessary data related to a "murder" crime
 * for the purpose of Gemini forensic analysis.
 *
 * This class uses `RepoInterface` to gather information about the victim, suspects,
 * the crime, witnesses, traces, evidence, tasks, relationships, and other related entities.
 *
 * @property repository Repository providing access to data from the database
 */
class GeminiMurderService(private val repository: RepoInterface) {
    /**
     * Collects all available data for a murder case and returns a consolidated result
     * as a [GeminiResponseRetrofit] object.
     *
     * If any required data is unavailable (e.g., `null`), the function logs a message and returns `null`.
     *
     * @return A [GeminiResponseRetrofit] object containing all relevant data, or `null` if any part is missing.
     */
    fun getGeminiMurder(): GeminiResponseRetrofit? {
        val id = repository.getUsedZlocinMurder() ?: run {
            println("Neki podaci su null — provera nije prošla. getUsedZlocinMurder")
            return null
        }
        println(id)

        val zlocinData = loadZlocinDataGeminiRetrofit(id) ?: run {
            println("Neki podaci su null — provera nije prošla. loadZlocinDataGeminiRetrofit")
            return null
        }

        val zrtvaData = loadZrtvaDataGeminiRetrofit(id)?: run {
            println("Neki podaci su null — provera nije prošla. loadZrtvaDataGeminiRetrofit")
            return null
        }

        val osumnjiceniData = loadOsumnjiceniDataGeminiRetrofit(id,zrtvaData) ?: run {
            println("Neki podaci su null — provera nije prošla. loadOsumnjiceniDataGeminiRetrofit")
            return null
        }

        val zadaciData = loadZadaciData(id) ?: run {
            println("Neki podaci su null — provera nije prošla. loadZadaciData")
            return null
        }

        val otherData = loadOtherData(id,osumnjiceniData, zrtvaData) ?: run {
            println("Neki podaci su null — provera nije prošla. loadOtherData")
            return null
        }

        val geminiResponseRetrofit: GeminiResponseRetrofit = GeminiResponseRetrofit(
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
        repository.updateUsedZlocinMurder(id)
        return geminiResponseRetrofit
    }

    fun getGeminiMurderbezupdateUsedZlocinMurder(): GeminiResponseRetrofit? {
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

        val otherData = loadOtherData(id,osumnjiceniData,zrtvaData) ?: run {
            println("Neki podaci su null — provera nije prošla.")
            return null
        }

        val geminiResponseRetrofit: GeminiResponseRetrofit = GeminiResponseRetrofit(
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
        //repository.updateUsedZlocinMurder(id)
        return geminiResponseRetrofit
    }


    /**
     * Loads data about the crime and its type (crime category).
     *
     * @param id Crime ID
     * @return A [ZlocinDataGeminiRetrofit] object with crime details, or `null` if data can't be loaded
     */
    private fun loadZlocinDataGeminiRetrofit(id: Int): ZlocinDataGeminiRetrofit? {
        val zl = repository.getZlocin(id) ?: return null
        val tip = repository.getTipZlocina(zl.tipZlocinaId) ?: return null
        return ZlocinDataGeminiRetrofit(zl, tip)
    }

    /**
     * Loads data about the victim, including contacts, messages, phones, forensics, and more.
     *
     * @param id Crime ID
     * @return A [ZrtvaDataGeminiRetrofit] object with all victim-related information, or `null` if something is missing
     */
    private fun loadZrtvaDataGeminiRetrofit(id: Int): ZrtvaDataGeminiRetrofit? {
        println("ovde "+id)
        val zrtva = repository.getZrtva(id) ?: return null
        val kontakti = repository.getKontakti(id, zrtva) ?: return null
        val whatsappKontakti = repository.getWhatsAppKontakt(id, zrtva) ?: return null
        val oneContact =repository.getOneContact(id) ?: return null
        val z = ZrtvaDataGeminiRetrofit(
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
        println(z)
        return z
    }

    /**
     * Loads data about suspects, including traces and evidence related to them.
     *
     * @param id Crime ID
     * @param zrtvaData Victim data, needed to correlate with suspects
     * @return An [OsumnjiceniDataGeminiRetrofit] object with suspect data, or `null` if something is missing
     */
    private fun loadOsumnjiceniDataGeminiRetrofit(id: Int,zrtvaData: ZrtvaDataGeminiRetrofit): OsumnjiceniDataGeminiRetrofit? {
        val osumnjiceni = repository.getOsumnjiceni(id) ?: return null
        //if (osumnjiceni.isEmpty()) return null

        val forenzika = zrtvaData.forenzika
        return OsumnjiceniDataGeminiRetrofit(
            osumnjiceni = osumnjiceni ?:return null,
            tragovi = repository.getTragovi(forenzika, osumnjiceni) ?: return null,
            dokaziOsumnjiceni = repository.getDokaziOsumnjiceni(zrtvaData.dokazi, osumnjiceni) ?: return null
        )
    }

    /**
     * Loads all investigation-related tasks, including forensic and other relevant tasks.
     *
     * @param id Crime ID
     * @return A [ZadaciDataGeminiRetrofit] object with tasks and related data, or `null` if something is missing
     */
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

    /**
     * Loads additional case information such as galleries, relationships, questions, and answers.
     *
     * @param id Crime ID
     * @return An [OtherDataGeminiRetrofit] object with supplementary case data, or `null` if something is missing
     */
    private fun loadOtherData(id: Int, osumnjiceniDataGeminiRetrofit: OsumnjiceniDataGeminiRetrofit, zrtvaData: ZrtvaDataGeminiRetrofit): OtherDataGeminiRetrofit? {
        val lista = mutableListOf<PitanjeIspitivanjeOsumnjicenogData>()

        for (o in osumnjiceniDataGeminiRetrofit.osumnjiceni) {
            val pitanja = repository.getPitanjeIspitivanjeOsumnjicenog(o.idOsumnjicen)
            if (pitanja != null) {
                lista.addAll(pitanja)
            }
        }

        val listaS = mutableListOf<PitanjeIspitivanjeSvedokaData>()

        for (s in zrtvaData.svedoci!!) {
            val pitanjaS = repository.getPitanjeIspitivanjeSvedoka(s.idSvedok)
            if (pitanjaS != null) {
                listaS.addAll(pitanjaS)
            }
        }


        return OtherDataGeminiRetrofit(
            gallery = repository.getGallery(id)?: return null,
            odnosi = repository.getOdnosOsumnjicenZrtva(id)?: return null,
            pitanja = repository.getPitanja(id)?: return null,
            odgovori = repository.getOdgovor(id)?: return null,
            pitanjaIspitivanjeOsumnjicenog = lista,
            pitanjaIspitivanjeSvedoka = listaS,
            osobe = repository.getOsobe(id)?: return null
        )
    }
}