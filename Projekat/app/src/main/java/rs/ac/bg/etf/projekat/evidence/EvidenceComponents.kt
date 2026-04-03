package rs.ac.bg.etf.projekat.evidence

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState


@Composable
fun ImageRowWithCenterFocus(imageResIds: List<Int>) {
    require(imageResIds.size == 5) { "Potrebno je tačno 5 slika" }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val spacing = 8.dp

    val totalSpacing = spacing * 4
    val availableWidth = screenWidth - totalSpacing

    val smallWeight = 1f
    val mediumWeight = 1.5f
    val largeWeight = 2f
    val totalWeight = smallWeight * 2 + mediumWeight * 2 + largeWeight

    val smallSize = availableWidth * (smallWeight / totalWeight)
    val mediumSize = availableWidth * (mediumWeight / totalWeight)
    val largeSize = availableWidth * (largeWeight / totalWeight)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leva mala
        Image(
            painter = painterResource(id = imageResIds[0]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(smallSize)
        )

        // Leva srednja
        Image(
            painter = painterResource(id = imageResIds[1]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(mediumSize)
        )

        Image(
            painter = painterResource(id = imageResIds[2]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(largeSize)
        )

        // Desna srednja
        Image(
            painter = painterResource(id = imageResIds[3]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(mediumSize)
        )

        // Desna mala
        Image(
            painter = painterResource(id = imageResIds[4]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(smallSize)
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EvidenceTextColumn(pagerState: PagerState, modifier: Modifier, onClick:() -> Unit){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = "Evidences",
            color = if (pagerState.currentPage == 0) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold
        )
        if (pagerState.currentPage == 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier.size(8.dp).background(Color.White, shape = CircleShape)
            )
        }
    }
}
