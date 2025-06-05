package rs.ac.bg.etf.projekat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.OdgovorR
import rs.ac.bg.etf.projekat.data.realm.PitanjeR

@Composable
fun ScoreQuestionsPage(navController: NavController, totalScore: String, myViewModel: MyViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
//        val imagePainter = painterResource(id = R.drawable.street_score)
        val imagePainter = painterResource(id = R.drawable.library_books)
        val realmViewModel: RealmViewModel = hiltViewModel()

        var questions by remember { mutableStateOf<List<PitanjeR>>(emptyList()) }
        var questionAnswersMap by remember { mutableStateOf<Map<PitanjeR, List<OdgovorR>>>(emptyMap()) }

        val selectedAnswers by myViewModel.uiSteteSelectedAnswers.collectAsState()

        LaunchedEffect(Unit) {
            questions = realmViewModel.getAllPitanje() ?: emptyList()
        }

        Image(
            painter = imagePainter,
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Score Page",
                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                fontSize = 28.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Your Score: $totalScore",
                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                fontSize = 20.sp,
                color = colorResource(id = R.color.mission_light_gray),
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {

                items(questions) { question ->
                    LaunchedEffect(question) {
                        if (!questionAnswersMap.containsKey(question)) {
                            val tacniOdgovori = realmViewModel
                                .getAllOdgovorForPitanje(question)
                                ?.filter { it.tacan } ?: emptyList()
                            questionAnswersMap = questionAnswersMap + (question to tacniOdgovori)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = question.tekst,
                                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                                fontSize = 18.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )

                            val tacniOdgovori = questionAnswersMap[question] ?: emptyList()
                            tacniOdgovori.forEach { answer ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color =  if (selectedAnswers.selectedAnswers?.get(question.idPitanje) == answer.idOdogovor) Color(0xFF388E3C).copy(alpha = 0.8f)
                                            else Color(0xFFB71C1C).copy(alpha = 0.8f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = answer.tekstOdgovora + " " +
                                                if (selectedAnswers.selectedAnswers?.get(question.idPitanje) == answer.idOdogovor)  "✅" else "❌",
                                                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.Normal)),
                                        fontSize = 15.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}