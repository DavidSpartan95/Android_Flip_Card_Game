package com.davidspartan.androidflipcardgame.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.davidspartan.androidflipcardgame.model.game.Card
import kotlinx.coroutines.flow.MutableStateFlow
import com.davidspartan.androidflipcardgame.model.game.GameState

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: MutableStateFlow<GameState> = _gameState

    private val _cards = MutableStateFlow<List<Card>>(emptyList()) // Initialize with an empty list
    val cards: MutableStateFlow<List<Card>> = _cards

    init {
        generateCards() // Generate cards when the ViewModel is created
    }

    private fun generateCards() {
        val cardList = listOf(
            Card(1, Color.Red),
            Card(2, Color.Blue),
            Card(3, Color.Green)
        )

        // Create pairs of each card
        val pairedCards = cardList + cardList

        // Update the StateFlow with the paired cards
        _cards.value = pairedCards.shuffled()
    }

    fun incrementScore() {
        _gameState.value.score++
    }

    fun resetScore() {
        _gameState.value.score = 0
    }

}
