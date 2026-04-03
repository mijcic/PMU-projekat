package rs.ac.bg.etf.projekat.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R

@Composable
fun SettingsAppVersion(modifier: Modifier){
    Divider(color = Color.Gray)
    Text(
        text = "App Version: 1.0.0",
        fontSize = 12.sp,
        color = Color.DarkGray,
        modifier = modifier
    )
}


@Composable
fun SettingRow(label: String, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 18.sp,
                color = Color.White
            )
        )
        content()
    }
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .shadow(10.dp, RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.light_gray))
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Composable
fun SettingsText(){
    Text(
        text = "Settings",
        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
        fontSize = 32.sp,
        color = Color.Black,
        style = TextStyle(
            shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 2f)
        )
    )
}