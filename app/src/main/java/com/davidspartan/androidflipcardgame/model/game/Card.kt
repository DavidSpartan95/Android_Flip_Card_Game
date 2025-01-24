package com.davidspartan.androidflipcardgame.model.game

data class Card(
    val id: String = java.util.UUID.randomUUID().toString(),
    val color: androidx.compose.ui.graphics.Color,
    var isFlipped: Boolean = false,
    var found: Boolean = false

)
