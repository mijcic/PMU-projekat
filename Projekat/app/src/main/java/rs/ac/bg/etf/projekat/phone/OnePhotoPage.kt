package rs.ac.bg.etf.projekat.phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.R

@Composable
fun OnePhotoPage(picture: Int, datum: String, mesto: String, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = mesto,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = datum,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        IconButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 30.dp)
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.three_dots_horizontal),
                contentDescription = "Three dots",
                tint = colorResource(R.color.iphone_blue)
            )
        }

        Image(
            painter = painterResource(picture),
            contentDescription = "Picture",
            modifier = Modifier.align(Alignment.Center)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource(R.drawable.box_arrow_up),
                contentDescription = "Export",
                tint = colorResource(R.color.iphone_blue),
                modifier = Modifier.size(23.dp)
            )
            Icon(
                painter = painterResource(R.drawable.heart),
                contentDescription = "Like",
                tint = colorResource(R.color.iphone_blue),
                modifier = Modifier.size(23.dp)
            )
            Icon(
                painter = painterResource(R.drawable.info_circle),
                contentDescription = "Info",
                tint = colorResource(R.color.iphone_blue),
                modifier = Modifier.size(23.dp)
            )
            Icon(
                painter = painterResource(R.drawable.sliders),
                contentDescription = "Settings",
                tint = colorResource(R.color.iphone_blue),
                modifier = Modifier.size(23.dp)
            )
            Icon(
                painter = painterResource(R.drawable.trash),
                contentDescription = "Delete",
                tint = colorResource(R.color.iphone_blue),
                modifier = Modifier.size(23.dp)
            )
        }
    }
}
