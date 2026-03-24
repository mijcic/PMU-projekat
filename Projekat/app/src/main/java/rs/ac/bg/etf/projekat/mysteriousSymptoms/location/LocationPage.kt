package rs.ac.bg.etf.projekat.mysteriousSymptoms.location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import rs.ac.bg.etf.projekat.data.MyViewModel

@Composable
fun LocationPage(
    viewModel: MyViewModel,
    onBack: () -> Unit
) {
    val uiStateData by viewModel.uiStateMysteriousSymptomsData.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        MapViewContainer(
            locations = uiStateData.locations,
            modifier = Modifier.fillMaxSize()
        )

        LocationOverlayHeader(
            Modifier.align(Alignment.TopStart),
            uiStateData.locations.size
        )

        LocationBackButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onBack = onBack
        )
    }
}