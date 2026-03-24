package rs.ac.bg.etf.projekat.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import rs.ac.bg.etf.projekat.R

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