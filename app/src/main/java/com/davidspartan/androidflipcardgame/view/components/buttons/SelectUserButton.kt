package com.davidspartan.androidflipcardgame.view.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
fun SelectUserButton(username: String, onClick: () -> Unit) {

    Box(contentAlignment = Alignment.Center){
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.user_button),
            contentDescription = "User Button",
            modifier = Modifier
                .size(width = 327.dp, height = 52.dp)
                .clickable { onClick.invoke() }
        )
        Text(
            text = username,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.pally)),
            color = Color(0xFF425511)
        )
    }
}