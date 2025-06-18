package com.example.data.remote.tables

import com.example.data.remote.generic.*
import kotlinx.serialization.Serializable

/**
 * Represents a comprehensive table-based data structure for storing all information
 * related to a case involving mysterious symptoms. This includes crime details,
 * individuals involved, communication data, medical reports, and investigation tasks.
 *
 * @property zlocinR Information about the crime under investigation.
 * @property osobaR List of people involved in the case (suspects, witnesses, etc.).
 * @property dokazR General evidence collected during the investigation.
 * @property forenzickiDokazR Forensic evidence from crime scenes or digital sources.
 * @property telefonR Phones associated with individuals in the case.
 * @property aplikacijaKtor List of mobile or digital applications found during analysis.
 * @property oneContactR Extracted contact entries from digital devices.
 * @property beleskaR Notes and annotations made by investigators.
 * @property whatsAppKontaktR WhatsApp contacts extracted from phones or backups.
 * @property whatsAppPorukaR WhatsApp message history linked to the investigation.
 * @property oneCallR Specific phone call records.
 * @property galleryR Digital gallery (images/media) linked to persons or evidence.
 * @property obicnaPorukaR Standard text messages (SMS/MMS).
 * @property pitanjeR Questions used in interviews or interrogations.
 * @property odgovorR Answers collected during interviews.
 * @property zadatakR List of investigation tasks.
 * @property dokazZadatakR Evidence related to specific investigative tasks.
 * @property telefonZadatakR Phone data associated with tasks.
 * @property forenzickiDokazZadatakR Forensic evidence obtained through tasks.
 * @property pacijentR Data about the patient affected by mysterious symptoms.
 * @property medicinskiIzvestajR A detailed medical report for the patient.
 * @property lekarskiTestR Medical test results related to the patient’s condition.
 * @property lokacijeIstrageR Locations examined in the course of the investigation.
 * @property izjavaZaPacijentaR Official statements or testimonies concerning the patient.
 */
@Serializable
data class TablesMysteriousSymptoms(
    val zlocinR: ZlocinR,
    val osobaR: List<OsobaR>,
    val dokazR: List<DokazR>,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    val aplikacijaKtor: List<AplikacijaKtor>,
    val oneContactR: List<OneContactR>,
    val beleskaR: List<BeleskaR>,
    val whatsAppKontaktR: List<WhatsAppKontaktR>,
    val whatsAppPorukaR: List<WhatsAppPorukaR>,
    val oneCallR: List<OneCallR>,
    val galleryR: List<GalleryR>,
    val obicnaPorukaR: List<ObicnaPorukaR>,
    val pitanjeR: List<PitanjeR>,
    val odgovorR: List<OdgovorR>,
    val zadatakR: List<ZadatakR>,
    val dokazZadatakR: List<DokazZadatakR>,
    val telefonZadatakR: List<TelefonZadatakR>,
    val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,
    val pacijentR: PacijentR,
    val medicinskiIzvestajR: MedicinskiIzvestajR,
    val lekarskiTestR: LekarskiTestR,
    val lokacijeIstrageR: List<LokacijeIstrageR>,
    val izjavaZaPacijentaR: IzjavaZaPacijentaR
)