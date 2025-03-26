package com.davidspartan.androidflipcardgame.view

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.davidspartan.database.realm.AllThemes
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.AutoResizedTextWithBackground
import com.davidspartan.androidflipcardgame.view.components.DialogPopup
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.components.ThemedText
import com.davidspartan.androidflipcardgame.view.components.UserNotLoggedInScreen
import com.davidspartan.androidflipcardgame.viewmodel.UserFlowViewModel
import com.davidspartan.androidflipcardgame.viewmodel.UserUiState
import com.davidspartan.database.realm.Theme
import com.davidspartan.database.realm.User

@Composable
fun ThemeScreen(
    navController: NavHostController,
    viewModel: UserFlowViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UserUiState.LoggedIn -> {
            val user = (uiState as UserUiState.LoggedIn).selectedUser
            ThemeScreenContent(user, navController, viewModel)
        }

        UserUiState.LoggedOut -> {
            UserNotLoggedInScreen(
                navController = navController
            )
        }
    }
}

@Composable
fun ThemeScreenContent(
    user: User,
    navController: NavHostController,
    viewModel: UserFlowViewModel
) {
    var themeSelectedForPurchase by remember { mutableStateOf(AllThemes[0]) }
    var showDeleteDialog by rememberSaveable  { mutableStateOf(false) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var isNavigating by rememberSaveable { mutableStateOf(false) }

    when (configuration.orientation) {

        Configuration.ORIENTATION_LANDSCAPE -> { //Landscape
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(stringToColor(user.selectedTheme.primaryHexColor))
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Spacer(modifier = Modifier.size(5.dp))
                    ThemedText(
                        text = "Points: ${user.score}",
                        theme = user.selectedTheme
                    )
                    Spacer(modifier = Modifier.size(50.dp))
                    OptionButton(
                        text = "Go To Menu",
                        theme = user.selectedTheme
                    ) {
                        if(!isNavigating){
                            isNavigating = true
                            navController.navigateUp()
                        }
                    }
                    Spacer(modifier = Modifier.size(5.dp))

                }
                ThemeSampleGrid(
                    user,
                    viewModel,
                    selectTheme = { theme ->
                        themeSelectedForPurchase = theme
                        showDeleteDialog = true
                    }
                )
            }
        }

        else -> { //Portrait
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(stringToColor(user.selectedTheme.primaryHexColor))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ThemedText(
                        text = "Points: ${user.score}",
                        theme = user.selectedTheme
                    )

                    Spacer(modifier = Modifier.size(50.dp))

                    ThemeSampleGrid(
                        user,
                        viewModel,
                        selectTheme = { theme ->
                            themeSelectedForPurchase = theme
                            showDeleteDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.size(50.dp))

                    OptionButton(
                        text = "Go To Menu",
                        theme = user.selectedTheme
                    ) {
                        if(!isNavigating){
                            isNavigating = true
                            navController.navigateUp()
                        }
                    }
                }
            }
        }
    }
    if (showDeleteDialog) {
        DialogPopup(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmation = {

                if (viewModel.purchaseTheme(user, themeSelectedForPurchase)) {
                    viewModel.selectTheme(user, themeSelectedForPurchase)
                    showDeleteDialog = false
                } else {
                    Toast.makeText(
                        context,
                        "You need ${themeSelectedForPurchase.price - user.score} more points to unlock this theme",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            },
            dialogTitle = "Unlock ${themeSelectedForPurchase.name} ?",
            dialogText = "By pressing confirm ${themeSelectedForPurchase.price} points will be removed from your score",
            icon = Icons.Default.ShoppingCart
        )
    }
}

@Composable
fun ThemeSampleGrid(
    user: User,
    viewModel: UserFlowViewModel,
    selectTheme: (Theme) -> Unit // Accept Theme as a parameter
) {


    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardSize = if (screenWidth < screenHeight) screenWidth * 0.25f else screenHeight * 0.25f

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 cards per row
        modifier = Modifier,
        verticalArrangement = Arrangement.Center
    ) {
        items(AllThemes) { theme -> // Iterate over each card directly

            Box {

                ThemeSample(
                    theme,
                    theme.name == user.selectedTheme.name
                ) {
                    viewModel.selectTheme(user, theme)
                }
                if (!viewModel.userHasThemeWithName(user, theme.name)) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(cardSize)
                            .background(
                                color = Color.Black.copy(alpha = 0.5f), // Semi-transparent black
                                shape = RoundedCornerShape(8.dp) // Rounded corners with 16dp radius
                            )
                            .clickable {
                                selectTheme(theme)
                            },
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeSample(
    theme: Theme,
    currentlySelected: Boolean,
    function: () -> Unit
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardSize = if (screenWidth < screenHeight) screenWidth * 0.25f else screenHeight * 0.25f

    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(cardSize) // Set size to 25% of screen width
            .clickable {
                function.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = stringToColor(theme.primaryHexColor))

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                AutoResizedTextWithBackground(
                    text = theme.name,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .background(
                            color = stringToColor(theme.secondaryHexColor),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    color = stringToColor(theme.textHexColor)
                )
            }
        }
        if (currentlySelected) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Delete User",
                    tint = stringToColor("#d4af37"),
                    modifier = Modifier
                        .padding(2.dp)
                )
            }
        }
    }
}

