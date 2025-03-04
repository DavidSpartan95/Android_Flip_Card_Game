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
import kotlinx.serialization.Serializable


@Composable
fun NavHost(
    navController: NavHostController,
){
    NavHost(navController,startDestination = NewUser) {
        composable<Home> {
            HomeScreen(
                navController = navController,
            )
        }
        composable<Game> {
            GameScreen(
                navController = navController,
            )
        }
        composable<Settings> {
            SettingsScreen()
        }
        composable<Appearance> {
            ThemeScreen(
                navController = navController,
            )
        }
        composable<NewUser> {
            SelectUserScreen(
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

