package com.davidspartan.androidflipcardgame.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.components.ThemedText
import com.davidspartan.androidflipcardgame.view.components.UserNotLoggedInScreen
import com.davidspartan.androidflipcardgame.view.components.buttons.PlayButton
import com.davidspartan.androidflipcardgame.view.components.buttons.ThemeButton
import com.davidspartan.androidflipcardgame.view.navigation.Appearance
import com.davidspartan.androidflipcardgame.view.navigation.Game
import com.davidspartan.androidflipcardgame.viewmodel.UserFlowViewModel
import com.davidspartan.androidflipcardgame.viewmodel.UserUiState
import com.davidspartan.database.realm.User

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: UserFlowViewModel
) {
    val uiState by viewModel.uiState.collectAsState()


    when (uiState) {

        is UserUiState.LoggedIn -> {

            val user = (uiState as UserUiState.LoggedIn).selectedUser

            HomeMenuContent(user, navController)

        }

        UserUiState.LoggedOut -> {

            UserNotLoggedInScreen(
                navController = navController
            )
        }
    }
}

@Composable
fun HomeMenuContent(user: User, navController: NavHostController) {
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
                UserMenuInfo(user)

                MenuButtons(user, navController)

            }
        }

        else -> { //Portrait
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(stringToColor(user.selectedTheme.primaryHexColor))
                    .padding(WindowInsets.statusBars.asPaddingValues()),
               horizontalAlignment = Alignment.CenterHorizontally
            ) {

                UserMenuInfo(user)
                Spacer(modifier = Modifier.weight(0.5f))
                MenuButtons(user, navController)
                Spacer(modifier = Modifier.weight(1f))

            }
        }

    }

}

@Composable
fun UserMenuInfo(user: User) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ThemedText(
            text = "User: ${user.name}",
            theme = user.selectedTheme
        )

        Spacer(modifier = Modifier.size(5.dp))

        ThemedText(
            text = "Points: ${user.score}",
            theme = user.selectedTheme
        )
    }
}

@Composable
fun MenuButtons(user: User, navController: NavHostController) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display content when a user is logged in
        PlayButton(
            text = "Play"
        ) {
            navController.navigate(Game)
        }

        Spacer(modifier = Modifier.size(16.dp))

        ThemeButton {
            navController.navigate(Appearance)
        }

        Spacer(modifier = Modifier.size(5.dp))

        OptionButton(
            text = "Go Back To Login",
            theme = user.selectedTheme
        ) {
            navController.navigateUp()

        }
    }

}