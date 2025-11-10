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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.DefaultViewModelProvider
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold

@Composable
fun QuizScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel(factory = DefaultViewModelProvider.Factory),
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToResults: (quizResults: List<QuizResult>) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.initializeWithId(id)
    }
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
                        "Remaining: ${uiState.remainingQuestionsCount}",
                        Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (answer in uiState.possibleAnswers) {
                            Button({ onAnswerSelected(answer, onNavigateToResults) }) {
                                Text(answer.romaji.uppercase())
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
            remainingQuestionsCount = "20",
            possibleAnswers = listOf(Hiragana.HI, Hiragana.MI, Hiragana.KA, Hiragana.SE)
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

