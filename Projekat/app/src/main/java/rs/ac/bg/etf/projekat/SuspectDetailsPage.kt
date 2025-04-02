package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realm.stZlocinR
import rs.ac.bg.etf.projekat.data.retrofit.models.AlibiData
import rs.ac.bg.etf.projekat.data.retrofit.models.DokazData
import rs.ac.bg.etf.projekat.data.retrofit.models.MisijaData
import rs.ac.bg.etf.projekat.data.retrofit.models.MotivData
import rs.ac.bg.etf.projekat.data.retrofit.models.OsumnjicenData
import rs.ac.bg.etf.projekat.data.retrofit.models.SvedokData
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinData
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.ZrtvaData
import java.util.Date

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SuspectDetailsPage(image: Int, title: String, navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF233331))
            .padding(top = 22.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF233331))
                .padding(top = (screenWidth / 8).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.White,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
            )

            Spacer(modifier = Modifier.height(16.dp)) // Razmak između naslova i slike

            // Slika osumnjičenog sa kružnim oblikom i senkom
            Image(
                painter = painterResource(id = image),
                contentDescription = "Suspect Image",
                modifier = Modifier
                    .size(220.dp) // Veličina slike
                    .clip(CircleShape) // Kružni oblik slike
                    .border(4.dp, Color.White, CircleShape) // Bela granica oko slike
                    .shadow(8.dp, CircleShape) // Dodajemo senku na sliku
            )

            Spacer(modifier = Modifier.height(24.dp)) // Razmak između slike i teksta

            // Informacije o osumnjičenom (motiv, alibi, status)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Motive: ?",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        fontSize = 18.sp,
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Alibi: ?",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        fontSize = 18.sp,
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Status: ?",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        fontSize = 18.sp,
                    )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {

                    navController.navigate(destinationSuspectsInterviewPage.route+ "/" + title) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors( Color(0xFF1F2D2D)), // Tamna nijansa dugmeta
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Interrogate the Suspect",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
