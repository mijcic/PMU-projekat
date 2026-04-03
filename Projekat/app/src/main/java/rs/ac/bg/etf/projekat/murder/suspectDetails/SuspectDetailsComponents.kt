package rs.ac.bg.etf.projekat.murder.suspectDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R


@Composable
fun oneRowAboutSuspect(tekst1: String, tekst2: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.width(IntrinsicSize.Min).weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = tekst1,
                color = Color.Black,
                maxLines = 100,
                softWrap = true,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    fontSize = 17.sp
                )
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = tekst2,
                color = Color.Black,
                maxLines = 100,
                softWrap = true,
                textAlign = TextAlign.End,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    fontSize = 17.sp,
                )
            )
        }
    }
}

@Composable
fun SuspectInfoFun(
    image: Int,
    title: String,
    tableData: List<List<String>>
) {
    Text(
        text = "Suspect Info",
        color = Color.Black,
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.special_elite)),
            fontSize = 18.sp
        )
    )

    Spacer(modifier = Modifier.height(20.dp))

    Image(
        painter = painterResource(id = image),
        contentDescription = "Suspect Image",
        modifier = Modifier
            .size(130.dp)
            .clip(CircleShape)
            .border(1.5.dp, Color.Black, CircleShape)
            .shadow(8.dp, CircleShape)
    )

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = title,
        color = Color.Black,
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.special_elite)),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
    )

    Spacer(modifier = Modifier.height(25.dp))

    if (tableData.isNotEmpty()) {
        oneRowAboutSuspect("Motive", tableData.get(0).get(1))
        Spacer(modifier = Modifier.height(5.dp))
        Divider(color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(5.dp))
        oneRowAboutSuspect("Alibi", tableData.get(1).get(1))
        Spacer(modifier = Modifier.height(5.dp))
        Divider(color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(5.dp))
        oneRowAboutSuspect("Status", tableData.get(2).get(1))
    }
}

@Composable
fun InterrogateButton(
    text: String,
    onDestinationSuspectsInterviewPage: () -> Unit
) {
    Button(
        onClick = onDestinationSuspectsInterviewPage,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(Color(0XFFA99367)),
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(horizontal = 16.dp)
            .height(50.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}