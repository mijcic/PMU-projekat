package com.example.service

import com.example.service.post.GeminiService
import com.example.utils.JsonLoader
import org.json.JSONObject

/**
 * Service responsible for initializing data related to murder investigations,
 * particularly for inserting default data if the database is empty.
 *
 * @property geminiService Service used to generate content (e.g. tables and data) via AI.
 * @property db The database service used to interact with the underlying data store.
 */
class InitialDataService(
    private val geminiService: GeminiService,
    private val db: DatabaseService
) {

    /**
     * Inserts initial data related to a murder case if the murder table is currently empty.
     *
     * This method:
     * - Checks if the database has no entries for murders.
     * - Loads a predefined JSON prompt and table structure.
     * - Uses [GeminiService] to generate data based on the prompt.
     * - Prints the generated result (for now, actual insertion logic not yet implemented).
     */
    suspend fun insertInitialMurderIfEmpty() {
        if (db.isMurderTableEmpty(
                connection = db.getDatabaseConnection()
            )) {
            println("yes if")
            val jsonMurder = JsonLoader.getJsonMurder()
            val json = JSONObject(jsonMurder)

            val prompt = json.getString("prompt")
            val tables = json.getJSONObject("tables").toString()

            val result = geminiService.generateContent(prompt, tables)
            println("Rezultat: $result")
        }
        else{
            println("no if")
        }
    }
}
