package rs.ac.bg.etf.projekat.data.realm

import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore

import io.realm.kotlin.types.annotations.PrimaryKey
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

// Zlocin table
open class PrijavljeniKorisnikR : RealmObject {
    @PrimaryKey
    var idKorisnik: Int = 0
    var korisnickoIme: String = ""
    var sifra: String = ""
}

// Enum for stZlocin
enum class stZlocinR {
    u_istrazi,
    resen
}

// Zlocin table
open class ZlocinR : RealmObject {
    @PrimaryKey
    var idZlocin: Int = 0
    var tipZlocinaId: TipZlocinaR? = null
    var naziv: String = ""

    var datum: RealmInstant ?=null
    var mesto: String = ""
    var opis: String = ""
    var status: String = stZlocinR.u_istrazi.name
}

// TipZlocinaR table
open class TipZlocinaR : RealmObject {
    @PrimaryKey
    var idTipZlocina: Int = 0
    var nazivTipaZlocina: String = nazivTipaZlocinaR.murder.name
}

// Enum for TipZlocina
enum class nazivTipaZlocinaR {
    murder,
    disappearance,
    robbery,
    kidnappingAndBlackmail,
    FamilySecrets,
    Abuse,
    GangConflicts,
    Corruption,
    MysteriousSymptoms,
    MafiaCrimesOfPassion,
    FalseIdentities,
    CultsAndSecrets
}

// NapredakIstrageR table
open class NapredakIstrageR : RealmObject {
    @PrimaryKey
    var idNapredak: Int = 0
    var korisnikId: Int = 0
    var zlocinId: ZlocinR? = null
}

// MisijaR table
open class MisijaR : RealmObject {
    @PrimaryKey
    var idMisija: Int = 0
    var zlocinId: ZlocinR? = null
    var naziv: String = ""
    var opis: String = ""
    var status: Int = 0
}

// MisijaPorukaR table
open class MisijaPorukaR : RealmObject {
    @PrimaryKey
    var idMisija: Int = 0
    var zlocinId: ZlocinR? = null
    var naziv: String = ""
    var poruka: PorukeR? = null
    var status: Int = 0
    var posiljalac: String = ""
}

//OsobaR table
open class OsobaR: RealmObject{
    @PrimaryKey
    var idOsoba: Int = 0
    var ime:String =""
    var kontakt: String =""
    var datum:RealmInstant? = null
    var zanimanje: String=""
    var pol: String = PolR.muski.name
    var zlocinId: ZlocinR? = null
}

enum class PolR{
    muski,
    zenski
}

// OsumnjicenR table
/*
open class OsumnjicenR : RealmObject {
    @PrimaryKey
    var idOsumnjicen: Int = 0
    var ime: String = ""
    var status: Int = 0
    var tipOsumnjicen: String = TipOsumnjicenR.pojedinac.name
    var motiv: MotivR? = null
    var zlocinId: ZlocinR? = null
    var kriv: Int = 0
}*/

open class OsumnjicenR : RealmObject {//-ime +osobaId
    @PrimaryKey
    var idOsumnjicen: Int = 0
    var status: Int = 0
    var tipOsumnjicen: String = TipOsumnjicenR.pojedinac.name
    var motiv: MotivR? = null
    var zlocinId: ZlocinR? = null
    var kriv: Int = 0
    var osobaId: OsobaR? = null
}

// Enum for TipOsumnjicen
enum class TipOsumnjicenR {
    pojedinac,
    organizacija
}

// Table IspitivanjeOsumnjicenogZadatak
open class IspitivanjeOsumnjicenogZadatakR : RealmObject {
    @PrimaryKey
    var idIspitivanjeOsumnjicenogZadatak: Int = 0
    var osumnjicenId: OsumnjicenR? =null
    var zadatakId: ZadatakR? = null
    var uradjen: Boolean = false
}

// Table IspitivanjeSvedokaZadatak
open class IspitivanjeSvedokaZadatakR : RealmObject {
    @PrimaryKey
    var idIspitivanjeSvedokaZadatak: Int = 0
    var svedokId: SvedokR? =null
    var zadatakId: ZadatakR? = null
    var uradjen: Boolean = false
}

// MotivR table
open class MotivR : RealmObject {
    @PrimaryKey
    var idMotiv: Int = 0
    var opis: String = ""
}

// DokazR table
open class DokazR : RealmObject {
    @PrimaryKey
    var idDokaz: Int = 0
    var tipDokaza: String = TipDokazaR.svedok.name
    var opis: String = ""
    var zlocinId: ZlocinR? = null
    var zrtvaId: ZrtvaR? = null
    var status: Int = 0
}

// DokazZadatakR table
open class DokazZadatakR: RealmObject {
    @PrimaryKey
    var idDokazZadatak: Int =0
    var tekst: String =""
    var dokazId: DokazR? =null
    var uradjen: Boolean =false
    var zadatakId: ZadatakR? =null
}

// Enum for TipDokaza
enum class TipDokazaR {
    fizicki,
    digitalni,
    svedok
}

// SvedokR table
/*
open class SvedokR : RealmObject {
    @PrimaryKey
    var idSvedok: Int = 0
    var ime: String = ""
    var kontakt: String = ""
    var izjava: String = ""
    var zlocinId: ZlocinR? = null
    var statusSvedok: String = StatusSvedokR.aktivno.name
    var statusIspitan: Int = 0
}*/
open class SvedokR : RealmObject {// -ime,kontakt  +osobaId
    @PrimaryKey
    var idSvedok: Int = 0
    var izjava: String = ""
    var statusSvedok: String = StatusSvedokR.aktivno.name
    var statusIspitan: Int = 0
    var zlocinId: ZlocinR? = null
    var osobaId: OsobaR? = null
}

// Enum for StatusSvedok
enum class StatusSvedokR {
    aktivno,
    zasticen,
    nesaradnja
}

// ZrtvaR table
/*
open class ZrtvaR : RealmObject {
    @PrimaryKey
    var idZrtva: Int = 0
    var tipZrtve: String = ""
    var ime: String = ""
    var detalji: String = ""
    var statusZrtva: String = StatusZrtvaR.ziva.name
    var zlocinId: ZlocinR? = null
}*/

open class ZrtvaR : RealmObject {// -ime
    @PrimaryKey
    var idZrtva: Int = 0
    var tipZrtve: String = ""
    var detalji: String = ""
    var statusZrtva: String = StatusZrtvaR.ziva.name
    var zlocinId: ZlocinR? = null
    var osobaId: OsobaR? = null
}

// Enum for StatusZrtva
enum class StatusZrtvaR {
    ziva,
    mrtva,
    nestala
}

// ZrtvaZlostavljanjaR table
open class ZrtvaZlostavljanjaR : RealmObject {
    @PrimaryKey
    var idZlostavljanje: Int = 0
    var tipZlostavljanja: String = TipZlostavljanjaR.fizicko.name
}

// Enum for TipZlostavljanja
enum class TipZlostavljanjaR {
    fizicko,
    emocionalno,
    seksualno
}

// BandaR table
open class BandaR : RealmObject {
    @PrimaryKey
    var idBanda: Int = 0
    var nazivBande: String = ""
    var tipBande: String = TipBandeR.iznuda.name
    var status: Int = 0
}

// Enum for TipBande
enum class TipBandeR {
    narkoTrgovina,
    iznuda,
    teritorija
}

// MafijaR table
open class MafijaR : RealmObject {
    @PrimaryKey
    var idMafije: Int = 0
    var naziv: String = ""
    var lider: String = ""
    var aktivnosti: String = ""
}

// KultoviR table
open class KultoviR : RealmObject {
    @PrimaryKey
    var idKult: Int = 0
    var naziv: String = ""
    var lider: String = ""
    var ucenja: String = ""
    var aktivnosti: String = ""
}

// TelefonR table
open class TelefonR : RealmObject {
    @PrimaryKey
    var idTelefon: Int = 0
    var model: String = ""
    var os: String = ""
    var zrtvaId: ZrtvaR? = null
    var sifra: String = ""
}

// TelefonZadatakR table
open class TelefonZadatakR: RealmObject {
    @PrimaryKey
    var idTelefonZadatak: Int=0
    var telefonId: TelefonR? =null
    var zadatakId: ZadatakR? =null
    var uradjen: Boolean =false
}

// KontaktR table
open class KontaktR : RealmObject {
    @PrimaryKey
    var idKontakt: Int = 0
    var ime: String = ""
    var broj: String = ""
    var status: Int = 0
    var zrtvaId: ZrtvaR? = null
}

// PorukeR table
open class PorukeR : RealmObject {
    @PrimaryKey
    var idPoruke: Int = 0
    var tipPoruke: String = ""
    var sadrzaj: String = ""
    var datumVreme: RealmInstant? = null
    var zrtvaId: ZrtvaR? = null
    var posiljalacId: KontaktR? = null
    var statusPoruke: String =StatusPorukeR.read.name
    var sifrovana: Boolean = false
}

//Table PorukeZadatak
open class PorukeZadatakR: RealmObject {
    @PrimaryKey
    var idPorukeZadatak: Int =0
    var porukeId: PorukeR? =null
    var zadatakId: ZadatakR? =null
    var uradjen: Boolean = false
}


// Enum for TipPoruke
enum class TipPorukeR {
    SMS,
    WhatsApp,
    email
}

// Enum for StatusPoruke
enum class StatusPorukeR {
    sent,
    read,
    delete
}

// PoziviR table
open class PoziviR : RealmObject {
    @PrimaryKey
    var idPoziv: Int = 0
    var tip: Int = 0
    var broj: String = ""
    var datumVreme: RealmInstant? = null
    var zrtvaId: ZrtvaR? = null
    var status: Int = 0
    var kontaktId: KontaktR? = null
}

// GalerijaR table
open class GalerijaR : RealmObject {
    @PrimaryKey
    var idGalerija: Int = 0
    var tip: Int = 0
    var putanja: String = ""
    var zrtvaId: ZrtvaR? = null
    var datumVreme: RealmInstant? = null
    var lokacija: String = ""
}

// AplikacijaR table
open class AplikacijaR : RealmObject {
    @PrimaryKey
    var idAplikacije: Int = 0
    var naziv: String = ""
    var tip: Int = 0
    var zrtvaId: ZrtvaR? = null
    var aktivna: Boolean = false
    var informacije: String = ""
}

// ObdukcijaR table
open class ObdukcijaR : RealmObject {
    @PrimaryKey
    var idObdukcija: Int = 0
    var izvestaj: String = ""
    var datum: RealmInstant? = null
    var uzrokSmrti: String = ""
    var zrtvaId: ZrtvaR? = null
    var informacije: String = ""
}

// ForenzickiDokazR table
open class ForenzickiDokazR : RealmObject {
    @PrimaryKey
    var idForenzickiDokaz: Int = 0
    var tipForenzickiDokaz: String = TipForenzickiDokazR.DNK.name
    var opis: String = ""
    var status: Int = 0
    var zrtvaId: ZrtvaR? = null
    var veza: String = ""
}

// ForenzickiDokazZadatakR table
open class ForenzickiDokazZadatakR : RealmObject {
    @PrimaryKey
    var idForenzickiDokazZadatak: Int = 0
    var tekst: String =""
    var forenzickiDokazId: ForenzickiDokazR? =null
    var uradjen: Boolean =false
    var zadatakId: ZadatakR? =null
}


// Enum for TipForenzickiDokaz
enum class TipForenzickiDokazR {
    otisak,
    DNK,
    dokument
}

// TragR table
open class TragR : RealmObject {
    @PrimaryKey
    var idTrag: Int = 0
    var forenzickiDokazId: ForenzickiDokazR? = null
    var osumnjicenId: OsumnjicenR? = null
}

// DokazOsumnjicenR table
open class DokazOsumnjicenR : RealmObject {
    @PrimaryKey
    var idDokazOsumnjicen: Int = 0
    var dokazId: DokazR? = null
    var osumnjicenId: OsumnjicenR? = null
}

// AlibiR table
open class AlibiR : RealmObject {
    @PrimaryKey
    var idAlibi: Int = 0
    var osumnjicenId: OsumnjicenR? = null
    var svedokId: SvedokR? = null
    var opis: String = ""
    var statusAlibija: String = StatusAlibijaR.lažan.name
}

// Enum for StatusAlibija
enum class StatusAlibijaR {
    potvrđen,
    lažan,
    nepotvrđen
}

// OdnosOsumnjicenZrtvaR table
open class OdnosOsumnjicenZrtvaR : RealmObject {
    @PrimaryKey
    var idOdnos: Int = 0
    var osumnjicenId: OsumnjicenR? = null
    var zrtvaId: ZrtvaR? = null
    var tipOdnosa: String = TipOdnosaR.lični.name
}

// Enum for TipOdnosa
enum class TipOdnosaR {
    poslovni,
    lični,
    porodični,
    rivalski,
    slučajni,
    ljubavni
}

open class PitanjeIspitivanjeOsumnjicenogR : RealmObject {
    @PrimaryKey
    var idPitanjeIspitivanjeOsumnjicenog: Int =0
    var kategorija =KategorijaIspitivanjeOsumnjicenog.opsta.name
    var tekst: String =""
    var odgovor: String =""
    var komentar: String =""
    var osumnjicenId: OsumnjicenR? = null
}

enum class KategorijaIspitivanjeOsumnjicenog {
    opsta,
    alibi,
    dokaz,
    kontradikcija
}

open class PitanjeIspitivanjeSvedokaR : RealmObject {
    @PrimaryKey
    var idPitanjeIspitivanjeSvedoka: Int =0
    var tekst: String =""
    var odgovor: String =""
    var svedokId: SvedokR? = null
    var next: Int =0
}

open class PitanjeR : RealmObject {
    @PrimaryKey
    var idPitanje: Int = 0
    var zlocinId: ZlocinR? = null
    var tekst: String = ""
}

open class OdgovorR : RealmObject {
    @PrimaryKey
    var idOdogovor: Int = 0
    var pitanjeId: PitanjeR? = null
    var tekstOdgovora: String = ""
    var tacan: Boolean = false
    var bodovi: Int = 0
}

open class ZadatakR : RealmObject {
    @PrimaryKey
    var idZadatak: Int =0
    var tekst: String =""
    var korak: String =""
    var uradjen: Boolean = false
    var next: ZadatakR? = null
    var zlocinId: ZlocinR? =null
}

val realmClasses = listOf(
    ZlocinR::class,
    TipZlocinaR::class,
    NapredakIstrageR::class,
    MisijaR::class,
    MisijaPorukaR::class,
    OsumnjicenR::class,
    MotivR::class,
    DokazR::class,
    SvedokR::class,
    ZrtvaR::class,
    ZrtvaZlostavljanjaR::class,
    BandaR::class,
    MafijaR::class,
    KultoviR::class,
    TelefonR::class,
    KontaktR::class,
    PorukeR::class,
    PoziviR::class,
    GalerijaR::class,
    AplikacijaR::class,
    ObdukcijaR::class,
    ForenzickiDokazR::class,
    TragR::class,
    DokazOsumnjicenR::class,
    AlibiR::class,
    OdnosOsumnjicenZrtvaR::class,
    PrijavljeniKorisnikR::class,
    PitanjeIspitivanjeOsumnjicenogR::class,
    PitanjeR::class,
    OdgovorR::class,
    PitanjeIspitivanjeSvedokaR::class,
    OsobaR::class,
    ZadatakR::class,
    DokazZadatakR::class,
    IspitivanjeOsumnjicenogZadatakR::class,
    IspitivanjeSvedokaZadatakR::class,
    TelefonZadatakR::class,
    ForenzickiDokazZadatakR::class,
    PorukeZadatakR::class
)