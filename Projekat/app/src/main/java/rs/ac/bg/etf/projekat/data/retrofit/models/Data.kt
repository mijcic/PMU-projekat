package rs.ac.bg.etf.projekat.data.retrofit.models

import java.time.LocalDate

data class Zlocin(
    val id: Int,
    val naziv: String,
    val opis: String,
    val idTipZlocina: Int,
    val datum: LocalDate
)