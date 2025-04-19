package rs.ac.bg.etf.projekat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun WhatsAppChatPage(name: String, photo: Int, navController: NavController) {
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
    )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = photo),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(text = name, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 12.dp).padding(top = 5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.camera_video),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                Icon(
                    painter = painterResource(R.drawable.telephone),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        WhatsappChatScreen(
            messages = messages,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = 8.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(40.dp)
                ) {
                    BasicTextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 15.sp,
                            color = Color.Black
                        ),
                        singleLine = true
                    )

                    Icon(
                        painter = painterResource(R.drawable.sticky),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    painter = painterResource(R.drawable.mic),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }


    }
}

@Composable
fun WhatsappChatScreen(messages: List<MessageInChat>, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.whatsapp_background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            var lastSender = ""
            itemsIndexed(messages) { index, message ->
                val spacing = if (message.sender == lastSender) 4.dp else 8.dp
                lastSender = message.sender

                WhatsappMessageBubble(message, spacing)
            }
        }
    }
}

@Composable
fun WhatsappMessageBubble(message: MessageInChat, spacing: Dp) {
    val backgroundColor = if (message.sender == "Me") colorResource(R.color.iphone_yellow_green) else Color.White
    val alignment = if (message.sender == "Me") Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing),
        contentAlignment = alignment
    ) {
        Text(
            text = message.content,
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            fontSize = 16.sp
        )
    }
}