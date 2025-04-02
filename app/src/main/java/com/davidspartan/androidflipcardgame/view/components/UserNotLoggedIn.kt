package com.davidspartan.androidflipcardgame.view.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.view.navigation.NewUser

@Composable
fun UserNotLoggedInScreen(navController: NavHostController) {

    Text(text = "USER HAS NOT LOGGED IN")


    Button(onClick = {
        navController.navigate(NewUser) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }) {
        Text(text = "TAKE ME BACK TO LOGIN SCREEN")
    }
}