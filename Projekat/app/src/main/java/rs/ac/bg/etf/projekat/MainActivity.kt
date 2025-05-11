package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
import rs.ac.bg.etf.projekat.data.realm.realmClasses
import rs.ac.bg.etf.projekat.mysteriousSymptoms.HospitalPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.InvestigationScreen
import rs.ac.bg.etf.projekat.mysteriousSymptoms.LekarskiTestPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.LocationPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.MedicalReportScreen
import rs.ac.bg.etf.projekat.mysteriousSymptoms.MedicalStatementPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.PatientScreen
import rs.ac.bg.etf.projekat.ui.theme.ProjekatTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        lateinit var realm: Realm

        fun clearDatabase() {
            realm.writeBlocking {
                deleteAll()
            }
        }
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

@SuppressLint("NewApi")
@Composable
fun NavigationGraph(navController: NavHostController) {

    val viewModel: MyViewModel= hiltViewModel()
    val realmViewModel: RealmViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        //startDestination = "destinationMainScreen1"
        startDestination = "destinationMainScreen2"
        //startDestination = "destinationCultsAndSectsPage"
        //startDestination = "destinationHospitalPage"
        //startDestination = "destinationSuspectDetailsPage/${1}/${R.drawable.whatsapp_profile_picture}/${"Blabla"}"
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
            ScorePage(navController,viewModel)
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
            ChatPage(id, name, photo, navController)
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
            PhoneSettingsPage(navController)
        }
        composable("destinationInvestigationPage") {
            InvestigationScreen(navController, viewModel,realmViewModel)
        }
        composable("destinationMedicalReportPage") {
            MedicalReportScreen(navController)
        }
        composable("destinationPatientPage"){
            PatientScreen(navController,realmViewModel)
        }
        composable("destinationMedicalStatementPage"){
            MedicalStatementPage(navController,realmViewModel)
        }
        composable("destinationLekarskiTestPage"){
            LekarskiTestPage()
        }
        composable("destinationCultsAndSectsPage"){
            CultAndSectsPage()
        }
        composable("destinationHospitalPage"){
            HospitalPage(navController,viewModel,realmViewModel)
        }
        composable("destinationLocationPage"){
            LocationPage(navController,viewModel,realmViewModel)
        }
    }
}