package com.example.models.dto

import kotlinx.serialization.Serializable

/**
 * Represents a full collection of data tables used in a standard criminal investigation case.
 * This includes details about the crime, involved individuals, evidence, communication data,
 * task-related information, and various digital traces collected during the investigation.
 *
 * @property zlocinR Core information about the reported crime.
 * @property osobaR All individuals involved in the case (suspects, victims, witnesses, etc.).
 * @property osumnjicenR List of suspects linked to the crime.
 * @property dokazR General evidence found throughout the investigation.
 * @property svedokR List of witnesses who provided statements or testimonies.
 * @property zrtvaR Data about the victim of the crime.
 * @property obdukcijaR Autopsy details for the victim, if applicable.
 * @property forenzickiDokazR Forensic evidence collected (biological, digital, etc.).
 * @property telefonR Phone data related to involved individuals.
 * @property kontaktKtor Contact entries retrieved from suspects’ or victims’ devices.
 * @property porukeKtor Text messages from various apps or sources.
 * @property poziviKtor Phone call records connected to the case.
 * @property galerijaKtor Images and media collected from digital devices.
 * @property aplikacijaKtor Applications installed on relevant devices.
 * @property tragKtor Traces or physical signs of suspect/victim movement or interaction.
 * @property dokazOsumnjicenKtor Evidence specifically tied to suspects.
 * @property oneContactR Extracted contacts from individual communication tools.
 * @property beleskaR Notes or observations made by investigators.
 * @property whatsAppKontaktR WhatsApp contact entries.
 * @property whatsAppPorukaR WhatsApp messages collected as digital evidence.
 * @property oneCallR Records of specific phone calls.
 * @property galleryR Media gallery content linked to the case.
 * @property obicnaPorukaR Standard SMS/MMS messages.
 * @property odnosOsumnjicenZrtvaR Relationships between suspects and victims.
 * @property prijavljeniKorisnikR Users who reported or were linked to the case (e.g. system users).
 * @property pitanjeR General questions used in interrogations or interviews.
 * @property odgovorR Answers corresponding to investigative questions.
 * @property pitanjeIspitivanjeOsumnjicenogR Specific questions for suspect interrogation.
 * @property pitanjeIspitivanjeSvedokaR Specific questions for witness interviews.
 * @property zadatakR List of investigative tasks carried out during the case.
 * @property dokazZadatakR Evidence found or confirmed through investigative tasks.
 * @property ispitivanjeOsumnjicenogZadatakR Task-related suspect interrogations.
 * @property ispitivanjeSvedokaZadatakR Task-related witness interviews.
 * @property telefonZadatakR Phone-related data uncovered during specific tasks.
 * @property forenzickiDokazZadatakR Forensic evidence found during task execution.
 */
@Serializable
data class Tables(
    val zlocinR: ZlocinR,
    val osobaR: List<OsobaR>,
    //val motivR: List<MotivR>,
    val osumnjicenR: List<OsumnjicenR>,
    val dokazR: List<DokazR>,
    //val dokazZadatakR: DokazZadatakR,
    val svedokR: List<SvedokR>,
    val zrtvaR: ZrtvaR,
    val obdukcijaR: ObdukcijaR,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    //val dokazOsumnjicenR: List<DokazOsumnjicenR>,
    //val zadatakR: ZadatakR,
    //val alibiR: List<AlibiR>,
    val kontaktKtor: List<KontaktKtor>,
    val porukeKtor: List<PorukeKtor>,
    val poziviKtor: List<PoziviKtor>,
    val galerijaKtor: List<GalerijaKtor>,
    val aplikacijaKtor: List<AplikacijaKtor>,
    val tragKtor: List<TragKtor>,
    val dokazOsumnjicenKtor: List<DokazOsumnjicenKtor>,
    val oneContactR: List<OneContactR>,
    val beleskaR: List<BeleskaR>,
    val whatsAppKontaktR: List<WhatsAppKontaktR>,
    val whatsAppPorukaR: List<WhatsAppPorukaR>,
    val oneCallR: List<OneCallR>,
    val galleryR: List<GalleryR>,
    val obicnaPorukaR: List<ObicnaPorukaR>,
    val odnosOsumnjicenZrtvaR: List<OdnosOsumnjicenZrtvaR>,
    val prijavljeniKorisnikR: List<PrijavljeniKorisnikR>,
    val pitanjeR: List<PitanjeR>,
    val odgovorR: List<OdgovorR>,
    val pitanjeIspitivanjeOsumnjicenogR: List<PitanjeIspitivanjeOsumnjicenogR>,
    val pitanjeIspitivanjeSvedokaR: List<PitanjeIspitivanjeSvedokaR>,
    val zadatakR: List<ZadatakR>,
    val dokazZadatakR: List<DokazZadatakR>,
    val ispitivanjeOsumnjicenogZadatakR: List<IspitivanjeOsumnjicenogZadatakR>,
    val ispitivanjeSvedokaZadatakR: List<IspitivanjeSvedokaZadatakR>,
    val telefonZadatakR: List<TelefonZadatakR>,
    val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,
    //val porukeZadatakR: List<PorukeZadatakR>
)