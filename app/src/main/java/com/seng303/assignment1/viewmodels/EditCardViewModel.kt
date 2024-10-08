package com.seng303.assignment1.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.seng303.assignment1.data.Answer
import com.seng303.assignment1.data.NoteCard

class EditCardViewModel : ViewModel() {
    var previousCard by mutableStateOf(NoteCard(0, "", emptyList()))
    var showDeletePopUp by mutableStateOf(false)

    var question by mutableStateOf("")
        private set

    fun changeQuestion(newQuestion: String) {
        question = newQuestion
    }

    var answersStrings = mutableStateListOf(
        Answer(false, ""),
        Answer(false, ""),
        Answer(false, ""),
        Answer(false, ""))
        private set

    fun addAnswer(isCorrect: Boolean, answerContent: String) {
        answersStrings.add(Answer(isCorrect, answerContent))
    }

    fun loadCardValues(loadedCard: NoteCard?) {
        loadedCard?.let {
            question = it.question
            answersStrings = it.answers.toMutableStateList()
            previousCard = it
        }
    }

    fun resetPrevCard() {
        previousCard = NoteCard(-1, "", emptyList())
    }
}