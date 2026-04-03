package rs.ac.bg.etf.projekat.mysteriousSymptoms.patient

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.realm.kotlin.types.RealmInstant
import rs.ac.bg.etf.projekat.Background
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.MyViewModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PatientScreen(
    myViewModel: MyViewModel,
    onDestinationMedicalReportPage: () -> Unit,
    onDestinationPhonePage: () -> Unit,
    onDestinationMedicalStatementPage: () -> Unit,
    onDestinationLekarskiTestPage: () -> Unit
) {
    val uiStateDataMysteriousSymptoms by myViewModel.uiStateMysteriousSymptomsData.collectAsState()

    LaunchedEffect(Unit) {
        myViewModel.getAllDataMysteriousSymptoms()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Background(
            image = R.drawable.patient,
            desc = "Background Image",
            modifier = Modifier.fillMaxSize(),
            alpha = 0.7f
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            Text(
                text = "Patient: ${uiStateDataMysteriousSymptoms.patient?.zrtvaId?.osobaId?.ime}",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                PatientExpandableSection(title = "Patient Details") {
                    DetailItem("Date of Birth", "${formattedDateDatum}")
                    DetailItem("Occupation", "${uiStateDataMysteriousSymptoms.patient?.zrtvaId?.osobaId?.zanimanje}")
                    DetailItem("Symptoms", "${uiStateDataMysteriousSymptoms.patient?.simptomi}")
                    DetailItem("Admission Date", "${formattedDate}")
                    DetailItem("Reported By", "${uiStateDataMysteriousSymptoms.patient?.prijavio}")
                }

                Spacer(modifier = Modifier.height(20.dp))

                ExpandableSection(title = "Documents") {
                    PatientInfoCard(icon ="📋", title = "Medical Report",
                        description = "Displays the patient's basic medical findings.",
                        onClick = onDestinationMedicalReportPage
                    )
                    PatientInfoCard(icon = "📱",
                        title ="Phone",
                        description = "Contains data from the patient's mobile phone.",
                        onClick = onDestinationPhonePage
                    )
                    PatientInfoCard(icon= "👪",title=  "Medical Statement",
                        description = "This can be an important lead in understanding the patient’s mental state.",
                        onClick = onDestinationMedicalStatementPage
                    )
                    PatientInfoCard(icon = "🧪", title="Initial Test Results",
                        description = "Laboratory test results confirm that no known psychoactive substances were found in the patient's body.",
                        onClick = onDestinationLekarskiTestPage
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun PatientExpandableSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.dark_purple)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White),
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.rotate(rotation)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp).animateContentSize()
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun ExpandableSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.dark_purple)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.special_elite)), color = Color.White),
                    textAlign = TextAlign.Center)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.rotate(rotation)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                        .animateContentSize()
                ) {
                    content()
                }
            }
        }
    }
}