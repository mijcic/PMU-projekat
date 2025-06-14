package rs.ac.bg.etf.projekat.mysteriousSymptoms.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R

@Composable
fun HospitalPage(
    //modifier: Modifier = Modifier,
    onPatientClick: () -> Unit,
    onLocationClick: () -> Unit,
    onEvidenceClick: () -> Unit,
    onTaskClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            HospitalTaskFloatingButton(onClick = onTaskClick)
        },
        content = { paddingValues ->
            HospitalBackground {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HospitalTitleBanner(text = "Detective...")

                    Spacer(modifier = Modifier.height(204.dp))

                    HospitalCardItem(title = "Patient", onClick = onPatientClick)
                    Spacer(modifier = Modifier.height(16.dp))
                    HospitalCardItem(title = "Locations", onClick = onLocationClick)
                    Spacer(modifier = Modifier.height(16.dp))
                    HospitalCardItem(title = "Evidences", onClick = onEvidenceClick)
                }
            }
        }
    )
}

@Composable
fun HospitalCardItem(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun HospitalTaskFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .size(60.dp),
        shape = CircleShape,
        containerColor = Color.Transparent,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.tasks2),
            contentDescription = "Tasks",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().clip(CircleShape)
        )
    }
}

@Composable
fun HospitalBackground(content: @Composable BoxScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.hospital),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.3f)))
        content()
    }
}

@Composable
fun HospitalTitleBanner(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

sealed class HospitalNavigationEvent {
    object ToPatient : HospitalNavigationEvent()
    object ToLocation : HospitalNavigationEvent()
    object ToEvidence : HospitalNavigationEvent()
    object ToMap : HospitalNavigationEvent()
}