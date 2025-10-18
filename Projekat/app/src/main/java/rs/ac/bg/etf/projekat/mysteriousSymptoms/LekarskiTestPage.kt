package rs.ac.bg.etf.projekat.mysteriousSymptoms

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.realm.kotlin.types.RealmInstant
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel.LekarskiTestRezultat
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LekarskiTestPage(myViewModel: MyViewModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val realmViewModel: RealmViewModel = hiltViewModel()
    var test by remember { mutableStateOf<LekarskiTestRezultat?>(null) }
    val uiStateDataMysteriousSymptoms by myViewModel.uiStateMysteriousSymptomsData.collectAsState()

    LaunchedEffect(Unit) {
        test = realmViewModel.getLastLekarskiTest()
    }

    Surface(
        modifier = Modifier.fillMaxSize().padding(top=22.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.medical_test_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = (screenWidth / 9).dp)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Test Results",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.special_elite)
                    )
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(350.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = colorResource(R.color.light_gray)),
                contentAlignment = Alignment.Center
            ) {
                val datum: RealmInstant? = uiStateDataMysteriousSymptoms.patient?.zrtvaId?.osobaId?.datum
                val formattedDateDatum: String = datum?.let {
                    val date = Date(it.epochSeconds)
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    dateFormat.format(date)
                } ?: "Nepoznato"

                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (test != null) {
                        Spacer(modifier = Modifier.height(20.dp))
                        uiStateDataMysteriousSymptoms.tests?.izvestaj?.let {
                            Text(
                                text = it,
                                color = Color.Black,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

fun realmInstantToDateBirth(realmInstant: RealmInstant?): String {
    if (realmInstant == null) return ""

    val instant = Instant.ofEpochSecond(
        realmInstant.epochSeconds,
        realmInstant.nanosecondsOfSecond.toLong()
    )

    val zoneId = ZoneId.systemDefault()
    val localDate = instant.atZone(zoneId).toLocalDate()

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return formatter.format(localDate) + "."
}