package rs.ac.bg.etf.projekat

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB388FF),
    secondary = Color(0xFF80CBC4),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color(0xFFE0E0E0),
)

sealed class GameTab(val title: String, val icon: ImageVector) {
    object Zajednica : GameTab("Zajednica", Icons.Default.Star)
    object Faze : GameTab("Faze", Icons.Default.Check)
    object Knjiga : GameTab("Knjiga", Icons.Default.Home)
    object Ispovesti : GameTab("Ispovesti", Icons.Default.Call)
    object StanjeUma : GameTab("Stanje Uma", Icons.Default.Star)
    object Mapa : GameTab("Mapa", Icons.Default.Star)
    object Codex : GameTab("Codex", Icons.Default.Star)
    object Dokazi : GameTab("Dokazi", Icons.Default.Star)
    object Zadaci : GameTab("Zadaci", Icons.Default.Star)
}

@Composable
fun CultAndSectsPage() {
    MaterialTheme(colorScheme = DarkColorScheme) {
        val tabs = listOf(
            GameTab.Zajednica,
            GameTab.Faze,
            GameTab.Knjiga,
            GameTab.Ispovesti,
            GameTab.StanjeUma,
            GameTab.Mapa,
            GameTab.Codex,
            GameTab.Dokazi,
            GameTab.Zadaci
        )
        var selectedTabIndex by remember { mutableStateOf(0) }

        Column(modifier = Modifier
            .fillMaxSize()
            .background(DarkColorScheme.background)) {

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = DarkColorScheme.surface,
                contentColor = DarkColorScheme.onSurface
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        text = { Text(tab.title, fontSize = 14.sp) },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }

            when (tabs[selectedTabIndex]) {
                is GameTab.Zajednica -> ZajednicaScreen()
                is GameTab.Faze -> FazeProsvetljenjaScreen()
                is GameTab.Knjiga -> KnjigaBezNaslovaScreen()
                is GameTab.Ispovesti -> IspovestiScreen()
                is GameTab.StanjeUma -> MindStateScreen()
                is GameTab.Mapa -> MapaKomuneScreen()
                is GameTab.Codex -> SymbolCodexScreen()
                is GameTab.Dokazi -> DokaziScreen()
                is GameTab.Zadaci -> ZadaciScreen()
            }
        }
    }
}

@Composable
fun StyledCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = DarkColorScheme.surface),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Composable
fun ZajednicaScreen() {
    val clanovi = listOf("Brat Priziv", "Sena", "Onaj Koji Vidi", "Tišina")

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.sects),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(clanovi) { ime ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {}
                        .height(200.dp)
                        .fillMaxWidth(0.4f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A2B2D)
                    ),
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {

                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = ime,
                            style = TextStyle(
                                fontFamily = FontFamily(
                                    Font(R.font.special_elite)
                                ),
                                color = Color.White
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun FazeProsvetljenjaScreen() {
    val faze = listOf(
        "Buđenje svesti",
        "Oslobađanje od iluzija",
        "Unutrašnji mir",
        "Proširena svest",
        "Jedinstvo sa svime"
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.sects_f),
            contentDescription = "Pozadinska slika",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Tvoje faze prosvetljenja:",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            faze.forEachIndexed { index, naziv ->
                if (index < 2) {
                    FazaCardOtkljucana(naziv, index + 1)
                } else {
                    FazaCardZakljucana(index + 1)
                }
            }
        }
    }
}

@Composable
fun FazaCardOtkljucana(nazivFaze: String, broj: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)) // svetloplava
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Otključano",
                    tint = Color(0xFF0D47A1),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Faza $broj: $nazivFaze",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ova faza je otključana. Nastavi put prosvetljenja.",
                style = TextStyle(fontSize = 14.sp, color = Color.DarkGray)
            )
        }
    }
}

@Composable
fun FazaCardZakljucana(broj: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Zaključano",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Faza $broj: Zaključana",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            )
            Text(
                text = "Nastavi da napreduješ kako bi otključao ovu fazu.",
                style = TextStyle(fontSize = 14.sp, color = Color.White)
            )
        }
    }
}

@Composable
fun KnjigaBezNaslovaScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.sects_book),
            contentDescription = "Pozadinska slika",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Knjiga Bez Naslova",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E).copy(alpha = 0.85f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Citirani tekst",
                            tint = Color(0xFFE0B0FF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tajanstveni zapis",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFE0B0FF)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "“Ko pogleda unazad, neće videti Zoru.”",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Stranice se otključavaju pronalaskom simbola...",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.LightGray
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun IspovestiScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Zagonetne Ispovesti", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(8.dp))
        StyledCard {
            Text("• 'Ona nije prešla... ona je pobegla.'")
            Spacer(Modifier.height(4.dp))
            Text("• 'Glas ne govori svima, samo onima koji su spremni.'")
        }
    }
}
@Composable
fun MindStateScreen() {
    var mindState by remember { mutableStateOf(0.4f) }

    val animatedMindState by animateFloatAsState(
        targetValue = mindState,
        animationSpec = tween(durationMillis = 800),
        label = "MindStateAnimation"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.detective_mind),
            contentDescription = "Pozadinska slika",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Mentalno stanje",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F).copy(alpha = 0.85f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Mental icon",
                            tint = Color(0xFF80CBC4),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Stabilnost uma",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = animatedMindState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = when {
                            mindState < 0.3f -> Color.Red
                            mindState < 0.6f -> Color.Yellow
                            else -> Color.Green
                        },
                        trackColor = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Stabilnost: ${(mindState * 100).toInt()}%",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.LightGray
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun MapaKomuneScreen() {
    val lokacije = listOf("Krug Svetlosti", "Soba Tišine", "Pećina Gledanja", "Zapisnička Kuća")
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Mapa Komune", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(8.dp))
        lokacije.forEach {
            StyledCard {
                Text("- $it (${if (it == "Pećina Gledanja") "Zaključano" else "Otključano"})")
            }
        }
    }
}

@Composable
fun SymbolCodexScreen() {
    val simboli = listOf("☼ Prelazak", "◉ Obnova", "✠ Žrtva", "∞ Prosvetljenje")
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Katalog Simbola", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(8.dp))
        simboli.forEach {
            StyledCard {
                Text(it, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun DokaziScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Dokazi", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(8.dp))
        StyledCard {
            Text("- Dnevnik Ane (pronađen u sobi snova)")
            Text("- Snimak rituala (skrivena kamera)")
            Text("- Simbol sa kamena (disk kod pećine)")
        }
    }
}

@Composable
fun ZadaciScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Dnevni zadaci", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(8.dp))
        StyledCard {
            Text("✔ Prisustvuj obredu tišine")
            Text("✔ Pronađi Simbol Svetlosti")
            Text("✖ Ispitaj Brata Priziva (previše sumnjičav)")
        }
    }
}
