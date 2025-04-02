package com.davidspartan.androidflipcardgame.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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

@Composable
fun FlipScoreTracker(score: Int, totalFlips: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = 218.dp, height = 52.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.purple_plate),
            contentDescription = "tracker plate"
        )
        Tracker(
            score = score,
            totalFlips = totalFlips
        )
    }
}

@Composable
private fun Tracker(score: Int, totalFlips: Int) {

    Row {
        ScoreCounter(score)
        FlipCounter(totalFlips)
    }
}

@Composable
private fun FlipCounter(totalFlips: Int) {
    Row {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = 30.dp, height = 30.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.rotate_card_icon),
                contentDescription = "tracker plate"
            )
        }
        AutoResizedText(
            text = "$totalFlips",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.pally)),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier,
            color = Color.White
        )
    }
}

@Composable
private fun ScoreCounter(score: Int, maxScore: Int = 3) {
    Row {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = 30.dp, height = 30.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.target_icon),
                contentDescription = "tracker plate"
            )
        }
        AutoResizedText(
            text = "$score/$maxScore",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.pally)),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier,
            color = Color.White
        )
    }

}