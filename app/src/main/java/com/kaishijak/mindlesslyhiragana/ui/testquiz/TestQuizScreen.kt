package com.kaishijak.mindlesslyhiragana.ui.testquiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaishijak.mindlesslyhiragana.R
import com.kaishijak.mindlesslyhiragana.data.model.Hiragana
import com.kaishijak.mindlesslyhiragana.ui.component.DefaultScaffold
import com.kaishijak.mindlesslyhiragana.ui.component.DefaultTopAppBar
import com.kaishijak.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@Composable
fun TestQuizScreen(
    viewModel: TestQuizViewModel,
    onNavigateUp: () -> Unit,
    onAllQuestionsAnswered: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentQuestion = uiState.currentQuestion
    val remainingQuestionsCount = uiState.remainingQuestionsCount

    DefaultScaffold(
        topAppBar = {
            DefaultTopAppBar(
                title = R.string.test_all_learned,
                onNavigationIconClick = onNavigateUp
            )
        },
        modifier = modifier
    ) {
        LaunchedEffect(Unit) {
            viewModel.observableUiEvent.collect { event ->
                when (event) {
                    TestQuizUiEvent.NavigateToTestResults -> onAllQuestionsAnswered()
                }
            }
        }

        if (currentQuestion != null && remainingQuestionsCount != null) {
            TestQuizContent(
                question = currentQuestion,
                remainingQuestionsCount = remainingQuestionsCount,
                selectedAnswers = uiState.selectedAnswers,
                onAnswerSelected = viewModel::selectAnswer
            )
        }
    }
}

@Composable
fun TestQuizContent(
    question: Hiragana,
    remainingQuestionsCount: Int,
    selectedAnswers: Set<Hiragana>,
    onAnswerSelected: (Hiragana) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = question.kana, style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(stringResource(R.string.remaining_n, remainingQuestionsCount))
        Spacer(modifier = Modifier.padding(4.dp))
        HiraganaKeyboard(
            selectedAnswers = selectedAnswers,
            onButtonClick = onAnswerSelected,
            modifier = modifier.height(240.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TestQuizScreenPreview() {
    MindlesslyHiraganaTheme {
        DefaultScaffold(
            topAppBar = {
                DefaultTopAppBar(
                    title = R.string.test_all_learned,
                    onNavigationIconClick = {}
                )
            }
        ) {
            TestQuizContent(
                question = Hiragana.HI,
                remainingQuestionsCount = 19,
                selectedAnswers = emptySet(),
                onAnswerSelected = {}
            )
        }
    }
}