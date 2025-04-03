package com.davidspartan.androidflipcardgame.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.davidspartan.androidflipcardgame.R
import com.davidspartan.model.Card
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.buttons.shrinkOnPress

@Composable
fun FlipCard(
    card: Card,
    flipAction: () -> Unit
){
    // Bind the `isFlipped` state to `card.isFlipped`
    val isFlipped by rememberUpdatedState(card.isFlipped)
    val colorTag = "card_color_${card.hexColor}" // Convert color to a unique tag

    // Animate the rotation angle
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600) // Customize the duration as needed
    )

    // Container to detect clicks and handle the flip logic
    Box(
        modifier = Modifier
            .padding(6.dp)
            .size(width = 106.dp, height = 154.dp)
            .shrinkOnPress {
                if (!isFlipped) flipAction.invoke()
            }
            .graphicsLayer {
                rotationY = rotation // Apply the rotation animation
                cameraDistance = 12f * density // Adjust for better 3D effect
            }
            .testTag(colorTag),

        contentAlignment = Alignment.Center
    ){

        if (rotation <= 90f) {
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(8.dp),

            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = R.drawable.card_backside),
                    contentDescription = "Card Back",
                    contentScale = ContentScale.Crop,
                )
            }

        } else {
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .graphicsLayer {
                        // Counter-rotate to ensure the image is not mirror reverted
                        rotationY = 180f
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 106.dp, height = 154.dp)
                        .background(stringToColor(card.hexColor))
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.android),
                        contentDescription = "Card Back"
                    )
                }
            }
        }
    }
}
