package rs.ac.bg.etf.projekat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.UiStateCntEvidence
import rs.ac.bg.etf.projekat.data.UiStateCntForensicEvidence
import rs.ac.bg.etf.projekat.data.UiStateEvidences
import rs.ac.bg.etf.projekat.data.UiStateForensicEvidences
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazZadatakR

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EvidencePage(navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel){

    val uiStateEvidences by myViewModel.uiStateEvidence.collectAsState()
    val uiStateForensicEvidences by myViewModel.uiStateForensicEvidence.collectAsState()

    LaunchedEffect(Unit) {
        myViewModel.getEvidences()
        myViewModel.getForensicEvidences()
    }

    val dokaziLista = uiStateEvidences.evidences
    val listaDokazZadataka = uiStateEvidences.evidencesTasks
    val listaForenzickihDokaza = uiStateForensicEvidences.forensicEvidences
    val listaForenzickiDokazZadataka = uiStateForensicEvidences.forensicEvidencesTasks

    val uiStateCntEvidence by myViewModel.uiStateCntEvidence.collectAsState()
    val uiStateCntForensicEvidence by myViewModel.uiStateCntForensicEvidence.collectAsState()

    val uiStateEvidence = UiStateEvidences(dokaziLista, listaDokazZadataka)
    val uiStateForensicEvidence = UiStateForensicEvidences(listaForenzickihDokaza, listaForenzickiDokazZadataka)

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().background(colorResource(R.color.dark_purple))
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                EvidenceTextColumn(
                    pagerState= pagerState,
                    modifier = Modifier.weight(1f),
                    {coroutineScope.launch { pagerState.animateScrollToPage(0) }}
                )

                BoxVerticalBar(modifier = Modifier.align(Alignment.Top))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                ) {
                    Text(
                        text = "Forensic Evidences",
                        color = if (pagerState.currentPage == 1) Color.White else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    if (pagerState.currentPage == 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    Color.White,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            EvidenceOrForensicEvidenceText(
                modifier = Modifier.align(Alignment.Start),
                pagerState = pagerState
            )

            if (pagerState.currentPage == 0) ImageRowWithCenterFocus(getimageResIdsEvidence())
            else ImageRowWithCenterFocus(getimageResIdsForensic())

            HorizontalPager(
                count = 2,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) { page ->
                when (page) {
                    0 -> EvidenceListSection(uiStateEvidence, uiStateCntEvidence, myViewModel)
                    1 -> ForensicEvidenceListSection(uiStateForensicEvidence, uiStateCntForensicEvidence, myViewModel)
                }
            }
        }
    }
}

@Composable
fun EvidenceListSection(
    uiState: UiStateEvidences,
    cntState: UiStateCntEvidence,
    myViewModel: MyViewModel
) {
    val showDialog = remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        itemsIndexed(uiState.evidences) { index, i ->
            val filteredTasks = uiState.evidencesTasks.filter { task ->
                task.dokazId?.idDokaz == i.idDokaz
            }
            if (index <= cntState.cnt) {
                CardEvidenceShow(showDialog, i)
                showDialog.value = EvidenceDialog(
                    showDialog, i, filteredTasks, myViewModel, cntState.cnt
                )
            } else {
                CardEvidenceLock(i)
            }
        }
    }
}

@Composable
fun ForensicEvidenceListSection(
    uiState: UiStateForensicEvidences,
    cntState: UiStateCntForensicEvidence,
    myViewModel: MyViewModel
) {
    val showDialog2 = remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        itemsIndexed(uiState.forensicEvidences) { index, i ->
            val filteredTasks = uiState.forensicEvidencesTasks.filter { task ->
                task.forenzickiDokazId?.idForenzickiDokaz == i.idForenzickiDokaz
            }
            if (index <= cntState.forensicCnt) {
                CardEvidenceShow(showDialog2, i)
                showDialog2.value = ForensicEvidenceDialog(
                    showDialog2, i, filteredTasks, myViewModel, cntState.forensicCnt
                )
            } else {
                CardEvidenceLock(i)
            }
        }
    }
}

@Composable
fun ImageRowWithCenterFocus(imageResIds: List<Int>) {
    require(imageResIds.size == 5) { "Potrebno je tačno 5 slika" }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val spacing = 8.dp

    val totalSpacing = spacing * 4
    val availableWidth = screenWidth - totalSpacing

    val smallWeight = 1f
    val mediumWeight = 1.5f
    val largeWeight = 2f
    val totalWeight = smallWeight * 2 + mediumWeight * 2 + largeWeight

    val smallSize = availableWidth * (smallWeight / totalWeight)
    val mediumSize = availableWidth * (mediumWeight / totalWeight)
    val largeSize = availableWidth * (largeWeight / totalWeight)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leva mala
        Image(
            painter = painterResource(id = imageResIds[0]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(smallSize)
                //.clip(RoundedCornerShape(8.dp))
        )

        // Leva srednja
        Image(
            painter = painterResource(id = imageResIds[1]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(mediumSize)
                //.clip(RoundedCornerShape(8.dp))
        )

        // Najveća u sredini
        Image(
            painter = painterResource(id = imageResIds[2]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(largeSize)
                //.clip(RoundedCornerShape(8.dp))
        )

        // Desna srednja
        Image(
            painter = painterResource(id = imageResIds[3]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(mediumSize)
                //.clip(RoundedCornerShape(8.dp))
        )

        // Desna mala
        Image(
            painter = painterResource(id = imageResIds[4]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(smallSize)
                //.clip(RoundedCornerShape(8.dp))
        )
    }
}

@Composable
fun <T> CardEvidenceShow(showDialog: MutableState<Boolean>, i: T) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8FF)),
            onClick = {
                showDialog.value = true
            }
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                when (i) {
                    is DokazR -> {
                        Text(
                            text = if (i.tipDokaza == "fizicki") "Physical Evidence" else "Digital Evidence",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = i.opis,
                            style = TextStyle(fontSize = 14.sp, color = Color.Black)
                        )
                    }

                    is ForenzickiDokazR -> {
                        Text(
                            text = when (i.tipForenzickiDokaz) {
                                "DNK" -> "DNK"
                                "otisak" -> "Otisak"
                                else -> "Dokument"
                            },
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = i.opis,
                            style = TextStyle(fontSize = 14.sp, color = Color.Black)
                        )
                    }

                    else -> {
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
    showDialog: MutableState<Boolean>,
    i: DokazR,
    dokazZadaci: List<DokazZadatakR>,
    myViewModel: MyViewModel,
    cnt: Int
): Boolean {

    if (!showDialog.value) return false

    val task = dokazZadaci.firstOrNull()
    val isTaskDone = task?.uradjen == true

    val titleText = when {
        task != null && !isTaskDone -> task.tekst
        task != null && isTaskDone -> "..."
        else -> "..."
    }

    val confirmText = when {
        task != null && !isTaskDone -> task.tekst
        task != null && isTaskDone -> task.tekst
        else -> "..."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable(enabled = true) { }
    ) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = titleText ?: "...") },
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
                        containerColor = Color.DarkGray,
                        disabledContainerColor = Color.DarkGray,
                        contentColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    onClick = {
                        if (!isTaskDone && task != null) {
                            myViewModel.updateEvidenceAndEvidenceTask(task)
                        }
                        showDialog.value = false
                        myViewModel.cntIncrement(cnt)
                    }
                ) {
                    Text(confirmText ?: "...", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonColors(
                        containerColor = Color.DarkGray,
                        disabledContainerColor = Color.DarkGray,
                        contentColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    onClick = { showDialog.value = false }
                ) {
                    Text("Cancel", color = Color.White)
                }
            },
            modifier = Modifier.align(Alignment.Center)
        )
    }

    return showDialog.value
}


@Composable
fun ForensicEvidenceDialog(
    showDialog: MutableState<Boolean>,
    i: ForenzickiDokazR,
    dokazZadaci: List<ForenzickiDokazZadatakR>,
    myViewModel: MyViewModel,
    cnt: Int
): Boolean {

    if (!showDialog.value) return false

    val task = dokazZadaci.firstOrNull()
    val titleText = task?.tekst ?: "..."
    val confirmText = task?.tekst ?: "..."

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable(enabled = true) { }
    ) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = titleText) },
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
                        containerColor = Color.DarkGray,
                        disabledContainerColor = Color.DarkGray,
                        contentColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    onClick = {
                        task?.let {
                            if (!it.uradjen) {
                                myViewModel.updateForensicEvidenceAndForensicEvidenceTask(it)
                            }
                        }
                        showDialog.value = false
                        myViewModel.cntForensicIncrement(cnt)
                    }
                ) {
                    Text(confirmText, color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonColors(
                        containerColor = Color.DarkGray,
                        disabledContainerColor = Color.DarkGray,
                        contentColor = Color.White,
                        disabledContentColor = Color.White
                    ),
                    onClick = { showDialog.value = false }
                ) {
                    Text("Cancel", color = Color.White)
                }
            },
            modifier = Modifier.align(Alignment.Center)
        )
    }

    return showDialog.value
}


@Composable
fun BoxVerticalBar(modifier: Modifier){
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxHeight()
            .padding(top = 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "|",
            color = Color.White,
            fontSize = 34.sp
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EvidenceOrForensicEvidenceText(modifier: Modifier,pagerState: PagerState){
    Spacer(modifier = Modifier.height(30.dp))
    Text(
        text = if (pagerState.currentPage == 0) "Evidences" else "Forensic Evidences",
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(top = 16.dp, bottom = 8.dp, start = 24.dp)
            .fillMaxWidth(),
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.special_elite))
        ),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(30.dp))
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EvidenceTextColumn(pagerState: PagerState, modifier: Modifier, onClick:() -> Unit){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = "Evidences",
            color = if (pagerState.currentPage == 0) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold
        )
        if (pagerState.currentPage == 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.White, shape = CircleShape)
            )
        }
    }
}

fun getimageResIdsEvidence(): List<Int>{
    return listOf(
        R.drawable.evidence1, R.drawable.evidence10, R.drawable.evidence7,
        R.drawable.evidence8, R.drawable.evidence9
    )
}

fun getimageResIdsForensic(): List<Int>{
    return listOf(
        R.drawable.evidence4, R.drawable.evidence2, R.drawable.evidence6,
        R.drawable.evidence3, R.drawable.evidence5
    )
}