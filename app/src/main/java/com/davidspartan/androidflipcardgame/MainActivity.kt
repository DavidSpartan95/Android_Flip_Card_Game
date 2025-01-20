package com.davidspartan.androidflipcardgame

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.davidspartan.androidflipcardgame.ui.theme.AndroidFlipCardGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidFlipCardGameTheme {
                FlipCard()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidFlipCardGameTheme {
        Greeting("Android")
    }
}

@Composable
fun FlipCard() {
    // State to track whether the card is flipped
    var isFlipped by remember { mutableStateOf(false) }

    // Animate the rotation angle
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600) // Customize the duration as needed
    )

    // Container to detect clicks and handle the flip logic
    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(200.dp) // Adjust the size as needed
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                rotationY = rotation // Apply the rotation animation
                cameraDistance = 12f * density // Adjust for better 3D effect
            },
        contentAlignment = Alignment.Center
    ) {
        // Conditional rendering based on the rotation
        if (rotation <= 90f) {
            FrontCard()
        } else {
            BackCard(
                rotation = rotation
            )
        }
    }
}

@Composable
fun FrontCard() {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Front Title", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Front Subtitle", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun BackCard(rotation: Float) {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier
                .graphicsLayer {
                    // Counter-rotate to ensure text is upright
                    rotationY = 180f
                }
                .padding(16.dp)
        ) {
            Text(text = "Back Title", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSecondary)
            Text(text = "Back Subtitle", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}



