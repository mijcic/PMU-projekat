package rs.ac.bg.etf.projekat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MainScreen2(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val imagePainter = painterResource(id = R.drawable.main_screen_background)
        var explanationOn by rememberSaveable { mutableStateOf(false) }

        Image(
            painter = imagePainter,
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.navigate("destinationLoginPage") },
                    modifier = Modifier.shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(15.dp),
                        clip = false
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = { navController.navigate("destinationSettingsPage") },
                    modifier = Modifier.shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(15.dp),
                        clip = false
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings Icon",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.3f))

            Text(
                text = "Welcome, detective!",
                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                fontSize = 35.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(10f, 10f),
                        blurRadius = 20f
                    )
                )
            )

            Button(
                onClick = { explanationOn = !explanationOn },
                colors = ButtonDefaults.buttonColors(Color(0XFFA99367)),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(0.8f).padding(10.dp).shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(15.dp),
                    clip = false
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (!explanationOn) "View a detailed explanation of the game" else "Hide a detailed explanation",
                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                        fontSize = 19.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(5f, 5f),
                                blurRadius = 10f
                            )
                        )
                    )
                    Icon(
                        painter = if (!explanationOn) painterResource(id = R.drawable.arrow_down) else painterResource(id = R.drawable.arrow_up),
                        contentDescription = "Arrow",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            AnimatedVisibility(visible = explanationOn) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .border(2.dp, Color.White)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Whodunit is an interactive detective game where you choose your own path to solving mysteries! As a skilled detective, you gather clues and interrogate suspicious characters. Every decision shapes the investigation – will you follow your instincts or rely on the evidence? Each puzzle is key to the truth, but be careful – one wrong move could lead to a dead end! Will you uncover the truth or remain trapped in a web of lies? The choice is yours!",
                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                        fontSize = 15.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate("destinationCardsPage") },
                    colors = ButtonDefaults.buttonColors(Color(0XFFA99367)),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.padding(10.dp).wrapContentWidth().shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(15.dp),
                        clip = false
                    )
                ) {
                    Text(
                        text = "PLAY GAME",
                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                        fontSize = 25.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(5f, 5f),
                                blurRadius = 10f
                            )
                        )
                    )
                }

                IconButton(
                    onClick = { navController.navigate("destinationScorePage") },
                    modifier = Modifier.padding(12.dp).shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(15.dp),
                        clip = false
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.trophy_fill),
                        contentDescription = "Trophy",
                        tint = colorResource(id = R.color.golden_yellow),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}
