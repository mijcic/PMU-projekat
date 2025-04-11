package rs.ac.bg.etf.projekat


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import rs.ac.bg.etf.projekat.data.selectPorukeZadatak
import rs.ac.bg.etf.projekat.data.selectTelefonZadatak

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
                shape = MaterialTheme.shapes.medium,
                containerColor = Color(0xFF424242),
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
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp

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
                            .background(Color.Black.copy(alpha = 0.6f))
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 22.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Office",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    val buttons = listOf(
                        "Suspects" to destinationSuspectsPage.route,
                        "Witnesses" to destinationWitnessesPage.route,
                        "Evidence" to destinationEvidencePage.route,
                        "Phone" to destinationPhonePage.route
                    )

                    buttons.forEach { (label, route) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
                        ) {
                            Button(
                                onClick = {
                                    when (label) {
                                        "Evidence" -> {
                                            myViewModel.getEvidences()
                                            myViewModel.getForensicEvidences()
                                        }
                                        "Phone" -> {
                                            selectTelefonZadatak()?.let { myViewModel.updateTelefonTask(it) }
                                            selectPorukeZadatak()?.let { myViewModel.updatePorukeTask(it) }
                                        }
                                    }
                                    navController.navigate(route)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242))
                            ) {
                                Text(
                                    text = label,
                                    color = Color.White,
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.special_elite)),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}