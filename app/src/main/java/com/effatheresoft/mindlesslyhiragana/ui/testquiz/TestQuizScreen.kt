package com.effatheresoft.mindlesslyhiragana.ui.testquiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.test.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.test.DefaultTopAppBar
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@Composable
fun TestQuizScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TestQuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DefaultScaffold(
        topAppBar = {
            DefaultTopAppBar(
                title = R.string.test_all_learned,
                onNavigationIconClick = onNavigateUp
            )
        },
        modifier = modifier
    ) {
        uiState.currentQuestion?.let { question ->
            TestQuizContent(
                question = question,
                remainingQuestionsCount = uiState.remainingQuestionsCount,
                onAnswerSelected = viewModel::selectAnswer
            )
        }
    }
}

@Composable
fun TestQuizContent(
    question: Hiragana,
    remainingQuestionsCount: Int,
    onAnswerSelected: (Hiragana) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(16.dp).padding(bottom = 16.dp)
    ) {
        Text(text = question.kana, style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text("Remaining: $remainingQuestionsCount")
        Spacer(modifier = Modifier.padding(4.dp))
        HiraganaKeyboard(
            onButtonClick = onAnswerSelected,
            modifier = modifier.height(240.dp)
        )
    }
}

@Composable
fun HiraganaKeyboard(
    onButtonClick: (Hiragana) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyGridState()
    LazyHorizontalGrid(
        state = listState,
        rows = GridCells.Fixed(5),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {

        Hiragana.entries.forEach {
            when (it) {
                Hiragana.YU -> {
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                    item(it) {
                        HiraganaKeyboardButton(
                            hiragana = it,
                            onButtonClick = { onButtonClick(it) }
                        )
                    }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                }
                Hiragana.WA -> {
                    item(it) {
                        HiraganaKeyboardButton(
                            hiragana = it,
                            onButtonClick = { onButtonClick(it) }
                        )
                    }
                    repeat(3) { item { Spacer(modifier = Modifier.width(72.dp)) } }
                }
                Hiragana.N -> {
                    item(it) {
                        HiraganaKeyboardButton(
                            hiragana = it,
                            onButtonClick = { onButtonClick(it) }
                        )
                    }
                    repeat(4) { item { Spacer(modifier = Modifier.width(72.dp)) } }
                }
                else -> item(it) {
                    HiraganaKeyboardButton(
                        hiragana = it,
                        onButtonClick = { onButtonClick(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun HiraganaKeyboardButton(
    hiragana: Hiragana,
    onButtonClick: () -> Unit
) {
    Button(onClick = onButtonClick, modifier = Modifier.width(76.dp)) {
        Text(hiragana.name)
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
                remainingQuestionsCount = 3,
                onAnswerSelected = {}
            )
        }
    }
}