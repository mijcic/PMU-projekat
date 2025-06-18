package com.example.repository

import com.example.data.remote.tables.PacijentData
import com.example.data.remote.tables.ZadatakData
import com.example.data.remote.tables.ZlocinData
import com.example.data.remote.gemini.retrofit.GeminiResponse2MysteriousSymptoms
import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofitMysteriousSymptoms
import com.example.interfaces.GeminiResponseCommon2
import com.example.interfaces.GeminiResponseRetrofitCommon

/**
 * Repository interface for handling data insertion related to mysterious symptoms in the GeminiPro system.
 */
interface GeminiProMysteriousSymptomsRepository{
    /**
     * Inserts patient data, including the victim and reporter, into the repository.
     *
     * @param geminiResponse2 Data transfer object containing parsed patient information.
     * @param geminiResponseRetrofit Retrofit-compatible response object to be updated.
     * @param zl Crime data associated with the patient.
     * @param repo Repository used for the actual data insertion.
     * @return Inserted patient data or null if insertion failed.
     */
    fun insertGeminiPacijent(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, zl: ZlocinData, repo: RepositoryInsert): PacijentData?

    /**
     * Inserts a medical report associated with a patient.
     *
     * @param geminiResponse2 DTO containing the report data.
     * @param geminiResponseRetrofit Retrofit-compatible response object to be updated.
     * @param pacijent The patient the report belongs to.
     * @param repo Repository used for data insertion.
     */
    fun insertGeminiMedicinskiIzvestaj(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData, repo: RepositoryInsert)

    /**
     * Inserts a statement given for a patient.
     *
     * @param geminiResponse2 DTO containing the statement data.
     * @param geminiResponseRetrofit Retrofit-compatible response object to be updated.
     * @param pacijent The patient the statement refers to.
     * @param zl Associated crime data.
     * @param repo Repository used for data insertion.
     */
    fun insertGeminiIzjavaZaPacijenta(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData, zl: ZlocinData, repo: RepositoryInsert)

    /**
     * Inserts a medical test associated with a patient.
     *
     * @param geminiResponse2 DTO containing the test data.
     * @param geminiResponseRetrofit Retrofit-compatible response object to be updated.
     * @param pacijent The patient who underwent the test.
     * @param repo Repository used for data insertion.
     */
    fun insertGeminiLekarskiTest(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, pacijent: PacijentData, repo: RepositoryInsert)

    /**
     * Inserts investigation location data related to a specific crime.
     *
     * @param geminiResponse2 DTO containing the investigation locations.
     * @param geminiResponseRetrofit Retrofit-compatible response object to be updated.
     * @param zl Associated crime data.
     * @param repo Repository used for data insertion.
     */
    fun insertGeminiLokacijeIstrage(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms, zl: ZlocinData, repo: RepositoryInsert)

    /**
     * Inserts tasks (zadatak) related to a patient and a crime.
     *
     * @param geminiResponse2 DTO containing task data.
     * @param zlocin Associated crime data.
     * @param repo Repository used for data insertion.
     * @return List of inserted tasks.
     */
    fun insertGeminiZadatakPacijent(geminiResponse2: GeminiResponseCommon2, zlocin: ZlocinData, repo: RepositoryInsert): MutableList<ZadatakData>

    /**
     * Updates a list of existing tasks related to a crime.
     *
     * @param geminiResponse2 DTO containing task data.
     * @param geminiResponseRetrofit Retrofit-compatible response object to be updated.
     * @param zlocin Associated crime data.
     * @param repo Repository used for update operations.
     */
    fun updateGeminiZadatakListPacijent(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zlocin: ZlocinData, repo:RepositoryInsert)
}