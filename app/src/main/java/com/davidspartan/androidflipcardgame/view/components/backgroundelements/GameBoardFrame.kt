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
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.database.realm.Theme

@Composable
fun GameBoardFrame(theme: Theme, content: @Composable () -> Unit) {
    val originalColor = stringToColor(theme.secondaryHexColor)
    val lighterColor = originalColor.copy(alpha = 0.8f)

    Box(
        Modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x80000000)
            )
            .width(389.dp)
            .height(375.dp)
            .background(color = lighterColor, shape = RoundedCornerShape(size = 22.dp))
            .border(width = 10.dp, stringToColor(theme.primaryHexColor), shape = RoundedCornerShape(size = 22.dp)),
        Alignment.Center
    ) {
        content()
    }
}
