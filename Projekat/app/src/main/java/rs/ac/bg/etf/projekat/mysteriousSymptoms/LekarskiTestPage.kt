package rs.ac.bg.etf.projekat.mysteriousSymptoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel.LekarskiTestRezultat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun LekarskiTestPage() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val realmViewModel: RealmViewModel = hiltViewModel()
    var test by remember { mutableStateOf<LekarskiTestRezultat?>(null) }

    LaunchedEffect(Unit) {
        test = realmViewModel.getLastLekarskiTest()!!
    }

    Surface(
        modifier = Modifier.fillMaxSize().background(Color(0xFF300000)).padding(top=22.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF300000))
                .padding(top = (screenWidth / 9).dp).padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rezultati testova",
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
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .aspectRatio(0.6f)
                    .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.medical_carton),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .fillMaxHeight()
                        .padding(top = 115.dp, bottom = 140.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (test != null) {
                        Text(
                            text = "Ime i prezime: ${test!!.ime}",
                            color = Color(0xFF000070),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Datum rodjenja: ${realmInstantToDateBirth(test!!.datum)}",
                            color = Color(0xFF000070),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Pol: ${test!!.pol}",
                            color = Color(0xFF000070),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = test!!.izvestaj,
                            color = Color(0xFF000070),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left,
                            lineHeight = 18.sp
                        )
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