package com.seng303.assignment1.screens

import android.content.res.Configuration
import android.graphics.Bitmap.Config
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
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.seng303.assignment1.data.Answer
import com.seng303.assignment1.data.NoteCard
import com.seng303.assignment1.viewmodels.CreateCardViewModel
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
                cardID = cardID
            )
        } else -> {
            PortraitEditCardScreen(
                navController = navController,
                editCardViewModel = editCardViewModel,
                noteCardViewModel = noteCardViewModel,
                cardID = cardID
            )
        }
    }
}

@Composable
fun EditScreenAnswerBox(answer: Answer, editCardViewModel: EditCardViewModel) {
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
    cardID: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(30.dp), modifier = Modifier.padding(start = 40.dp)) {
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
                    modifier = Modifier.padding(top = 8.dp, bottom = 52.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(editCardViewModel.answersStrings) { flashCard ->
                        EditScreenAnswerBox(flashCard, editCardViewModel)
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
                    Button(onClick = {
                        noteCardViewModel.editCardById(cardID.toIntOrNull(), card = NoteCard(cardID.toInt(), editCardViewModel.question, editCardViewModel.answersStrings))
                        editCardViewModel.resetPrevCard()
                        navController.popBackStack()
                    }) {
                        Text(text = "Save and return")
                    }
                }
            }
        }
    }
}


@Composable
fun PortraitEditCardScreen(navController: NavController, editCardViewModel: EditCardViewModel, noteCardViewModel: NoteCardViewModel, cardID: String) {
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
                    EditScreenAnswerBox(flashCard, editCardViewModel)
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
                Button(onClick = {
                    noteCardViewModel.editCardById(cardID.toIntOrNull(), card = NoteCard(cardID.toInt(), editCardViewModel.question, editCardViewModel.answersStrings))
                    editCardViewModel.resetPrevCard()
                    navController.popBackStack()
                }) {
                    Text(text = "Save and return")
                }
            }
        }
    }
}