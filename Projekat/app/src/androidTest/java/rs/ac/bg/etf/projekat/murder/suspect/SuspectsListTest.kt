package rs.ac.bg.etf.projekat.murder.suspect

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import rs.ac.bg.etf.projekat.data.realm.OsobaR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.murder.suspects.SuspectsList

class SuspectsListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun suspectsList_prikazuje_sve_osumnjicene_iz_liste() {
        val mockSuspects = listOf(
            OsumnjicenR().apply {
                osobaId = OsobaR().apply { idOsoba = 1; ime = "Mika Mikic" }
            },
            OsumnjicenR().apply {
                osobaId = OsobaR().apply { idOsoba = 2; ime = "Pera Peric" }
            },
            OsumnjicenR().apply {
                osobaId = OsobaR().apply { idOsoba = 3; ime = "Zika Zikic" }
            }
        )

        composeTestRule.setContent {
            SuspectsList(
                suspects = mockSuspects,
                onSuspectClick = { _, _ -> }
            )
        }

        // provera - da li se vidi sve
        composeTestRule.onNodeWithText("Mika Mikic").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pera Peric").assertIsDisplayed()
        composeTestRule.onNodeWithText("Zika Zikic").assertIsDisplayed()
    }

    @Test
    fun suspectsList_scrolling_radi() {
        // dugacka lista za scroll
        val dugaLista = List(20) { i ->
            OsumnjicenR().apply {
                osobaId = OsobaR().apply { idOsoba = i; ime = "Osumnjiceni $i" }
            }
        }

        composeTestRule.setContent {
            SuspectsList(suspects = dugaLista, onSuspectClick = { _, _ -> })
        }

        composeTestRule.onNodeWithText("Osumnjiceni 0").assertIsDisplayed()

        // skroluj do poslednjeg
        composeTestRule.onNode(hasScrollAction()).performScrollToNode(hasText("Osumnjiceni 19"))

        // provera da li je poslednji vidljiv
        composeTestRule.onNodeWithText("Osumnjiceni 19").assertIsDisplayed()
    }
}