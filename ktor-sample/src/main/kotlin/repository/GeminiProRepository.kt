package com.example.repository

import com.example.models.dto.*
import com.example.models.dto.gemini.retrofit.GeminiResponse2
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit
import com.example.models.interfaces.GeminiResponseCommon2
import com.example.models.interfaces.GeminiResponseRetrofitCommon

/**
 * Interface for inserting and processing data obtained from Gemini AI responses
 * into various domain-specific data models related to crime investigation.
 */
interface GeminiProRepository{

    /**
     * Inserts victim-related data into the repository.
     *
     * @param geminiResponse2 Gemini AI standard response containing victim data.
     * @param geminiResponseRetrofit Parsed Retrofit response.
     * @param timestamp Timestamp when the data was received or processed.
     * @param zl The crime data to associate the victim with.
     * @param repo The repository used for insertion operations.
     * @return A container with all evidence and related data extracted from the victim.
     */
    fun insertGeminiZrtva(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, timestamp:Long, zl: ZlocinData, repo:RepositoryInsert): SviDokaziOdZrtve

    /**
     * Inserts generic evidence related to a victim.
     *
     * @param geminiResponse2 Common Gemini response containing evidence.
     * @param geminiResponseRetrofit Common Retrofit response.
     * @param zl Associated crime.
     * @param zrtva Victim data.
     * @param repo Repository for storing evidence.
     * @return A list of inserted evidence.
     */
    fun insertGeminiDokaz(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData, zrtva: ZrtvaData, repo:RepositoryInsert): MutableList<DokazData>

    /**
     * Inserts phone records linked to a victim.
     *
     * @param geminiResponse2 Common Gemini response with phone data.
     * @param geminiResponseRetrofit Common Retrofit response.
     * @param zrtva Victim.
     * @param repo Repository for storing phone data.
     * @return A list of inserted phone records.
     */
    fun insertGeminiTelefon(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zrtva: ZrtvaData,repo: RepositoryInsert): MutableList<TelefonData>

    /**
     * Inserts forensic evidence related to a victim.
     *
     * @param geminiResponse2 Common Gemini response with forensic data.
     * @param geminiResponseRetrofit Common Retrofit response.
     * @param zrtva Victim.
     * @param repo Repository for storing forensic evidence.
     * @return A list of forensic evidence inserted.
     */
    fun insertGeminiForenzickiDokaz(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zrtva: ZrtvaData,repo: RepositoryInsert): MutableList<ForenzickiDokazData>

    /**
     * Inserts autopsy report and details.
     *
     * @param geminiResponse2 Gemini response with autopsy information.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zl Associated crime.
     * @param zrtva Victim.
     * @param timestamp Time when the data was processed.
     * @param repo Repository to insert data.
     */
    fun insertGeminiObdukcija(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zl: ZlocinData, zrtva: ZrtvaData, timestamp: Long, repo: RepositoryInsert)

    /**
     * Inserts suspect data related to a specific crime.
     *
     * @param geminiResponse2 Gemini response with suspect data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param timestamp Timestamp of operation.
     * @param zl Crime data.
     * @param repo Repository for data storage.
     * @return List of suspects inserted.
     */
    fun insertGeminiOsumnjicen(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, timestamp:Long, zl: ZlocinData, repo: RepositoryInsert): MutableList<OsumnjicenData>

    /**
     * Inserts witness data associated with a crime.
     *
     * @param geminiResponse2 Gemini response containing witness details.
     * @param geminiResponseRetrofit Retrofit-compatible response.
     * @param timestamp The time the operation is performed.
     * @param zl The crime the witness is associated with.
     * @param repo Repository for data insertion.
     * @return List of inserted witnesses.
     */
    fun insertGeminiSvedok(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, timestamp:Long, zl: ZlocinData, repo: RepositoryInsert): MutableList<SvedokData>

    /**
     * Inserts a list of simplified contact information.
     *
     * @param geminiResponse2 Gemini response with basic contact data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zl Related crime.
     * @param repo Repository to persist the contact information.
     * @return List of inserted basic contacts.
     */
    fun insertGeminiOneContact(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData,repo: RepositoryInsert): MutableList<OneContactData>

    /**
     * Inserts victim's contact list.
     *
     * @param geminiResponse2 Gemini response with contact data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zrtva Victim whose contacts are being saved.
     * @param repo Repository handler.
     * @return List of contacts associated with the victim.
     */
    fun insertGeminiKontakt(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zrtva: ZrtvaData, repo: RepositoryInsert):MutableList<KontaktData>

    /**
     * Inserts SMS or regular messages.
     *
     * @param geminiResponse2 Gemini response with message content.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zrtva Victim data.
     * @param kontaktLista List of known contacts.
     * @param timestamp Time of processing.
     * @param repo Repository used for saving messages.
     */
    fun insertGeminiPoruke(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zrtva: ZrtvaData, kontaktLista: MutableList<KontaktData>, timestamp: Long, repo: RepositoryInsert)

    /**
     * Inserts call log data for a victim.
     *
     * @param geminiResponse2 Gemini response with call details.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zrtva Victim data.
     * @param kontaktLista Contact list to associate with calls.
     * @param timestamp Time of processing.
     * @param repo Repository for storing call records.
     */
    fun insertGeminiPozivi(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zrtva: ZrtvaData, kontaktLista: MutableList<KontaktData>, timestamp: Long, repo: RepositoryInsert)

    /**
     * Inserts gallery/media data associated with a victim.
     *
     * @param geminiResponse2 Gemini response containing gallery info.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zrtva Victim associated with gallery items.
     * @param timestamp Timestamp of operation.
     * @param repo Repository for storing gallery content.
     */
    fun insertGeminiGalerija(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zrtva: ZrtvaData, timestamp: Long, repo: RepositoryInsert)

    /**
     * Inserts data about installed or used applications by the victim.
     *
     * @param geminiResponse2 Gemini response with application data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zrtva Victim whose application data is saved.
     * @param repo Repository for storing the data.
     */
    fun insertGeminiAplikacija(geminiResponse2: GeminiResponseCommon2,geminiResponseRetrofit: GeminiResponseRetrofitCommon,zrtva: ZrtvaData, repo: RepositoryInsert)

    /**
     * Links forensic traces to specific suspects.
     *
     * @param geminiResponse2 Gemini response with trace data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param forenzickiDokaz List of forensic evidence.
     * @param osumnjicen List of suspects.
     * @param repo Repository for linking data.
     */
    fun insertGeminiTrag(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, forenzickiDokaz: MutableList<ForenzickiDokazData>, osumnjicen: MutableList<OsumnjicenData>, repo: RepositoryInsert)

    /**
     * Associates evidence with specific suspects.
     *
     * @param geminiResponse2 Gemini response with evidence.
     * @param geminiResponseRetrofit Retrofit response.
     * @param dokazi Evidence data.
     * @param osumnjicen Suspect list.
     * @param repo Repository used for linking.
     */
    fun insertGeminiDokazOsumnjicen(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, dokazi: MutableList<DokazData>, osumnjicen: MutableList<OsumnjicenData>, repo: RepositoryInsert)

    /**
     * Inserts notes or memos associated with the investigation.
     *
     * @param geminiResponse2 Gemini response with note data.
     * @param geminiResponseRetrofit Retrofit-compatible response.
     * @param zl Associated crime.
     * @param timestamp Timestamp of note creation.
     * @param repo Repository for note storage.
     */
    fun insertGeminiBeleska(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData, timestamp: Long, repo: RepositoryInsert)

    /**
     * Inserts WhatsApp contact data.
     *
     * @param geminiResponse2 Gemini response with WhatsApp contact info.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zl Associated crime.
     * @param repo Repository for WhatsApp contact data.
     * @return List of inserted WhatsApp contacts.
     */
    fun insertGeminiWhatsAppKontakt(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,zl: ZlocinData, repo: RepositoryInsert): MutableList<WhatsAppKontaktData>

    /**
     * Inserts WhatsApp message data.
     *
     * @param geminiResponse2 Gemini response with WhatsApp messages.
     * @param geminiResponseRetrofit Retrofit response.
     * @param kontaktiLista List of WhatsApp contacts.
     * @param timestamp Time of operation.
     * @param repo Repository for storing messages.
     */
    fun insertGeminiWhatsAppPoruka(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, kontaktiLista: MutableList<WhatsAppKontaktData>, timestamp: Long,repo: RepositoryInsert)

    /**
     * Inserts a record of a phone call linked to a basic contact.
     *
     * @param geminiResponse2 Gemini response with call information.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zrtvaData Victim data.
     * @param kontaktiLista List of OneContactData.
     * @param timestamp Processing timestamp.
     * @param repo Repository to save the call.
     */
    fun insertGeminiOneCall(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zrtvaData: ZrtvaData, kontaktiLista: MutableList<OneContactData>, timestamp: Long,repo: RepositoryInsert)

    /**
     * Inserts general gallery or media data.
     *
     * @param geminiResponse2 Gemini response with gallery data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zl Associated crime.
     * @param timestamp Timestamp of insertion.
     * @param repo Repository interface for saving data.
     */
    fun insertGeminiGallery(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData, timestamp: Long,repo: RepositoryInsert)

    /**
     * Inserts standard text message (non-WhatsApp).
     *
     * @param geminiResponse2 Gemini response with message content.
     * @param geminiResponseRetrofit Retrofit-compatible response.
     * @param kontaktiLista Contacts associated with messages.
     * @param timestamp Processing timestamp.
     * @param repo Repository handler.
     */
    fun insertGeminiObicnaPoruka(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, kontaktiLista: MutableList<OneContactData>, timestamp: Long,repo: RepositoryInsert)

    /**
     * Links suspects with victims based on Gemini data.
     *
     * @param geminiResponse2 Gemini response with relationship data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param osumnjicenLista List of suspects.
     * @param zrtva Victim.
     * @param repo Repository interface for storing the relationship.
     */
    fun insertGeminiOdnosOsumnjicenZrtva(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, osumnjicenLista: MutableList<OsumnjicenData>, zrtva: ZrtvaData, repo: RepositoryInsert)

    /**
     * Inserts the registered user information.
     *
     * @param geminiResponse2 Gemini response with user data.
     * @param repo Repository handler for user data.
     */
    fun insertGeminiPrijavljeniKorisnik(geminiResponse2: GeminiResponse2, repo: RepositoryInsert)

    /**
     * Inserts interrogation questions.
     *
     * @param geminiResponse2 Gemini response with question data.
     * @param geminiResponseRetrofit Retrofit-compatible response.
     * @param zl Related crime.
     * @param repo Repository for saving questions.
     * @return List of inserted questions.
     */
    fun insertGeminiPitanje(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData,repo: RepositoryInsert): MutableList<PitanjeData>

    /**
     * Inserts answers to previously added questions.
     *
     * @param geminiResponse2 Gemini response with answers.
     * @param geminiResponseRetrofit Retrofit response.
     * @param pitanjeLista List of questions answered.
     * @param repo Repository for storing the answers.
     */
    fun insertGeminiOdgovor(geminiResponse2: GeminiResponseCommon2,geminiResponseRetrofit: GeminiResponseRetrofitCommon, pitanjeLista: MutableList<PitanjeData>,repo: RepositoryInsert)

    /**
     * Inserts interrogation questions targeting suspects.
     *
     * @param geminiResponse2 Gemini response with question data.
     * @param geminiResponseRetrofit Retrofit-compatible response.
     * @param osumnjiceniLista List of suspects.
     * @param repo Repository handler.
     */
    fun insertGeminiPitanjeIspitivanjeOsumnjicenog(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, osumnjiceniLista: MutableList<OsumnjicenData>, repo: RepositoryInsert)

    /**
     * Inserts interrogation questions targeting witnesses.
     *
     * @param geminiResponse2 Gemini response.
     * @param geminiResponseRetrofit Retrofit-compatible response.
     * @param svedociLista List of witnesses.
     * @param repo Repository handler.
     */
    fun insertGeminiPitanjeIspitivanjeSvedoka(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, svedociLista: MutableList<SvedokData>, repo: RepositoryInsert)

    /**
     * Inserts generic person data associated with a crime.
     *
     * @param geminiResponse2 Gemini response with person data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zlocin Associated crime.
     * @param timestamp Time of insertion.
     * @param repo Repository interface.
     */
    fun insertGeminiOsoba(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zlocin: ZlocinData, timestamp: Long,repo: RepositoryInsert)

    /**
     * Inserts tasks related to the investigation.
     *
     * @param geminiResponse2 Gemini response with task data.
     * @param zlocin Crime the tasks are related to.
     * @param repo Repository for saving the tasks.
     * @return List of inserted tasks.
     */
    fun insertGeminiZadatak(geminiResponse2: GeminiResponse2, zlocin: ZlocinData, repo: RepositoryInsert): MutableList<ZadatakData>

    /**
     * Updates the list of existing tasks.
     *
     * @param geminiResponse2 Gemini response with task updates.
     * @param geminiResponseRetrofit Retrofit response.
     * @param zlocin Associated crime.
     * @param repo Repository for updating the tasks.
     */
    fun updateGeminiZadatakList(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, zlocin: ZlocinData, repo: RepositoryInsert)

    /**
     * Links evidence items to specific tasks.
     *
     * @param geminiResponse2 Gemini response.
     * @param geminiResponseRetrofit Retrofit-compatible response.
     * @param dokazList Evidence items.
     * @param zadatakList Related tasks.
     * @param repo Repository for saving links.
     */
    fun insertGeminiDokazZadatak(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, dokazList: MutableList<DokazData>, zadatakList: MutableList<ZadatakData>,repo: RepositoryInsert)

    /**
     * Links suspect interrogations to specific tasks.
     *
     * @param geminiResponse2 Gemini response with interrogation data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param osumnjicenList Suspects being interrogated.
     * @param zadatakList Associated tasks.
     * @param repo Repository for saving links.
     */
    fun insertGeminiIspitivanjeOsumnjicenogZadatak(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, osumnjicenList: MutableList<OsumnjicenData>, zadatakList: MutableList<ZadatakData>, repo: RepositoryInsert)

    /**
     * Links witness interrogations to tasks.
     *
     * @param geminiResponse2 Gemini response.
     * @param geminiResponseRetrofit Retrofit response.
     * @param svedokList Witness list.
     * @param zadatakList Task list.
     * @param repo Repository handler.
     */
    fun insertGeminiIspitivanjeSvedokaZadatak(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, svedokList: MutableList<SvedokData>, zadatakList: MutableList<ZadatakData>, repo: RepositoryInsert)

    /**
     * Links phone data to tasks.
     *
     * @param geminiResponse2 Gemini response.
     * @param geminiResponseRetrofit Retrofit response.
     * @param telefonList Phones to associate.
     * @param zadatakList Tasks to associate with.
     * @param repo Repository.
     */
    fun insertGeminiTelefonZadatak(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, telefonList: MutableList<TelefonData>, zadatakList: MutableList<ZadatakData>,repo: RepositoryInsert)

    /**
     * Links forensic evidence to tasks.
     *
     * @param geminiResponse2 Gemini response with forensic data.
     * @param geminiResponseRetrofit Retrofit response.
     * @param forenzickiDokazList List of forensic items.
     * @param zadatakList Tasks to associate with.
     * @param repo Repository.
     */
    fun insertGeminiForenzickiDokazZadatak(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, forenzickiDokazList: MutableList<ForenzickiDokazData>, zadatakList: MutableList<ZadatakData>,repo: RepositoryInsert)

    //fun insertGeminiPorukeZadatak(geminiResponse2: GeminiResponse2, porukeList: MutableList<PorukeData>, zadatakList: MutableList<ZadatakData>)

    /**
     * Suspended function that inserts contact data including both WhatsApp contacts and regular contacts
     * associated with a specific victim and crime.
     *
     * @param whatsAppKontaktiLista List of WhatsApp contacts to be inserted.
     * @param geminiResponse2 Gemini response containing raw contact data.
     * @param geminiResponseRetrofit Retrofit-compatible Gemini response.
     * @param timestamp Time of insertion.
     * @param kontaktiLista List of basic contacts to be inserted.
     * @param zl Crime data to associate with the contacts.
     * @param zrtva Victim to whom the contacts are related.
     * @param repo Repository used to persist the data.
     */
    suspend fun suspendInsertKontakti(
        whatsAppKontaktiLista: MutableList<WhatsAppKontaktData>,
        geminiResponse2: GeminiResponse2,
        geminiResponseRetrofit: GeminiResponseRetrofit,
        timestamp: Long,
        kontaktiLista: MutableList<OneContactData>,
        zl: ZlocinData,
        zrtva: ZrtvaData,
        repo: RepositoryInsert
    )

    /**
     * Suspended function that inserts a set of interrogation questions associated with suspects and witnesses
     * for a given crime.
     *
     * @param pitanjaLista List of questions to be inserted.
     * @param geminiResponse2 Gemini response containing question data.
     * @param geminiResponseRetrofit Retrofit-compatible Gemini response.
     * @param timestamp Time of insertion.
     * @param osumnjiceniLista List of suspects the questions target.
     * @param svedociLista List of witnesses the questions may involve.
     * @param zl Crime associated with the questions.
     * @param repo Repository used to persist the questions.
     */
    suspend fun suspendInsertPitanja(
        pitanjaLista: MutableList<PitanjeData>,
        geminiResponse2: GeminiResponse2,
        geminiResponseRetrofit: GeminiResponseRetrofit,
        timestamp: Long,
        osumnjiceniLista: MutableList<OsumnjicenData>,
        svedociLista: MutableList<SvedokData>,
        zl: ZlocinData,
        repo: RepositoryInsert
    )

    /**
     * Suspended function that inserts a list of tasks (ZadatakData) and associates them with relevant
     * suspects, witnesses, and victim evidence.
     *
     * @param zadaciLista List of tasks to insert.
     * @param geminiResponse2 Gemini response containing task-related data.
     * @param geminiResponseRetrofit Retrofit-compatible Gemini response.
     * @param osumnjiceniLista List of suspects to link with tasks.
     * @param svedociLista List of witnesses to link with tasks.
     * @param sviDokaziZrtva All evidence data collected from the victim.
     * @param repo Repository used for inserting and linking tasks.
     */
    suspend fun suspendInsertZadaci(
        zadaciLista: MutableList<ZadatakData>,
        geminiResponse2: GeminiResponse2,
        geminiResponseRetrofit: GeminiResponseRetrofit,
        osumnjiceniLista: MutableList<OsumnjicenData>,
        svedociLista: MutableList<SvedokData>,
        sviDokaziZrtva:SviDokaziOdZrtve,
        repo: RepositoryInsert
    )
}