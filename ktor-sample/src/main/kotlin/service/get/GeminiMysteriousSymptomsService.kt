package com.example.service.get

import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofitMysteriousSymptoms
import com.example.models.dto.service.*
import com.example.repository.RepoInterface


/**
 * Service responsible for gathering and assembling all necessary data related to mysterious symptoms
 * for Gemini forensic analysis.
 *
 * This class uses the [RepoInterface] to collect information about the crime, victim, patient,
 * tasks, and other related entities.
 *
 * @property repository Repository providing access to data storage.
 */
class GeminiMysteriousSymptomsService(private val repository: RepoInterface) {

    /**
     * Retrieves all available data for mysterious symptoms and returns a consolidated response object.
     *
     * If any required data is missing (null), logs an error message and returns null.
     *
     * @return [GeminiResponseRetrofitMysteriousSymptoms]? consolidated data object or null if incomplete.
     */
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
        repository.updateUsedZlocinMurder(id)  //POSLE DODaj
        return geminiResponseRetrofit
    }

    /**
     * Loads data about the crime and its type based on the provided ID.
     *
     * @param id Crime identifier.
     * @return [ZlocinDataGeminiRetrofit]? crime data or null if not found.
     */
    private fun loadZlocinDataGeminiRetrofit(id: Int): ZlocinDataGeminiRetrofit? {
        val zl = repository.getZlocin(id) ?: return null
        val tip = repository.getTipZlocina(zl.tipZlocinaId) ?: return null
        return ZlocinDataGeminiRetrofit(zl, tip)
    }


    /**
     * Loads victim-related data including evidence, phones, forensic data, contacts, messages, etc.
     *
     * @param id Crime identifier.
     * @return [ZrtvaMSDataGeminiRetrofit]? victim data or null if missing.
     */
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

    /**
     * Loads patient-related data, including medical reports, tests, investigation locations, and statements.
     *
     * @param id Crime identifier.
     * @param zlocin Crime data object.
     * @param zrtva Victim data object.
     * @return [PacijentMSDataGeminiRetrofit]? patient data or null if incomplete.
     */
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

    /**
     * Loads tasks related to mysterious symptoms.
     *
     * @param id Crime identifier.
     * @return [ZadaciMSDataGeminiRetrofit]? task data or null if missing.
     */
    private fun loadZadaciMSData(id: Int): ZadaciMSDataGeminiRetrofit? {
        val zadaci = repository.getZadaci(id) ?: return null
        return ZadaciMSDataGeminiRetrofit(
            zadaci = zadaci,
            dokaziZadaci = repository.getDokaziZadaci(id, zadaci) ?: return null,
            telefonZadaci = repository.getTelefonZadaci(id, zadaci) ?: return null,
            forenzickiDokazZadaci = repository.getForenzickiDokazZadatak(id, zadaci) ?: return null
        )
    }

    /**
     * Loads other supplementary data related to mysterious symptoms case.
     *
     * @param id Crime identifier.
     * @return [OtherMSDataGeminiRetrofit]? additional data or null if missing.
     */
    private fun loadMSOtherData(id: Int): OtherMSDataGeminiRetrofit? {
        return OtherMSDataGeminiRetrofit(
            gallery = repository.getGallery(id)?: return null,
            pitanja = repository.getPitanja(id)?: return null,
            odgovori = repository.getOdgovor(id)?: return null,
        )
    }
}