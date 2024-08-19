package com.seng303.assignment1.screens

import android.app.SearchManager
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.seng303.assignment1.data.NoteCard
import com.seng303.assignment1.dialogs.AlertDialog
import com.seng303.assignment1.viewmodels.EditCardViewModel
import com.seng303.assignment1.viewmodels.NoteCardViewModel
import kotlinx.coroutines.delay


@Composable
fun ViewFlashCardScreen(navController: NavController, noteCardViewModel: NoteCardViewModel, editCardViewModel: EditCardViewModel) {
    noteCardViewModel.getAllCards()
    val flashCards: List<NoteCard> by noteCardViewModel.noteCards.collectAsState(emptyList())
    var readyToShow by rememberSaveable {
        mutableStateOf(false)
    }
    editCardViewModel.resetPrevCard()
    
    LaunchedEffect(key1 = flashCards) {
        var timer: Int = 0
        while (noteCardViewModel.getAllCards().isActive && timer != 500 && flashCards.isEmpty()) {
            timer += 5
            delay(5)
        }
        readyToShow = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (flashCards.isNotEmpty() && readyToShow) {
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter) {
                Text(text = "Flash Cards",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold)

                LazyColumn (Modifier.padding(top = 40.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    items(flashCards) { flashCard ->
                        CardElement(navController = navController, flashCard = flashCard,
                            deleteLambda = {id: Int ->  noteCardViewModel.deleteCardById(id)}, numCards = flashCards.size)
                    }
                }
            }
        } else if (readyToShow) {
            AlertDialog(
                onDismiss = { navController.popBackStack() },
                onConfirm = { navController.popBackStack()
                            navController.navigate("CreateFlashCard")},
                alertTitle = "No Cards Found",
                alertText = "There were no cards found. Please create some cards.",
                dismissText = "Return Home",
                confirmText = "Create Card",
                icon = Icons.Default.Info
            )
        }
    }
}

@Composable
fun CardElement(navController: NavController, flashCard: NoteCard, deleteLambda: (id: Int) -> Unit, numCards: Int) {
    val currentContext = LocalContext.current
    val searchIntent = Intent(Intent.ACTION_WEB_SEARCH)
    searchIntent.putExtra(SearchManager.QUERY, flashCard.question)
    var showAlertDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 22.dp)
        .clip(RoundedCornerShape(21.dp))
        .background(Color.White)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = flashCard.question, modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 4.dp, bottom = 2.dp), textAlign = TextAlign.Center)
            Row {
                Button(onClick = {
                    startActivity(currentContext, searchIntent, null)
                }, modifier = Modifier.padding(end =18.dp, bottom = 6.dp)) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                Button(onClick = {
                    navController.navigate("EditCard/${flashCard.id}")
                }, modifier = Modifier.padding(end =18.dp, bottom = 6.dp)) {
                    Icon(imageVector = Icons.Default.Create, contentDescription = "Edit")
                }
                Button(onClick = {
                    showAlertDialog = true
                }, modifier = Modifier.padding(bottom = 6.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }

    if (showAlertDialog) {
        AlertDialog(
            onDismiss = {
                navController.popBackStack()
                navController.navigate("ViewFlashCards")
            },
            onConfirm = {
                deleteLambda(flashCard.id)
                navController.popBackStack()
                if (numCards > 1) {
                    navController.navigate("ViewFlashCards")
                }
            },
            alertTitle = "Delete Flash Card?",
            alertText = "Are you sure you would like to delete flash card: \"${flashCard.question}\" ?",
            dismissText = "Cancel",
            confirmText = "Delete",
            icon = Icons.Default.Delete
        )
    }
}