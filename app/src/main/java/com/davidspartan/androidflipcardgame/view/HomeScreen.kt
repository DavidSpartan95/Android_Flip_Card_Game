package com.davidspartan.androidflipcardgame.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.navigation.Appearance
import com.davidspartan.androidflipcardgame.view.navigation.Game
import com.davidspartan.androidflipcardgame.view.navigation.Settings
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: UserRepositoryViewModel
) {
    val selectedUser by viewModel.selectedUser.collectAsState(initial = null)
    val selectedTheme by viewModel.selectedTheme.collectAsState(initial = null)


    if (selectedUser == null || selectedTheme == null) {
        // Show message when no user is logged in
        Text(
            text = "No user is logged in.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(stringToColor(selectedTheme!!.primaryHexColor)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("TOTAL SCORE, ${selectedUser!!.score}!")
            // Display content when a user is logged in
            OptionButton(
                text = "Play",
                theme = selectedTheme!!
            ) {
                navController.navigate(Game)
            }

            Spacer(modifier = Modifier.size(5.dp))

            OptionButton(
                text = "Themes",
                theme = selectedTheme!!
            ) { navController.navigate(Appearance) }

            Spacer(modifier = Modifier.size(5.dp))

            OptionButton(
                text = "Settings",
                theme = selectedTheme!!
            ) { navController.navigate(Settings) }
        }
    }
}

