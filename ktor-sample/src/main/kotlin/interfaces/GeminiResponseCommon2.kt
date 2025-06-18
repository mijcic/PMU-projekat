package com.example.interfaces

import com.example.data.remote.generic.*

/**
 * Interface that defines a common structure for Gemini response data classes.
 * It encapsulates shared fields related to evidence, individuals, communication,
 * forensic analysis, and investigative tasks across multiple domain-specific responses.
 *
 * Implementing classes typically represent different types of investigative responses
 * (e.g., standard crimes, medical cases) that include a subset of these common elements.
 *
 * @property dokazR List of general evidence related to the investigation.
 * @property dokazZadatakR List of evidence obtained through specific investigative tasks.
 * @property osobaR List of individuals involved in the investigation (suspects, witnesses, etc.).
 * @property forenzickiDokazR General forensic evidence (e.g., biological, digital).
 * @property forenzickiDokazZadatakR Forensic evidence specifically tied to tasks.
 * @property telefonR Phones associated with persons involved in the case.
 * @property telefonZadatakR Phone-related findings linked to tasks.
 * @property aplikacijaKtor Applications found on devices used by subjects of interest.
 * @property oneContactR Individual contact records extracted from devices.
 * @property beleskaR Notes written or logged during the investigation.
 * @property whatsAppKontaktR WhatsApp contact entries extracted from devices.
 * @property whatsAppPorukaR WhatsApp messages relevant to the case.
 * @property oneCallR Specific phone call records.
 * @property galleryR Digital media files (images, videos) from suspects or victims.
 * @property obicnaPorukaR Regular SMS or MMS messages retrieved during analysis.
 * @property pitanjeR General questions used in interrogations or interviews.
 * @property odgovorR Answers provided during questioning.
 * @property zadatakR List of investigative tasks assigned or completed.
 */
interface GeminiResponseCommon2{
    val dokazR: List<DokazR>
    val dokazZadatakR:List<DokazZadatakR>
    val osobaR:  List<OsobaR>
    val forenzickiDokazR: List<ForenzickiDokazR>
    val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>
    val telefonR: List<TelefonR>
    val telefonZadatakR: List<TelefonZadatakR>
    val aplikacijaKtor: List<AplikacijaKtor>

    val oneContactR: List<OneContactR>
    val beleskaR: List<BeleskaR>
    val whatsAppKontaktR: List<WhatsAppKontaktR>
    val whatsAppPorukaR: List<WhatsAppPorukaR>
    val oneCallR: List<OneCallR>
    val galleryR: List<GalleryR>
    val obicnaPorukaR: List<ObicnaPorukaR>
    val pitanjeR: List<PitanjeR>
    val odgovorR: List<OdgovorR>
    val zadatakR: List<ZadatakR>
}