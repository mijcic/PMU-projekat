package rs.ac.bg.etf.projekat.murder.witnesses

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.UiStateDataZlocin

@Composable
fun WitnessesPage(
    myViewModel: MyViewModel,
    onNavigateToDetails: (id: Int, image: Int, ime: String) -> Unit,
    onLoadPitanja: (String) -> Unit,
){
    var paddingStart by remember { mutableStateOf(0.dp) }
    val uiStateDataZlocin by myViewModel.uiStateZlocinData.collectAsState()

    LaunchedEffect(uiStateDataZlocin.witnesses) {
        myViewModel.getAllDataZlocin()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        WitnessBackground()

        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WitnessHeader(paddingStart = paddingStart)

            WitnessesList(
                uiStateDataZlocin = uiStateDataZlocin,
                onWitnessesClick = { id, ime ->
                    onLoadPitanja(ime)
                    onNavigateToDetails(id, R.drawable.witness, ime)
                }
            )
        }
    }
}

@Composable
fun WitnessesList(
    uiStateDataZlocin: UiStateDataZlocin,
    onWitnessesClick: (id: Int, ime: String) -> Unit
) {
    Column(modifier = Modifier) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                uiStateDataZlocin.witnesses.forEach { witness ->
                    Log.d("GEMINI WIT",witness.idSvedok.toString())
                    witness.osobaId?.let { osoba ->
                        WitnessCardWithImage(
                            image = R.drawable.witness,
                            title = osoba.ime,
                            onClick = {
                                onWitnessesClick(osoba.idOsoba, osoba.ime)
                            }
                        )
                    }
                }
            }
        }
    }
}