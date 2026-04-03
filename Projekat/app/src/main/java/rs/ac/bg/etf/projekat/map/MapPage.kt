package rs.ac.bg.etf.projekat.map

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel

@Composable
fun MapPage(navController: NavController, myViewModel: MyViewModel) {
    var paddingStart by remember { mutableStateOf(0.dp) }
    val uiStateTasks by myViewModel.uiStateTasks.collectAsState()

    Log.d("Zadaci",uiStateTasks.tasks.toString())

    Box(modifier = Modifier.fillMaxSize()) {
        MapBackground()

        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            TasksHeader(paddingStart)

            MapPageTaskListWithQuestions(
                tasks = uiStateTasks.tasks,
                navController = navController
            )
        }

        MapBackButton(
            onBack = {navController.popBackStack()},
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}