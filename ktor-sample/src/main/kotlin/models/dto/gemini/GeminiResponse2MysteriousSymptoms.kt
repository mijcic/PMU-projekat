package com.example.models.dto.gemini

import com.example.models.dto.*
import com.example.models.interfaces.GeminiResponseCommon2
import kotlinx.serialization.Serializable


@Serializable
data class GeminiResponse2MysteriousSymptoms(
    val zlocinR: ZlocinR,
    override val osobaR: List<OsobaR>,
    override val dokazR: List<DokazR>,
    override val forenzickiDokazR: List<ForenzickiDokazR>,
    override val telefonR: List<TelefonR>,
    override val aplikacijaKtor: List<AplikacijaKtor>,

    override val oneContactR: List<OneContactR>,
    override val beleskaR: List<BeleskaR>,
    override val whatsAppKontaktR: List<WhatsAppKontaktR>,
    override val whatsAppPorukaR: List<WhatsAppPorukaR>,
    override val oneCallR: List<OneCallR>,
    override val galleryR: List<GalleryR>,
    override val obicnaPorukaR: List<ObicnaPorukaR>,
    override val pitanjeR: List<PitanjeR>,
    override val odgovorR: List<OdgovorR>,
    override val zadatakR: List<ZadatakR>,

    override val dokazZadatakR: List<DokazZadatakR>,
    override val telefonZadatakR: List<TelefonZadatakR>,
    override val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,

    val pacijentR: PacijentR,
    val medicinskiIzvestajR: MedicinskiIzvestajR,
    val lekarskiTestR: LekarskiTestR,
    val lokacijeIstrageR: List<LokacijeIstrageR>,
    val izjavaZaPacijentaR: IzjavaZaPacijentaR
) : GeminiResponseCommon2

