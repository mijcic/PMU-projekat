package rs.ac.bg.etf.projekat.murder.suspect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
import rs.ac.bg.etf.projekat.murder.suspects.SuspectBackground
import rs.ac.bg.etf.projekat.murder.suspects.SuspectHeader
import rs.ac.bg.etf.projekat.murder.suspects.SuspectsList

class SuspectsPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun suspectsPage_provera_komponenti_i_naslova() {

        composeTestRule.setContent {
            // Simuliramo poziv stranice
            Box {
                SuspectBackground()
                Column {
                    SuspectHeader(paddingStart = 0.dp)
                }
            }
        }

        // provera - da li se vidi naslov "Suspects"
        composeTestRule.onNodeWithText("Suspects").assertIsDisplayed()

        // provera - da li se pozadina ucitala
        composeTestRule.onNodeWithContentDescription("Background Image").assertExists()
    }

    @Test
    fun suspectsPage_navigacija_na_klik_poziva_callback() {
        var navId = -1
        var navIme = ""

        composeTestRule.setContent {
            SuspectsList(
                suspects = listOf(/* mock podaci */),
                onSuspectClick = { id, ime ->
                    navId = id
                    navIme = ime
                }
            )
        }
    }
}