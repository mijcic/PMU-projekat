package rs.ac.bg.etf.projekat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorPage() {
    Box(modifier = Modifier.fillMaxSize()) {
        ErrorBackgroundImage(imageRes = R.drawable.error_background)
        ErrorOverlay(overlayColor = Color.Black.copy(alpha = 0.5f))
        ErrorMessageContent(
            title = "E R R O R",
            messages = listOf("No data available for display", "Try again later"),
            textColor = Color(0xFFDC143C),
            fontFamily =  FontFamily(Font(R.font.special_elite))
        )
    }
}

@Composable
fun ErrorBackgroundImage(imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Background image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ErrorOverlay(overlayColor: Color) {
    Box(
        modifier = Modifier.fillMaxSize().background(overlayColor)
    )
}

@Composable
fun ErrorMessageContent(
    title: String,
    messages: List<String>,
    textColor: Color,
    fontFamily: FontFamily
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = textColor,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                style = TextStyle(fontFamily = fontFamily)
            )

            Spacer(modifier = Modifier.height(10.dp))

            messages.forEach {
                Text(
                    text = it,
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 3.dp),
                    style = TextStyle(fontFamily = fontFamily)
                )
            }
        }
    }
}