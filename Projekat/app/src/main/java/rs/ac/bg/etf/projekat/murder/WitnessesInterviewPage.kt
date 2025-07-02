package rs.ac.bg.etf.projekat.murder

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
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
import rs.ac.bg.etf.projekat.data.UiStatePitanjaZaOsumnjicenog
import rs.ac.bg.etf.projekat.data.UiStatePitanjaZaSvedoka
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WitnessesInterviewPage(
    navController: NavController,
    myViewModel: MyViewModel,
    title: String,
    realmViewModel: RealmViewModel
) {
    val uiPitanjaZaSvedoka by myViewModel.uiStatePitanjaZaSvedoka.collectAsState()
    var selectedQuestion by remember { mutableStateOf<QuestionDetail?>(null) }
    val questions = uiPitanjaZaSvedoka.questions.map {
        QuestionDetail(it.tekst, it.odgovor, "")
    }

    LaunchedEffect(Unit) {
        myViewModel.getPitanjaZaSvedoka(title)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { SuspectInfo(title = title) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0XFFA99367))
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {

                InterviewBackground(modifier = Modifier.matchParentSize())

                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(90.dp))

                    if (questions.isEmpty()) {
                        Text(
                            text = "No questions available.",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.special_elite))
                        )
                    } else {
                        QuestionList(
                            questions = questions,
                            onQuestionSelected = {
                                selectedQuestion = it
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        selectedQuestion?.let {
                            ResponseSection(
                                response = it.odgovor,
                                selectedQuestionDetail = it
                            )
                        }


                    }
                    // Dugme fiksirano na dnu
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        WitnessesFinishInvestigationButton(
                            uiPitanjaZaSvedoka = uiPitanjaZaSvedoka,
                            myViewModel = myViewModel,
                            onFinished = {
                            },
                            modifier = Modifier.align(Alignment.BottomCenter),
                            title = title
                        )
                    }
                    Log.d("SVE", "FINISH")

                }
            }
        }
    )
}

@Composable
fun WitnessesFinishInvestigationButton(
    uiPitanjaZaSvedoka: UiStatePitanjaZaSvedoka,
    myViewModel: MyViewModel,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    title: String
) {
    Log.d("SVE", "FINISHsss")
    Button(
        onClick = {
            if(uiPitanjaZaSvedoka.questions.isNotEmpty()){
                Log.d("SVEDOK","nije prazno")
                val wId = uiPitanjaZaSvedoka.questions.first().svedokId
                Log.d("SVEDOK",wId.toString())
                wId?.let {
                    myViewModel.selectIspitivanjeSvedokaZadatakViewModel(it)?.let { zadatak->
                        myViewModel.updateWitnessTask(zadatak)
                    }

                }
                onFinished()
            }
            else{
                myViewModel.updatePitanjaZaSvedokaPitanjaEmptyViewModel(title)
            }

        },
        modifier = modifier
            .widthIn(min = 180.dp)
            .wrapContentWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(5.dp))
            .shadow(4.dp, RoundedCornerShape(5.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(
            R.color.dark_purple
        )
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Finish",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
            )
            Text(
                text = "investigation",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
            )
        }
    }
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