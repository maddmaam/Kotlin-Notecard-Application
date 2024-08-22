package com.seng303.assignment1

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.seng303.assignment1.screens.HomeScreen
import com.seng303.assignment1.viewmodels.NoteCardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteCardViewModel by koinViewModel()
    //private val playerDataViewModel: PlayerDataViewModel by koinViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        enableEdgeToEdge()
        setContent {
            HomeScreen(noteViewModel, vibrator)//, playerViewModel)
        }
    }
}



