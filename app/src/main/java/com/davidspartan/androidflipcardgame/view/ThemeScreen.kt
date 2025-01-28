package com.davidspartan.androidflipcardgame.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
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
import com.davidspartan.androidflipcardgame.view.components.FlipCard
import com.davidspartan.androidflipcardgame.viewmodel.UserRepositoryViewModel

@Composable
fun ThemeScreen(
    navController: NavHostController,
    viewModel: UserRepositoryViewModel
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val selectedTheme by viewModel.selectedTheme.collectAsState(initial = AllThemes[0])
    val selectedUser by viewModel.selectedUser.collectAsState(initial = null)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(stringToColor(selectedTheme!!.primaryHexColor))
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // 3 cards per row
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            items(AllThemes) { theme -> // Iterate over each card directly

                Box {

                    ThemeSample(theme)
                    if(!viewModel.userHasThemeWithName(selectedUser, theme.name)){
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(screenWidth * 0.25f)
                                .background(
                                    color = Color.Black.copy(alpha = 0.5f), // Semi-transparent black
                                    shape = RoundedCornerShape(8.dp) // Rounded corners with 16dp radius
                                )
                            ,

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeSample(theme: Theme) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(screenWidth * 0.25f), // Set size to 25% of screen width
        contentAlignment = Alignment.Center
    ){
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
    }
}