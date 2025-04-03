package com.davidspartan.androidflipcardgame.view.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.davidspartan.androidflipcardgame.R

@Composable
fun ThemeButton(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = 240.dp, height = 56.dp)
            .shrinkOnPress{
                onClick.invoke()
            }


    ) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.theme_nav_button),
            contentDescription = "User Button"
        )
        Text(
            text = "Themes",
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.pally)),
            color = Color.White
        )
    }
}