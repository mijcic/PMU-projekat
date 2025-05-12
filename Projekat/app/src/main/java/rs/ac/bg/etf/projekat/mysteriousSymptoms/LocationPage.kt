package rs.ac.bg.etf.projekat.mysteriousSymptoms

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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


@Composable
fun LocationPage(navController: NavController,viewModel: MyViewModel,realmViewModel: RealmViewModel) {

    val context = LocalContext.current

    AndroidView(
        factory = {
            Configuration.getInstance().load(context, context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE))
            val mapView = MapView(context)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)

            val controller = mapView.controller
            controller.setZoom(13.0)
            val startPoint = GeoPoint(44.8020, 20.4620) // centar Beograda
            controller.setCenter(startPoint)

            // Lista lokacija
            val lokacije = listOf(
                Triple("🧘 Duhovni centar 'Novi Krug'", GeoPoint(44.8189, 20.4632), "Sveska, nepoznat napitak"),
                Triple("🏠 Porodični stan", GeoPoint(44.8025, 20.4481), "Sestra ga pronašla"),
                Triple("🖥️ Markov računar", GeoPoint(44.7900, 20.4680), "Pretrage: 'proširena svest'..."),
                Triple("🏥 Bolnica", GeoPoint(44.8020, 20.4780), "Lekari bez objašnjenja")
            )

            // Dodaj markere
            for ((naziv, geo, opis) in lokacije) {
                val marker = Marker(mapView).apply {
                    position = geo
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = naziv
                    subDescription = opis
                    isDraggable = false
                }

                mapView.overlays.add(marker)
            }



            mapView
        },
        modifier = Modifier
            .fillMaxSize().padding(20.dp)
    )
}
