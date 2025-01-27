package com.davidspartan.androidflipcardgame.model

import com.davidspartan.androidflipcardgame.model.realm.Theme

val AllThemes = listOf(
    Theme().apply {
        name = "Default"
        primaryHexColor = "#FFFFFF"
        secondaryHexColor = "#000000"
        textHexColor = "#FFFFFF"
    },
    Theme().apply {
        name = "Forest"
        primaryHexColor = "#07553B"
        secondaryHexColor = "#CED46A"
        textHexColor = "#07553B"
    },
    Theme().apply {
        name = "Lavender"
        primaryHexColor = "#50586C"
        secondaryHexColor = "#DCE2F0"
        textHexColor = "#50586C"
    },
    Theme().apply {
        name = "Wood"
        primaryHexColor = "#F9EBDE"
        secondaryHexColor = "#815854"
        textHexColor = "#F9EBDE"
    }
)