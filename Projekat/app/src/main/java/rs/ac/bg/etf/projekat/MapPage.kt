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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.navigation.questionsPage

@Composable
fun MapPage(navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel) {
    var paddingStart by remember { mutableStateOf(0.dp) }
    val uiStateTasks by myViewModel.uiStateTasks.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    val allTasksCompleted = uiStateTasks.tasks.all { it.uradjen }

    Box(modifier = Modifier.fillMaxSize()) {
        MapBackground()

        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            TasksHeader(paddingStart)

            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                LazyColumn(modifier = Modifier.fillMaxSize()){
                    var firstFalseFound = false
                    var firstIndex = -1

                    itemsIndexed(uiStateTasks.tasks) { index, item ->
                        var isLocked = !item.uradjen
                        var isChecked = item.uradjen

                        if (item.uradjen == true) {
                            isLocked = false
                            isChecked = true
                        } else {
                            if (!firstFalseFound || firstIndex == index) {
                                isLocked = false
                                isChecked = false
                                firstFalseFound = true
                                firstIndex = index
                            } else {
                                isLocked = true
                                isChecked = false
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { if (!isLocked) { /* Akcija pri kliku na karticu */ } },
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isLocked) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Zaključano",
                                        modifier = Modifier.size(32.dp),
                                        tint = Color.Gray
                                    )
                                } else {
                                    Text(
                                        text = item.tekst,
                                        color = Color.Black,
                                        style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.special_elite)),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        ),
                                        modifier = Modifier.weight(2f).padding(end = 8.dp)
                                    )

                                    Text(
                                        text = item.korak,
                                        color = Color.Gray,
                                        style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.special_elite)),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            letterSpacing = 0.5.sp
                                        ),
                                        modifier = Modifier.weight(1.5f).padding(end = 8.dp)
                                    )
                                }

                                if (!isLocked) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { if (!isLocked) { /* Akcija kada se checkbox klikne */ } },
                                        modifier = Modifier.size(24.dp).padding(start = 8.dp),
                                    )
                                }
                            }
                        }
                    }

                    if (allTasksCompleted) {
                        item {
                            Divider(modifier = Modifier.padding(vertical = 16.dp))
                        }
                        item {
                            Button(
                                onClick = { showDialog = true },
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(text = "Questions")
                            }
                        }
                    }
                }

                if (showDialog) {
                    MapQuestionsDialog(
                        onConfirm ={ showDialog = false
                            navController.navigate(questionsPage.route) },
                        onDismiss ={ showDialog = false },
                        dismissButton ={ showDialog = false }
                    )
                }
            }
        }

        MapBackButton(
            onBack = {navController.popBackStack()},
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
fun MapBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.white_paper),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.5f)))
    }
}

@Composable
fun TasksHeader(paddingStart: Dp) {
    Column(modifier = Modifier) { Spacer(modifier = Modifier.height(16.dp)) }
    Column(modifier = Modifier.padding(start = paddingStart)) {
        Text(
            text = "Tasks",
            color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 26.sp,
                color = Color.Black
            )
        )
    }
}

@Composable
fun MapQuestionsDialog(onConfirm: () -> Unit, onDismiss: () -> Unit, dismissButton: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Are you sure?") },
        text = { Text(text = "Do you want to finish the case and go to the questions page?") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Yes") }
        },
        dismissButton = {
            TextButton(onClick = dismissButton) { Text("No") }
        }
    )
}


@Composable
fun MapBackButton(onBack: () -> Unit, modifier: Modifier) {
    IconButton(
        onClick = onBack,
        modifier = modifier
            .padding(top=40.dp,end=25.dp).size(18.dp)
            .background(Color(0xFF8B0000), shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
    }
}
