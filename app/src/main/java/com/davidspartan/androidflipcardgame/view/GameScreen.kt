package com.davidspartan.androidflipcardgame.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.FlipCard
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.components.ThemedText
import com.davidspartan.androidflipcardgame.view.components.UserNotLoggedInScreen
import com.davidspartan.androidflipcardgame.viewmodel.GameUiState
import com.davidspartan.androidflipcardgame.viewmodel.GameViewModel
import com.davidspartan.androidflipcardgame.viewmodel.UserFlowViewModel
import com.davidspartan.androidflipcardgame.viewmodel.UserUiState
import com.davidspartan.database.realm.User
import com.davidspartan.model.Card
import com.davidspartan.model.GameState
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(
    navController: NavHostController,
    viewModel: UserFlowViewModel,
    gameViewModel: GameViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val gameUiState by gameViewModel.uiState.collectAsState()
    val cards by gameViewModel.cards.collectAsState()

    when (uiState) {

        is UserUiState.LoggedIn -> {

            val selectedUser = (uiState as UserUiState.LoggedIn).selectedUser

            when (gameUiState) {

                is GameUiState.Playing -> {
                    val gameState = (gameUiState as GameUiState.Playing).gameState

                    GameIsPlayingContent(
                        user = selectedUser,
                        gameViewModel = gameViewModel,
                        gameState = gameState,
                        navController = navController,
                        cards = cards
                    )
                }

                is GameUiState.GameOver -> {
                    val gameState = (gameUiState as GameUiState.GameOver).gameState

                    GameIsOverContent(
                        user = selectedUser,
                        gameState = gameState,
                        navController = navController,
                        gameViewModel = gameViewModel
                    )

                }
            }
        }

        UserUiState.LoggedOut -> {
            UserNotLoggedInScreen()
        }
    }
}

@Composable
fun GameIsPlayingContent(
    user: User,
    gameViewModel: GameViewModel,
    gameState: GameState,
    navController: NavHostController,
    cards: List<Card>
) {
    var isNavigating by rememberSaveable { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> { //Landscape
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(stringToColor(user.selectedTheme.primaryHexColor))
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ThemedText(
                        text = "${user.name}\nCurrent score is ${gameState.score}\nTotal flips ${gameState.totalFlips}",
                        theme = user.selectedTheme
                    )
                    Spacer(modifier = Modifier.size(50.dp))

                    OptionButton(
                        text = "Go To Menu",
                        theme = user.selectedTheme
                    ) {
                        if(!isNavigating){
                            isNavigating = true
                            navController.navigateUp()
                        }
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 cards per row
                    modifier = Modifier
                        .background(stringToColor(user.selectedTheme.primaryHexColor)),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(cards) { card -> // Iterate over each card directly

                        FlipCard(
                            theme = user.selectedTheme,
                            card = card
                        ) {
                            gameViewModel.flipCard(
                                card = card
                            )
                        }
                    }
                }

            }
        }

        else -> { //Portrait
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(stringToColor(user.selectedTheme.primaryHexColor)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ThemedText(
                    text = "${user.name}\nCurrent score is ${gameState.score}\nTotal flips ${gameState.totalFlips}",
                    theme = user.selectedTheme
                )
                Spacer(modifier = Modifier.size(50.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 cards per row
                    modifier = Modifier
                        .background(stringToColor(user.selectedTheme.primaryHexColor)),
                    verticalArrangement = Arrangement.Center
                ) {
                    items(cards) { card -> // Iterate over each card directly

                        FlipCard(
                            theme = user.selectedTheme,
                            card = card
                        ) {
                            gameViewModel.flipCard(
                                card = card
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(50.dp))

                OptionButton(
                    text = "Go To Menu",
                    theme = user.selectedTheme
                ) {
                    if(!isNavigating){
                        isNavigating = true
                        navController.navigateUp()
                    }
                }
            }
        }
    }

}

@Composable
fun GameIsOverContent(
    user: User,
    gameState: GameState,
    navController: NavHostController,
    gameViewModel: GameViewModel
) {
    var isNavigating by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(stringToColor(user.selectedTheme.primaryHexColor)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemedText(
            text = "Result\nScore: ${gameState.score}\nTotal flips: ${gameState.totalFlips}",
            theme = user.selectedTheme
        )
        Spacer(modifier = Modifier.size(50.dp))
        OptionButton(
            text = "New Game",
            theme = user.selectedTheme
        ) {
            gameViewModel.resetGame()
        }
        Spacer(modifier = Modifier.size(8.dp))
        OptionButton(
            text = "Go To Menu",
            theme = user.selectedTheme
        ) {
            if(!isNavigating){
                isNavigating = true
                navController.navigateUp()
            }
        }
    }
}