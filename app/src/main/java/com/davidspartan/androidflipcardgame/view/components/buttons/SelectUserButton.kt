package com.davidspartan.androidflipcardgame.view.components.buttons

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.davidspartan.androidflipcardgame.R


@Composable
fun SelectUserButton(username: String, onClick: () -> Unit) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = 327.dp, height = 52.dp)
            .shrinkOnPress{
                onClick.invoke()
            }


    ) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.user_button),
            contentDescription = "User Button"
        )
        Text(
            text = username,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.pally)),
            color = Color(0xFF425511)
        )
    }
}

// Shrinking effect extension function
@Composable
fun Modifier.shrinkOnPress(scaleFactor: Float = 0.9f,onClick: () -> Unit): Modifier {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleFactor else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    return this
        .scale(scale)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                    onClick.invoke()
                }
            )
        }
}