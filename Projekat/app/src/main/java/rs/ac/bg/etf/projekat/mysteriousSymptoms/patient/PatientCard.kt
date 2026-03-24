package rs.ac.bg.etf.projekat.mysteriousSymptoms.patient

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R

@Composable
fun PatientInfoCard(icon: String, title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF342348))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 22.sp, modifier = Modifier.padding(end = 12.dp),
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
            )
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
                )
                Text(description, color = Color.LightGray, fontSize = 14.sp,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
                )
            }
        }
    }
}