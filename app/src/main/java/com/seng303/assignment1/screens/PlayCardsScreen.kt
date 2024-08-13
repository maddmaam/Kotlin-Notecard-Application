package com.seng303.assignment1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.seng303.assignment1.data.Answer
import com.seng303.assignment1.data.NoteCard
import com.seng303.assignment1.viewmodels.NoteCardViewModel

// TODO: Need to find a way to swap to new versions of this screen for each question.

@Composable
fun PlayCardScreen(navController: NavController, noteCardViewModel: NoteCardViewModel) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Play flash cards",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp))
        QuestionCard(noteCardViewModel)
    }
}

@Composable
fun QuestionCard(noteCardViewModel: NoteCardViewModel) {
    var currentActiveIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    // List of the current questions stored in permanent storage
    noteCardViewModel.getAllCards()
    val noteCads: List<NoteCard> by noteCardViewModel.noteCards.collectAsState(emptyList())

    var question by remember {
        mutableStateOf("question?\n question")
    }

    var answers by remember {
        mutableStateOf(listOf(
            Answer(false, "world"),
            Answer(true, "Hello"),
            Answer(false, "Dont click this answer"),
            Answer(false, "Click this one if u want"),
            Answer(false, "world"),
            Answer(true, "Hello"),
            Answer(false, "Dont click this answer"),
            Answer(false, "Click this one if u want"),
            Answer(false, "world"),
            Answer(true, "Hello"),
            Answer(false, "Dont click this answer"),
            Answer(false, "Click this one if u want")
        ))
    }

    val selectedOption = remember {
        mutableStateOf(answers[1])
    }

    Box(modifier = Modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(Color.LightGray)
        .fillMaxWidth()
        .border(1.5.dp, Color.hsv(222F, 0.54F, 0.59F), RoundedCornerShape(8.dp))) {
        Text(text = question, modifier = Modifier.padding(14.dp))
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(top = 8.dp, bottom = 52.dp)) {
            items(answers) { answer ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (answer == selectedOption.value)/*TODO*/, onClick = { selectedOption.value = answer })
                    Text(text = answer.answerContent, Modifier.padding(horizontal = 12.dp))
                }
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(Color.hsv(203F, 0.24F, 1F)),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "1/1" /*Question Numbers*/, Modifier.padding(end = 60.dp))
                Button(onClick = { /*TODO*/ }, Modifier.padding(start = 60.dp)) {
                    Text(text = "Submit")
                }
            }

        }
    }

}

//@Composable
//fun answerItem(answer: Answer) {
//    val select by remember {
//        mutableStateOf(false)
//    }
//    Row(
//        Modifier.fillMaxWidth()
//    ) {
//        RadioButton(selected = select/*TODO*/, onClick = { !select })
//        Text(text = answer.answerContent)
//    }
//}