package com.davidspartan.androidflipcardgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidspartan.model.GameState
import com.davidspartan.model.Card
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: MutableStateFlow<GameState> = _gameState

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: MutableStateFlow<List<Card>> = _cards

    private val _flippedCards = MutableStateFlow(0)
    private val flippedCards: StateFlow<Int> = _flippedCards


    val uiState: StateFlow<GameUiState> = combine(
        gameState,
        flippedCards
    ) { selectedId, userList ->
        val state = gameState.value
        if (state.isGameOver) {
            GameUiState.GameOver(state)
        } else {
            GameUiState.Playing(state)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GameUiState.Playing(gameState.value)
    )

    init {
        generateCards() // Generate cards when the ViewModel is created

        // Observe changes in gameState and react when flippedCards reaches 2
        viewModelScope.launch {
            flippedCards.collect { state ->

                if (state == 2) {
                    delay(700)
                    if (checkCards()){
                        incrementScore()
                    }else{
                        flipBackCards()
                    }
                    checkGameOver()
                }
            }
        }
    }

    private fun checkGameOver() {
        if (_cards.value.all { it.found }) {
            _gameState.value = _gameState.value.copy(isGameOver = true)

        }
    }

    private fun generateCards() {

        val colors = listOf(
            "#FF0000",
            "#0000FF",
            "#00FF00"
        )
        val pairedCards = colors.flatMap { color ->
            listOf(
                Card(hexColor = color),
                Card(hexColor = color)
            )
        }
        _cards.value = pairedCards.shuffled()
    }

    fun incrementScore() {
        _flippedCards.value = 0
        _gameState.value= _gameState.value.copy(score = _gameState.value.score + 1)
    }

    fun flipCard(card: com.davidspartan.model.Card) {
        if (flippedCards.value == 2) return // Prevent flipping if two cards are already flipped
        // Update all cards with the same ID
       _cards.value = _cards.value.map { existingCard ->
            if (existingCard.id == card.id) {
                existingCard.copy(isFlipped = !existingCard.isFlipped) // Toggle the isFlipped state
            } else {
                existingCard
            }
        }

        // Update the game state for flippedCards count
        _flippedCards.value += 1
        _gameState.value = _gameState.value.copy(totalFlips = _gameState.value.totalFlips + 1)
    }

    fun flipBackCards() {
        // Update all cards with isFlipped = true to isFlipped = false
        _cards.value = _cards.value.map { card ->

            if (card.isFlipped && !card.found) card.copy(isFlipped = false) else card
        }

        // Reset the flippedCards count in the game state
        _flippedCards.value = 0
    }

    fun checkCards(): Boolean {
        // Find the two flipped cards
        val flippedCards = _cards.value.filter { it.isFlipped && !it.found }

        // Check if exactly two cards are flipped
        if (flippedCards.size == 2){

            if(flippedCards[0].hexColor == flippedCards[1].hexColor){

                _cards.value = _cards.value.map { existingCard ->
                    //Mark the two cards as found
                    if (existingCard.id == flippedCards[0].id || existingCard.id == flippedCards[1].id) {

                        existingCard.copy(found = true)

                    } else {
                        existingCard
                    }
                }
                return true
            }
        }

        return false

    }

    fun resetGame() {
        // Reset the game state to default values when starting a new game
        _gameState.value =
            GameState(score = 0, totalFlips = 0, isGameOver = false)
        generateCards() // Generate the cards again
    }

}

sealed interface GameUiState{
    data class Playing(val gameState:GameState): GameUiState
    data class GameOver(val gameState: GameState): GameUiState
}
