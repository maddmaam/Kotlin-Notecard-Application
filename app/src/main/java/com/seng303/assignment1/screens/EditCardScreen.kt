package com.seng303.assignment1.screens

import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.seng303.assignment1.R
import com.seng303.assignment1.data.Answer
import com.seng303.assignment1.data.NoteCard
import com.seng303.assignment1.dialogs.AlertDialog
import com.seng303.assignment1.dialogs.ErrorDialog
import com.seng303.assignment1.viewmodels.EditCardViewModel
import com.seng303.assignment1.viewmodels.NoteCardViewModel

@Composable
fun EditCardScreen(
    navController: NavController,
    editCardViewModel: EditCardViewModel,
    noteCardViewModel: NoteCardViewModel,
    cardID: String
) {
    val currentCard by noteCardViewModel.selectedNoteCard.collectAsState(null)
    val card: NoteCard? = currentCard

    var screenOrientation by remember {
        mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT)
    }

    val currentConfig = LocalConfiguration.current
    val currentContext = LocalContext.current

    val trashSoundMediaPlayer = MediaPlayer.create(currentContext, R.raw.trash)

    LaunchedEffect(currentConfig) {
        snapshotFlow { currentConfig.orientation }.collect {screenOrientation = it}
    }

    LaunchedEffect(card) {
        if (card == null) {
            noteCardViewModel.getCardById(cardID.toIntOrNull())
        }
        if (card != null) {
            if (editCardViewModel.previousCard.id != card.id) {
                editCardViewModel.loadCardValues(card)
            }
        }
    }

    when (screenOrientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeEditScreen(
                navController = navController,
                editCardViewModel = editCardViewModel,
                noteCardViewModel = noteCardViewModel,
                cardID = cardID,
                trashSoundPlayer =trashSoundMediaPlayer
            )
        } else -> {
            PortraitEditCardScreen(
                navController = navController,
                editCardViewModel = editCardViewModel,
                noteCardViewModel = noteCardViewModel,
                cardID = cardID,
                trashSoundPlayer = trashSoundMediaPlayer
            )
        }
    }
}

@Composable
fun EditScreenAnswerBox(answer: Answer) {
    // This needs to be updated so it calls the backend logic which will be a class and it can set the
    // answer boolean of the class

    var checked by remember { mutableStateOf(answer.isCorrectAnswer) }

    checked = answer.isCorrectAnswer

    var answerContent by remember {
        mutableStateOf("")
    }

    answerContent = answer.answerContent

    Row(Modifier.padding(12.dp)) {
        Checkbox(checked = checked , onCheckedChange = {
            answer.isCorrectAnswer = it
            checked = it
        })
        TextField(value = answerContent, onValueChange = {
            answer.answerContent = it
            answerContent = it
        })
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LandscapeEditScreen(
    navController: NavController,
    editCardViewModel: EditCardViewModel,
    noteCardViewModel: NoteCardViewModel,
    cardID: String,
    trashSoundPlayer: MediaPlayer
) {
    var showErrorPopUpQuestion by rememberSaveable{
        mutableStateOf(false)
    }

    var showErrorPopUpAnswers by rememberSaveable{
        mutableStateOf(false)
    }

    var showErrorPopUpNoneCorrect by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(30.dp), modifier = Modifier.padding(start = 4.dp)) {
                Column {
                    Text(
                        text = "Edit flash card", // Title
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(8.dp),
                        color = when (isSystemInDarkTheme()) { true -> {MaterialTheme.colorScheme.inverseOnSurface} else -> {MaterialTheme.colorScheme.onSurface}}
                    )
                    OutlinedTextField( // Input Question Field
                        value = editCardViewModel.question,
                        onValueChange = { editCardViewModel.changeQuestion(it) },
                        label = { Text(text = "Input question here", color = when (isSystemInDarkTheme()) {
                            true -> {MaterialTheme.colorScheme.inverseOnSurface}
                            else -> {MaterialTheme.colorScheme.onSurface}
                        } )},
                        modifier = Modifier
                            .weight(1F)
                            .padding(bottom = 52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                            focusedBorderColor = Color.hsv(222F, 0.54F, 0.59F),
                            unfocusedBorderColor = Color.hsv(222F, 0.54F, 0.59F),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                }
                LazyColumn(
                    modifier = Modifier.padding(top = 2.dp, bottom = 52.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(editCardViewModel.answersStrings) { flashCard ->
                        EditScreenAnswerBox(flashCard)
                    }
                    item {
                        Button(
                            onClick = { editCardViewModel.addAnswer(false, "") },
                            modifier = Modifier.size(width = 75.dp, height = 42.dp)
                        ) {
                            Text(
                                text = "+",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }

            }
            Box(Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        Button(onClick = {
                            editCardViewModel.showDeletePopUp = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Flash Card",
                                tint = Color.Red
                            )
                        }
                        Button(onClick = {
                            if (editCardViewModel.question.isEmpty()) {
                                showErrorPopUpQuestion = true
                            } else if (editCardViewModel.answersStrings.count { it.isCorrectAnswer } < 1) {
                                showErrorPopUpNoneCorrect = true
                            } else if (editCardViewModel.answersStrings.count() < 2) {
                                showErrorPopUpAnswers = true
                            } else {
                                noteCardViewModel.editCardById(
                                    cardID.toIntOrNull(),
                                    card = NoteCard(
                                        cardID.toInt(),
                                        editCardViewModel.question,
                                        editCardViewModel.answersStrings
                                    )
                                )
                                editCardViewModel.resetPrevCard()
                                navController.popBackStack()
                            }
                        }) {
                            Text(text = "Save and return")
                        }
                    }
                }
            }
        }
    }
    if (editCardViewModel.showDeletePopUp) {
        AlertDialog(
            onDismiss = {
                editCardViewModel.showDeletePopUp = false
            },
            onConfirm = {
                editCardViewModel.showDeletePopUp = false
                editCardViewModel.resetPrevCard()
                navController.popBackStack()
                noteCardViewModel.deleteCardById(cardID.toIntOrNull())
                trashSoundPlayer.isLooping = false
                trashSoundPlayer.setVolume(0.7F, 0.7F)
                trashSoundPlayer.start()
            },
            alertTitle = "Delete Flash Card?",
            alertText = "Are you sure you want to delete this flash card?",
            dismissText = "Cancel",
            confirmText = "Delete",
            icon = Icons.Default.Delete,
            confirmColor = Color.Red
        )
    }
    if (showErrorPopUpQuestion) {
        ErrorDialog(onDismiss = { showErrorPopUpQuestion = false }, message = "A flash card must have a question", height = 135)
    } else if (showErrorPopUpAnswers) {
        ErrorDialog(onDismiss = { showErrorPopUpAnswers = false }, message = "A flash card must have at least 2 answers.", height = 160)
    } else if (showErrorPopUpNoneCorrect) {
        ErrorDialog(onDismiss = { showErrorPopUpNoneCorrect=false }, message = "A flash card must have 1 correct answer.", height = 160)
    }
}


@Composable
fun PortraitEditCardScreen(
    navController: NavController,
    editCardViewModel: EditCardViewModel,
    noteCardViewModel: NoteCardViewModel,
    cardID: String,
    trashSoundPlayer: MediaPlayer
) {
    var showErrorPopUpQuestion by rememberSaveable{
        mutableStateOf(false)
    }

    var showErrorPopUpAnswers by rememberSaveable{
        mutableStateOf(false)
    }

    var showErrorPopUpNoneCorrect by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Edit flash card", // Title
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp),
            color = when (isSystemInDarkTheme()) { true -> MaterialTheme.colorScheme.inverseOnSurface else -> MaterialTheme.colorScheme.onSurface}
        )
        OutlinedTextField( // Input Question Field
            value = editCardViewModel.question,
            onValueChange = { editCardViewModel.changeQuestion(it) },
            label = { Text(text = "Input question here", color = when (isSystemInDarkTheme()) {
                true -> MaterialTheme.colorScheme.inverseOnSurface
                else -> MaterialTheme.colorScheme.onSurface}) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                focusedBorderColor = Color.hsv(222F, 0.54F, 0.59F),
                unfocusedBorderColor = Color.hsv(222F, 0.54F, 0.59F),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.padding(top = 8.dp, bottom = 52.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(editCardViewModel.answersStrings) { flashCard ->
                    EditScreenAnswerBox(flashCard)
                }
                item {
                    Button(
                        onClick = { editCardViewModel.addAnswer(false, "") },
                        modifier = Modifier.size(width = 75.dp, height = 42.dp)
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    Button(onClick = {
                        editCardViewModel.showDeletePopUp = true
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Flash Card", tint = Color.Red)
                    }

                    Button(onClick = {
                        if (editCardViewModel.question.isEmpty()) {
                            showErrorPopUpQuestion = true
                        } else if (editCardViewModel.answersStrings.count { it.isCorrectAnswer } < 1) {
                            showErrorPopUpNoneCorrect = true
                        } else if (editCardViewModel.answersStrings.count() < 2) {
                            showErrorPopUpAnswers = true
                        } else {
                            noteCardViewModel.editCardById(
                                cardID.toIntOrNull(),
                                card = NoteCard(
                                    cardID.toInt(),
                                    editCardViewModel.question,
                                    editCardViewModel.answersStrings
                                )
                            )
                            editCardViewModel.resetPrevCard()
                            navController.popBackStack()
                        }
                    }) {
                        Text(text = "Save and return")
                    }

                }
            }
        }
    }

    if (editCardViewModel.showDeletePopUp) {
        AlertDialog(
            onDismiss = {
                editCardViewModel.showDeletePopUp = false
            },
            onConfirm = {
                editCardViewModel.showDeletePopUp = false
                editCardViewModel.resetPrevCard()
                navController.popBackStack()
                noteCardViewModel.deleteCardById(cardID.toIntOrNull())
                trashSoundPlayer.isLooping = false
                trashSoundPlayer.setVolume(0.7F, 0.7F)
                trashSoundPlayer.start()
            },
            alertTitle = "Delete Flash Card?",
            alertText = "Are you sure you want to delete this flash card?",
            dismissText = "Cancel",
            confirmText = "Delete",
            icon = Icons.Default.Delete,
            confirmColor = Color.Red
        )
    }
    if (showErrorPopUpQuestion) {
        ErrorDialog(onDismiss = { showErrorPopUpQuestion = false }, message = "A flash card must have a question", height = 135)
    } else if (showErrorPopUpAnswers) {
        ErrorDialog(onDismiss = { showErrorPopUpAnswers = false }, message = "A flash card must have at least 2 answers.", height = 160)
    } else if (showErrorPopUpNoneCorrect) {
        ErrorDialog(onDismiss = { showErrorPopUpNoneCorrect=false }, message = "A flash card must have 1 correct answer.", height = 160)
    }
}