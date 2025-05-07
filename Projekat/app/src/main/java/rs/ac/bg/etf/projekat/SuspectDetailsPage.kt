package rs.ac.bg.etf.projekat

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.xr.compose.testing.toDp
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.BeleskaR
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

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SuspectDetailsPage(idOsoba: Int, image: Int, title: String, navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val realmViewModel: RealmViewModel = hiltViewModel()
    var motiveAlibiStatus by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        motiveAlibiStatus = realmViewModel.getMotiveAlibiStatus(idOsoba) ?: emptyList()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF233331))
            .padding(top = 22.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF233331))
                .padding(top = (screenWidth / 8).dp)
                .animateContentSize(animationSpec = tween(durationMillis = 600)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.White,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.special_elite)),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = image),
                contentDescription = "Suspect Image",
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
                    .shadow(8.dp, CircleShape)
            )

            Spacer(modifier = Modifier.height(20.dp))

            var isButtonClicked by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    isButtonClicked = !isButtonClicked
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFB8860B)),
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 16.dp)
                    .heightIn(min = 50.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (!isButtonClicked) "If you want more information about the suspect..."
                        else "If you want less information about the suspect...",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.special_elite)),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                    )
                    Icon(
                        painter = painterResource(id = if (!isButtonClicked) R.drawable.arrow_down else R.drawable.arrow_up),
                        contentDescription = "Arrow",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(20.dp)
                            .graphicsLayer(
                                scaleX = 1.7f,
                                scaleY = 1.7f
                            )
                    )
                }
            }

            AnimatedVisibility(
                visible = isButtonClicked,
                enter = expandVertically(animationSpec = tween(900)),
                exit = shrinkVertically(animationSpec = tween(900))
            ) {
                if (motiveAlibiStatus.isNotEmpty()) {
                    CustomTable(motiveAlibiStatus)
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {
                    navController.navigate(destinationSuspectsInterviewPage.route + "/" + title)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF1F2D2D)),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    text = "Interrogate the Suspect",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.special_elite)),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun CustomTable(motiveAlibiStatus: List<String>) {
//    val tableData = listOf(
//        listOf("Motive", motiveAlibiStatus.getOrNull(0).takeUnless { it.isNullOrBlank() } ?: "?"),
//        listOf("Alibi", motiveAlibiStatus.getOrNull(1).takeUnless { it.isNullOrBlank() } ?: "?"),
//        listOf("Status", motiveAlibiStatus.getOrNull(2).takeUnless { it.isNullOrBlank() } ?: "?")
//    )

    val tableData = listOf(
        listOf("Motive", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
        listOf("Alibi", motiveAlibiStatus.getOrNull(1).takeUnless { it.isNullOrBlank() } ?: "?"),
        listOf("Status", motiveAlibiStatus.getOrNull(2).takeUnless { it.isNullOrBlank() } ?: "?")
    )

    // PAPIR

//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 0.dp)
//            .heightIn(min = 350.dp, max = 500.dp)
//            .clip(RoundedCornerShape(16.dp)),
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.paper_with_data),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(400.dp)
//        )
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.Center)
//                .fillMaxWidth(0.7f)
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
//        ) {
//            Text(
//                text = "Information about the suspect",
//                color = Color(0xFF000070),
//                fontSize = 19.sp,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            tableData.forEach { (label, value) ->
//                Text(
//                    text = "$label: $value",
//                    color = Color(0xFF000070),
//                    fontSize = 17.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//    }

    // KRAJ PAPIR

    // TABELA

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .background(Color.Transparent)
    ) {
        tableData.forEach { row ->
            var rowHeight by remember { mutableStateOf(0) }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                row.forEach { cellText ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color(0xFFB8860B))
                            .background(Color.White)
                            .padding(8.dp)
                            .onGloballyPositioned { coordinates ->
                                rowHeight = maxOf(rowHeight, coordinates.size.height)
                            }
                            .heightIn(min = with(LocalDensity.current) { rowHeight.toDp() }),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cellText,
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.special_elite)),
                                //fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                        )
                    }
                }
            }
        }
    }
}
