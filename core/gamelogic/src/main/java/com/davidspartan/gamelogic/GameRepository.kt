package com.davidspartan.gamelogic

import com.davidspartan.model.GameState
import com.davidspartan.model.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*


class GameRepository {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: MutableStateFlow<GameState> = _gameState

    private val _cards = MutableStateFlow<List<Card>>(generateCards())
    val cards: StateFlow<List<Card>> = _cards

    private val _flippedCards = MutableStateFlow<List<Card>>(emptyList())

    fun flipCard(card: Card) {

        if (_flippedCards.value.size + 1 >= 3) return // Prevent flipping more than 2 cards

        _gameState.value = _gameState.value.copy(totalFlips = _gameState.value.totalFlips + 1)

        _cards.value = _cards.value.map {
            if (it.id == card.id) it.copy(isFlipped = !it.isFlipped) else it
        }

        _flippedCards.value = _cards.value.filter { it.isFlipped && !it.found }

        if (_flippedCards.value.size == 2) {

            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                checkCards()
            }
        }
    }

    private fun checkCards() {

        val flipped = _flippedCards.value

        if (flipped.size == 2 && flipped[0].hexColor == flipped[1].hexColor) {
            _cards.value = _cards.value.map {
                if (it.id in flipped.map { c -> c.id }) it.copy(found = true) else it
            }
            _gameState.value = _gameState.value.copy(score = _gameState.value.score + 1)
        } else {

            _cards.value = _cards.value.map { if (it.isFlipped && !it.found) it.copy(isFlipped = false) else it }
        }
        _flippedCards.value = emptyList()
        checkGameOver()
    }

    private fun checkGameOver() {
        if (_cards.value.all { it.found }) {
            _gameState.value = _gameState.value.copy(isGameOver = true)
        }
    }

    fun resetGame() {
        _gameState.value = _gameState.value.copy(isGameOver = false, score = 0, totalFlips = 0)
        _cards.value = generateCards()
        _flippedCards.value = emptyList()
    }

    companion object {
        private fun generateCards(): List<Card> {
            val colors = listOf("#3bf9a0", "#f9d73b", "#eb49aa")
            return colors.flatMap { color -> listOf(Card(hexColor = color), Card(hexColor = color)) }
                .shuffled()
        }
    }
}
