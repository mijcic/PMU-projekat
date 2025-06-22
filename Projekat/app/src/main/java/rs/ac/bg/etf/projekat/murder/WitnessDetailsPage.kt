package rs.ac.bg.etf.projekat.murder

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.navigation.destinationWitnessesInterviewPage

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WitnessDetailsPage(image: Int, title: String, navController: NavController) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C))
            .padding(top = 22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1C)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WitnessData(
                title = title,
                image = image
            )

            Spacer(modifier = Modifier.height(24.dp))

            StickyNote()

            Spacer(modifier = Modifier.height(16.dp))

            PsychologicalProfile()

            Spacer(modifier = Modifier.height(24.dp))

            InterrogateWitnessButton(
                navController = navController,
                title = title
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Text(
        text = "$label: $value",
        color = Color.White,
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.special_elite)),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun WitnessData(
    title: String,
    image: Int
) {
    Text(
        text = "CASE FILE",
        color = Color(0xFFE0C97F),
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.special_elite)),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = title,
        color = Color.White,
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.special_elite)),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
    )

    Spacer(modifier = Modifier.height(16.dp))

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF333333),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "Suspect Image",
                modifier = Modifier.size(180.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(label = "Motive", value = "?")
            InfoRow(label = "Alibi", value = "?")
            InfoRow(label = "Status", value = "?")
        }
    }
}

@Composable
fun InterrogateWitnessButton(
    navController: NavController,
    title: String
) {
    Button(
        onClick = { navController.navigate(destinationWitnessesInterviewPage.route + "/" + title) },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(Color(0xFFB71C1C)),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp)
    ) {
        Text(
            text = "Interrogate the Witness",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun PsychologicalProfile() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF424242),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Psychological Profile:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("- Emotional: Stable", color = Color.White)
            Text("- Cooperativeness: Low", color = Color.White)
            Text("- Stress Level: High", color = Color.White)
        }
    }
}

@Composable
fun StickyNote() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF9C4),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "\"Izgleda nervozno. Proveri alibi još jednom.\"",
            modifier = Modifier.padding(16.dp),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 16.sp,
                color = Color.Black
            )
        )
    }
}