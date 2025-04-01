package com.davidspartan.androidflipcardgame.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidspartan.database.realm.AllThemes
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.Background
import com.davidspartan.androidflipcardgame.view.components.DialogPopup
import com.davidspartan.androidflipcardgame.view.components.buttons.WideOrangeButton
import com.davidspartan.androidflipcardgame.view.components.buttons.DeleteButton
import com.davidspartan.androidflipcardgame.view.components.buttons.SelectUserButton
import com.davidspartan.androidflipcardgame.view.navigation.Home
import com.davidspartan.androidflipcardgame.viewmodel.UserFlowViewModel
import com.davidspartan.database.realm.User


@Composable
fun SelectUserScreen(
    navController: NavHostController,
    viewModel: UserFlowViewModel
) {
    val users by viewModel.users.collectAsState(initial = emptyList()) // Provide an initial value
    val showPopup = rememberSaveable { mutableStateOf(false) } // Track if popup is visible
    val showDeleteDialog = rememberSaveable { mutableStateOf(false) }
    var userSelected by remember { mutableStateOf<User?>(null) }
    val context = LocalContext.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val theme = viewModel.getSelectedUser() ?: AllThemes[0]
    Background(
        theme = theme
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                Modifier
                    .padding(16.dp)
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(50.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(28.dp) // Keeps spacing between items
                ) {
                    items(users.size) { userIndex ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SelectUserButton(
                                username = users[userIndex].name,
                                onClick = {
                                    if (navController.currentDestination?.route?.contains("NewUser") == true) {
                                        viewModel.selectUser(users[userIndex])
                                        navController.navigate(Home)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            DeleteButton {
                                userSelected = users[userIndex]
                                showDeleteDialog.value = true
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                WideOrangeButton {
                    if (users.size >= 90) {
                        Toast.makeText(context, "Max 8 users allowed.", Toast.LENGTH_SHORT).show()
                    } else {
                        showPopup.value = true
                    }
                }

                Spacer(modifier = Modifier.height(76.dp))
            }

            if (showDeleteDialog.value) {
                DialogPopup(
                    onDismissRequest = { showDeleteDialog.value = false },
                    onConfirmation = {
                        userSelected?.let { user ->
                            viewModel.deleteUser(user)
                        }
                        showDeleteDialog.value = false
                    },
                    dialogTitle = "DELETE USER",
                    dialogText = "are you user you want to delete this user ?",
                    icon = Icons.Default.Delete
                )
            }

            // Show popup with darkened or blurred background
            if (showPopup.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)) // Darkened background
                        .clickable { showPopup.value = false } // Dismiss popup on background click
                ) {
                    CreateUserPopupBox(viewModel, onDismiss = { showPopup.value = false })
                }
            }
        }
    }

}

@Composable
fun CreateUserPopupBox(
    viewModel: UserFlowViewModel,
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
            Text("Please type in a username you wish to use")

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
                colors = ButtonDefaults.buttonColors(containerColor = stringToColor(AllThemes[0].primaryHexColor)),
                onClick = {
                    viewModel.addUser(
                        name = newUserName,
                        onResult = { result ->
                            if (result.first) {
                                Toast.makeText(
                                    context,
                                    result.second,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                onDismiss()
                            } else {
                                Toast.makeText(
                                    context,
                                    result.second,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create User")
            }
        }
    }
}