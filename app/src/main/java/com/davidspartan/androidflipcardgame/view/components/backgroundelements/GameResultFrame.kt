package com.davidspartan.androidflipcardgame.view.components.backgroundelements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidspartan.androidflipcardgame.R

@Composable
fun GameResultFrame(score: Int, totalFlips: Int) {
    Box(
        Modifier
            .shadow(elevation = 52.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
            .width(258.dp)
            .height(166.dp)
            .background(color = Color(0xFF7410CB), shape = RoundedCornerShape(size = 22.dp))
            .border(width = 10.dp, color = Color(0xFF9A4DFF), shape = RoundedCornerShape(size = 22.dp)),
        Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Results",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.pally)),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(23.dp))
            Text(
                text = "Score: $score",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.pally)),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text(
                text = "Total flips: $totalFlips",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.pally)),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}