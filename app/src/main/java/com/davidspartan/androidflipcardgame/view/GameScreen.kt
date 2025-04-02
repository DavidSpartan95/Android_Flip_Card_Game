package com.davidspartan.androidflipcardgame.view

import android.content.res.Configuration
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
import com.davidspartan.androidflipcardgame.view.components.FlipCard
import com.davidspartan.androidflipcardgame.view.components.FlipScoreTracker
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.components.ThemedText
import com.davidspartan.androidflipcardgame.view.components.UserNotLoggedInScreen
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.Background
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.GameBoardFrame
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.NamePlate
import com.davidspartan.androidflipcardgame.view.components.buttons.OrangeButton
import com.davidspartan.androidflipcardgame.view.components.buttons.WideOrangeButton
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
                    Background(
                        theme = selectedUser.selectedTheme
                    ) {
                        GameIsPlayingContent(
                            user = selectedUser,
                            gameViewModel = gameViewModel,
                            gameState = gameState,
                            navController = navController,
                            cards = cards
                        )
                    }
                }

                is GameUiState.GameOver -> {
                    val gameState = (gameUiState as GameUiState.GameOver).gameState
                    Background(
                        theme = selectedUser.selectedTheme
                    ) {
                        GameIsOverContent(
                            user = selectedUser,
                            gameState = gameState,
                            navController = navController,
                            gameViewModel = gameViewModel
                        )
                    }
                }
            }
        }

        UserUiState.LoggedOut -> {
            UserNotLoggedInScreen(
                navController = navController
            )
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
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    NamePlate(
                        name = user.name
                    )
                    Spacer(modifier = Modifier.size(27.dp))
                    FlipScoreTracker(
                        score = gameState.score,
                        totalFlips = gameState.totalFlips
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    OrangeButton(
                        text = "Go To Menu"
                    ) {
                        if(!isNavigating){
                            isNavigating = true
                            navController.navigateUp()
                        }
                    }
                    Spacer(modifier = Modifier.size(27.dp))
                }
                GameBoard(
                    cards = cards,
                    gameViewModel = gameViewModel
                )
            }
        }

        else -> { //Portrait
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(27.dp))

                NamePlate(
                    name = user.name
                )
                Spacer(modifier = Modifier.weight(0.5f))
                FlipScoreTracker(
                    score = gameState.score,
                    totalFlips = gameState.totalFlips
                )

                Spacer(modifier = Modifier.size(27.dp))
                
                GameBoard(
                    cards = cards,
                    gameViewModel = gameViewModel
                )
                
                Spacer(modifier = Modifier.weight(0.5f))

                WideOrangeButton(
                    text = "Go To Menu"
                ) {
                    if(!isNavigating){
                        isNavigating = true
                        navController.navigateUp()
                    }
                }
                Spacer(modifier = Modifier.size(27.dp))
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
            .fillMaxSize(),
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

@Composable
fun GameBoard(cards: List<Card>, gameViewModel: GameViewModel) {
    GameBoardFrame {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Split the list into two rows
            val firstRow = cards.take(3)  // First 3 cards
            val secondRow = cards.drop(3) // Last 3 cards

            Row{
                firstRow.forEach { card ->
                    FlipCard(
                        card = card
                    ) {
                        gameViewModel.flipCard(card)
                    }
                }
            }

            Row{
                secondRow.forEach { card ->
                    FlipCard(
                        card = card
                    ) {
                        gameViewModel.flipCard(card)
                    }
                }
            }
        }
    }
}