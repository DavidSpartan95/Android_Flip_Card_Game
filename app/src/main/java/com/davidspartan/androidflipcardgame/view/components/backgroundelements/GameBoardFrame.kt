package com.davidspartan.androidflipcardgame.view.components.backgroundelements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GameBoardFrame(content: @Composable () -> Unit) {
    Box(
        Modifier
            .shadow(elevation = 52.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
            .width(389.dp)
            .height(375.dp)
            .background(color = Color(0xFF7410CB), shape = RoundedCornerShape(size = 22.dp))
            .border(width = 10.dp, color = Color(0xFF9A4DFF), shape = RoundedCornerShape(size = 22.dp)),
        Alignment.Center
    )
    {
        content()
    }

}