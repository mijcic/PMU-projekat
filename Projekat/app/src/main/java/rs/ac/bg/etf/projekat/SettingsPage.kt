package rs.ac.bg.etf.projekat

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SettingsPage(navController: NavController) {
    var musicVolume by rememberSaveable { mutableStateOf(0.5f) }
    var isNotificationEnabled by rememberSaveable { mutableStateOf(true) }
    var isSoundEffectsEnabled by rememberSaveable { mutableStateOf(true) }
    var isDarkMode by rememberSaveable { mutableStateOf(false) }
    var selectedLanguage by rememberSaveable { mutableStateOf("ENG") }

    val languages = listOf("ENG", "SRB", "FRA")
    var expanded by remember { mutableStateOf(false) }

    Surface(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF34452F))
        .padding(top = 48.dp)) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(16.dp))
                .background(Color(0xFF2F4825), shape = RoundedCornerShape(11.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Text(
                text = "SETTINGS",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    fontSize = 32.sp,
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            SettingRow("🎵 Music") {
                Slider(
                    value = musicVolume,
                    onValueChange = { musicVolume = it },
                    valueRange = 0f..1f,
                    steps = 8,
                    modifier = Modifier.weight(1f)
                )
            }

            SettingRow("🔔 Notifications") {
                Checkbox(
                    checked = isNotificationEnabled,
                    onCheckedChange = { isNotificationEnabled = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color.White)
                )
            }

            SettingRow("🔊 Sound FX") {
                Checkbox(
                    checked = isSoundEffectsEnabled,
                    onCheckedChange = { isSoundEffectsEnabled = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color.White)
                )
            }

            SettingRow("🌗 Dark Mode") {
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isDarkMode = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                )
            }

            SettingRow("🌍 Language") {
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(
                            text = selectedLanguage,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        languages.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang) },
                                onClick = {
                                    selectedLanguage = lang
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            val context = LocalContext.current

            SettingRow("❓ Help") {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("support@detectivegame.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "Help Request - Murder at the Casino")
                            putExtra(Intent.EXTRA_TEXT, "Hello, I need help with...")
                        }

                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Send a Message",
                        color = Color.Black,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontSize = 16.sp
                        )
                    )
                }
            }

            Divider(color = Color.LightGray)

            Button(
                onClick = {
                    musicVolume = 0.5f
                    isNotificationEnabled = true
                    isSoundEffectsEnabled = true
                    isDarkMode = false
                    selectedLanguage = "ENG"
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Reset to Default", color = Color.Black)
            }
        }
    }
}

@Composable
fun SettingRow(label: String, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 18.sp,
                color = Color.White
            )
        )
        content()
    }
}
