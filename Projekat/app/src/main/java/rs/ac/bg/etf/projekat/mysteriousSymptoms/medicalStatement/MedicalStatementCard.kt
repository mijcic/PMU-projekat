package rs.ac.bg.etf.projekat.mysteriousSymptoms.medicalStatement

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R


@Composable
fun MedicalStatementCard(personName: String?, statementText: String?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = RoundedCornerShape(24.dp)
                clip = true
            }
            .background(Color(0xFF1E1E1E))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            MedicalStatementCardTitle(personName = personName)

            MedicalStatementCardContent(statementText = statementText)
        }
    }
}

@Composable
fun MedicalStatementCardTitle(personName: String?) {
    Text(
        text = "Statement of ${personName ?: "Unknown person"}",
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite))),
        modifier = Modifier.padding(bottom = 16.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun MedicalStatementCardContent(statementText: String?) {
    Text(
        text = statementText ?: "Nema dostupne izjave.",
        fontSize = 18.sp,
        color = Color.White,
        lineHeight = 24.sp,
        style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite))),
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}