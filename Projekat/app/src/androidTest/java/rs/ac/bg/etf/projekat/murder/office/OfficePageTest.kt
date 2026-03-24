package rs.ac.bg.etf.projekat.murder.office

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class OfficePageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun officePage_provera_svih_elemenata_i_interakcija() {
        var phoneClicked = false
        var suspectsClicked = false
        var witnessesClicked = false
        var evidenceClicked = false
        var mapClicked = false
        var tasksLoaded = false

        // stranice sa laznim callback-ovima
        composeTestRule.setContent {
            OfficePage(
                onDestinationMapPageClick = { mapClicked = true },
                onDestinationPhonePageClick = { phoneClicked = true },
                onDestinationSuspectsPageClick = { suspectsClicked = true },
                onDestinationWitnessesPageClick = { witnessesClicked = true },
                onDestinationEvidencePageClick = { evidenceClicked = true },
                onLoadTasks = { tasksLoaded = true },
                onSelectPhoneTasks = {  },
                onLoadEvidences = {  }
            )
        }

        composeTestRule.onNodeWithContentDescription("Background Image").assertExists()

        composeTestRule.onNodeWithText("Detective, this is your office. Choose the topic you want to investigate.")
            .assertIsDisplayed()

        // testiranje klikova

        composeTestRule.onNodeWithText("Victim's Phone").performClick()
        assert(phoneClicked)

        composeTestRule.onNodeWithText("Suspects").performClick()
        assert(suspectsClicked)

        composeTestRule.onNodeWithText("Witnesses").performClick()
        assert(witnessesClicked)

        composeTestRule.onNodeWithText("Evidences").performClick()
        assert(evidenceClicked)


        composeTestRule.onNodeWithContentDescription("Tasks").performClick()

        assert(tasksLoaded)
        assert(mapClicked)
    }

    @Test
    fun topicForInvestigation_se_ne_prikazuje_ako_su_dimenzije_nula() {
        composeTestRule.setContent {
            TopicForInvestigation(
                imageSize = androidx.compose.ui.unit.IntSize(0, 0),
                x = 0.5f,
                y = 0.5f,
                text = "Invisible",
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("Invisible").assertDoesNotExist()
    }
}