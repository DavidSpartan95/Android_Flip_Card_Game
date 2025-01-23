package com.davidspartan.androidflipcardgame.model

import androidx.compose.ui.graphics.Color

//This function takes a string (example format "#FFFFFF") and returns a Color object.
fun stringToColor(color: String): Color {

    return Color(android.graphics.Color.parseColor(color))
}