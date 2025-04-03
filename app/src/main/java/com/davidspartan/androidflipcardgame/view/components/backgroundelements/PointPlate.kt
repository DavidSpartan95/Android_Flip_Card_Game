package com.davidspartan.androidflipcardgame.view.components.backgroundelements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.davidspartan.androidflipcardgame.R
import com.davidspartan.androidflipcardgame.view.components.AutoResizedText

@Composable
fun PointPlate(points: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = 121.dp, height = 56.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.point_plate),
            contentDescription = "point plate"
        )
        AutoResizedText(
            text = points,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.pally)),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier,
            color = Color.White
        )
    }
}