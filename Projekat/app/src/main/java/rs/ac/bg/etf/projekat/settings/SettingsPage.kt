package rs.ac.bg.etf.projekat.settings

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import rs.ac.bg.etf.projekat.Background
import rs.ac.bg.etf.projekat.R

@SuppressLint("ResourceAsColor")
@Composable
fun SettingsPage(onClick: () -> Unit) {
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
        Background(
            image = R.drawable.background_login_signup,
            desc = null,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f
        )

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
                            onClick()
                        }
                    }
                )

                SettingsAppVersion(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}