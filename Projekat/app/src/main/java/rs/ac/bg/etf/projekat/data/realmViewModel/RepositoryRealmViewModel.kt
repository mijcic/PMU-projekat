package rs.ac.bg.etf.projekat.data.realmViewModel

import io.realm.kotlin.Realm
import io.realm.kotlin.types.RealmInstant
import rs.ac.bg.etf.projekat.data.realm.AlibiR
import rs.ac.bg.etf.projekat.data.realm.AplikacijaR
import rs.ac.bg.etf.projekat.data.realm.BeleskaR
import rs.ac.bg.etf.projekat.data.realm.DokazOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.GalleryR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeOsumnjicenogZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeSvedokaZadatakR
import rs.ac.bg.etf.projekat.data.realm.IzjavaZaPacijentaR
import rs.ac.bg.etf.projekat.data.realm.KontaktR
import rs.ac.bg.etf.projekat.data.realm.KorisnikRequestR
import rs.ac.bg.etf.projekat.data.realm.LekarskiTestR
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR
import rs.ac.bg.etf.projekat.data.realm.MedicinskiIzvestajR
import rs.ac.bg.etf.projekat.data.realm.MisijaPorukaR
import rs.ac.bg.etf.projekat.data.realm.MisijaR
import rs.ac.bg.etf.projekat.data.realm.MotivR
import rs.ac.bg.etf.projekat.data.realm.ObdukcijaR
import rs.ac.bg.etf.projekat.data.realm.ObicnaPorukaR
import rs.ac.bg.etf.projekat.data.realm.OdgovorR
import rs.ac.bg.etf.projekat.data.realm.OdnosOsumnjicenZrtvaR
import rs.ac.bg.etf.projekat.data.realm.OneCallR
import rs.ac.bg.etf.projekat.data.realm.OneContactR
import rs.ac.bg.etf.projekat.data.realm.OsobaR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.PacijentR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeOsumnjicenogR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR
import rs.ac.bg.etf.projekat.data.realm.PitanjeR
import rs.ac.bg.etf.projekat.data.realm.PorukeR
import rs.ac.bg.etf.projekat.data.realm.PorukeZadatakR
import rs.ac.bg.etf.projekat.data.realm.ScoreKorisnikaR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonR
import rs.ac.bg.etf.projekat.data.realm.TelefonZadatakR
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import rs.ac.bg.etf.projekat.data.realm.TragR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppKontaktR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppPorukaR
import rs.ac.bg.etf.projekat.data.realm.ZadatakR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR
import rs.ac.bg.etf.projekat.data.realm.ZrtvaR

interface RepositoryRealmViewModel {
    fun getRealm(): Realm

    suspend fun insertTipZlocina(nazivTZ: String): TipZlocinaR?

    suspend fun insertZlocin(idZlocinZ:Int, tipZlocina: TipZlocinaR?, nazivZ: String, datumZ: String, mestoZ: String, opisZ: String, statusZ: String): ZlocinR?

    suspend fun insertOsoba(idOsobaO:Int, imeZ: String, kontaktZ:String, datumZ: RealmInstant, zanimanjeZ: String, polZ: String, zlocinZ: ZlocinR?): OsobaR?

    suspend fun insertZrtva(idZrtvaZ:Int, tipZ: String, imeZ: String, detaljiZ: String, statusZ: String, zlocinZ: ZlocinR?, kontaktZ: String, datumZ: RealmInstant, zanimanjeZ: String, polZ: String): ZrtvaR?

    suspend fun insertMotiv(opisM: String): MotivR?

    suspend fun insertOsumnjiceni(
        idOsumnjicenO:Int,imeO: String, statusO: Int, tipOsumnjicenO: String, motivO: MotivR?, zlocinO: ZlocinR?, krivO: Int,
        kontaktO: String, datumO: RealmInstant, zanimanjO: String, polO: String
    ): OsumnjicenR?

    suspend fun insertDokaz(idDokazD:Int,tipDokazaD: String, opisD: String, zlocinD: ZlocinR?, zrtvaD: ZrtvaR?, statusD: Int): DokazR?

    suspend fun insertDokazOsumnjicenog(idDokazOsumnjicenDO:Int,dokazIdDO: DokazR?, osumnjicenIdDO: OsumnjicenR?): DokazOsumnjicenR?

    suspend fun insertSvedok(
        idSvedokS:Int, imeS: String, kontaktS: String, izjavaS: String, zlocinS: ZlocinR?,
        statusSvedokS: String, statusIspitanS: Int, datumS: RealmInstant, zanimanjS: String, polS: String
    ): SvedokR?

    suspend fun insertAlibi(osumnjicenA: OsumnjicenR?, svedokA: SvedokR?, opisA: String, statusAlibijaA: String): AlibiR?

    suspend fun insertMisija(zlocinM: ZlocinR?, nazivM: String, opisM: String, statusM: Int): MisijaR?

    suspend fun insertKontakt(idKontaktK:Int,imeK: String, brojK: String, statusK: Int, zrtvaK: ZrtvaR?):KontaktR?

    suspend fun insertPoruka(tipP: String, sadrzajP: String, datumVremeP: RealmInstant?, zrtvaP: ZrtvaR?, posiljalacP: KontaktR?, statusP: String, sifrovanaP: Boolean): PorukeR?

    suspend fun insertMisijaPoruka(zlocinMP: ZlocinR?, nazivMP: String, porukaMP: PorukeR?, statusMP: Int, posiljalacMP: String): MisijaPorukaR?

    suspend fun insertObdukcija(idObdukcijaO:Int,izvestajO: String, datumO: String, uzrokSmrtiO: String, zrtvaO: ZrtvaR?, informacijeO: String): ObdukcijaR?

    suspend fun insertForenzickiDokaz(idForenzickiDokazFD:Int,tipFD: String, opisFD: String, statusFD: Int, zrtvaFD: ZrtvaR?, vezaFD: String): ForenzickiDokazR?

    suspend fun insertTelefon(idTelefonT:Int,modelT: String, osT: String, zrtvaT: ZrtvaR?, sifraT: String): TelefonR?

    suspend fun insertOdnosOsumnjicenZrtva(idOdnosOOZ:Int,osumnjicenOOZ: OsumnjicenR?, zrtvaOOZ: ZrtvaR?, tipOdnosaOOZ: String): OdnosOsumnjicenZrtvaR?

    suspend fun insertKorisnik(imeK: String, prezimeK: String, korisnickoImeK: String, sifraK: String, emailK: String, nacinPrijaveK: String, idTokenK: String, idTokenLast256K: String): KorisnikRequestR?

    suspend fun insertScoreKorisnika(korisnickoImeK: String, scoreK: Int): ScoreKorisnikaR?

    suspend fun getAllScores(): List<ScoreKorisnikaR>?

    suspend fun insertPitanjeIspitivanjeOsumnjicenog(idPitanjeIspitivanjeOsumnjicenogZ:Int, osumnjicenIdZ: Int, kategorijaZ: String, tekstZ: String, odgovorZ: String, komentarZ: String): PitanjeIspitivanjeOsumnjicenogR?

    suspend fun insertPitanjeIspitivanjeSvedoka(idPitanjeIspitivanjeSvedokaP:Int,svedokZ: SvedokR?, tekstZ: String, odgovorZ: String): PitanjeIspitivanjeSvedokaR?

    suspend fun insertZadatak(idZadatakZ:Int, tekstZ: String, korakZ: String, uradjenZ: Boolean,
        nextZ: ZadatakR?, zlocinZ: ZlocinR?): ZadatakR?

    suspend fun updateZadatak(idZadatakZ: Int, idNextZadatak: Int)

    suspend fun insertDokazZadatak(
        idDokazZadatakZ:Int,tekstZ: String, dokazIdZ: DokazR?, uradjenZ: Boolean, zadatakIdZ: ZadatakR?
    ): DokazZadatakR?

    suspend fun insertForenzickiDokazZadatak(
        idForenzickiDokazZadatakZ:Int, tekstZ: String, forenzickiDokazIdZ: ForenzickiDokazR?, uradjenZ: Boolean,
        zadatakIdZ: ZadatakR?): ForenzickiDokazZadatakR?

    suspend fun insertIspitivanjeOsumnjicenogZadatak(
        idIspitivanjeOsumnjicenogZadatakZ:Int,osumnjicenIdZ: OsumnjicenR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): IspitivanjeOsumnjicenogZadatakR?

    suspend fun insertIspitivanjeSvedokaZadatak(
        idIspitivanjeSvedokaZadatakZ:Int,svedokIdZ: SvedokR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): IspitivanjeSvedokaZadatakR?

    suspend fun insertTelefonZadatak(
        idTelefonZadatakZ:Int,telefonZ: TelefonR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): TelefonZadatakR?

    suspend fun insertPorukeZadatak(porukeIdZ: PorukeR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean): PorukeZadatakR?

    suspend fun insertPacijent(
        idPacijentP: Int, simptomiP: String, statusPacijentaP: String,
        datumPrijaveP: RealmInstant, prijavioP: String, zlocinP: ZlocinR, zrtvaP: ZrtvaR
    ): PacijentR?

    suspend fun insertIzjavaZaPacijenta(
        idIzjavaZaPacijentaI: Int, izjavaI: String, pacijentIdI: PacijentR, osobaP: OsobaR): IzjavaZaPacijentaR?

    suspend fun insertLekarskiTest(idLekarskiTestL: Int, pacijentIdL: PacijentR, izvestajL: String): LekarskiTestR?

    suspend fun insertLokacijeIstrage(idLokacijeIstrageL: Int, mestoL: String, nazivL: String, opisL: String, zlocinIdL: ZlocinR,geoTackaALatitudeL:Double, geoTackaALongitudeL:Double): LokacijeIstrageR?

    suspend fun insertMedicinskiIzvestaj(idMedicinskiIzvestajM: Int, rezimeM: String, CTnalazM: String, MRInalazM: String, krvnaSlikaM: String, toksikoloskeAnalizeM: String, zakljucakM: String, pacijentIdM: PacijentR): MedicinskiIzvestajR?

    suspend fun insertPitanje(idPitanjeP:Int,zlocinIdP: ZlocinR?, tekstP: String): PitanjeR?

    suspend fun insertOdogovor(idOdogovorO:Int,pitanjeIdO: PitanjeR?, tekstOdgovoraO: String, tacanO: Boolean, bodoviO: Int): OdgovorR?

    suspend fun getAllOdgovorForPitanje(pitanjeO: PitanjeR?): List<OdgovorR>?

    suspend fun insertBeleska(idBeleskaB:Int,zlocinIdB: ZlocinR?, tekstB: String, datumB: RealmInstant?): BeleskaR?

    suspend fun insertAplikacija(idAplikacijeA:Int, zrtvaA: ZrtvaR?, nazivA: String, tipA:Int, aktivnaA:Boolean,informacijeA:String): AplikacijaR?

    suspend fun insertTrag(idTragT:Int, forenzickiDokazIdT: ForenzickiDokazR,osumnjicenIdT:OsumnjicenR): TragR?

    suspend fun insertWhatsAppKontakt(idWhatsAppKontaktW:Int,zlocinIdW: ZlocinR?, imeW: String, brojW: String, slikaW: Int): WhatsAppKontaktR?

    suspend fun insertWhatsAppPoruka(idWhatsAppPorukaW:Int, kontaktKoSalje: WhatsAppKontaktR, kontaktKomeSalje: WhatsAppKontaktR, tekstW: String, datumW: RealmInstant?, procitanaW: Boolean): WhatsAppPorukaR?

    suspend fun insertOneContact(idOneContactC:Int,zlocinIdC: ZlocinR?, imeC: String, brojC: String, slikaC: Int?): OneContactR?

    suspend fun insertOneCall(idOneCallC:Int,kontaktC: OneContactR?, datumC: RealmInstant?, propustenC: Boolean, dolazniC: Boolean): OneCallR?

    suspend fun insertGalleryPhoto(idPhotoG:Int,zlocinIdG: ZlocinR?, slikaG: Int, datumG: RealmInstant?, mestoG: String): GalleryR?

    suspend fun insertObicnaPoruka(kontaktKoSaljeO: OneContactR?, kontaktKomeSaljeO: OneContactR?, tekstO: String, datumO: RealmInstant?, procitanaO: Boolean): ObicnaPorukaR?


}