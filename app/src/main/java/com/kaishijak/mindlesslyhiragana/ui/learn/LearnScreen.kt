package com.kaishijak.mindlesslyhiragana.ui.learn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.kaishijak.mindlesslyhiragana.R
import com.kaishijak.mindlesslyhiragana.data.model.HiraganaCategory
import com.kaishijak.mindlesslyhiragana.ui.component.DefaultScaffold
import com.kaishijak.mindlesslyhiragana.ui.component.DefaultTopAppBar
import com.kaishijak.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    viewModel: LearnViewModel,
    onNavigationIconClick: () -> Unit,
    onLearnButtonClick: () -> Unit,
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
        val repeatCategoryCount = uiState.repeatCategoryCount

        if(category != null && repeatCategoryCount != null) {
            LearnScreenContent(
                category = category.kanaWithNakaguro,
                repeatCategoryCount = repeatCategoryCount,
                onRepeatCategoryCountChange = viewModel::updateRepeatCategoryCount,
                onLearnButtonClick = onLearnButtonClick,
            )
        }
    }
}

@Composable
fun LearnScreenContent(
    category: String,
    repeatCategoryCount: Int,
    onRepeatCategoryCountChange: (Int) -> Unit,
    onLearnButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(category)
        Text(stringResource(R.string.learning_sets_n_sets, repeatCategoryCount))
        Slider(
            value = repeatCategoryCount.toFloat(),
            onValueChange = { onRepeatCategoryCountChange(it.toInt()) },
            valueRange = 1f..10f,
            steps = 8
        )
        Button(onClick = onLearnButtonClick) { Text(stringResource(R.string.learn)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LearnScreenPreview() {
    MindlesslyHiraganaTheme {
        DefaultScaffold(
            topAppBar = {
                DefaultTopAppBar(
                    title = R.string.learn,
                    onNavigationIconClick = {}
                )
            }
        ) {
            LearnScreenContent(
                category = HiraganaCategory.HIMIKASE.kanaWithNakaguro,
                repeatCategoryCount = 5,
                onRepeatCategoryCountChange = {},
                onLearnButtonClick = {},
            )
        }
    }
}