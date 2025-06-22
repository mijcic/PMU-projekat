package rs.ac.bg.etf.projekat.phone

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.bg.etf.projekat.BottomNavigationBar
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.realm.OneContactR
import java.util.SortedMap

@Composable
fun PhonebookPage(navController: NavController) {
    val font = FontFamily.SansSerif

    val realmViewModel: RealmViewModel = hiltViewModel()
    var contacts by remember { mutableStateOf<List<OneContactR>>(emptyList()) }

    LaunchedEffect(Unit) {
        contacts = realmViewModel.getAllContacts()!!
    }

    val groupedContacts = contacts
        .groupBy { it.ime[0].toString().uppercase() }
        .toSortedMap()

    val destinations = listOf(
        navDestination(
            route = "destinationCallsPage",
            icon = R.drawable.clock_fill,
            label = "Recents"
        ),
        navDestination(
            route = "destinationPhonebookPage",
            icon = R.drawable.person_circle,
            label = "Contacts"
        ),
        navDestination(
            route = "destinationKeypadPage",
            icon = R.drawable.grid_3x3_gap,
            label = "Keypad"
        )
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, destinations = destinations)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            AddIconButton()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Contacts",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 16.dp),
                fontFamily = font
            )

            ContactsList(
                groupedContacts = groupedContacts,
                navController = navController
            )
        }
    }
}

@Composable
fun AddIconButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 30.dp)
            .padding(horizontal = 20.dp).padding(top = 5.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        IconButton(
            onClick = { },
            modifier = Modifier.size(30.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = colorResource(R.color.iphone_blue)
            )
        }
    }
}

@Composable
fun ContactsList(groupedContacts: SortedMap<String, List<OneContactR>>, navController: NavController) {
    LazyColumn {
        groupedContacts.forEach { (letter, people) ->
            item {
                Text(
                    text = letter,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(12.dp),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }

            items(people) { person ->
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray.copy(alpha = 0.3f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        val name = person.ime ?: "No Caller ID"
                        val phoneNumber = person.broj ?: ""
                        val photo = person.slika ?: R.drawable.no_account

                        val encodedName = Uri.encode(name)

                        navController.navigate("destinationOneContactPage/$encodedName/$phoneNumber/$photo")}
                ) {
                    Text(
                        text = person.ime,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}