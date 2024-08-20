package com.seng303.assignment1.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.seng303.assignment1.data.Answer
import com.seng303.assignment1.data.NoteCard
import com.seng303.assignment1.dialogs.AlertDialog
import com.seng303.assignment1.viewmodels.NoteCardViewModel
import com.seng303.assignment1.viewmodels.PlayGameViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayCardScreen(navController: NavController, noteCardViewModel: NoteCardViewModel, playGameViewModel: PlayGameViewModel) {
    var screenOrientation by remember {
        mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT)
    }

    val currentConfig = LocalConfiguration.current

    LaunchedEffect(currentConfig) {
        snapshotFlow { currentConfig.orientation }.collect {screenOrientation = it}
    }

    when (screenOrientation) {
       Configuration.ORIENTATION_LANDSCAPE -> {
           LandscapePlayCardScreen(
               navController = navController,
               noteCardViewModel = noteCardViewModel,
               playGameViewModel = playGameViewModel)
       } else -> {
            PortraitPlayCardScreen (
                navController = navController,
                noteCardViewModel = noteCardViewModel,
                playGameViewModel = playGameViewModel
            )
       }
    }
}

@Composable
fun QuestionCard(noteCardViewModel: NoteCardViewModel, navController: NavController, playGameViewModel: PlayGameViewModel) {
    val context = LocalContext.current

    // List of the current questions stored in permanent storage
    noteCardViewModel.getAllCards()
    val noteCards: List<NoteCard> by noteCardViewModel.noteCards.collectAsState(emptyList())

    var correctAnswersCurrentQuestion: List<Answer>

    var readyToShow by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(noteCards) { // CURSED BUT IT WORKS haha
        var timer: Int = 0
        while (noteCardViewModel.getAllCards().isActive && timer != 500 && noteCards.isEmpty()) {
            timer += 5
            delay(5)
        }
        if (!playGameViewModel.getCurrentPlayStatus()) {
            playGameViewModel.initializeAnswerList(noteCards.size)
            playGameViewModel.initializeQuestionList(noteCards.toList())
            playGameViewModel.startPlaying()
        }
        readyToShow = true
    }

    if (readyToShow) {
        if (noteCards.isEmpty()) {
            AlertDialog(
                onDismiss = { navController.popBackStack() },
                onConfirm = {
                    navController.popBackStack()
                    navController.navigate("CreateFlashCard")
                },
                alertTitle = "An Error Occurred",
                alertText = "Timed out while retrieving Flash Cards! \nHave you created Flash Cards for use?",
                confirmText = "Create Cards",
                dismissText = "Return Home",
                icon = Icons.Default.Info
            )
        } else {
            val selectedOption = remember {
                mutableStateOf(noteCards[playGameViewModel.currentActiveIndex].answers[1])
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .border(1.5.dp, Color.hsv(222F, 0.54F, 0.59F), RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = noteCards[playGameViewModel.currentActiveIndex].question,
                    modifier = Modifier.padding(14.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.padding(top = 8.dp, bottom = 52.dp)) {
                    items(noteCards[playGameViewModel.currentActiveIndex].answers) { answer ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (answer == selectedOption.value),
                                onClick = { selectedOption.value = answer })
                            Text(text = answer.answerContent, Modifier.padding(horizontal = 12.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val questionNum = playGameViewModel.currentActiveIndex + 1
                        Text(
                            text = "$questionNum/${noteCards.count()}" /*Question Numbers*/,
                            Modifier.padding(end = 60.dp), color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Button(onClick = {
                            correctAnswersCurrentQuestion =
                                noteCards[playGameViewModel.currentActiveIndex].answers.filter { it.isCorrectAnswer }
                            val answerCorrect =
                                correctAnswersCurrentQuestion.contains(selectedOption.value)
                            playGameViewModel.setAnswerCorrect(
                                playGameViewModel.currentActiveIndex,
                                answerCorrect
                            )
                            val toastText: String = if (answerCorrect) {
                                "Correct Answer"
                            } else {
                                "Wrong Answer"
                            }
                            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                            if (questionNum < noteCards.count()) {
                                playGameViewModel.currentActiveIndex++
                            } else {
                                navController.popBackStack()
                                navController.navigate("GameFinish")
                            }
                        }, Modifier.padding(start = 60.dp)) {
                            Text(text = "Submit")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun PortraitPlayCardScreen(navController: NavController, noteCardViewModel: NoteCardViewModel, playGameViewModel: PlayGameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Play flash cards",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSecondaryContainer)
        QuestionCard(noteCardViewModel, navController, playGameViewModel)
    }
}

@Composable
fun LandscapePlayCardScreen(navController: NavController, noteCardViewModel: NoteCardViewModel, playGameViewModel: PlayGameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        QuestionCard(noteCardViewModel = noteCardViewModel, navController = navController, playGameViewModel = playGameViewModel)
    }
}

