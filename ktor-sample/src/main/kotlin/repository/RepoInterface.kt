package com.example.repository

import com.example.data.remote.tables.*

/**
 * Repository interface for accessing structured forensic and criminal case data from the database.
 *
 * This interface defines all necessary methods to retrieve entities and their relationships
 * for use in various layers of the application (e.g., Gemini AI, API responses, case reconstruction).
 *
 * Each method retrieves a specific domain model or related data for a given investigation or victim.
 *
 * ### Usage
 * This interface is implemented by a concrete class that performs actual database operations,
 * typically using JDBC or an ORM.
 */
interface RepoInterface {
    /**
     * Delete story from database.
     *
     * @param id The ID of the crime record to be marked as used.
     * @return `true` if the update affected at least one row, `false` otherwise.
     *
     * @throws SQLException if a database access error occurs.
     */
    fun deleteStoryById(id:Int):Boolean
    /**
     * Updates the `used` flag of a crime record in the `usedzlocin` table to indicate that it has been used.
     *
     * This method sets the `used` column to `1` for the record with the specified `id` in the `idUsedZlocin` column.
     *
     * @param id The ID of the crime record to be marked as used.
     * @return `true` if the update affected at least one row, `false` otherwise.
     *
     * @throws SQLException if a database access error occurs.
     */
    fun updateUsedZlocinMurder(id: Int): Boolean
    /**
     * Returns the ID of the most recently used crime for the "Murder" investigation.
     *
     * @return The ID of the last used murder-related crime, or `null` if not found.
     */
    fun getUsedZlocinMurder(): Int?

    /**
     * Returns the ID of the most recently used crime for the "Mysterious Symptoms" investigation.
     *
     * @return The ID of the last used mysterious symptoms case, or `null` if not found.
     */
    fun getUsedZlocinMysteriousSymptoms(): Int?

    /**
     * Retrieves a crime (zlocin) by its ID.
     *
     * @param zlocinId The unique ID of the crime.
     * @return A [ZlocinData] object if found, or `null` if not.
     */
    fun getZlocin(zlocinId:Int): ZlocinData?

    /**
     * Retrieves a list of crimes (zlocin).
     *
     * @return A [List<ZlocinData>] object if found, or `null` if not.
     */
    fun getAllZlocin(): List<ZlocinData>?


    /**
     * Retrieves the crime type by ID.
     *
     * @param id The type ID.
     * @return A [TipZlocinaDC] object or `null` if not found.
     */
    fun getTipZlocina(id:Int): TipZlocinaDC?

    /**
     * Retrieves the victim's data.
     *
     * @param id The ID of the victim.
     * @return A [ZrtvaData] object or `null` if not found.
     */
    fun getZrtva(id:Int): ZrtvaData?

    /**
     * Retrieves all suspects for a given crime.
     *
     * @param id The ID of the crime.
     * @return A list of [OsumnjicenData], or `null` if none found.
     */
    fun getOsumnjiceni(id: Int): List<OsumnjicenData>?

    /**
     * Retrieves all evidence items related to the crime and victim.
     *
     * @param id The crime ID.
     * @param zr The victim data.
     * @return A list of [DokazData], or `null` if none found.
     */
    fun getDokazi(id: Int,zr: ZrtvaData): List<DokazData>?

    /**
     * Retrieves all phone data associated with the given victim ID.
     *
     * @param id The ID of the victim.
     * @return A list of [TelefonData], or `null` if none is found.
     */
    fun getTelefon(id: Int): List<TelefonData>?

    /**
     * Retrieves all forensic evidence data associated with the given victim ID.
     *
     * @param id The ID of the victim.
     * @return A list of [ForenzickiDokazData], or `null` if none found.
     */
    fun getForenzickiDokazi(id: Int): List<ForenzickiDokazData>?

    /**
     * Retrieves autopsy data for the victim.
     *
     * @param id The ID of the victim.
     * @return An [ObdukcijaData] object or `null` if not available.
     */
    fun getObdukcija(id: Int): ObdukcijaData?

    /**
     * Retrieves witness data for the crime.
     *
     * @param id The crime ID.
     * @return A list of [SvedokData], or `null` if none found.
     */
    fun getSvedoci(id: Int): List<SvedokData>?

    /**
     * Retrieves one-on-one contact data.
     *
     * @param id The crime ID.
     * @return A list of [OneContactData], or `null` if none found.
     */
    fun getOneContact(id: Int): List<OneContactData>?

    /**
     * Retrieves contact data related to the victim.
     *
     * @param id The ID of the victim.
     * @param zr The victim data.
     * @return A list of [KontaktData], or `null` if none found.
     */
    fun getKontakti(id: Int,zr: ZrtvaData): List<KontaktData>?

    /**
     * Retrieves message data from the victim’s contacts.
     *
     * @param id The ID of the victim.
     * @param zr The victim data.
     * @param kontakti The list of victim’s contacts.
     * @return A list of [PorukeData], or `null` if none found.
     */
    fun getPoruke(id: Int, zr: ZrtvaData, kontakti: List<KontaktData>?): List<PorukeData>?

    /**
     * Retrieves call logs from the victim’s phone.
     *
     * @param id The ID of the victim.
     * @param zr The victim data.
     * @param kontakti The list of victim’s contacts.
     * @return A list of [PoziviData], or `null` if none found.
     */
    fun getPozivi(id: Int, zr: ZrtvaData, kontakti: List<KontaktData>?): List<PoziviData>?

    /**
     * Retrieves all images from the victim's device gallery.
     *
     * @param id The ID of the victim.
     * @param zr The victim associated with the gallery.
     * @return A list of [GalerijaData] items, or `null` if no images are found.
     */
    fun getGalerija(id: Int,zr: ZrtvaData): List<GalerijaData>?

    /**
     * Retrieves a list of applications installed on the victim's device.
     *
     * @param id The ID of the victim.
     * @param zr The victim whose device is being analyzed.
     * @return A list of [AplikacijaData], or `null` if no apps are found.
     */
    fun getAplikacije(id: Int,zr: ZrtvaData): List<AplikacijaData>?


    /**
     * Matches forensic traces to potential suspects based on collected data.
     *
     * @param forenzickiDokazi A list of forensic evidence items.
     * @param osumnjiceni A list of suspects.
     * @return A list of [TragData] representing trace evidence linked to suspects, or `null` if no matches exist.
     */
    fun getTragovi(forenzickiDokazi: List<ForenzickiDokazData>?, osumnjiceni: List<OsumnjicenData>?): List<TragData>?

    /**
     * Matches general evidence items to suspects involved in the case.
     *
     * @param dokazi A list of general evidence items.
     * @param osumnjiceni A list of suspects.
     * @return A list of [DokazOsumnjicenData] indicating matched evidence, or `null` if no matches are found.
     */
    fun getDokaziOsumnjiceni(dokazi: List<DokazData>?, osumnjiceni: List<OsumnjicenData>?): List<DokazOsumnjicenData>?

    /**
     * Retrieves personal notes written or stored by the victim.
     *
     * @param id The ID of the crime.
     * @param zr The victim whose notes are being retrieved.
     * @return A list of [BeleskaData], or `null` if none are found.
     */
    fun getBeleske(id: Int,zr: ZrtvaData): List<BeleskaData>?

    /**
     * Retrieves WhatsApp contacts associated with the victim.
     *
     * @param id The ID of the crime.
     * @param zr The victim whose WhatsApp contacts are being analyzed.
     * @return A list of [WhatsAppKontaktData], or `null` if none are found.
     */
    fun getWhatsAppKontakt(id: Int,zr: ZrtvaData): List<WhatsAppKontaktData>?

    /**
     * Retrieves WhatsApp messages linked to the victim's contacts.
     *
     * @param id The ID of the crime.
     * @param whatsAppKontakti A list of WhatsApp contacts.
     * @return A list of [WhatsAppPorukaData], or `null` if no messages are found.
     */
    fun getWhatsAppPoruka(id: Int, whatsAppKontakti : List<WhatsAppKontaktData>?): List<WhatsAppPorukaData>?

    /**
     * Retrieves gallery content unrelated to the victim's main device (e.g. backups or other sources).
     *
     * @param id The ID of the crime.
     * @return A list of [GalleryData] items, or `null` if no additional gallery content is available.
     */
    fun getGallery(id: Int): List<GalleryData>?

    /**
     * Retrieves the relationships between suspects and the victim.
     *
     * @param id The ID of the victim.
     * @return A list of [OdnosOsumnjicenZrtvaData], or `null` if no relationships are recorded.
     */
    fun getOdnosOsumnjicenZrtva(id: Int): List<OdnosOsumnjicenZrtvaData>?

    /**
     * Retrieves all predefined questions used during the investigation process.
     *
     * @param id The ID of the crime.
     * @return A list of [PitanjeData], or `null` if none exist.
     */
    fun getPitanja(id: Int): List<PitanjeData>?

    /**
     * Retrieves predefined answers that may correspond to investigation questions.
     *
     * @param id The ID of the crime.
     * @return A list of [OdgovorData], or `null` if none exist.
     */
    fun getOdgovor(id: Int): List<OdgovorData>?

    /**
     * Retrieves a list of questions used to interrogate a specific suspect.
     *
     * @param id The suspect's ID.
     * @return A list of [PitanjeIspitivanjeOsumnjicenogData], or `null` if none are found.
     */
    fun getPitanjeIspitivanjeOsumnjicenog(id: Int): List<PitanjeIspitivanjeOsumnjicenogData>?

    /**
     * Retrieves a list of questions used to interrogate a specific witness.
     *
     * @param id The witness's ID.
     * @return A list of [PitanjeIspitivanjeSvedokaData], or `null` if none are found.
     */
    fun getPitanjeIspitivanjeSvedoka(id: Int): List<PitanjeIspitivanjeSvedokaData>?

    /**
     * Retrieves all persons involved in the case.
     *
     * @param id The ID of the crime.
     * @return A list of [OsobaData], or `null` if none are involved.
     */
    fun getOsobe(id: Int): List<OsobaData>?

    /**
     * Retrieves all tasks assigned during the investigation.
     *
     * @param id The ID of the crime.
     * @return A list of [ZadatakData], or `null` if no tasks are assigned.
     */
    fun getZadaci(id: Int): List<ZadatakData>?

    /**
     * Retrieves evidence items associated with specific tasks.
     *
     * @param id The ID of the crime.
     * @param zadaci The list of tasks to search against.
     * @return A list of [DokazZadatakData], or `null` if no matches are found.
     */
    fun getDokaziZadaci(id: Int, zadaci: List<ZadatakData>?): List<DokazZadatakData>?

    /**
     * Retrieves suspect interrogation records associated with specific tasks.
     *
     * @param id The ID of the crime.
     * @param zadaci The list of related tasks.
     * @return A list of [IspitivanjeOsumnjicenogZadatakData], or `null` if not available.
     */
    fun getIspitivanjeOsumnjicenogZadatak(id: Int, zadaci: List<ZadatakData>?): List<IspitivanjeOsumnjicenogZadatakData>?

    /**
     * Retrieves witness interrogation records associated with specific tasks.
     *
     * @param id The ID of the crime.
     * @param zadaci The list of related tasks.
     * @return A list of [IspitivanjeSvedokaZadatakData], or `null` if not available.
     */
    fun getIspitivanjeSvedokaZadatak(id: Int,zadaci: List<ZadatakData>?): List<IspitivanjeSvedokaZadatakData>?

    /**
     * Retrieves phone logs associated with investigation tasks.
     *
     * @param id The witness's ID.
     * @param zadaci The list of tasks.
     * @return A list of [TelefonZadatakData], or `null` if not available.
     */
    fun getTelefonZadaci(id: Int,zadaci: List<ZadatakData>?): List<TelefonZadatakData>?

    /**
     * Retrieves forensic evidence linked to investigation tasks.
     *
     * @param id The witness's ID.
     * @param zadaci The list of tasks.
     * @return A list of [ForenzickiDokazZadatakData], or `null` if not available.
     */
    fun getForenzickiDokazZadatak(id: Int,zadaci: List<ZadatakData>?): List<ForenzickiDokazZadatakData>?

    /**
     * Retrieves call records from individual (1:1) contacts.
     *
     * @param id The ID of the crime.
     * @param oneContact A list of individual contacts.
     * @return A list of [OneCallData], or `null` if no call data is available.
     */
    fun getOneCall(id: Int,oneContact: List<OneContactData>?): List<OneCallData>?

    /**
     * Retrieves messages from individual (1:1) contacts.
     *
     * @param id The ID of the crime.
     * @param oneContact A list of individual contacts.
     * @return A list of [ObicnaPorukaData], or `null` if no messages are found.
     */
    fun getObicnaPoruka(id: Int,oneContact: List<OneContactData>?): List<ObicnaPorukaData>?

    /**
     * Retrieves patient data.
     *
     * @param id The ID of the crime.
     * @param zl The crime data.
     * @param zr The victim data.
     * @param osobe A list of involved persons.
     * @return A [PacijentData] object, or `null` if not applicable.
     */
    fun getPacijent(id: Int, zl: ZlocinData, zr: ZrtvaData, osobe: List<OsobaData>): PacijentData?

    /**
     * Retrieves the medical report associated with a specific patient.
     *
     * @param pacijent The patient for whom the medical report is being requested. May be `null`.
     * @return A [MedicinskiIzvestajData] object containing medical findings, or `null` if no report exists.
     */
    fun getMedicinskiIzvetaj(pacijent: PacijentData?): MedicinskiIzvestajData?

    /**
     * Retrieves the results of medical tests performed on the specified patient.
     *
     * @param pacijent The patient whose test results are being retrieved. May be `null`.
     * @return A [LekarskiTestData] object containing test information, or `null` if no results are found.
     */
    fun getLekarskiTest(pacijent: PacijentData?): LekarskiTestData?

    /**
     * Retrieves all locations involved in the investigation of the specified case.
     *
     * @param id The ID of the crime or investigation.
     * @return A list of [LokacijeIstrageData], or `null` if no locations are available.
     */
    fun getLokacijeIstrage(id: Int): List<LokacijeIstrageData>?

    /**
     * Retrieves a formal statement related to the patient, provided by or about the patient.
     *
     * @param pacijent The patient for whom the statement is being retrieved.
     * @param osobe A list of related persons who may have contributed to the statement. May be `null`.
     * @return An [IzjavaZaPacijentaData] object, or `null` if no statement is found.
     */
    fun getIzjavaZaPacijenta(pacijent: PacijentData, osobe: List<OsobaData>?): IzjavaZaPacijentaData?

    /**
     * Retrieves all user scores from the database.
     *
     * This function returns a list of [ScoreKorisnikaRequest] objects representing
     * the users and their respective scores. If there are no scores available, an empty list is returned.
     *
     * @return A list of [ScoreKorisnikaRequest] containing all user scores, or an empty list if none exist.
     */
    fun getAllScores(): List<ScoreKorisnikaRequest>?

    /**
     * Retrieves all registered users from the database.
     *
     * This function returns a list of [Korisnik] objects representing
     * all users stored in the system. If there are no users in the database,
     * an empty list is returned.
     *
     * @return A list of [Korisnik] containing all users, or an empty list if none exist.
     */
    fun getAllUsers(): List<Korisnik>?
}