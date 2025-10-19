package com.effatheresoft.mindlesslyhiragana.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.effatheresoft.mindlesslyhiragana.ui.DefaultViewModelProvider
import com.effatheresoft.mindlesslyhiragana.ui.details.DetailsScreen
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreen
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.QuizScreen
import com.effatheresoft.mindlesslyhiragana.ui.results.ResultsScreen
import kotlinx.serialization.json.Json

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
                viewModel = viewModel(
                    factory = DefaultViewModelProvider.getFactoryWithStringData(details.id)
                ),
                onNavigationIconClicked = { navController.popBackStack() },
                onNavigateToLearn = { learningSetsCount ->
                    navController.navigate(Route.Quiz(details.id, learningSetsCount))
                }
            )
        }

        composable<Route.Quiz> { backStackEntry ->
            val quiz: Route.Quiz = backStackEntry.toRoute()
            QuizScreen(
                viewModel = viewModel(
                    factory = DefaultViewModelProvider.getFactoryWithRouteParameter(quiz)
                ),
                onNavigationIconClicked = { navController.popBackStack() },
                onNavigateToResults = { quizResults ->
                    val quizResultsJson = Json.encodeToString(quizResults)
                    navController.navigate(Route.Results(quiz.id, quizResultsJson)) {
                        popUpTo<Route.Quiz> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Route.Results> { backStackEntry ->
            val results: Route.Results = backStackEntry.toRoute()
            ResultsScreen(
                viewModel = viewModel(
                    factory = DefaultViewModelProvider
                        .getFactoryWithStringData(results.quizResultsJson)
                ),
                onNavigationIconClicked = {
                    navController.popBackStack(
                        route = Route.Details(results.id),
                        inclusive = false
                    )
                }
            )
        }
    }
}