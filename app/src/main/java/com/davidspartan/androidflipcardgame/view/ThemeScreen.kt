package com.davidspartan.androidflipcardgame.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.davidspartan.androidflipcardgame.R
import com.davidspartan.androidflipcardgame.model.AllThemes
import com.davidspartan.androidflipcardgame.model.realm.Theme
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.DialogPopup
import com.davidspartan.androidflipcardgame.view.components.FlipCard
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.components.ThemedText
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel

@Composable
fun ThemeScreen(
    navController: NavHostController,
    viewModel: UserRepositoryViewModel
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val selectedUser by viewModel.selectedUser.collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }
    var themeSelectedForPurchase by remember { mutableStateOf(AllThemes[0]) }
    val context = LocalContext.current

    selectedUser?.let { user ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(stringToColor(user.selectedTheme!!.primaryHexColor))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ThemedText(
                    text = "Points: ${user.score}",
                    theme = user.selectedTheme!!
                )

                Spacer(modifier = Modifier.size(50.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 cards per row
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(AllThemes) { theme -> // Iterate over each card directly

                        Box {

                            ThemeSample(
                                theme,
                                theme.name == user.selectedTheme!!.name
                            ) {
                                viewModel.selectTheme(user, theme)
                            }
                            if (!viewModel.userHasThemeWithName(user, theme.name)) {
                                Box(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .size(screenWidth * 0.25f)
                                        .background(
                                            color = Color.Black.copy(alpha = 0.5f), // Semi-transparent black
                                            shape = RoundedCornerShape(8.dp) // Rounded corners with 16dp radius
                                        )
                                        .clickable {
                                            themeSelectedForPurchase = theme
                                            showDeleteDialog = true

                                        },
                                )
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.size(50.dp))

                OptionButton(
                    text = "Go Back ",
                    theme = user.selectedTheme!!
                ) {
                    navController.popBackStack()
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
                                "You need ${themeSelectedForPurchase.price - selectedUser!!.score} more points to unlock this theme",
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
    }

}

@Composable
fun ThemeSample(theme: Theme, currentlySelected: Boolean, function: () -> Unit) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(screenWidth * 0.25f) // Set size to 25% of screen width
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
                Text(
                    text = theme.name,
                    color = stringToColor(theme.textHexColor),
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