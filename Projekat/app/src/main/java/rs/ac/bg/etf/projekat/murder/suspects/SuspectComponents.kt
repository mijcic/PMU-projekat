package rs.ac.bg.etf.projekat.murder.suspects

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import rs.ac.bg.etf.projekat.R

@Composable
fun SuspectBackground(){
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.suspects_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
    }
}

@Composable
fun SuspectHeader(paddingStart: Dp){
    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.height(16.dp))
    }
    Column(
        modifier = Modifier.padding(start = paddingStart),
    ) {
        Text(text = "Suspects", color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily(
                    Font(R.font.special_elite)
                ),
                fontSize = 26.sp,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )
        )
    }
}