package com.effatheresoft.mindlesslyhiragana.ui.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.ui.quiz.Quiz
import com.effatheresoft.mindlesslyhiragana.ui.test.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.test.DefaultTopAppBar
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    onNavigateUp: () -> Unit,
    onTryAgain: () -> Unit,
    onTestAllLearned: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = hiltViewModel()
) {
    DefaultScaffold(
        topAppBar = { DefaultTopAppBar(
            title = R.string.result,
            onNavigationIconClick = onNavigateUp
        ) },
        modifier = modifier
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        ResultContent(
            correctCounts = uiState.correctCounts,
            incorrectCounts = uiState.incorrectCounts,
            individualIncorrectCounts = uiState.individualIncorrectCounts,
            onTryAgainButtonClick = onTryAgain,
            onTestAllLearnedButtonClick = onTestAllLearned,
            isTestAllLearnedButtonEnabled = uiState.isTestUnlocked
        )
    }
}

val Quiz.isCorrect
    get() = this.possibleAnswers.count { it.isSelected } == 1
val List<Quiz>.correctCounts
    get() = count { quiz -> quiz.isCorrect }
val List<Quiz>.incorrectCounts
    get() = count { quiz -> !quiz.isCorrect }


@Composable
fun ResultContent(
    correctCounts: Int,
    incorrectCounts: Int,
    individualIncorrectCounts: List<Pair<Hiragana, Int>>,
    onTryAgainButtonClick: () -> Unit,
    onTestAllLearnedButtonClick: () -> Unit,
    isTestAllLearnedButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Text("Correct: $correctCounts")
        Text("Incorrect: $incorrectCounts")
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.incorrect_counts))
        for ((hiragana, count) in individualIncorrectCounts) {
            Text("${hiragana.kana}: $count")
        }

        Spacer(Modifier.weight(1f))
        Button(onClick = onTryAgainButtonClick) { Text("Try Again") }
        Button(
            onClick = onTestAllLearnedButtonClick,
            enabled = isTestAllLearnedButtonEnabled
        ) {
            Text("Test All Learned")
        }
        Spacer(Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    MindlesslyHiraganaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Result") },
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
            ResultContent(
                correctCounts = 4,
                incorrectCounts = 4,
                individualIncorrectCounts = listOf(
                    HI to 0,
                    MI to 2,
                    KA to 0,
                    SE to 2
                ),
                onTryAgainButtonClick = {},
                onTestAllLearnedButtonClick = {},
                isTestAllLearnedButtonEnabled = false,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}