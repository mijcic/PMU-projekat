package rs.ac.bg.etf.projekat.murder.suspectInterview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.murder.QuestionDetail

@Composable
fun SuspectInfo(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title, color = Color.White, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Investigation in progress ...", color = Color.White, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun ResponseSection(selectedQuestionDetail: QuestionDetail?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Answer: ${selectedQuestionDetail?.odgovor ?: "No answer available"}",
                    style = TextStyle(fontSize = 18.sp, color = Color.Black)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Detective's Comment: ${selectedQuestionDetail?.komentar ?: "No comment available"}",
                    style = TextStyle(fontSize = 18.sp, color = Color.Black, fontStyle = FontStyle.Italic)
                )
            }
        }
    }
}