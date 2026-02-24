package com.kaishijak.mindlesslyhiragana.ui.testresult

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaishijak.mindlesslyhiragana.R
import com.kaishijak.mindlesslyhiragana.data.model.Hiragana
import com.kaishijak.mindlesslyhiragana.data.model.HiraganaCategory
import com.kaishijak.mindlesslyhiragana.ui.component.DefaultScaffold
import com.kaishijak.mindlesslyhiragana.ui.component.DefaultTopAppBar
import com.kaishijak.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@Composable
fun TestResultScreen(
    viewModel: TestResultViewModel,
    onNavigateUp: () -> Unit,
    onTryAgain: () -> Unit,
    onContinueLearning: (newProgress: HiraganaCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val progress = uiState.progress
    val correctCount = uiState.correctCount
    val incorrectCount = uiState.incorrectCount

    DefaultScaffold(
        topAppBar = {
            DefaultTopAppBar(
                title = R.string.result,
                onNavigationIconClick = onNavigateUp
            )
        },
        modifier = modifier
    ) {
        if (progress != null && correctCount != null && incorrectCount != null) {
            TestResultContent(
                correctCount = correctCount,
                incorrectCount = incorrectCount,
                incorrectHiraganaList = uiState.incorrectHiraganaList,
                isContinueLearningButtonEnabled = uiState.canContinueLearning,
                onTryAgainButtonClick = onTryAgain,
                onContinueLearningButtonClick = { onContinueLearning(progress) }
            )

        }
    }
}

@Composable
fun TestResultContent(
    correctCount: Int,
    incorrectCount: Int,
    incorrectHiraganaList: List<Hiragana>,
    isContinueLearningButtonEnabled: Boolean,
    onTryAgainButtonClick: () -> Unit,
    onContinueLearningButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.correct_n, correctCount))
        Text(stringResource(R.string.incorrect_n, incorrectCount))
        Text(stringResource(R.string.incorrect_hiragana))
        incorrectHiraganaList.chunked(4).forEach { fourHiragana ->
            Text(
                fourHiragana.joinToString(", ") {
                    hiragana -> "${hiragana.kana}(${hiragana.name})"
                }.trim()
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onTryAgainButtonClick,
            enabled = !isContinueLearningButtonEnabled
        ) {
            Text(stringResource(R.string.try_again))
        }
        Button(
            onClick = onContinueLearningButtonClick,
            enabled = isContinueLearningButtonEnabled
        ) {
            Text(stringResource(R.string.continue_learning))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestResultScreenPreview() {
    MindlesslyHiraganaTheme {
        DefaultScaffold(
            topAppBar = {
                DefaultTopAppBar(
                    title = R.string.result,
                    onNavigationIconClick = {}
                )
            }
        ) {
            TestResultContent(
                correctCount = 3,
                incorrectCount = 1,
                incorrectHiraganaList = listOf(Hiragana.HI),
                isContinueLearningButtonEnabled = false,
                onTryAgainButtonClick = {},
                onContinueLearningButtonClick = {}
            )
        }
    }
}