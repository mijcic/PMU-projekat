package rs.ac.bg.etf.projekat.phone

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.ObicnaPorukaR

@Composable
fun ChatPage(id: Int, name: String, photo: Int) {
    val realmViewModel: RealmViewModel = hiltViewModel()
    var messages by remember { mutableStateOf<List<ObicnaPorukaR>>(emptyList()) }

    LaunchedEffect(Unit) {
        messages = realmViewModel.getMessagesWithContact(id)!!.reversed()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(12.dp).padding(top = 30.dp)
    ) {
        ContactInfo(photo = photo, name = name)

        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        ChatScreen(
            messages = messages,
            modifier = Modifier.weight(1f).fillMaxWidth()
        )
        TextInsertation()
    }
}

@Composable
fun ContactInfo(photo: Int, name: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current

        val validPictureResId = remember(photo) {
            try {
                context.resources.getResourceName(photo)
                photo
            } catch (e: Resources.NotFoundException) {
                R.drawable.no_account
            }
        }

        Image(
            painter = painterResource(id = validPictureResId),
            contentDescription = "Profile",
            modifier = Modifier.size(60.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(7.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = name, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                painter = painterResource(R.drawable.right_arow),
                contentDescription = null,
                modifier = Modifier.size(9.dp),
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun ChatScreen(messages: List<ObicnaPorukaR>, modifier: Modifier) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        state = listState,
        reverseLayout = true
    ) {
        var lastSender = ""
        itemsIndexed(messages) { index, message ->
            val spacing = if (message.kontaktKoSalje?.ime == lastSender) 4.dp else 8.dp
            lastSender = message.kontaktKoSalje?.ime ?: ""

            MessageBubble(message, spacing)
        }
    }
}

@Composable
fun MessageBubble(message: ObicnaPorukaR, spacing: Dp) {
    val backgroundColor = if (message.kontaktKoSalje?.ime == "Me") colorResource(R.color.iphone_green) else colorResource(
        R.color.light_gray
    )
    val textColor = if (message.kontaktKoSalje?.ime == "Me") Color.White else Color.Black
    val horizontalAlignment = if (message.kontaktKoSalje?.ime == "Me") Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = spacing),
        horizontalArrangement = horizontalAlignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = (LocalConfiguration.current.screenWidthDp.dp * 0.75f))
                .background(backgroundColor, RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.tekst,
                color = textColor,
                fontSize = 16.sp,
                softWrap = true
            )
        }
    }
}

@Composable
fun TextInsertation() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
            .wrapContentHeight(),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(end = 12.dp)
        ) {
            TextField(
                enabled = false,
                value = "",
                onValueChange = { },
                placeholder = { Text("Text Message") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f).padding(0.dp)
            )

            Box(
                modifier = Modifier.size(32.dp)
                    .background(color = Color(0xFF34C759), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_up),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
            }
        }
    }
}