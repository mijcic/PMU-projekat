package rs.ac.bg.etf.projekat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
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
fun QuestionsPage(navController: NavController,myViewModel: MyViewModel) {
    val realmViewModel: RealmViewModel = hiltViewModel()
    var questions by remember { mutableStateOf<List<PitanjeR>>(emptyList()) }
    var selectedAnswers = remember { mutableStateOf<Map<Int, Int?>>(emptyMap()) }
    var totalScore = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) { questions = realmViewModel.getAllPitanje()!! }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundWithOverlay(imagePainter = R.drawable.library, alpha = 0.5F)

        Column(
            modifier = Modifier.fillMaxSize().padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            QuestionsHeaderText()

            QuestionsPageList(
                questions = questions,
                realmViewModel = realmViewModel,
                totalScore = totalScore,
                selectedAnswers = selectedAnswers,
                navController = navController,
                myViewModel = myViewModel
            )
        }
    }
}

@Composable
fun QuestionsHeaderText(){
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
}

@Composable
fun QuestionsPageList(
    questions: List<PitanjeR>,
    realmViewModel: RealmViewModel,
    totalScore: MutableState<Int>,
    selectedAnswers: MutableState<Map<Int, Int?>>,
    myViewModel: MyViewModel,
    navController: NavController
){
    var questionAnswersMap by remember { mutableStateOf<Map<PitanjeR, List<OdgovorR>>>(emptyMap()) }
    LazyColumn {
        items(questions) { question ->
            LaunchedEffect(question) {
                if (!questionAnswersMap.containsKey(question)) {
                    val odgovori = realmViewModel.getAllOdgovorForPitanje(question) ?: emptyList()
                    questionAnswersMap = questionAnswersMap + (question to odgovori)
                }
            }

            QuestionCardItem(
                question = question,
                answers = questionAnswersMap[question] ?: emptyList(),
                selectedAnswers = selectedAnswers,
                totalScore = totalScore
            )
        }

        item {
            QuestionsSubmitButton(onClick = {
                myViewModel.updateSelectedanswes(selectedAnswers)
                navController.navigate("destinationScoreQuestionsPage/${totalScore.value.toString()}")
            })
        }
    }
}

@Composable
fun QuestionCardItem(
    question: PitanjeR,
    answers: List<OdgovorR>,
    selectedAnswers: MutableState<Map<Int, Int?>>,
    totalScore: MutableState<Int>
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
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
                text = question.tekst,
                modifier = Modifier.padding(16.dp),
                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            answers.forEach { answer ->

                AnswerButton(
                    answer = answer,
                    isSelected = selectedAnswers.value[question.idPitanje] == answer.idOdogovor,
                    onClick = {
                        selectedAnswers.value = selectedAnswers.value + (question.idPitanje to answer.idOdogovor)
                        if (answer.tacan) {
                            totalScore.value += answer.bodovi
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AnswerButton(
    answer: OdgovorR,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 8.dp).clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) colorResource(id = R.color.dark_purple) else Color(0xFF5E554F)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) colorResource(id = R.color.dark_purple) else Color(0xFF5E554F),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = answer.tekstOdgovora,
            fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun QuestionsSubmitButton(onClick: () -> Unit){
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().wrapContentWidth()
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