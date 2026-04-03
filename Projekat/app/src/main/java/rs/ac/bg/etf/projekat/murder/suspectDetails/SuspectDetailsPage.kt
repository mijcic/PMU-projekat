package rs.ac.bg.etf.projekat.murder.suspectDetails

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.Background

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SuspectDetailsPage(
    idOsoba: Int, image: Int, title: String,
    onNavigateToInterview: (String) -> Unit,
    realmViewModel: RealmViewModel = hiltViewModel()
) {
    var motiveAlibiStatus by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        motiveAlibiStatus = realmViewModel.getMotiveAlibiStatus(idOsoba) ?: emptyList()
    }

    val tableData = listOf(
        listOf("Motive", motiveAlibiStatus.getOrNull(0).takeUnless { it.isNullOrBlank() } ?: "?"),
        listOf("Alibi", motiveAlibiStatus.getOrNull(1).takeUnless { it.isNullOrBlank() } ?: "?"),
        listOf("Status", motiveAlibiStatus.getOrNull(2).takeUnless { it.isNullOrBlank() } ?: "?")
    )

    Surface(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.fillMaxSize()) {

            Background(
                image = R.drawable.suspects_details_background,
                desc = "Background",
                modifier = Modifier.fillMaxSize(),
                alpha = 0.6f
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.92f)
                        .shadow(12.dp, RoundedCornerShape(24.dp), clip = true)
                        .background(colorResource(id = R.color.light_gray))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        SuspectInfoFun(
                            image = image,
                            title = title,
                            tableData = tableData
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        InterrogateButton(
                            text = "Interrogate the Suspect",
                            onDestinationSuspectsInterviewPage = {
                                onNavigateToInterview(title)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}