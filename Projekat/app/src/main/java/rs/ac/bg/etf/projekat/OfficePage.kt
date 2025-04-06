package rs.ac.bg.etf.projekat


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel

@Composable
fun OfficePage(navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel) {
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
                shape = MaterialTheme.shapes.small,
                containerColor = Color.DarkGray
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Tasks",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tasks",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp
                var textWidth by remember { mutableStateOf(0f) }
                var paddingStart by remember { mutableStateOf(0.dp) }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.office),
                        contentDescription = "Background Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 22.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Column(
                        modifier = Modifier
                    ) {
                        Button(onClick = { navController.navigate(destinationSuspectsPage.route) }) {
                            Text(
                                text = "Suspects", color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(
                                        Font(R.font.special_elite)
                                    ), fontSize = 26.sp, color = Color.Black
                                )
                            )
                        }
                        Button(onClick = { navController.navigate(destinationWitnessesPage.route) }) {
                            Text(
                                text = "Witnesses", color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(
                                        Font(R.font.special_elite)
                                    ), fontSize = 26.sp, color = Color.Black
                                )
                            )
                        }
                        Button(onClick = {
                            navController.navigate(destinationEvidencePage.route)
                            myViewModel.getEvidences()
                        }) {
                            Text(
                                text = "Evidence", color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(
                                        Font(R.font.special_elite)
                                    ), fontSize = 26.sp, color = Color.Black
                                )
                            )
                        }
                        Button(onClick = { navController.navigate(destinationPhonePage.route) }) {
                            Text(
                                text = "Phone", color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(
                                        Font(R.font.special_elite)
                                    ), fontSize = 26.sp, color = Color.Black
                                )
                            )
                        }
                    }
                }
            }
        }
    )
}
