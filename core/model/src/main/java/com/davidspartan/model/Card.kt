package com.davidspartan.model

data class Card(
    val id: String = java.util.UUID.randomUUID().toString(),
    val color: String,
    var isFlipped: Boolean = false,
    var found: Boolean = false

)
