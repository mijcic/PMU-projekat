package rs.ac.bg.etf.projekat.phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.R

@Composable
fun OneContactPage(name: String, phoneNumber: String, picture: Int, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp).padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 30.dp)
                .padding(horizontal = 20.dp).padding(top = 5.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier.size(30.dp)
            ) {
                Text(text = "Edit", color = colorResource(R.color.iphone_blue), modifier = Modifier.clickable {  })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = picture),
            contentDescription = "Profile",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ContactAction(icon = R.drawable.iphone_chat_icon, label = "home")
            ContactAction(icon = R.drawable.iphone_telephone_fill_icon, label = "call")
            ContactAction(icon = R.drawable.iphone_video_icon, label = "video")
            ContactAction(icon = R.drawable.iphone_envelope_icon, label = "mail")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "phone", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(modifier = Modifier.fillMaxWidth().clickable {  }) {
            Text(text = "+63 123 4567", fontSize = 18.sp, color = colorResource(R.color.iphone_blue))
        }
        Spacer(modifier = Modifier.height(5.dp))
        Divider()

        Spacer(modifier = Modifier.height(3.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Notes", color = Color.LightGray, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Divider()
        ContactOption("Send Message")
        ContactOption("Share Contact")
        ContactOption("Add to Favorites")
        ContactOption("Share My Location")
    }
}

@Composable
fun ContactAction(icon: Int, label: String, enabled: Boolean = true) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colorResource(R.color.iphone_blue)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier
                    .size(19.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
        Text(text = label, fontSize = 12.sp, color = if (enabled) Color.Black else Color.LightGray)
    }
}

@Composable
fun ContactOption(text: String) {
    Text(
        text = text, fontSize = 16.sp, color = colorResource(R.color.iphone_blue),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp)
    )
    Divider()
}