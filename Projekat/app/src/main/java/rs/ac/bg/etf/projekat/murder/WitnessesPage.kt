package rs.ac.bg.etf.projekat.murder


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.UiStateDataZlocin
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.navigation.destinationWitnessDetailsPage

@Composable
fun WitnessesPage(navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel){
    var paddingStart by remember { mutableStateOf(0.dp) }
    val uiStateDataZlocin by myViewModel.uiStateZlocinData.collectAsState()

    LaunchedEffect(uiStateDataZlocin.witnesses) {
        myViewModel.getAllDataZlocin()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        WitnessBackground()

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WitnessHeader(paddingStart = paddingStart)

            WitnessesList(
                uiStateDataZlocin = uiStateDataZlocin,
                navController = navController,
                myViewModel = myViewModel
            )
        }
    }
}

@Composable
fun WitnessBackground(){
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.suspects_background), // koristi istu kao za Suspects
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
    }
}

@Composable
fun WitnessHeader(paddingStart: Dp){
    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.height(16.dp))
    }
    Column(modifier = Modifier.padding(start = paddingStart)) {
        Text(
            text = "Witnesses", color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        )
    }
}

@Composable
fun WitnessCardWithImage(osobaId: Int, image: Int, title: String, navController: NavController, myViewModel: MyViewModel) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                myViewModel.getPitanjaZaSvedoka(title)
                navController.navigate(destinationWitnessDetailsPage.route + "/$osobaId/$image/$title")
            }
            .height(170.dp)
            .fillMaxWidth(0.4f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFA99367) // ista boja kao SuspectCard
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(13.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "Witness Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun WitnessesList(
    uiStateDataZlocin: UiStateDataZlocin,
    navController: NavController,
    myViewModel: MyViewModel
) {
    Column(modifier = Modifier) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                uiStateDataZlocin.witnesses.forEach { witness ->
                    Log.d("GEMINI WIT",witness.idSvedok.toString())
                    witness.osobaId?.let { osoba ->
                        WitnessCardWithImage(
                            osobaId = osoba.idOsoba,
                            image = R.drawable.witness, // zameni po potrebi
                            title = osoba.ime,
                            navController = navController,
                            myViewModel = myViewModel
                        )
                    }
                }
            }
        }
    }
}