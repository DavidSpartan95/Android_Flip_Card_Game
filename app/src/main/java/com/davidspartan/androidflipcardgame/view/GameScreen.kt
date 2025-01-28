package com.davidspartan.androidflipcardgame.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.FlipCard
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.viewmodel.GameViewModel
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel

@Composable
fun GameScreen(
    navController: NavHostController,
    viewModel: UserRepositoryViewModel,
) {
    var gameViewModel = remember { GameViewModel() }
    val selectedUser by viewModel.selectedUser.collectAsState(initial = null)
    val cards by gameViewModel.cards.collectAsState()
    val gameState by gameViewModel.gameState.collectAsState()

    LaunchedEffect(gameState.isGameOver) {
        print("Game over state changed: ${gameState.isGameOver}")
        viewModel.addScore(selectedUser!!.id, gameState.score)

    }

    if (selectedUser == null) {
        // Show message when no user is logged in
        Text(
            text = "No user is logged in.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    } else if (!gameState.isGameOver) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(stringToColor(selectedUser!!.selectedTheme!!.primaryHexColor)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(selectedUser!!.name)
            Text("Current score is ${gameState.score}")
            Text("Current flipped cards is ${gameState.totalFlips}")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 3 cards per row
                modifier = Modifier
                    .background(stringToColor(selectedUser!!.selectedTheme!!.primaryHexColor)),
                verticalArrangement = Arrangement.Center
            ) {
                items(cards) { card -> // Iterate over each card directly

                    FlipCard(
                        theme = selectedUser!!.selectedTheme!!,
                        card = card
                    ){
                        gameViewModel.flipCard(
                            card = card
                        )
                    }
                }
            }
        }
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(stringToColor(selectedUser!!.selectedTheme!!.primaryHexColor)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Game Over")
            OptionButton(
                text = "New Game",
                theme = selectedUser!!.selectedTheme!!
            ) {
                gameViewModel.resetGame()
            }
        }
    }
}