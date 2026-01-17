package com.effatheresoft.mindlesslyhiragana.ui.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.ui.component.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.component.DefaultTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit,
    onCompleted: () -> Unit,
    viewModel: QuizViewModel
) {
    DefaultScaffold(
        topAppBar = { DefaultTopAppBar(
            title = R.string.learn,
            onNavigationIconClick = onNavigationIconClick
        ) },
        modifier = modifier
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect {
                when (it) {
                    QuizUiEvent.NavigateToResult -> onCompleted()
                }
            }
        }

        uiState.currentQuiz?.let { currentQuiz ->
            uiState.remainingQuestionsCount?.let { remainingQuestionsCount ->
                QuizContent(
                    question = currentQuiz.question,
                    remainingQuestionsCount = remainingQuestionsCount,
                    possibleAnswers = currentQuiz.possibleAnswers,
                    onAnswerSelected = viewModel::selectCurrentQuizAnswer
                )
            }
        }
    }
}

@Composable
fun QuizContent(
    question: Hiragana,
    remainingQuestionsCount: Int,
    possibleAnswers: List<PossibleAnswer>,
    onAnswerSelected: (Hiragana) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(text = question.kana, style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text("Remaining: $remainingQuestionsCount")
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            for (answer in possibleAnswers) {
                Button(
                    onClick = { onAnswerSelected(answer.answer) },
                    enabled = !answer.isSelected
                ) {
                    Text(answer.answer.name)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.learn)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_24px),
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        QuizContent(
            question = HI,
            remainingQuestionsCount = HIMIKASE.hiraganaList.size * DEFAULT_LEARNING_SETS_COUNT - 1,
            possibleAnswers = listOf(
                PossibleAnswer(answer = HI, isCorrect = true, isSelected = false),
                PossibleAnswer(answer = MI, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = KA, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = SE, isCorrect = false, isSelected = false)
            ),
            onAnswerSelected = {},
            modifier = Modifier.padding(paddingValues)
        )
    }
}