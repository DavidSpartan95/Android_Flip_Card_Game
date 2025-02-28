package com.davidspartan.model

data class Card(
    val id: String = java.util.UUID.randomUUID().toString(),
    val hexColor: String,
    var isFlipped: Boolean = false,
    var found: Boolean = false

)
