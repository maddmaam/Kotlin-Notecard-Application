package com.seng303.assignment1.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.seng303.assignment1.data.NoteCard
import com.seng303.assignment1.viewmodels.EditCardViewModel
import com.seng303.assignment1.viewmodels.NoteCardViewModel

@Composable
fun EditCardScreen(
    navController: NavController,
    editCardViewModel: EditCardViewModel,
    noteCardViewModel: NoteCardViewModel,
    cardID: Int
) {
    val currentCard by noteCardViewModel.selectedNoteCard.collectAsState(null)
    val card: NoteCard? = currentCard

    LaunchedEffect(card) {
        if (card == null) {
            noteCardViewModel.getCardById(cardID)
        } else {
            editCardViewModel.loadCardVales(card)
        }
    }
}