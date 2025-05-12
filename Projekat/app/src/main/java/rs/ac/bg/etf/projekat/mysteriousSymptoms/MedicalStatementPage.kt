package rs.ac.bg.etf.projekat.mysteriousSymptoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import rs.ac.bg.etf.projekat.data.RealmViewModel

@Composable
fun MedicalStatementPage(navController: NavController, realmViewModel: RealmViewModel,myViewModel: MyViewModel) {
    val uiStateDataMysteriousSymptoms by myViewModel.uiStateMysteriousSymptomsData.collectAsState()


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image with a blur effect to improve readability
        Image(
            painter = painterResource(id = R.drawable.hospital_statement),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Semi-transparent overlay for better text visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))  // This will darken the background
        )

        // Content Column with padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centers the content vertically
        ) {
            // Title - the word "Izjava"
            Text(
                text = "Izjava ${uiStateDataMysteriousSymptoms.statement?.osobaId?.ime}",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)

            )

            // Statement Text - the actual content of the statement
            Text(
                text = uiStateDataMysteriousSymptoms.statement?.izjava?: "Nema dostupne izjave.",
                color = Color.White,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)

            )

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}
