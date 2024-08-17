package com.seng303.assignment1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.seng303.assignment1.data.Answer
import androidx.compose.foundation.lazy.items
import com.seng303.assignment1.dialogs.ErrorDialog
import com.seng303.assignment1.viewmodels.NoteCardViewModel

// TODO: THIS NEED TO SHOW ERROR WHEN NO CARDS ARE RETRIEVED
@Composable
fun CreateCardScreen(navController: NavController, noteCardViewModel: NoteCardViewModel) {
    var question by rememberSaveable {
        mutableStateOf("")
    }

    var answersStrings = remember {
        mutableStateListOf(
            Answer(false, ""),
            Answer(false, ""),
            Answer(false, ""),
            Answer(false, ""))
    }

    var answerCount by rememberSaveable {
        mutableIntStateOf(2)
    }

    var showErrorPopUpQuestion by remember {
        mutableStateOf(false)
    }

    var showErrorPopUpAnswers by remember {
        mutableStateOf(false)
    }

    var showErrorPopUpNoneCorrect by remember {
        mutableStateOf(false)
    }
//        mutableStateListOf(Answer(false, ""), Answer(false, ""))
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Add a new flash card", // Title
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField( // Input Question Field
            value = question, 
            onValueChange = {question = it},
            label = { Text(text = "Input question here")},
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = Color.LightGray,
                focusedBorderColor = Color.hsv(222F, 0.54F, 0.59F),
                unfocusedBorderColor = Color.hsv(222F, 0.54F, 0.59F)
            )
        )
        Box (Modifier.fillMaxSize()){
            LazyColumn(modifier = Modifier.padding(top = 8.dp, bottom = 52.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                items(answersStrings) {
                    flashCard -> AnswerCheckBox(flashCard)
                }
                item { Button(
                    onClick = {answersStrings.add(Answer(false, ""))},
                    modifier = Modifier.size(width = 75.dp, height = 42.dp)
                ) {
                    Text(text = "+",
                        style = MaterialTheme.typography.titleLarge)
                } }
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.hsv(203F, 0.24F, 1F)),
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Button(onClick = {
                    val filteredAnswers = answersStrings.filter { it.answerContent != "" }
                    val numCorrectAnswers = filteredAnswers.count { it.isCorrectAnswer }
                    if (question.isEmpty()) {
                        showErrorPopUpQuestion = true;
                    } else if (filteredAnswers.count() < 2) {
                        showErrorPopUpAnswers = true;
                    } else if (numCorrectAnswers < 1) {
                        showErrorPopUpNoneCorrect = true;
                    } else {
                        noteCardViewModel.createCard(question, filteredAnswers)
                        navController.navigate("Home")
                    }

                }) {
                    Text(text = "Save and return")
                }
            }
        }

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
fun AnswerCheckBox(answer: Answer) {
    // This needs to be updated so it calls the backend logic which will be a class and it can set the
    // answer boolean of the class
    var checked by rememberSaveable {
        mutableStateOf(answer.isCorrectAnswer)
    }

    var possibleAnswer by rememberSaveable {
        mutableStateOf(answer.answerContent)
    }

    Row(Modifier.padding(12.dp)) {
        Checkbox(checked = checked , onCheckedChange = {
            answer.isCorrectAnswer = it
            checked = it
        })
        TextField(value = possibleAnswer, onValueChange = {
            answer.answerContent = it
            possibleAnswer = it
        })
    }
}

@Composable
fun SaveAndReturn(navController: NavController,
                  noteCardViewModel: NoteCardViewModel,
                  question: String,
                  answers: MutableList<Answer>
) {

}
