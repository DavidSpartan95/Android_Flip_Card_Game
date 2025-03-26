package com.davidspartan.androidflipcardgame.view.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.view.navigation.NewUser

@Composable
fun UserNotLoggedInScreen(navController: NavHostController) {
    LaunchedEffect(true){
        // This will pop the stack and navigate to the login screen
        navController.navigate(NewUser) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }
}