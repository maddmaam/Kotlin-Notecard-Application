package com.seng303.assignment1.screens

import android.os.Vibrator
import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.seng303.assignment1.ui.theme.NotecardAppTheme
import com.seng303.assignment1.viewmodels.CreateCardViewModel
import com.seng303.assignment1.viewmodels.EditCardViewModel
import com.seng303.assignment1.viewmodels.NoteCardViewModel
import com.seng303.assignment1.viewmodels.PlayGameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(noteViewModel: NoteCardViewModel, vibrator: Vibrator) {//, playerDataViewModel: PlayerDataViewModel) {
    NotecardAppTheme {
        val navigationController = rememberNavController()
        Scaffold(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Flash Cards App")},
                    navigationIcon = {
                        IconButton(onClick = { navigationController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) {
            Box(modifier = Modifier
                .padding(it)
                .clip(RoundedCornerShape(21.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .border(1.5.dp, Color.hsv(222F, 0.54F, 0.59F), RoundedCornerShape(21.dp))
            ) {
                val editCardViewModel: EditCardViewModel = viewModel()
                val playGameViewModel: PlayGameViewModel = viewModel()
                val createCardViewModel: CreateCardViewModel = viewModel()
                if (navigationController.currentBackStackEntryAsState().value?.destination?.route == "Home") {
                    createCardViewModel.resetInputFields()
                    playGameViewModel.resetPlaying()
                }
                NavHost(navController = navigationController, startDestination = "Home") {
                    composable("Home"){
                        Home(navController = navigationController)
                    }
                    composable("EditCard/{cardId}", arguments = listOf(navArgument("cardId") {
                        type = NavType.StringType
                    })) {
                        backStackEntry ->
                        val cardID = backStackEntry.arguments?.getString("cardId")
                        cardID?.let { cardIDParam: String -> EditCardScreen(
                            navController = navigationController,
                            editCardViewModel = editCardViewModel,
                            noteCardViewModel = noteViewModel,
                            cardID = cardIDParam
                        ) }
                    }
                    composable("GameFinish") {
                        GameFinishScreen(playGameViewModel = playGameViewModel)
                    }
                    composable("ViewFlashCards") {
                        ViewFlashCardScreen(navController = navigationController, noteViewModel) {
                            editCardViewModel.resetPrevCard()
                        }
                    }
                    composable("CreateFlashCard") {
                        CreateCardScreen(navigationController, noteViewModel, createCardViewModel)
                    }
                    composable("PlayFlashCards") {
                        PlayCardScreen(navController = navigationController,
                            noteCardViewModel = noteViewModel,
                            playGameViewModel = playGameViewModel,
                            vibrator = vibrator
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Home(navController: NavController) {
    val view = LocalView.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(modifier = Modifier.padding(10.dp),
            onClick = {
                navController.navigate("ViewFlashCards")
                view.playSoundEffect(SoundEffectConstants.CLICK)
            }) {
            Text(text = "View Flash Cards")
        }
        Button(modifier = Modifier.padding(10.dp),
            onClick = {
                navController.navigate("CreateFlashCard")
                view.playSoundEffect(SoundEffectConstants.CLICK)
            }) {
            Text(text = "Create Flash Card")
        }
        Button(modifier = Modifier.padding(10.dp),
            onClick = {
                navController.navigate("PlayFlashCards")
                view.playSoundEffect(SoundEffectConstants.CLICK)
            }) {
            Text(text = "Play Flash Cards")
        }
    }
}