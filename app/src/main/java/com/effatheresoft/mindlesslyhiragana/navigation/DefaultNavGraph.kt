package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.effatheresoft.mindlesslyhiragana.home.HomeScreen
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    object Home: Route
}

@Composable
fun DefaultNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Route.Home = Route.Home
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Route.Home> {
            HomeScreen()
        }
    }
}