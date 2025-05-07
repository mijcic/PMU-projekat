package rs.ac.bg.etf.projekat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.data.UiStateMysteriousSymptoms

@Composable
fun PatientScreen(navController: NavController, realmViewModel: RealmViewModel) {
    val data by realmViewModel.uiStateMysteriousSymptoms.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.patient),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                "👤 Pacijent: ${data.osobaPacijent?.ime}",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.height(30.dp))

            PatientExpandableSection(title = "📌 Detalji o pacijentu") {
                DetailItem("Godine", "28")
                DetailItem("Zanimanje", "${data.osobaPacijent?.zanimanje}")
                DetailItem("Simptomi", "${data.pacijentR?.simptomi}")
                DetailItem("Datum prijema", "/")
                DetailItem("Prijavila", "${data.pacijentR?.prijavio}")
            }

            Spacer(modifier = Modifier.height(20.dp))

            ExpandableSection(title = "🧾 Dokumenti") {
                PacijentInfoCard("📋", "Medicinski izveštaj", "Osnovni nalazi su čisti. CT i MR bez promena.",
                    { navController.navigate(destinationMedicalReportPage.route) })
                PacijentInfoCard("📱", "Zaključan telefon", "Poslednje poruke upućuju na duhovni centar 'Novi Krug'.",{ navController.navigate(destinationMedicalReportPage.route) })
                PacijentInfoCard("👪", "Izjava sestre", "Marko se povukao nakon vikenda u 'Novom Krugu'.",{ navController.navigate(destinationMedicalReportPage.route) })
                PacijentInfoCard("🧪", "Prvi rezultati testova", "Nisu pronađeni tragovi poznatih psihoaktivnih supstanci.",{ navController.navigate(destinationMedicalReportPage.route) })
            }
        }
    }
}

@Composable
fun PatientExpandableSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    title,
                    color = Color(0xFFBBBBBB),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.rotate(rotation)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .animateContentSize()
                ) {
                    content()
                }
            }
        }
    }
}

