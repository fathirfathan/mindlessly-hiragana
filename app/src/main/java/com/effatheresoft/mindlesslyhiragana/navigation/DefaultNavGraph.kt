package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreen
import com.effatheresoft.mindlesslyhiragana.ui.learn.LearnScreen
import com.effatheresoft.mindlesslyhiragana.ui.quiz.QuizScreen
import com.effatheresoft.mindlesslyhiragana.ui.result.ResultScreen
import com.effatheresoft.mindlesslyhiragana.ui.test.TestScreen
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.TestQuizScreen
import com.effatheresoft.mindlesslyhiragana.ui.testresult.TestResultScreen
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
    data class Test(val categoryId: String): Route

    @Serializable
    data class TestQuiz(val categoryId: String): Route

    @Serializable
    object TestResult: Route
}

@Composable
fun DefaultNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Route = Route.Home
) {
    val navigationViewModel: NavigationViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Route.Home> {
            HomeScreen(
                onNavigateToLearn = { navController.navigate(Route.Learn(it)) },
                onNavigateToTest = { navController.navigate(Route.Test(it)) }
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
                onNavigateUp = {
                    navController.popBackStack(route = learnRoute, inclusive = false)
                },
                onTryAgain = {
                    navController.navigate(Route.Quiz(resultRoute.categoryId)) {
                        popUpTo(Route.Result(resultRoute.categoryId)) { inclusive = true }
                    }
                },
                onTestAllLearned = {
                    navController.navigate(Route.Test(resultRoute.categoryId)) {
                        popUpTo(Route.Home)
                    }
                }
            )
        }

        composable<Route.Test> { navBackStackEntry ->
            val testRoute: Route.Test = navBackStackEntry.toRoute()
            TestScreen(
                onNavigationIconClick = { navController.navigateUp() },
                onChallengeLearn = {
                    navController.navigate(Route.Learn(testRoute.categoryId))  {
                        popUpTo(Route.Test(testRoute.categoryId)) { inclusive = true }
                    }
                },
                onTestAllLearned = {
                    navController.navigate(Route.TestQuiz(testRoute.categoryId))
                }
            )
        }

        composable<Route.TestQuiz> { navBackStackEntry ->
            val testQuizRoute: Route.TestQuiz = navBackStackEntry.toRoute()
            TestQuizScreen(
                onNavigateUp = navController::navigateUp,
                onAllQuestionsAnswered = { questionStates ->
                    navigationViewModel.setQuestionStates(questionStates)
                    navController.navigate(Route.TestResult) {
                        popUpTo(Route.Test(testQuizRoute.categoryId)) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.TestResult> {
            TestResultScreen(navigationViewModel)
        }
    }
}