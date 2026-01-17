package com.effatheresoft.mindlesslyhiragana.ui.learn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.ui.component.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.component.DefaultTopAppBar
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    onNavigationIconClick: () -> Unit,
    onLearnButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LearnViewModel
) {
    DefaultScaffold(
        topAppBar = { DefaultTopAppBar(
            title = R.string.learn,
            onNavigationIconClick = onNavigationIconClick
        ) },
        modifier = modifier
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        uiState.category?.let { category ->
            uiState.learningSetsCount?.let { learningSetsCount ->
                LearnScreenContent(
                    category = category.toHiraganaStringWithNakaguro(),
                    learningSetsCount = learningSetsCount,
                    onLearningSetsCountChange = { viewModel.updateLearningSetsCount(it) },
                    onLearnButtonClick = onLearnButtonClick,
                )
            }
        }
    }
}

@Composable
fun LearnScreenContent(
    category: String,
    learningSetsCount: Int,
    onLearningSetsCountChange: (Int) -> Unit,
    onLearnButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(category)
        Text(stringResource(R.string.learning_sets_n_sets, learningSetsCount))
        Slider(
            value = learningSetsCount.toFloat(),
            valueRange = 1f..10f,
            steps = 8,
            onValueChange = { onLearningSetsCountChange(it.toInt()) }
        )
        Button(onClick = onLearnButtonClick) { Text(stringResource(R.string.learn)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LearnScreenPreview() {
    MindlesslyHiraganaTheme {
        Scaffold(
            topBar = { DefaultTopAppBar(
                title = R.string.learn,
                onNavigationIconClick = {}
            ) },
            modifier = Modifier.fillMaxSize(),
        ) { paddingValues ->
            LearnScreenContent(
                category = HIMIKASE.kanaWithNakaguro,
                learningSetsCount = DEFAULT_LEARNING_SETS_COUNT,
                onLearningSetsCountChange = {},
                onLearnButtonClick = {},
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}