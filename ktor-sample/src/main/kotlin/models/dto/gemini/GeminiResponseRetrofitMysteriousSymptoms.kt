package com.example.models.dto.gemini

import com.example.models.dto.*
import com.example.models.interfaces.GeminiResponseRetrofitCommon
import kotlinx.serialization.Serializable


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
