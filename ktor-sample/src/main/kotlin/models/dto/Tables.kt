package com.example.models.dto

import kotlinx.serialization.Serializable


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
