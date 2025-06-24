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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.google.firebase.auth.GoogleAuthProvider
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import rs.ac.bg.etf.projekat.navigation.destinationMainScreen2

@SuppressLint("StateFlowValueCalledInComposition", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val imagePainter = painterResource(id = R.drawable.background_login_signup)
        var nameAndSurname by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        val viewModel: MyViewModel = hiltViewModel()
        val uistateSignUp by viewModel.uiStateSignUp.collectAsState()

        var ime by remember { mutableStateOf("") }
        var prezime by remember { mutableStateOf("") }
        var context = LocalContext.current

        // signup via gmail
        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

//        LaunchedEffect(Unit) {
//            mGoogleSignInClient.signOut().addOnCompleteListener {
//                firebaseAuth.signOut()
//                Log.d("SIGNUP", "Signed out from previous Google session")
//            }
//        }

        val updateUI: (GoogleSignInAccount) -> Unit = { account ->
            val credential= GoogleAuthProvider.getCredential(account.idToken,null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
                if(task.isSuccessful) {
                    viewModel.signUp(
                        KorisnikRequest(
                            ime = account.givenName ?: "",
                            prezime = account.familyName ?: "",
                            korisnickoIme = account.email?.substringBefore("@") ?: "",
                            sifra = "",
                            email = account.email ?: "",
                            nacinPrijave = "Google",
                            idToken = account.idToken ?: ""
                        )
                    )

                    Log.d("SIGNUP", "Google account: ${account.email}, ${account.givenName}, ${account.familyName}, ${account.idToken}")

                    Toast.makeText(context, "Google SignIn success: ${account.email}", Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "Google SignIn success: ${account.idToken}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task, context, updateUI)
                navController.navigate("destinationMainScreen2")
            } else {
                Toast.makeText(context, "Sign in canceled or failed", Toast.LENGTH_SHORT).show()
            }
        }

        fun signInWithGoogle() {
            val signInIntent = mGoogleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        LaunchedEffect(uistateSignUp.message?.message) {
            val toastMessage = uistateSignUp.message?.message

            Log.d("SIGNUPx", "tionCardsPage")
            if (toastMessage != null && toastMessage.isNotEmpty()) {
                if (toastMessage == "Korisnik inserted successfully") {
                    Log.d("SIGNUPx", "Navigating to destinationCardsPage")
                    navController.navigate(destinationMainScreen2.route)
                } else {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
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
                        value = nameAndSurname,
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
                        )
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
                        )
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
                        )
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
                        value = email,
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
                        )
                    )
                    Spacer(modifier = Modifier.height(13.dp))
                    Button(
                        onClick = {
                            if(username=="" || password=="" || email=="" || nameAndSurname==""){
                                Toast.makeText(context, "The data has not been entered!", Toast.LENGTH_SHORT).show()
                            }
                            else if (password.length < 6) {
                                Toast.makeText(context, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
                            }
                            else if (!(username.length >= 3 && username.matches("^[a-zA-Z0-9_]*$".toRegex()))) {
                                Toast.makeText(context, "Username must be at least 3 characters and contain only alphanumeric characters or underscores!", Toast.LENGTH_LONG).show()
                            }
                            else if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                                Toast.makeText(context, "Invalid email format!", Toast.LENGTH_SHORT).show()
                            }
                            val parts = nameAndSurname.split(" ")
                            if (parts.size < 2) {
                                Toast.makeText(context, "Please enter both first name and last name!", Toast.LENGTH_SHORT).show()
                            } else {
                                ime = parts[0] // First name
                                prezime = parts[1] // Last name
                            }
                            viewModel.signUp(KorisnikRequest(
                                ime, prezime, username, password, email, nacinPrijave = "registracija", idToken = ""))
                            //navController.navigate("destinationCardsPage")
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