package com.example.repository

import com.example.*
import com.example.models.dto.*
import java.sql.Connection

class Repository(private val connection: Connection):RepoInterface {
    override fun getUsedZlocinMurder(): Int? {
        val query =
            "SELECT MIN(uz.zlocinId) AS zlocinId FROM UsedZlocin uz JOIN Zlocin z ON uz.zlocinId = z.idZlocin JOIN TipZlocina tz ON z.tipZlocinaId = tz.idTipZlocina WHERE uz.used = false AND tz.naziv = 'murder'"

        val statement = connection.createStatement()
        val resultSet = statement?.executeQuery(query)

        if (resultSet != null) {
            return if (resultSet.next()) {
                resultSet.getInt("zlocinId").takeIf { !resultSet.wasNull() }
            } else {
                null
            }
        }
        return null
    }

    override fun getUsedZlocinMysteriousSymptoms(): Int? {
        val query =
            "SELECT MIN(uz.zlocinId) AS zlocinId FROM UsedZlocin uz JOIN Zlocin z ON uz.zlocinId = z.idZlocin JOIN TipZlocina tz ON z.tipZlocinaId = tz.idTipZlocina WHERE uz.used = false AND tz.naziv = 'MysteriousSymptoms'"

        val statement = connection.createStatement()
        val resultSet = statement?.executeQuery(query)

        if (resultSet != null) {
            return if (resultSet.next()) {
                resultSet.getInt("zlocinId").takeIf { !resultSet.wasNull() }
            } else {
                null
            }
        }
        return null
    }

    override fun getZlocin(zlocinId:Int): ZlocinData? {
        val query = "SELECT * from zlocin WHERE idZlocin=$zlocinId"

        val statement = connection.createStatement()
        val resultSet = statement?.executeQuery(query)

        if (resultSet != null) {
            return if (resultSet.next()) {
                val idZlocin = resultSet.getInt("idZlocin")
                val tipZlocinaId = resultSet.getInt("tipZlocinaId")

                val naziv = resultSet.getString("naziv")
                val datum = resultSet.getTimestamp("datum")
                val mesto = resultSet.getString("mesto")
                val opis = resultSet.getString("opis")
                val status = resultSet.getString("statusS")
                val zl= ZlocinData(
                    idZlocin = idZlocin,
                    tipZlocinaId = tipZlocinaId,
                    naziv = naziv,
                    datum = datum.time,
                    mesto = mesto,
                    opis = opis,
                    status = status
                )
                return zl
            } else {
                null
            }
        }
        return null
    }

    override fun getTipZlocina(id:Int): TipZlocinaDC? {
        val query = "SELECT * from tipzlocina WHERE idTipZlocina=$id"
        val connection = getDatabaseConnection()
        val statement = connection?.createStatement()
        val resultSet = statement?.executeQuery(query)

        if (resultSet != null) {
            return if (resultSet.next()) {
                val idTip = resultSet.getInt("idTipZlocina")
                val naziv = resultSet.getString("naziv")
                val tipZl= TipZlocinaDC(
                    id =idTip,
                    naziv = naziv
                )
                return tipZl
            } else {
                null
            }
        }
        return null
    }

    override fun getZrtva(id:Int): ZrtvaData? {
        //idZrtva, tipZrtve, detalji, statusZrtva, zlocinId, osobaId
        val query = "SELECT * from Zrtva WHERE zlocinId=$id"

        val statement = connection.createStatement()
        val resultSet = statement?.executeQuery(query)

        if (resultSet != null) {
            return if (resultSet.next()) {
                val idZrtva = resultSet.getInt("idZrtva")
                val tipZrtve = resultSet.getString("tipZrtve")
                val detalji = resultSet.getString("detalji")
                val statusZrtva = resultSet.getString("statusZrtva")
                val zlocinId = resultSet.getInt("zlocinId")
                val osobaId = resultSet.getInt("osobaId")

                val query2 = "SELECT * from Osoba WHERE idOsoba=$osobaId"
                val resultSet2 = statement?.executeQuery(query2)
                var os: OsobaData? = null

                if (resultSet2 != null) {
                    //idOsoba, ime, kontakt, datum, zanimanje, pol, zlocinId
                    if (resultSet2.next()) {
                        val idOsoba = resultSet2.getInt("idOsoba")
                        val ime = resultSet2.getString("ime")
                        val kontakt = resultSet2.getString("kontakt")
                        val datum = resultSet2.getTimestamp("datum")
                        val zanimanje = resultSet2.getString("zanimanje")
                        val pol = resultSet2.getString("pol")
                        val zlocinId = resultSet2.getInt("zlocinId")

                        os= OsobaData(
                            idOsoba = idOsoba,
                            ime = ime,
                            kontakt = kontakt,
                            datum = datum.time,
                            zanimanje = zanimanje,
                            pol = pol,
                            zlocinId = zlocinId
                        )
                    }
                }

                val zr= os?.let {
                    ZrtvaData(
                        idZrtva = idZrtva,
                        tipZrtve = tipZrtve,
                        detalji = detalji,
                        statusZrtva = statusZrtva,
                        zlocinId = zlocinId,
                        osobaId = it
                    )
                }
                return zr
            } else {
                null
            }
        }
        return null
    }

    override fun getOsumnjiceni(id: Int): List<OsumnjicenData>? {
        val mainQuery = "SELECT * FROM osumnjicen WHERE zlocinId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaOsumnjicenih = mutableListOf<OsumnjicenData>()

        while (resultSet != null && resultSet.next()) {
            val idOsumnjicen = resultSet.getInt("idOsumnjicen")
            val statusS = resultSet.getInt("statusS")
            val tipOsumnjicen = resultSet.getString("tipOsumnjicen")
            val motivId = resultSet.getInt("motiv")
            val zlocinId = resultSet.getInt("zlocinId")
            val kriv = resultSet.getInt("kriv")
            val osobaId = resultSet.getInt("osobaId")

            // Novi statement za osobu
            val osobaQuery = "SELECT * FROM Osoba WHERE idOsoba = ?"
            val osobaStatement = connection.prepareStatement(osobaQuery)
            osobaStatement.setInt(1, osobaId)
            val osobaResult = osobaStatement.executeQuery()

            var os: OsobaData? = null
            if (osobaResult.next()) {
                os = OsobaData(
                    idOsoba = osobaResult.getInt("idOsoba"),
                    ime = osobaResult.getString("ime"),
                    kontakt = osobaResult.getString("kontakt"),
                    datum = osobaResult.getTimestamp("datum").time,
                    zanimanje = osobaResult.getString("zanimanje"),
                    pol = osobaResult.getString("pol"),
                    zlocinId = osobaResult.getInt("zlocinId")
                )
            }
            osobaResult.close()
            osobaStatement.close()

            // Novi statement za motiv
            val motivQuery = "SELECT * FROM Motiv WHERE idMotiv = ?"
            val motivStatement = connection.prepareStatement(motivQuery)
            motivStatement.setInt(1, motivId)
            val motivResult = motivStatement.executeQuery()

            var motiv: MotivData? = null
            if (motivResult.next()) {
                motiv = MotivData(
                    idMotiv = motivResult.getInt("idMotiv"),
                    opis = motivResult.getString("opis")
                )
            }
            motivResult.close()
            motivStatement.close()

            if (motiv != null && os!=null) {
                val osumnjicen = OsumnjicenData(
                    idOsumnjicen = idOsumnjicen,
                    status = statusS,
                    tipOsumnjicen = tipOsumnjicen,
                    motiv = motiv,
                    zlocinId = zlocinId,
                    kriv = kriv,
                    osobaId = os
                )
                listaOsumnjicenih.add(osumnjicen)
            }
        }

        // Zatvori sve na kraju
        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaOsumnjicenih
    }

    override fun getDokazi(id: Int,zr: ZrtvaData): List<DokazData>?{
        val mainQuery = "SELECT * FROM dokaz WHERE zlocinId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaDokaza = mutableListOf<DokazData>()

        while (resultSet != null && resultSet.next()) {
            //idDokaz, tipDokaza, opis, zlocinId, zrtvaId, statusS
            val idDokaz = resultSet.getInt("idDokaz")
            val tipDokaza = resultSet.getString("tipDokaza")
            val opis = resultSet.getString("opis")
            val zlocinId = resultSet.getInt("zlocinId")
            val zrtvaId = resultSet.getInt("zrtvaId")
            val statusS = resultSet.getInt("statusS")


            val dokaz= DokazData(
                idDokaz = idDokaz,
                tipDokaza = tipDokaza,
                opis = opis,
                zlocinId = zlocinId,
                zrtvaId = zrtvaId,
                status = statusS
            )
            listaDokaza.add(dokaz)
        }

        resultSet?.close()
        mainStatement?.close()
       // connection?.close()

        return listaDokaza
    }

    override fun getTelefon(id: Int): List<TelefonData>?{
        val mainQuery = "SELECT * FROM telefon WHERE zrtvaId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaTelefon = mutableListOf<TelefonData>()

        while (resultSet != null && resultSet.next()) {
            //idTelefon, model, os, zrtvaId, sifra, informacije

            val idTelefon = resultSet.getInt("idTelefon")
            val model = resultSet.getString("model")
            val os = resultSet.getString("os")
            val zrtvaId = resultSet.getInt("zrtvaId")
            val sifra = resultSet.getString("sifra")
            val informacije = resultSet.getString("informacije")


            val tel = TelefonData(
                idTelefon = idTelefon,
                model = model,
                os = os,
                sifra = sifra,
                informacije = informacije,
                zrtvaId = zrtvaId
            )
            listaTelefon.add(tel)
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaTelefon
    }

    override fun getForenzickiDokazi(id: Int): List<ForenzickiDokazData>?{
        val mainQuery = "SELECT * FROM forenzickiDokaz WHERE zrtvaId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaDokaza = mutableListOf<ForenzickiDokazData>()

        while (resultSet != null && resultSet.next()) {
            //idForenzickiDokaz, tipForenzickiDokaz, opis, statusS, zrtvaId, veza

            val idForenzickiDokaz = resultSet.getInt("idForenzickiDokaz")
            val tipForenzickiDokaz = resultSet.getString("tipForenzickiDokaz")
            val opis = resultSet.getString("opis")
            val statusS = resultSet.getInt("statusS")
            val zrtvaId = resultSet.getInt("zrtvaId")
            val veza = resultSet.getString("veza")


            val dokaz= ForenzickiDokazData(
                idForenzickiDokaz = idForenzickiDokaz,
                tipForenzickiDokaz = tipForenzickiDokaz,
                opis = opis,
                statusS = statusS,
                veza = veza,
                zrtvaId = id
            )
            listaDokaza.add(dokaz)
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaDokaza
    }

    override fun getObdukcija(id: Int): ObdukcijaData?{
        val mainQuery = "SELECT * FROM obdukcija WHERE zrtvaId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        if (resultSet != null && resultSet.next()) {
            //idObdukcija, izvestaj, datum, uzrokSmrti, zrtvaId, informacije

            val idObdukcija = resultSet.getInt("idObdukcija")
            val izvestaj = resultSet.getString("izvestaj")
            val datum = resultSet.getTimestamp("datum")
            val uzrokSmrti = resultSet.getString("uzrokSmrti")
            val zrtvaId = resultSet.getInt("zrtvaId")
            val informacije = resultSet.getString("informacije")


            val obd = ObdukcijaData(
                idObdukcija = idObdukcija,
                izvestaj = izvestaj,
                datum = datum.time,
                uzrokSmrti = uzrokSmrti,
                zrtvaId = zrtvaId,
                informacije = informacije
            )
            return obd
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return null
    }

    override fun getSvedoci(id: Int): List<SvedokData>?{
        val mainQuery = "SELECT * FROM svedok WHERE zlocinId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaSvedoka = mutableListOf<SvedokData>()

        while (resultSet != null && resultSet.next()) {
            //idSvedok, izjava, statusSvedok, statusIspitan, zlocinId, osobaId

            val idSvedok = resultSet.getInt("idSvedok")
            val izjava = resultSet.getString("izjava")
            val statusSvedok = resultSet.getString("statusSvedok")
            val statusIspitan = resultSet.getInt("statusIspitan")
            val zlocinId = resultSet.getInt("zlocinId")
            val osobaId = resultSet.getInt("osobaId")

            // Novi statement za osobu
            val osobaQuery = "SELECT * FROM Osoba WHERE idOsoba = ?"
            val osobaStatement = connection.prepareStatement(osobaQuery)
            osobaStatement.setInt(1, osobaId)
            val osobaResult = osobaStatement.executeQuery()

            var os: OsobaData? = null
            if (osobaResult.next()) {
                os = OsobaData(
                    idOsoba = osobaResult.getInt("idOsoba"),
                    ime = osobaResult.getString("ime"),
                    kontakt = osobaResult.getString("kontakt"),
                    datum = osobaResult.getTimestamp("datum").time,
                    zanimanje = osobaResult.getString("zanimanje"),
                    pol = osobaResult.getString("pol"),
                    zlocinId = osobaResult.getInt("zlocinId")
                )
            }
            osobaResult.close()
            osobaStatement.close()

            val svedok= os?.let {
                SvedokData(
                    idSvedok = idSvedok,
                    izjava = izjava,
                    statusSvedok = statusSvedok,
                    statusIspitan = statusIspitan,
                    zlocinId = zlocinId,
                    osobaId = it
                )
            }
            if (svedok != null) {
                listaSvedoka.add(svedok)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaSvedoka
    }

    override fun getOneContact(id: Int): List<OneContactData>?{
        val mainQuery = "SELECT * FROM oneContact WHERE zlocinId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaOneContact = mutableListOf<OneContactData>()

        while (resultSet != null && resultSet.next()) {
            // idOneContact, zlocinId, ime, broj, slika

            val idOneContact = resultSet.getInt("idOneContact")
            val ime = resultSet.getString("ime")
            val broj = resultSet.getString("broj")
            val slika = resultSet.getInt("slika")
            val zlocinId = resultSet.getInt("zlocinId")

            val oc = OneContactData(
                idOneContact = idOneContact,
                zlocinId = zlocinId,
                ime = ime,
                broj = broj,
                slika = slika
            )

            listaOneContact.add(oc)
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaOneContact
    }

    override fun getKontakti(id: Int,zr: ZrtvaData): List<KontaktData>?{
        connection.autoCommit = true
        val mainQuery = "SELECT * FROM kontakt WHERE zrtvaId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaKontakt = mutableListOf<KontaktData>()

        while (resultSet != null && resultSet.next()) {
            //idKontakt, ime, broj, statusS, zrtvaId

            val idKontakt = resultSet.getInt("idKontakt")
            val ime = resultSet.getString("ime")
            val broj = resultSet.getString("broj")
            val statusS = resultSet.getInt("statusS")
            val zrtvaId = resultSet.getInt("zrtvaId")

            val k = KontaktData(
                idKontakt = idKontakt,
                ime = ime,
                broj = broj,
                status = statusS,
                zrtvaId = zr
            )
            listaKontakt.add(k)
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaKontakt
    }

    override fun getPoruke(id: Int, zr: ZrtvaData, kontakti: List<KontaktData>?): List<PorukeData>?{
        val mainQuery = "SELECT * FROM poruke WHERE zrtvaId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaKontakt = mutableListOf<PorukeData>()

        while (resultSet != null && resultSet.next()) {
            //idPoruke, tipPoruke, sadrzaj, datumVreme, zrtvaId, posiljalacId, statusPoruke, sifrovana

            val idPoruke = resultSet.getInt("idPoruke")
            val tipPoruke = resultSet.getString("tipPoruke")
            val sadrzaj = resultSet.getString("sadrzaj")
            val datumVreme = resultSet.getTimestamp("datumVreme")
            val zrtvaId = resultSet.getInt("zrtvaId")
            val posiljalacId = resultSet.getInt("posiljalacId")
            val statusPoruke = resultSet.getString("statusPoruke")
            val sifrovana = resultSet.getBoolean("sifrovana")

            val k = kontakti?.find {  it.idKontakt == posiljalacId }

            val p = k?.let {
                PorukeData(
                    idPoruke = idPoruke,
                    tipPoruke = tipPoruke,
                    sadrzaj = sadrzaj,
                    datumVreme = datumVreme.time,
                    zrtvaId = zr,
                    posiljalacId = it,
                    statusPoruke = statusPoruke,
                    sifrovana = sifrovana
                )
            }
            if (p != null) {
                listaKontakt.add(p)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaKontakt
    }

    override fun getPozivi(id: Int, zr: ZrtvaData, kontakti: List<KontaktData>?): List<PoziviData>?{
        val mainQuery = "SELECT * FROM pozivi WHERE zrtvaId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaKontakt = mutableListOf<PoziviData>()

        while (resultSet != null && resultSet.next()) {
            //idPoziv, tip, broj, datumVreme, zrtvaId, statusS, kontaktId

            val idPoziv = resultSet.getInt("idPoziv")
            val tip = resultSet.getInt("tip")
            val broj = resultSet.getString("broj")
            val datumVreme = resultSet.getTimestamp("datumVreme")
            val zrtvaId = resultSet.getInt("zrtvaId")
            val kontaktId = resultSet.getInt("kontaktId")
            val statusS = resultSet.getInt("statusS")

            val ko = kontakti?.find {  it.idKontakt == kontaktId }

            val p = ko?.let {
                PoziviData(
                    idPoziv = idPoziv,
                    tip = tip,
                    broj = broj,
                    datumVreme = datumVreme.time,
                    zrtvaId = zr,
                    status = statusS,
                    kontaktId = it
                )
            }
            if (p != null) {
                listaKontakt.add(p)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaKontakt
    }


    override fun getGalerija(id: Int,zr: ZrtvaData): List<GalerijaData>?{
        val mainQuery = "SELECT * FROM galerija WHERE zrtvaId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaGalerija = mutableListOf<GalerijaData>()

        while (resultSet != null && resultSet.next()) {
            //idGalerija, tip, putanja, zrtvaId, datumVreme, lokacija

            val idGalerija = resultSet.getInt("idGalerija")
            val tip = resultSet.getInt("tip")
            val putanja = resultSet.getString("putanja")
            val datumVreme = resultSet.getTimestamp("datumVreme")
            val lokacija = resultSet.getString("lokacija")
            val zrtvaId = resultSet.getInt("zrtvaId")

            val g = GalerijaData(
                idGalerija = idGalerija,
                tip = tip,
                putanja = putanja,
                zrtvaId = zr,
                datumVreme = datumVreme.time,
                lokacija = lokacija
            )
            listaGalerija.add(g)
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaGalerija
    }

    override fun getAplikacije(id: Int,zr: ZrtvaData): List<AplikacijaData>?{
        val mainQuery = "SELECT * FROM aplikacija WHERE zrtvaId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaAplikacija = mutableListOf<AplikacijaData>()

        while (resultSet != null && resultSet.next()) {
            //idAplikacije, naziv, tip, zrtvaId, aktivna, informacije

            val idAplikacije = resultSet.getInt("idAplikacije")
            val tip = resultSet.getInt("tip")
            val naziv = resultSet.getString("naziv")
            val aktivna = resultSet.getBoolean("aktivna")
            val informacije = resultSet.getString("informacije")
            val zrtvaId = resultSet.getInt("zrtvaId")

            val a = AplikacijaData(
                idAplikacije = idAplikacije,
                naziv = naziv,
                tip = tip,
                zrtvaId = zr,
                aktivna = aktivna,
                informacije = informacije
            )
            listaAplikacija.add(a)
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaAplikacija
    }

    override fun getTragovi(forenzickiDokazi: List<ForenzickiDokazData>?, osumnjiceni: List<OsumnjicenData>?): List<TragData>?{
        val mainQuery = "SELECT * FROM trag"
        val mainStatement = connection.prepareStatement(mainQuery)
        //mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaTrag = mutableListOf<TragData>()

        while (resultSet != null && resultSet.next()) {
            //idTrag, forenzickiDokazId, osumnjicenId

            val idTrag = resultSet.getInt("idTrag")
            val forenzickiDokazId = resultSet.getInt("forenzickiDokazId")
            val osumnjicenId = resultSet.getInt("osumnjicenId")

            val f = forenzickiDokazi?.find {  it.idForenzickiDokaz == forenzickiDokazId }
            val o = osumnjiceni?.find {  it.idOsumnjicen == osumnjicenId }

            if (o != null && f!=null) {
                val t= TragData(
                    idTrag = idTrag,
                    forenzickiDokazId = f,
                    osumnjicenId = o
                )
                listaTrag.add(t)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaTrag
    }

    override fun getDokaziOsumnjiceni(dokazi: List<DokazData>?, osumnjiceni: List<OsumnjicenData>?): List<DokazOsumnjicenData>?{
        val mainQuery = "SELECT * FROM dokazOsumnjicen"
        val mainStatement = connection.prepareStatement(mainQuery)
        // mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaDokazOsumnjicen = mutableListOf<DokazOsumnjicenData>()

        while (resultSet != null && resultSet.next()) {
            //idDokazOsumnjicen, dokazId, osumnjicenId

            val idDokazOsumnjicen = resultSet.getInt("idDokazOsumnjicen")
            val dokazId = resultSet.getInt("dokazId")
            val osumnjicenId = resultSet.getInt("osumnjicenId")

            val d = dokazi?.find {  it.idDokaz == dokazId }
            val o = osumnjiceni?.find {  it.idOsumnjicen == osumnjicenId }

            if (o != null && d!=null) {
                val t= DokazOsumnjicenData(
                    idDokazOsumnjicen = idDokazOsumnjicen,
                    dokazId = d,
                    osumnjicenId = o
                )
                listaDokazOsumnjicen.add(t)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaDokazOsumnjicen
    }

    override fun getBeleske(id: Int,zr: ZrtvaData): List<BeleskaData>?{
        val mainQuery = "SELECT * FROM beleska WHERE zlocinId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaBeleske = mutableListOf<BeleskaData>()

        while (resultSet != null && resultSet.next()) {
            //idBeleska, zlocinId, tekst, datum

            val idBeleska = resultSet.getInt("idBeleska")
            val zlocinId = resultSet.getInt("zlocinId")
            val tekst = resultSet.getString("tekst")
            val datum = resultSet.getTimestamp("datum")

            val b= BeleskaData(
                idBeleska = idBeleska,
                zlocinId = zlocinId,
                tekst = tekst,
                datum = datum.time
            )
            listaBeleske.add(b)
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaBeleske
    }

    override fun getWhatsAppKontakt(id: Int,zr: ZrtvaData): List<WhatsAppKontaktData>?{
        val mainQuery = "SELECT * FROM whatsappkontakt WHERE zlocinId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaWhatsAppKontakt = mutableListOf<WhatsAppKontaktData>()

        while (resultSet != null && resultSet.next()) {
            //idWhatsAppKontakt, zlocinId, ime, broj, slika

            val idWhatsAppKontakt = resultSet.getInt("idWhatsAppKontakt")
            val zlocinId = resultSet.getInt("zlocinId")
            val ime = resultSet.getString("ime")
            val broj = resultSet.getString("broj")
            val slika = resultSet.getInt("slika")

            val wk = WhatsAppKontaktData(
                idWhatsAppKontakt = idWhatsAppKontakt,
                zlocinId = zlocinId,
                ime = ime,
                broj = broj,
                slika = slika
            )
            listaWhatsAppKontakt.add(wk)
        }

        resultSet?.close()
        mainStatement?.close()
        //connection?.close()

        return listaWhatsAppKontakt
    }

    override fun getWhatsAppPoruka(id: Int, whatsAppKontakti : List<WhatsAppKontaktData>?): List<WhatsAppPorukaData>?{
        if (whatsAppKontakti == null) return null
        val relevantniKontakti = whatsAppKontakti.filter { it.zlocinId == id }.map { it.idWhatsAppKontakt }.toSet()

        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM whatsappporuka"
        val mainStatement = connection?.prepareStatement(mainQuery)
        //mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaWhatsAppPoruka = mutableListOf<WhatsAppPorukaData>()

        while (resultSet != null && resultSet.next()) {
            //idWhatsAppPoruka, kontaktKoSalje, kontaktKomeSalje, tekst, datum, procitana
            val kontaktKoSalje = resultSet.getInt("kontaktKoSalje")
            val kontaktKomeSalje = resultSet.getInt("kontaktKomeSalje")

            if (kontaktKoSalje in relevantniKontakti || kontaktKomeSalje in relevantniKontakti) {
                val idWhatsAppPoruka = resultSet.getInt("idWhatsAppPoruka")
                val tekst = resultSet.getString("tekst")
                val datum = resultSet.getTimestamp("datum")
                val procitana = resultSet.getBoolean("procitana")

                val wp = WhatsAppPorukaData(
                    idWhatsAppPoruka = idWhatsAppPoruka,
                    kontaktKoSalje = kontaktKoSalje,
                    kontaktKomeSalje = kontaktKomeSalje,
                    tekst = tekst,
                    datum = datum.time,
                    procitana = procitana
                )
                listaWhatsAppPoruka.add(wp)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaWhatsAppPoruka
    }

    override fun getGallery(id: Int): List<GalleryData>?{
        val mainQuery = "SELECT * FROM gallery WHERE zlocinId=?"
        val mainStatement = connection.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaGallery = mutableListOf<GalleryData>()

        while (resultSet != null && resultSet.next()) {
            //idPhoto, zlocinId, slika, datum, mesto
            val idPhoto = resultSet.getInt("idPhoto")
            val zlocinId = resultSet.getInt("zlocinId")
            val slika = resultSet.getInt("slika")
            val mesto = resultSet.getString("mesto")
            val datum = resultSet.getTimestamp("datum")

            val g = GalleryData(
                idPhoto = idPhoto,
                zlocinId = zlocinId,
                slika = slika,
                datum = datum.time,
                mesto = mesto
            )
            listaGallery.add(g)
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaGallery
    }

    override fun getOdnosOsumnjicenZrtva(id: Int): List<OdnosOsumnjicenZrtvaData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM odnososumnjicenzrtva WHERE zrtvaId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaOdnosOsumnjicenZrtva = mutableListOf<OdnosOsumnjicenZrtvaData>()

        while (resultSet != null && resultSet.next()) {
            //idOdnos, osumnjicenId, zrtvaId, tipOdnosa
            val idOdnos = resultSet.getInt("idOdnos")
            val osumnjicenId = resultSet.getInt("osumnjicenId")
            val zrtvaId = resultSet.getInt("zrtvaId")
            val tipOdnosa = resultSet.getString("tipOdnosa")

            val o = OdnosOsumnjicenZrtvaData(
                idOdnos = idOdnos,
                osumnjicenId = osumnjicenId,
                zrtvaId = zrtvaId,
                tipOdnosa = tipOdnosa
            )
            listaOdnosOsumnjicenZrtva.add(o)
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaOdnosOsumnjicenZrtva
    }

    override fun getPitanja(id: Int): List<PitanjeData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM pitanje WHERE zlocinId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaPitanja = mutableListOf<PitanjeData>()

        while (resultSet != null && resultSet.next()) {
            //idPitanje, zlocinId, tekst
            val idPitanje = resultSet.getInt("idPitanje")
            val zlocinId = resultSet.getInt("zlocinId")
            val tekst = resultSet.getString("tekst")

            val p = PitanjeData(
                idPitanje = idPitanje,
                zlocinId = zlocinId,
                tekst = tekst
            )
            listaPitanja.add(p)
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaPitanja
    }

    override fun getOdgovor(id: Int): List<OdgovorData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = """
            SELECT o.idOdogovor, o.pitanjeId, o.tekstOdgovora, o.tacan, o.bodovi
            FROM odgovor o
            JOIN pitanje p ON o.pitanjeId = p.idPitanje
            WHERE p.zlocinId = ?
        """.trimIndent()
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaOdgovora = mutableListOf<OdgovorData>()

        while (resultSet != null && resultSet.next()) {
            //idOdogovor, pitanjeId, tekstOdgovora, tacan, bodovi
            val idOdogovor = resultSet.getInt("idOdogovor")
            val pitanjeId = resultSet.getInt("pitanjeId")
            val tekstOdgovora = resultSet.getString("tekstOdgovora")
            val tacan = resultSet.getBoolean("tacan")
            val bodovi = resultSet.getInt("bodovi")

            val p = OdgovorData(
                idOdogovor = idOdogovor,
                pitanjeId = pitanjeId,
                tekstOdgovora = tekstOdgovora,
                tacan = tacan,
                bodovi = bodovi
            )
            listaOdgovora.add(p)
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaOdgovora
    }

    override fun getPitanjeIspitivanjeOsumnjicenog(id: Int): List<PitanjeIspitivanjeOsumnjicenogData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM pitanjeispitivanjeosumnjicenog WHERE osumnjicenId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaPitanjeIspitivanjeOsumnjicenog = mutableListOf<PitanjeIspitivanjeOsumnjicenogData>()

        while (resultSet != null && resultSet.next()) {
            //idPitanjeIspitivanjeOsumnjicenog, kategorija, tekst, odgovor, komentar, osumnjicenId
            val idPitanjeIspitivanjeOsumnjicenog = resultSet.getInt("idPitanjeIspitivanjeOsumnjicenog")
            val kategorija = resultSet.getString("kategorija")
            val tekst = resultSet.getString("tekst")
            val odgovor = resultSet.getString("odgovor")
            val komentar = resultSet.getString("komentar")
            val osumnjicenId = resultSet.getInt("osumnjicenId")

            val p = PitanjeIspitivanjeOsumnjicenogData(
                idPitanjeIspitivanjeOsumnjicenog = idPitanjeIspitivanjeOsumnjicenog,
                kategorija = kategorija,
                tekst = tekst,
                odgovor = odgovor,
                komentar = komentar,
                osumnjicenId = osumnjicenId
            )
            listaPitanjeIspitivanjeOsumnjicenog.add(p)
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaPitanjeIspitivanjeOsumnjicenog
    }

    override fun getPitanjeIspitivanjeSvedoka(id: Int): List<PitanjeIspitivanjeSvedokaData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM pitanjeispitivanjesvedoka WHERE svedokId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaPitanjeIspitivanjeSvedokaData = mutableListOf<PitanjeIspitivanjeSvedokaData>()

        while (resultSet != null && resultSet.next()) {
            //idPitanjeIspitivanjeSvedoka, tekst, odgovor, svedokId, nextPitanje

            val idPitanjeIspitivanjeSvedoka = resultSet.getInt("idPitanjeIspitivanjeSvedoka")
            val tekst = resultSet.getString("tekst")
            val odgovor = resultSet.getString("odgovor")
            val svedokId = resultSet.getInt("svedokId")
            val nextPitanje = resultSet.getInt("nextPitanje")

            val p = PitanjeIspitivanjeSvedokaData(
                idPitanjeIspitivanjeSvedoka = idPitanjeIspitivanjeSvedoka,
                tekst = tekst,
                odgovor = odgovor,
                svedokId = svedokId,
                nextPitanje = nextPitanje
            )
            listaPitanjeIspitivanjeSvedokaData.add(p)
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaPitanjeIspitivanjeSvedokaData
    }

    override fun getOsobe(id: Int): List<OsobaData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM Osoba WHERE zlocinId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaOsobe = mutableListOf<OsobaData>()

        while (resultSet != null && resultSet.next()) {
            //idOsoba, ime, kontakt, datum, zanimanje, pol, zlocinId

            val idOsoba = resultSet.getInt("idOsoba")
            val ime = resultSet.getString("ime")
            val kontakt = resultSet.getString("kontakt")
            val datum = resultSet.getTimestamp("datum")
            val zanimanje = resultSet.getString("zanimanje")
            val pol = resultSet.getString("pol")
            val zlocinId = resultSet.getInt("zlocinId")

            val p = OsobaData(
                idOsoba = idOsoba,
                ime = ime,
                kontakt = kontakt,
                datum = datum.time,
                zanimanje = zanimanje,
                pol = pol,
                zlocinId = zlocinId
            )
            listaOsobe.add(p)
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaOsobe
    }

    override fun getZadaci(id: Int): List<ZadatakData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM zadatak WHERE zlocinId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaZadatak = mutableListOf<ZadatakData>()

        while (resultSet != null && resultSet.next()) {
            //idZadatak, tekst, korak, uradjen, nextZadatak, zlocinId

            val idZadatak = resultSet.getInt("idZadatak")
            val tekst = resultSet.getString("tekst")
            val korak = resultSet.getString("korak")
            val uradjen = resultSet.getBoolean("uradjen")
            val nextZadatak = resultSet.getObject("nextZadatak")?.let { resultSet.getInt("nextZadatak") }
            val zlocinId = resultSet.getInt("zlocinId")

            val p = ZadatakData(
                idZadatak = idZadatak,
                tekst = tekst,
                korak = korak,
                uradjen = uradjen,
                nextZadatak = nextZadatak,
                zlocinId = zlocinId
            )
            listaZadatak.add(p)
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaZadatak
    }

    override fun getDokaziZadaci(id: Int, zadaci: List<ZadatakData>?): List<DokazZadatakData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = """
            SELECT dz.idDokazZadatak, dz.tekst, dz.dokazId, dz.uradjen, dz.zadatakId
            FROM dokazzadatak dz
            JOIN dokaz d ON dz.dokazId = d.idDokaz
            WHERE d.zlocinId = ?
        """.trimIndent()
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaZadatak = mutableListOf<DokazZadatakData>()

        while (resultSet != null && resultSet.next()) {
            //idDokazZadatak, tekst, dokazId, uradjen, zadatakId

            val idDokazZadatak = resultSet.getInt("idDokazZadatak")
            val tekst = resultSet.getString("tekst")
            val dokazId = resultSet.getInt("dokazId")
            val uradjen = resultSet.getBoolean("uradjen")
            val zadatakId = resultSet.getInt("zadatakId")

            val z = zadaci?.find { it.idZadatak == zadatakId }

            if(z!=null){
                val p = DokazZadatakData(
                    idDokazZadatak = idDokazZadatak,
                    tekst = tekst,
                    dokazId = dokazId,
                    uradjen = uradjen,
                    zadatakId = zadatakId
                )
                listaZadatak.add(p)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaZadatak
    }

    override fun getIspitivanjeOsumnjicenogZadatak(id: Int, zadaci: List<ZadatakData>?): List<IspitivanjeOsumnjicenogZadatakData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = """
            SELECT io.* FROM ispitivanjeosumnjicenogzadatak io
            JOIN osumnjicen o ON io.osumnjicenId = o.idOsumnjicen
            WHERE o.zlocinId = ?
        """.trimIndent()
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaIspitivanjeOsumnjicenogZadatak = mutableListOf<IspitivanjeOsumnjicenogZadatakData>()

        while (resultSet != null && resultSet.next()) {
            //idIspitivanjeOsumnjicenogZadatak, osumnjicenId, zadatakId, uradjen

            val idIspitivanjeOsumnjicenogZadatak = resultSet.getInt("idIspitivanjeOsumnjicenogZadatak")
            val osumnjicenId = resultSet.getInt("osumnjicenId")
            val uradjen = resultSet.getBoolean("uradjen")
            val zadatakId = resultSet.getInt("zadatakId")

            val z = zadaci?.find { it.idZadatak == zadatakId }

            if(z!=null){
                val p = IspitivanjeOsumnjicenogZadatakData(
                    idIspitivanjeOsumnjicenogZadatak = idIspitivanjeOsumnjicenogZadatak,
                    osumnjicenId = osumnjicenId,
                    zadatakId = zadatakId,
                    uradjen = uradjen
                )
                listaIspitivanjeOsumnjicenogZadatak.add(p)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaIspitivanjeOsumnjicenogZadatak
    }

    override fun getIspitivanjeSvedokaZadatak(id: Int,zadaci: List<ZadatakData>?): List<IspitivanjeSvedokaZadatakData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = """
            SELECT isz.idIspitivanjeSvedokaZadatak, isz.svedokId, isz.zadatakId, isz.uradjen
            FROM ispitivanjesvedokazadatak isz
            JOIN svedok s ON isz.svedokId = s.idSvedok
            WHERE s.zlocinId = ?
        """.trimIndent()
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaIspitivanjeSvedokaZadatakData = mutableListOf<IspitivanjeSvedokaZadatakData>()

        while (resultSet != null && resultSet.next()) {
            //idIspitivanjeSvedokaZadatak, svedokId, zadatakId, uradjen

            val idIspitivanjeSvedokaZadatak = resultSet.getInt("idIspitivanjeSvedokaZadatak")
            val svedokId = resultSet.getInt("svedokId")
            val uradjen = resultSet.getBoolean("uradjen")
            val zadatakId = resultSet.getInt("zadatakId")

            val z = zadaci?.find { it.idZadatak == zadatakId }

            if(z!=null) {
                val p = IspitivanjeSvedokaZadatakData(
                    idIspitivanjeSvedokaZadatak = idIspitivanjeSvedokaZadatak,
                    svedokId = svedokId,
                    zadatakId = zadatakId,
                    uradjen = uradjen
                )
                listaIspitivanjeSvedokaZadatakData.add(p)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaIspitivanjeSvedokaZadatakData
    }

    override fun getTelefonZadaci(id: Int, zadaci: List<ZadatakData>?): List<TelefonZadatakData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = """
            SELECT tz.*
            FROM telefonzadatak tz
            JOIN telefon t ON tz.telefonId = t.idTelefon
            WHERE t.zrtvaId = ?
        """.trimIndent()
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaTelefonZadatak = mutableListOf<TelefonZadatakData>()

        while (resultSet != null && resultSet.next()) {
            //idTelefonZadatak, telefonId, zadatakId, uradjen

            val idTelefonZadatak = resultSet.getInt("idTelefonZadatak")
            val telefonId = resultSet.getInt("telefonId")
            val uradjen = resultSet.getBoolean("uradjen")
            val zadatakId = resultSet.getInt("zadatakId")

            val z = zadaci?.find { it.idZadatak == zadatakId }

            if(z!=null) {
                val p = TelefonZadatakData(
                    idTelefonZadatak = idTelefonZadatak,
                    telefonId = telefonId,
                    zadatakId = zadatakId,
                    uradjen = uradjen
                )
                listaTelefonZadatak.add(p)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaTelefonZadatak
    }


    override fun getForenzickiDokazZadatak(id: Int,zadaci: List<ZadatakData>?): List<ForenzickiDokazZadatakData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = """
            SELECT fdz.idForenzickiDokazZadatak, fdz.tekst, fdz.forenzickiDokazId, fdz.uradjen, fdz.zadatakId
            FROM forenzickidokazzadatak fdz
            JOIN forenzickiDokaz fd ON fdz.forenzickiDokazId = fd.idForenzickiDokaz
            WHERE fd.zrtvaId = ?
        """.trimIndent()
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaForenzickiDokazZadatak = mutableListOf<ForenzickiDokazZadatakData>()

        while (resultSet != null && resultSet.next()) {
            //idForenzickiDokazZadatak, tekst, forenzickiDokazId, uradjen, zadatakId

            val idForenzickiDokazZadatak = resultSet.getInt("idForenzickiDokazZadatak")
            val tekst = resultSet.getString("tekst")
            val forenzickiDokazId = resultSet.getInt("forenzickiDokazId")
            val uradjen = resultSet.getBoolean("uradjen")
            val zadatakId = resultSet.getInt("zadatakId")

            val z = zadaci?.find { it.idZadatak == zadatakId }

            if(z!=null) {
                val p = ForenzickiDokazZadatakData(
                    idForenzickiDokazZadatak = idForenzickiDokazZadatak,
                    tekst = tekst,
                    forenzickiDokazId = forenzickiDokazId,
                    uradjen = uradjen,
                    zadatakId = zadatakId
                )
                listaForenzickiDokazZadatak.add(p)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaForenzickiDokazZadatak
    }


    override fun getOneCall(id: Int,oneContact: List<OneContactData>?): List<OneCallData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = """
            SELECT *
            FROM onecall ocall
            JOIN oneContact ocontact ON ocall.kontakt = ocontact.idOneContact
            WHERE ocontact.zlocinId = ?
        """.trimIndent()
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaOneCall = mutableListOf<OneCallData>()

        while (resultSet != null && resultSet.next()) {
            //idOneCall, kontakt, datum, propusten, dolazni

            val idOneCall = resultSet.getInt("idOneCall")
            val kontakt = resultSet.getInt("kontakt")
            val datum = resultSet.getTimestamp("datum")
            val propusten = resultSet.getBoolean("propusten")
            val dolazni = resultSet.getBoolean("dolazni")
            val zrtvaId = resultSet.getInt("zrtvaId")

            val o = oneContact?.find { it.idOneContact == kontakt }

            val p = o?.let {
                OneCallData(
                    idOneCall = idOneCall,
                    kontakt = it.idOneContact,
                    datum = datum.time,
                    propusten = propusten,
                    dolazni = dolazni,
                    zrtvaId = zrtvaId
                )
            }
            if (p != null) {
                listaOneCall.add(p)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaOneCall
    }

    override fun getObicnaPoruka(id: Int,oneContact: List<OneContactData>?): List<ObicnaPorukaData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM obicnaporuka"
        val mainStatement = connection?.prepareStatement(mainQuery)
        //mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        val listaObicnaPoruka = mutableListOf<ObicnaPorukaData>()

        val kontaktiZaZlocin = oneContact?.filter { it.zlocinId == id } ?: emptyList()
        val kontaktIdSet = kontaktiZaZlocin.map { it.idOneContact }.toSet()

        while (resultSet != null && resultSet.next()) {
            val idObicnaPoruka = resultSet.getInt("idObicnaPoruka")
            val kontaktKoSalje = resultSet.getInt("kontaktKoSalje")
            val kontaktKomeSalje = resultSet.getInt("kontaktKomeSalje")
            val datum = resultSet.getTimestamp("datum")
            val tekst = resultSet.getString("tekst")
            val procitana = resultSet.getBoolean("procitana")

            if (kontaktKoSalje in kontaktIdSet && kontaktKomeSalje in kontaktIdSet) {
                val poruka = ObicnaPorukaData(
                    idObicnaPoruka = idObicnaPoruka,
                    kontaktKoSalje = kontaktKoSalje,
                    kontaktKomeSalje = kontaktKomeSalje,
                    tekst = tekst,
                    datum = datum.time,
                    procitana = procitana
                )
                listaObicnaPoruka.add(poruka)
            }
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaObicnaPoruka
    }

    override fun getPacijent(id: Int, zl: ZlocinData, zr: ZrtvaData, osobe: List<OsobaData>): PacijentData?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM pacijent WHERE zlocinId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()

        if (resultSet != null && resultSet.next()) {
            //idPacijent, simptomi, statusPacijenta, datumPrijave, prijavio, zlocinId, zrtvaId

            val idPacijent = resultSet.getInt("idPacijent")
            val simptomi = resultSet.getString("simptomi")
            val statusPacijenta = resultSet.getString("statusPacijenta")
            val datumPrijave = resultSet.getTimestamp("datumPrijave")
            val prijavio = resultSet.getInt("prijavio")
            val zlocinId = resultSet.getInt("zlocinId")
            val zrtvaId = resultSet.getInt("zrtvaId")

            val o = osobe?.find { it.idOsoba == prijavio }

            val p = o?.let {
                PacijentData(
                    idPacijent = idPacijent,
                    simptomi = simptomi,
                    statusPacijenta = statusPacijenta,
                    datumPrijave = datumPrijave.time,
                    prijavio = it,
                    zlocinId = zl,
                    zrtvaId = zr
                )
            }
            return p
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return null
    }


    override fun getMedicinskiIzvetaj(pacijent: PacijentData?): MedicinskiIzvestajData?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM medicinskiizvestaj WHERE pacijentId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        if (pacijent != null) {
            mainStatement?.setInt(1, pacijent.idPacijent)
        }
        val resultSet = mainStatement?.executeQuery()

        if (resultSet != null && resultSet.next()) {
            //idMedicinskiIzvestaj, rezime, CTnalaz, MRInalaz, krvnaSlika, toksikoloskeAnalize, zakljucak, pacijentId

            val idMedicinskiIzvestaj = resultSet.getInt("idMedicinskiIzvestaj")
            val rezime = resultSet.getString("rezime")
            val CTnalaz = resultSet.getString("CTnalaz")
            val MRInalaz = resultSet.getString("MRInalaz")
            val krvnaSlika = resultSet.getString("krvnaSlika")
            val toksikoloskeAnalize = resultSet.getString("toksikoloskeAnalize")
            val zakljucak = resultSet.getString("zakljucak")
            val pacijentId = resultSet.getInt("pacijentId")

            val m = pacijent?.let {
                MedicinskiIzvestajData(
                    idMedicinskiIzvestaj = idMedicinskiIzvestaj,
                    rezime = rezime,
                    CTnalaz = CTnalaz,
                    MRInalaz = MRInalaz,
                    krvnaSlika = krvnaSlika,
                    toksikoloskeAnalize = toksikoloskeAnalize,
                    zakljucak = zakljucak,
                    pacijentId = it
                )
            }
            return m
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return null
    }


    override fun getLekarskiTest(pacijent: PacijentData?): LekarskiTestData?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM lekarskitest WHERE pacijentId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        if (pacijent != null) {
            mainStatement?.setInt(1, pacijent.idPacijent)
        }
        val resultSet = mainStatement?.executeQuery()

        if (resultSet != null && resultSet.next()) {
            //idLekarskiTest, pacijentId, izjava

            val idLekarskiTest = resultSet.getInt("idLekarskiTest")
            val izjava = resultSet.getString("izjava")
            val pacijentId = resultSet.getInt("pacijentId")

            val m = pacijent?.let {
                LekarskiTestData(
                    idLekarskiTest = idLekarskiTest,
                    pacijentId = pacijent,
                    izvestaj = izjava
                )
            }
            return m
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return null
    }


    override fun getLokacijeIstrage(id: Int): List<LokacijeIstrageData>?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM lokacijeistrage WHERE zlocinId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, id)
        val resultSet = mainStatement?.executeQuery()


        val listaLokacijeIstrage = mutableListOf<LokacijeIstrageData>()

        if (resultSet != null && resultSet.next()) {
            //idLokacijeIstrage, mesto, naziv, opis, zlocinId

            val idLokacijeIstrage = resultSet.getInt("idLokacijeIstrage")
            val mesto = resultSet.getString("mesto")
            val naziv = resultSet.getString("naziv")
            val opis = resultSet.getString("opis")
            val zlocinId = resultSet.getInt("zlocinId")
            val geoTackaALatitude = resultSet.getDouble("geoTackaALatitude")
            val geoTackaALongitude = resultSet.getDouble("geoTackaALongitude")

            val l = LokacijeIstrageData(
                idLokacijeIstrage = idLokacijeIstrage,
                mesto = mesto,
                naziv = naziv,
                opis = opis,
                zlocinId = zlocinId,
                geoTackaALatitude = geoTackaALatitude,
                geoTackaALongitude = geoTackaALongitude
            )
            listaLokacijeIstrage.add(l)
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return listaLokacijeIstrage
    }

    override fun getIzjavaZaPacijenta(pacijent: PacijentData, osobe: List<OsobaData>?): IzjavaZaPacijentaData?{
        // val connection = getDatabaseConnection()
        val mainQuery = "SELECT * FROM izjavazapacijenta WHERE pacijentId=?"
        val mainStatement = connection?.prepareStatement(mainQuery)
        mainStatement?.setInt(1, pacijent.idPacijent)
        val resultSet = mainStatement?.executeQuery()

        if (resultSet != null && resultSet.next()) {
            //idIzjavaZaPacijenta, izjava, pacijentId, osobaId

            val idIzjavaZaPacijenta = resultSet.getInt("idIzjavaZaPacijenta")
            val izjava = resultSet.getString("izjava")
            val pacijentId = resultSet.getInt("pacijentId")
            val osobaId = resultSet.getInt("osobaId")

            val o = osobe?.find { it.idOsoba == osobaId }

            val p = o?.let {
                IzjavaZaPacijentaData(
                    idIzjavaZaPacijenta = idIzjavaZaPacijenta,
                    izjava = izjava,
                    pacijentId = pacijent,
                    osobaId = it
                )
            }
            return  p
        }

        resultSet?.close()
        mainStatement?.close()
        // connection?.close()

        return null
    }
}