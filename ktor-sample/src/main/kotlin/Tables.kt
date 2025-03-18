package com.example

import kotlinx.serialization.Serializable

@Serializable
data class TipZlocinaDC(
    val id: Int,
    val naziv: String,
)

@Serializable
data class Zlocin(
    val id: Int,
    val naziv: String,
    val opis: String,
    val idTipZlocina: Int,
    val datum: Long
)

@Serializable
data class ClanOrganizacije(
    val idClan:Int,
    val idOsumnjicen:Int,
    val idOrganizacija:Int
)

@Serializable
data class Dokaz(
    val idDokaz:Int,
    val opis: String,
    val lokacija: String,
    val idZlocin:Int
)

@Serializable
data class ForenzickiDokaz(
    val idForenzickiDokaz:Int,
    val opis: String,
    val tip: String,
    val idZlocin:Int
)

@Serializable
data class Korisnik(
    val idKorisnik:Int,
    val korisnickoIme: String,
    val ime: String,
    val prezime: String,
    val sifra: String,
    val email: String,
    val nacinPrijave: String,
    val poeni:Int,
    val poslednjaAktivnost:Long
)

@Serializable
data class Misija(
    val idMisija:Int,
    val naziv: String,
    val opis: String,
    val cilj: String
)

@Serializable
data class Motiv(
    val idMotiv:Int,
    val opis: String
)

@Serializable
data class NapredakIstrage(
    val idNapredak:Int,
    val idKorisnik:Int,
    val idZlocin:Int,
    val status:String,
    val datumPromene:Long
)

@Serializable
data class Obdukcija(
    val idObdukcija:Int,
    val uzrokSmrti:String,
    val zakljucak:String,
    val idZlocin: Int
)

@Serializable
data class Organizacije(
    val idOrganizacija: Int,
    val naziv: String,
    val tip: String
)

@Serializable
data class Osumnjicen(
    val idOsumnjicen: Int,
    val ime: String,
    val prezime: String,
    val alibi:String,
    val idZlocin: Int
)

@Serializable
data class Svedok(
    val idSvedok: Int,
    val ime: String,
    val prezime: String,
    val iskaz:String,
    val idZlocin: Int
)

@Serializable
data class TajnaPorodice(
    val idTajna: Int,
    val opis: String,
    val idOsumnjicen: Int
)

@Serializable
data class Trag(
    val idTrag: Int,
    val opis: String,
    val idDokaz: Int,
    val idOsumnjicen: Int
)

@Serializable
data class Ucena(
    val idUcena: Int,
    val opis: String,
    val idOsumnjicen: Int
)

@Serializable
data class ZabelezeniIzbor(
    val idIzbor:Int,
    val idKorisnik: Int,
    val idZlocin: Int,
    val opisIzbora:String,
    val datumIzbora:Long
)