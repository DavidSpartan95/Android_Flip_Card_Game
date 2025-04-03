package com.davidspartan.androidflipcardgame.view

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.davidspartan.androidflipcardgame.model.stringToColor
import com.davidspartan.database.realm.AllThemes
import com.davidspartan.androidflipcardgame.view.components.ThemeCard
import com.davidspartan.androidflipcardgame.view.components.UserNotLoggedInScreen
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.AppBackground
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.GameBoardFrame
import com.davidspartan.androidflipcardgame.view.components.backgroundelements.PointPlate
import com.davidspartan.androidflipcardgame.view.components.buttons.OrangeButton
import com.davidspartan.androidflipcardgame.view.components.buttons.WideOrangeButton
import com.davidspartan.androidflipcardgame.view.components.buttons.shrinkOnPress
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
            AppBackground(
                theme = user.selectedTheme
            ) {
                ThemeScreenContent(user, navController, viewModel)
            }
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
    var showPurchasePopup by rememberSaveable  { mutableStateOf(false) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var isNavigating by rememberSaveable { mutableStateOf(false) }

    when (configuration.orientation) {

        Configuration.ORIENTATION_LANDSCAPE -> { //Landscape
            Row(
                modifier = Modifier
                    .fillMaxSize()
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
                    PointPlate(
                        points = "Points: ${user.score}"
                    )

                    Spacer(modifier = Modifier.size(50.dp))

                    OrangeButton(
                        text = "Go To Menu"
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
                        showPurchasePopup = true
                    }
                )
            }
        }

        else -> { //Portrait
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PointPlate(
                        points = "Points: ${user.score}"
                    )

                    Spacer(modifier = Modifier.size(50.dp))

                    ThemeSampleGrid(
                        user,
                        viewModel,
                        selectTheme = { theme ->
                            themeSelectedForPurchase = theme
                            showPurchasePopup = true
                        }
                    )

                    Spacer(modifier = Modifier.size(50.dp))
                    WideOrangeButton(text = "Go To Menu"){
                        if(!isNavigating){
                            isNavigating = true
                            navController.navigateUp()
                        }
                    }
                }
            }
        }
    }
    if (showPurchasePopup) {
        UnlockThemePopUp(user.selectedTheme, onDismiss = { showPurchasePopup = false }){
            viewModel.purchaseTheme(user, themeSelectedForPurchase) { success ->
                if (success) {
                    viewModel.selectTheme(user, themeSelectedForPurchase)
                    showPurchasePopup = false
                } else {
                    Toast.makeText(
                        context,
                        "You need ${themeSelectedForPurchase.price - user.score} more points to unlock this theme",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
fun ThemeSampleGrid(
    user: User,
    viewModel: UserFlowViewModel,
    selectTheme: (Theme) -> Unit // Accept Theme as a parameter
) {
    GameBoardFrame(user.selectedTheme) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val firstRow = AllThemes.take(3)  // First 3 cards
            val secondRow = AllThemes.drop(3) // Last 3 cards

            Row {
                firstRow.forEach { theme ->
                    Box {
                        ThemeCard(
                            text = theme.name,
                            selected = theme.name == user.selectedTheme.name,
                            theme = theme
                        ) {
                            viewModel.selectTheme(user, theme)
                        }
                        if (!viewModel.userHasThemeWithName(user, theme.name)) {
                            LockedThemeShadow(){
                                selectTheme(theme)
                            }
                        }
                    }

                }
            }
            Row{
                secondRow.forEach { theme ->
                    Box {
                        ThemeCard(
                            text = theme.name,
                            selected = theme.name == user.selectedTheme.name,
                            theme = theme
                        ) {
                            viewModel.selectTheme(user, theme)
                        }
                        if (!viewModel.userHasThemeWithName(user, theme.name)) {
                            LockedThemeShadow(){
                                selectTheme(theme)
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun LockedThemeShadow(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(3.dp)
            .size(width = 106.dp, height = 154.dp)
            .background(
                color = Color.Black.copy(alpha = 0.5f), // Semi-transparent black
                shape = RoundedCornerShape(8.dp) // Rounded corners with 16dp radius
            )
            .clickable {
                onClick.invoke()
            },
    )
}

@Composable
fun UnlockThemePopUp(theme: Theme, onDismiss: () -> Unit ,onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ){
        Box(
            Modifier
                .width(318.59082.dp)
                .height(363.37695.dp)
                .background(color = stringToColor(theme.primaryHexColor), shape = RoundedCornerShape(size = 20.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = rememberAsyncImagePainter(model = R.drawable.shopping_bag),
                    contentDescription = "shopping back icon",
                )
                Spacer(modifier = Modifier.height(46.dp))
                Text(
                    text = "Unlock Forest?",
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
                    text = "By Pressing confirm 10 points will be\n removed from your score.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.pally)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFEAEAEA),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(46.dp))
                OrangeButton(
                    text = "Confirm"
                ) {
                    onClick.invoke()
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(width = 240.dp, height = 56.dp)
                        .shrinkOnPress{
                            onDismiss.invoke()
                        }
                ){
                    Text(
                        text = "Dismiss",
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