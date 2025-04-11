package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.data.Repository
import rs.ac.bg.etf.projekat.data.realm.MisijaR
import rs.ac.bg.etf.projekat.data.realm.NapredakIstrageR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.realmClasses
import rs.ac.bg.etf.projekat.data.retrofit.Api
import rs.ac.bg.etf.projekat.ui.theme.ProjekatTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val realmClassesSet = realmClasses.toSet()

        val config = RealmConfiguration.Builder(
            schema = realmClassesSet
        )
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build()

        realm = Realm.open(config)


        enableEdgeToEdge()
        setContent {
            ProjekatTheme {
                val navController = rememberNavController()
                NavigationGraph(navController)
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {

    val viewModel: MyViewModel= hiltViewModel()
    val realmViewModel: RealmViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        //startDestination = "destinationMainScreen1"
        //startDestination = "destinationMainScreen2"
        startDestination = "destinationQuestionsPage"
    ) {
        composable("destinationMainScreen1") {
            MainScreen1(navController)
        }
        composable("destinationMainScreen2") {
            MainScreen2(navController)
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
        composable("destinationSettingsPage") {
            SettingsPage(navController)
        }
        composable("destinationScorePage") {
            ScorePage(navController)
        }
        composable("destinationLoginPage") {
            LoginPage(navController)
        }
        composable("destinationSignUpPage") {
            SignUpPage(navController)
        }
        composable("destinationOfficePage"){
            OfficePage(navController,viewModel,realmViewModel)
        }
        composable("destinationSuspectsPage"){
            SuspectsPage(navController,viewModel,realmViewModel)
        }
        composable(route = "destinationSuspectDetailsPage/{image}/{title}",
            arguments = listOf(navArgument("image") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType }),
        ) { navBackStackEntry ->
            val image = navBackStackEntry.arguments?.getInt("image") ?: 0
            val title =navBackStackEntry.arguments?.getString("title") ?:""
            SuspectDetailsPage(image=image,title=title,navController)
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
        composable("destinationPhonePage") {
            PhonePage(navController)
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
        composable(route = "destinationScoreQuestionsPage/{totalScore}",
            arguments = listOf(navArgument("totalScore") { type = NavType.StringType }),
        ) { navBackStackEntry ->
            val totalScore =navBackStackEntry.arguments?.getString("totalScore") ?:""
            ScoreQuestionsPage(navController,totalScore,viewModel)
        }
    }
}