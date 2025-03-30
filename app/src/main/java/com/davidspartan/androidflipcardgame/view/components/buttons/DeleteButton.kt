package com.davidspartan.androidflipcardgame.view.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.davidspartan.androidflipcardgame.R

@Composable
fun DeleteButton(onClick: () -> Unit) {
    Image(
        painter = rememberAsyncImagePainter(model = R.drawable.delete_button),
        contentDescription = "User Button",
        modifier = Modifier
            .size(width = 50.dp, height = 50.dp)
            .clickable { onClick.invoke() }
    )
}