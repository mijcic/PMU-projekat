package rs.ac.bg.etf.projekat.mysteriousSymptoms.location

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR

@Composable
fun MapViewContainer(locations: List<LokacijeIstrageR>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE))

            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(13.0)
            }
        },
        modifier = modifier,
        update = { mapView ->
            mapView.overlays.clear()

            if (locations.isNotEmpty()) {
                val firstLoc = locations.first()
                mapView.controller.animateTo(GeoPoint(firstLoc.geoTackaALatitude, firstLoc.geoTackaALongitude))

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
            mapView.invalidate()
        }
    )
}