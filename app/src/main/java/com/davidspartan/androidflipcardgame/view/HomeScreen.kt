package com.davidspartan.androidflipcardgame.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.components.ThemedText
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


    if (selectedUser == null) {
        // Show message when no user is logged in
        Text(
            text = "No user is logged in.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(stringToColor(selectedUser!!.selectedTheme!!.primaryHexColor))
                .padding(WindowInsets.statusBars.asPaddingValues())
            ,
            Alignment.TopCenter
        ){
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                ThemedText(
                    text = "User: ${selectedUser!!.name}",
                    theme = selectedUser!!.selectedTheme!!
                )

                Spacer(modifier = Modifier.size(5.dp))

                ThemedText(
                    text = "Points: ${selectedUser!!.score}",
                    theme = selectedUser!!.selectedTheme!!
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display content when a user is logged in
                OptionButton(
                    text = "Play",
                    theme = selectedUser!!.selectedTheme!!
                ) {
                    navController.navigate(Game)
                }

                Spacer(modifier = Modifier.size(5.dp))

                OptionButton(
                    text = "Themes",
                    theme = selectedUser!!.selectedTheme!!
                ) { navController.navigate(Appearance) }

                Spacer(modifier = Modifier.size(5.dp))

                OptionButton(
                    text = "Settings",
                    theme = selectedUser!!.selectedTheme!!
                ) { navController.navigate(Settings) }
            }
        }
    }
}

