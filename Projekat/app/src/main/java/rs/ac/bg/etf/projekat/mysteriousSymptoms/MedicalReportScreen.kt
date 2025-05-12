package rs.ac.bg.etf.projekat.mysteriousSymptoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.realm.kotlin.types.RealmInstant
import rs.ac.bg.etf.projekat.data.MyViewModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MedicalReportScreen(navController: NavController,myViewModel: MyViewModel) {
    val uiStateDataMysteriousSymptoms by myViewModel.uiStateMysteriousSymptomsData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Medical Report",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                // HEADER
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null,
                        tint = Color(0xFF0047AB),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            "St. Mary's Hospital",
                            color = Color(0xFF222222),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Neurology Department",
                            color = Color(0xFF666666),
                            fontSize = 14.sp
                        )
                    }
                }

                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // REPORT TITLE
                Text(
                    "Medical Report",
                    color = Color(0xFF111111),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                val datum: RealmInstant? = uiStateDataMysteriousSymptoms.patient?.zrtvaId?.osobaId?.datum
                val datumPrijave: RealmInstant? = uiStateDataMysteriousSymptoms.patient?.datumPrijave
                var formattedDateDatum:String=""
                var formattedDate:String=""
                datum?.let {
                    val date = Date(it.epochSeconds)
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    formattedDateDatum = dateFormat.format(date)
                }
                datumPrijave?.let {
                    val date = Date(it.epochSeconds)
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    formattedDate = dateFormat.format(date)
                }

                InfoRowPdf("Patient", "${uiStateDataMysteriousSymptoms.patient?.zrtvaId?.osobaId?.ime}")
                InfoRowPdf("Date", formattedDateDatum)
                InfoRowPdf("Date of Admission", formattedDate)

                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                SectionTitlePdf("Summary")
                SectionTextPdf(
                    "${uiStateDataMysteriousSymptoms.medicalReport?.rezime}"
                )

                Divider(
                    color = Color.LightGray.copy(alpha = 0.6f),
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                SectionTitlePdf("Imaging")
                BulletTextPdf("- CT scan:${uiStateDataMysteriousSymptoms.medicalReport?.CTnalaz}")
                BulletTextPdf("- MRI scan: ${uiStateDataMysteriousSymptoms.medicalReport?.MRInalaz}")

                Divider(
                    color = Color.LightGray.copy(alpha = 0.6f),
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                SectionTitlePdf("Laboratory")
                BulletTextPdf("- Blood tests: ${uiStateDataMysteriousSymptoms.medicalReport?.krvnaSlika}")
                BulletTextPdf("- Toxicology screen: ${uiStateDataMysteriousSymptoms.medicalReport?.toksikoloskeAnalize}")

                Divider(
                    color = Color.LightGray.copy(alpha = 0.6f),
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                SectionTitlePdf("Observations")
                SectionTextPdf(
                    "${uiStateDataMysteriousSymptoms.medicalReport?.zakljucak}"
                )

                Divider(
                    color = Color.LightGray.copy(alpha = 0.6f),
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                SectionTitlePdf("Recommendation")
                SectionTextPdf(
                    "Continuous monitoring and multidisciplinary evaluation recommended."
                )

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)

                // FOOTER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Signed: Dr. Emily Carter",
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        "Date: April 4, 2025",
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRowPdf(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            "$label:",
            color = Color(0xFF222222),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(value, color = Color(0xFF333333), fontSize = 16.sp)
    }
}

@Composable
fun SectionTitlePdf(title: String) {
    Text(title, color = Color(0xFF111111), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
}

@Composable
fun SectionTextPdf(text: String) {
    Text(
        text,
        color = Color(0xFF444444),
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = FontFamily.Serif
    )
}

@Composable
fun BulletTextPdf(text: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text("•", color = Color(0xFF444444), fontSize = 16.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, color = Color(0xFF444444), fontSize = 16.sp, fontFamily = FontFamily.Serif)
    }
}