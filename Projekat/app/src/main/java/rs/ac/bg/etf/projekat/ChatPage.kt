package rs.ac.bg.etf.projekat

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
import androidx.navigation.NavController
import androidx.compose.material.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatPage(name: String, photo: Int, navController: NavController) {
    var messages by remember { mutableStateOf(
        listOf(
            MessageInChat("Cao", "Me"),
            MessageInChat("Ej", "Other"),
            MessageInChat("Kako si?", "Me"),
            MessageInChat("Dobro", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
            MessageInChat("Ti?", "Other"),
        )
    )}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)
            .padding(top = 30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = photo),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(7.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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

        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        ChatScreen(
            messages = messages,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(25.dp)
                )
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
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("Text Message") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
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
}

@Composable
fun ChatScreen(messages: List<MessageInChat>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        var lastSender = ""
        itemsIndexed(messages) { index, message ->
            val spacing = if (message.sender == lastSender) 4.dp else 8.dp
            lastSender = message.sender

            MessageBubble(message, spacing)
        }
    }
}

@Composable
fun MessageBubble(message: MessageInChat, spacing: Dp) {
    val backgroundColor = if (message.sender == "Me") colorResource(R.color.iphone_green) else colorResource(R.color.light_gray)
    val alignment = if (message.sender == "Me") Alignment.CenterEnd else Alignment.CenterStart
    val textColor = if (message.sender == "Me") Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing),
        contentAlignment = alignment
    ) {
        Text(
            text = message.content,
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            color = textColor,
            fontSize = 16.sp
        )
    }
}

data class MessageInChat(val content: String, val sender: String)