package com.davidspartan.androidflipcardgame.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.view.navigation.Home
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel

@Composable
fun NewUserScreen(
    navController: NavHostController,
    viewModel: UserRepositoryViewModel
) {
    val users by viewModel.users.collectAsState(initial = emptyList()) // Provide an initial value
    val selectedUser by viewModel.selectedUser.collectAsState(initial = null)

    var newUserName by remember { mutableStateOf("username") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("New User Screen", style = MaterialTheme.typography.bodyMedium)

        if (selectedUser != null) {
            Text("Selected User: ${selectedUser?.name}")
        }

        if (users.isNotEmpty()) {
            Column {
                users.forEach { user ->
                    Box(
                        modifier = Modifier
                            .padding(8.dp) // Add spacing around each rectangle
                            .background(
                                color = MaterialTheme.colorScheme.primary, // Background color with some transparency
                                shape = RoundedCornerShape(8.dp) // Rounded corners for the rectangle
                            )
                            .padding(16.dp) // Padding inside the rectangle
                            .clickable {
                                viewModel.selectUser(user)
                                navController.navigate(Home)
                            }
                    ) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary // Text color contrasting the background
                        )
                    }
                }
            }
        } else {
            // Display a form to create a new user if no users are found
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No users found. Please create a new user.")

                Spacer(modifier = Modifier.height(16.dp))

                // TextInput for the new user name
                OutlinedTextField(
                    value = newUserName,
                    onValueChange = { newUserName = it },
                    label = { Text("User Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button to create the user
                Button(
                    onClick = {
                        if (newUserName.isNotEmpty()) {
                            viewModel.addUser(
                                name = newUserName
                            ) // Call the function to add the user
                            //navController.navigate(Home) // Navigate to the Home screen after adding
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create User")
                }
            }
        }
    }
}
