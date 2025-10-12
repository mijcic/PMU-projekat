package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
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
import com.google.android.material.color.utilities.Score
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.UiSteteSelectedAnswers
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.OdgovorR
import rs.ac.bg.etf.projekat.data.realm.PitanjeR
import rs.ac.bg.etf.projekat.data.retrofit.models.ScoreKorisnikaRequest

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ScoreQuestionsPage(navController: NavController, totalScore: String, myViewModel: MyViewModel) {
    val realmViewModel: RealmViewModel = hiltViewModel()
    var questions by remember { mutableStateOf<List<PitanjeR>>(emptyList()) }
    val selectedAnswers by myViewModel.uiSteteSelectedAnswers.collectAsState()
    var userScore by remember { mutableStateOf(0) }

    val uiStateKorisnik by myViewModel.uiStateKorisnik.collectAsState()
    val loggedUser = uiStateKorisnik.korisnickoIme

    LaunchedEffect(userScore) {
        if (loggedUser != null) {
            realmViewModel.insertScoreKorisnika(loggedUser, userScore)

            val request = ScoreKorisnikaRequest(loggedUser, userScore)
            myViewModel.setScoreKorisnika(request)

            Log.d("SCORE_SAVE", "Score $userScore sačuvan za $loggedUser")
        }
    }

    LaunchedEffect(Unit) {
        questions = realmViewModel.getAllPitanje() ?: emptyList()
    }

    LaunchedEffect(questions.hashCode(), selectedAnswers.hashCode()) {
        userScore = calculateScore(
            questions,
            selectedAnswers.selectedAnswers ?: emptyMap(),
            realmViewModel
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        BackgroundWithOverlay(imagePainter = R.drawable.library_books, alpha = 0.6F)

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            ScoreHeader(totalScore = totalScore)

            ScoreQuestionsList(
                realmViewModel = realmViewModel,
                questions = questions,
                selectedAnswers = selectedAnswers
            )
        }
    }
}

@Composable
fun ScoreHeader(totalScore: String) {
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
}

@Composable
fun BackgroundWithOverlay(imagePainter:Int, alpha:Float){
    Image(
        painter = painterResource(id = imagePainter),
        contentDescription = "Background image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = alpha)))
}

@Composable
fun ScoreQuestionsList(
    selectedAnswers: UiSteteSelectedAnswers,
    realmViewModel: RealmViewModel,
    questions: List<PitanjeR>
) {
    var questionAnswersMap by remember { mutableStateOf<Map<PitanjeR, List<OdgovorR>>>(emptyMap()) }
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

            ScoreQuestionsCard(
                question = question,
                answers = questionAnswersMap[question] ?: emptyList(),
                selectedAnswer = selectedAnswers.selectedAnswers?.get(question.idPitanje)
            )
        }
    }
}


@Composable
fun ScoreQuestionsCardQuestionText(questionText: String) {
    Text(
        text = questionText,
        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
        fontSize = 18.sp,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ScoreQuestionsCard(question: PitanjeR, answers: List<OdgovorR>, selectedAnswer: Int?){
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ScoreQuestionsCardQuestionText(questionText = question.tekst)

            val tacniOdgovori = answers
            tacniOdgovori.forEach { answer ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(
                            color =  if (selectedAnswer == answer.idOdogovor) Color(0xFF388E3C).copy(alpha = 0.8f)
                            else Color(0xFFB71C1C).copy(alpha = 0.8f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = answer.tekstOdgovora + " " + if (selectedAnswer == answer.idOdogovor)  "✅" else "❌",
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

suspend fun calculateScore(
    questions: List<PitanjeR>,
    selectedAnswers: Map<Int, Int?>,
    realmViewModel: RealmViewModel
): Int {
    var score = 0

    for (question in questions) {
        val odgovori = realmViewModel.getAllOdgovorForPitanje(question) ?: emptyList()
        val selectedAnswerId = selectedAnswers[question.idPitanje]

        if (selectedAnswerId != null) {
            val selectedAnswer = odgovori.find { it.idOdogovor == selectedAnswerId }
            if (selectedAnswer != null && selectedAnswer.tacan) {
                score += selectedAnswer.bodovi
            } else {
                val netacan = odgovori.find { it.idOdogovor == selectedAnswerId }
                score -= netacan?.bodovi ?: 1
            }
        }
    }

    return score
}