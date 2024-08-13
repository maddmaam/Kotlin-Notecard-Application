package com.seng303.assignment1.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seng303.assignment1.data.Answer
import com.seng303.assignment1.data.NoteCard
import com.seng303.assignment1.datastore.Storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

class NoteCardViewModel(private val flashCardStorage: Storage<NoteCard>) : ViewModel() {
    private val _noteCards = MutableStateFlow<List<NoteCard>>(emptyList())
    val noteCards: StateFlow<List<NoteCard>> get() = _noteCards
    private val _selectedNoteCard = MutableStateFlow<NoteCard?>(null)
    val selectedNoteCard: StateFlow<NoteCard?> = _selectedNoteCard

    fun getCardById(cardId: Int?) = viewModelScope.launch {
        if (cardId != null) {
            _selectedNoteCard.value = flashCardStorage.get { it.getIdentifier() == cardId }.first()
        } else {
            _selectedNoteCard.value = null
        }
    }

    fun getAllCards() = viewModelScope.launch {
        flashCardStorage.getAll().catch { Log.e("NOTE_VIEW_MODEL", it.toString()) }.collect{_noteCards.emit(it)}
    }

    fun createCard(question: String, answers: MutableList<Answer>) = viewModelScope.launch {
        val filteredAnswers = answers.filter { it.answerContent != "" }
        val flashCard = NoteCard(id = Random.nextInt(0, Int.MAX_VALUE), question = question, answers = filteredAnswers)
        flashCardStorage.insert(flashCard).catch { Log.e("NOTE_CARD_VIEW_MODEL", "Could not add flash card") }.collect()
        flashCardStorage.getAll().catch { Log.e("NOTE_CARD_VIEW_MODEL", it.toString()) }.collect{_noteCards.emit(it)}
        Log.e("NOTE_CARD_VIEW_MODEL", "CREATED THE CARD!!!!")
    }
}