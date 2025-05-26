package rs.ac.bg.etf.projekat.mysteriousSymptoms

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPage(
    navController: NavController,
    viewModel: MyViewModel,
    realmViewModel: RealmViewModel
) {
    val uiStateData by viewModel.uiStateMysteriousSymptomsData.collectAsState()
    val context = LocalContext.current
    val mapViewRef = remember { mutableStateOf<MapView?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                Configuration.getInstance().load(
                    context,
                    context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE)
                )
                val mapView = MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                }

                val controller = mapView.controller
                val locations = uiStateData.locations
                if (locations.isNotEmpty()) {
                    val startPoint = GeoPoint(
                        locations[0].geoTackaALatitude,
                        locations[0].geoTackaALongitude
                    )
                    controller.setZoom(13.0)
                    controller.setCenter(startPoint)

                    for (loc in locations) {
                        val marker = Marker(mapView).apply {
                            position = GeoPoint(loc.geoTackaALatitude, loc.geoTackaALongitude)
                            title = loc.naziv
                            subDescription = "${loc.mesto}\n${loc.opis}"
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }
                        mapView.overlays.add(marker)
                    }
                }

                mapViewRef.value = mapView
                mapView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Overlay: Gornji levi ugao
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xAA000000), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = "Misteriozne Lokacije",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${uiStateData.locations.size} pronađenih mesta",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Back dugme u gornjem desnom uglu
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color(0xAA000000), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Nazad",
                tint = Color.White
            )
        }
    }
}

