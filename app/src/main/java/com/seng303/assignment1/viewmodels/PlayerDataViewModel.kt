package com.seng303.assignment1.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seng303.assignment1.data.PlayedGame
import com.seng303.assignment1.data.Player
import com.seng303.assignment1.datastore.Storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

class PlayerDataViewModel(private val playedGameStorage: Storage<PlayedGame>): ViewModel() {
    private val _playedGames = MutableStateFlow<List<PlayedGame>>(emptyList())
    val playedGames: StateFlow<List<PlayedGame>> get() = _playedGames
    private val _selectedGame = MutableStateFlow<PlayedGame?>(null)
    val selectedGame: StateFlow<PlayedGame?> = _selectedGame


    fun getGameById(gameId: Int?) = viewModelScope.launch {
        if (gameId != null) {
            _selectedGame.value = playedGameStorage.get { it.getIdentifier() == gameId }.first()
        } else {
            _selectedGame.value = null
        }
    }

    fun getAllGames() = viewModelScope.launch {
        playedGameStorage.getAll().catch { Log.e("PLAYER_DATA_VIEW_MODEL", it.toString()) }
            .collect { _playedGames.emit(it) }
    }

    fun createGame(gameScore: Int, totalQuestions: Int, player: Player) = viewModelScope.launch {
        val game = PlayedGame(gameId = Random.nextInt(), gameScore = gameScore, player = player, totalQuestions = totalQuestions)
        playedGameStorage.insert(game).catch { Log.e("PLAY_GAME_VIEW_MODEL", "Could not add Game") }.collect()
        playedGameStorage.getAll().catch { Log.e("PLAY_GAME_VIEW_MODEL", it.toString()) }.collect{_playedGames.emit(it)}
    }
}