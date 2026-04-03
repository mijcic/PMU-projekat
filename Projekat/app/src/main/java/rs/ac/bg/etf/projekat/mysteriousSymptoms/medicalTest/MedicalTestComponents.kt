package rs.ac.bg.etf.projekat.mysteriousSymptoms.medicalTest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.Background
import rs.ac.bg.etf.projekat.R

@Composable
fun MedicalTestBackground(content: @Composable BoxScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Background(
            image = R.drawable.medical_test_background,
            desc = null,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.5f
        )

        content()
    }
}

@Composable
fun TestResultsCard(
    reportText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(280.dp)
            .height(350.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.light_gray))
            .padding(20.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = reportText,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp,
            style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite))) // Opciono za "retro" izgled
        )
    }
}