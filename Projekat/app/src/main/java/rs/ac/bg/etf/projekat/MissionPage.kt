package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel

/*

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MissionPage(image:Int, title:String, date: String, place: String, description: String, navController: NavController,realmViewModel: RealmViewModel){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val viewModel:MyViewModel= hiltViewModel()

    LaunchedEffect(viewModel.uiState.value.zlocin) {
        InsertData(viewModel)
    }

    val crimeData = realmViewModel.uiStateCrimeData.collectAsState()
    Log.d("CrimeData2", crimeData.value.toString())

    Surface(
        modifier = Modifier.fillMaxSize().background(Color(0xFF233331)).padding(top=22.dp).clickable {
            navController.navigate(destinationOfficePage.route)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF233331))
                .padding(top=(screenWidth/8).dp).padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            crimeData.value.title?.let {
                Text(
                    text = it,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            crimeData.value.date?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = colorResource(id = R.color.mission_light_gray),
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            crimeData.value.place?.let {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Image (
                painter = painterResource(id = image),
                contentDescription = "Image",
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),
            )

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.mission_overlay_color), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                crimeData.value.description?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.special_elite)
                            )
                        )
                    )
                }
            }
        }
    }
}

 */

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MissionPage(
    image: Int,
    title: String,
    date: String,
    place: String,
    description: String,
    navController: NavController,
    realmViewModel: RealmViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val viewModel: MyViewModel = hiltViewModel()

    LaunchedEffect(viewModel.uiState.value.zlocin) {
        InsertData(viewModel)
    }

    val crimeData = realmViewModel.uiStateCrimeData.collectAsState()
    Log.d("CrimeData2", crimeData.value.toString())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                if(image==R.drawable.murder) {
                    navController.navigate(destinationOfficePage.route)
                }
                else{
                    navController.navigate(
                        destinationHospitalPage.route
                    )
                }
            }
    ) {
        // Background image
        Image(
            painter = painterResource(id = image),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if(image==R.drawable.m_symptoms2){
            //Box(
              //  modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f))
            //)
        }

        // Super modern card
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            ModernGlassCard(
                title = crimeData.value.title,
                date = crimeData.value.date,
                place = crimeData.value.place,
                description = crimeData.value.description
            )
        }
    }

}

@Composable
fun ModernGlassCard(
    title: String?,
    date: String?,
    place: String?,
    description: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 16.dp)
            .graphicsLayer {
                // Blur je samo vizuelni, ne muti tekst
                shadowElevation = 8.dp.toPx()
                shape = RoundedCornerShape(24.dp)
                clip = true
            }
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black.copy(alpha = 0.45f),
                        Color.Black.copy(alpha = 0.35f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            title?.let {
                Text(
                    text = it,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                date?.let {
                    Text(
                        text = "📅 $it",
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
                    )
                }
                place?.let {
                    Text(
                        text = "📍 $it",
                        fontSize = 14.sp,
                        color = Color.White,
                        style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            description?.let {
                Text(
                    text = it,
                    fontSize = 15.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    lineHeight = 20.sp,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
                )
            }
        }
    }
}
