package rs.ac.bg.etf.projekat.murder

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import rs.ac.bg.etf.projekat.R

@Composable
fun InterviewBackground(modifier: Modifier){
    Image(
        painter = painterResource(id = R.drawable.interview_background),
        contentDescription = "Suspect Interview Background",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
    )
}