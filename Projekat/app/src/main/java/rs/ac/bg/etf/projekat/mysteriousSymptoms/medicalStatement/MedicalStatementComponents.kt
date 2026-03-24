package rs.ac.bg.etf.projekat.mysteriousSymptoms.medicalStatement

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import rs.ac.bg.etf.projekat.R


@Composable
fun MedicalStatementBackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.hospital_room2),
        contentDescription = "Background Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}