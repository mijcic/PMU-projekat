package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.stZlocinR
import rs.ac.bg.etf.projekat.data.retrofit.models.AlibiData
import rs.ac.bg.etf.projekat.data.retrofit.models.DokazData
import rs.ac.bg.etf.projekat.data.retrofit.models.MisijaData
import rs.ac.bg.etf.projekat.data.retrofit.models.MotivData
import rs.ac.bg.etf.projekat.data.retrofit.models.OsumnjicenData
import rs.ac.bg.etf.projekat.data.retrofit.models.SvedokData
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinData
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.ZrtvaData
import java.util.Date

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MissionPage(image:Int, title:String, date: String, place: String, description: String, navController: NavController,realmViewModel: RealmViewModel){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val viewModel:MyViewModel= hiltViewModel()

    LaunchedEffect(viewModel.uiState.value.zlocin) {
        InsertData(viewModel)
    }

    val crimeData = realmViewModel.uiStateCrimeData.collectAsState()
    Log.d("CrimeData2", crimeData.value.toString())

    Surface(
        modifier = Modifier.fillMaxSize().background(Color(0xFF233331)).padding(top=22.dp).clickable {
            navController.navigate(destinationOfficePage.route)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF233331))
                .padding(top=(screenWidth/8).dp).padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            crimeData.value.title?.let {
                Text(
                    text = it,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            crimeData.value.date?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = colorResource(id = R.color.mission_light_gray),
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            crimeData.value.place?.let {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Image (
                painter = painterResource(id = image),
                contentDescription = "Image",
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),
            )

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.mission_overlay_color), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                crimeData.value.description?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.special_elite)
                            )
                        )
                    )
                }
            }
        }
    }
}