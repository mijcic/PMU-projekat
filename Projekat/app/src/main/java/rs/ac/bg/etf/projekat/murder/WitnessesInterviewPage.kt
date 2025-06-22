package rs.ac.bg.etf.projekat.murder

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WitnessesInterviewPage(navController: NavController, myViewModel: MyViewModel, title: String) {
    val uiPitanjaZaSvedoka by myViewModel.uiStatePitanjaZaSvedoka.collectAsState()

    val allQuestions = remember {
        uiPitanjaZaSvedoka.questions
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }

    fun nextQuestion() {
        if (currentQuestionIndex < allQuestions.size - 1) {
            currentQuestionIndex++
        }
        else {
            if (uiPitanjaZaSvedoka.questions.isEmpty() == false) {
                myViewModel.selectIspitivanjeSvedokaZadatakViewModel(uiPitanjaZaSvedoka.questions.first().svedokId)?.let {
                    myViewModel.updateWitnessTask(
                        it
                    )
                }
            }

            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    WitnessInfo(title = title)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF8A6018))
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Spacer(modifier = Modifier.height(50.dp))

                Image(
                    painter = painterResource(id = R.drawable.witness_int),
                    contentDescription = "Witness Interview",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(40.dp))

                IndexedQuestion(
                    allQuestions = allQuestions,
                    currentQuestionIndex = currentQuestionIndex
                )

                Spacer(modifier = Modifier.height(40.dp))

                NextOrFinishButton(
                    onClickFunction = { nextQuestion() },
                    allQuestions = allQuestions,
                    currentQuestionIndex = currentQuestionIndex
                )

                RestartButton(
                    onClickFunction = { currentQuestionIndex = 0 },
                    allQuestions = allQuestions,
                    currentQuestionIndex = currentQuestionIndex
                )
            }
        }
    )
}

@Composable
fun WitnessInfo(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title, color = Color.White, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Witness Interview", color = Color.White, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun IndexedQuestion(
    allQuestions: List<PitanjeIspitivanjeSvedokaR>,
    currentQuestionIndex: Int
) {
    if (allQuestions.isNotEmpty()) {
        Box(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Pitanje
                Text(
                    text = "Question: ${allQuestions[currentQuestionIndex].tekst ?: "No question available"}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Odgovor
                Text(
                    text = "Answer: ${allQuestions[currentQuestionIndex].odgovor ?: "No answer available"}",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

    }
}

@Composable
fun NextOrFinishButton(
    onClickFunction: () -> Unit,
    allQuestions: List<PitanjeIspitivanjeSvedokaR>,
    currentQuestionIndex: Int
) {
    Button(
        onClick = { onClickFunction() },
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(horizontal = 16.dp)
            .height(50.dp)
    ) {
        Text(
            text = if (currentQuestionIndex < allQuestions.size - 1) "Next Question" else "Finish Interview",
            color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun RestartButton(
    onClickFunction: () -> Unit,
    allQuestions: List<PitanjeIspitivanjeSvedokaR>,
    currentQuestionIndex: Int
) {
    if (currentQuestionIndex == allQuestions.size - 1) {
        Button(
            onClick = {
                onClickFunction()
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(horizontal = 16.dp)
                .height(50.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Restart Interview",
                color = Color.White,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}