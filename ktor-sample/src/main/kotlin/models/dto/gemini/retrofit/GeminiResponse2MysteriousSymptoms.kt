package com.example.models.dto.gemini.retrofit

import com.example.models.dto.*
import com.example.models.interfaces.GeminiResponseCommon2
import kotlinx.serialization.Serializable

/**
 * Represents a detailed response from the Gemini system related to a case involving mysterious symptoms.
 * This class extends [GeminiResponseCommon2] and includes both standard investigative data and
 * additional medical and patient-related information.
 *
 * @property zlocinR Information about the crime under investigation.
 * @property osobaR List of people involved in the case.
 * @property dokazR List of standard physical or circumstantial evidence.
 * @property forenzickiDokazR List of forensic evidence collected during the investigation.
 * @property telefonR List of phones associated with persons or the investigation.
 * @property aplikacijaKtor List of applications found on analyzed digital devices.
 *
 * @property oneContactR List of individual contact entries from digital sources.
 * @property beleskaR Notes recorded during the investigation.
 * @property whatsAppKontaktR WhatsApp contacts extracted from devices.
 * @property whatsAppPorukaR WhatsApp messages related to the investigation.
 * @property oneCallR List of individual phone call records.
 * @property galleryR Image gallery entries from digital evidence.
 * @property obicnaPorukaR Regular SMS messages.
 * @property pitanjeR Questions asked during the course of the investigation.
 * @property odgovorR Answers given during questioning or interviews.
 * @property zadatakR Tasks assigned to investigators.
 * @property dokazZadatakR Evidence tied to specific tasks or investigative actions.
 * @property telefonZadatakR Phone data related to specific tasks.
 * @property forenzickiDokazZadatakR Forensic evidence associated with tasks.
 *
 * @property pacijentR Details about the patient involved in the case.
 * @property medicinskiIzvestajR Medical report summarizing the patient's condition.
 * @property lekarskiTestR Results of medical tests performed on the patient.
 * @property lokacijeIstrageR List of investigation locations tied to the case.
 * @property izjavaZaPacijentaR Official statement regarding the patient.
 */
@Serializable
data class GeminiResponse2MysteriousSymptoms(
    val zlocinR: ZlocinR,
    override val osobaR: List<OsobaR>,
    override val dokazR: List<DokazR>,
    override val forenzickiDokazR: List<ForenzickiDokazR>,
    override val telefonR: List<TelefonR>,
    override val aplikacijaKtor: List<AplikacijaKtor>,

    override val oneContactR: List<OneContactR>,
    override val beleskaR: List<BeleskaR>,
    override val whatsAppKontaktR: List<WhatsAppKontaktR>,
    override val whatsAppPorukaR: List<WhatsAppPorukaR>,
    override val oneCallR: List<OneCallR>,
    override val galleryR: List<GalleryR>,
    override val obicnaPorukaR: List<ObicnaPorukaR>,
    override val pitanjeR: List<PitanjeR>,
    override val odgovorR: List<OdgovorR>,
    override val zadatakR: List<ZadatakR>,

    override val dokazZadatakR: List<DokazZadatakR>,
    override val telefonZadatakR: List<TelefonZadatakR>,
    override val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,

    val pacijentR: PacijentR,
    val medicinskiIzvestajR: MedicinskiIzvestajR,
    val lekarskiTestR: LekarskiTestR,
    val lokacijeIstrageR: List<LokacijeIstrageR>,
    val izjavaZaPacijentaR: IzjavaZaPacijentaR
) : GeminiResponseCommon2

