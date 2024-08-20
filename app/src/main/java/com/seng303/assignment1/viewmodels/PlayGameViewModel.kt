package com.seng303.assignment1.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.seng303.assignment1.data.NoteCard

class PlayGameViewModel : ViewModel() {
    private var correctAnswers = mutableListOf(false, false)
    private var questions = mutableListOf("")
    private var isPlaying by mutableStateOf(false)
    var currentActiveIndex by mutableIntStateOf(0)

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

    fun startPlaying() {
        isPlaying = true
        currentActiveIndex = 0
    }

    fun getCurrentPlayStatus(): Boolean {
        return isPlaying
    }

    fun resetPlaying() {
        isPlaying = false
    }
}