package rs.ac.bg.etf.projekat.murder

data class QuestionDetail(
    val tekst: String,
    val odgovor: String,
    val komentar: String
)

enum class Section(val label: String) {
    GENERAL("GENERAL QUESTIONS"),
    ALIBI("ALIBI QUESTIONS"),
    EVIDENCE("EVIDENCE QUESTIONS"),
    PASSING("PASSING QUESTIONS")
}