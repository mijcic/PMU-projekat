package rs.ac.bg.etf.projekat.murder.witnessInterview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R

@Composable
fun WitnessesFinishInvestigationButton(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onFinished,
        modifier = modifier.widthIn(min = 180.dp)
            .wrapContentWidth().wrapContentHeight()
            .clip(RoundedCornerShape(5.dp))
            .shadow(4.dp, RoundedCornerShape(5.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_purple)),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Finish",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
            )
            Text(
                text = "investigation",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)))
            )
        }
    }
}