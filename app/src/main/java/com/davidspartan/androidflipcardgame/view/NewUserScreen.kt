package com.davidspartan.androidflipcardgame.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel

@Composable
fun NewUserScreen(
    viewModel: UserRepositoryViewModel
) {
    val users by viewModel.users.collectAsState(initial = emptyList()) // Provide an initial value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("New User Screen", style = MaterialTheme.typography.bodyMedium)

        if (users.isNotEmpty()) {
            val firstUser = users[0]
            Text("User Name: ${firstUser.name}")
            Button(onClick = {
                viewModel.editUser(
                firstUser.id,
                newName = "William Dobby",
                incrementScore = true
            )})
            { Text("Change Name") }
            if (firstUser.themes.isNotEmpty()) {
                Text("Theme Name: ${firstUser.themes[0].name}")
            } else {
                Text("No themes assigned.")
            }
        } else {
            Text("No users found.")
        }
    }
}
