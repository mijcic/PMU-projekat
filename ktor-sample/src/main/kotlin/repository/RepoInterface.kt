package com.example.repository

import com.example.models.dto.*

interface RepoInterface {
    fun getUsedZlocinMurder(): Int?

    fun getUsedZlocinMysteriousSymptoms(): Int?

    fun getZlocin(zlocinId:Int): ZlocinData?

    fun getTipZlocina(id:Int): TipZlocinaDC?

    fun getZrtva(id:Int): ZrtvaData?

    fun getOsumnjiceni(id: Int): List<OsumnjicenData>?

    fun getDokazi(id: Int,zr: ZrtvaData): List<DokazData>?

    fun getTelefon(id: Int): List<TelefonData>?

    fun getForenzickiDokazi(id: Int): List<ForenzickiDokazData>?

    fun getObdukcija(id: Int): ObdukcijaData?

    fun getSvedoci(id: Int): List<SvedokData>?

    fun getOneContact(id: Int): List<OneContactData>?

    fun getKontakti(id: Int,zr: ZrtvaData): List<KontaktData>?

    fun getPoruke(id: Int, zr: ZrtvaData, kontakti: List<KontaktData>?): List<PorukeData>?

    fun getPozivi(id: Int, zr: ZrtvaData, kontakti: List<KontaktData>?): List<PoziviData>?

    fun getGalerija(id: Int,zr: ZrtvaData): List<GalerijaData>?

    fun getAplikacije(id: Int,zr: ZrtvaData): List<AplikacijaData>?

    fun getTragovi(forenzickiDokazi: List<ForenzickiDokazData>?, osumnjiceni: List<OsumnjicenData>?): List<TragData>?

    fun getDokaziOsumnjiceni(dokazi: List<DokazData>?, osumnjiceni: List<OsumnjicenData>?): List<DokazOsumnjicenData>?

    fun getBeleske(id: Int,zr: ZrtvaData): List<BeleskaData>?

    fun getWhatsAppKontakt(id: Int,zr: ZrtvaData): List<WhatsAppKontaktData>?

    fun getWhatsAppPoruka(id: Int, whatsAppKontakti : List<WhatsAppKontaktData>?): List<WhatsAppPorukaData>?

    fun getGallery(id: Int): List<GalleryData>?

    fun getOdnosOsumnjicenZrtva(id: Int): List<OdnosOsumnjicenZrtvaData>?

    fun getPitanja(id: Int): List<PitanjeData>?

    fun getOdgovor(id: Int): List<OdgovorData>?

    fun getPitanjeIspitivanjeOsumnjicenog(id: Int): List<PitanjeIspitivanjeOsumnjicenogData>?

    fun getPitanjeIspitivanjeSvedoka(id: Int): List<PitanjeIspitivanjeSvedokaData>?

    fun getOsobe(id: Int): List<OsobaData>?

    fun getZadaci(id: Int): List<ZadatakData>?

    fun getDokaziZadaci(id: Int, zadaci: List<ZadatakData>?): List<DokazZadatakData>?

    fun getIspitivanjeOsumnjicenogZadatak(id: Int, zadaci: List<ZadatakData>?): List<IspitivanjeOsumnjicenogZadatakData>?

    fun getIspitivanjeSvedokaZadatak(id: Int,zadaci: List<ZadatakData>?): List<IspitivanjeSvedokaZadatakData>?

    fun getTelefonZadaci(id: Int,zadaci: List<ZadatakData>?): List<TelefonZadatakData>?

    fun getForenzickiDokazZadatak(id: Int,zadaci: List<ZadatakData>?): List<ForenzickiDokazZadatakData>?

    fun getOneCall(id: Int,oneContact: List<OneContactData>?): List<OneCallData>?

    fun getObicnaPoruka(id: Int,oneContact: List<OneContactData>?): List<ObicnaPorukaData>?

    fun getPacijent(id: Int, zl: ZlocinData, zr: ZrtvaData, osobe: List<OsobaData>): PacijentData?

    fun getMedicinskiIzvetaj(id: Int,pacijent: PacijentData?): MedicinskiIzvestajData?

    fun getLekarskiTest(id: Int,pacijent: PacijentData?): LekarskiTestData?

    fun getLokacijeIstrage(id: Int): List<LokacijeIstrageData>?

    fun getIzjavaZaPacijenta(pacijent: PacijentData, osobe: List<OsobaData>?): IzjavaZaPacijentaData?
}
