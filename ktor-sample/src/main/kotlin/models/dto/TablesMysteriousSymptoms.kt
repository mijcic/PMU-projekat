package com.example.models.dto

import com.example.*
import kotlinx.serialization.Serializable

@Serializable
data class TablesMysteriousSymptoms(
    val zlocinR: ZlocinR,
    val osobaR: List<OsobaR>,
    val dokazR: List<DokazR>,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    val aplikacijaKtor: List<AplikacijaKtor>,
    val oneContactR: List<OneContactR>,
    val beleskaR: List<BeleskaR>,
    val whatsAppKontaktR: List<WhatsAppKontaktR>,
    val whatsAppPorukaR: List<WhatsAppPorukaR>,
    val oneCallR: List<OneCallR>,
    val galleryR: List<GalleryR>,
    val obicnaPorukaR: List<ObicnaPorukaR>,
    val pitanjeR: List<PitanjeR>,
    val odgovorR: List<OdgovorR>,
    val zadatakR: List<ZadatakR>,
    val dokazZadatakR: List<DokazZadatakR>,
    val telefonZadatakR: List<TelefonZadatakR>,
    val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,
    val pacijentR: PacijentR,
    val medicinskiIzvestajR: MedicinskiIzvestajR,
    val lekarskiTestR: LekarskiTestR,
    val lokacijeIstrageR: List<LokacijeIstrageR>,
    val izjavaZaPacijentaR: IzjavaZaPacijentaR
)
