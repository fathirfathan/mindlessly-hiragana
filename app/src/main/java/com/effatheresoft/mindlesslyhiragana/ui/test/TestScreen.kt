package com.effatheresoft.mindlesslyhiragana.ui.test

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@Composable
fun TestScreen(
    viewModel: TestViewModel,
    onNavigationIconClick: () -> Unit,
    onChallengeLearn: (categoryId: String) -> Unit,
    onTestAllLearned: () -> Unit
) {
    DefaultScaffold(
        topAppBar = {
            DefaultTopAppBar(
                title = R.string.test_all_learned,
                onNavigationIconClick = onNavigationIconClick
            )
        }
    ) {

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        TestScreenContent(
            categoryList = uiState.categoryList.map { it.kanaWithNakaguro },
            isTestButtonEnabled = uiState.isTestUnlocked,
            onChallengeButtonClick = { onChallengeLearn(uiState.categoryList.last().id) },
            onTestAllLearnedButtonClick = onTestAllLearned
        )
    }
}

@Composable
fun TestScreenContent(
    categoryList: List<String>,
    isTestButtonEnabled: Boolean,
    onChallengeButtonClick: () -> Unit,
    onTestAllLearnedButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 16.dp)
    ) {
        Text(text = stringResource(R.string.test_categories_n, categoryList.size))
        Text(text = stringResource(R.string.including))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f).padding(bottom = 8.dp).verticalScroll(scrollState)
        ) {
            categoryList.forEach { category ->
                Text(text = category)
            }
        }
        Button(onChallengeButtonClick) { Text(stringResource(R.string.challenge_all_correct_on_learn)) }
        Button(onTestAllLearnedButtonClick, enabled = isTestButtonEnabled) { Text(stringResource(R.string.test_all_learned)) }
    }
}

@Preview(showBackground = true)
@Composable
fun TestScreenContentPreview() {
    TestScreenContentPreviewBase()
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 480
)
@Composable
fun TestScreenContentSmallDevicePreview() {
    TestScreenContentPreviewBase()
}

@Composable
fun TestScreenContentPreviewBase() {
    MindlesslyHiraganaTheme {
        DefaultScaffold(
            topAppBar = {
                DefaultTopAppBar(
                    title = R.string.test_all_learned,
                    onNavigationIconClick = {}
                )
            }
        ) {
            TestScreenContent(
                categoryList = HiraganaCategory.entries
                    .filter { it.ordinal <= HiraganaCategory.SAKICHIRA.ordinal }
                    .map { it.kanaWithNakaguro },
                isTestButtonEnabled = false,
                onChallengeButtonClick = {},
                onTestAllLearnedButtonClick = {}
            )
        }
    }
}

@Composable
fun DefaultScaffold(
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { topAppBar() },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->

        Surface(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    @StringRes title: Int,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back_24px),
                    contentDescription = stringResource(R.string.navigate_back)
                )
            }
        },
        modifier = modifier
    )
}
