package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ResourceAsColor")
@Composable
fun MainScreen1(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().background(colorResource(id = R.color.gray_1)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Whodunit?",
            fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
            fontSize = 35.sp,
            color = colorResource(id = R.color.white)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LinearDeterminateIndicator(navController)
    }
}

@Composable
fun LinearDeterminateIndicator(navController: NavController) {
    var currentProgress by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            loadProgress { progress ->
                currentProgress = progress
                if (progress >= 0.99f) {
                    navController.navigate("destinationMainScreen2")
                }
            }
        }
    }

    LinearProgressIndicator(
        progress = {
            currentProgress
        },
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(30.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, colorResource(id = R.color.white), RoundedCornerShape(16.dp)),
        color = colorResource(id = R.color.dark_purple)
    )
}

suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    var progress = 0f
    while (progress <= 1f) {
        updateProgress(progress)
        progress += 0.02f
        delay(100)
    }
}