package rs.ac.bg.etf.projekat

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SettingsPage(navController: NavController){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    var musicVolume by remember { mutableStateOf(0.5f) }
    var isNotificationEnabled by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.background(Color(0xFF34452F)).padding(top =(screenHeight/10).dp )) {

            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .border(BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(16.dp))
                    .background(Color(0xFF2F4825), shape = RoundedCornerShape(11.dp))
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SETTINGS",
                        color = Color.White, // Tekst boja
                        modifier = Modifier.padding(8.dp),
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontSize = 32.sp,
                            color = Color.Black
                        ),
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row{
                            Text(
                                text = "Music",
                                color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.special_elite)),
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                ),
                                modifier = Modifier.padding(top=15.dp)
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            Slider(
                                value = musicVolume,
                                onValueChange = { musicVolume = it },
                                valueRange = 0f..1f,
                                modifier = Modifier.fillMaxWidth(),
                                steps = 8
                            )
                        }
                        Row{
                            Text(
                                text = "Notification",
                                color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.special_elite)),
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                ),
                                modifier = Modifier.padding(top=15.dp)
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            Checkbox(
                                checked = isNotificationEnabled,
                                onCheckedChange = { isNotificationEnabled = it },
                                colors = androidx.compose.material3.CheckboxDefaults.colors(checkedColor = Color.White)
                            )
                        }
                        Row{
                            Text(
                                text = "Language",
                                color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.special_elite)),
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                ),
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            Text(
                                text = "ENG",
                                color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.special_elite)),
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                ),
                            )
                        }

                        Row {
                            Text(
                                text = "Help",
                                color = Color.White,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.special_elite)),
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                ),modifier = Modifier.padding(top=27.dp)
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            Button(
                                onClick = {
                                },
                                modifier = Modifier.padding(8.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Send a Message",
                                    color = Color.Black,
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.special_elite)),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}