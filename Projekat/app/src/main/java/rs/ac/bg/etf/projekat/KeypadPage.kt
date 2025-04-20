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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource

data class navDestination(
    val route: String,
    val label: String,
    val icon: Int
)

@Composable
fun KeypadPage(navController: NavController) {
    var chosenNumber by remember { mutableStateOf("") }

    val destinationRecentCalls = navDestination(
        route = "destinationCallsPage",
        icon = R.drawable.clock_fill,
        label = "Recents"
    )

    val destinationContacts = navDestination(
        route = "destinationPhonebookPage",
        icon = R.drawable.person_circle,
        label = "Contacts"
    )

    val destinationKeypad = navDestination(
        route = "destinationKeypadPage",
        icon = R.drawable.grid_3x3_gap,
        label = "Keypad"
    )

    val destinations = listOf(
        destinationRecentCalls,
        destinationContacts,
        destinationKeypad
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, destinations = destinations)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = chosenNumber, fontSize = 34.sp)

            Spacer(Modifier.height((LocalConfiguration.current.screenHeightDp / 9).dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                oneButton("1", "", onClick = { chosenNumber += "1" })
                oneButton("2", "A B C", onClick = { chosenNumber += "2" })
                oneButton("3", "D E F", onClick = { chosenNumber += "3" })
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                oneButton("4", "G H I", onClick = { chosenNumber += "4" })
                oneButton("5", "J K L", onClick = { chosenNumber += "5" })
                oneButton("6", "M N O", onClick = { chosenNumber += "6" })
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                oneButton("7", "P Q R S", onClick = { chosenNumber += "7" })
                oneButton("8", "T U V", onClick = { chosenNumber += "8" })
                oneButton("9", "W X Y Z", onClick = { chosenNumber += "9" })
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                oneButton("*", "", onClick = { chosenNumber += "*" })
                oneButton("0", "+", onClick = { chosenNumber += "0" })
                oneButton("#", "", onClick = { chosenNumber += "#" })
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(80.dp)) // prazno mesto levo

                callButton()

                if (chosenNumber.isNotEmpty()) {
                    deleteButton(onClick = {
                        chosenNumber = chosenNumber.dropLast(1)
                    })
                } else {
                    Spacer(modifier = Modifier.width(80.dp)) // zadrži razmak kad dugmeta nema
                }
            }
        }
    }
}

@Composable
fun oneButton(number: String, letters: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = number, fontSize = 35.sp, fontWeight = FontWeight.Medium)
                Text(text = letters, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun callButton() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.green_call_photo),
            contentDescription = "Green phone",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
    }
}

@Composable
fun deleteButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.backspace_fill),
            contentDescription = "Delete",
            tint = Color(0xFFE0E0E0),
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    destinations: List<navDestination>
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    Column {
        Divider()

        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier.wrapContentHeight()
        ) {
            destinations.forEach { destination ->
                val isSelected = currentDestination == destination.route

                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(id = destination.icon),
                            contentDescription = "Ruta",
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(
                                if (isSelected) colorResource(R.color.iphone_blue) else Color.Gray
                            )
                        )
                    },
                    label = {
                        Text(
                            destination.label, fontSize = 12.sp,
                            color = if (isSelected) colorResource(R.color.iphone_blue) else Color.Gray
                        )
                    },
                    selected = currentDestination == destination.route,
                    onClick = {
                        if (currentDestination != destination.route) {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationRoute ?: "") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}