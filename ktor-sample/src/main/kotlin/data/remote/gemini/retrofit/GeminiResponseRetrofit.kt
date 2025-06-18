package com.example.data.remote.gemini.retrofit

import com.example.data.remote.tables.*
import com.example.interfaces.GeminiResponseRetrofitCommon
import kotlinx.serialization.Serializable

/**
 * Represents a data transfer object used with Retrofit for communicating with the Gemini system API.
 * This class extends [GeminiResponseRetrofitCommon] and contains serialized fields for a full investigation case,
 * including crime details, suspects, victims, evidence, forensic data, communication records, and tasks.
 *
 * @property zlocinRetrofit Basic information about the crime.
 * @property zrtvaRetrofit Details about the victim.
 * @property osumnjiceniRetrofit List of suspects involved in the case.
 * @property dokaziRetrofit List of general evidence items.
 * @property telefoniRetrofit List of phones associated with people or the case.
 * @property forenzickiDokazRetrofit List of forensic evidence items.
 * @property obdukcijaRetrofit Autopsy report for the victim.
 * @property svedociRetrofit List of witnesses.
 * @property oneContactRetrofit Individual contact entries extracted from digital sources.
 * @property kontaktiRetrofit Full contact lists obtained during investigation.
 * @property porukeRetrofit List of standard messages/SMS retrieved from devices.
 * @property poziviRetrofit List of phone call logs retrieved during the investigation.
 * @property galerijaRetrofit Photo gallery data from analyzed devices.
 * @property aplikacijeRetrofit List of applications discovered on digital devices.
 * @property tragoviRetrofit Digital traces and logs from devices or environments.
 * @property dokaziOsumnjiceniRetrofit Evidence directly linked to suspects.
 * @property beleskeRetrofit Investigation notes and observations.
 * @property whatsappKontaktRetrofit WhatsApp contacts found during device analysis.
 * @property whatsappPorukaRetrofit WhatsApp messages included in the investigation.
 * @property oneCallRetrofit Individual call metadata entries.
 * @property galleryRetrofit Gallery entries related to the case.
 * @property obicnePorukeRetrofit Standard (non-WhatsApp) SMS or MMS messages.
 * @property odnosiOsumnjiceniZrtvaRetrofit Relationships between suspects and victims.
 * @property pitanjaRetrofit Questions used during suspect/witness interrogation.
 * @property odgovoriRetrofit Answers collected during interrogation sessions.
 * @property pitanjeIspitivanjeOsumnjicenogRetrofit Specific questions asked to suspects.
 * @property pitanjeIspitivanjeSvedokaRetrofit Specific questions asked to witnesses.
 * @property osobeRetrofit List of all individuals related to the case.
 * @property zadaciRetrofit Tasks assigned to investigative personnel.
 * @property dokaziZadaciRetrofit Evidence linked to specific investigative tasks.
 * @property ispitivanjeOsumnjicenogZadaciRetrofit Interrogations of suspects performed as part of tasks.
 * @property ispitivanjeSvedokaZadaciRetrofit Interrogations of witnesses performed as part of tasks.
 * @property telefonZadaciRetrofit Phone data associated with specific tasks.
 * @property forenzickiDokazZadaciRetrofit Forensic data linked to specific investigative tasks.
 */
@Serializable
data class GeminiResponseRetrofit(
    var zlocinRetrofit: ZlocinData?,
    var zrtvaRetrofit: ZrtvaData?,
    var osumnjiceniRetrofit: List<OsumnjicenData>?,
    override var dokaziRetrofit: List<DokazData>?,
    override var telefoniRetrofit: List<TelefonData>?,
    override var forenzickiDokazRetrofit: List<ForenzickiDokazData>?,
    var obdukcijaRetrofit: ObdukcijaData?,
    var svedociRetrofit: List<SvedokData>?,
    override var oneContactRetrofit: List<OneContactData>?,
    var kontaktiRetrofit: List<KontaktData>?,
    var porukeRetrofit: List<PorukeData>?,
    var poziviRetrofit: List<PoziviData>?,
    var galerijaRetrofit: List<GalerijaData>?,
    override var aplikacijeRetrofit: List<AplikacijaData>?,
    var tragoviRetrofit: List<TragData>?,
    var dokaziOsumnjiceniRetrofit: List<DokazOsumnjicenData>?,
    override var beleskeRetrofit: List<BeleskaData>?,
    override var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?,
    override var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?,
    override var oneCallRetrofit: List<OneCallData>?,
    override var galleryRetrofit: List<GalleryData>?,
    override var obicnePorukeRetrofit: List<ObicnaPorukaData>?,
    var odnosiOsumnjiceniZrtvaRetrofit: List<OdnosOsumnjicenZrtvaData>?,
    override var pitanjaRetrofit: List<PitanjeData>?,
    override var odgovoriRetrofit: List<OdgovorData>?,
    var pitanjeIspitivanjeOsumnjicenogRetrofit: List<PitanjeIspitivanjeOsumnjicenogData>?,
    var pitanjeIspitivanjeSvedokaRetrofit: List<PitanjeIspitivanjeSvedokaData>?,
    override var osobeRetrofit: List<OsobaData>?,
    override var zadaciRetrofit: List<ZadatakData>?,
    override var dokaziZadaciRetrofit: List<DokazZadatakData>?,
    var ispitivanjeOsumnjicenogZadaciRetrofit: List<IspitivanjeOsumnjicenogZadatakData>?,
    var ispitivanjeSvedokaZadaciRetrofit: List<IspitivanjeSvedokaZadatakData>?,
    override var telefonZadaciRetrofit: List<TelefonZadatakData>?,
    override var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?,
): GeminiResponseRetrofitCommon
