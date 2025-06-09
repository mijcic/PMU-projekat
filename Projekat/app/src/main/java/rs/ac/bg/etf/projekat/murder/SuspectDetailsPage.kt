package rs.ac.bg.etf.projekat.murder

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.destinationSuspectsInterviewPage

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SuspectDetailsPage(idOsoba: Int, image: Int, title: String, navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val realmViewModel: RealmViewModel = hiltViewModel()
    var motiveAlibiStatus by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        motiveAlibiStatus = realmViewModel.getMotiveAlibiStatus(idOsoba) ?: emptyList()
    }

    val tableData = listOf(
        listOf("Motive", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
        listOf("Alibi", motiveAlibiStatus.getOrNull(1).takeUnless { it.isNullOrBlank() } ?: "?"),
        listOf("Status", motiveAlibiStatus.getOrNull(2).takeUnless { it.isNullOrBlank() } ?: "?")
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.suspects_details_background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.92f)
                        .shadow(12.dp, RoundedCornerShape(24.dp), clip = true)
                        .background(colorResource(id = R.color.light_gray))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Suspect Info",
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Image(
                            painter = painterResource(id = image),
                            contentDescription = "Suspect Image",
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color.Black, CircleShape)
                                .shadow(8.dp, CircleShape)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = title,
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        if (tableData.isNotEmpty()) {
                            oneRowAboutSuspect("Motive", tableData.get(0).get(1))
                            Spacer(modifier = Modifier.height(5.dp))
                            Divider(color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp))
                            Spacer(modifier = Modifier.height(5.dp))
                            oneRowAboutSuspect("Alibi", tableData.get(1).get(1))
                            Spacer(modifier = Modifier.height(5.dp))
                            Divider(color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp))
                            Spacer(modifier = Modifier.height(5.dp))
                            oneRowAboutSuspect("Status", tableData.get(2).get(1))
                        }

                        Spacer(modifier = Modifier.height(25.dp))

                        Button(
                            onClick = {
                                navController.navigate(destinationSuspectsInterviewPage.route + "/" + title)
                            },
                            shape = RoundedCornerShape(16.dp),
//                            colors = ButtonDefaults.buttonColors(Color(0XFFA99367)),
//                            colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_purple)),
                            colors = ButtonDefaults.buttonColors(Color(0XFFA99367)),
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(horizontal = 16.dp)
                                .height(50.dp)
//                                .border(
//                                    width = 1.dp,
//                                    color = Color.White,
//                                    shape = RoundedCornerShape(16.dp)
//                                )
                        ) {
                            Text(
                                text = "Interrogate the Suspect",
                                color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.special_elite)),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun oneRowAboutSuspect(tekst1: String, tekst2: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = tekst1,
                color = Color.Black,
                maxLines = 100,
                softWrap = true,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    fontSize = 17.sp
                )
            )
        }

        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = tekst2,
                color = Color.Black,
                maxLines = 100,
                softWrap = true,
                textAlign = TextAlign.End,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    fontSize = 17.sp,
                )
            )
        }
    }
}