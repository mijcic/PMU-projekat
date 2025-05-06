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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel

@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
@Composable
fun CardsPage(modifier: Modifier = Modifier, navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        var textWidth by remember { mutableStateOf(0f) }
        var paddingStart by remember { mutableStateOf(0.dp) }

        val crimeData = realmViewModel.uiStateCrimeData.collectAsState()
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            //realmViewModel.getTitleDatePlaceDescFromCrime()
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cards_image),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }


        Column(modifier = Modifier
            .align(Alignment.TopCenter).padding(top = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)

        {
            Column(
                modifier = Modifier
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier.padding(start = paddingStart),
            ) {
                Text(text = "Detective,", color = Color.White,
                    style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.special_elite)
                    ),
                        fontSize = 17.sp,
                    color = Color.Black
                ))
            }
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Text("choose one case from the options provided.", color = Color.White,
                style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.special_elite)
                    ),
                    color = Color.Black,
                            fontSize = 17.sp
                ))
        }


        Column(
            modifier = Modifier
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                item {
                    CardWithImage(
                        R.drawable.murder,
                        "Murder \uD83D\uDD2A",
                        "Dive into a chilling investigation " +
                                "to solve a brutal murder and " +
                                "uncover the truth behind the crime.",
                        navController,
                        {
                            //realmViewModel.insertDataForMurder()
                            //realmViewModel.callGetTitleDatePlaceDescFromCrime()

                        },
                        crimeData.value.title.toString(),
                        crimeData.value.date.toString(),
                        crimeData.value.place.toString(),
                        crimeData.value.description.toString(),myViewModel,realmViewModel
                    )

                    CardWithImage(
                        R.drawable.dissapear,
                        "Disappearance \uD83D\uDCCC",
                        "A thrilling mission where the detective seeks to uncover the " +
                                "mystery of a missing person, uncovering hidden secrets along " +
                                "the way.",
                        navController,
                        {myViewModel.getGeminiData(realmViewModel)},
                        "", "", "", "",myViewModel,realmViewModel
                    )


                    CardWithImage(
                        R.drawable.bank_robbery,
                        " Robbery \uD83D\uDCB0",
                        "Focuses on solving a robbery case, whether it's a bank heist, " +
                                "a museum theft, or the stealing of valuable items.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )

                    CardWithImage(
                        R.drawable.kidnapping,
                        "Kidnapping and Blackmail \uD83E\uDEE5",
                        "Investigate the dark world of kidnappings and blackmail, " +
                                "where innocent lives are held ransom for secrets or money.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )

                    CardWithImage(
                        R.drawable.family_secrets,
                        "Family Secrets \uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66",
                        "The detective explores crimes rooted in family dynamics, unraveling " +
                                "secrets that tie blood relatives to criminal activity.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )

                    CardWithImage(
                        R.drawable.abuse,
                        "Abuse \uD83D\uDC7A",
                        "Investigating cases of abuse, be it physical, emotional, or " +
                                "psychological, to uncover the perpetrators and bring justice.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )


                    CardWithImage(
                        R.drawable.gang,
                        "Gang Conflicts \uD83E\uDE78",
                        "Delve into a dangerous world of " +
                                "gang wars and criminal " +
                                "organizations, solving cases of violence and turf battles.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )

                    CardWithImage(
                        R.drawable.corruption,
                        "Corruption \uD83C\uDFDB\uFE0F",
                        "Uncover the hidden faces of " +
                                "corruption in politics or corporations, revealing the " +
                                "extent of fraudulent activities " +
                                "and their consequences.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )

                    CardWithImage2(
                        R.drawable.m_symptoms,
                        "Mysterious Symptoms ⚕\uFE0F ",
                        "Investigate strange diseases or unusual deaths, connecting the " +
                                "dots between mysterious health " +
                                "conditions and criminal activity.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )
                    CardWithImage(
                        R.drawable.mafia,
                        "Mafia ❌",
                        "Explore the dangerous world of " +
                                "mafia organizations, " +
                                "investigating murders, extortion, drug trafficking, and other " +
                                "heinous crimes.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )

                    CardWithImage(
                        R.drawable.crime_passion,
                        "Crimes of Passion \uD83C\uDFAD",
                        "Investigate intense emotional " +
                                "motives behind crimes of passion, " +
                                "such as murders driven by jealousy " +
                                "or violent love affairs.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )

                    CardWithImage(
                        R.drawable.identities,
                        " False Identities \uD83E\uDEAA",
                        "Solve cases involving the use of fake or stolen identities for illegal activities, " +
                                "uncovering the culprits behind them.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )
                    CardWithImage(
                        R.drawable.sects,
                        "Cults and Sects \uD83D\uDC80",
                        "Uncover the sinister operations of dangerous cults or ideological " +
                                "sects, revealing manipulation, brainwashing, and murder.",
                        navController,
                        {},
                        "", "", "", "",myViewModel,realmViewModel
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

            }
        }
        }

    }
}

@Composable
fun CardWithImage(image: Int, title:String, text:String, navController: NavController, insertIntoDatabase: () -> Unit, titleMP: String, dateMP: String, placeMP: String, descMP: String,myViewModel: MyViewModel,realmViewModel: RealmViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.detective_loupe_magnifying_glass_svgrepo_com),
                    contentDescription = "Detective Icon",
                    tint = Color(0xFF4CAF50), // zelena nijansa, možeš promeniti
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
                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        MainActivity.clearDatabase()
                        myViewModel.getGeminiData(realmViewModel)

                        insertIntoDatabase()
                        navController.navigate(
                            destinationMissionPage.route + "/" + image + "/" +
                                    "PROBA TITLE" + "/" + "PROBA DATE" + "/" + "PROBA PLACE" + "/" + "PROBA DESCRIPTION"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)) // crvena nijansa
                ) {
                    Text("Start New", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDialog = false
                        navController.navigate(
                            destinationMissionPage.route + "/" + image + "/" +
                                    "PROBA TITLE" + "/" + "PROBA DATE" + "/" + "PROBA PLACE" + "/" + "PROBA DESCRIPTION"
                        )
                    }
                ) {
                    Text("Continue")
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
                showDialog=true
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

@Composable
fun CardWithImage2(image: Int, title:String, text:String, navController: NavController, insertIntoDatabase: () -> Unit, titleMP: String, dateMP: String, placeMP: String, descMP: String,myViewModel: MyViewModel,realmViewModel: RealmViewModel) {

    Card(
        modifier = Modifier
            .padding(1.dp)
            .clickable{
                navController.navigate(
                    destinationInvestigationPage.route
                )
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