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

val navigationDestinations = listOf(destinationMainScreen1, destinationMainScreen2)