package com.example.models.dto.service

import com.example.models.dto.*

data class ZlocinDataGeminiRetrofit(
    val zlocin: ZlocinData,
    val tip: TipZlocinaDC
)

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

data class OsumnjiceniDataGeminiRetrofit(
    val osumnjiceni: List<OsumnjicenData>,
    val tragovi: List<TragData>?,
    val dokaziOsumnjiceni: List<DokazOsumnjicenData>?
)

data class ZadaciDataGeminiRetrofit(
    val zadaci: List<ZadatakData>,
    val dokaziZadaci: List<DokazZadatakData>?,
    val ispitivanjeOsumnjicenogZadaci: List<IspitivanjeOsumnjicenogZadatakData>?,
    val ispitivanjeSvedokaZadaci: List<IspitivanjeSvedokaZadatakData>?,
    val telefonZadaci: List<TelefonZadatakData>?,
    val forenzickiDokazZadaci: List<ForenzickiDokazZadatakData>?
)

data class OtherDataGeminiRetrofit(
    val gallery: List<GalleryData>?,
    val odnosi: List<OdnosOsumnjicenZrtvaData>?,
    val pitanja: List<PitanjeData>?,
    val odgovori: List<OdgovorData>?,
    val pitanjaIspitivanjeOsumnjicenog: List<PitanjeIspitivanjeOsumnjicenogData>?,
    val pitanjaIspitivanjeSvedoka: List<PitanjeIspitivanjeSvedokaData>?,
    val osobe: List<OsobaData>?
)