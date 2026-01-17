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
import com.effatheresoft.mindlesslyhiragana.ui.learn.LearnViewModel
import com.effatheresoft.mindlesslyhiragana.ui.quiz.QuizScreen
import com.effatheresoft.mindlesslyhiragana.ui.quiz.QuizViewModel
import com.effatheresoft.mindlesslyhiragana.ui.result.ResultScreen
import com.effatheresoft.mindlesslyhiragana.ui.test.TestScreen
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.TestQuizScreen
import com.effatheresoft.mindlesslyhiragana.ui.testresult.TestResultScreen

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
                onNavigateToLearnOrTest = { categoryId ->
                    when (categoryId) {
                        "Test All Learned" -> navController.navigate(Route.Test)
                        else -> navController.navigate(Route.Learn(categoryId))
                    }
                },
                viewModel = hiltViewModel()
            )
        }

        composable<Route.Learn> { navBackStackEntry ->
            val learnRoute: Route.Learn = navBackStackEntry.toRoute()
            LearnScreen(
                onNavigationIconClick = { navController.navigateUp() },
                onLearnButtonClick = { navController.navigate(Route.Quiz(learnRoute.categoryId)) },
                viewModel = hiltViewModel<LearnViewModel, LearnViewModel.Factory>(
                    creationCallback = { factory -> factory.create(learnRoute.categoryId)}
                )
            )
        }

        composable<Route.Quiz> { navBackStackEntry ->
            val quizRoute: Route.Quiz = navBackStackEntry.toRoute()
            QuizScreen(
                onNavigationIconClick = { navController.navigateUp() },
                onCompleted = { navController.navigate(Route.Result(quizRoute.categoryId)) {
                    popUpTo(Route.Quiz(quizRoute.categoryId)) { inclusive = true }
                } },
                viewModel = hiltViewModel<QuizViewModel, QuizViewModel.Factory>(
                    creationCallback = { factory -> factory.create(quizRoute.categoryId)}
                )
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
                    navController.navigate(Route.Test) {
                        popUpTo(Route.Home)
                    }
                }
            )
        }

        composable<Route.Test> {
            TestScreen(
                viewModel = hiltViewModel(),
                onNavigationIconClick = { navController.navigateUp() },
                onChallengeLearn = { categoryId ->
                    navController.navigate(Route.Learn(categoryId))  {
                        popUpTo(Route.Test) { inclusive = true }
                    }
                },
                onTestAllLearned = { navController.navigate(Route.TestQuiz) }
            )
        }

        composable<Route.TestQuiz> {
            TestQuizScreen(
                viewModel = hiltViewModel(),
                onNavigateUp = navController::navigateUp,
                onAllQuestionsAnswered = {
                    navController.navigate(Route.TestResult) {
                        popUpTo(Route.Test) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.TestResult> {
            TestResultScreen(
                viewModel = hiltViewModel(),
                onNavigateUp = navController::navigateUp,
                onTryAgain = {
                    navController.navigate(Route.Test) {
                        popUpTo(Route.TestResult) { inclusive = true }
                    }
                },
                onContinueLearning = { newProgress ->
                    navController.navigate(Route.Learn(newProgress)) {
                        popUpTo(Route.TestResult) { inclusive = true }
                    }
                }
            )
        }
    }
}