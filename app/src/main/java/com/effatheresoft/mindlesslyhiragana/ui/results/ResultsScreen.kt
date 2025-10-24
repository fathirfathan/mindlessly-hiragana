package com.effatheresoft.mindlesslyhiragana.ui.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold

@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel,
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit = {}
) {
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
                    Text("Correct answers: ${uiState.quizResults.correctAnswersCount}")
                    Text("Incorrect answers: ${uiState.quizResults.incorrectAnswersCount}")
                    Column {
                        for ((index, result) in uiState.quizResults.withIndex()) {
                            if (!result.isCorrect) {
                                IncorrectAnswerRow(
                                    questionIndex = index + 1,
                                    result = result
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

@Composable
private fun IncorrectAnswerRow(
    questionIndex: Int,
    result: QuizResult
) {
    Text(
        text = buildAnnotatedString {
            append("Q${questionIndex}: ${result.question.hiragana} -> ")
            append(result.question.romaji.uppercase())
            append(", not ")
            append(result.incorrectAttempts.joinToString(", ") { it.romaji.uppercase() }) },
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Preview(showBackground = true, name = "Results Screen Success")
@Composable
fun ResultsScreenSuccessPreview() {
    val attemptedAnswers = mapOf(
        Hiragana.HI to false, Hiragana.MI to false, Hiragana.KA to false, Hiragana.SE to false
    )
    ResultsScreenContent(
        uiState = ResultsUiState.Success(
            listOf(
                QuizResult(
                    question = Hiragana.HI,
                    attemptedAnswers = attemptedAnswers + (Hiragana.HI to true)
                ),
                QuizResult(
                    question = Hiragana.MI,
                    attemptedAnswers = attemptedAnswers
                    + (Hiragana.MI to true) + (Hiragana.SE to true)
                ),
                QuizResult(
                    question = Hiragana.KA,
                    attemptedAnswers = attemptedAnswers
                    + (Hiragana.KA to true)
                ),
                QuizResult(
                    question = Hiragana.KA,
                    attemptedAnswers = attemptedAnswers
                    + (Hiragana.KA to true) + (Hiragana.SE to true) + (Hiragana.HI to true)
                ),
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



