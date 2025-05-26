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
import androidx.compose.foundation.lazy.items
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

    LaunchedEffect(uiStateScoreKorisnika.scoreList){
        myViewModel.scoreKorisnika()
    }

    LaunchedEffect(Unit){
        myViewModel.scoreKorisnika()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.score_background), // zameni sa tvojom slikom
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.75f))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
                //.background(Color(0xFF2F271A))
                //.background(Color(0xFF262626))
                //.background(Color(0xFF1A2B2D))
        ) {
            Spacer(modifier = Modifier.padding((screenHeight/40).dp))

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
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    color = Color.White,
                    fontSize = 18.sp,
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Login",
                modifier = Modifier.padding(horizontal = 16.dp).clickable {
                    navController.navigate("destinationLoginPage")
                },
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    color = Color.White,
                    fontSize = 18.sp,
                    textDecoration = TextDecoration.Underline
                ),
            )

            Spacer(modifier = Modifier.padding((screenHeight/50).dp))

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = (screenWidth/4).dp, end =(screenWidth/4).dp )
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(20.dp),
                        clip = false
                    )
                    .clip(RoundedCornerShape(20.dp))
                    //.background(Color(0XFFD1D5D8))
                    .background(colorResource(id = R.color.light_gray))
                    .padding((screenWidth/16).dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Your score: 100",
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
                        text = "real_detective_101",
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
                        text = "257 XP",
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
            Spacer(modifier = Modifier.padding((screenHeight/40).dp))

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Gray,
                thickness = 3.dp
            )
            if (uiStateScoreKorisnika.scoreList!=null){
                LazyColumn {
                    items(uiStateScoreKorisnika.scoreList!!){ items->
                        Row{
                            Spacer(modifier = Modifier.width(22.dp))
                            Text(
                                text = items.mesto.toString()+"  ",
                                style = TextStyle(color = Color.White),
                                modifier = Modifier.padding(top=15.dp)
                            )
                            Spacer(modifier = Modifier.width(42.dp))
                            Image(
                                painter = painterResource(id = R.drawable.abuse),
                                contentDescription = "User Avatar",
                                modifier = Modifier.size(40.dp)
                            )

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
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.Gray,
                            thickness = 3.dp
                        )
                    }
                }
            }
        }
    }
}