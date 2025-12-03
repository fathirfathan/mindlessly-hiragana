package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.effatheresoft.mindlesslyhiragana.home.HomeScreen
import com.effatheresoft.mindlesslyhiragana.learn.LearnScreen
import com.effatheresoft.mindlesslyhiragana.quiz.QuizScreen
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    object Home: Route

    @Serializable
    data class Learn(val categoryId: String): Route

    @Serializable
    data class Quiz(val categoryId: String): Route
}

@Composable
fun DefaultNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Route = Route.Home
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Route.Home> {
            HomeScreen(
                onNavigateToLearn = { navController.navigate(Route.Learn(it)) }
            )
        }

        composable<Route.Learn> { navBackStackEntry ->
            val learnRoute: Route.Learn = navBackStackEntry.toRoute()
            LearnScreen(
                categoryId = learnRoute.categoryId,
                onNavigationIconClick = { navController.navigate(Route.Home) },
                onLearnButtonClick = { navController.navigate(Route.Quiz(learnRoute.categoryId)) }
            )
        }

        composable<Route.Quiz> { navBackStackEntry ->
            val quizRoute: Route.Quiz = navBackStackEntry.toRoute()
            QuizScreen(categoryId = quizRoute.categoryId)
        }
    }
}