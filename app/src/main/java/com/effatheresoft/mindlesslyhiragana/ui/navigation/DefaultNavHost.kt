package com.effatheresoft.mindlesslyhiragana.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.effatheresoft.mindlesslyhiragana.ui.details.DetailsScreen
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreen

@Composable
fun DefaultNavHost(
    navController: NavHostController,
    route: Route
) {
    NavHost(navController = navController, startDestination = route) {
        composable<Route.Home> {
            HomeScreen(
                onNavigateToDetails = { id -> navController.navigate(Route.Details(id)) }
            )
        }

        composable<Route.Details> { backStackEntry ->
            val details: Route.Details = backStackEntry.toRoute()
            DetailsScreen(
                details.id,
                onNavigationIconClicked = { navController.popBackStack() }
            )
        }
    }
}