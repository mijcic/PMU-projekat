package rs.ac.bg.etf.projekat.murder.suspect

import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
import rs.ac.bg.etf.projekat.murder.suspects.SuspectBackground

class SuspectBackgroundTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun background_isDisplayed_andHasCorrectContentDescription() {
        composeTestRule.setContent {
            SuspectBackground()
        }

        // provera da li postoji slika sa opisom
        composeTestRule
            .onNodeWithContentDescription("Background Image")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun background_fillMaxSize_isCorrect() {
        composeTestRule.setContent {
            SuspectBackground()
        }

        // provera da li komponenta zauzima prostor na ekranu
        composeTestRule
            .onNodeWithContentDescription("Background Image")
            .assertHeightIsAtLeast(1.dp)
            .assertWidthIsAtLeast(1.dp)
    }
}