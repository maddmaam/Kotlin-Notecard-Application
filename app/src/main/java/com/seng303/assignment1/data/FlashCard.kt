package com.seng303.assignment1.data

data class Answer (
    var isCorrectAnswer: Boolean,
    var answerContent: String
)

data class NoteCard(
    val id: Int,
    val question: String,
    val answers: List<Answer>
) : Identifiable {
    override fun getIdentifier(): Int {
        return id
    }
}