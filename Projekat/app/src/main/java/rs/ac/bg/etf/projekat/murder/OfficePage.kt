package rs.ac.bg.etf.projekat.murder

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    myViewModel.getTasks()
                    navController.navigate(destinationMapPage.route)
                },
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

                TopicForInvestigation(
                    imageSize = imageSize,
                    x = 0.465f,
                    y = 0.74f,
                    text = "Victim's Phone",
                    onClick = {
                        myViewModel.selectTelefonZadatakViewModel()
                        myViewModel.selectPorukeZadatakViewModel()
                        navController.navigate(destinationPhonePage.route)
                    }
                )

                TopicForInvestigation(
                    imageSize = imageSize,
                    x = 0.35f,
                    y = 0.3f,
                    text = "Suspects",
                    onClick = {
                        navController.navigate(destinationSuspectsPage.route)
                    }
                )

                TopicForInvestigation(
                    imageSize = imageSize,
                    x = 0.045f,
                    y = 0.74f,
                    text = "Witnesses",
                    onClick = {
                        navController.navigate(destinationWitnessesPage.route)
                    }
                )

                TopicForInvestigation(
                    imageSize = imageSize,
                    x = 0.2f,
                    y = 0.9f,
                    text = "Evidences",
                    onClick = {
                        myViewModel.getEvidences()
                        myViewModel.getForensicEvidences()
                        navController.navigate(destinationEvidencePage.route)
                    }
                )

                DescriptionForDetective()
            }
        }
    )
}

@Composable
fun TopicForInvestigation(
    imageSize: IntSize,
    x: Float,
    y: Float,
    text: String,
    onClick: () -> Unit
) {
    if (imageSize.width > 0 && imageSize.height > 0) {
        val xOffset = (imageSize.width * x).toInt()
        val yOffset = (imageSize.height * y).toInt()

        Text(
            text = text,
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
                    onClick()
                }
        )
    }
}

@Composable
fun DescriptionForDetective() {
    Column(
        modifier = Modifier
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