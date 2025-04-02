package com.davidspartan.androidflipcardgame.view.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidspartan.androidflipcardgame.R
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.buttons.shrinkOnPress
import com.davidspartan.database.realm.Theme

@Composable
fun ThemeCard(text: String, theme: Theme, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                spotColor = Color(0x26000000),
                ambientColor = Color(0x26000000)
            )
            .padding(3.dp)
            .size(width = 106.dp, height = 154.dp)
            .background(
                stringToColor(color = theme.primaryHexColor),
                shape = RoundedCornerShape(8.dp)
            )
            .shrinkOnPress {
                onClick.invoke()
            },
        Alignment.Center
    ) {
        DotGrid()
        ThemedText(
            text = text,
            theme = theme
        )
        if (selected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selected",
                tint = stringToColor("#FEB93A"),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp) // Adjust size if needed
            )
        }
    }
}

@Composable
private fun DotGrid() {

    val columns = 9
    val rows = 12

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
                    Canvas(
                        modifier = Modifier.size(2.5.dp) // Size of the dot
                    ) {
                        drawCircle(
                            color = Color(0x1AFFFFFF),
                            radius = size.minDimension / 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemedText(text: String, theme: Theme) {

    Row(
        modifier = Modifier
            .size(width = 84.dp, height = 28.6.dp)
            .background(
                color = stringToColor(color = theme.secondaryHexColor),
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AutoResizedText(
            text = text,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.pally)),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier,
            color = Color.White
        )
    }
}