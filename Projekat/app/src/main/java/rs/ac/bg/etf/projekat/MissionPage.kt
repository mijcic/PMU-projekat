package rs.ac.bg.etf.projekat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun MissionPage(image:Int,title:String){
    Box(
        modifier = Modifier.fillMaxSize() // Puni celu veličinu ekrana
    ) {
        Column(
            modifier = Modifier

        ) {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            Image(
                painter = painterResource(id = image),
                contentDescription = "Image",
                modifier = Modifier, // Slika pokriva celu površinu
            )

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
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}