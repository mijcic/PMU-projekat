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

object destinationPhonePage : NavigationDestinations {
    override val route = "destinationPhonePage"
}

object questionsPage : NavigationDestinations {
    override val route = "questionsPage"
}

val navigationDestinations = listOf(destinationMainScreen1, destinationMainScreen2,
    destinationCardsPage,destinationMissionPage, destinationSettingsPage,
    destinationScorePage, destinationLoginPage, destinationSignUpPage, destinationPhonePage,
    questionsPage)