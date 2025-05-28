package com.example.models.interfaces

import com.example.models.dto.*

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