package com.davidspartan.androidflipcardgame.view.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.davidspartan.database.realm.AllThemes

@Composable
fun Background(content: @Composable () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF923FFF)),
        Alignment.Center
        ){
        DotGrid()
        content()
    }

}

@Composable
fun DotGrid() {
    val configuration = LocalConfiguration.current

    val columns = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 23
        else -> 11
    }
    val rows = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 11
        else -> 23
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        items(rows) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(columns) {
                    BackgroundDot()
                }
            }
        }
    }
}

@Composable
fun BackgroundDot() {
    Canvas(
        modifier = Modifier.size(10.dp) // Size of the dot
    ) {
        drawCircle(
            color = Color(0x1AFFFFFF),
            radius = size.minDimension / 2
        )
    }
}