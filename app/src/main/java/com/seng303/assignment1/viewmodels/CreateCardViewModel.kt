package com.seng303.assignment1.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.seng303.assignment1.data.Answer

class CreateCardViewModel : ViewModel() {
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

}