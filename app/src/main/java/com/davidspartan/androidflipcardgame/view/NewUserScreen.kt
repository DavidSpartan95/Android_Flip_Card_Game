package com.davidspartan.androidflipcardgame.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.model.isValidUsernameForUser
import com.davidspartan.androidflipcardgame.view.navigation.Home
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel

@Composable
fun NewUserScreen(
    navController: NavHostController,
    viewModel: UserRepositoryViewModel
) {
    val users by viewModel.users.collectAsState(initial = emptyList()) // Provide an initial value
    val showPopup = remember { mutableStateOf(false) } // Track if popup is visible

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()


    ) {

        Column(
            Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                showPopup.value = true
            }) {
                Text("Create New User")
            }
            users.forEach { user ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp) // Add spacing around each rectangle
                        .background(
                            color = MaterialTheme.colorScheme.primary, // Background color with some transparency
                            shape = RoundedCornerShape(8.dp) // Rounded corners for the rectangle
                        )
                        .padding(8.dp) // Padding inside the rectangle
                        .clickable {
                            viewModel.selectUser(user)
                            navController.navigate(Home)
                        }
                        .width(250.dp)
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary // Text color contrasting the background
                    )
                }
            }
        }


        // Show popup with darkened or blurred background
        if (showPopup.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Darkened background
                    .clickable { showPopup.value = false } // Dismiss popup on background click
            ) {
                PopupBox(viewModel, onDismiss = { showPopup.value = false })
            }
        }

    }
}

@Composable
fun PopupBox(
    viewModel: UserRepositoryViewModel,
    onDismiss: () -> Unit
) {
    var newUserName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .size(250.dp)
                .fillMaxWidth()
                .padding(2.dp)
                .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(20.dp)
        ) {
            Text("No users found. Please create a new user.")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newUserName,
                onValueChange = { newUserName = it },
                label = { Text("User Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    if (isValidUsernameForUser(newUserName)) {
                        viewModel.addUser(name = newUserName)
                        onDismiss()
                    }else{
                        Toast.makeText(
                            context,
                            "Invalid username. Only letters and numbers allowed (max 16 characters).",
                            Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create User")
            }
        }
    }
}