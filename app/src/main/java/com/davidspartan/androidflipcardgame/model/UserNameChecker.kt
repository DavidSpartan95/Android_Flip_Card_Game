package com.davidspartan.androidflipcardgame.model

fun isValidUsernameForUser(username: String): Boolean {
    // Regular expression to allow only letters and numbers
    val regex = "^[a-zA-Z0-9]{1,16}$".toRegex()
    return regex.matches(username)
}