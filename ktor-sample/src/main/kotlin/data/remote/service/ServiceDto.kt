package com.example.data.remote.service

import com.example.data.remote.tables.*

/**
 * Contains crime data along with its classified crime type.
 *
 * @property zlocin Core data about the crime.
 * @property tip The type/category of the crime.
 */
data class ZlocinDataGeminiRetrofit(
    val zlocin: ZlocinData,
    val tip: TipZlocinaDC
)

/**
 * Wraps all victim-related information gathered during the investigation.
 *
 * @property zrtva Core data about the victim.
 * @property dokazi List of evidence connected to the victim.
 * @property telefoni Phones associated with the victim.
 * @property forenzika Forensic findings related to the victim.
 * @property obdukcija Autopsy data, if available.
 * @property svedoci Witnesses related to the victim.
 * @property oneContact Individual contacts from the victim’s devices.
 * @property kontakti Full contact lists.
 * @property poruke Text messages (e.g., SMS) related to the victim.
 * @property pozivi Call logs related to the victim.
 * @property galerija Media files or images found on the victim’s devices.
 * @property aplikacije Apps installed on devices linked to the victim.
 * @property beleske Notes found or written about the victim.
 * @property whatsappKontakti WhatsApp contacts from the victim’s phone.
 * @property whatsappPoruke WhatsApp messages involving the victim.
 * @property oneCall Specific phone calls linked to the victim.
 * @property obicnaPoruka Standard text messages (SMS/MMS).
 */
data class ZrtvaDataGeminiRetrofit(
    val zrtva: ZrtvaData,
    val dokazi: List<DokazData>?,
    val telefoni: List<TelefonData>?,
    val forenzika: List<ForenzickiDokazData>?,
    val obdukcija: ObdukcijaData?,
    val svedoci: List<SvedokData>?,
    val oneContact: List<OneContactData>?,
    val kontakti: List<KontaktData>?,
    val poruke: List<PorukeData>?,
    val pozivi: List<PoziviData>?,
    val galerija: List<GalerijaData>?,
    val aplikacije: List<AplikacijaData>?,
    val beleske: List<BeleskaData>?,
    val whatsappKontakti: List<WhatsAppKontaktData>?,
    val whatsappPoruke: List<WhatsAppPorukaData>?,
    val oneCall: List<OneCallData>?,
    val obicnaPoruka: List<ObicnaPorukaData>?
)

/**
 * Holds suspect-related data and digital evidence.
 *
 * @property osumnjiceni List of suspects in the case.
 * @property tragovi Digital or physical traces linked to the suspects.
 * @property dokaziOsumnjiceni Evidence specifically associated with suspects.
 */
data class OsumnjiceniDataGeminiRetrofit(
    val osumnjiceni: List<OsumnjicenData>,
    val tragovi: List<TragData>?,
    val dokaziOsumnjiceni: List<DokazOsumnjicenData>?
)

/**
 * Groups investigation tasks and all task-specific data.
 *
 * @property zadaci List of tasks assigned during the investigation.
 * @property dokaziZadaci Evidence connected to those tasks.
 * @property ispitivanjeOsumnjicenogZadaci Interrogations of suspects as part of task execution.
 * @property ispitivanjeSvedokaZadaci Interrogations of witnesses as part of task execution.
 * @property telefonZadaci Phones investigated in the context of a task.
 * @property forenzickiDokazZadaci Forensic data linked to tasks.
 */
data class ZadaciDataGeminiRetrofit(
    val zadaci: List<ZadatakData>,
    val dokaziZadaci: List<DokazZadatakData>?,
    val ispitivanjeOsumnjicenogZadaci: List<IspitivanjeOsumnjicenogZadatakData>?,
    val ispitivanjeSvedokaZadaci: List<IspitivanjeSvedokaZadatakData>?,
    val telefonZadaci: List<TelefonZadatakData>?,
    val forenzickiDokazZadaci: List<ForenzickiDokazZadatakData>?
)

/**
 * Represents additional miscellaneous data linked to the case.
 *
 * @property gallery Image gallery entries not tied to a specific person.
 * @property odnosi Relationships between suspects and victims.
 * @property pitanja Generic or global questions asked in the case.
 * @property odgovori Answers to the above questions.
 * @property pitanjaIspitivanjeOsumnjicenog Specific questions for suspects.
 * @property pitanjaIspitivanjeSvedoka Specific questions for witnesses.
 * @property osobe List of individuals involved in the case.
 */
data class OtherDataGeminiRetrofit(
    val gallery: List<GalleryData>?,
    val odnosi: List<OdnosOsumnjicenZrtvaData>?,
    val pitanja: List<PitanjeData>?,
    val odgovori: List<OdgovorData>?,
    val pitanjaIspitivanjeOsumnjicenog: List<PitanjeIspitivanjeOsumnjicenogData>?,
    val pitanjaIspitivanjeSvedoka: List<PitanjeIspitivanjeSvedokaData>?,
    val osobe: List<OsobaData>?
)

/**
 * Victim-specific data structure for cases involving mysterious symptoms.
 *
 * @property zrtva Core victim information.
 * @property dokazi Evidence linked to the victim.
 * @property telefoni Phones associated with the victim.
 * @property forenzika Forensic evidence relevant to the victim.
 * @property oneContact Individual contacts.
 * @property galerija Images and media files linked to the victim.
 * @property aplikacije Installed applications found on victim's devices.
 * @property beleske Notes related to the victim or their symptoms.
 * @property whatsappKontakti WhatsApp contact list.
 * @property whatsappPoruke WhatsApp message logs.
 * @property oneCall Specific call records.
 * @property obicnaPoruka Standard text message logs.
 */
data class ZrtvaMSDataGeminiRetrofit(
    val zrtva: ZrtvaData,
    val dokazi: List<DokazData>?,
    val telefoni: List<TelefonData>?,
    val forenzika: List<ForenzickiDokazData>?,
    val oneContact: List<OneContactData>?,
    val galerija: List<GalerijaData>?,
    val aplikacije: List<AplikacijaData>?,
    val beleske: List<BeleskaData>?,
    val whatsappKontakti: List<WhatsAppKontaktData>?,
    val whatsappPoruke: List<WhatsAppPorukaData>?,
    val oneCall: List<OneCallData>?,
    val obicnaPoruka: List<ObicnaPorukaData>?,
)

/**
 * Data related to a patient in a case involving mysterious symptoms.
 *
 * @property pacijent Basic information about the patient.
 * @property medicinskiIzvestaj Medical report summarizing condition and symptoms.
 * @property lekarskiTest Results of any medical tests conducted.
 * @property lokacijeIstrage Locations investigated for medical or contamination clues.
 * @property izjavaZaPacijenta Formal statement or interview about the patient.
 * @property osobe People related to the patient’s case.
 */
data class PacijentMSDataGeminiRetrofit(
    val pacijent: PacijentData,
    val medicinskiIzvestaj: MedicinskiIzvestajData?,
    val lekarskiTest: LekarskiTestData?,
    val lokacijeIstrage: List<LokacijeIstrageData>?,
    val izjavaZaPacijenta: IzjavaZaPacijentaData?,
    val osobe: List<OsobaData>?
)

/**
 * Investigation tasks and associated data specific to mysterious symptoms cases.
 *
 * @property zadaci List of investigation tasks.
 * @property dokaziZadaci Evidence linked to those tasks.
 * @property telefonZadaci Phones analyzed as part of the tasks.
 * @property forenzickiDokazZadaci Forensic evidence obtained through task execution.
 */
data class ZadaciMSDataGeminiRetrofit(
    val zadaci: List<ZadatakData>,
    val dokaziZadaci: List<DokazZadatakData>?,
    val telefonZadaci: List<TelefonZadatakData>?,
    val forenzickiDokazZadaci: List<ForenzickiDokazZadatakData>?
)

/**
 * Miscellaneous data for mysterious symptoms cases.
 *
 * @property gallery Collection of images related to the case.
 * @property pitanja General questions asked in the investigation.
 * @property odgovori Answers provided during the investigation process.
 */
data class OtherMSDataGeminiRetrofit(
    val gallery: List<GalleryData>?,
    val pitanja: List<PitanjeData>?,
    val odgovori: List<OdgovorData>?,
)