package rs.ac.bg.etf.projekat.murder


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
import androidx.compose.material3.Icon
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
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.navigation.destinationEvidencePage
import rs.ac.bg.etf.projekat.navigation.destinationMapPage
import rs.ac.bg.etf.projekat.navigation.destinationPhonePage
import rs.ac.bg.etf.projekat.navigation.destinationSuspectsPage
import rs.ac.bg.etf.projekat.navigation.destinationWitnessesPage

@Composable
fun OfficePage(navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel) {
    val crimeData = realmViewModel.uiStateCrimeData.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    myViewModel.getTasks()
                    navController.navigate(destinationMapPage.route)
                },
//                containerColor = colorResource(R.color.dark_purple),
                //containerColor = colorResource(id = R.color.light_gray),
                containerColor = Color(0XFFA99367),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .size(60.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.planning),
                    contentDescription = "Tasks",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
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
                                myViewModel.selectTelefonZadatakViewModel()
                                myViewModel.selectPorukeZadatakViewModel()
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