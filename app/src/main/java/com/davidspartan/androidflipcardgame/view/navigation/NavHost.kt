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
import kotlinx.serialization.Serializable


@Composable
fun NavHost(
    navController: NavHostController,
){
    NavHost(navController,startDestination = Home) {
        composable<Home> {
            HomeScreen()
        }
        composable<Game> {
            GameScreen()
        }
        composable<Settings> {
            SettingsScreen()
        }
        composable<Theme> {
            ThemeScreen()
        }
        composable<NewUser> {
            NewUserScreen()
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
object Theme

@Serializable
object NewUser

