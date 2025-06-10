package com.example.models.dto.gemini.retrofit

import com.example.models.dto.*
import com.example.models.interfaces.GeminiResponseRetrofitCommon
import kotlinx.serialization.Serializable

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
