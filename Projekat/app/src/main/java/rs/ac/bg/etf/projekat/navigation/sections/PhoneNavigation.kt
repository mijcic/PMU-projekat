package rs.ac.bg.etf.projekat.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import rs.ac.bg.etf.projekat.KeypadPage
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.phone.CallsPage
import rs.ac.bg.etf.projekat.phone.ChatPage
import rs.ac.bg.etf.projekat.phone.GalleryPage
import rs.ac.bg.etf.projekat.phone.MessagesPage
import rs.ac.bg.etf.projekat.phone.NotesPage
import rs.ac.bg.etf.projekat.phone.OneContactPage
import rs.ac.bg.etf.projekat.phone.OneNotePage
import rs.ac.bg.etf.projekat.phone.OnePhotoPage
import rs.ac.bg.etf.projekat.phone.PhonePage
import rs.ac.bg.etf.projekat.phone.PhoneSettingsPage
import rs.ac.bg.etf.projekat.phone.PhonebookPage
import rs.ac.bg.etf.projekat.phone.WhatsAppChatPage
import rs.ac.bg.etf.projekat.phone.WhatsAppPage

fun NavGraphBuilder.phoneNavigation(navController: NavHostController,myViewModel: MyViewModel) {
    composable("destinationPhonePage") {
        PhonePage(myViewModel, onClickNav = { destination ->
            navController.navigate(destination)
        })
    }
    composable("destinationWhatsAppPage") {
        WhatsAppPage(navController)
    }
    composable("destinationWhatsAppChatPage/{id}/{name}/{photo}",
        arguments = listOf(
            navArgument("id") { type = NavType.IntType },
            navArgument("name") { type = NavType.StringType },
            navArgument("photo") { type = NavType.IntType })
    ) { navBackStackEntry ->
        val id = navBackStackEntry.arguments?.getInt("id") ?: 0
        val name = navBackStackEntry.arguments?.getString("name") ?: ""
        val photo = navBackStackEntry.arguments?.getInt("photo") ?: 0
        WhatsAppChatPage(id, name, photo, navController)
    }
    composable("destinationNotesPage") {
        NotesPage(navController)
    }
    composable("destinationOneNotePage/{text}/{date}",
        arguments = listOf(
            navArgument("text") { type = NavType.StringType },
            navArgument("date") { type = NavType.StringType })
    ) { navBackStackEntry ->
        val text = navBackStackEntry.arguments?.getString("text") ?: ""
        val date = navBackStackEntry.arguments?.getString("date") ?: ""

        OneNotePage(text, date, navController)
    }
    composable("destinationCallsPage") {
        CallsPage(navController)
    }
    composable("destinationPhonebookPage") {
        PhonebookPage(navController)
    }
    composable("destinationOneContactPage/{name}/{phoneNumber}/{picture}",
        arguments = listOf(
            navArgument("name") { type = NavType.StringType },
            navArgument("phoneNumber") { type = NavType.StringType },
            navArgument("picture") { type = NavType.IntType })
    ) { navBackStackEntry ->
        val name = navBackStackEntry.arguments?.getString("name") ?: ""
        val phoneNumber = navBackStackEntry.arguments?.getString("phoneNumber") ?: ""
        val picture = navBackStackEntry.arguments?.getInt("picture") ?: 0

        OneContactPage(name, phoneNumber, picture, navController)
    }
    composable("destinationKeypadPage") {
        KeypadPage(navController)
    }
    composable("destinationMessagesPage") {
        MessagesPage(navController)
    }
    composable("destinationChatPage/{id}/{name}/{photo}",
        arguments = listOf(
            navArgument("id") { type = NavType.IntType },
            navArgument("name") { type = NavType.StringType },
            navArgument("photo") { type = NavType.IntType })
    ) { navBackStackEntry ->
        val id = navBackStackEntry.arguments?.getInt("id") ?: 0
        val name = navBackStackEntry.arguments?.getString("name") ?: ""
        val photo = navBackStackEntry.arguments?.getInt("photo") ?: 0
        ChatPage(id, name, photo)
    }
    composable("destinationGalleryPage") {
        GalleryPage(navController)
    }
    composable("destinationOnePhotoPage/{picture}/{datum}/{mesto}",
        arguments = listOf(
            navArgument("picture") { type = NavType.IntType },
            navArgument("datum") { type = NavType.StringType },
            navArgument("mesto") { type = NavType.StringType })
    ) { navBackStackEntry ->
        val picture = navBackStackEntry.arguments?.getInt("picture") ?: 0
        val datum = navBackStackEntry.arguments?.getString("datum") ?: ""
        val mesto = navBackStackEntry.arguments?.getString("mesto") ?: ""
        OnePhotoPage(picture, datum, mesto, navController)
    }
    composable("destinationPhoneSettingsPage") {
        PhoneSettingsPage(navController,myViewModel)
    }
}
