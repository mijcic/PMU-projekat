package rs.ac.bg.etf.projekat

interface NavigationDestinations {
    val route: String
}

object destinationMainScreen1 : NavigationDestinations {
    override val route = "destinationMainScreen1"
}

object destinationMainScreen2 : NavigationDestinations {
    override val route = "destinationMainScreen2"
}

object destinationCardsPage: NavigationDestinations{
    override val route = "destinationCardsPage"
}

object destinationMissionPage: NavigationDestinations{
    override val route = "destinationMissionPage"
}

object destinationSettingsPage: NavigationDestinations{
    override val route = "destinationSettingsPage"
}

object destinationScorePage: NavigationDestinations{
    override val route = "destinationScorePage"
}

object destinationLoginPage : NavigationDestinations {
    override val route = "destinationLoginPage"
}

object destinationSignUpPage : NavigationDestinations {
    override val route = "destinationSignUpPage"
}

object destinationOfficePage : NavigationDestinations {
    override val route = "destinationOfficePage"
}

object destinationSuspectsPage : NavigationDestinations {
    override val route = "destinationSuspectsPage"
}

object destinationSuspectDetailsPage : NavigationDestinations {
    override val route = "destinationSuspectDetailsPage"
}

object destinationSuspectsInterviewPage: NavigationDestinations {
    override val route = "destinationSuspectsInterviewPage"
}

object destinationWitnessesPage: NavigationDestinations {
    override val route = "destinationWitnessesPage"
}

object destinationPhonePage : NavigationDestinations {
    override val route = "destinationPhonePage"
}

object questionsPage : NavigationDestinations {
    override val route = "destinationQuestionsPage"
}

object destinationWitnessDetailsPage : NavigationDestinations {
    override val route = "destinationWitnessDetailsPage"
}

object destinationWitnessesInterviewPage : NavigationDestinations {
    override val route = "destinationWitnessesInterviewPage"
}

object destinationEvidencePage : NavigationDestinations {
    override val route = "destinationEvidencePage"
}

object destinationWhatsAppPage : NavigationDestinations {
    override val route = "destinationWhatsAppPage"
}

object destinationWhatsAppChatPage : NavigationDestinations {
    override val route = "destinationWhatsAppChatPage"
}

object destinationNotesPage : NavigationDestinations {
    override val route = "destinationNotesPage"
}

object destinationOneNotePage : NavigationDestinations {
    override val route = "destinationOneNotePage"
}

object destinationCallsPage : NavigationDestinations {
    override val route = "destinationCallsPage"
}

object destinationPhonebookPage : NavigationDestinations {
    override val route = "destinationPhonebookPage"
}

object destinationOneContactPage : NavigationDestinations {
    override val route = "destinationOneContactPage"
}

object destinationKeypadPage : NavigationDestinations {
    override val route = "destinationKeypadPage"
}

object destinationMessagesPage : NavigationDestinations {
    override val route = "destinationMessagesPage"
}

object destinationChatPage : NavigationDestinations {
    override val route = "destinationChatPage"
}

object destinationGalleryPage : NavigationDestinations {
    override val route = "destinationGalleryPage"
}

object destinationOnePhotoPage : NavigationDestinations {
    override val route = "destinationOnePhotoPage"
}

object destinationPhoneSettingsPage : NavigationDestinations {
    override val route = "destinationPhoneSettingsPage"
}

val navigationDestinations = listOf(destinationMainScreen1, destinationMainScreen2,
    destinationCardsPage,destinationMissionPage, destinationSettingsPage,
    destinationScorePage, destinationLoginPage, destinationSignUpPage, destinationPhonePage,
    questionsPage, destinationWhatsAppPage, destinationWhatsAppChatPage, destinationNotesPage,
    destinationOneNotePage, destinationCallsPage, destinationPhonebookPage, destinationOneContactPage,
    destinationKeypadPage, destinationMessagesPage, destinationChatPage, destinationGalleryPage,
    destinationOnePhotoPage, destinationPhoneSettingsPage)

val phoneBarDestinations = listOf(
    destinationCallsPage,
    destinationPhonebookPage,
    destinationKeypadPage
)