package com.davidspartan.androidflipcardgame.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.AppBackground
import com.davidspartan.androidflipcardgame.view.components.UserNotLoggedInScreen
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.NamePlate
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.PointPlate
import com.davidspartan.androidflipcardgame.view.components.buttons.OrangeButton
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
            AppBackground(
                theme = user.selectedTheme
            ) {
                HomeMenuContent(user, navController)
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
fun HomeMenuContent(user: User, navController: NavHostController) {
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
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NamePlate(
                        name = user.name
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    PointPlate(
                        name = "Points: ${user.score}"
                    )
                }

                MenuButtons(false,navController)

            }
        }

        else -> { //Portrait
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
               horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(14.dp))
                    NamePlate(
                        name = user.name
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    PointPlate(
                        name = "Points: ${user.score}"
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                }

                MenuButtons(
                    portrait = true,
                    navController = navController
                )

            }
        }

    }

}


@Composable
fun MenuButtons(portrait: Boolean, navController: NavHostController) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (portrait){
            Spacer(modifier = Modifier.weight(0.4f))
        }
        PlayButton(
            text = "Play"
        ) {
            navController.navigate(Game)
        }

        Spacer(modifier = Modifier.size(16.dp))
        ThemeButton {
            navController.navigate(Appearance)
        }

        Spacer(modifier = Modifier.weight(0.6f))

        OrangeButton(
            text = "Go Back To Login"
        ) {
            navController.navigateUp()
        }
        Spacer(modifier = Modifier.size(64.dp))
    }

}