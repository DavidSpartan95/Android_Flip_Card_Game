package com.davidspartan.androidflipcardgame.view.components.backgroundelements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
fun NamePlate(name: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = 168.dp, height = 56.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.name_plate),
            contentDescription = "name plate"
        )
        AutoResizedText(
            text =name,
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