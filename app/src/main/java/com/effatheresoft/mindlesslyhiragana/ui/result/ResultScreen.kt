package com.effatheresoft.mindlesslyhiragana.ui.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.ui.component.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.component.DefaultTopAppBar
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: ResultViewModel,
    onNavigateUp: () -> Unit,
    onTryAgain: () -> Unit,
    onTestAllLearned: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DefaultScaffold(
        topAppBar = {
            DefaultTopAppBar(
                title = R.string.result,
                onNavigationIconClick = onNavigateUp
            )
        },
        modifier = modifier
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val correctCounts = uiState.correctCounts
        val incorrectCounts = uiState.incorrectCounts

        if (correctCounts != null && incorrectCounts != null) {
            ResultContent(
                correctCounts = correctCounts,
                incorrectCounts = incorrectCounts,
                individualIncorrectCounts = uiState.individualIncorrectCounts,
                isTestAllLearnedButtonEnabled = uiState.isTestUnlocked,
                onTryAgainButtonClick = onTryAgain,
                onTestAllLearnedButtonClick = onTestAllLearned
            )
        }
    }
}

@Composable
fun ResultContent(
    correctCounts: Int,
    incorrectCounts: Int,
    individualIncorrectCounts: List<Pair<Hiragana, Int>>,
    isTestAllLearnedButtonEnabled: Boolean,
    onTryAgainButtonClick: () -> Unit,
    onTestAllLearnedButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.correct_n, correctCounts))
        Text(stringResource(R.string.incorrect_n, incorrectCounts))
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.incorrect_counts))
        for ((hiragana, count) in individualIncorrectCounts) {
            Text("${hiragana.kana}: $count")
        }

        Spacer(Modifier.weight(1f))
        Button(onClick = onTryAgainButtonClick) { Text(stringResource(R.string.try_again)) }
        Button(
            onClick = onTestAllLearnedButtonClick,
            enabled = isTestAllLearnedButtonEnabled
        ) {
            Text(stringResource(R.string.test_all_learned))
        }
        Spacer(Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    MindlesslyHiraganaTheme {
        DefaultScaffold(
            topAppBar = {
                DefaultTopAppBar(
                    title = R.string.result,
                    onNavigationIconClick = {}
                )
            }
        ) {
            ResultContent(
                correctCounts = 20,
                incorrectCounts = 0,
                individualIncorrectCounts = listOf(
                    HI to 0,
                    MI to 0,
                    KA to 0,
                    SE to 0
                ),
                isTestAllLearnedButtonEnabled = false,
                onTryAgainButtonClick = {},
                onTestAllLearnedButtonClick = {}
            )
        }
    }
}