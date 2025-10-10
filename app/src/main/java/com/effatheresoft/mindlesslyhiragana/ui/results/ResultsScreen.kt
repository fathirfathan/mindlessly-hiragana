package com.effatheresoft.mindlesslyhiragana.ui.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.DefaultViewModelProvider
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.QuizResult
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.getCorrectAnswersCount
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.getIncorrectAnswersCount

@Composable
fun ResultsScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit = {},
    viewModel: ResultsViewModel = viewModel(factory = DefaultViewModelProvider.Factory),
    quizResults: List<QuizResult>
) {
    LaunchedEffect(Unit) {
        viewModel.initializeWithQuizResults(quizResults)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    ResultsScreenContent(
        modifier = modifier,
        onNavigationIconClicked = onNavigationIconClicked,
        uiState = uiState
    )
}

@Composable
fun ResultsScreenContent(
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit = {},
    uiState: ResultsUiState = ResultsUiState.Loading
) {
    DefaultScaffold(
        appBarTitle = "Results",
        onNavigationIconClicked = onNavigationIconClicked
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(uiState) {
                is ResultsUiState.Success -> {
                    Text("Correct answers: ${uiState.quizResults.getCorrectAnswersCount()}")
                    Text("Incorrect answers: ${uiState.quizResults.getIncorrectAnswersCount()}")
                    Column {
                        for ((index, result) in uiState.quizResults.withIndex()) {
                            if (result.question != result.answer) {
                                Text(
                                    "Q${index + 1}: ${result.question.hiragana} -> " +
                                            "${result.question.romaji.uppercase()}, not " +
                                            result.answer.romaji.uppercase()
                                )
                            }
                        }
                    }
                }
                is ResultsUiState.Loading -> {
                    Text("Loading")
                }
                is ResultsUiState.Error -> {
                    Text("Error")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Results Screen Success")
@Composable
fun ResultsScreenSuccessPreview() {
    ResultsScreenContent(
        uiState = ResultsUiState.Success(
            listOf(
                QuizResult(question = Hiragana.HI, answer = Hiragana.HI),
                QuizResult(question = Hiragana.MI, answer = Hiragana.HI),
                QuizResult(question = Hiragana.KA, answer = Hiragana.KA),
                QuizResult(question = Hiragana.SE, answer = Hiragana.KA),
                QuizResult(question = Hiragana.HI, answer = Hiragana.HI),
            )
        )
    )
}

@Preview(showBackground = true, name = "Results Screen Loading")
@Composable
fun ResultsScreenLoadingPreview() {
    ResultsScreenContent(
        uiState = ResultsUiState.Loading
    )
}

@Preview(showBackground = true, name = "Results Screen Error")
@Composable
fun ResultsScreenErrorPreview() {
    ResultsScreenContent(
        uiState = ResultsUiState.Error(Exception("Error"))
    )
}



