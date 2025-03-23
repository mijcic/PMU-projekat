package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MissionPage(image:Int,title:String,navController: NavController){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val viewModel:MyViewModel= hiltViewModel()
    val uistate by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.uiState.value.zlocin) {
        //viewModel.getAllData()
        viewModel.saveData()
    }

    Surface(
        modifier = Modifier.fillMaxSize().background(Color(0xFF233331)).padding(top=22.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFF233331))
                .padding(top=(screenWidth/8).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.White,
                style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.special_elite)
                    ),
                    fontSize = 26.sp,
                    color = Color.Black
                ),
            )
            Spacer(modifier = Modifier.padding((screenWidth/16).dp))

            Image(
                painter = painterResource(id = image),
                contentDescription = "Image",
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),
            )
            Spacer(modifier = Modifier.padding((screenWidth/15).dp))

            Box(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = "“Detective, the murder took place at 2:15 AM. \n" +
                            "Neighbors reported hearing a scream \n" +
                            "followed by a loud thud, \n" +
                            "then everything went quiet. \n" +
                            "\n" +
                            "When we arrived, Richard Hayes was found \n" +
                            "dead in his living room, stabbed several \n" +
                            "times in the chest. \n" +
                            "\n" +
                            "There were no signs of forced entry, so it’s \n" +
                            "likely the killer was someone he knew.\n" +
                            "His wife, Evelyn Hayes, was reportedly out \n" +
                            "of town on business, but we’re still verifying \n" +
                            "her alibi. A strange note was found on his \n" +
                            "desk, reading: ‘It was always you.’ No signs \n" +
                            "of a robbery, so this could be personal. We’re \n" +
                            "waiting for forensics to confirm details, \n" +
                            "but it’s clear this wasn’t a random attack.”",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        ),
                    )
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.White.copy(alpha = 0.1f))
                )
            }
        }
    }
}