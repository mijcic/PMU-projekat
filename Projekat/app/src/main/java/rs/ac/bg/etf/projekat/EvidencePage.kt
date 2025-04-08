package rs.ac.bg.etf.projekat


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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazZadatakR

@Composable
fun EvidencePage(navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel){

    LaunchedEffect(Unit) {
       // realmViewModel.insertDataForMurder()
       // myViewModel.getAllDataZlocin()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        var textWidth by remember { mutableStateOf(0f) }
        var paddingStart by remember { mutableStateOf(0.dp) }
        val uiStateEvidence by myViewModel.uiStateEvidence.collectAsState()
        val uiStateForensicEvidence by myViewModel.uiStateForensicEvidence.collectAsState()

        val uiStateCntEvidence by myViewModel.uiStateCntEvidence.collectAsState()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.evidence),
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
            Column(modifier = Modifier) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier.padding(start = paddingStart),
            ) {
                Text(text = "Evidences", color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        ),
                        fontSize = 26.sp,
                        color = Color.Black
                    )
                )
            }

            val showDialog = remember { mutableStateOf(false) }
            val showDialog2 = remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
            ) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    itemsIndexed(uiStateEvidence.evidences) {
                        index,i->

                            if(index==0){
                                Text(text = "Evidences", color = Color.White,
                                    style = TextStyle(
                                        fontFamily = FontFamily(
                                            Font(R.font.special_elite)
                                        ),
                                        fontSize = 26.sp,
                                        color = Color.Black
                                    ),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            val filteredTasks = uiStateEvidence.evidencesTasks.filter { task ->
                                task.dokazId?.idDokaz == i.idDokaz
                            }
                            if(index<=uiStateCntEvidence.cnt) {
                                CardEvidenceShow(showDialog,i)
                                showDialog.value=EvidenceDialog(showDialog,i,filteredTasks,myViewModel,uiStateCntEvidence.cnt)

                            }
                            else {
                                CardEvidenceLock(i)
                            }
                    }


                    itemsIndexed(uiStateForensicEvidence.forensicEvidences) {
                            index,i->

                        if(index==0){

                            Text(text = "Forensic Evidences", color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(
                                        Font(R.font.special_elite)
                                    ),
                                    fontSize = 26.sp,
                                    color = Color.Black
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        val filteredTasks = uiStateForensicEvidence.forensicEvidencesTasks.filter { task ->
                            task.forenzickiDokazId?.idForenzickiDokaz == i.idForenzickiDokaz
                        }
                        if(index==0) {
                            CardEvidenceShow(showDialog2,i)
                            showDialog2.value=ForensicEvidenceDialog(showDialog2,i,filteredTasks,myViewModel)
                        }
                        else {
                            CardEvidenceLock(i)
                        }
                    }
                }
            }


            Column(
                modifier = Modifier.padding(start = paddingStart),
            ) {
                Text(text = "Forensic Evidences", color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        ),
                        fontSize = 26.sp,
                        color = Color.Black
                    )
                )
            }
        }
    }
}


@Composable
fun <T> CardEvidenceShow(showDialog: MutableState<Boolean>, i: T) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        onClick = {
            showDialog.value = true
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Determining the type of the evidence and handling accordingly
            when (i) {
                is DokazR -> { // If the type is DokazR
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Evidence Type",
                            tint = Color(0xFF1D72B8), // Blue for physical evidence
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (i.tipDokaza == "fizicki") "Physical Evidence" else  "Digital Evidence",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = i.opis, // Using 'opis' for DokazR
                        style = TextStyle(fontSize = 14.sp, color = Color.Black)
                    )
                }
                is ForenzickiDokazR -> { // If the type is ForenzickiDokazR
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Evidence Type",
                            tint = Color(0xFF1D72B8), // Blue for physical evidence
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (i.tipForenzickiDokaz == "DNK") "DNK" else if(i.tipForenzickiDokaz =="otisak") "otisak" else "dokument",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = i.opis, // Using 'opis' for ForenzickiDokazR
                        style = TextStyle(fontSize = 14.sp, color = Color.Black)
                    )
                }
                else -> { // Handle any other type or default case
                    Text(
                        text = "Unknown Evidence Type",
                        style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Status: In Progress",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Victim: Isabelle Moreau",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Black
                )
            )
        }
    }
}

@Composable
fun <T> CardEvidenceLock(i: T){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Evidence Pending",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Unlock evidence by progressing in the game.",
                    style = TextStyle(fontSize = 14.sp, color = Color.White)
                )
            }
        }
    }
}

@Composable
fun EvidenceDialog(
    showDialog: MutableState<Boolean>, i: DokazR, dokazZadaci: List<DokazZadatakR>,
    myViewModel: MyViewModel, cnt: Int
):Boolean {
    if (showDialog.value && !dokazZadaci.first().uradjen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable(enabled = true) { }
        ) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = {
                    showDialog.value = false
                },
                title = { Text(text = dokazZadaci.first().tekst.toString()) },
                text = {
                    Column {
                        Text("Only if you agree, please confirm.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Evidence Type: ${if (i.tipDokaza == "fizicki") "Physical Evidence" else "Digital Evidence"}")
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonColors(
                            containerColor = Color.DarkGray, disabledContainerColor = Color.DarkGray,
                            contentColor = Color.DarkGray,
                            disabledContentColor = Color.DarkGray
                        ),
                        onClick = {
                            myViewModel.updateEvidenceAndEvidenceTask(dokazZadaci.first())
                            showDialog.value = false
                            myViewModel.cntIncrement(cnt)
                        }
                    ) {
                        Text(dokazZadaci.first().tekst,color=Color.White)

                    }
                },
                dismissButton = {
                    Button(
                        colors = ButtonColors(
                            containerColor = Color.DarkGray, disabledContainerColor = Color.DarkGray,
                            contentColor = Color.DarkGray,
                            disabledContentColor = Color.DarkGray
                        ),
                        onClick = { showDialog.value = false }
                    ) {
                        Text("Cancel",color=Color.White)
                    }
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    return showDialog.value
}


@Composable
fun ForensicEvidenceDialog(showDialog: MutableState<Boolean>, i: ForenzickiDokazR, dokazZadaci: List<ForenzickiDokazZadatakR>,myViewModel: MyViewModel):Boolean {
    if (showDialog.value && !dokazZadaci.first().uradjen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable(enabled = true) { }
        ) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = {
                    showDialog.value = false
                },
                title = { Text(text = dokazZadaci.first().tekst.toString()) },
                text = {
                    Column {
                        Text("Only if you agree, please confirm.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Forensic Evidence Type: ${if (i.tipForenzickiDokaz == "DNK") "DNK" else "otisak"}")
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonColors(
                            containerColor = Color.DarkGray, disabledContainerColor = Color.DarkGray,
                            contentColor = Color.DarkGray,
                            disabledContentColor = Color.DarkGray
                        ),
                        onClick = {
                            //myViewModel.updateEvidenceAndEvidenceTask(dokazZadaci.first())
                            showDialog.value = false

                        }
                    ) {
                        Text(dokazZadaci.first().tekst,color=Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        colors = ButtonColors(
                            containerColor = Color.DarkGray, disabledContainerColor = Color.DarkGray,
                            contentColor = Color.DarkGray,
                            disabledContentColor = Color.DarkGray
                        ),
                        onClick = { showDialog.value = false }
                    ) {
                        Text("Cancel",color=Color.White)
                    }
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    return showDialog.value
}