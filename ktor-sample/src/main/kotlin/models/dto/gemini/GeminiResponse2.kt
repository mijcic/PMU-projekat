package com.example.models.dto.gemini

import com.example.models.dto.*
import com.example.models.interfaces.GeminiResponseCommon2
import kotlinx.serialization.Serializable

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