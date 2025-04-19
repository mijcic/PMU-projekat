package rs.ac.bg.etf.projekat


import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.data.selectPorukeZadatak
import rs.ac.bg.etf.projekat.data.selectTelefonZadatak

@Composable
fun OfficePage(navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel) {
    val crimeData = realmViewModel.uiStateCrimeData.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { AnimatedInfoBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    myViewModel.getTasks()
                    navController.navigate(destinationMapPage.route)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(60.dp),
                shape = MaterialTheme.shapes.medium,
                containerColor = Color.Black,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Tasks",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Background
                Image(
                    painter = painterResource(id = R.drawable.office),
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            renderEffect = BlurEffect(3f, 3f)
                        },
                    contentScale = ContentScale.Crop
                )

                Box(modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.7f)))

                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Case title
                    crimeData.value.title?.let {
                        Text(
                            text = "CASE: "+it,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White, textAlign =  TextAlign.Center
                        )
                    )}
                    crimeData.value.date?.let {
                        Text(
                            text = "Location: "+crimeData.value.place+" – Date:"+it,
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp),
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                color = Color.White
                            )
                        )
                    }

                    // First row (2 buttons)
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OfficeButton(label = "Suspects", icon = R.drawable.ic_suspect) {
                            navController.navigate(destinationSuspectsPage.route)
                        }
                        OfficeButton(label = "Witnesses", icon = R.drawable.ic_witness) {
                            navController.navigate(destinationWitnessesPage.route)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Second row (2 buttons)
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OfficeButton(label = "Evidence", icon = R.drawable.ic_evidence) {
                            myViewModel.getEvidences()
                            myViewModel.getForensicEvidences()
                            navController.navigate(destinationEvidencePage.route)
                        }
                        OfficeButton(label = "Phone", icon = R.drawable.ic_phone) {
                            selectTelefonZadatak()?.let { myViewModel.updateTelefonTask(it) }
                            selectPorukeZadatak()?.let { myViewModel.updatePorukeTask(it) }
                            navController.navigate(destinationPhonePage.route)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }

    )
}

@Composable
fun OfficeButton(label: String, icon: Int, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "ButtonScaleAnimation"
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(width = 150.dp, height = 100.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Button(
            onClick = {
                isPressed = true
                onClick()
                isPressed = false
            },
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier.size(52.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = label,
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedInfoBar() {
    val messages = listOf(
        "🕒 Vreme smrti: 03:24 AM",
        "🧬 DNK analiza u toku",
        "📱 Šifra telefona: 4862",
        "🎥 Kamera kazina: Nema snimaka"
    )
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L)
            currentIndex = (currentIndex + 1) % messages.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1C))
            .navigationBarsPadding()  // OVDE JE KLJUČ
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = messages[currentIndex],
            color = Color.LightGray,
            fontSize = 14.sp,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite))
            )
        )
    }
}
