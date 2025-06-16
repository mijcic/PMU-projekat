package com.example.models.dto.gemini.retrofit

import com.example.models.dto.*
import com.example.models.interfaces.GeminiResponseRetrofitCommon
import kotlinx.serialization.Serializable

/**
 * Represents a Retrofit-compatible data structure for handling cases involving mysterious medical symptoms
 * within the Gemini investigative system. This class extends [GeminiResponseRetrofitCommon] and combines
 * standard criminal investigation data with medically relevant information.
 *
 * @property zlocinRetrofit Basic details about the suspected crime or incident.
 * @property dokaziRetrofit List of collected physical or circumstantial evidence.
 * @property telefoniRetrofit List of phones associated with individuals or the investigation.
 * @property forenzickiDokazRetrofit Forensic evidence retrieved during the investigation.
 * @property oneContactRetrofit Individual contact entries extracted from digital sources.
 * @property aplikacijeRetrofit Applications found on relevant digital devices.
 * @property beleskeRetrofit Investigator notes recorded throughout the case.
 * @property whatsappKontaktRetrofit WhatsApp contacts identified from analyzed devices.
 * @property whatsappPorukaRetrofit WhatsApp messages involved in the case.
 * @property oneCallRetrofit Records of individual phone calls.
 * @property galleryRetrofit Image data retrieved from digital devices.
 * @property obicnePorukeRetrofit Standard SMS/MMS messages collected during the investigation.
 * @property pitanjaRetrofit Questions asked during interviews or interrogations.
 * @property odgovoriRetrofit Responses given during questioning.
 * @property osobeRetrofit List of people involved in the case.
 * @property zadaciRetrofit Tasks assigned to investigators.
 * @property dokaziZadaciRetrofit Evidence linked to specific investigative tasks.
 * @property telefonZadaciRetrofit Phone-related data tied to tasks.
 * @property forenzickiDokazZadaciRetrofit Forensic evidence associated with specific investigative tasks.
 *
 * @property pacijentRetrofit Data related to the affected patient showing mysterious symptoms.
 * @property medicinskiIzvestajRetrofit Medical report describing the patient’s condition and symptoms.
 * @property lekarskiTestRetrofit Results of clinical or laboratory tests performed on the patient.
 * @property lokacijeIstrageRetrofit Locations investigated in relation to the patient and the case.
 * @property izjavaZaPacijentaRetrofit Official statement or testimony related to the patient.
 */
@Serializable
data class GeminiResponseRetrofitMysteriousSymptoms(
    var zlocinRetrofit: ZlocinData?,
    override var dokaziRetrofit: List<DokazData>?,
    override var telefoniRetrofit: List<TelefonData>?,
    override var forenzickiDokazRetrofit: List<ForenzickiDokazData>?,
    override var oneContactRetrofit: List<OneContactData>?,
    override var aplikacijeRetrofit: List<AplikacijaData>?,
    override var beleskeRetrofit: List<BeleskaData>?,
    override var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?,
    override var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?,
    override var oneCallRetrofit: List<OneCallData>?,
    override var galleryRetrofit: List<GalleryData>?,
    override var obicnePorukeRetrofit: List<ObicnaPorukaData>?,
    override var pitanjaRetrofit: List<PitanjeData>?,
    override var odgovoriRetrofit: List<OdgovorData>?,
    override var osobeRetrofit: List<OsobaData>?,
    override var zadaciRetrofit: List<ZadatakData>?,
    override var dokaziZadaciRetrofit: List<DokazZadatakData>?,
    override var telefonZadaciRetrofit: List<TelefonZadatakData>?,
    override var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?,

    var pacijentRetrofit: PacijentData?,
    var medicinskiIzvestajRetrofit: MedicinskiIzvestajData?,
    var lekarskiTestRetrofit: LekarskiTestData?,
    var lokacijeIstrageRetrofit: List<LokacijeIstrageData>?,
    var izjavaZaPacijentaRetrofit: IzjavaZaPacijentaData?
): GeminiResponseRetrofitCommon
