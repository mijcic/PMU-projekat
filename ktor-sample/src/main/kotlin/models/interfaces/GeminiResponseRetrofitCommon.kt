package com.example.models.interfaces

import com.example.models.dto.*

interface GeminiResponseRetrofitCommon{
    var osobeRetrofit: List<OsobaData>?
    var dokaziRetrofit:List<DokazData>?
    var forenzickiDokazRetrofit: List<ForenzickiDokazData>?
    var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?
    var telefoniRetrofit: List<TelefonData>?
    var telefonZadaciRetrofit: List<TelefonZadatakData>?
    var aplikacijeRetrofit: List<AplikacijaData>?

    var oneContactRetrofit: List<OneContactData>?
    var beleskeRetrofit: List<BeleskaData>?
    var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?
    var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?
    var oneCallRetrofit: List<OneCallData>?
    var galleryRetrofit: List<GalleryData>?
    var obicnePorukeRetrofit: List<ObicnaPorukaData>?
    var pitanjaRetrofit: List<PitanjeData>?
    var odgovoriRetrofit: List<OdgovorData>?
    var zadaciRetrofit: List<ZadatakData>?
    var dokaziZadaciRetrofit: List<DokazZadatakData>?
}