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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CardsPage(modifier: Modifier = Modifier, navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        var textWidth by remember { mutableStateOf(0f) }
        var paddingStart by remember { mutableStateOf(0.dp) }

        Image(
            painter = painterResource(id = R.drawable.cards_image),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier
            .align(Alignment.TopCenter).padding(top = 22.dp)) {

            Column(
                modifier = Modifier
            ) {


                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier.padding(start = paddingStart)
            ) {
                Text(text = "Detective,", color = Color.White,
                    style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.special_elite)
                    ),
                    color = Color.Black
                ))
            }
        Column(
            modifier = Modifier
        ) {
            Text("choose one case from the options provided.", color = Color.White,
                style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.special_elite)
                    ),
                    color = Color.Black
                ))
        }

        Column(
            modifier = Modifier
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                item {
                    CardWithImage(
                        R.drawable.murder,
                        "Murder \uD83D\uDD2A",
                        "Dive into a chilling investigation " +
                                "to solve a brutal murder and " +
                                "uncover the truth behind the crime.",
                        navController
                    )

                    CardWithImage(
                        R.drawable.dissapear,
                        "Disappearance \uD83D\uDCCC",
                        "A thrilling mission where the detective seeks to uncover the " +
                                "mystery of a missing person, uncovering hidden secrets along " +
                                "the way.",
                        navController
                    )


                    CardWithImage(
                        R.drawable.bank_robbery,
                        " Robbery \uD83D\uDCB0",
                        "Focuses on solving a robbery case, whether it's a bank heist, " +
                                "a museum theft, or the stealing of valuable items.",
                        navController
                    )

                    CardWithImage(
                        R.drawable.kidnapping,
                        "Kidnapping and Blackmail \uD83E\uDEE5",
                        "Investigate the dark world of kidnappings and blackmail, " +
                                "where innocent lives are held ransom for secrets or money.",
                        navController
                    )

                    CardWithImage(
                        R.drawable.family_secrets,
                        "Family Secrets \uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66",
                        "The detective explores crimes rooted in family dynamics, unraveling " +
                                "secrets that tie blood relatives to criminal activity.",
                        navController
                    )

                    CardWithImage(
                        R.drawable.abuse,
                        "Abuse \uD83D\uDC7A",
                        "Investigating cases of abuse, be it physical, emotional, or \n" +
                                "psychological, to uncover the perpetrators and bring justice.",
                        navController
                    )


                    CardWithImage(
                        R.drawable.gang,
                        "Gang Conflicts \uD83E\uDE78",
                        "Delve into a dangerous world of \n" +
                                "gang wars and criminal \n" +
                                "organizations, solving cases of violence and turf battles.",
                        navController
                    )

                    CardWithImage(
                        R.drawable.corruption,
                        "Corruption \uD83C\uDFDB\uFE0F",
                        "Uncover the hidden faces of \n" +
                                "corruption in politics or corporations, revealing the \n" +
                                "extent of fraudulent activities \n" +
                                "and their consequences.",
                        navController
                    )

                    CardWithImage(
                        R.drawable.m_symptoms,
                        "Mysterious Symptoms ⚕\uFE0F ",
                        "Investigate strange diseases or unusual deaths, connecting the \n" +
                                "dots between mysterious health \n" +
                                "conditions and criminal activity.",
                        navController
                    )
                    CardWithImage(
                        R.drawable.mafia,
                        "Mafia ❌",
                        "Explore the dangerous world of \n" +
                                "mafia organizations, \n" +
                                "investigating murders, extortion, drug trafficking, and other \n" +
                                "heinous crimes.",
                        navController
                    )

                    CardWithImage(
                        R.drawable.crime_passion,
                        "Crimes of Passion \uD83C\uDFAD",
                        "Investigate intense emotional \n" +
                                "motives behind crimes of passion, \n" +
                                "such as murders driven by jealousy \n" +
                                "or violent love affairs.",
                        navController
                    )

                    CardWithImage(
                        R.drawable.identities,
                        " False Identities \uD83E\uDEAA",
                        "Solve cases involving the use of fake or stolen identities for illegal activities, " +
                                "uncovering the culprits behind them.",
                        navController
                    )
                    CardWithImage(
                        R.drawable.sects,
                        "Cults and Sects \uD83D\uDC80",
                        "Uncover the sinister operations of dangerous cults or ideological \n" +
                                "sects, revealing manipulation, brainwashing, and murder.",
                        navController
                    )
                }

            }
        }
        }

    }
}

@Composable
fun CardWithImage(image: Int, title:String, text:String,navController: NavController) {
    Card(
        modifier = Modifier
            .padding(1.dp)
            .clickable{
                navController.navigate(destinationMissionPage.route+"/"+image+"/"+title)
            }
            .padding(bottom = 18.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(11.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2B2D)
        ),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        ),
                        color = Color.Black
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.special_elite)
                        ),
                        color = Color.Black
                    ),
                    color = Color.White
                )
            }
        }
    }
}