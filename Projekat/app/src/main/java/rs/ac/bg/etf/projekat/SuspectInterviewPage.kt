package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.data.UiStatePitanjaZaOsumnjicenog
import rs.ac.bg.etf.projekat.data.selectIspitivanjeOsumnjicenogZadatak

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuspectInterviewPage(navController: NavController, myViewModel: MyViewModel, title: String,realmViewModel: RealmViewModel) {
    LaunchedEffect(Unit) {
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

    var selectedQuestionDetail by remember { mutableStateOf<QuestionDetail?>(null) }
    var suspectResponse by remember { mutableStateOf("Click on a question to get the answer.") }

    var selectedSection by remember { mutableStateOf(Section.GENERAL) }

    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val sectionToCategoryMap = mapOf(
        Section.GENERAL to "General Questions",
        Section.ALIBI to "Alibi Questions",
        Section.EVIDENCE to "Evidence Questions",
        Section.PASSING to "Passing Questions"
    )

    val currentQuestions = questionsData[sectionToCategoryMap[selectedSection]] ?: emptyList()

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
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0XFFA99367))
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.interview_background),
                    contentDescription = "Suspect Interview Background",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 0.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(90.dp))

                    Row(modifier = Modifier.fillMaxSize()) {
                        // Vertical tab bar
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 4.dp, top = 32.dp, bottom = 32.dp),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Section.values().forEach { section ->
                                Text(
                                    text = section.label.replace(" ", "\n"),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable { selectedSection = section },
                                    color = if (selectedSection == section) Color.White else Color.Gray,
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.special_elite)),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 16.dp, end = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 80.dp), // Rezerviši prostor za dugme
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                ) {
                                    if (currentQuestions.isEmpty()) {
                                        item {
                                            Text(
                                                text = "No questions available.",
                                                color = Color.White,
                                                fontSize = 17.sp,
                                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                                modifier = Modifier.padding(16.dp),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else {
                                        items(currentQuestions) { questionDetail ->
                                            FlashcardItem(
                                                questionDetail = questionDetail,
                                                isSelected = selectedQuestionDetail == questionDetail,
                                                onClick = {
                                                    selectedQuestionDetail = questionDetail
                                                    suspectResponse = "Answer to the question: ${questionDetail.odgovor}"
                                                },
                                                onReset = {
                                                    selectedQuestionDetail = null
                                                    suspectResponse = "Click on a question to get the answer."
                                                },
                                                myViewModel = myViewModel,
                                                realmViewModel = realmViewModel,
                                                uiPitanjaZaOsumnjicenog = uiPitanjaZaOsumnjicenog
                                            )
                                            Spacer(modifier = Modifier.height(30.dp))
                                        }
                                    }
                                }
                            }

                            // Dugme fiksirano na dnu
                            Button(
                                onClick = {
                                    Log.d("UPO", uiPitanjaZaOsumnjicenog.generalQuestions.firstOrNull()?.osumnjicenId.toString())
                                    selectIspitivanjeOsumnjicenogZadatak(uiPitanjaZaOsumnjicenog.generalQuestions.firstOrNull()?.osumnjicenId)?.let {
                                        myViewModel.updateSuspectTask(it)
                                    }
                                    selectedCategory = null
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 16.dp)
                                    .widthIn(min = 180.dp)
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(5.dp))
                                    .shadow(4.dp, RoundedCornerShape(5.dp)),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_purple)),
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

                    }

                    val currentQuestions = questionsData[sectionToCategoryMap[selectedSection]] ?: emptyList()

                    QuestionList(questions = currentQuestions) { questionDetail ->
                        selectedQuestionDetail = questionDetail
                        suspectResponse = "Answer to the question: ${questionDetail.odgovor}"
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    ResponseSection(response = suspectResponse, selectedQuestionDetail = selectedQuestionDetail)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    )
}

// DODATO
@Composable
fun FlashcardItem(
    questionDetail: QuestionDetail,
    isSelected: Boolean,
    onClick: () -> Unit,
    onReset: () -> Unit,
    myViewModel: MyViewModel,
    realmViewModel: RealmViewModel,
    uiPitanjaZaOsumnjicenog: UiStatePitanjaZaOsumnjicenog
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        // 0xFFC8E6C9
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFFFFFE0) else colorResource(id = R.color.light_gray)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = questionDetail.tekst,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite))),
                textAlign = TextAlign.Center
            )

            if (isSelected) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Answer: ${questionDetail.odgovor}",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Detective's Comment: ${questionDetail.komentar}",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontStyle = FontStyle.Italic,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
                )
                Spacer(modifier = Modifier.height(12.dp))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap to reveal answer",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite))),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
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
                    style = TextStyle(fontSize = 18.sp, color = Color.Black, fontStyle = FontStyle.Italic)
                )
            }
        }
    }
}

data class QuestionDetail(
    val tekst: String,
    val odgovor: String,
    val komentar: String
)

enum class Section(val label: String) {
    GENERAL("GENERAL QUESTIONS"),
    ALIBI("ALIBI QUESTIONS"),
    EVIDENCE("EVIDENCE QUESTIONS"),
    PASSING("PASSING QUESTIONS")
}