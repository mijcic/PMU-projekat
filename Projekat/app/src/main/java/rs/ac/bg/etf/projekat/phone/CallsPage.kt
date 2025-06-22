package rs.ac.bg.etf.projekat.phone

import android.content.res.Resources
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.GenericFontFamily
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.BottomNavigationBar
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.OneCallR
import java.time.Instant

@Composable
fun CallsPage(navController: NavController) {
    var selectedButton by remember { mutableStateOf(1) }
    val font = FontFamily.SansSerif

    val realmViewModel: RealmViewModel = hiltViewModel()
    var calls by remember { mutableStateOf<List<OneCallR>>(emptyList()) }

    LaunchedEffect(Unit) {
        calls = realmViewModel.getAllCalls()
            ?.sortedByDescending { call ->
                call.datum?.let {
                    Instant.ofEpochSecond(it.epochSeconds, it.nanosecondsOfSecond.toLong())
                }
            } ?: emptyList()
    }

    val filteredCalls = if (selectedButton == 1) calls else calls.filter { it.propusten == true }

    val destinations = listOf(
        navDestination(
            route = "destinationCallsPage",
            icon = R.drawable.clock_fill,
            label = "Recents"
        ),
        navDestination(
            route = "destinationPhonebookPage",
            icon = R.drawable.person_circle,
            label = "Contacts"
        ),
        navDestination(
            route = "destinationKeypadPage",
            icon = R.drawable.grid_3x3_gap,
            label = "Keypad"
        )
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, destinations = destinations)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 0.dp
                )
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            CallFilterToggleButtons(
                selectedButton = selectedButton,
                onSelectedChange = { selectedButton = it },
                font = font
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recents",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 16.dp),
                fontFamily = font
            )

            CallsList(
                filteredCalls = filteredCalls,
                navController = navController,
                font = font
            )
        }
    }
}

@Composable
fun AllOrMissedCallsButton(
    onClickFunction: () -> Unit,
    buttonIndex: Int,
    selectedButton: Int,
    text: String,
    font: GenericFontFamily
) {
    Button(
        onClick = onClickFunction,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedButton == buttonIndex) Color.White else colorResource(
                R.color.light_gray
            )
        ),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .width(60.dp)
            .height(25.dp)
            .clip(RoundedCornerShape(4.dp))
            .then(
                if (selectedButton == buttonIndex) {
                    Modifier.shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(4.dp),
                        clip = true
                    )
                } else Modifier
            )
    ) {
        Text(text, fontSize = 12.sp, color = Color.Black, fontFamily = font)
    }
}

@Composable
fun CallFilterToggleButtons(
    selectedButton: Int,
    onSelectedChange: (Int) -> Unit,
    font: GenericFontFamily
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .background(colorResource(R.color.light_gray), shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 3.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            AllOrMissedCallsButton(
                onClickFunction = { onSelectedChange(1) },
                buttonIndex = 1,
                selectedButton = selectedButton,
                text = "All",
                font = font
            )

            AllOrMissedCallsButton(
                onClickFunction = { onSelectedChange(2) },
                buttonIndex = 2,
                selectedButton = selectedButton,
                text = "Missed",
                font = font
            )
        }
    }
}

@Composable
fun CallerData(
    call: OneCallR,
    font: GenericFontFamily
) {
    val context = LocalContext.current
    val imageResId = call.kontakt?.slika ?: -1
    val validImageResId =
        try {
            context.resources.getResourceName(imageResId)
            imageResId
        }
        catch (e: Resources.NotFoundException) {
            R.drawable.no_account
        }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
    ) {
        Image(
            painter = painterResource(id = validImageResId),
            contentDescription = "Profile picture",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
    }

    Spacer(modifier = Modifier.width(12.dp))

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            call.kontakt?.ime ?: call.kontakt?.broj ?: "No Caller ID",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = if (call.propusten == true) Color.Red else Color.Black,
            fontFamily = font
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(
                    id = if (call.dolazni == false) R.drawable.outgoing_call
                    else R.drawable.ingoing_call
                ),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.LightGray
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("mobile", color = Color.Gray, fontSize = 14.sp, fontFamily = font)
        }
    }

    Spacer(modifier = Modifier.height(8.dp)) // ili potpuno izbaci

    Text(
        realmInstantToTimeString(call.datum),
        color = Color.Gray,
        fontSize = 14.sp,
        fontFamily = font,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
    )
}

@Composable
fun CallsList(
    filteredCalls: List<OneCallR>?,
    navController: NavController,
    font: GenericFontFamily
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        filteredCalls?.forEach { call ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val name = call.kontakt?.ime ?: "No Caller ID"
                        val phoneNumber = call.kontakt?.broj ?: ""
                        val photo = call.kontakt?.slika ?: R.drawable.no_account

                        val encodedName = Uri.encode(name)

                        navController.navigate("destinationOneContactPage/$encodedName/$phoneNumber/$photo")
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CallerData(call, font)
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f)
            )
        }
    }
}