package com.davidspartan.androidflipcardgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.davidspartan.androidflipcardgame.view.navigation.NavHost
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel = UserRepositoryViewModel()

            NavHost(navController,viewModel)
        }
    }
}





