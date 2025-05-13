package rs.ac.bg.etf.projekat.mysteriousSymptoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.destinationEvidencePage
import rs.ac.bg.etf.projekat.destinationLocationPage
import rs.ac.bg.etf.projekat.destinationMapPage
import rs.ac.bg.etf.projekat.destinationPatientPage
import rs.ac.bg.etf.projekat.destinationPhonePage
import rs.ac.bg.etf.projekat.destinationSuspectsPage
import rs.ac.bg.etf.projekat.destinationWitnessesPage

@Composable
fun HospitalPage(navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    myViewModel.getTasks()
                    navController.navigate(destinationMapPage.route)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(60.dp),
                shape = CircleShape,
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
            ) {
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
            val p=paddingValues
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                var imageSize by remember { mutableStateOf(IntSize.Zero) }

                // Background image
                Image(
                    painter = painterResource(id = R.drawable.hospital),
                    contentDescription = "Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark overlay over background
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )

                // Content
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Intro text
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.3f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Detective...",
                            fontSize = 20.sp,
                            color = Color.White,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(204.dp))

                    // Cards
                    CardItem(
                        title = "Patient",
                        onClick = {
                            myViewModel.getAllDataMysteriousSymptoms()
                            navController.navigate(destinationPatientPage.route)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CardItem(
                        title = "Locations",
                        onClick = {
                            myViewModel.getAllDataMysteriousSymptoms()
                            navController.navigate(destinationLocationPage.route)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CardItem(
                        title = "Evidences",
                        onClick = {
                            navController.navigate(destinationEvidencePage.route)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun CardItem(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        )
    }
}
