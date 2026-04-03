package rs.ac.bg.etf.projekat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun Background(image: Int, desc: String?, modifier: Modifier, alpha: Float){
    Image(
        painter = painterResource(id = image),
        contentDescription = desc,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = alpha)))
}