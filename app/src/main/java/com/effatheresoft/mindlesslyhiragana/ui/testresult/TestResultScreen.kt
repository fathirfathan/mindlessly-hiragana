package com.effatheresoft.mindlesslyhiragana.ui.testresult

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.navigation.NavigationViewModel
import com.effatheresoft.mindlesslyhiragana.ui.test.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.test.DefaultTopAppBar
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@Composable
fun TestResultScreen(
    onNavigateUp: () -> Unit,
    onTryAgain: () -> Unit,
    modifier: Modifier = Modifier,
    navigationViewModel: NavigationViewModel = hiltViewModel(),
    viewModel: TestResultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigationUiState by navigationViewModel.uiState.collectAsStateWithLifecycle()

    DefaultScaffold(
        topAppBar = {
            DefaultTopAppBar(
                title = R.string.result,
                onNavigationIconClick = onNavigateUp
            )
        },
        modifier = modifier
    ) {
        TestResultContent(
            correctCount = navigationUiState.correctCount,
            incorrectCount = navigationUiState.incorrectCount,
            incorrectHiraganaList = navigationUiState.incorrectHiraganaList,
            isContinueLearningButtonEnabled = uiState.canContinueLearning,
            onTryAgainButtonClick = onTryAgain
        )
    }
}

@Composable
fun TestResultContent(
    correctCount: Int,
    incorrectCount: Int,
    incorrectHiraganaList: List<Hiragana>,
    isContinueLearningButtonEnabled: Boolean,
    onTryAgainButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 16.dp)
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
        Button(onClick = onTryAgainButtonClick) { Text(stringResource(R.string.try_again)) }
        Button(
            onClick = {},
            enabled = isContinueLearningButtonEnabled
        ) { Text(stringResource(R.string.continue_learning)) }
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
                correctCount = 4,
                incorrectCount = 0,
                incorrectHiraganaList = listOf(Hiragana.HI),
                isContinueLearningButtonEnabled = false,
                onTryAgainButtonClick = {}
            )
        }
    }
}