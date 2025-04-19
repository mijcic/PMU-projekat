package rs.ac.bg.etf.projekat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.Divider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun PhoneSettingsPage(navController: NavController) {
    val font = FontFamily.SansSerif

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
            .verticalScroll(rememberScrollState())
            .padding(top = 50.dp, bottom = 20.dp)
    ) {
        Text(
            text = "Settings",
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            modifier = Modifier.padding(start = 16.dp),
            fontFamily = font
        )

        Spacer(Modifier.height(10.dp))

        SettingsCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp).padding(vertical = 25.dp)
            ) {
                CircleAvatar("IM")

                Spacer(Modifier.width(5.dp))

                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text("Isabele Moreau", fontWeight = FontWeight.Bold, fontFamily = font)
                    Text("Apple Account, iCloud+ and more", color = Color.Gray, fontFamily = font)
                }

                Spacer(Modifier.width(35.dp))

                Icon(
                    painter = painterResource(R.drawable.right_arow),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        SettingsCard {
            SettingsItem(
                "Airplane Mode",
                icon = R.drawable.airplane_fill,
                toggle = true,
                backgroundColor = colorResource(R.color.orange),
                rotationAngle = 90f
            )
            Divider()
            SettingsItem(
                "Wi-Fi",
                icon = R.drawable.wifi,
                value = "MARJANOVIC-5G",
                backgroundColor = colorResource(R.color.iphone_blue)
            )
            Divider()
            SettingsItem(
                "Bluetooth",
                icon = R.drawable.bluetooth,
                value = "Not Connected",
                backgroundColor = colorResource(R.color.iphone_blue)
            )
            Divider()
            SettingsItem(
                "Mobile Service",
                icon = R.drawable.reception_4,
                backgroundColor = colorResource(R.color.iphone_green)
            )
            Divider()
            SettingsItem(
                "Personal Hotspot",
                icon = R.drawable.link_45deg,
                backgroundColor = colorResource(R.color.iphone_green)
            )
            Divider()
            SettingsItem(
                "Battery",
                icon = R.drawable.battery_full,
                backgroundColor = colorResource(R.color.iphone_green)
            )
        }

        Spacer(Modifier.height(20.dp))

        SettingsCard {
            SettingsItem(
                "General",
                icon = R.drawable.general_icon,
                backgroundColor = Color.Gray
            )
            Divider()
            SettingsItem(
                "Accessibility",
                icon = R.drawable.accessbility_icon,
                backgroundColor = colorResource(R.color.iphone_blue)
            )
            Divider()
            SettingsItem(
                "Restore Data",
                icon = R.drawable.database_fill,
                toggle = true,
                backgroundColor = colorResource(R.color.golden_yellow)
            )
            Divider()
            SettingsItem(
                "Camera",
                icon = R.drawable.camera_fill,
                backgroundColor = Color.Gray
            )
            Divider()
            SettingsItem(
                "Control Centre",
                icon = R.drawable.toggles,
                backgroundColor = Color.Gray
            )
            Divider()
            SettingsItem(
                "Display & Brightness",
                icon = R.drawable.brightness_high_fill,
                backgroundColor = colorResource(R.color.iphone_blue)
            )
            Divider()
            SettingsItem(
                "Search",
                icon = R.drawable.search,
                backgroundColor = Color.Gray
            )
            Divider()
            SettingsItem(
                "Wallpaper",
                icon = R.drawable.flower1,
                backgroundColor = colorResource(R.color.purple_200)
            )
        }

        Spacer(Modifier.height(20.dp))

        SettingsCard {
            SettingsItem(
                "Notifications",
                icon = R.drawable.bell_fill,
                backgroundColor = Color.Red
            )
            Divider()
            SettingsItem(
                "Sounds & Haptics",
                icon = R.drawable.volume_up_fill,
                backgroundColor = Color.Red
            )
            Divider()
            SettingsItem(
                "Focus",
                icon = R.drawable.moon_fill,
                backgroundColor = colorResource(R.color.purple_700)
            )
            Divider()
            SettingsItem(
                "Screen Time",
                icon = R.drawable.hourglass_split,
                backgroundColor = colorResource(R.color.purple_700)
            )
        }

        Spacer(Modifier.height(20.dp))

        SettingsCard {
            SettingsItem(
                "Face ID & Passcode",
                icon = R.drawable.fingerprint,
                backgroundColor = colorResource(R.color.iphone_green)
            )
            Divider()
            SettingsItem(
                "Privacy & Security",
                icon = R.drawable.shield_fill_check,
                backgroundColor = colorResource(R.color.iphone_blue)
            )
        }

        Spacer(Modifier.height(20.dp))

        SettingsCard {
            SettingsItem(
                "Game Center",
                icon = R.drawable.controller,
                backgroundColor = colorResource(R.color.purple_700)
            )
            Divider()
            SettingsItem(
                "iCloud",
                icon = R.drawable.cloudy_fill,
                backgroundColor = Color.White,
                fillColor = colorResource(R.color.iphone_blue)
            )
            Divider()
            SettingsItem(
                "Wallet & Apple Pay",
                icon = R.drawable.wallet2,
                backgroundColor = Color.Black
            )
        }
    }
}

@Composable
fun CircleAvatar(initials: String) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
    }
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.background(Color.White), content = content)
    }
}

@Composable
fun SettingsItem(
    title: String,
    icon: Int? = 0,
    value: String? = null,
    toggle: Boolean = false,
    badge: String? = null,
    backgroundColor: Color = Color.Transparent,
    rotationAngle: Float = 0f,
    fillColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                    .padding(6.dp)
                    .rotate(rotationAngle),
                tint = fillColor
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 15.sp)
            if (value != null) {
                Text(text = value, color = Color.Gray, fontSize = 15.sp)
            }
        }

        when {
            toggle -> {
                var isChecked = remember { mutableStateOf(false) }
                Switch(checked = isChecked.value, onCheckedChange = { isChecked.value = it })
            }
            badge != null -> {
                Box(
                    modifier = Modifier
                        .background(Color.Red, shape = CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = badge, color = Color.White, fontSize = 15.sp)
                }
            }
            else -> {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null,
                    tint = Color.LightGray, modifier = Modifier.padding(start = 10.dp))
            }
        }
    }
}