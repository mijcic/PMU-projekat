package rs.ac.bg.etf.projekat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel

@Composable
fun MainScreen2(navController: NavController,viewModel: MyViewModel) {
    var explanationOn by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        MainScreen2BackgroundWithOverlay(
            imageRes = R.drawable.main_screen_background
        )

        MainScreen2Content(
            explanationOn = explanationOn,
            onToggleExplanation = { explanationOn = !explanationOn },
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun MainScreen2BackgroundWithOverlay(imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))
}

@Composable
fun MainScreen2Content(
    explanationOn: Boolean,
    onToggleExplanation: () -> Unit,
    navController: NavController,
    viewModel: MyViewModel
) {
    val uiStateUser by viewModel.uiStateUser.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainScreen2TopNavigationBar(
            onProfileClick = {

                    if (uiStateUser.korisnickoIme!=null){
                        navController.navigate("destinationUserProfile")
                    }else{
                        navController.navigate("destinationLoginPage")
                    }
                 },
            onSettingsClick = { navController.navigate("destinationSettingsPage") }
        )

        WelcomeMessage(modifier = Modifier.weight(0.3f))

        ExplanationToggleButton(
            explanationOn = explanationOn,
            onToggle = onToggleExplanation
        )

        MainScreen2AnimatedVisibility(explanationOn)

        Spacer(modifier = Modifier.weight(1f))

        MainScreen2BottomButtons(
            onPlayClick = { navController.navigate("destinationCardsPage") },
            onScoreClick = { navController.navigate("destinationScorePage") }
        )
    }
}

@Composable
fun MainScreen2TopNavigationBar(
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TopNavIconButton(
            icon = Icons.Default.Person,
            contentDescription = "User",
            onClick = onProfileClick
        )

        TopNavIconButton(
            icon = Icons.Default.Settings,
            contentDescription = "Settings",
            onClick = onSettingsClick
        )
    }
}

@Composable
private fun TopNavIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.shadow(
            elevation = 20.dp,
            shape = RoundedCornerShape(15.dp),
            clip = false
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun WelcomeMessage(modifier: Modifier) {
    Spacer(modifier = modifier)
    Text(
        text = "Welcome, detective!",
        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
        fontSize = 35.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        style = TextStyle(
            shadow = Shadow(Color.Black, Offset(10f, 10f), blurRadius = 20f)
        )
    )
}
@Composable
fun ExplanationToggleButton(explanationOn: Boolean, onToggle: () -> Unit) {
    Button(
        onClick = onToggle,
        colors = ButtonDefaults.buttonColors(Color(0XFFA99367)),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth(0.8f).padding(10.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(15.dp),
                clip = false)
    ) {
        ExplanationButtonContent(explanationOn)
    }
}

@Composable
private fun ExplanationButtonContent(explanationOn: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ExplanationText(explanationOn)
        ExplanationIcon(explanationOn)
    }
}

@Composable
private fun ExplanationText(explanationOn: Boolean) {
    val text = if (!explanationOn) "View a detailed explanation of the game" else "Hide a detailed explanation"
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
        fontSize = 19.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(5f, 5f),
                blurRadius = 10f
            )
        )
    )
}

@Composable
private fun ExplanationIcon(explanationOn: Boolean) {
    val iconRes = if (!explanationOn) R.drawable.arrow_down else R.drawable.arrow_up
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = if (!explanationOn) "Expand" else "Collapse",
        tint = Color.White,
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun MainScreen2AnimatedVisibility(explanationOn: Boolean){
    AnimatedVisibility(visible = explanationOn) {
        Box(
            modifier = Modifier.fillMaxWidth(0.9f).border(2.dp, Color.White)
                .background(Color.Black.copy(alpha = 0.5f)).padding(10.dp)
        ) {
            Text(
                text = "Whodunit is an interactive detective game where you choose your own path to solving mysteries! As a skilled detective, you gather clues and interrogate suspicious characters. Every decision shapes the investigation – will you follow your instincts or rely on the evidence? Each puzzle is key to the truth, but be careful – one wrong move could lead to a dead end! Will you uncover the truth or remain trapped in a web of lies? The choice is yours!",
                fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
                fontSize = 15.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MainScreen2BottomButtons(
    onPlayClick: () -> Unit,
    onScoreClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PlayGameButton(onClick = onPlayClick)
        ScoreButton(onClick = onScoreClick)
    }
}

@Composable
fun PlayGameButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color(0XFFA99367)),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.padding(10.dp)
            .wrapContentWidth().elevatedIconButton()
    ) {
        Text(
            text = "PLAY GAME",
            fontFamily = FontFamily(Font(R.font.special_elite, FontWeight.ExtraBold)),
            fontSize = 25.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(Color.Black, Offset(5f, 5f), 10f)
            )
        )
    }
}

@Composable
fun ScoreButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(12.dp).elevatedIconButton()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.trophy_fill),
            contentDescription = "Trophy",
            tint = colorResource(id = R.color.golden_yellow),
            modifier = Modifier.size(30.dp)
        )
    }
}

fun Modifier.elevatedIconButton(): Modifier {
    return this.shadow(20.dp, RoundedCornerShape(15.dp), clip = false)
}