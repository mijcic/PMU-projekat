package rs.ac.bg.etf.projekat.phone

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel

@Composable
fun PhonePage(myViewModel: MyViewModel, onClickNav: (String)-> Unit) {
    LaunchedEffect(Unit) {
        Log.d("Telefon ","OVDe")
        val zad =myViewModel.selectTelefonZadatakViewModel()
        Log.d("Telefon ",zad.toString())
        Log.d("Telefon ",zad?.telefonId.toString())
        delay(3000)
        if (zad != null) {
            myViewModel.updateTelefonTask(zad)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        val imagePainter = painterResource(id = R.drawable.iphone_background)
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp

        Image(
            painter = imagePainter,
            contentDescription = "Background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AppIconPhotoTextItem("WhatsApp", R.drawable.iphone_whatsapp_icon, { onClickNav("destinationWhatsAppPage") })
                AppIconPhotoTextItem("Notes", R.drawable.iphone_notes_icon, { onClickNav("destinationNotesPage")} )
                AppIconPhotoTextItem("Settings", R.drawable.iphone_settings_icon, { onClickNav("destinationPhoneSettingsPage")})
                AppIconPhotoTextItem("", R.drawable.iphone_empty_icon, {})
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenWidth / 4.5).dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.White.copy(alpha = 0.3f))
                        .graphicsLayer {
                            renderEffect = BlurEffect(20f, 20f)
                        }
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIconPhotoItem(R.drawable.iphone_phone_icon, Modifier.weight(1f), { onClickNav("destinationCallsPage") })
                    AppIconPhotoItem(R.drawable.iphone_photos_icon, Modifier.weight(1f), { onClickNav("destinationGalleryPage") })
                    AppIconPhotoItem(R.drawable.iphone_message_icon, Modifier.weight(1f), { onClickNav("destinationMessagesPage") })
                    AppIconPhotoItem(R.drawable.iphone_contacts_icon, Modifier.weight(1f), { onClickNav("destinationPhonebookPage") })
                }
            }
        }
    }
}

@Composable
fun AppIconPhotoTextItem(iconName: String, iconPicture: Int, onClickFunction: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width((screenWidth / 4).dp)
    ) {
        Image(
            painter = painterResource(id = iconPicture),
            contentDescription = "Image",
            modifier = Modifier
                .size((screenWidth / 7).dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClickFunction() }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = iconName,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun AppIconPhotoItem(iconPicture: Int, modifier: Modifier = Modifier, onClickFunction: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = iconPicture),
            contentDescription = "Image",
            modifier = Modifier
                .size((screenWidth / 7).dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClickFunction() }
        )
    }
}
