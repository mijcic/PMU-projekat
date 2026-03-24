package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.CaseCard
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.navigation.destinationMissionPage

val specialEliteFont = FontFamily(Font(R.font.special_elite))

@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
@Composable
fun CardsPage(modifier: Modifier = Modifier, navController: NavController,
    myViewModel: MyViewModel, realmViewModel: RealmViewModel
){
    var paddingStart by remember { mutableStateOf(0.dp) }

    Box(modifier = Modifier.fillMaxSize()) {

        CardsBackground()

        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {

            DetectiveHeader(paddingStart = paddingStart)

            CardsPageCardList(
                cases = getCases(realmViewModel),
                navController = navController,
                myViewModel = myViewModel,
                realmViewModel = realmViewModel
            )
        }
    }
}

@Composable
fun CardsBackground(){
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cards_image),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.5f)))
    }
}

@SuppressLint("ResourceAsColor")
@Composable
fun CardWithImage(
    image: Int,
    title: String,
    text: String,
    navController: NavController,
    insertIntoDatabase: () -> Unit,
    titleMP: String,
    dateMP: String,
    placeMP: String,
    descMP: String,
    myViewModel: MyViewModel,
    realmViewModel: RealmViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isLoading) showDialog = false
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.detective_loupe_magnifying_glass_svgrepo_com),
                    contentDescription = "Detective Icon",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Start New Game?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Do you want to start a new investigation\nor continue the previous one?",
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isLoading) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isLoading = true
                        realmViewModel.clearDatabase()
                        myViewModel.clearCnt()
                        myViewModel.getGeminiData(
                            realmViewModel,
                            onSuccess = {
                                isLoading = false
                                showDialog = false
                                //insertIntoDatabase()
                                navController.navigate(
                                    destinationMissionPage.route + "/" + image + "/" +
                                            "titleMP" + "/" + "dateMP" + "/" + "placeMP" + "/" + "descMP"
                                )

                                /*myViewModel.postGeminiData(
                                    onSuccess = { Log.d("GEMINI","GENERISANA PRICA")},
                                    onError = {navController.navigate("destinationErrorPage")}
                                )*/
                            },
                            onError = {
                                isLoading = false
                                showDialog = false
                                navController.navigate("destinationErrorPage")
                            }
                        )


                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_purple))
                ) {
                    Text("Start New", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        if (!isLoading) {
                            showDialog = false
                            navController.navigate(
                                destinationMissionPage.route + "/" + image + "/" +
                                        titleMP + "/" + dateMP + "/" + placeMP + "/" + descMP
                            )
                        }
                    },
                    enabled = !isLoading
                ) {
                    Text("Continue", color = Color.White)
                }
            },
            containerColor = Color(0xFF1A2B2D),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }

    Card(
        modifier = Modifier
            .padding(1.dp)
            .clickable { showDialog = true }
            .padding(bottom = 18.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(11.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2B2D)
        ),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun CardWithImage2(image: Int, title:String, text:String, navController: NavController, insertIntoDatabase: () -> Unit, titleMP: String, dateMP: String, placeMP: String, descMP: String,myViewModel: MyViewModel,realmViewModel: RealmViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { if (!isLoading) showDialog = false },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.detective_loupe_magnifying_glass_svgrepo_com),
                    contentDescription = "Detective Icon",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Start New Game?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Do you want to start a new investigation\nor continue the previous one?",
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isLoading) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isLoading = true
                        realmViewModel.clearDatabase()
                        myViewModel.clearCnt()
                        myViewModel.getGeminiDataMS(
                            realmViewModel,
                            onSuccess = {
                                isLoading = false
                                showDialog = false
                                navController.navigate(
                                    destinationMissionPage.route + "/" + image + "/" +
                                            "titleMP" + "/" + "dateMP" + "/" + "placeMP" + "/" + "descMP"
                                )

                                /*
                                myViewModel.postGeminiMSData(
                                    onSuccess = { Log.d("GEMINI","MS GENERISANA PRICA")},
                                    onError = {navController.navigate("destinationErrorPage")}
                                )*/
                            },
                            onError = {
                                isLoading = false
                                showDialog = false
                                navController.navigate("destinationErrorPage")
                            }
                        )
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_purple))
                ) {
                    Text("Start New", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        if (!isLoading) {
                            showDialog = false
                            navController.navigate(
                                destinationMissionPage.route + "/" + image + "/" +
                                        titleMP + "/" + dateMP + "/" + placeMP + "/" + descMP
                            )
                        }
                    },
                    enabled = !isLoading
                ) {
                    Text("Continue", color = Color.White)
                }
            },
            containerColor = Color(0xFF1A2B2D),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }

    Card(
        modifier = Modifier
            .padding(1.dp)
            .clickable{
                showDialog = true
//                realmViewModel.clearDatabase()
//                myViewModel.getGeminiDataMS(realmViewModel)
//
//                navController.navigate(
//                    destinationMissionPage.route + "/" + image + "/" +
//                            "PROBA TITLE" + "/" + "PROBA DATE" + "/" + "PROBA PLACE" + "/" + "PROBA DESCRIPTION"
//                )
            }
            .padding(bottom = 18.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(11.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2B2D)
        ),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        ),
                        color = Color.Black
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        ),
                        color = Color.Black
                    ),
                    color = Color.White
                )
            }
        }
    }
}

fun getCases(realmViewModel: RealmViewModel): List<CaseCard> {
    return listOf(
        CaseCard(
            imageRes = R.drawable.murder,
            title = "Murder \uD83D\uDD2A",
            description = "Dive into a chilling investigation to solve a brutal murder and uncover the truth behind the crime.",
            onClick = { /*...*/ },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.dissapear,
            title = "Disappearance \uD83D\uDCCC",
            description = "A thrilling mission where the detective seeks to uncover the mystery of a missing person, uncovering hidden secrets along the way.",
            onClick = { /*...*/ },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.bank_robbery,
            title = "Robbery \uD83D\uDCB0",
            description = "Focuses on solving a robbery case, whether it's a bank heist, " +
                    "a museum theft, or the stealing of valuable items.",
            onClick = { /*...*/ },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.kidnapping,
            title = "Kidnapping and Blackmail \uD83E\uDEE5",
            description = "Investigate the dark world of kidnappings and blackmail, " +
                    "where innocent lives are held ransom for secrets or money.",
            onClick = { /*...*/ },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.family_secrets,
            title = "Family Secrets \uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66",
            description = "The detective explores crimes rooted in family dynamics, unraveling " +
                    "secrets that tie blood relatives to criminal activity.",
            onClick = { /*...*/ },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.abuse,
            title = "Abuse \uD83D\uDC7A",
            description = "Investigating cases of abuse, be it physical, emotional, or " +
                    "psychological, to uncover the perpetrators and bring justice.",
            onClick = { /*...*/ },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.gang,
            title = "Gang Conflicts \uD83E\uDE78",
            description = "Delve into a dangerous world of " +
                    "gang wars and criminal " +
                    "organizations, solving cases of violence and turf battles.",
            onClick = { /*...*/ },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.corruption,
            title = "Corruption \uD83C\uDFDB\uFE0F",
            description = "Uncover the hidden faces of " +
                    "corruption in politics or corporations, revealing the " +
                    "extent of fraudulent activities " +
                    "and their consequences.",
            onClick = { /*...*/ },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.m_symptoms2,
            title = "Mysterious Symptoms ⚕\uFE0F ",
            description = "Investigate strange diseases or unusual deaths, connecting the " +
                    "dots between mysterious health " +
                    "conditions and criminal activity.",
            onClick = { },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.mafia,
            title = "Mafia ❌",
            description = "Explore the dangerous world of " +
                    "mafia organizations, " +
                    "investigating murders, extortion, drug trafficking, and other " +
                    "heinous crimes.",
            onClick = {  },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.crime_passion,
            title = "Crimes of Passion \uD83C\uDFAD",
            description = "Investigate intense emotional " +
                    "motives behind crimes of passion, " +
                    "such as murders driven by jealousy " +
                    "or violent love affairs.",
            onClick = { },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.identities,
            title = " False Identities \uD83E\uDEAA",
            description = "Solve cases involving the use of fake or stolen identities for illegal activities, " +
                    "uncovering the culprits behind them.",
            onClick = { },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        ),
        CaseCard(
            imageRes = R.drawable.sects,
            title = "Cults and Sects \uD83D\uDC80",
            description = "Uncover the sinister operations of dangerous cults or ideological " +
                    "sects, revealing manipulation, brainwashing, and murder.",
            onClick = { },
            titleMP = "",
            dateMP = "",
            placeMP = "",
            descMP = ""
        )
    )
}

@Composable
fun DetectiveHeader(paddingStart: Dp){
    Column(modifier = Modifier) { Spacer(modifier = Modifier.height(16.dp)) }

    Column(modifier = Modifier.padding(start = paddingStart),) {
        Text(text = "Detective,", color = Color.White,
            style = TextStyle(
                fontFamily = specialEliteFont,
                fontSize = 17.sp,
                color = Color.Black
            )
        )
    }
    Column(modifier = Modifier, verticalArrangement = Arrangement.Center) {
        Text("choose one case from the options provided.", color = Color.White,
            style = TextStyle(
                fontFamily = specialEliteFont,
                color = Color.Black,
                fontSize = 17.sp
            )
        )
    }
}

@Composable
fun CardsPageCardList(
    cases: List<CaseCard>,
    navController: NavController,
    myViewModel: MyViewModel,
    realmViewModel: RealmViewModel
) {
    Column(modifier = Modifier) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            items(getCases(realmViewModel).size) { index ->
                val case = getCases(realmViewModel)[index]

                CaseCardListItem(
                    case = case,
                    navController = navController,
                    myViewModel = myViewModel,
                    realmViewModel = realmViewModel
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun CaseCardListItem(
    case: CaseCard,
    navController: NavController,
    myViewModel: MyViewModel,
    realmViewModel: RealmViewModel
) {
    if(case.imageRes==R.drawable.m_symptoms2){
        CardWithImage2(
            image = case.imageRes,
            title = case.title,
            text = case.description,
            navController = navController,
            insertIntoDatabase = case.onClick,
            titleMP = case.titleMP,
            dateMP = case.dateMP,
            placeMP = case.placeMP,
            descMP = case.descMP,
            myViewModel = myViewModel,
            realmViewModel = realmViewModel
        )
    }
    else{
        CardWithImage(
            image = case.imageRes,
            title = case.title,
            text = case.description,
            navController = navController,
            insertIntoDatabase = case.onClick,
            titleMP = case.titleMP,
            dateMP = case.dateMP,
            placeMP = case.placeMP,
            descMP = case.descMP,
            myViewModel = myViewModel,
            realmViewModel = realmViewModel
        )
    }
}