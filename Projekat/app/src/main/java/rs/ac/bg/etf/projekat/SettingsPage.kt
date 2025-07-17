package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("ResourceAsColor")
@Composable
fun SettingsPage(navController: NavController) {
    val context = LocalContext.current
    var isDarkMode by rememberSaveable { mutableStateOf(false) }
    var selectedLanguage by rememberSaveable { mutableStateOf("ENG") }

    val firebaseAuth = FirebaseAuth.getInstance()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    Box(modifier = Modifier.fillMaxSize()) {
        SettingsImage()

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsCard {

                SettingsText()

                DarkModeSetting(
                    isDarkMode = isDarkMode,
                    onToggle = {
                        isDarkMode = it
                        Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
                    }
                )

                LanguageSetting(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = {
                        Toast.makeText(context, "Language change coming soon!", Toast.LENGTH_SHORT)
                            .show()
                    }
                )

                HelpSetting(context = context)

                LogoutSetting(
                    onLogout = {
                        googleSignInClient.signOut().addOnCompleteListener {
                            firebaseAuth.signOut()
                            Toast.makeText(context, "Logged out!", Toast.LENGTH_SHORT).show()
                            navController.navigate("destinationLoginPage") {
                                popUpTo("destinationMainScreen2") { inclusive = true }
                            }
                        }
                    }
                )

                SettingsAppVersion(modifier = Modifier.align(Alignment.CenterHorizontally))
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

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .shadow(10.dp, RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.light_gray))
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Composable
fun SettingsImage(){
    val imagePainter = painterResource(id = R.drawable.background_login_signup)

    Image(
        painter = imagePainter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))
}

@Composable
fun SettingsText(){
    Text(
        text = "Settings",
        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
        fontSize = 32.sp,
        color = Color.Black,
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(1f, 1f),
                blurRadius = 2f
            )
        )
    )
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
fun SettingsAppVersion(modifier: Modifier){
    Divider(color = Color.Gray)
    Text(
        text = "App Version: 1.0.0",
        fontSize = 12.sp,
        color = Color.DarkGray,
        modifier = modifier
    )
}