package rs.ac.bg.etf.projekat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.realm.kotlin.types.RealmInstant
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.WhatsAppPreviewItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppPage(navController: NavController) {
    val realmViewModel: RealmViewModel = hiltViewModel()
    var chats by remember { mutableStateOf<List<WhatsAppPreviewItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        val result = realmViewModel.getContactsLastWhatsappMessages()
        chats = result?.sortedByDescending { it.lastMessage?.datum } ?: emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "WhatsApp",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 22.sp
                )},
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = colorResource(id = R.color.whatsapp_green)
            )
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(chats) { chat ->
                val kontakt = chat.kontakt
                val poruka = chat.lastMessage
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                var ime = kontakt?.ime ?: kontakt?.broj ?: "No Caller ID"
                                var slika = kontakt?.slika ?: R.drawable.no_account
                                navController.navigate("destinationWhatsAppChatPage/${kontakt.idWhatsAppKontakt}/$ime/$slika")
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
                                painter = painterResource(id = kontakt.slika ?: R.drawable.no_account),
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier.weight(1f) // Ograničava širinu teksta
                        ) {
                            Text(
                                text = kontakt.ime,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = poruka?.tekst ?: "",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = realmInstantForWA(poruka?.datum),
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.Top)
                        )
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

data class Chat(val name: String, val message: String, val time: String, val profilePicture: Int)

fun realmInstantForWA(realmInstant: RealmInstant?): String {
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
            timeFormatter.format(zonedDateTime.toLocalTime())
        }
        yesterday -> "Yesterday"
        else -> {
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
            dateFormatter.format(localDate)
        }
    }
}