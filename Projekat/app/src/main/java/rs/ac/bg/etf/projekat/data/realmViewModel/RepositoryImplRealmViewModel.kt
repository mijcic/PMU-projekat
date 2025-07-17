package rs.ac.bg.etf.projekat.data.realmViewModel

import android.util.Log
import io.realm.kotlin.Realm
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
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
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonR
import rs.ac.bg.etf.projekat.data.realm.TelefonZadatakR
import rs.ac.bg.etf.projekat.data.realm.TragR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppKontaktR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppPorukaR
import rs.ac.bg.etf.projekat.data.realm.ZadatakR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR
import rs.ac.bg.etf.projekat.data.realm.ZrtvaR
import java.time.Instant
import javax.inject.Inject

class RepositoryImplRealmViewModel @Inject constructor(
    private val realm: Realm
): RepositoryRealmViewModel {

    override fun getRealm(): Realm {
        return realm
    }

    override suspend fun insertTipZlocina(nazivTZ: String): TipZlocinaR? {
        var tipZlocina: TipZlocinaR? = null
        realm.write {
            tipZlocina = query<TipZlocinaR>("nazivTipaZlocina == $0", nazivTZ).find().firstOrNull()

            if (tipZlocina == null) {
                val maxId = query<TipZlocinaR>().find().maxOfOrNull { it.idTipZlocina } ?: 0
                tipZlocina = TipZlocinaR().apply {
                    idTipZlocina = maxId + 1
                    nazivTipaZlocina = nazivTZ
                }
                copyToRealm(tipZlocina!!)
            }
        }
        return tipZlocina
    }

    override suspend fun insertZlocin(idZlocinZ:Int, tipZlocina: TipZlocinaR?, nazivZ: String, datumZ: String, mestoZ: String, opisZ: String, statusZ: String): ZlocinR? {
        var zlocin: ZlocinR? = null
        realm.write {
            val millis = datumZ.toLong()
            val instant = Instant.ofEpochMilli(millis)
            val realmInstantDate = RealmInstant.from(instant.epochSecond, instant.nano)

            // Ako tipZlocina nije unet u bazu, unesite ga
            val existingTipZlocina = query<TipZlocinaR>("nazivTipaZlocina == $0", tipZlocina?.nazivTipaZlocina).find().firstOrNull()
                ?: tipZlocina?.let {
                    copyToRealm(it)
                }

            zlocin = query<ZlocinR>("idZlocin==$0 AND tipZlocinaId == $1 AND naziv == $2 AND datum == $3 AND mesto == $4 AND opis == $5 AND status == $6",
                idZlocinZ,existingTipZlocina, nazivZ, realmInstantDate, mestoZ, opisZ, statusZ).find().firstOrNull()
                ?: ZlocinR().apply {
                    idZlocin = idZlocinZ
                    tipZlocinaId = existingTipZlocina // Povezivanje sa tipom zločina
                    naziv = nazivZ
                    datum = realmInstantDate
                    mesto = mestoZ
                    opis = opisZ
                    status = statusZ
                }
            copyToRealm(zlocin!!)
        }
        return zlocin
    }

    override suspend fun insertOsoba(idOsobaO:Int,imeZ: String, kontaktZ:String, datumZ: RealmInstant, zanimanjeZ: String,polZ: String,zlocinZ: ZlocinR?): OsobaR?{
        var osoba: OsobaR? =null

        realm.write {
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinZ?.idZlocin).find().firstOrNull()
                ?: zlocinZ?.let {
                    copyToRealm(it)
                }


            osoba = query<OsobaR>("idOsoba ==$0 AND ime == $1 AND kontakt == $2 AND datum == $3 AND zanimanje == $4 AND pol == $5 AND zlocinId == $6",
                idOsobaO,imeZ, kontaktZ, datumZ, zanimanjeZ,polZ, existingZlocin).find().firstOrNull()
                ?: OsobaR().apply {
                    idOsoba= idOsobaO
                    ime = imeZ
                    kontakt = kontaktZ
                    datum = datumZ
                    zanimanje = zanimanjeZ
                    pol = polZ
                    zlocinId = existingZlocin
                }
            copyToRealm(osoba!!)
        }
        return osoba
    }

    override suspend fun insertZrtva(idZrtvaZ:Int,
                                     tipZ: String, imeZ: String, detaljiZ: String, statusZ: String,
                                     zlocinZ: ZlocinR?, kontaktZ: String, datumZ: RealmInstant,
                                     zanimanjeZ: String, polZ: String
    ): ZrtvaR? {
        var zrtva: ZrtvaR?=null
        // Unesi osobu i proveri da li je već u bazi
        val osoba = insertOsoba(1,imeZ, kontaktZ, datumZ, zanimanjeZ, polZ, zlocinZ)

        realm.write {
            // Proveri i unesi zlocinZ ako nije u bazi
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinZ?.idZlocin).find().firstOrNull()
                ?: zlocinZ?.let {
                    copyToRealm(it)  // Ako nije u bazi, dodajte ga
                }

            // Proveri i unesi osobu ako nije u bazi
            val existingOsoba = query<OsobaR>("ime == $0 AND kontakt == $1", imeZ, kontaktZ).find().firstOrNull()
                ?: osoba?.let {
                    copyToRealm(it)  // Ako osoba nije u bazi, dodajte je
                }

            // Upit za traženje već postojećeg Zrtva
            zrtva = query<ZrtvaR>(
                "idZrtva ==$0 AND tipZrtve == $1 AND detalji == $2 AND statusZrtva == $3 AND zlocinId == $4 AND osobaId == $5",
                idZrtvaZ,tipZ, detaljiZ, statusZ, existingZlocin, existingOsoba
            ).find().firstOrNull()

            // Ako Zrtva ne postoji, kreirajte novu
            if (zrtva == null) {
                zrtva = ZrtvaR().apply {
                    idZrtva = idZrtvaZ
                    tipZrtve = tipZ
                    detalji = detaljiZ
                    statusZrtva = statusZ
                    zlocinId = existingZlocin
                    osobaId = existingOsoba
                }
                copyToRealm(zrtva!!)  // dodajmo Zrtvu u Realm
            }
        }
        return zrtva
    }

    override suspend fun insertMotiv(opisM: String): MotivR? {
        var motiv: MotivR? = null
        realm.write {
            motiv = query<MotivR>("opis == $0", opisM).find().firstOrNull()
                ?: MotivR().apply {
                    idMotiv = (query<MotivR>().find().maxOfOrNull { it.idMotiv } ?: 0) + 1
                    opis = opisM
                }
            copyToRealm(motiv!!)
        }
        return motiv
    }

    override suspend fun insertOsumnjiceni(
        idOsumnjicenO:Int,imeO: String, statusO: Int, tipOsumnjicenO: String, motivO: MotivR?, zlocinO: ZlocinR?, krivO: Int,
        kontaktO: String, datumO: RealmInstant, zanimanjO: String, polO: String
    ): OsumnjicenR? {
        var osumnjiceni: OsumnjicenR? = null
        var osoba: OsobaR? =null

        realm.write {
            // Ako motivO nije unet u bazu, unesite ga i povežite sa Realm bazom
            val existingMotiv = motivO?.let {
                query<MotivR>("idMotiv == $0", it.idMotiv).find().firstOrNull()
                    ?: copyToRealm(it)  // Ako motiv nije u bazi, dodaj ga u bazu
            }

            // Ako zlocinO nije unet u bazu, unesite ga i povežite sa Realm bazom
            val existingZlocin = zlocinO?.let {
                query<ZlocinR>("idZlocin == $0", it.idZlocin).find().firstOrNull()
                    ?: copyToRealm(it)  // Ako zlocin nije u bazi, dodaj ga u bazu
            }

            val existingOsoba = query<OsobaR>("ime == $0 AND kontakt == $1", imeO, kontaktO).find().firstOrNull()
                ?: osoba?.let {
                    copyToRealm(it)  // Ako osoba nije u bazi, dodajte je
                }

            // Pronađi ili kreiraj novi OsumnjicenR
            osumnjiceni = query<OsumnjicenR>(
                "idOsumnjicen == $0 AND status == $1 AND tipOsumnjicen == $2 AND motiv == $3 AND zlocinId == $4 AND kriv == $5 AND osobaId == $6",
                idOsumnjicenO,statusO, tipOsumnjicenO, existingMotiv, existingZlocin, krivO, existingOsoba
            ).find().firstOrNull()

            if (osumnjiceni == null) {
                osumnjiceni = OsumnjicenR().apply {
                    idOsumnjicen = idOsumnjicenO
                    status = statusO
                    tipOsumnjicen = tipOsumnjicenO
                    motiv = existingMotiv
                    zlocinId = existingZlocin
                    kriv = krivO
                    osobaId = existingOsoba
                }
                copyToRealm(osumnjiceni!!)
            }
        }
        return osumnjiceni
    }

    override suspend fun insertDokaz(idDokazD:Int,tipDokazaD: String, opisD: String, zlocinD: ZlocinR?, zrtvaD: ZrtvaR?, statusD: Int): DokazR? {
        var dokaz: DokazR? = null
        realm.write {
            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinD?.idZlocin).find().firstOrNull()
                ?: zlocinD?.let {
                    copyToRealm(it)
                }

            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaD?.idZrtva).find().firstOrNull()
                ?: zrtvaD?.let {
                    copyToRealm(it)
                }

            dokaz = query<DokazR>("idDokaz ==$0 AND tipDokaza == $1 AND opis == $2 AND zlocinId == $3 AND zrtvaId == $4 AND status == $5",
                idDokazD,tipDokazaD, opisD, existingZlocin, existingZrtva, statusD).find().firstOrNull()
                ?: DokazR().apply {
                    idDokaz = idDokazD
                    tipDokaza = tipDokazaD
                    opis = opisD
                    zlocinId = existingZlocin
                    zrtvaId = existingZrtva
                    status = statusD
                }
            copyToRealm(dokaz!!)
        }
        return dokaz
    }

    override suspend fun insertDokazOsumnjicenog(idDokazOsumnjicenDO:Int,dokazIdDO: DokazR?, osumnjicenIdDO: OsumnjicenR?): DokazOsumnjicenR? {
        var dokazOsumnjicenog: DokazOsumnjicenR? = null
        realm.write {
            // Ako dokaz nije unet u bazu, unesite ga
            val existingDokaz = query<DokazR>("idDokaz == $0", dokazIdDO?.idDokaz).find().firstOrNull()
                ?: dokazIdDO?.let {
                    copyToRealm(it)
                }

            val existingOsumnjiceni = query<OsumnjicenR>("idOsumnjicen == $0", osumnjicenIdDO?.idOsumnjicen).find().firstOrNull()
                ?: osumnjicenIdDO?.let {
                    copyToRealm(it)
                }

            dokazOsumnjicenog = query<DokazOsumnjicenR>("idDokazOsumnjicen ==$0 AND dokazId == $1 AND osumnjicenId == $2", idDokazOsumnjicenDO,existingDokaz, existingOsumnjiceni).find().firstOrNull()
                ?: DokazOsumnjicenR().apply {
                    idDokazOsumnjicen = idDokazOsumnjicenDO
                    dokazId = existingDokaz
                    osumnjicenId = existingOsumnjiceni
                }
            copyToRealm(dokazOsumnjicenog!!)
        }
        return dokazOsumnjicenog
    }

    override suspend fun insertSvedok(
        idSvedokS:Int, imeS: String, kontaktS: String, izjavaS: String, zlocinS: ZlocinR?,
        statusSvedokS: String, statusIspitanS: Int, datumS: RealmInstant, zanimanjS: String, polS: String
    ): SvedokR? {
        var svedok: SvedokR? = null

        var osoba: OsobaR? =null


        realm.write {
            val existingZlocin = zlocinS?.let {
                query<ZlocinR>("idZlocin == $0", it.idZlocin).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            val existingOsoba = query<OsobaR>("ime == $0 AND kontakt == $1", imeS, kontaktS).find().firstOrNull()
                ?: osoba?.let {
                    copyToRealm(it)
                }

            svedok = query<SvedokR>(
                "idSvedok == $0 AND izjava == $1 AND statusSvedok == $2 AND statusIspitan == $3 AND zlocinId == $4 AND osobaId == $5",
                idSvedokS,izjavaS, statusSvedokS, statusIspitanS, existingZlocin, existingOsoba
            ).find().firstOrNull()

            if (svedok == null) {
                svedok = SvedokR().apply {
                    idSvedok = idSvedokS
                    izjava = izjavaS
                    statusSvedok = statusSvedokS
                    statusIspitan = statusIspitanS
                    zlocinId = existingZlocin
                    osobaId = existingOsoba
                }
                copyToRealm(svedok!!)
            }
        }
        return svedok
    }

    override suspend fun insertAlibi(osumnjicenA: OsumnjicenR?, svedokA: SvedokR?, opisA: String, statusAlibijaA: String): AlibiR? {
        var alibi: AlibiR? = null
        realm.write {
            // Ako osumnjiceni nije unet u bazu, unesite ga
            val existingOsumnjiceni = query<OsumnjicenR>("idOsumnjicen == $0", osumnjicenA?.idOsumnjicen).find().firstOrNull()
                ?: osumnjicenA?.let {
                    copyToRealm(it)
                }

            // Ako svedok nije unet u bazu, unesite ga
            val existingSvedok = query<SvedokR>("idSvedok == $0", svedokA?.idSvedok).find().firstOrNull()
                ?: svedokA?.let {
                    copyToRealm(it)
                }

            alibi = query<AlibiR>("osumnjicenId == $0 AND svedokId == $1 AND opis == $2 AND statusAlibija == $3",
                existingOsumnjiceni, existingSvedok, opisA, statusAlibijaA).find().firstOrNull()
                ?: AlibiR().apply {
                    idAlibi = (query<AlibiR>().find().maxOfOrNull { it.idAlibi } ?: 0) + 1
                    osumnjicenId = existingOsumnjiceni
                    svedokId = existingSvedok
                    opis = opisA
                    statusAlibija = statusAlibijaA
                }
            copyToRealm(alibi!!)
        }
        return alibi
    }

    override suspend fun insertMisija(zlocinM: ZlocinR?, nazivM: String, opisM: String, statusM: Int): MisijaR? {
        var misija: MisijaR? = null
        realm.write {
            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinM?.idZlocin).find().firstOrNull()
                ?: zlocinM?.let {
                    copyToRealm(it)
                }

            misija = query<MisijaR>("zlocinId == $0 AND naziv == $1 AND opis == $2 AND status == $3",
                existingZlocin, nazivM, opisM, statusM).find().firstOrNull()
                ?: MisijaR().apply {
                    idMisija = (query<MisijaR>().find().maxOfOrNull { it.idMisija } ?: 0) + 1
                    zlocinId = existingZlocin
                    naziv = nazivM
                    opis = opisM
                    status = statusM
                }
            copyToRealm(misija!!)
        }
        return misija
    }

    override suspend fun insertKontakt(idKontaktK:Int,imeK: String, brojK: String, statusK: Int, zrtvaK: ZrtvaR?): KontaktR? {
        var kontakt: KontaktR? = null
        realm.write {
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaK?.idZrtva).find().firstOrNull()
                ?: zrtvaK?.let {
                    copyToRealm(it)
                }

            kontakt = query<KontaktR>("idKontakt ==$0 AND ime == $1 AND broj == $2 AND status == $3 AND zrtvaId == $4",
                idKontaktK,imeK, brojK, statusK, existingZrtva).find().firstOrNull()
                ?: KontaktR().apply {
                    idKontakt = idKontaktK
                    ime = imeK
                    broj = brojK
                    status = statusK
                    zrtvaId = existingZrtva
                }
            copyToRealm(kontakt!!)
        }
        return kontakt
    }

    override suspend fun insertPoruka(tipP: String, sadrzajP: String, datumVremeP: RealmInstant?, zrtvaP: ZrtvaR?, posiljalacP: KontaktR?, statusP: String, sifrovanaP: Boolean): PorukeR? {
        var poruka: PorukeR? = null
        realm.write {
            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaP?.idZrtva).find().firstOrNull()
                ?: zrtvaP?.let {
                    copyToRealm(it)
                }

            // Ako posiljalac nije unet u bazu, unesite ga
            val existingPosiljalac = query<KontaktR>("idKontakt == $0", posiljalacP?.idKontakt).find().firstOrNull()
                ?: posiljalacP?.let {
                    copyToRealm(it)
                }

            poruka = query<PorukeR>("tipPoruke == $0 AND sadrzaj == $1 AND datumVreme == $2 AND zrtvaId == $3 AND posiljalacId == $4 AND statusPoruke == $5 AND sifrovana == $6",
                tipP, sadrzajP, datumVremeP, existingZrtva, existingPosiljalac, statusP, sifrovanaP).find().firstOrNull()
                ?: PorukeR().apply {
                    idPoruke = (query<PorukeR>().find().maxOfOrNull { it.idPoruke } ?: 0) + 1
                    tipPoruke = tipP
                    sadrzaj = sadrzajP
                    datumVreme = datumVremeP
                    zrtvaId = existingZrtva
                    posiljalacId = existingPosiljalac
                    statusPoruke = statusP
                    sifrovana = sifrovanaP
                }
            copyToRealm(poruka!!)
        }
        return poruka
    }

    override suspend fun insertMisijaPoruka(zlocinMP: ZlocinR?, nazivMP: String, porukaMP: PorukeR?, statusMP: Int, posiljalacMP: String): MisijaPorukaR? {
        var misijaPoruka: MisijaPorukaR? = null
        realm.write {
            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinMP?.idZlocin).find().firstOrNull()
                ?: zlocinMP?.let {
                    copyToRealm(it)
                }

            // Ako poruka nije uneta u bazu, unesite je
            val existingPoruka = query<PorukeR>("idPoruke == $0", porukaMP?.idPoruke).find().firstOrNull()
                ?: porukaMP?.let {
                    copyToRealm(it)
                }

            misijaPoruka = query<MisijaPorukaR>("zlocinId == $0 AND naziv == $1 AND poruka == $2 AND status == $3 AND posiljalac == $4",
                existingZlocin, nazivMP, existingPoruka, statusMP, posiljalacMP).find().firstOrNull()
                ?: MisijaPorukaR().apply {
                    idMisija = (query<MisijaPorukaR>().find().maxOfOrNull { it.idMisija } ?: 0) + 1
                    zlocinId = existingZlocin
                    naziv = nazivMP
                    poruka = existingPoruka
                    status = statusMP
                    posiljalac = posiljalacMP
                }
            copyToRealm(misijaPoruka!!)
        }
        return misijaPoruka
    }

    override suspend fun insertObdukcija(idObdukcijaO:Int,izvestajO: String, datumO: String, uzrokSmrtiO: String, zrtvaO: ZrtvaR?, informacijeO: String): ObdukcijaR? {
        var obdukcija: ObdukcijaR? = null
        realm.write {
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaO?.idZrtva).find().firstOrNull()
                ?: zrtvaO?.let {
                    copyToRealm(it)
                }

            val millis = datumO.toLong()
            val instant = Instant.ofEpochMilli(millis)
            val realmInstantDateO = RealmInstant.from(instant.epochSecond, instant.nano)


            obdukcija = query<ObdukcijaR>("idObdukcija ==$0 AND izvestaj == $1 AND datum == $2 AND uzrokSmrti == $3 AND zrtvaId == $4 AND informacije == $5",
                idObdukcijaO,izvestajO, realmInstantDateO, uzrokSmrtiO, existingZrtva, informacijeO).find().firstOrNull()
                ?: ObdukcijaR().apply {
                    idObdukcija = idObdukcijaO
                    izvestaj = izvestajO
                    datum = realmInstantDateO
                    uzrokSmrti = uzrokSmrtiO
                    zrtvaId = existingZrtva
                    informacije = informacijeO
                }
            copyToRealm(obdukcija!!)
        }
        return obdukcija
    }

    override suspend fun insertForenzickiDokaz(idForenzickiDokazFD:Int,tipFD: String, opisFD: String, statusFD: Int, zrtvaFD: ZrtvaR?, vezaFD: String): ForenzickiDokazR? {
        var forenzickiDokaz: ForenzickiDokazR? = null
        realm.write {
            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaFD?.idZrtva).find().firstOrNull()
                ?: zrtvaFD?.let {
                    copyToRealm(it)
                }

            forenzickiDokaz = query<ForenzickiDokazR>("idForenzickiDokaz ==$0 AND tipForenzickiDokaz == $1 AND opis == $2 AND status == $3 AND zrtvaId == $4 AND veza == $5",
                idForenzickiDokazFD,tipFD, opisFD, statusFD, existingZrtva, vezaFD).find().firstOrNull()
                ?: ForenzickiDokazR().apply {
                    idForenzickiDokaz = idForenzickiDokazFD
                    tipForenzickiDokaz = tipFD
                    opis = opisFD
                    status = statusFD
                    zrtvaId = existingZrtva
                    veza = vezaFD
                }
            copyToRealm(forenzickiDokaz!!)
        }
        return forenzickiDokaz
    }

    override suspend fun insertTelefon(idTelefonT:Int,modelT: String, osT: String, zrtvaT: ZrtvaR?, sifraT: String): TelefonR? {
        var telefon: TelefonR? = null
        realm.write {
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaT?.idZrtva).find().firstOrNull()
                ?: zrtvaT?.let {
                    copyToRealm(it)
                }

            telefon = query<TelefonR>("idTelefon ==$0 AND model == $1 AND os == $2 AND zrtvaId == $3 AND sifra == $4",
                idTelefonT,modelT, osT, existingZrtva, sifraT).find().firstOrNull()
                ?: TelefonR().apply {
                    idTelefon = idTelefonT
                    model = modelT
                    os = osT
                    zrtvaId = existingZrtva
                    sifra = sifraT
                }
            copyToRealm(telefon!!)
        }
        return telefon
    }

    override suspend fun insertOdnosOsumnjicenZrtva(idOdnosOOZ:Int,osumnjicenOOZ: OsumnjicenR?, zrtvaOOZ: ZrtvaR?, tipOdnosaOOZ: String): OdnosOsumnjicenZrtvaR? {
        var odnos: OdnosOsumnjicenZrtvaR? = null
        realm.write {
            val existingOsumnjiceni = query<OsumnjicenR>("idOsumnjicen == $0", osumnjicenOOZ?.idOsumnjicen).find().firstOrNull()
                ?: osumnjicenOOZ?.let {
                    copyToRealm(it)
                }

            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaOOZ?.idZrtva).find().firstOrNull()
                ?: zrtvaOOZ?.let {
                    copyToRealm(it)
                }

            odnos = query<OdnosOsumnjicenZrtvaR>("idOdnos ==$0 AND osumnjicenId == $1 AND zrtvaId == $2 AND tipOdnosa == $3", idOdnosOOZ,existingOsumnjiceni, existingZrtva, tipOdnosaOOZ).find().firstOrNull()
                ?: OdnosOsumnjicenZrtvaR().apply {
                    idOdnos = idOdnosOOZ
                    osumnjicenId = existingOsumnjiceni
                    zrtvaId = existingZrtva
                    tipOdnosa = tipOdnosaOOZ
                }
            copyToRealm(odnos!!)
        }
        return odnos
    }

    override suspend fun insertKorisnik(imeK: String, prezimeK: String, korisnickoImeK: String, sifraK: String, emailK: String, nacinPrijaveK: String, idTokenK: String, idTokenLast256K: String): KorisnikRequestR? {
        var requestKorisnik: KorisnikRequestR? = null
        realm.write {
            requestKorisnik = query<KorisnikRequestR>("korisnickoIme == $0 OR email == $1", korisnickoImeK, emailK).find().firstOrNull()
                ?: KorisnikRequestR().apply {
                    idKorisnik = (query<KorisnikRequestR>().find().maxOfOrNull { it.idKorisnik } ?: 0) + 1
                    ime = imeK
                    prezime = prezimeK
                    korisnickoIme = korisnickoImeK
                    sifra = sifraK
                    email = emailK
                    nacinPrijave = nacinPrijaveK
                    idToken = idTokenK
                    idTokenLast256 = idTokenLast256K
                }
            copyToRealm(requestKorisnik!!)
        }
        return requestKorisnik
    }

    override suspend fun insertPitanjeIspitivanjeOsumnjicenog(idPitanjeIspitivanjeOsumnjicenogZ:Int, osumnjicenIdZ: Int, kategorijaZ: String, tekstZ: String, odgovorZ: String, komentarZ: String): PitanjeIspitivanjeOsumnjicenogR? {
        var pitanje: PitanjeIspitivanjeOsumnjicenogR? = null
        realm.write {
            val existingOsumnjicen = query<OsumnjicenR>("idOsumnjicen == $0", osumnjicenIdZ).find().firstOrNull()

            pitanje = query<PitanjeIspitivanjeOsumnjicenogR>("idPitanjeIspitivanjeOsumnjicenog ==$0 AND kategorija == $1 AND tekst == $2 AND odgovor == $3 AND komentar == $4 AND osumnjicenId == $5",
                idPitanjeIspitivanjeOsumnjicenogZ,kategorijaZ, tekstZ, odgovorZ, komentarZ, existingOsumnjicen).find().firstOrNull()
                ?: PitanjeIspitivanjeOsumnjicenogR().apply {
                    idPitanjeIspitivanjeOsumnjicenog = idPitanjeIspitivanjeOsumnjicenogZ
                    kategorija = kategorijaZ
                    tekst = tekstZ
                    odgovor = odgovorZ
                    komentar = komentarZ
                    osumnjicenId = existingOsumnjicen
                }

            copyToRealm(pitanje!!)
        }
        return pitanje
    }

    override suspend fun insertPitanjeIspitivanjeSvedoka(idPitanjeIspitivanjeSvedokaP:Int,svedokZ: SvedokR?, tekstZ: String, odgovorZ: String): PitanjeIspitivanjeSvedokaR? {
        var pitanje: PitanjeIspitivanjeSvedokaR? = null
        realm.write {
            val existingSvedok = query<SvedokR>("idSvedok == $0", svedokZ?.idSvedok).find().firstOrNull()
                ?: svedokZ?.let {
                    copyToRealm(it)
                }

            pitanje = query<PitanjeIspitivanjeSvedokaR>("idPitanjeIspitivanjeSvedoka ==$0 AND tekst == $1 AND odgovor == $2 AND svedokId == $3",
                idPitanjeIspitivanjeSvedokaP,tekstZ, odgovorZ, existingSvedok).find().firstOrNull()
                ?: PitanjeIspitivanjeSvedokaR().apply {

                    idPitanjeIspitivanjeSvedoka = idPitanjeIspitivanjeSvedokaP
                    tekst = tekstZ
                    odgovor = odgovorZ
                    next = idPitanjeIspitivanjeSvedoka+1
                    svedokId = existingSvedok
                }

            copyToRealm(pitanje!!)
        }
        return pitanje
    }

    override suspend fun insertZadatak(
        idZadatakZ:Int, tekstZ: String, korakZ: String, uradjenZ: Boolean,
        nextZ: ZadatakR?,
        zlocinZ: ZlocinR?
    ): ZadatakR? {
        var zadatak: ZadatakR? = null

        realm.write {
            val existingZlocin = zlocinZ?.let {
                query<ZlocinR>("idZlocin == $0", it.idZlocin).find().firstOrNull()
                    ?: copyToRealm(it)
            }


            val existingZadatak = nextZ?.let {
                query<ZadatakR>("idZadatak == $0", it.idZadatak).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            zadatak = query<ZadatakR>(
                "idZadatak == $0 AND tekst == $1 AND korak == $2 AND uradjen == $3 AND next == $4 AND zlocinId == $5",
                idZadatakZ,tekstZ, korakZ, uradjenZ, existingZadatak, existingZlocin
            ).find().firstOrNull() ?: ZadatakR().apply {
                idZadatak = idZadatakZ
                tekst = tekstZ
                korak = korakZ
                next = existingZadatak
                zlocinId = existingZlocin
                uradjen=uradjenZ
            }

            copyToRealm(zadatak!!)
        }
        return zadatak
    }

    override suspend fun updateZadatak(idZadatakZ: Int, idNextZadatak: Int) {
        realm.write {
            val existingZadatak = query<ZadatakR>("idZadatak == $0", idZadatakZ).find().firstOrNull()

            val existingNextZadatak = query<ZadatakR>("idZadatak == $0", idNextZadatak).find().firstOrNull()

            if (existingZadatak != null && existingNextZadatak != null) {
                existingZadatak.next = existingNextZadatak
            }
        }
    }

    override suspend fun insertDokazZadatak(
        idDokazZadatakZ:Int,tekstZ: String, dokazIdZ: DokazR?, uradjenZ: Boolean,
        zadatakIdZ: ZadatakR?
    ): DokazZadatakR? {
        var dokazZadatak: DokazZadatakR? = null

        realm.write {
            val existingZadatak = zadatakIdZ?.let {
                query<ZadatakR>("idZadatak == $0", it.idZadatak).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            val existingDokaz = dokazIdZ?.let {
                query<DokazR>("idDokaz == $0", it.idDokaz).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            dokazZadatak = query<DokazZadatakR>(
                "idDokazZadatak ==$0 AND tekst == $1 AND dokazId == $2 AND uradjen == $3 AND zadatakId == $4",
                idDokazZadatakZ,tekstZ, existingDokaz, uradjenZ, existingZadatak
            ).find().firstOrNull() ?: DokazZadatakR().apply {
                idDokazZadatak = idDokazZadatakZ
                tekst = tekstZ
                dokazId = existingDokaz
                zadatakId = existingZadatak
                uradjen=uradjenZ
            }

            copyToRealm(dokazZadatak!!)
        }
        return dokazZadatak
    }

    override suspend fun insertForenzickiDokazZadatak(
        idForenzickiDokazZadatakZ:Int, tekstZ: String, forenzickiDokazIdZ: ForenzickiDokazR?, uradjenZ: Boolean,
        zadatakIdZ: ZadatakR?
    ): ForenzickiDokazZadatakR? {
        var forenzickiDokazZadatak: ForenzickiDokazZadatakR? = null

        realm.write {
            val existingZadatak = zadatakIdZ?.let {
                query<ZadatakR>("idZadatak == $0", it.idZadatak).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            val existingForenzickiDokaz = forenzickiDokazIdZ?.let {
                query<ForenzickiDokazR>("idForenzickiDokaz == $0", it.idForenzickiDokaz).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            forenzickiDokazZadatak = query<ForenzickiDokazZadatakR>(
                "idForenzickiDokazZadatak ==$0 AND tekst == $1 AND forenzickiDokazId == $2 AND uradjen == $3 AND zadatakId == $4",
                idForenzickiDokazZadatakZ,tekstZ, existingForenzickiDokaz, uradjenZ, existingZadatak
            ).find().firstOrNull() ?: ForenzickiDokazZadatakR().apply {
                idForenzickiDokazZadatak = idForenzickiDokazZadatakZ
                tekst = tekstZ
                forenzickiDokazId = existingForenzickiDokaz
                zadatakId = existingZadatak
                uradjen=uradjenZ
            }
            copyToRealm(forenzickiDokazZadatak!!)
        }
        return forenzickiDokazZadatak
    }

    override suspend fun insertIspitivanjeOsumnjicenogZadatak(
        idIspitivanjeOsumnjicenogZadatakZ:Int,osumnjicenIdZ: OsumnjicenR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): IspitivanjeOsumnjicenogZadatakR? {
        var ispitivanjeOsumnjicenogZadatak: IspitivanjeOsumnjicenogZadatakR? = null

        realm.write {
            val existingZadatak = zadatakIdZ?.let {
                query<ZadatakR>("idZadatak == $0", it.idZadatak).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            val existingOsumnjicenog = osumnjicenIdZ?.let {
                query<OsumnjicenR>("idOsumnjicen == $0", it.idOsumnjicen).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            ispitivanjeOsumnjicenogZadatak = query<IspitivanjeOsumnjicenogZadatakR>(
                "idIspitivanjeOsumnjicenogZadatak ==$0 AND osumnjicenId == $1 AND zadatakId == $2 AND uradjen ==$3",
                idIspitivanjeOsumnjicenogZadatakZ,existingOsumnjicenog, existingZadatak, uradjenZ
            ).find().firstOrNull() ?: IspitivanjeOsumnjicenogZadatakR().apply {
                idIspitivanjeOsumnjicenogZadatak =idIspitivanjeOsumnjicenogZadatakZ
                osumnjicenId = existingOsumnjicenog
                zadatakId = existingZadatak
                uradjen = uradjenZ
            }

            copyToRealm(ispitivanjeOsumnjicenogZadatak!!)
        }
        return ispitivanjeOsumnjicenogZadatak
    }

    override suspend fun insertIspitivanjeSvedokaZadatak(
        idIspitivanjeSvedokaZadatakZ:Int,svedokIdZ: SvedokR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): IspitivanjeSvedokaZadatakR? {
        var ispitivanjeSvedokaZadatak: IspitivanjeSvedokaZadatakR? = null

        realm.write {
            val existingZadatak = zadatakIdZ?.let {
                query<ZadatakR>("idZadatak == $0", it.idZadatak).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            val existingSvedok = svedokIdZ?.let {
                query<SvedokR>("idSvedok == $0", it.idSvedok).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            ispitivanjeSvedokaZadatak = query<IspitivanjeSvedokaZadatakR>(
                "idIspitivanjeSvedokaZadatak ==$0 AND svedokId == $1 AND zadatakId == $2 AND uradjen ==$3",
                idIspitivanjeSvedokaZadatakZ,existingSvedok, existingZadatak, uradjenZ
            ).find().firstOrNull() ?: IspitivanjeSvedokaZadatakR().apply {
                idIspitivanjeSvedokaZadatak =idIspitivanjeSvedokaZadatakZ
                svedokId = existingSvedok
                zadatakId = existingZadatak
                uradjen = uradjenZ
            }

            copyToRealm(ispitivanjeSvedokaZadatak!!)
        }
        return ispitivanjeSvedokaZadatak
    }

    override suspend fun insertTelefonZadatak(
        idTelefonZadatakZ:Int,telefonZ: TelefonR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): TelefonZadatakR? {
        var telefonZadatak: TelefonZadatakR? = null

        realm.write {
            val existingZadatak = zadatakIdZ?.let {
                query<ZadatakR>("idZadatak == $0", it.idZadatak).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            val existingTelefon = telefonZ?.let {
                query<TelefonR>("idTelefon == $0", it.idTelefon).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            telefonZadatak = query<TelefonZadatakR>(
                "idTelefonZadatak ==$0 AND telefonId == $1 AND zadatakId == $2 AND uradjen ==$3",
                idTelefonZadatakZ,existingTelefon, existingZadatak, uradjenZ
            ).find().firstOrNull() ?: TelefonZadatakR().apply {
                idTelefonZadatak =idTelefonZadatakZ
                telefonId = existingTelefon
                zadatakId = existingZadatak
                uradjen = uradjenZ
            }

            copyToRealm(telefonZadatak!!)
        }
        return telefonZadatak
    }

    override suspend fun insertPorukeZadatak(
        porukeIdZ: PorukeR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): PorukeZadatakR? {
        var porukeZadatak: PorukeZadatakR? = null

        realm.write {
            val existingZadatak = zadatakIdZ?.let {
                query<ZadatakR>("idZadatak == $0", it.idZadatak).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            //val existingPoruke = porukeIdZ?.let {
            //  query<PorukeR>("idPoruke == $0", it.idPoruke).find().firstOrNull()
            //    ?: copyToRealm(it)
            //}

            porukeZadatak = query<PorukeZadatakR>(
                "porukeId == $0 AND zadatakId == $1 AND uradjen ==$2",
                null, existingZadatak, uradjenZ
            ).find().firstOrNull() ?: PorukeZadatakR().apply {
                idPorukeZadatak =
                    (query<PorukeZadatakR>().find().maxOfOrNull { it.idPorukeZadatak } ?: 0) + 1
                porukeId = null
                zadatakId = existingZadatak
                uradjen = uradjenZ
            }

            copyToRealm(porukeZadatak!!)
        }
        return porukeZadatak
    }

    override suspend fun insertPacijent(
        idPacijentP: Int, simptomiP: String, statusPacijentaP: String,
        datumPrijaveP: RealmInstant, prijavioP: String, zlocinP: ZlocinR, zrtvaP: ZrtvaR
    ): PacijentR? {
        var pacijent: PacijentR? = null
        realm.write {
            val existingZlocin =
                query<ZlocinR>("idZlocin == $0", zlocinP.idZlocin).find().firstOrNull()
                    ?: zlocinP?.let {
                        copyToRealm(it)
                    }

            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaP.idZrtva).find().firstOrNull()
                ?: zrtvaP?.let {
                    copyToRealm(it)
                }

            pacijent = query<PacijentR>(
                "idPacijent ==$0 AND simptomi == $1 AND statusPacijenta == $2 AND datumPrijave ==$3 AND prijavio ==$4 AND zlocinId==$5 AND zrtvaId==$6",
                idPacijentP,
                simptomiP,
                statusPacijentaP,
                datumPrijaveP,
                prijavioP,
                existingZlocin,
                existingZrtva
            ).find().firstOrNull()
                ?: PacijentR().apply {
                    idPacijent = idPacijentP
                    simptomi = simptomiP
                    statusPacijenta = statusPacijentaP
                    datumPrijave = datumPrijaveP
                    prijavio = prijavioP
                    zlocinId = existingZlocin
                    zrtvaId = existingZrtva
                }
            copyToRealm(pacijent!!)
        }
        return pacijent
    }

    override suspend fun insertIzjavaZaPacijenta(
        idIzjavaZaPacijentaI: Int, izjavaI: String, pacijentIdI: PacijentR, osobaP: OsobaR
    ): IzjavaZaPacijentaR? {
        var izjavaZ: IzjavaZaPacijentaR? = null
        realm.write {


            val existingOsoba = query<OsobaR>("idOsoba == $0", osobaP.idOsoba).find().firstOrNull()
                ?: osobaP?.let {
                    copyToRealm(it)
                }

            val existingPacijent = query<PacijentR>("idPacijent == $0", pacijentIdI.idPacijent).find().firstOrNull()
                ?:  pacijentIdI?.let {
                    copyToRealm(it)
                }

            izjavaZ = query<IzjavaZaPacijentaR>(
                "idIzjavaZaPacijenta ==$0 AND izjava == $1 AND pacijentId == $2 AND osobaId ==$3",
                idIzjavaZaPacijentaI,
                izjavaI,
                existingPacijent,
                existingOsoba
            ).find().firstOrNull()
                ?: IzjavaZaPacijentaR().apply {
                    idIzjavaZaPacijenta = idIzjavaZaPacijentaI
                    izjava = izjavaI
                    pacijentId = existingPacijent
                    osobaId = existingOsoba
                }
            copyToRealm(izjavaZ!!)
        }
        return izjavaZ
    }

    override suspend fun insertLekarskiTest(idLekarskiTestL: Int, pacijentIdL: PacijentR, izvestajL: String): LekarskiTestR? {
        var lekarskiTest: LekarskiTestR? = null
        realm.write {
            val existingPacijent = query<PacijentR>("idPacijent == $0", pacijentIdL.idPacijent).find().firstOrNull()
                ?:  pacijentIdL?.let {
                    copyToRealm(it)
                }

            lekarskiTest = query<LekarskiTestR>(
                "idLekarskiTest ==$0 AND pacijentId == $1 AND izvestaj == $2",
                idLekarskiTestL,
                existingPacijent,
                izvestajL
            ).find().firstOrNull()
                ?: LekarskiTestR().apply {
                    idLekarskiTest = idLekarskiTestL
                    pacijentId = existingPacijent
                    izvestaj = izvestajL
                }
            copyToRealm(lekarskiTest!!)
        }
        return lekarskiTest
    }

    override suspend fun insertLokacijeIstrage(idLokacijeIstrageL: Int, mestoL: String, nazivL: String, opisL: String, zlocinIdL: ZlocinR,geoTackaALatitudeL:Double, geoTackaALongitudeL:Double): LokacijeIstrageR? {
        var lokacijaIstrage: LokacijeIstrageR? = null
        realm.write {
            val existingZlocin =
                query<ZlocinR>("idZlocin == $0", zlocinIdL.idZlocin).find().firstOrNull()
                    ?: zlocinIdL?.let {
                        copyToRealm(it)
                    }

            lokacijaIstrage = query<LokacijeIstrageR>(
                "idLokacijeIstrage ==$0 AND mesto == $1 AND naziv == $2 AND opis == $3 AND zlocinId == $4",
                idLokacijeIstrageL,
                mestoL,
                nazivL,
                opisL,
                existingZlocin,geoTackaALatitudeL,geoTackaALongitudeL
            ).find().firstOrNull()
                ?: LokacijeIstrageR().apply {
                    idLokacijeIstrage = idLokacijeIstrageL
                    mesto = mestoL
                    naziv = nazivL
                    opis = opisL
                    zlocinId = existingZlocin
                    geoTackaALatitude = geoTackaALatitudeL
                    geoTackaALongitude = geoTackaALongitudeL
                }
            copyToRealm(lokacijaIstrage!!)
        }
        return lokacijaIstrage
    }

    override suspend fun insertMedicinskiIzvestaj(idMedicinskiIzvestajM: Int, rezimeM: String, CTnalazM: String, MRInalazM: String, krvnaSlikaM: String, toksikoloskeAnalizeM: String, zakljucakM: String, pacijentIdM: PacijentR): MedicinskiIzvestajR? {
            var medicinskiIzvestaj: MedicinskiIzvestajR? = null
        Log.d("GEMINI MED",idMedicinskiIzvestajM.toString())
        realm.write {
            val existingPacijent = query<PacijentR>("idPacijent == $0", pacijentIdM.idPacijent).find().firstOrNull()
                ?:  pacijentIdM?.let {
                    copyToRealm(it)
                }


            medicinskiIzvestaj = query<MedicinskiIzvestajR>(
                "idMedicinskiIzvestaj ==$0 AND rezime == $1 AND CTnalaz == $2 AND MRInalaz == $3 AND krvnaSlika == $4 AND toksikoloskeAnalize == $5 AND zakljucak == $6 AND pacijentId == $7",
                idMedicinskiIzvestajM, rezimeM, CTnalazM, MRInalazM, krvnaSlikaM, toksikoloskeAnalizeM, zakljucakM, existingPacijent
            ).find().firstOrNull()
                ?: MedicinskiIzvestajR().apply {
                    idMedicinskiIzvestaj = idMedicinskiIzvestajM
                    rezime = rezimeM
                    CTnalaz = CTnalazM
                    MRInalaz = MRInalazM
                    krvnaSlika = krvnaSlikaM
                    toksikoloskeAnalize = toksikoloskeAnalizeM
                    zakljucak = zakljucakM
                    pacijentId = existingPacijent
                }
            copyToRealm(medicinskiIzvestaj!!)
        }
        return medicinskiIzvestaj
    }

    override suspend fun insertPitanje(idPitanjeP:Int,zlocinIdP: ZlocinR?, tekstP: String): PitanjeR? {
        var pitanje: PitanjeR? = null
        realm.write {
            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinIdP?.idZlocin).find().firstOrNull()
                ?: zlocinIdP?.let {
                    copyToRealm(it)
                }

            pitanje = query<PitanjeR>("idPitanje ==$0 AND zlocinId == $1 AND tekst == $2", idPitanjeP,existingZlocin, tekstP).find().firstOrNull()
                ?: PitanjeR().apply {
                    idPitanje = idPitanjeP
                    zlocinId = existingZlocin
                    tekst = tekstP
                }
            copyToRealm(pitanje!!)
        }
        return pitanje
    }

    override suspend fun insertOdogovor(idOdogovorO:Int,pitanjeIdO: PitanjeR?, tekstOdgovoraO: String, tacanO: Boolean, bodoviO: Int): OdgovorR? {
        var odgovor: OdgovorR? = null
        realm.write {
            // Ako pitanje nije uneto u bazu, unesite ga
            val existingPitanje = query<PitanjeR>("idPitanje == $0", pitanjeIdO?.idPitanje).find().firstOrNull()
                ?: pitanjeIdO?.let {
                    copyToRealm(it)
                }

            odgovor = query<OdgovorR>("idOdogovor ==$0 AND pitanjeId == $1 AND tekstOdgovora == $2 AND tacan == $3 AND bodovi == $4",
                idOdogovorO,existingPitanje, tekstOdgovoraO, tacanO, bodoviO).find().firstOrNull()
                ?: OdgovorR().apply {
                    idOdogovor = idOdogovorO
                    pitanjeId = existingPitanje
                    tekstOdgovora = tekstOdgovoraO
                    tacan = tacanO
                    bodovi = bodoviO
                }
            copyToRealm(odgovor!!)
        }
        return odgovor
    }

    override suspend fun getAllOdgovorForPitanje(pitanjeO: PitanjeR?): List<OdgovorR>? {
        var existingPitanje: PitanjeR? = null
        realm.write {
            existingPitanje = query<PitanjeR>("idPitanje == $0", pitanjeO?.idPitanje).find().firstOrNull()
                ?: pitanjeO?.let {
                    copyToRealm(it)
                }
        }

        return realm.query<OdgovorR>("pitanjeId == $0", existingPitanje).find()
    }

    override suspend fun insertBeleska(idBeleskaB:Int,zlocinIdB: ZlocinR?, tekstB: String, datumB: RealmInstant?): BeleskaR? {
        var beleska: BeleskaR? = null
        realm.write {
            val existingZlocin = zlocinIdB?.let {
                query<ZlocinR>("idZlocin == $0", it.idZlocin).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            beleska = query<BeleskaR>("idBeleska ==$0 AND zlocinId == $1 AND tekst == $2 AND datum == $3", idBeleskaB,existingZlocin, tekstB, datumB).find().firstOrNull()
                ?: BeleskaR().apply {
                    idBeleska = idBeleskaB
                    this.zlocinId = existingZlocin
                    this.tekst = tekstB
                    this.datum = datumB
                }
            copyToRealm(beleska!!)
        }
        return beleska
    }

    override suspend fun insertAplikacija(idAplikacijeA:Int, zrtvaA: ZrtvaR?, nazivA: String, tipA:Int, aktivnaA:Boolean,informacijeA:String): AplikacijaR? {
        var aplikacija: AplikacijaR? = null
        realm.write {

            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaA?.idZrtva).find().firstOrNull()
                ?: zrtvaA?.let {
                    copyToRealm(it)
                }


            aplikacija = query<AplikacijaR>("idAplikacije ==$0 AND naziv == $1 AND tip == $2 AND zrtvaId == $3 AND aktivna == $4 AND informacije ==$5",
                idAplikacijeA,nazivA, tipA, existingZrtva,aktivnaA,informacijeA).find().firstOrNull()
                ?: AplikacijaR().apply {
                    idAplikacije=idAplikacijeA
                    naziv = nazivA
                    tip= tipA
                    zrtvaId=existingZrtva
                    aktivna = aktivnaA
                    informacije =informacijeA
                }
            copyToRealm(aplikacija!!)
        }
        return aplikacija
    }

    override suspend fun insertTrag(idTragT:Int, forenzickiDokazIdT: ForenzickiDokazR,osumnjicenIdT:OsumnjicenR): TragR? {
        var trag: TragR? = null
        realm.write {

            val existingOsumnjicen = query<OsumnjicenR>("idOsumnjicen == $0", osumnjicenIdT?.idOsumnjicen).find().firstOrNull()
                ?: osumnjicenIdT?.let {
                    copyToRealm(it)
                }


            val existingForenzickiDokaz = query<ForenzickiDokazR>("idForenzickiDokaz == $0", forenzickiDokazIdT?.idForenzickiDokaz).find().firstOrNull()
                ?: forenzickiDokazIdT?.let {
                    copyToRealm(it)
                }

            trag = query<TragR>("idTrag ==$0 AND forenzickiDokazId == $1 AND osumnjicenId == $2",
                idTragT,existingForenzickiDokaz,existingOsumnjicen).find().firstOrNull()
                ?: TragR().apply {
                    var idTrag= idTragT
                    var forenzickiDokazId =existingForenzickiDokaz
                    var osumnjicenId=existingOsumnjicen
                }
            copyToRealm(trag!!)
        }
        return trag
    }

    override suspend fun insertWhatsAppKontakt(idWhatsAppKontaktW:Int,zlocinIdW: ZlocinR?, imeW: String, brojW: String, slikaW: Int): WhatsAppKontaktR? {
        var kontakt: WhatsAppKontaktR? = null
        realm.write {
            val existingZlocin = zlocinIdW?.let {
                query<ZlocinR>("idZlocin == $0", it.idZlocin).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            kontakt = query<WhatsAppKontaktR>("idWhatsAppKontakt ==$0 AND zlocinId == $1 AND ime == $2 AND broj == $3 AND slika == $4", idWhatsAppKontaktW,existingZlocin, imeW, brojW, slikaW).find().firstOrNull()
                ?: WhatsAppKontaktR().apply {
                    idWhatsAppKontakt =  idWhatsAppKontaktW
                    this.zlocinId = existingZlocin
                    this.ime = imeW
                    this.broj = brojW
                    this.slika = slikaW
                }
            copyToRealm(kontakt!!)
        }
        return kontakt
    }

    override suspend fun insertWhatsAppPoruka(idWhatsAppPorukaW:Int, kontaktKoSalje: WhatsAppKontaktR, kontaktKomeSalje: WhatsAppKontaktR, tekstW: String, datumW: RealmInstant?, procitanaW: Boolean): WhatsAppPorukaR? {
        var poruka: WhatsAppPorukaR? = null
        realm.write {
            val existingWhatsAppKontakt1 = query<WhatsAppKontaktR>("idWhatsAppKontakt == $0", kontaktKoSalje?.idWhatsAppKontakt).find().firstOrNull()
                ?: kontaktKoSalje?.let {
                    copyToRealm(it)
                }

            val existingWhatsAppKontakt2 = query<WhatsAppKontaktR>("idWhatsAppKontakt == $0", kontaktKomeSalje?.idWhatsAppKontakt).find().firstOrNull()
                ?: kontaktKomeSalje?.let {
                    copyToRealm(it)
                }

            poruka = query<WhatsAppPorukaR>("idWhatsAppPoruka ==$0 AND kontaktKoSalje == $1 AND kontaktKomeSalje == $2 AND tekst == $3 AND datum == $4 AND procitana == $5", idWhatsAppPorukaW,existingWhatsAppKontakt1, existingWhatsAppKontakt2, tekstW, datumW, procitanaW).find().firstOrNull()
                ?: WhatsAppPorukaR().apply {
                    idWhatsAppPoruka = idWhatsAppPorukaW
                    this.kontaktKoSalje = existingWhatsAppKontakt1
                    this.kontaktKomeSalje = existingWhatsAppKontakt2
                    this.tekst = tekstW
                    this.datum = datumW
                    this.procitana = procitanaW
                }
            copyToRealm(poruka!!)
        }
        return poruka
    }

    override suspend fun insertOneContact(idOneContactC:Int,zlocinIdC: ZlocinR?, imeC: String, brojC: String, slikaC: Int?): OneContactR? {
        var kontakt: OneContactR? = null
        realm.write {
            // Ako zlocin nije u bazi, dodaj ga u bazu
            val existingZlocin = zlocinIdC?.let {
                query<ZlocinR>("idZlocin == $0", it.idZlocin).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            kontakt = query<OneContactR>("idOneContact ==$0 AND zlocinId == $1 AND ime == $2 AND broj == $3 AND slika == $4", idOneContactC,existingZlocin, imeC, brojC, slikaC).find().firstOrNull()
                ?: OneContactR().apply {
                    idOneContact = idOneContactC
                    this.zlocinId = existingZlocin
                    this.ime = imeC
                    this.broj = brojC
                    this.slika = slikaC
                }
            copyToRealm(kontakt!!)
        }
        return kontakt
    }

    override suspend fun insertOneCall(idOneCallC:Int,kontaktC: OneContactR?, datumC: RealmInstant?, propustenC: Boolean, dolazniC: Boolean): OneCallR? {
        var call: OneCallR? = null
        realm.write {
            // Ako kontakt nije unet u bazu, unesite ga
            val existingKontakt = query<OneContactR>("idOneContact == $0", kontaktC?.idOneContact).find().firstOrNull()
                ?: kontaktC?.let {
                    copyToRealm(it)
                }

            call = query<OneCallR>("idOneCall ==$0 AND kontakt == $1 AND datum == $2 AND propusten == $3 AND dolazni == $4", idOneCallC,existingKontakt, datumC, propustenC, dolazniC).find().firstOrNull()
                ?: OneCallR().apply {
                    idOneCall = idOneCallC
                    this.kontakt = existingKontakt
                    this.datum = datumC
                    this.propusten = propustenC
                    this.dolazni = dolazniC
                }
            copyToRealm(call!!)
        }
        return call
    }

    override suspend fun insertGalleryPhoto(idPhotoG:Int,zlocinIdG: ZlocinR?, slikaG: Int, datumG: RealmInstant?, mestoG: String): GalleryR? {
        var photo: GalleryR? = null
        realm.write {
            val existingZlocin = zlocinIdG?.let {
                query<ZlocinR>("idZlocin == $0", it.idZlocin).find().firstOrNull()
                    ?: copyToRealm(it)
            }

            photo = query<GalleryR>("idPhoto ==$0 AND zlocinId == $1 AND slika == $2 AND datum == $3 AND mesto == $4", idPhotoG,existingZlocin, slikaG, datumG, mestoG).find().firstOrNull()
                ?: GalleryR().apply {
                    idPhoto = idPhotoG
                    this.zlocinId = existingZlocin
                    this.slika = slikaG
                    this.datum = datumG
                    this.mesto = mestoG
                }
            copyToRealm(photo!!)
        }
        return photo
    }

    override suspend fun insertObicnaPoruka(kontaktKoSaljeO: OneContactR?, kontaktKomeSaljeO: OneContactR?, tekstO: String, datumO: RealmInstant?, procitanaO: Boolean): ObicnaPorukaR? {
        var poruka: ObicnaPorukaR? = null
        realm.write {
            // Ako kontakt nije unet u bazu, unesite ga
            val existingKontakt1 = query<OneContactR>("idOneContact == $0", kontaktKoSaljeO?.idOneContact).find().firstOrNull()
                ?: kontaktKoSaljeO?.let {
                    copyToRealm(it)
                }

            val existingKontakt2 = query<OneContactR>("idOneContact == $0", kontaktKomeSaljeO?.idOneContact).find().firstOrNull()
                ?: kontaktKomeSaljeO?.let {
                    copyToRealm(it)
                }

            poruka = query<ObicnaPorukaR>("kontaktKoSalje == $0 AND kontaktKomeSalje == $1 AND tekst == $2 AND datum == $3 AND procitana == $4", existingKontakt1, existingKontakt2, tekstO, datumO, procitanaO).find().firstOrNull()
                ?: ObicnaPorukaR().apply {
                    idObicnaPoruka = (query<ObicnaPorukaR>().find().maxOfOrNull { it.idObicnaPoruka } ?: 0) + 1
                    this.kontaktKoSalje = existingKontakt1
                    this.kontaktKomeSalje = existingKontakt2
                    this.tekst = tekstO
                    this.datum = datumO
                    this.procitana = procitanaO
                }
            copyToRealm(poruka!!)
        }
        return poruka
    }


}