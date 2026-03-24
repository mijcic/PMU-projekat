package rs.ac.bg.etf.projekat.mysteriousSymptoms.medicalTest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel.LekarskiTestRezultat

@Composable
fun LekarskiTestPage(
    myViewModel: MyViewModel,
    realmViewModel: RealmViewModel = hiltViewModel()
) {
    val uiStateData by myViewModel.uiStateMysteriousSymptomsData.collectAsState()
    var lastTest by remember { mutableStateOf<LekarskiTestRezultat?>(null) }

    LaunchedEffect(Unit) {
        lastTest = realmViewModel.getLastLekarskiTest()
    }

    MedicalTestBackground {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TEST RESULTS",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
            )

            Spacer(modifier = Modifier.height(30.dp))

            val report = uiStateData.tests?.izvestaj
            if (lastTest != null && report != null) {
                TestResultsCard(reportText = report)
            } else {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}