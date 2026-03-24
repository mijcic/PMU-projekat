package rs.ac.bg.etf.projekat.mysteriousSymptoms.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LocationOverlayHeader(modifier: Modifier, locationCount: Int) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .background(Color(0xAA000000), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Mysterious Locations",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "$locationCount locations found",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun LocationBackButton(modifier: Modifier, onBack: () -> Unit) {
    IconButton(
        onClick = onBack,
        modifier = modifier
            .padding(16.dp)
            .background(Color(0xAA000000), shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
        )
    }
}