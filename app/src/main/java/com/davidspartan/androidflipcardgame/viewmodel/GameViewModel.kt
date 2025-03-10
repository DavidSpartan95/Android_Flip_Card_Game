package com.davidspartan.androidflipcardgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidspartan.gamelogic.GameRepository
import com.davidspartan.model.GameState
import com.davidspartan.model.Card
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    val uiState: StateFlow<GameUiState> = combine(
        repository.gameState,
        repository.cards
    ) { gameState, _ ->
        if (gameState.isGameOver) {
            GameUiState.GameOver(gameState)
        } else {
            GameUiState.Playing(gameState)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GameUiState.Playing(repository.gameState.value)
    )

    val cards: StateFlow<List<Card>> = repository.cards

    fun flipCard(card: Card) {
        repository.flipCard(card)
    }

    fun resetGame() {
        repository.resetGame()
    }
}

sealed interface GameUiState {
    data class Playing(val gameState: GameState) : GameUiState
    data class GameOver(val gameState: GameState) : GameUiState
}

