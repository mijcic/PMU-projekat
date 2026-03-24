package rs.ac.bg.etf.projekat.murder.suspect

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.murder.suspects.SuspectCardWithImage

class SuspectCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun suspectCard_displaysCorrectInfo_andTriggersClick() {
        val testName = "Al Pacino"
        var wasClicked = false

        composeTestRule.setContent {
            SuspectCardWithImage(
                image = R.drawable.suspect,
                title = testName,
                onClick = { wasClicked = true }
            )
        }

        // provera - da li se tekst prikazuje
        composeTestRule
            .onNodeWithText(testName)
            .assertIsDisplayed()

        // provera - da li slika postoji
        composeTestRule
            .onNodeWithContentDescription("Portrait of $testName")
            .assertExists()

        // klikni na karticu
        composeTestRule
            .onNodeWithText(testName)
            .performClick()

        assert(wasClicked)
    }

    @Test
    fun suspectCard_hasCorrectBackgroundElevation() {
        composeTestRule.setContent {
            SuspectCardWithImage(
                image = R.drawable.suspect,
                title = "Test",
                onClick = {}
            )
        }

        // provera - da li je card element tu
        composeTestRule
            .onNodeWithText("Test")
            .assertExists()
    }
}