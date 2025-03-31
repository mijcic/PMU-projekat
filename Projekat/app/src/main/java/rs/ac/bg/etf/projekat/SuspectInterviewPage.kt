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
fun SuspectInterviewPage(navController: NavController, myViewModel: MyViewModel, title: String) {
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
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF233331))
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
                    painter = painterResource(id = R.drawable.suspect_int),
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

@Composable
fun CategoryMenu(questionsData: Map<String, List<QuestionDetail>>, onCategorySelected: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(5.dp)
    ) {
        items(questionsData.keys.toList()) { category ->
            CategoryButton(category = category, onClick = { onCategorySelected(category) })
        }
    }
}

@Composable
fun CategoryButton(category: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)  // Adjusted padding
            .clip(RoundedCornerShape(20.dp))  // Rounded corners for a smoother, modern look
            .shadow(4.dp, RoundedCornerShape(20.dp)), // Adding subtle shadow to make it pop
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D3D29)), // Darker green
        interactionSource = interactionSource,
    ) {
        Text(
            text = category,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.White, // White text for contrast
                fontSize = 18.sp // Slightly larger text for a better appearance
            )
        )
    }
}

@Composable
fun QuestionList(questions: List<QuestionDetail>, onQuestionSelected: (QuestionDetail) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(questions) { question ->
            QuestionButton(question = question, onClick = { onQuestionSelected(question) })
        }
    }
}


@Composable
fun QuestionButton(question: QuestionDetail, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .shadow(4.dp, RoundedCornerShape(14.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D8C6F)),
        interactionSource = interactionSource,
    ) {
        Text(
            text = question.tekst,  // Display question text
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
            )
        )
    }
}


@Composable
fun ResponseSection(response: String, selectedQuestionDetail: QuestionDetail?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Main response
                Text(
                    text = "Answer: ${selectedQuestionDetail?.odgovor ?: "No answer available"}",
                    style = TextStyle(fontSize = 18.sp, color = Color.Black)
                )
                Spacer(modifier = Modifier.height(8.dp)) // Spacer between response and comment

                // Detective's comment
                Text(
                    text = "Detective's Comment: ${selectedQuestionDetail?.komentar ?: "No comment available"}",
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray, fontStyle = FontStyle.Italic)
                )
            }
        }
    }
}


@Composable
fun NavigationButtons(onReset: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { /* Implement next step logic here */ },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp)) // Rounded corners
                .shadow(4.dp, RoundedCornerShape(20.dp)), // Added shadow
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A9A6E)) // Next step green color
        ) {
            Text("Continue investigation", color = Color.White)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { onReset() },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp)) // Rounded corners
                .shadow(4.dp, RoundedCornerShape(20.dp)), // Added shadow
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD85D5D)) // Red background for reset
        ) {
            Text("Finish investigation", color = Color.White)
        }
    }
}

data class QuestionDetail(
    val tekst: String,
    val odgovor: String,
    val komentar: String
)
