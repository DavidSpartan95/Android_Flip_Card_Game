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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidspartan.androidflipcardgame.R
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.database.realm.Theme

@Composable
fun GameResultFrame(score: Int, totalFlips: Int, theme: Theme) {
    val originalColor = stringToColor(theme.secondaryHexColor)
    val lighterColor = originalColor.copy(alpha = 0.8f)
    Box(
        Modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x80000000)
            )
            .width(258.dp)
            .height(166.dp)
            .background(color = lighterColor, shape = RoundedCornerShape(size = 22.dp))
            .border(width = 10.dp, stringToColor(theme.primaryHexColor), shape = RoundedCornerShape(size = 22.dp)),
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