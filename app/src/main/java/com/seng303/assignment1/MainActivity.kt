package com.seng303.assignment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.seng303.assignment1.screens.HomeScreen
import com.seng303.assignment1.ui.theme.NotecardAppTheme
import com.seng303.assignment1.viewmodels.NoteCardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

//TODO: CUSTOM APP ICON and CUSTOM SPLASH SCREEN

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteCardViewModel by koinViewModel()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen(noteViewModel)
        }
    }
}



