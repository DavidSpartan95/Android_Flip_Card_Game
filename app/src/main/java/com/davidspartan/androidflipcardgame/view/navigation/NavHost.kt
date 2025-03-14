package com.davidspartan.androidflipcardgame.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davidspartan.androidflipcardgame.view.GameScreen
import com.davidspartan.androidflipcardgame.view.HomeScreen
import com.davidspartan.androidflipcardgame.view.SelectUserScreen
import com.davidspartan.androidflipcardgame.view.SettingsScreen
import com.davidspartan.androidflipcardgame.view.ThemeScreen
import com.davidspartan.androidflipcardgame.viewmodel.UserFlowViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavHost(
    navController: NavHostController,
    viewModel: UserFlowViewModel = koinViewModel()
){

    NavHost(navController,startDestination = NewUser) {
        composable<Home> {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<Game> {
            GameScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<Settings> {
            SettingsScreen()
        }
        composable<Appearance> {
            ThemeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<NewUser> {
            SelectUserScreen(
                navController = navController
                ,viewModel = viewModel
            )
        }
    }
}

@Serializable
object Home

@Serializable
object Game

@Serializable
object Settings

@Serializable
object Appearance

@Serializable
object NewUser

