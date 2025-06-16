package com.example.models.dto.gemini.retrofit

import com.example.models.dto.*
import com.example.models.interfaces.GeminiResponseCommon2
import kotlinx.serialization.Serializable

/**
 * Represents a comprehensive response from the Gemini system used in criminal investigations.
 * This data class implements [GeminiResponseCommon2] and includes various entities related to a case,
 * such as details about the crime, suspects, victims, evidence, contacts, and interrogations.
 *
 * @property zlocinR Information about the crime.
 * @property osobaR List of all persons involved in the investigation.
 * @property osumnjicenR List of suspects.
 * @property dokazR List of standard evidence.
 * @property svedokR List of witnesses.
 * @property zrtvaR Information about the victim.
 * @property obdukcijaR Autopsy report details.
 * @property forenzickiDokazR List of forensic evidence.
 * @property telefonR List of phones related to the case.
 * @property kontaktKtor List of contacts extracted from digital sources (Ktor).
 * @property porukeKtor List of messages from digital sources (Ktor).
 * @property poziviKtor List of calls from digital sources (Ktor).
 * @property galerijaKtor List of images from digital sources (Ktor).
 * @property aplikacijaKtor List of applications found on seized devices (Ktor).
 * @property tragKtor Digital traces found during the investigation.
 * @property dokazOsumnjicenKtor Digital evidence linked to suspects.
 * @property oneContactR List of individual contacts.
 * @property beleskaR Investigation notes.
 * @property whatsAppKontaktR WhatsApp contacts of suspects or witnesses.
 * @property whatsAppPorukaR WhatsApp messages included in the investigation.
 * @property oneCallR List of individual call records.
 * @property galleryR Gallery of images related to the case.
 * @property obicnaPorukaR Standard SMS messages.
 * @property odnosOsumnjicenZrtvaR Relationships between suspects and victims.
 * @property prijavljeniKorisnikR List of users who reported the crime or submitted statements.
 * @property pitanjeR Questions asked during the investigation.
 * @property odgovorR Answers provided during interrogations.
 * @property pitanjeIspitivanjeOsumnjicenogR Questions during suspect interrogations.
 * @property pitanjeIspitivanjeSvedokaR Questions during witness interrogations.
 * @property zadatakR List of tasks assigned to investigators.
 * @property dokazZadatakR Evidence linked to specific tasks.
 * @property ispitivanjeOsumnjicenogZadatakR Suspect interrogations conducted as part of tasks.
 * @property ispitivanjeSvedokaZadatakR Witness interrogations conducted as part of tasks.
 * @property telefonZadatakR Phones related to specific tasks.
 * @property forenzickiDokazZadatakR Forensic evidence linked to investigation tasks.
 */
@Serializable
data class GeminiResponse2(
    val zlocinR: ZlocinR,
    override val osobaR:  List<OsobaR>,
    //val motivR: List<MotivR>,
    val osumnjicenR:  List<OsumnjicenR>,
    override val dokazR:  List<DokazR>,
    val svedokR:  List<SvedokR>,
    val zrtvaR: ZrtvaR,
    val obdukcijaR: ObdukcijaR,
    override val forenzickiDokazR: List<ForenzickiDokazR>,
    override val telefonR: List<TelefonR>,
    //val dokazOsumnjicenR: List<DokazOsumnjicenR>,
    //val zadatakR: ZadatakR,
    //val alibiR: List<AlibiR>,
    val kontaktKtor: List<KontaktKtor>,
    val porukeKtor: List<PorukeKtor>,
    val poziviKtor: List<PoziviKtor>,
    val galerijaKtor: List<GalerijaKtor>,
    override val aplikacijaKtor: List<AplikacijaKtor>,
    val tragKtor: List<TragKtor>,
    val dokazOsumnjicenKtor: List<DokazOsumnjicenKtor>,
    override val oneContactR: List<OneContactR>,
    override val beleskaR: List<BeleskaR>,
    override val whatsAppKontaktR: List<WhatsAppKontaktR>,
    override val whatsAppPorukaR: List<WhatsAppPorukaR>,
    override val oneCallR: List<OneCallR>,
    override val galleryR: List<GalleryR>,
    override val obicnaPorukaR: List<ObicnaPorukaR>,
    val odnosOsumnjicenZrtvaR: List<OdnosOsumnjicenZrtvaR>,
    val prijavljeniKorisnikR: List<PrijavljeniKorisnikR>,
    override val pitanjeR: List<PitanjeR>,
    override val odgovorR: List<OdgovorR>,
    val pitanjeIspitivanjeOsumnjicenogR: List<PitanjeIspitivanjeOsumnjicenogR>,
    val pitanjeIspitivanjeSvedokaR: List<PitanjeIspitivanjeSvedokaR>,
    override val zadatakR: List<ZadatakR>,
    override val dokazZadatakR: List<DokazZadatakR>,
    val ispitivanjeOsumnjicenogZadatakR: List<IspitivanjeOsumnjicenogZadatakR>,
    val ispitivanjeSvedokaZadatakR: List<IspitivanjeSvedokaZadatakR>,
    override val telefonZadatakR: List<TelefonZadatakR>,
    override val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,
) : GeminiResponseCommon2