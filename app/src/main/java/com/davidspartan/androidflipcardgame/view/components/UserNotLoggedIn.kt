package com.davidspartan.androidflipcardgame.view.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.view.navigation.NewUser
import com.davidspartan.database.realm.AllThemes

@Composable
fun UserNotLoggedInScreen(navController: NavHostController) {

    ThemedText(
        text = "USER HAS BEEN LOGGED OUT",
        theme = AllThemes[0]
    )

    Button(onClick = {
        navController.navigate(NewUser) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }) {
        Text(text = "TAKE ME BACK TO LOGIN SCREEN")
    }
}