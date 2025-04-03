package com.davidspartan.androidflipcardgame.view

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.davidspartan.androidflipcardgame.R
import com.davidspartan.database.realm.AllThemes
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.AppBackground
import com.davidspartan.androidflipcardgame.view.components.buttons.WideOrangeButton
import com.davidspartan.androidflipcardgame.view.components.buttons.DeleteButton
import com.davidspartan.androidflipcardgame.view.components.buttons.OrangeButton
import com.davidspartan.androidflipcardgame.view.components.buttons.SelectUserButton
import com.davidspartan.androidflipcardgame.view.components.buttons.shrinkOnPress
import com.davidspartan.androidflipcardgame.view.navigation.Home
import com.davidspartan.androidflipcardgame.viewmodel.UserFlowViewModel
import com.davidspartan.database.realm.Theme
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
    val theme = viewModel.getSelectedUser() ?: AllThemes[0]
    AppBackground(
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

                WideOrangeButton(
                    text = "Create New User"
                ) {
                    if (users.size >= 90) {
                        Toast.makeText(context, "Max 8 users allowed.", Toast.LENGTH_SHORT).show()
                    } else {
                        showPopup.value = true
                    }
                }

                Spacer(modifier = Modifier.height(76.dp))
            }

            if (showDeleteDialog.value) {

                DeleteUserPopupBox(
                    theme = theme,
                    onDismiss = {
                        showDeleteDialog.value = false
                    },
                    onConfirmation = {
                        userSelected?.let { user ->
                            viewModel.deleteUser(user)
                        }
                        showDeleteDialog.value = false
                    }
                )
            }

            // Show popup with darkened or blurred background
            if (showPopup.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)) // Darkened background
                        .clickable { showPopup.value = false }, // Dismiss popup on background click
                    contentAlignment = Alignment.Center
                ) {
                    CreateUserPopupBox(theme, viewModel, onDismiss = { showPopup.value = false })
                }
            }
        }
    }

}

@Composable
fun CreateUserPopupBox(
    theme: Theme,
    viewModel: UserFlowViewModel,
    onDismiss: () -> Unit
) {
    var newUserName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .width(318.59082.dp)
            .height(256.17969.dp)
            .background(
                color = stringToColor(theme.primaryHexColor),
                shape = RoundedCornerShape(size = 20.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please type in a username you\nwish to use.",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pally)),
                    fontWeight = FontWeight(500),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp, spotColor = Color.Black, ambientColor = Color.Black,
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .width(268.dp)
                    .height(56.dp)
                    .background(
                        color = stringToColor(theme.primaryHexColor),
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = stringToColor(theme.primaryHexColor),
                        shape = RoundedCornerShape(size = 12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = newUserName,
                    onValueChange = { newUserName = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.pally)),
                        fontWeight = FontWeight(700),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxSize(),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.Center) {
                            if (newUserName.isEmpty()) {
                                Text(
                                    text = "Enter User Name",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily(Font(R.font.pally)),
                                        fontWeight = FontWeight(700),
                                        color = Color(0x33FFFFFF),
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OrangeButton(
                text = "Create user"
            ) {
                viewModel.addUser(
                    name = newUserName,
                    onResult = { result ->
                        Toast.makeText(context, result.second, Toast.LENGTH_SHORT).show()
                        if (result.first) {
                            onDismiss()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteUserPopupBox(
    theme: Theme,
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onConfirmation.invoke() }
        ,
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .width(318.dp)
                .height(300.dp)
                .background(
                    color = stringToColor(theme.primaryHexColor),
                    shape = RoundedCornerShape(size = 20.dp)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = rememberAsyncImagePainter(model = R.drawable.delete_trash),
                    contentDescription = "shopping back icon",
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "DELETE USER",
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontFamily = FontFamily(Font(R.font.pally)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Are you sure you want to\n delete this user?",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.pally)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFEAEAEA),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OrangeButton(
                    text = "Dismiss"
                ) {
                    onDismiss.invoke()
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(width = 240.dp, height = 56.dp)
                        .shrinkOnPress {
                            onConfirmation.invoke()
                        }
                ) {
                    Text(
                        text = "Confirm",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.pally)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        }
    }
}