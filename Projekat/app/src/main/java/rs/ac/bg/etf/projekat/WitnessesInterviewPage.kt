package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import kotlinx.coroutines.delay
import rs.ac.bg.etf.projekat.data.MyViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WitnessesInterviewPage(navController: NavController, myViewModel: MyViewModel, title: String) {
    LaunchedEffect(title) {
        myViewModel.getPitanjaZaOsumnjicenog(title)
    }

    val uiPitanjaZaOsumnjicenog by myViewModel.uiStatePitanjaZaOsumnjicenog.collectAsState()

    val questionsData = remember {
        mapOf(
            "General Questions" to (uiPitanjaZaOsumnjicenog.generalQuestions.map { QuestionDetail(it.tekst, it.odgovor, it.komentar) } ?: listOf()),
            "Alibi Questions" to (uiPitanjaZaOsumnjicenog.alibiQuestions.map { QuestionDetail(it.tekst, it.odgovor, it.komentar) } ?: listOf()),
            "Evidence Questions" to (uiPitanjaZaOsumnjicenog.evidenceQuestions .map { QuestionDetail(it.tekst, it.odgovor, it.komentar) } ?: listOf()),
            "Passing Questions" to (uiPitanjaZaOsumnjicenog.passingQuestions.map { QuestionDetail(it.tekst, it.odgovor, it.komentar) } ?: listOf()),
        )
    }

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedQuestionDetail by remember { mutableStateOf<QuestionDetail?>(null) }
    var suspectResponse by remember { mutableStateOf("Click on a question to get the answer.") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "$title", color = Color.White, style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Investigation in progress ...", color = Color.White, style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor =  Color(0xFF8A6018))
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
                    contentDescription = "Suspect Interview",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                if (selectedCategory == null) {
                    CategoryMenu(questionsData = questionsData) { category ->
                        selectedCategory = category
                    }
                } else {
                    QuestionList(questions = questionsData[selectedCategory] ?: listOf()) { questionDetail ->
                        selectedQuestionDetail = questionDetail
                        suspectResponse = "Answer to the question: ${questionDetail.odgovor}"
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    // Add response and detective's comment
                    ResponseSection(response = suspectResponse, selectedQuestionDetail = selectedQuestionDetail)
                    Spacer(modifier = Modifier.height(20.dp))
                    NavigationButtons {
                        selectedCategory = null
                    }
                }
            }
        }
    )
}
