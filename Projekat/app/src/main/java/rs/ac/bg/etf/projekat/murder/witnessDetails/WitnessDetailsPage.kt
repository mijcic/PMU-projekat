package rs.ac.bg.etf.projekat.murder.witnessDetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.murder.DetailsBackground
import rs.ac.bg.etf.projekat.murder.suspectDetails.oneRowAboutSuspect

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun WitnessDetailsPage(
    idOsoba:Int, image: Int,
    title: String,
    onClick: () -> Unit,
    realmViewModel: RealmViewModel
) {
    var motiveAlibiStatus by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        motiveAlibiStatus = realmViewModel.getDetailsWitnessStatus(idOsoba) ?: emptyList()
    }

    val tableData = listOf(
        listOf("Zanimanje", motiveAlibiStatus.getOrNull(0).takeUnless { it.isNullOrBlank() } ?: "?"),
        listOf("Status", motiveAlibiStatus.getOrNull(1).takeUnless { it.isNullOrBlank() } ?: "?")
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {

            DetailsBackground()

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
                        modifier = Modifier.wrapContentHeight().fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        WitnessInfoCard(
                            image = image,
                            title = title,
                            tableData = tableData
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        InterrogateWitnessButton(
                            text="Interrogate the Witness",
                            onClick = onClick
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WitnessInfoCard(image: Int, title: String, tableData: List<List<String>>) {
    Text(
        text = "Witness Info",
        color = Color.Black,
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.special_elite)),
            fontSize = 18.sp
        )
    )

    Spacer(modifier = Modifier.height(20.dp))

    Image(
        painter = painterResource(id = image),
        contentDescription = "Witness Image",
        modifier = Modifier
            .size(130.dp)
            .clip(CircleShape)
            .border(1.5.dp, Color.Black, CircleShape)
            .shadow(8.dp, CircleShape)
    )

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = title,
        color = Color.Black,
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.special_elite)),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
    )

    Spacer(modifier = Modifier.height(25.dp))

    if (tableData.isNotEmpty()) {
        oneRowAboutSuspect("Occupation", tableData[0][1])
        Spacer(modifier = Modifier.height(5.dp))
        Divider(color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(5.dp))
        oneRowAboutSuspect("Status", tableData[1][1])
        Spacer(modifier = Modifier.height(5.dp))
    }
}