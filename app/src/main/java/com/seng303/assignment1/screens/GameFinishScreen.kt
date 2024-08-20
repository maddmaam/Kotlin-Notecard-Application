package com.seng303.assignment1.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seng303.assignment1.viewmodels.PlayGameViewModel

@Composable
fun GameFinishScreen(
    playGameViewModel: PlayGameViewModel,
) {
    val numCorrectAnswers = playGameViewModel.getNumCorrect()
    val correctAnswers = playGameViewModel.getResults()
    val questions = playGameViewModel.getQuestions()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Summary",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.hsv(194F, 1.0F, 0.53F),
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.padding(8.dp))
        Text(text = "Score: ${numCorrectAnswers}/${correctAnswers.count()}",
            fontSize = 36.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onTertiary)
        LazyColumn {
            itemsIndexed(correctAnswers) { index, answer ->
                ScoreBox(question = questions[index], correct = answer)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScoreBox(question: String, correct: Boolean) {
    FlowRow(
        Modifier.padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.Center,
        maxItemsInEachRow = 2
    ) {
        val imagePadding = Modifier.align(Alignment.CenterVertically).padding(top = 2.dp, bottom = 2.dp, end = 8.dp).weight(1f)
        Text(text = question, Modifier.fillMaxWidth(0.8f).padding(top = 8.dp, bottom = 8.dp, start = 8.dp), color = MaterialTheme.colorScheme.onSecondaryContainer)
        if (correct) {
            Image(imageVector = Icons.Default.Check, contentDescription = "Correct Answers", modifier = imagePadding, contentScale = FixedScale(1.5f))
        } else {
            Image(imageVector = Icons.Default.Close, contentDescription = "Incorrect Answer", modifier = imagePadding, contentScale = FixedScale(1.5f))
        }
    }
}