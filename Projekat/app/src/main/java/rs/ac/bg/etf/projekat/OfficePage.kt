package rs.ac.bg.etf.projekat


import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                //shape = MaterialTheme.shapes.medium,
                shape = CircleShape,
                //containerColor = Color.Black,
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
            ) {
//                Icon(
//                    imageVector = Icons.Default.Check,
//                    contentDescription = "Tasks",
//                    tint = Color.White,
//                    modifier = Modifier.size(32.dp)
//                )
                Image(
                    painter = painterResource(id = R.drawable.tasks2),
                    contentDescription = "Tasks",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                var imageSize by remember { mutableStateOf(IntSize.Zero) }

                // Background
                Image(
                    painter = painterResource(id = R.drawable.office),
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { coordinates ->
                            imageSize = coordinates.size
                        }
                        .graphicsLayer {
                            renderEffect = BlurEffect(3f, 3f)
                        },
                    contentScale = ContentScale.Crop
                )

                Box(modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.3f)))

                if (imageSize.width > 0 && imageSize.height > 0) {
                    val xOffset = (imageSize.width * 0.465f).toInt()
                    val yOffset = (imageSize.height * 0.74f).toInt()

                    Text(
                        text = "Victim's Phone",
                        fontSize = 16.sp,
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .offset { IntOffset(x = xOffset, y = yOffset) }
                            .clickable {
                                selectTelefonZadatak()?.let { myViewModel.updateTelefonTask(it) }
                            selectPorukeZadatak()?.let { myViewModel.updatePorukeTask(it) }
                            navController.navigate(destinationPhonePage.route)
                            }
                    )
                }

                if (imageSize.width > 0 && imageSize.height > 0) {
                    val xOffset = (imageSize.width * 0.35f).toInt()
                    val yOffset = (imageSize.height * 0.3f).toInt()

                    Text(
                        text = "Suspects",
                        fontSize = 16.sp,
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .offset { IntOffset(x = xOffset, y = yOffset) }
                            .clickable {
                                navController.navigate(destinationSuspectsPage.route)
                            }
                    )
                }

                if (imageSize.width > 0 && imageSize.height > 0) {
                    val xOffset = (imageSize.width * 0.045f).toInt()
                    val yOffset = (imageSize.height * 0.74f).toInt()

                    Text(
                        text = "Witnesses",
                        fontSize = 16.sp,
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .offset { IntOffset(x = xOffset, y = yOffset) }
                            .clickable {
                                navController.navigate(destinationWitnessesPage.route)
                            }
                    )
                }

                if (imageSize.width > 0 && imageSize.height > 0) {
                    val xOffset = (imageSize.width * 0.2f).toInt()
                    val yOffset = (imageSize.height * 0.9f).toInt()

                    Text(
                        text = "Evidences",
                        fontSize = 16.sp,
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .offset { IntOffset(x = xOffset, y = yOffset) }
                            .clickable {
                                myViewModel.getEvidences()
                                myViewModel.getForensicEvidences()
                                navController.navigate(destinationEvidencePage.route)
                            }
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.3f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Detective, this is your office. Choose the topic you want to investigate.",
                            fontSize = 20.sp,
                            color = Color.White,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                        )
                    }

//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    Text(
//                        text = "CASE: Nathan's murder",
//                        style = TextStyle(
//                            fontFamily = FontFamily(Font(R.font.special_elite)),
//                            fontSize = 18.sp,
//                            //fontWeight = FontWeight.Bold,
//                            color = Color.LightGray, textAlign =  TextAlign.Center
//                        )
//                    )
//
//                    Spacer(modifier = Modifier.height(3.dp))
//
//                    Text(
//                        text = "Location: "+crimeData.value.place+" – Date: 25.08.2016.",
//                        color = Color.LightGray,
//                        fontSize = 18.sp,
//                        modifier = Modifier.padding(top = 4.dp, bottom = 32.dp),
//                        style = TextStyle(
//                            fontFamily = FontFamily(Font(R.font.special_elite)),
//                            color = Color.White, textAlign = TextAlign.Center
//                        )
//                    )

//                    // Case title
//                    crimeData.value.title?.let {
//                        Text(
//                            text = "CASE: "+it,
//                        style = TextStyle(
//                            fontFamily = FontFamily(Font(R.font.special_elite)),
//                            fontSize = 28.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White, textAlign =  TextAlign.Center
//                        )
//                    )}
//                    Spacer(modifier = Modifier.height(10.dp))
//                    crimeData.value.date?.let {
//                        Text(
//                            text = "Location: "+crimeData.value.place+" – Date:"+it,
//                            color = Color.LightGray,
//                            fontSize = 14.sp,
//                            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp),
//                            style = TextStyle(
//                                fontFamily = FontFamily(Font(R.font.special_elite)),
//                                color = Color.White, textAlign = TextAlign.Center
//                            )
//                        )
//                    }

//                    // First row (2 buttons)
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceEvenly,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        OfficeButton(label = "Suspects", icon = R.drawable.ic_suspect) {
//                            navController.navigate(destinationSuspectsPage.route)
//                        }
//                        OfficeButton(label = "Witnesses", icon = R.drawable.ic_witness) {
//                            navController.navigate(destinationWitnessesPage.route)
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Second row (2 buttons)
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceEvenly,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        OfficeButton(label = "Evidence", icon = R.drawable.ic_evidence) {
//                            myViewModel.getEvidences()
//                            myViewModel.getForensicEvidences()
//                            navController.navigate(destinationEvidencePage.route)
//                        }
//                        OfficeButton(label = "Phone", icon = R.drawable.ic_phone) {
//                            selectTelefonZadatak()?.let { myViewModel.updateTelefonTask(it) }
//                            selectPorukeZadatak()?.let { myViewModel.updatePorukeTask(it) }
//                            navController.navigate(destinationPhonePage.route)
//                        }
//                    }

//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        OfficeButton(label = "Suspects", icon = R.drawable.ic_suspect) {
//                            navController.navigate(destinationSuspectsPage.route)
//                        }
//                        Spacer(modifier = Modifier.height(10.dp))
//                        OfficeButton(label = "Witnesses", icon = R.drawable.ic_witness) {
//                            navController.navigate(destinationWitnessesPage.route)
//                        }
//                        Spacer(modifier = Modifier.height(10.dp))
//                        OfficeButton(label = "Evidences",
////                            icon = R.drawable.ic_evidence
//                            icon = R.drawable.evidences
//                        ) {
//                            myViewModel.getEvidences()
//                            myViewModel.getForensicEvidences()
//                            navController.navigate(destinationEvidencePage.route)
//                        }
//                        Spacer(modifier = Modifier.height(10.dp))
//                        OfficeButton(label = "Victim's Phone", icon = R.drawable.ic_phone) {
//                            selectTelefonZadatak()?.let { myViewModel.updateTelefonTask(it) }
//                            selectPorukeZadatak()?.let { myViewModel.updatePorukeTask(it) }
//                            navController.navigate(destinationPhonePage.route)
//                        }
//                    }

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
            .padding(4.dp)
            .size(width = 170.dp, height = 100.dp)
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
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
                    maxLines = 1,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
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
