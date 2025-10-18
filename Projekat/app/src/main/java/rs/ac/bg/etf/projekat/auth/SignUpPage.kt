package rs.ac.bg.etf.projekat.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import rs.ac.bg.etf.projekat.navigation.destinationMainScreen2
import rs.ac.bg.etf.projekat.navigation.destinationMissionPage

@SuppressLint("StateFlowValueCalledInComposition", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(
    navController: NavController,viewModel:MyViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val imagePainter = painterResource(id = R.drawable.background_login_signup)
        val realmViewModel: RealmViewModel = hiltViewModel()
        var nameAndSurname by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        //val viewModel: MyViewModel = hiltViewModel()
        val uistateSignUp by viewModel.uiStateSignUp.collectAsState()
        val uiStateLogIn by viewModel.uiStateLogIn.collectAsState()

        var ime by remember { mutableStateOf("") }
        var prezime by remember { mutableStateOf("") }
        var nacinPrijave by remember { mutableStateOf("") }
        var idToken by remember { mutableStateOf("") }
        var idTokenLast256 by remember { mutableStateOf("") }
        var context = LocalContext.current
        var showDialog by remember { mutableStateOf(false) }
        var errorMsg by remember { mutableStateOf("") }

        // signup via gmail
        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        val updateUI: (GoogleSignInAccount) -> Unit = { account ->
            val credential= GoogleAuthProvider.getCredential(account.idToken,null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
                if(task.isSuccessful) {
                    ime = account.givenName ?: ""
                    prezime = account.familyName ?: ""
                    username = account.email?.substringBefore("@") ?: ""
                    password = ""
                    email = account.email ?: ""
                    nacinPrijave = "Google"
                    idToken = account.idToken ?: ""
                    idTokenLast256 = account.idToken?.takeLast(256) ?: ""

                    viewModel.signUp(
                        KorisnikRequest(
                            ime = account.givenName ?: "",
                            prezime = account.familyName ?: "",
                            korisnickoIme = account.email?.substringBefore("@") ?: "",
                            sifra = "",
                            email = account.email ?: "",
                            nacinPrijave = "Google",
                            idToken = account.idToken ?: "",
                            idTokenLast256 = account.idToken?.takeLast(256) ?: ""
                        )
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        realmViewModel.insertKorisnik(
                            imeK = account.givenName ?: "",
                            prezimeK = account.familyName ?: "",
                            korisnickoImeK = account.email?.substringBefore("@") ?: "",
                            sifraK = "",
                            emailK = account.email ?: "",
                            nacinPrijaveK = "Google",
                            idTokenK = account.idToken ?: "",
                            idTokenLast256K = account.idToken?.takeLast(256) ?: ""
                        )
                    }

                    Log.d("SIGNUP", "Google account: ${account.email}, ${account.givenName}, ${account.familyName}, ${account.idToken}")
                    Log.d("SIGNUP", uistateSignUp.message.toString())


                    // Toast.makeText(context, "Google SignIn success: ${account.email}", Toast.LENGTH_SHORT).show()
                    // Toast.makeText(context, "Google SignIn success: ${account.idToken}", Toast.LENGTH_SHORT).show()
                }
                else {
                    errorMsg = "There has been an error while signing up!"
                    showDialog = true
                }
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task, context, updateUI)
            } else {
                errorMsg = "Sign in canceled or failed!"
                showDialog = true
                // Toast.makeText(context, "Sign in canceled or failed", Toast.LENGTH_SHORT).show()
            }
        }

        fun signInWithGoogle() {
            val signInIntent = mGoogleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        LaunchedEffect(uistateSignUp.message?.message) {
            val message = uistateSignUp.message?.message
            if (message != null) {
                if (message == "Korisnik inserted successfully" || message == "Korisnik already exists.") {
                    viewModel.logIn(
                        KorisnikRequest(
                            ime = ime,
                            prezime = prezime,
                            korisnickoIme = username,
                            sifra = password,
                            email = email,
                            nacinPrijave = nacinPrijave,
                            idToken = idToken,
                            idTokenLast256 = idTokenLast256
                        )
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        realmViewModel.insertKorisnik(
                            imeK = ime,
                            prezimeK = prezime,
                            korisnickoImeK = username,
                            sifraK = password,
                            emailK = email,
                            nacinPrijaveK = nacinPrijave,
                            idTokenK = idToken,
                            idTokenLast256K = idTokenLast256
                        )
                    }
                }
                else {
                    errorMsg = "Sign in canceled or failed!"
                    showDialog = true
                }
            }
        }

        LaunchedEffect(uiStateLogIn.message?.message) {
            if (uiStateLogIn.message?.message != null) {
                if (uiStateLogIn.message?.message == "TRUE") {
                    navController.navigate("destinationMainScreen2")
                }
                else {
                    errorMsg = "Sign in canceled or failed!"
                    showDialog = true
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
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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
                        text = "Sign up",
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
                        text = "Name & Surname",
                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                        fontSize = 25.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = if (nacinPrijave == "Google") "" else nameAndSurname,
                        onValueChange = { nameAndSurname = it },
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
                        text = "Username",
                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                        fontSize = 25.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = if (nacinPrijave == "Google") "" else username,
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
                        value = if (nacinPrijave == "Google") "" else password,
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
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "E-mail",
                        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                        fontSize = 25.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(
                        value = if (nacinPrijave == "Google") "" else email,
                        onValueChange = { email = it },
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
                            if(username=="" || password=="" || email=="" || nameAndSurname==""){
                                errorMsg = "The data has not been entered!"
                                showDialog = true
                                return@Button
                                // Toast.makeText(context, "The data has not been entered!", Toast.LENGTH_SHORT).show()
                            }
                            else if (password.length < 6) {
                                errorMsg = "Password must be at least 6 characters!"
                                showDialog = true
                                return@Button
                                // Toast.makeText(context, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
                            }
                            else if (!(username.length >= 3 && username.matches("^[a-zA-Z0-9_]*$".toRegex()))) {
                                errorMsg = "Username must be at least 3 characters and contain only alphanumeric characters or underscores!"
                                showDialog = true
                                return@Button
                                // Toast.makeText(context, "Username must be at least 3 characters and contain only alphanumeric characters or underscores!", Toast.LENGTH_LONG).show()
                            }
                            else if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                                errorMsg = "Invalid email format!"
                                showDialog = true
                                return@Button
                                // Toast.makeText(context, "Invalid email format!", Toast.LENGTH_SHORT).show()
                            }
                            val parts = nameAndSurname.split(" ")
                            if (parts.size < 2) {
                                errorMsg = "Please enter both first name and last name!"
                                showDialog = true
                                return@Button
                                // Toast.makeText(context, "Please enter both first name and last name!", Toast.LENGTH_SHORT).show()
                            } else {
                                ime = parts[0] // First name
                                prezime = parts[1] // Last name
                                nacinPrijave = "registracija"
                                if (idToken == "") idToken = username + password
                                idTokenLast256 = idToken.takeLast(256)

                                viewModel.signUp(KorisnikRequest(
                                    ime, prezime, username, password, email, nacinPrijave = "registracija", idToken = username + password, idTokenLast256 = ""))

                                //viewModel.logIn(KorisnikRequest("","",username, password,"","","",""))
                                navController.navigate("destinationMainScreen2")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_purple)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Sign up",
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
                                text = "Sign up with gmail",
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
                            text = "If you already have an account",
                            fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                            fontSize = 17.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        OutlinedButton(
                            onClick = { navController.navigate("destinationLoginPage") },
                            border = BorderStroke(2.dp, Color.Transparent),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Login",
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

private fun handleResult(
    completedTask: Task<GoogleSignInAccount>,
    context: Context,
    updateUI: (GoogleSignInAccount) -> Unit
) {
    try {
        val account = completedTask.getResult(ApiException::class.java)
        if (account != null) {
            updateUI(account)
        }
    } catch (e: ApiException) {
        Toast.makeText(context, "Sign in failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}