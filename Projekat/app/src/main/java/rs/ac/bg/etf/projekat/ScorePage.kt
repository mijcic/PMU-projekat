package rs.ac.bg.etf.projekat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel

@Composable
fun ScorePage(navController: NavController,myViewModel: MyViewModel){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val uiStateScoreKorisnika by myViewModel.uiStateScoreKorisnika.collectAsState()
    val uiStateScoreKorisnikaList by myViewModel.uiStateScoreKorisnikaList.collectAsState()

    LaunchedEffect(Unit){
        myViewModel.scoreKorisnika()
        myViewModel.scoreKorisnikaList()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        ScoreBackground()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding((screenHeight/40).dp))

            ScorePageHeader(
                screenHeight = screenHeight,
                navController = navController
            )

            Spacer(modifier = Modifier.padding((screenHeight/50).dp))

            UserScoreCard(screenWidth = screenWidth,myViewModel)

            Spacer(modifier = Modifier.padding((screenHeight/40).dp))

            ScorePageDivider()

            if (uiStateScoreKorisnika.scoreList!=null){
                LazyColumn {
                    itemsIndexed(uiStateScoreKorisnika.scoreList!!){ index, items->
                        Row{
                            Spacer(modifier = Modifier.width(22.dp))
                            Text(
                                text = "${index + 1}.",
                                style = TextStyle(color = Color.White),
                                modifier = Modifier.padding(top=15.dp)
                            )
                            Spacer(modifier = Modifier.width(42.dp))
                            /*Image(
                                painter = painterResource(id = R.drawable.abuse),
                                contentDescription = "User Avatar",
                                modifier = Modifier.size(40.dp)
                            )*/

                            Spacer(modifier = Modifier.width(42.dp))

                            Text(
                                text = items.korisnickoIme,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.special_elite)),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                ),
                                modifier = Modifier.padding(top=15.dp)
                            )
                            Spacer(modifier = Modifier.width(42.dp))
                            Text(
                                text = items.poeni.toString() + " XP",
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.special_elite)),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                ),
                                modifier = Modifier.padding(top=15.dp)
                            )
                        }

                        ScorePageDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreBackground() {
    Box {
        Image(
            painter = painterResource(id = R.drawable.score_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(modifier = Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.75f)))
    }
}

@Composable
fun ScorePageHeader(screenHeight: Int, navController: NavController) {
    Icon(
        painter = painterResource(id = R.drawable.trophy_fill),
        contentDescription = "Trophy",
        tint = colorResource(id = R.color.golden_yellow),
        modifier = Modifier.size(45.dp)
    )

    Spacer(modifier = Modifier.padding((screenHeight/50).dp))

    Text(
        text = "If you want to keep track of your score",
        modifier = Modifier.padding(horizontal = 16.dp),
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.special_elite)),
            color = Color.White,
            fontSize = 18.sp,
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = "Login",
        modifier = Modifier.padding(horizontal = 16.dp).clickable {
            navController.navigate("destinationLoginPage")
        },
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.special_elite)),
            color = Color.White,
            fontSize = 18.sp,
            textDecoration = TextDecoration.Underline
        )
    )
}

@Composable
fun UserScoreCard(screenWidth: Int,myViewModel: MyViewModel) {
    val uiStateUser by myViewModel.uiStateUser.collectAsState()

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth()
            .padding(start = (screenWidth/4).dp, end =(screenWidth/4).dp )
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.light_gray))
            .padding((screenWidth/16).dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Your score: ${uiStateUser.mesto ?: ""}",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    color = Color.White,
                    fontSize = 18.sp,
                ),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = painterResource(id = R.drawable.abuse),
                contentDescription = "User Avatar",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${uiStateUser.korisnickoIme ?: ""}",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    color = Color.White,
                    fontSize = 14.sp,
                ),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "${uiStateUser.poeni ?: ""} XP",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    color = Color.White,
                    fontSize = 25.sp,
                ),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ScorePageDivider(){
    Divider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color.Gray,
        thickness = 3.dp
    )
}