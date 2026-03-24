package rs.ac.bg.etf.projekat.mysteriousSymptoms.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.projekat.R

@Composable
fun DetailItem(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("$label: ", color = Color.LightGray, fontWeight = FontWeight.Bold,
            style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
        )
        Text(value, color = Color.White,
            style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
        )
    }
}

@Composable
fun PatientBackground(){
    Image(
        painter = painterResource(id = R.drawable.patient),
        contentDescription = "Background Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)))
}