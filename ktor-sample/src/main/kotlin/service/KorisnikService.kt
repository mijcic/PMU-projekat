package com.example.service

import com.example.models.dto.ScoreKorisnik

/**
 * Service responsible for handling logic related to users (korisnici).
 *
 * @property db The database service used to perform queries.
 */
class KorisnikService(private val db: DatabaseService) {

    /**
     * Fetches the top 5 users sorted by their score in descending order.
     *
     * Each user is mapped to a [ScoreKorisnik] DTO, and their rank (position) is assigned starting from 1.
     *
     * @return A list of top 5 users with their scores and rankings.
     */
    fun fetchTopScored(): List<ScoreKorisnik> {
        val query = "SELECT * FROM korisnik ORDER BY poeni DESC LIMIT 5"
        val list = db.executeQuery(query) { rs ->
            val korisnickoIme = rs.getString("korisnickoIme")
            val poeni = rs.getInt("poeni")
            ScoreKorisnik(0, korisnickoIme, poeni)
        }
        return list.mapIndexed { index, korisnik ->
            korisnik.copy(mesto = index + 1)
        }
    }
}
