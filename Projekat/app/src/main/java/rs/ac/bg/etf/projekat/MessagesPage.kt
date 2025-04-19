package rs.ac.bg.etf.projekat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MessagesPage(navController: NavController) {
    val font = FontFamily.SansSerif

    val messages = listOf(
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
        Message("Ana", 1, "10:45 AM", R.drawable.whatsapp_profile_picture, "\uD83D\uDE0A ❤\uFE0F \uD83D\uDE0A ❤\uFE0F"),
        Message("Marko", 0, "9:15 AM", R.drawable.whatsapp_profile_picture, "Eej kako si"),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, bottom = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 30.dp)
                .padding(horizontal = 20.dp, vertical = 5.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Edit",
                    fontSize = 17.sp,
                    fontFamily = font,
                    color = colorResource(R.color.iphone_blue)
                )

                IconButton(
                    onClick = { },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.pencil_edit),
                        contentDescription = "Add",
                        tint = colorResource(R.color.iphone_blue)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Messages",
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            modifier = Modifier.padding(start = 16.dp),
            fontFamily = font
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            messages.forEach { message ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("destinationChatPage/" + message.name + "/" + message.picture) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (message.readOrNot == 0) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 6.dp, end = 6.dp)
                                    .size(8.dp)
                                    .background(colorResource(R.color.iphone_blue), shape = CircleShape)
                            )
                        } else {
                            Spacer(modifier = Modifier.width(20.dp))
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = message.picture),
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
                                    message.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    fontFamily = font
                                )
                                Text(
                                    message.lastMessage,
                                    fontSize = 16.sp,
                                    fontFamily = font
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    message.time,
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontFamily = font
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Icon(
                                    painter = painterResource(R.drawable.right_arow),
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
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

data class Message(val name: String, val readOrNot: Int, val time: String, val picture: Int, val lastMessage: String)