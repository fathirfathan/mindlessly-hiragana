package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.FUWOYA
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreen
import com.effatheresoft.mindlesslyhiragana.ui.learn.LearnScreen
import com.effatheresoft.mindlesslyhiragana.ui.quiz.QuizScreen
import com.effatheresoft.mindlesslyhiragana.ui.result.ResultScreen
import com.effatheresoft.mindlesslyhiragana.ui.test.TestScreen
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    object Home: Route

    @Serializable
    data class Learn(val categoryId: String): Route

    @Serializable
    data class Quiz(val categoryId: String): Route

    @Serializable
    data class Result(val categoryId: String): Route

    @Serializable
    object Test: Route
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
                onNavigateToLearn = { navController.navigate(Route.Learn(it)) },
                onNavigateToTest = { navController.navigate(Route.Test) }
            )
        }

        composable<Route.Learn> { navBackStackEntry ->
            val learnRoute: Route.Learn = navBackStackEntry.toRoute()
            LearnScreen(
                categoryId = learnRoute.categoryId,
                onNavigationIconClick = { navController.navigateUp() },
                onLearnButtonClick = { navController.navigate(Route.Quiz(learnRoute.categoryId)) }
            )
        }

        composable<Route.Quiz> { navBackStackEntry ->
            val quizRoute: Route.Quiz = navBackStackEntry.toRoute()
            QuizScreen(
                categoryId = quizRoute.categoryId,
                onNavigationIconClick = { navController.navigateUp() },
                onCompleted = { navController.navigate(Route.Result(quizRoute.categoryId)) {
                    popUpTo(Route.Quiz(quizRoute.categoryId)) { inclusive = true }
                } }
            )
        }

        composable<Route.Result> { navBackStackEntry ->
            val resultRoute: Route.Result = navBackStackEntry.toRoute()
            val learnRoute = Route.Learn(resultRoute.categoryId)
            ResultScreen(
                onNavigationIconClick = {
                    navController.popBackStack(route = learnRoute, inclusive = false)
                }
            )
        }

        composable<Route.Test> {
            TestScreen(
                categoryList = listOf(HIMIKASE, FUWOYA),
                isTestUnlocked = false
            )
        }
    }
}