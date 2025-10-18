package rs.ac.bg.etf.projekat.mysteriousSymptoms

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel

@Composable
fun LocationPage(
    navController: NavController,
    viewModel: MyViewModel,
    realmViewModel: RealmViewModel
) {
    val uiStateData by viewModel.uiStateMysteriousSymptomsData.collectAsState()
    val context = LocalContext.current
    val mapViewRef = remember { mutableStateOf<MapView?>(null) }
    Log.d("LOKACIJA", uiStateData.locations.toString())
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                val mapView = createMapView(context, uiStateData.locations)
                mapViewRef.value = mapView
                mapView
            },
            modifier = Modifier.fillMaxSize()
        )

        LocationOverlayHeader(Modifier.align(Alignment.TopStart),uiStateData.locations.size)

        LoacationBackButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onBack = { navController.popBackStack() }
        )
    }
}

fun createMapView(context: Context, locations: List<LokacijeIstrageR>): MapView {
    Configuration.getInstance().load(
        context,
        context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE)
    )
    Configuration.getInstance().userAgentValue = context.packageName


    val mapView = MapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
    }

    if (locations.isNotEmpty()) {
        val controller = mapView.controller
        val startPoint = GeoPoint(locations[0].geoTackaALatitude, locations[0].geoTackaALongitude)
        controller.setZoom(13.0)
        controller.setCenter(startPoint)

        locations.forEach { loc ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(loc.geoTackaALatitude, loc.geoTackaALongitude)
                title = loc.naziv
                subDescription = "${loc.mesto}\n${loc.opis}"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            mapView.overlays.add(marker)
        }
    }

    return mapView
}


@Composable
fun LocationOverlayHeader(modifier: Modifier,locationCount: Int) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .background(Color(0xAA000000), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Misteriozne Lokacije",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "$locationCount pronađenih mesta",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
fun LoacationBackButton(modifier: Modifier,onBack: () -> Unit) {
    IconButton(
        onClick = onBack,
        modifier = modifier
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