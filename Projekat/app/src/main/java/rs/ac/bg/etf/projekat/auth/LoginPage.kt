package rs.ac.bg.etf.projekat.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val imagePainter = painterResource(id = R.drawable.background_login_signup)
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        var context = LocalContext.current
        val viewModel: MyViewModel = hiltViewModel()
        val realmViewModel: RealmViewModel = hiltViewModel()
        val uiState by viewModel.uiStateLogIn.collectAsState()
        var showDialog by remember { mutableStateOf(false) }
        var errorMsg by remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()

        val firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleGoogleLoginResult(task, context, viewModel)
            } else {
                errorMsg = "Sign in canceled or failed!"
                showDialog = true
                // Toast.makeText(context, "Google login canceled", Toast.LENGTH_SHORT).show()
            }
        }

        fun signInWithGoogle() {
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        }

        LaunchedEffect(uiState.message) {
            uiState.message?.let { response ->
                when (response.message.toString()) {
                    "TRUE" -> {
                        navController.navigate("destinationMainScreen2")
                    }
                    "FALSE", "User not found" -> {
                        errorMsg = "Incorrect credentials or user not registered!"
                        showDialog = true
                        // Toast.makeText(context, "Incorrect credentials or user not registered", Toast.LENGTH_SHORT).show()

                        GoogleSignIn.getClient(
                            context,
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(context.getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build()
                        ).signOut()
                    }
                }
            }
        }

        Image(
            painter = imagePainter,
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shadow(10.dp, RoundedCornerShape(20.dp))
                    .background(colorResource(id = R.color.light_gray))
                    .padding(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login",
                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                        fontSize = 36.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(1f, 1f),
                                blurRadius = 1f
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(13.dp))
                    Text(
                        text = "Username",
                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                        fontSize = 25.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White
                        ),
                        shape = RectangleShape,
                        textStyle = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Password",
                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                        fontSize = 25.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White
                        ),
                        shape = RectangleShape,
                        textStyle = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    if (showDialog) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(top = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.info_circle),
                                        contentDescription = "Info Icon",
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .padding(top = 2.dp)
                                            .size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = errorMsg,
                                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                                        fontSize = 17.sp,
                                        color = Color.Red,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .widthIn(max = 300.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(13.dp))
                    Button(
                        onClick = {
                            if (username == "" || password == ""){
                                errorMsg = "The data has not been entered!"
                                showDialog = true
                                return@Button
                                // Toast.makeText(context, "The data has not been entered!", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                viewModel.logIn(KorisnikRequest("", "", username, password, "", "", "", ""))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_purple)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Login",
                            fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                            fontSize = 17.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(1f, 1f),
                                    blurRadius = 2f
                                )
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            signInWithGoogle()
                            //navController.navigate("destinationCardsPage")
                        },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_purple)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icons8_google),
                                contentDescription = "Google Icon",
                                modifier = Modifier.size(25.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Login with gmail",
                                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                                fontSize = 17.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(1f, 1f),
                                        blurRadius = 2f
                                    )
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "If you don't have an account",
                            fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                            fontSize = 17.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        OutlinedButton(
                            onClick = { navController.navigate("destinationSignUpPage") },
                            border = BorderStroke(2.dp, Color.Transparent),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Sign up",
                                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                                fontSize = 17.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    textDecoration = TextDecoration.Underline,
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(1f, 1f),
                                        blurRadius = 1f
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

fun handleGoogleLoginResult(
    completedTask: Task<GoogleSignInAccount>,
    context: Context,
    viewModel: MyViewModel
) {
    try {
        val account = completedTask.getResult(ApiException::class.java)
        if (account != null) {
            val idTokenLast256 = account.idToken?.takeLast(256) ?: ""

            viewModel.logIn(
                KorisnikRequest(
                    ime = account.givenName ?: "",
                    prezime = account.familyName ?: "",
                    korisnickoIme = account.email?.substringBefore("@") ?: "",
                    sifra = "",
                    email = account.email ?: "",
                    nacinPrijave = "Google",
                    idToken = account.idToken ?: "",
                    idTokenLast256 = idTokenLast256.takeLast(256)
                )
            )
        }
    } catch (e: ApiException) {
        Toast.makeText(context, "Sign in failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}