package rs.ac.bg.etf.projekat.murder.office

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R

@Composable
fun DescriptionForDetective(text: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 32.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer { alpha = 0.9f }
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF1A1A1A).copy(alpha = 0.8f))
                .padding(16.dp)
        ) {
            Text(
                text = text,
                color = Color(0xFFE0E0E0),
                fontSize = 19.sp,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            )
        }
    }
}