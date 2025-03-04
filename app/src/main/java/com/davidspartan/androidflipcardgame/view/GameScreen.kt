package com.davidspartan.androidflipcardgame.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.FlipCard
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.components.ThemedText
import com.davidspartan.androidflipcardgame.viewmodel.GameViewModel
import com.davidspartan.androidflipcardgame.viewmodel.UserFlowViewModel
import com.davidspartan.androidflipcardgame.viewmodel.UserUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(
    navController: NavHostController,
    viewModel: UserFlowViewModel = koinViewModel(),
    gameViewModel: GameViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cards by gameViewModel.cards.collectAsState()
    val gameState by gameViewModel.gameState.collectAsState()

    LaunchedEffect(gameState.isGameOver) {
        print("Game over state changed: ${gameState.isGameOver}")
        val selectedUser = (uiState as UserUiState.LoggedIn).selectedUser
        viewModel.addScore(selectedUser.id, gameState.score)

    }

    when(uiState){

        is UserUiState.LoggedIn -> {
            val selectedUser = (uiState as UserUiState.LoggedIn).selectedUser

            if (!gameState.isGameOver) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(stringToColor(selectedUser!!.selectedTheme!!.primaryHexColor)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    ThemedText(
                        text = "${selectedUser!!.name}\nCurrent score is ${gameState.score}\nTotal flips ${gameState.totalFlips}",
                        theme = selectedUser!!.selectedTheme!!
                    )
                    Spacer(modifier = Modifier.size(50.dp))
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

                    Spacer(modifier = Modifier.size(50.dp))

                    OptionButton(
                        text = "Go To Menu",
                        theme = selectedUser!!.selectedTheme!!
                    ) {
                        navController.navigateUp()
                    }
                }
            }else{

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(stringToColor(selectedUser!!.selectedTheme!!.primaryHexColor))
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    ThemedText(
                        text = "Result\nScore: ${gameState.score}\nTotal flips: ${gameState.totalFlips}",
                        theme = selectedUser!!.selectedTheme!!
                    )
                    Spacer(modifier = Modifier.size(50.dp))
                    OptionButton(
                        text = "New Game",
                        theme = selectedUser!!.selectedTheme!!
                    ) {
                        gameViewModel.resetGame()
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    OptionButton(
                        text = "Go To Menu",
                        theme = selectedUser!!.selectedTheme!!
                    ) {
                        navController.navigateUp()
                    }
                }
            }
        }
        UserUiState.LoggedOut -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "No user is logged in.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}