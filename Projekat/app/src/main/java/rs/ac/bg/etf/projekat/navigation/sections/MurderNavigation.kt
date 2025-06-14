package rs.ac.bg.etf.projekat.navigation.sections

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import rs.ac.bg.etf.projekat.CardsPage
import rs.ac.bg.etf.projekat.ErrorPage
import rs.ac.bg.etf.projekat.EvidencePage
import rs.ac.bg.etf.projekat.MapPage
import rs.ac.bg.etf.projekat.MissionPage
import rs.ac.bg.etf.projekat.QuestionsPage
import rs.ac.bg.etf.projekat.ScorePage
import rs.ac.bg.etf.projekat.ScoreQuestionsPage
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.murder.OfficePage
import rs.ac.bg.etf.projekat.murder.SuspectDetailsPage
import rs.ac.bg.etf.projekat.murder.SuspectInterviewPage
import rs.ac.bg.etf.projekat.murder.SuspectsPage
import rs.ac.bg.etf.projekat.murder.WitnessDetailsPage
import rs.ac.bg.etf.projekat.murder.WitnessesInterviewPage
import rs.ac.bg.etf.projekat.murder.WitnessesPage

@SuppressLint("NewApi")
fun NavGraphBuilder.murderNavigation(
    navController: NavHostController,
    viewModel: MyViewModel,
    realmViewModel: RealmViewModel
) {
    composable("destinationOfficePage"){
        OfficePage(navController,viewModel,realmViewModel)
    }
    composable("destinationSuspectsPage"){
        SuspectsPage(navController,viewModel,realmViewModel)
    }
    composable(route = "destinationSuspectDetailsPage/{idOsoba}/{image}/{title}",
        arguments = listOf(
            navArgument("idOsoba") { type = NavType.IntType },
            navArgument("image") { type = NavType.IntType },
            navArgument("title") { type = NavType.StringType }),
    ) { navBackStackEntry ->
        val idOsoba = navBackStackEntry.arguments?.getInt("idOsoba") ?: 0
        val image = navBackStackEntry.arguments?.getInt("image") ?: 0
        val title =navBackStackEntry.arguments?.getString("title") ?:""
        SuspectDetailsPage(idOsoba=idOsoba, image=image,title=title,navController)
    }
    composable(route = "destinationSuspectsInterviewPage/{title}",
        arguments = listOf(
            navArgument("title") { type = NavType.StringType }),
    ) { navBackStackEntry ->
        val title =navBackStackEntry.arguments?.getString("title") ?:""
        SuspectInterviewPage(navController, viewModel,title,realmViewModel)
    }
    composable(route = "destinationWitnessesInterviewPage/{title}",
        arguments = listOf(
            navArgument("title") { type = NavType.StringType }),
    ) { navBackStackEntry ->
        val title =navBackStackEntry.arguments?.getString("title") ?:""

        WitnessesInterviewPage(navController,viewModel, title)
    }
    composable(route = "destinationWitnessDetailsPage/{image}/{title}",
        arguments = listOf(navArgument("image") { type = NavType.IntType },
            navArgument("title") { type = NavType.StringType }),
    ) { navBackStackEntry ->
        val image = navBackStackEntry.arguments?.getInt("image") ?: 0
        val title =navBackStackEntry.arguments?.getString("title") ?:""
        WitnessDetailsPage(image=image,title=title,navController)
    }
    composable("destinationWitnessesPage"){
        WitnessesPage(navController, viewModel,realmViewModel)
    }
    composable("destinationQuestionsPage") {
        QuestionsPage(navController, viewModel)
    }
    composable("destinationEvidencePage") {
        EvidencePage(navController, viewModel,realmViewModel)
    }
    composable("destinationMapPage") {
        MapPage(navController, viewModel,realmViewModel)
    }
    composable("destinationErrorPage"){
        ErrorPage()
    }
    composable("destinationCardsPage") {
        CardsPage(Modifier,navController, viewModel, realmViewModel)
    }
    composable(route = "destinationMissionPage/{image}/{title}/{date}/{place}/{description}",
        arguments = listOf(
            navArgument("image") { type = NavType.IntType },
            navArgument("title") { type = NavType.StringType },
            navArgument("date") { type = NavType.StringType },
            navArgument("place") { type = NavType.StringType },
            navArgument("description") { type = NavType.StringType })
    ) { navBackStackEntry ->
        val image = navBackStackEntry.arguments?.getInt("image") ?: 0
        val title = navBackStackEntry.arguments?.getString("title") ?:""
        val date = navBackStackEntry.arguments?.getString("date") ?:""
        val place = navBackStackEntry.arguments?.getString("place") ?:""
        val description = navBackStackEntry.arguments?.getString("description") ?:""
        MissionPage(image=image, title=title, date = date, place = place, description = description, navController,realmViewModel)
    }

    composable("destinationScorePage") {
        ScorePage(navController,viewModel)
    }

    composable(route = "destinationScoreQuestionsPage/{totalScore}",
        arguments = listOf(navArgument("totalScore") { type = NavType.StringType }),
    ) { navBackStackEntry ->
        val totalScore =navBackStackEntry.arguments?.getString("totalScore") ?:""
        ScoreQuestionsPage(navController,totalScore,viewModel)
    }
}
