package com.davidspartan.androidflipcardgame.model

import com.davidspartan.androidflipcardgame.model.realm.Theme

val AllThemes = listOf(
    Theme().apply {
        name = "Default"
        primaryHexColor = "#7b9acc"
        secondaryHexColor = "#FCF6F5"
        textHexColor = "#7b9acc"
        price = 0
    },
    Theme().apply {
        name = "Forest"
        primaryHexColor = "#07553B"
        secondaryHexColor = "#CED46A"
        textHexColor = "#07553B"
        price = 10
    },
    Theme().apply {
        name = "Lavender"
        primaryHexColor = "#50586C"
        secondaryHexColor = "#DCE2F0"
        textHexColor = "#50586C"
        price = 20
    },
    Theme().apply {
        name = "Wood"
        primaryHexColor = "#F9EBDE"
        secondaryHexColor = "#815854"
        textHexColor = "#F9EBDE"
        price = 30
    }
)