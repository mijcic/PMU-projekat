package rs.ac.bg.etf.projekat.phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.GalleryR

@Composable
fun GalleryPage(navController: NavController) {
    val font = FontFamily.SansSerif
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageSize = (screenWidth - 48.dp) / 4

    val realmViewModel: RealmViewModel = hiltViewModel()
    var images by remember { mutableStateOf<List<GalleryR>>(emptyList()) }

    LaunchedEffect(Unit) {
        images = realmViewModel.getAllGalleryPhotos()!!
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)
            .padding(top = 30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "All photos",
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        val listState = rememberLazyGridState()

        LaunchedEffect(images) {
            if (images.isNotEmpty()) {
                listState.scrollToItem(0)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            state = listState,
            reverseLayout = true
        ) {
            items(images.size) { index ->
                Image(
                    painter = painterResource(images[index]?.slika ?: R.drawable.no_account),
                    contentDescription = "Image $index",
                    modifier = Modifier
                        .size(imageSize)
                        .border(0.5.dp, Color.White)
                        .clickable {
                            val slika = images[index].slika
                            val datum = realmInstantForWA(images[index].datum)
                            val mesto = images[index].mesto

                            navController.navigate("destinationOnePhotoPage/$slika/$datum/$mesto")
                        }
                )
            }
        }
    }
}