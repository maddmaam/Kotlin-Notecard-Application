package com.seng303.assignment1.screens

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seng303.assignment1.dialogs.ErrorDialog
import com.seng303.assignment1.ui.theme.NotecardAppTheme
import com.seng303.assignment1.viewmodels.NoteCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(noteViewModel: NoteCardViewModel) {
    NotecardAppTheme {
        val navigationController = rememberNavController()
        Scaffold(Modifier.padding(12.dp),
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
                .background(Color.hsv(203F, 0.24F, 1F))
                .border(1.5.dp, Color.hsv(222F, 0.54F, 0.59F), RoundedCornerShape(21.dp))
            ) {
                NavHost(navController = navigationController, startDestination = "Home") {
                    composable("Home"){
                        Home(navController = navigationController)
                    }
                    composable("ViewFlashCards") {
                        ViewFlashCardScreen(navController = navigationController, noteViewModel)
                    }
                    composable("CreateFlashCard") {
                        CreateCardScreen(navigationController, noteViewModel)
                    }
                    composable("PlayFlashCards") {
                        PlayCardScreen(navController = navigationController, noteCardViewModel = noteViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(modifier = Modifier.padding(10.dp),
            onClick = { navController.navigate("ViewFlashCards") }) {
            Text(text = "View Flash Cards")
        }
        Button(modifier = Modifier.padding(10.dp),
            onClick = { navController.navigate("CreateFlashCard") }) {
            Text(text = "Create Flash Card")
        }
        Button(modifier = Modifier.padding(10.dp),
            onClick = { navController.navigate("PlayFlashCards") }) {
            Text(text = "Play Flash Cards")
        }
    }
}