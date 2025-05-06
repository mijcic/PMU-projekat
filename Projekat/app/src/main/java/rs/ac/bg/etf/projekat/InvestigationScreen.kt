package rs.ac.bg.etf.projekat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun InvestigationScreen(navController: NavController) {
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
            1 -> PacijentScreen(navController)
            2 -> LokacijeScreen(navController)
            3 -> DokaziScreen(navController)
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
                lineHeight = 26.sp
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
fun PacijentScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(20.dp)
    ) {
        Text(
            "👤 Pacijent: Marko Maric",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(30.dp))

        ExpandableSection(title = "📌 Detalji o pacijentu") {
            DetailItem("Godine", "28")
            DetailItem("Zanimanje", "Softverski inženjer")
            DetailItem("Simptomi", "Katatonično stanje, ne reaguje na zvuke")
            DetailItem("Datum prijema", "03.04.2025.")
            DetailItem("Prijavila", "Sestra")
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
                    modifier = Modifier.weight(1f)
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


@Composable
fun LokacijeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .padding(20.dp)
    ) {
        Text("📍 Lokacije istrage:", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        PacijentInfoCard("🧘", "Duhovni centar 'Novi Krug'", "Vođa centra i instruktorka meditacije izbegavaju odgovore. Nađeni: sveska, flašica nepoznatog napitka.",{ navController.navigate(destinationMedicalReportPage.route) })
        PacijentInfoCard("🏠", "Porodični stan", "Sestra ga pronašla nepomičnog. Tragovi meditacije u sobi.",
            { navController.navigate(destinationMedicalReportPage.route) })
        PacijentInfoCard("🖥️", "Markov računar", "Nedavni pretraživački pojmovi: 'proširena svest', 'ritual inicijacije'.",
            { navController.navigate(destinationMedicalReportPage.route) })
        PacijentInfoCard("🏥", "Bolnica", "Lekari nemoćni da objasne uzrok, potrebne dodatne analize.",
            { navController.navigate(destinationMedicalReportPage.route) })
    }
}

@Composable
fun DokaziScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .padding(20.dp)
    ) {
        Text("🧾 Prikupljeni dokazi:", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        PacijentInfoCard("📄", "Sveska iz centra", "Kriptirane poruke: 'Sve je povezano', crteži simbola.",
            { navController.navigate(destinationMedicalReportPage.route) })
        PacijentInfoCard("🧪", "Flašica nepoznatog napitka", "Čeka se analiza na nepoznate supstance.",
            { navController.navigate(destinationMedicalReportPage.route) })
        PacijentInfoCard("🎥", "Kamera iz centra", "Snimak Markovog odlaska iz sale, delovao dezorijentisano.",
            { navController.navigate(destinationMedicalReportPage.route) })
        PacijentInfoCard("📱", "SMS poslednja poruka", "Poruka u 23:45: 'Sve je povezano.'",
            { navController.navigate(destinationMedicalReportPage.route) })
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
        Text("$label: ", color = Color.LightGray, fontWeight = FontWeight.Bold)
        Text(value, color = Color.White)
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
            Text(icon, fontSize = 22.sp, modifier = Modifier.padding(end = 12.dp))
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(description, color = Color.LightGray, fontSize = 14.sp)
            }
        }
    }
}
