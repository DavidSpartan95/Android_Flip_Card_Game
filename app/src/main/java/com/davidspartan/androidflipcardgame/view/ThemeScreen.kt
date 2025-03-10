package com.davidspartan.androidflipcardgame.view

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.davidspartan.database.realm.AllThemes
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.androidflipcardgame.view.components.DialogPopup
import com.davidspartan.androidflipcardgame.view.components.OptionButton
import com.davidspartan.androidflipcardgame.view.components.ThemedText
import com.davidspartan.androidflipcardgame.viewmodel.UserFlowViewModel
import com.davidspartan.androidflipcardgame.viewmodel.UserUiState

@Composable
fun ThemeScreen(
    navController: NavHostController,
    viewModel: UserFlowViewModel
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var themeSelectedForPurchase by remember { mutableStateOf(AllThemes[0]) }
    val context = LocalContext.current

    when(uiState){
        is UserUiState.LoggedIn -> {
            val user = (uiState as UserUiState.LoggedIn).selectedUser

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
                        text = "Go To Menu",
                        theme = user.selectedTheme!!
                    ) {
                        navController.navigateUp()
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
        }
        UserUiState.LoggedOut -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "No user is logged in.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ThemeSample(theme: com.davidspartan.database.realm.Theme, currentlySelected: Boolean, function: () -> Unit) {

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

@Composable
fun AutoResizedTextWithBackground(
    text: String,
    style: TextStyle,
    modifier: Modifier,
    color: Color = style.color
) {
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }
    val defaultFontSize = MaterialTheme.typography.bodySmall.fontSize
    Text(
        text = text,
        color = color,
        modifier = modifier.drawWithContent {
            if (shouldDraw){
                drawContent()
            }
        },
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if(style.fontSize.isUnspecified){
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95
                )
            }else{
                shouldDraw = true
            }
        }
    )
}