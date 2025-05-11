package rs.ac.bg.etf.projekat.mysteriousSymptoms

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.data.UiStateMysteriousSymptoms
import rs.ac.bg.etf.projekat.destinationLekarskiTestPage
import rs.ac.bg.etf.projekat.destinationMedicalReportPage
import rs.ac.bg.etf.projekat.destinationMedicalStatementPage

@Composable
fun InvestigationScreen(navController: NavController,myViewModel: MyViewModel,realmViewModel: RealmViewModel) {
    val data by realmViewModel.uiStateMysteriousSymptoms.collectAsState()
    val tabs = listOf("🏠 Uvod", "👤 Pacijent", "📍 Lokacije", "🧾 Dokazi", "⚖️ Zaključak")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Surface(
            tonalElevation = 6.dp,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 15.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedTabIndex) {
            0 -> UvodScreen()
            1 -> PacijentScreen(navController,data)
            4 -> ZakljucakScreen()
        }
    }
}

@Composable
fun UvodScreen() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.hospital),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Muškarac u kasnim dvadesetim primljen je na urgentno odeljenje u katatoničnom stanju.\n\nNema povreda, nema tragova nasilja. Lekari ne mogu da utvrde uzrok.\n\nTi, kao detektiv specijalizovan za 'neobične slučajeve', pozvan si da istražiš.",
                color = Color.White,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
    }
}

@Composable
fun PacijentScreen(navController: NavController, data: UiStateMysteriousSymptoms) {

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
                " Pacijent: ${data.osobaPacijent?.ime}",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.height(30.dp))

            ExpandableSection(title = "Detalji o pacijentu") {
                DetailItem("Godine", "28")
                DetailItem("Zanimanje", "${data.osobaPacijent?.zanimanje}")
                DetailItem("Simptomi", "${data.pacijentR?.simptomi}")
                DetailItem("Datum prijema", "/")
                DetailItem("Prijavila", "${data.pacijentR?.prijavio}")
            }

            Spacer(modifier = Modifier.height(20.dp))

            ExpandableSection(title = "Dokumenti") {
                PacijentInfoCard("📋", "Medicinski izveštaj", "Osnovni nalazi su čisti. CT i MR bez promena.",
                    { navController.navigate(destinationMedicalReportPage.route) })
                PacijentInfoCard("📱", "Zaključan telefon", "Poslednje poruke upućuju na duhovni centar 'Novi Krug'.",{ navController.navigate(
                    destinationMedicalReportPage.route
                ) })
                PacijentInfoCard("👪", "Izjava sestre", "Marko se povukao nakon vikenda u 'Novom Krugu'.",
                    { navController.navigate(destinationMedicalStatementPage.route) })
                PacijentInfoCard("🧪", "Prvi rezultati testova", "Nisu pronađeni tragovi poznatih psihoaktivnih supstanci.",
                    { navController.navigate(destinationLekarskiTestPage.route) })
            }
        }
    }

}

@Composable
fun ExpandableSection(title: String, content: @Composable ColumnScope.() -> Unit) {
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
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White),
                    textAlign = TextAlign.Center)
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


@Composable
fun ZakljucakScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111111))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("⚖️ Zaključak istrage", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Izaberi kraj slučaja:", color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* potvrdi zakljucak */ }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("✅ Toksični napitak – analiza potvrđuje upotrebu ilegalne supstance.")
        }
        Button(onClick = { /* potvrdi zakljucak */ }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("🌀 Psihološka trauma – mentalni kolaps izazvan hipnozom.")
        }
        Button(onClick = { /* potvrdi zakljucak */ }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("🕯️ Sektaški eksperiment – Marko žrtvovan u ritualu.")
        }
        Button(onClick = { /* potvrdi zakljucak */ }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text("🎭 Lažni slučaj – Marko simulirao sve da pobegne od stvarnosti.")
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("$label: ", color = Color.LightGray, fontWeight = FontWeight.Bold,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
        )
        Text(value, color = Color.White,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
        )
    }
}

@Composable
fun PacijentInfoCard(icon: String, title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 22.sp, modifier = Modifier.padding(end = 12.dp),
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
            )
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
                )
                Text(description, color = Color.LightGray, fontSize = 14.sp,
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White)
                )
            }
        }
    }
}
