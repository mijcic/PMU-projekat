package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.navigation.destinationHospitalPage
import rs.ac.bg.etf.projekat.navigation.destinationOfficePage

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MissionPage(image: Int, navController: NavController, realmViewModel: RealmViewModel) {
    val crimeData = realmViewModel.uiStateCrimeData.collectAsState()

    MissionBackground(
        image = image,
        onClick = {
            when (image) {
                R.drawable.murder -> navController.navigate(destinationOfficePage.route)
                else -> navController.navigate(destinationHospitalPage.route)
            }
        }
    ) {
        ModernGlassCard(
            title = crimeData.value.title,
            date = crimeData.value.date,
            place = crimeData.value.place,
            description = crimeData.value.description
        )
    }
}

@Composable
fun MissionBackground(
    image: Int, onClick: () -> Unit, content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().clickable { onClick() }) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(modifier = Modifier.align(Alignment.Center).padding(bottom = 24.dp)) {
            content()
        }
    }
}

@Composable
fun ModernGlassCard(title: String?, date: String?, place: String?, description: String?) {
    MissionBox {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(top = 20.dp)
        ) {
            TitleSection(title = title)
            DatePlaceSection(date = date, place = place)
            DescriptionSection(description = description)
        }
    }
}

@Composable
fun TitleSection(title: String?) {
    title?.let {
        Text(
            text = it.uppercase(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                shadow = Shadow(Color.Black, offset = Offset(1f, 1f), blurRadius = 4f)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DatePlaceSection(date: String?, place: String?) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp)
    ) {
        date?.let {
            Text(
                text = "📅 $it",
                fontSize = 14.sp,
                color = Color(0xFFCCCCCC),
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
            )
        }
        place?.let {
            Text(
                text = "📍 $it",
                fontSize = 14.sp,
                color = Color(0xFFE0E0E0),
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
            )
        }
    }
}

@Composable
fun DescriptionSection(description: String?) {
    description?.let {
        Text(
            text = it,
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 12.dp),
            style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
        )
    }
}

@Composable
fun MissionBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
            .heightIn(min = 300.dp).padding(top = 50.dp)
            .graphicsLayer {
                shadowElevation = 12.dp.toPx()
                shape = RoundedCornerShape(24.dp)
                clip = true
            }
            .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.05f))))
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        content()
    }
}