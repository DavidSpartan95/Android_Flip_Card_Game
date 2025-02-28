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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.davidspartan.database.realm.AllThemes
import com.davidspartan.androidflipcardgame.model.isValidUsernameForUser
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.DialogPopup
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.navigation.Home
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel

@Composable
fun SelectUserScreen(
    navController: NavHostController,
    viewModel: UserRepositoryViewModel
) {
    val users by viewModel.users.collectAsState(initial = emptyList()) // Provide an initial value
    val showPopup = remember { mutableStateOf(false) } // Track if popup is visible
    val showDeleteDialog = remember { mutableStateOf(false) }
    val showInfoDialog = remember { mutableStateOf(false) }
    var userSelected by remember { mutableStateOf<com.davidspartan.database.realm.User?>(null) }
    val context = LocalContext.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(stringToColor(AllThemes[0].primaryHexColor)),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            Modifier
                .padding(16.dp)
                .padding(WindowInsets.statusBars.asPaddingValues())
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OptionButton(
                text = "Create New User",
                theme = AllThemes[0]
            ) {
                if (users.size>=8) {
                    Toast.makeText(context, "Max 8 users allowed.", Toast.LENGTH_SHORT).show()
                } else {
                    showPopup.value = true
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            users.forEach { user ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = stringToColor(AllThemes[0].primaryHexColor),
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Handle info click here
                            }
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                color = stringToColor(AllThemes[0].secondaryHexColor),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                            .clickable {
                                viewModel.selectUser(user)
                                navController.navigate(Home)
                            }
                            .width(screenWidth * 0.45f)
                            .testTag(user.name)
                    ) {
                        Text(
                            text = user.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = stringToColor(AllThemes[0].textHexColor)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Delete, // Trashcan icon
                        contentDescription = "Delete User",
                        tint = stringToColor("#DA5A2A"),
                        modifier = Modifier
                            .padding(8.dp) // Optional padding around the icon
                            .clickable {
                                userSelected = user
                                showDeleteDialog.value = true
                            }
                    )
                }
            }
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

@Composable
fun CreateUserPopupBox(
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

                    if (isValidUsernameForUser(newUserName)) {
                        viewModel.addUser(name = newUserName)
                        onDismiss()
                    } else {
                        Toast.makeText(
                            context,
                            "Invalid username. Only letters and numbers allowed (max 16 characters).",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create User")
            }
        }
    }
}