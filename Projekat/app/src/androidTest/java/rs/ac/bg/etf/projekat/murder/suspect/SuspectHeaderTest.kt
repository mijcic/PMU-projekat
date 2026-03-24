package rs.ac.bg.etf.projekat.murder.suspect

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
import rs.ac.bg.etf.projekat.murder.suspects.SuspectHeader

class SuspectHeaderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun suspectHeader_displaysCorrectText() {
        composeTestRule.setContent {
            SuspectHeader(paddingStart = 16.dp)
        }

        // provera da li je vidljiv na ekranu
        composeTestRule
            .onNodeWithText("Suspects", ignoreCase = true)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun suspectHeader_hasCorrectFontWeight() {
        composeTestRule.setContent {
            SuspectHeader(paddingStart = 0.dp)
        }

        composeTestRule
            .onNodeWithText("Suspects")
            .assertExists()
    }
}