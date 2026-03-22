package rs.ac.bg.etf.projekat.murder.office

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.projekat.R

@Composable
fun OfficePage(
    onDestinationMapPageClick: () -> Unit,
    onDestinationPhonePageClick: () -> Unit,
    onDestinationSuspectsPageClick: () -> Unit,
    onDestinationWitnessesPageClick: () -> Unit,
    onDestinationEvidencePageClick: () -> Unit,
    onLoadTasks: () -> Unit,
    onSelectPhoneTasks: () -> Unit,
    onLoadEvidences: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onLoadTasks()
                    onDestinationMapPageClick()
                },
                containerColor = Color(0XFFA99367),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .size(60.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.planning),
                    contentDescription = "Tasks",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                var imageSize by remember { mutableStateOf(IntSize.Zero) }

                Image(
                    painter = painterResource(id = R.drawable.office),
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { coordinates ->
                             imageSize = coordinates.size
                        }
                        .graphicsLayer {
                            renderEffect = BlurEffect(3f, 3f)
                        },
                    contentScale = ContentScale.Crop
                )

                Box(modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.3f)))

                TopicForInvestigation(
                    imageSize = imageSize,
                    x = 0.465f,
                    y = 0.74f,
                    text = "Victim's Phone",
                    onClick = {
                        onSelectPhoneTasks()
                        onDestinationPhonePageClick()
                    }
                )

                TopicForInvestigation(
                    imageSize = imageSize,
                    x = 0.35f,
                    y = 0.3f,
                    text = "Suspects",
                    onClick = onDestinationSuspectsPageClick
                )

                TopicForInvestigation(
                    imageSize = imageSize,
                    x = 0.045f,
                    y = 0.74f,
                    text = "Witnesses",
                    onClick = onDestinationWitnessesPageClick
                )

                TopicForInvestigation(
                    imageSize = imageSize,
                    x = 0.2f,
                    y = 0.9f,
                    text = "Evidences",
                    onClick = {
                        onLoadEvidences()
                        onDestinationEvidencePageClick()
                    }
                )

                DescriptionForDetective(
                    text = "Detective, this is your office. Choose the topic you want to investigate."
                )
            }
        }
    )
}

@Composable
fun TopicForInvestigation(
    imageSize: IntSize,
    x: Float,
    y: Float,
    text: String,
    onClick: () -> Unit
) {
    if (imageSize.width > 0 && imageSize.height > 0) {
        val xOffset = (imageSize.width * x).toInt()
        val yOffset = (imageSize.height * y).toInt()

        Text(
            text = text,
            modifier = Modifier
                .offset { IntOffset(x = xOffset, y = yOffset) }
                .padding(8.dp)
                .clickable { onClick() },
            fontSize = 17.sp,
            color = Color.White,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.special_elite)),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black,
                    blurRadius = 8f
                )
            )
        )
    }
}