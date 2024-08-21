package com.seng303.assignment1.data

data class Player (
    val playerName: String
)

data class PlayedGame(
    val gameId: Int,
    val gameScore: Int,
    val totalQuestions: Int,
    val player: Player
): Identifiable {
    override fun getIdentifier(): Int {
        return gameId
    }
}