package rs.ac.bg.etf.projekat

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.NavController

@Composable
fun QuestionsPage(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val imagePainter = painterResource(id = R.drawable.questions_background)
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp

        var questions = listOf("Who do you think killed Isabelle Moreau?", "Who do you think killed Isabelle Moreau?",
            "Who do you think killed Isabelle Moreau?", "Who do you think killed Isabelle Moreau?")

        var answers = listOf("Marco Bellini", "Vincent Duval", "Amelia Fontaine")

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
            modifier = Modifier.fillMaxSize().padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Questions about the case",
                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                fontSize = 25.sp,
                color = colorResource(id = R.color.white)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Choose the correct answer",
                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.mission_light_gray)
            )

            LazyColumn {
                items(questions) { question ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.5f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = question,
                                modifier = Modifier.padding(16.dp),
                                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                                fontSize = 16.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )

                            answers.forEach { answer ->
                                Button(
                                    onClick = {},
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .padding(vertical = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF5E554F),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = answer,
                                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("destinationScorePage")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.dark_purple),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Did I solve the case?",
                            fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                            fontSize = 18.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.ExtraBold)
                        )
                    }
                }
            }
        }
    }
}