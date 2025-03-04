package rs.ac.bg.etf.projekat

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ScorePage(navController: NavController){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().background(Color(0xFF0A1736))
        ) {
            Spacer(modifier = Modifier.padding((screenHeight/30).dp))
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Trophy",
                modifier = Modifier.size(100.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.padding((screenHeight/40).dp))

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
            Text(
                text = "Login",
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    color = Color.White,
                    fontSize = 18.sp,
                ),
            )

            Spacer(modifier = Modifier.padding((screenHeight/40).dp))

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = (screenWidth/4).dp, end =(screenWidth/4).dp )
                    .border(1.dp, Color.White, shape = RoundedCornerShape(20.dp))
                    .padding((screenWidth/16).dp).background(Color(0xFF0A1736))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(
                        text = "Your score: 100",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            color = Color.White,
                            fontSize = 18.sp,
                        ),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Image(
                        painter = painterResource(id = R.drawable.abuse),
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))


                    Text(
                        text = "real_detective_101",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            color = Color.White,
                            fontSize = 14.sp,
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "257 XP",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            color = Color.White,
                            fontSize = 25.sp,
                        ),
                    )
                }
            }
            Spacer(modifier = Modifier.padding((screenHeight/30).dp))

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Gray,
                thickness = 3.dp
            )

            for (i in 1..5){
                Row{
                    Text(
                        text = "$i. ",
                        style = TextStyle(color = Color.White),
                        modifier = Modifier.padding(top=15.dp)
                    )
                    Spacer(modifier = Modifier.width(22.dp))
                    Image(
                        painter = painterResource(id = R.drawable.abuse),
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(22.dp))

                    Text(
                        text = "real_detective_101",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            color = Color.White,
                            fontSize = 12.sp,
                        ),
                        modifier = Modifier.padding(top=15.dp)
                    )
                    Spacer(modifier = Modifier.width(22.dp))
                    Text(
                        text = "257 XP",
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