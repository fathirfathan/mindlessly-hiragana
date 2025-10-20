package com.effatheresoft.mindlesslyhiragana.ui.learntrain

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.getLearnedHiraganaUpToId
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.results.QuizResult

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToResults: (quizResults: List<QuizResult>) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    QuizScreenContent(
        modifier = modifier,
        uiState = uiState,
        onNavigationIconClicked = onNavigationIconClicked,
        onNavigateToResults = onNavigateToResults,
        onAnswerSelected = viewModel::onAnswerSelected
    )
}

@Composable
fun QuizScreenContent(
    modifier: Modifier = Modifier,
    uiState: QuizUiState = QuizUiState.Loading,
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToResults: (quizResults: List<QuizResult>) -> Unit = {},
    onAnswerSelected: (
        selectedAnswer: Hiragana,
        onNavigateToResults: (quizResults: List<QuizResult>) -> Unit
    ) -> Unit = { _, _ -> }
) {
    DefaultScaffold(
        appBarTitle = when(uiState) {
            is QuizUiState.Success -> uiState.appBarTitle
            is QuizUiState.Loading -> "Loading"
            is QuizUiState.Error -> "Error"
        },
        onNavigationIconClicked = onNavigationIconClicked
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp, bottom = 48.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(uiState) {
                is QuizUiState.Success -> {
                    Text(
                        uiState.currentQuestion,
                        fontSize = 57.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "Remaining Questions: ${uiState.remainingQuestionsCount}",
                        Modifier.padding(bottom = 16.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        uiState.possibleAnswers.chunked(4).forEach { rowAnswers ->
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (answer in rowAnswers) {
                                    Button(
                                        modifier = Modifier.weight(1f),
                                        onClick = { onAnswerSelected(answer, onNavigateToResults) },
                                        enabled = uiState.selectedAnswersHistory[answer] == false
                                    ) {
                                        Text(answer.romaji.uppercase())
                                    }
                                }
                                val spacersNeeded = 4 - rowAnswers.size
                                repeat(spacersNeeded) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
                is QuizUiState.Loading -> {
                    Text("Loading")
                }
                is QuizUiState.Error -> {
                    Text("Error")
                }
            }
        }
    }
}

@Preview
@Composable
fun QuizScreenContentSuccessPreview() {
    QuizScreenContent(
        uiState = QuizUiState.Success(
            appBarTitle = "HI MI KA SE",
            currentQuestion = "ひ",
            remainingQuestionsCount = 19,
            possibleAnswers = listOf(HI, MI, KA, SE),
            selectedAnswersHistory = mapOf()
        )
    )
}

@Preview
@Composable
fun QuizScreenContentSuccessTestPreview() {
    QuizScreenContent(
        uiState = QuizUiState.Success(
            appBarTitle = "Test All Learned",
            currentQuestion = "ひ",
            remainingQuestionsCount = 19,
            possibleAnswers = Hiragana.categories.getLearnedHiraganaUpToId("11"),
            selectedAnswersHistory = mapOf()
        )
    )
}

@Preview
@Composable
fun QuizScreenContentLoadingPreview() {
    QuizScreenContent(
        uiState = QuizUiState.Loading
    )
}

@Preview
@Composable
fun QuizScreenContentErrorPreview() {
    QuizScreenContent(
        uiState = QuizUiState.Error(Exception("Error"))
    )
}

