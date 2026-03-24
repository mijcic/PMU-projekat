package rs.ac.bg.etf.projekat.mysteriousSymptoms.medicalStatement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import rs.ac.bg.etf.projekat.data.MyViewModel

@Composable
fun MedicalStatementPage(
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