package com.davidspartan.androidflipcardgame.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davidspartan.androidflipcardgame.view.GameScreen
import com.davidspartan.androidflipcardgame.view.HomeScreen
import com.davidspartan.androidflipcardgame.view.NewUserScreen
import com.davidspartan.androidflipcardgame.view.SettingsScreen
import com.davidspartan.androidflipcardgame.view.ThemeScreen
import com.davidspartan.androidflipcardgame.view.components.FlipCard
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel
import kotlinx.serialization.Serializable


@Composable
fun NavHost(
    navController: NavHostController,
    viewModel: UserRepositoryViewModel
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
            ThemeScreen()
        }
        composable<NewUser> {
            NewUserScreen(
                viewModel = viewModel,
                navController = navController
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

