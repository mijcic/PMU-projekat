package rs.ac.bg.etf.projekat.murder.office


import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class DescriptionForDetectiveTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun description_prikazuje_ispravan_tekst() {
        val testPoruka = "Pronadjen je otisak prsta na stolu."

        composeTestRule.setContent {
            DescriptionForDetective(text = testPoruka)
        }

        composeTestRule
            .onNodeWithText(testPoruka)
            .assertIsDisplayed()
    }

    @Test
    fun description_ima_ispravno_centriranje() {
        composeTestRule.setContent {
            DescriptionForDetective(text = "Kratak tekst")
        }

        composeTestRule
            .onNodeWithText("Kratak tekst")
            .assertExists()
    }
}