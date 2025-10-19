package rs.ac.bg.etf.projekat.mysteriousSymptoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel

@Composable
fun MedicalStatementPage(
    navController: NavController,
    realmViewModel: RealmViewModel,
    myViewModel: MyViewModel
) {
    val uiStateDataMysteriousSymptoms by myViewModel.uiStateMysteriousSymptomsData.collectAsState()

    MedicalStatementScreen(
        personName = uiStateDataMysteriousSymptoms.statement?.osobaId?.ime,
        statementText = uiStateDataMysteriousSymptoms.statement?.izjava
    )
}

@Composable
fun MedicalStatementScreen(personName: String?, statementText: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        MedicalStatementBackgroundImage()
        MedicalStatementCard(
            personName = personName,
            statementText = statementText,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun MedicalStatementBackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.hospital_room2),
        contentDescription = "Background Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

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