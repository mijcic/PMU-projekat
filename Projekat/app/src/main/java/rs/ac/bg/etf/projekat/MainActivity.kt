package rs.ac.bg.etf.projekat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import rs.ac.bg.etf.projekat.data.realm.MisijaR
import rs.ac.bg.etf.projekat.data.realm.NapredakIstrageR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.realmClasses
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

    NavHost(
        navController = navController,
        startDestination = "destinationMainScreen1"
    ) {
        composable("destinationMainScreen1") {
            MainScreen1(navController)
        }
        composable("destinationMainScreen2") {
            MainScreen2(navController)
        }
        composable("destinationCardsPage") {
            CardsPage(Modifier,navController)
        }
        composable(route = "destinationMissionPage/{image}/{title}",
            arguments = listOf(navArgument("image") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType }),
            ) { navBackStackEntry ->
            val image = navBackStackEntry.arguments?.getInt("image") ?: 0
            val title =navBackStackEntry.arguments?.getString("title") ?:""
            MissionPage(image=image,title=title,navController)
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
    }
}