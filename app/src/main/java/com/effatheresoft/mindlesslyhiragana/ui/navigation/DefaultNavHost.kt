package com.effatheresoft.mindlesslyhiragana.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.effatheresoft.mindlesslyhiragana.ui.details.DetailsScreen
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreen
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.QuizScreen
import com.effatheresoft.mindlesslyhiragana.ui.results.QuizResult
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
                details.id,
                onNavigationIconClicked = { navController.popBackStack() },
                onNavigateToLearn = { navController.navigate(Route.Quiz(details.id)) }
            )
        }

        composable<Route.Quiz> { backStackEntry ->
            val quiz: Route.Quiz = backStackEntry.toRoute()
            QuizScreen(
                quiz.id,
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
            val quizResults = Json.decodeFromString<List<QuizResult>>(results.quizResultsJson)
            ResultsScreen(
                onNavigationIconClicked = {
                    navController.popBackStack(
                        route = Route.Details(results.id),
                        inclusive = false
                    )
                },
                quizResults = quizResults
            )
        }
    }
}