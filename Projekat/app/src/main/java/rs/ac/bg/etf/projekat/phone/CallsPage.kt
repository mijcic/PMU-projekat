package rs.ac.bg.etf.projekat.phone

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.realm.kotlin.types.RealmInstant
import rs.ac.bg.etf.projekat.BottomNavigationBar
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.OneCallR
import rs.ac.bg.etf.projekat.navDestination
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallsPage(navController: NavController) {
    var selectedButton by remember { mutableStateOf(1) }
    val font = FontFamily.SansSerif

    val realmViewModel: RealmViewModel = hiltViewModel()
    var calls by remember { mutableStateOf<List<OneCallR>>(emptyList()) }

    LaunchedEffect(Unit) {
        calls = realmViewModel.getAllCalls()
            ?.sortedByDescending { call ->
                call.datum?.let {
                    Instant.ofEpochSecond(it.epochSeconds, it.nanosecondsOfSecond.toLong())
                }
            } ?: emptyList()
    }

    val filteredCalls = if (selectedButton == 1) calls else calls.filter { it.propusten == true }

    val destinationRecentCalls = navDestination(
        route = "destinationCallsPage",
        icon = R.drawable.clock_fill,
        label = "Recents"
    )

    val destinationContacts = navDestination(
        route = "destinationPhonebookPage",
        icon = R.drawable.person_circle,
        label = "Contacts"
    )

    val destinationKeypad = navDestination(
        route = "destinationKeypadPage",
        icon = R.drawable.grid_3x3_gap,
        label = "Keypad"
    )

    val destinations = listOf(
        destinationRecentCalls,
        destinationContacts,
        destinationKeypad
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, destinations = destinations)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 0.dp
                )
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .background(colorResource(R.color.light_gray), shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 3.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Button(
                        onClick = { selectedButton = 1 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedButton == 1) Color.White else colorResource(
                                R.color.light_gray
                            )
                        ),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .width(60.dp)
                            .height(25.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .then(
                                if (selectedButton == 1) {
                                    Modifier.shadow(
                                        elevation = 12.dp,
                                        shape = RoundedCornerShape(4.dp),
                                        clip = true
                                    )
                                } else Modifier
                            )
                    ) {
                        Text("All", fontSize = 12.sp, color = Color.Black, fontFamily = font)
                    }

                    Button(
                        onClick = { selectedButton = 2 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedButton == 2) Color.White else colorResource(
                                R.color.light_gray
                            )
                        ),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .width(60.dp)
                            .height(25.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .then(
                                if (selectedButton == 2) {
                                    Modifier.shadow(
                                        elevation = 12.dp,
                                        shape = RoundedCornerShape(4.dp),
                                        clip = true
                                    )
                                } else Modifier
                            )
                    ) {
                        Text("Missed", fontSize = 12.sp, color = Color.Black, fontFamily = font)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recents",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 16.dp),
                fontFamily = font
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                filteredCalls.forEach { call ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val name = call.kontakt?.ime ?: "No Caller ID"
                                val phoneNumber = call.kontakt?.broj ?: ""
                                val photo = call.kontakt?.slika ?: R.drawable.no_account

                                val encodedName = Uri.encode(name)

                                navController.navigate("destinationOneContactPage/$encodedName/$phoneNumber/$photo")
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = call.kontakt?.slika ?: R.drawable.no_account),
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                call.kontakt?.ime ?: call.kontakt?.broj ?: "No Caller ID",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = if (call.propusten == true) Color.Red else Color.Black,
                                fontFamily = font
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(
                                        id = if (call.dolazni == false) R.drawable.outgoing_call
                                        else R.drawable.ingoing_call
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.LightGray
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text("mobile", color = Color.Gray, fontSize = 14.sp, fontFamily = font)
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Text(realmInstantToTimeString(call.datum), color = Color.Gray, fontSize = 14.sp, fontFamily = font)
                    }

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

fun realmInstantToTimeString(realmInstant: RealmInstant?): String {
    if (realmInstant == null) return ""

    val instant = Instant.ofEpochSecond(
        realmInstant.epochSeconds,
        realmInstant.nanosecondsOfSecond.toLong()
    )

    val zoneId = ZoneId.systemDefault()
    val zonedDateTime = instant.atZone(zoneId)
    val localDate = zonedDateTime.toLocalDate()
    val today = LocalDate.now(zoneId)
    val yesterday = today.minusDays(1)

    return when (localDate) {
        today -> {
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            timeFormatter.format(zonedDateTime)
        }
        yesterday -> "Yesterday"
        else -> {
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            dateFormatter.format(localDate)
        }
    }
}

data class Call(val name: String, val missedOrNot: Int, val time: String, val picture: Int, val type: String)