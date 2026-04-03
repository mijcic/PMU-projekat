package rs.ac.bg.etf.projekat.murder.witnessInterview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.Background
import rs.ac.bg.etf.projekat.murder.QuestionDetail
import rs.ac.bg.etf.projekat.murder.suspectInterview.QuestionList
import rs.ac.bg.etf.projekat.murder.suspectInterview.ResponseSection
import rs.ac.bg.etf.projekat.murder.suspectInterview.SuspectInfo

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WitnessesInterviewPage(
    myViewModel: MyViewModel,
    title: String,
    onDestinationWitnessesPage: () -> Unit
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

                Background(
                    image = R.drawable.interview_background,
                    desc = "Suspect Interview Background",
                    modifier = Modifier.matchParentSize(),
                    alpha = 0.7f
                )

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
                                selectedQuestionDetail = it
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ) {
                        WitnessesFinishInvestigationButton(
                            onFinished = {
                                if(uiPitanjaZaSvedoka.questions.isNotEmpty()){
                                    val wId = uiPitanjaZaSvedoka.questions.first().svedokId

                                    wId?.let {
                                        myViewModel.selectIspitivanjeSvedokaZadatakViewModel(it)?.let { zadatak->
                                            myViewModel.updateWitnessTask(zadatak)
                                        }

                                    }
                                    onDestinationWitnessesPage()
                                }
                                else{
                                    myViewModel.updatePitanjaZaSvedokaPitanjaEmptyViewModel(title)
                                }

                            },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    )
}