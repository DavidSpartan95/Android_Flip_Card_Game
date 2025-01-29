package com.davidspartan.androidflipcardgame.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidspartan.androidflipcardgame.model.realm.Theme
import com.davidspartan.androidflipcardgame.model.stringToColor

@Composable
fun ThemedText(text: String, theme: Theme) {

    Text(
        text = text,
        color = stringToColor(theme.textHexColor),
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .background(
                color = stringToColor(theme.secondaryHexColor),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),

        )
}