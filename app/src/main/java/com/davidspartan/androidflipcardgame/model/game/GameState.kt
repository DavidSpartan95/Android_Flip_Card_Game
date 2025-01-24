package com.davidspartan.androidflipcardgame.model.game

data class GameState(
    var score: Int = 0,
    var totalFlips: Int = 0,
    var isGameOver: Boolean = false

) {

}