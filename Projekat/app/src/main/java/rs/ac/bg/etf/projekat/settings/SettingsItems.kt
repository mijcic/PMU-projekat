package rs.ac.bg.etf.projekat.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R

@Composable
fun LanguageSetting(selectedLanguage: String, onLanguageSelected: (String) -> Unit) {
    val languages = listOf("ENG", "SRB", "FRA")
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by rememberSaveable { mutableStateOf("ENG") }

    SettingRow("🌍 Language") {
        Box {
            TextButton(onClick = { expanded = true }) {
                Text(
                    text = selectedLanguage,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        fontSize = 18.sp,
                        color = Color.Black
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
}

@Composable
fun HelpSetting(context: Context) {
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
                    Toast.makeText(context, "Email app not found", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_purple)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Send a Message",
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun LogoutSetting(onLogout: () -> Unit) {
    SettingRow("🚪 Log out") {
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_purple)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Log Out",
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun DarkModeSetting(isDarkMode: Boolean, onToggle: (Boolean) -> Unit) {
    SettingRow("🌗 Dark Mode") {
        Switch(
            checked = isDarkMode,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.Black)
        )
    }
}
