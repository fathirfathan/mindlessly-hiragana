package com.kaishijak.mindlesslyhiragana.ui.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaishijak.mindlesslyhiragana.R
import com.kaishijak.mindlesslyhiragana.data.model.Hiragana
import com.kaishijak.mindlesslyhiragana.data.model.HiraganaCategory
import com.kaishijak.mindlesslyhiragana.ui.component.DefaultScaffold
import com.kaishijak.mindlesslyhiragana.ui.component.DefaultTopAppBar
import com.kaishijak.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onNavigationIconClick: () -> Unit,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    DefaultScaffold(
        topAppBar = {
            DefaultTopAppBar(
                title = R.string.learn,
                onNavigationIconClick = onNavigationIconClick
            )
        },
        modifier = modifier
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val category = uiState.category
        val currentQuiz = uiState.currentQuiz
        val remainingQuestionsCount = uiState.remainingQuestionsCount

        LaunchedEffect(Unit) {
            viewModel.observableUiEvent.collect { when (it) { QuizUiEvent.NavigateToResult -> onCompleted() } }
        }

        if ((category != null) && (currentQuiz != null) && (remainingQuestionsCount != null)) {
            QuizContent(
                category = category,
                question = currentQuiz,
                selectedAnswers = uiState.selectedAnswers,
                remainingQuestionsCount = remainingQuestionsCount,
                onAnswerSelected = viewModel::selectAnswer
            )
        }
    }
}

@Composable
fun QuizContent(
    category: HiraganaCategory,
    question: Hiragana,
    selectedAnswers: Set<Hiragana>,
    remainingQuestionsCount: Int,
    onAnswerSelected: (Hiragana) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(bottom = 16.dp).padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = question.kana, style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text("Remaining: $remainingQuestionsCount")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            for (answer in category.hiraganaList) {
                Button(
                    onClick = { onAnswerSelected(answer) },
                    enabled = answer !in selectedAnswers
                ) {
                    Text(answer.name)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    MindlesslyHiraganaTheme {
        DefaultScaffold(
            topAppBar = {
                DefaultTopAppBar(
                    title = R.string.learn,
                    onNavigationIconClick = {}
                )
            }
        ) {
            QuizContent(
                category = HiraganaCategory.HIMIKASE,
                question = Hiragana.HI,
                selectedAnswers = emptySet(),
                remainingQuestionsCount = 19,
                onAnswerSelected = {}
            )
        }
    }
}