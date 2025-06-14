package com.example.repository

import com.example.models.dto.*

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

    fun getTipZlocina(id:Int): TipZlocinaDC?

    fun getZrtva(id:Int): ZrtvaData?

    /**
     * Retrieves all suspects for a given crime.
     *
     * @param id The ID of the crime.
     * @return A list of [OsumnjicenData], or `null` if none found.
     */
    fun getOsumnjiceni(id: Int): List<OsumnjicenData>?

    fun getDokazi(id: Int,zr: ZrtvaData): List<DokazData>?

    fun getTelefon(id: Int): List<TelefonData>?

    /**
     * Retrieves forensic evidence for the given crime ID.
     *
     * @param id The crime ID.
     * @return A list of [ForenzickiDokazData], or `null` if not available.
     */
    fun getForenzickiDokazi(id: Int): List<ForenzickiDokazData>?

    fun getObdukcija(id: Int): ObdukcijaData?

    fun getSvedoci(id: Int): List<SvedokData>?

    fun getOneContact(id: Int): List<OneContactData>?

    fun getKontakti(id: Int,zr: ZrtvaData): List<KontaktData>?

    fun getPoruke(id: Int, zr: ZrtvaData, kontakti: List<KontaktData>?): List<PorukeData>?

    fun getPozivi(id: Int, zr: ZrtvaData, kontakti: List<KontaktData>?): List<PoziviData>?

    fun getGalerija(id: Int,zr: ZrtvaData): List<GalerijaData>?

    fun getAplikacije(id: Int,zr: ZrtvaData): List<AplikacijaData>?

    fun getTragovi(forenzickiDokazi: List<ForenzickiDokazData>?, osumnjiceni: List<OsumnjicenData>?): List<TragData>?

    fun getDokaziOsumnjiceni(dokazi: List<DokazData>?, osumnjiceni: List<OsumnjicenData>?): List<DokazOsumnjicenData>?

    fun getBeleske(id: Int,zr: ZrtvaData): List<BeleskaData>?

    fun getWhatsAppKontakt(id: Int,zr: ZrtvaData): List<WhatsAppKontaktData>?

    fun getWhatsAppPoruka(id: Int, whatsAppKontakti : List<WhatsAppKontaktData>?): List<WhatsAppPorukaData>?

    fun getGallery(id: Int): List<GalleryData>?

    fun getOdnosOsumnjicenZrtva(id: Int): List<OdnosOsumnjicenZrtvaData>?

    fun getPitanja(id: Int): List<PitanjeData>?

    fun getOdgovor(id: Int): List<OdgovorData>?

    fun getPitanjeIspitivanjeOsumnjicenog(id: Int): List<PitanjeIspitivanjeOsumnjicenogData>?

    fun getPitanjeIspitivanjeSvedoka(id: Int): List<PitanjeIspitivanjeSvedokaData>?

    fun getOsobe(id: Int): List<OsobaData>?

    fun getZadaci(id: Int): List<ZadatakData>?

    fun getDokaziZadaci(id: Int, zadaci: List<ZadatakData>?): List<DokazZadatakData>?

    fun getIspitivanjeOsumnjicenogZadatak(id: Int, zadaci: List<ZadatakData>?): List<IspitivanjeOsumnjicenogZadatakData>?

    fun getIspitivanjeSvedokaZadatak(id: Int,zadaci: List<ZadatakData>?): List<IspitivanjeSvedokaZadatakData>?

    fun getTelefonZadaci(id: Int,zadaci: List<ZadatakData>?): List<TelefonZadatakData>?

    fun getForenzickiDokazZadatak(id: Int,zadaci: List<ZadatakData>?): List<ForenzickiDokazZadatakData>?

    fun getOneCall(id: Int,oneContact: List<OneContactData>?): List<OneCallData>?

    fun getObicnaPoruka(id: Int,oneContact: List<OneContactData>?): List<ObicnaPorukaData>?

    fun getPacijent(id: Int, zl: ZlocinData, zr: ZrtvaData, osobe: List<OsobaData>): PacijentData?

    fun getMedicinskiIzvetaj(pacijent: PacijentData?): MedicinskiIzvestajData?

    fun getLekarskiTest(pacijent: PacijentData?): LekarskiTestData?

    fun getLokacijeIstrage(id: Int): List<LokacijeIstrageData>?

    fun getIzjavaZaPacijenta(pacijent: PacijentData, osobe: List<OsobaData>?): IzjavaZaPacijentaData?
}
