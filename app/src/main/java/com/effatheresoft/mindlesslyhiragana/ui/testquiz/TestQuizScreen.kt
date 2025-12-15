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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.test.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.test.DefaultTopAppBar
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@Composable
fun TestQuizScreen(
    question: String = "ひ",
    remainingQuestionsCount: Int = 3,
    modifier: Modifier = Modifier
) {

    DefaultScaffold(
        topAppBar = {
            DefaultTopAppBar(
                title = R.string.test_all_learned,
                onNavigationIconClick = {}
            )
        },
        modifier = modifier
    ) {
        TestQuizContent(
            question = question,
            remainingQuestionsCount = remainingQuestionsCount
        )
    }
}

@Composable
fun TestQuizContent(
    question: String,
    remainingQuestionsCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(16.dp).padding(bottom = 16.dp)
    ) {
        Text(text = question, style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text("Remaining: $remainingQuestionsCount")
        Spacer(modifier = Modifier.padding(4.dp))
        HiraganaKeyboard(modifier = modifier.height(240.dp))
    }
}

@Composable
fun HiraganaKeyboard(modifier: Modifier = Modifier) {
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
                    item(it) { HiraganaKeyboardButton(it) }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                }
                Hiragana.WA -> {
                    item(it) { HiraganaKeyboardButton(it) }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                }
                Hiragana.N -> {
                    item(it) { HiraganaKeyboardButton(it) }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                }
                else -> item(it) { HiraganaKeyboardButton(it) }
            }
        }
    }
}

@Composable
fun HiraganaKeyboardButton(hiragana: Hiragana) {
    Button(onClick = {}, modifier = Modifier.width(76.dp)) {
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
                question = "ひ",
                remainingQuestionsCount = 3
            )
        }
    }
}