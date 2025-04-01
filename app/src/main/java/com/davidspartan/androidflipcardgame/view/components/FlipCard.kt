package com.davidspartan.androidflipcardgame.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.davidspartan.androidflipcardgame.R
import com.davidspartan.model.Card
import com.davidspartan.database.realm.Theme
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.buttons.shrinkOnPress

@Composable
fun FlipCard(
    theme: Theme,
    card: Card,
    flipAction: () -> Unit
){
    // Bind the `isFlipped` state to `card.isFlipped`
    val isFlipped by rememberUpdatedState(card.isFlipped)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardSize = if (screenWidth < screenHeight) screenWidth * 0.25f else screenHeight * 0.25f
    val colorTag = "card_color_${card.hexColor}" // Convert color to a unique tag

    // Animate the rotation angle
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600) // Customize the duration as needed
    )

    // Container to detect clicks and handle the flip logic
    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(width = 106.dp, height = 154.dp) // Set size to 25% of screen width
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
                colors = CardDefaults.cardColors(containerColor = stringToColor(theme.secondaryHexColor))

            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = R.drawable.card_backside),
                    contentDescription = "Card Back",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

        } else {
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .graphicsLayer {
                        // Counter-rotate to ensure the image is not mirror reverted
                        rotationY = 180f
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.android_card_front),
                    contentDescription = "Card Back",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = ColorFilter.tint(stringToColor(card.hexColor))
                )
            }
        }
    }
}
