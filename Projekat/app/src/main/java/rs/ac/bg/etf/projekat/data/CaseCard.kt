package rs.ac.bg.etf.projekat.data

data class CaseCard(
    val imageRes: Int,
    val title: String,
    val description: String,
    val onClick: () -> Unit,
    val titleMP: String = "",
    val dateMP: String = "",
    val placeMP: String = "",
    val descMP: String = ""
)