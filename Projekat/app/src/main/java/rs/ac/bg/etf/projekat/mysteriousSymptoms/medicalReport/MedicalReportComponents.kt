package rs.ac.bg.etf.projekat.mysteriousSymptoms.medicalReport

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun InfoRowPdf(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            "$label:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            style = TextStyle(fontFamily = FontFamily(
                Font(R.font.special_elite)
            ))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(value, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun SectionTitlePdf(title: String) {
    Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold,style = TextStyle(fontFamily = FontFamily(
        Font(R.font.special_elite)
    )))
}

@Composable
fun SectionTextPdf(text: String) {
    Text(
        text,
        color = Color.White,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = FontFamily.Serif,
        style = TextStyle(fontFamily = FontFamily(
            Font(R.font.special_elite)
        ))
    )
}

@Composable
fun BulletTextPdf(text: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text("•", color = Color.White, fontSize = 16.sp,style = TextStyle(fontFamily = FontFamily(
            Font(R.font.special_elite)
        )))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, color = Color.White, fontSize = 16.sp, fontFamily = FontFamily.Serif,style = TextStyle(fontFamily = FontFamily(
            Font(R.font.special_elite)
        )))
    }
}