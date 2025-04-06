package rs.ac.bg.etf.projekat


import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.RealmViewModel

@Composable
fun MapPage(navController: NavController, myViewModel: MyViewModel, realmViewModel: RealmViewModel){
    LaunchedEffect(Unit) {

    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        var textWidth by remember { mutableStateOf(0f) }
        var paddingStart by remember { mutableStateOf(0.dp) }
        val uiStateTasks by myViewModel.uiStateTasks.collectAsState()


        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.white_paper),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }

        Column(modifier = Modifier
            .align(Alignment.TopCenter).padding(top = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)

        {
            Column(modifier = Modifier) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier.padding(start = paddingStart),
            ) {
                Text(text = "Tasks", color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        ),
                        fontSize = 26.sp,
                        color = Color.Black
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    var firstFalseFound = false
                    var firstIndex=-1

                    itemsIndexed(uiStateTasks.tasks) { index, item ->
                        var isLocked = !item.uradjen
                        var isChecked = item.uradjen

                        if (item.uradjen==true){
                            isLocked = false
                            isChecked = true
                        }else {
                            if (firstFalseFound==false || firstIndex==index){
                                isLocked = false
                                isChecked = false
                                firstFalseFound=true
                                firstIndex=index
                            }
                            else{
                                isLocked = true
                                isChecked = false
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { if (!isLocked) { /* Akcija pri kliku na karticu */ } },
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isLocked) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Zaključano",
                                        modifier = Modifier.size(32.dp),
                                        tint = Color.Gray
                                    )
                                } else {
                                    Text(
                                        text = item.tekst,
                                        color = Color.Black,
                                        style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.special_elite)),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        ),
                                        modifier = Modifier
                                            .weight(2f)
                                            .padding(end = 8.dp)
                                    )

                                    Text(
                                        text = item.korak,
                                        color = Color.Gray,
                                        style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.special_elite)),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            letterSpacing = 0.5.sp
                                        ),
                                        modifier = Modifier
                                            .weight(1.5f)
                                            .padding(end = 8.dp)
                                    )
                                }

                                if (!isLocked) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { if (!isLocked) { /* Akcija kada se checkbox klikne */ } },
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(start = 8.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}