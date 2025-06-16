package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.navigation.destinationHospitalPage
import rs.ac.bg.etf.projekat.navigation.destinationOfficePage

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
    val crimeData = realmViewModel.uiStateCrimeData.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
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
        Image(
            painter = painterResource(id = image),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

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