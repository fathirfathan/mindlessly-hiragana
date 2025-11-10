package com.effatheresoft.mindlesslyhiragana.ui.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.effatheresoft.mindlesslyhiragana.ui.DefaultViewModelProvider
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold
import kotlin.text.append

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

//@Preview(showBackground = true, name = "Results Screen Success")
//@Composable
//fun ResultsScreenSuccessPreview() {
//    ResultsScreenContent(
//        uiState = ResultsUiState.Success(
//            listOf(
//                QuizResult(question = Hiragana.HI, attemptedAnswers = Hiragana.HI),
//                QuizResult(question = Hiragana.MI, attemptedAnswers = Hiragana.HI),
//                QuizResult(question = Hiragana.KA, attemptedAnswers = Hiragana.KA),
//                QuizResult(question = Hiragana.SE, attemptedAnswers = Hiragana.KA),
//                QuizResult(question = Hiragana.HI, attemptedAnswers = Hiragana.HI),
//            )
//        )
//    )
//}

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



