package com.seng303.assignment1.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.seng303.assignment1.data.NoteCard

class PlayGameViewModel : ViewModel() {
    private var correctAnswers = mutableListOf(false, false)
    private var questions = mutableListOf("")

    fun initializeQuestionList(cards: List<NoteCard>) {
        questions = emptyList<String>().toMutableList()
        cards.forEach {
            questions.add(it.question)
        }
    }

    fun initializeAnswerList(capacity: Int) {
        correctAnswers = MutableList(capacity) { false }
    }

    fun setAnswerCorrect(index: Int, correctness: Boolean) {
        correctAnswers[index] = correctness
    }

    fun getResults(): MutableList<Boolean> {
        return correctAnswers
    }

    fun getNumCorrect() : Int {
        return correctAnswers.count { it }
    }

    fun getQuestions() : List<String> {
        return questions.toList()
    }
}