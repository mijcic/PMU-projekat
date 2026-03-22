package rs.ac.bg.etf.projekat.murder.suspects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR

@Composable
fun SuspectsPage(
    onNavigateToDetails: (id: Int, image: Int, ime: String) -> Unit,
    onLoadPitanja: (String) -> Unit,
    myViewModel: MyViewModel
) {
    val uiStateDataZlocin by myViewModel.uiStateZlocinData.collectAsState()

    LaunchedEffect(Unit) {
        myViewModel.getAllDataZlocin()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SuspectBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SuspectHeader(paddingStart = 0.dp)

            SuspectsList(
                suspects = uiStateDataZlocin.suspects,
                onSuspectClick = { id, ime ->
                    onLoadPitanja(ime)
                    onNavigateToDetails(id, R.drawable.suspect, ime)
                }
            )
        }
    }
}

@Composable
fun SuspectsList(
    suspects: List<OsumnjicenR>,
    onSuspectClick: (id: Int, ime: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        items(suspects) { suspect ->
            suspect.osobaId?.let { osoba ->
                SuspectCardWithImage(
                    image = R.drawable.suspect,
                    title = osoba.ime,
                    onClick = {
                        onSuspectClick(osoba.idOsoba, osoba.ime)
                    }
                )
            }
        }
    }
}