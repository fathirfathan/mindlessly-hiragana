package com.effatheresoft.mindlesslyhiragana.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.getLearnedHiraganaUpToId
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToLearn: (Int) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    DetailsScreenContent(
        modifier = modifier,
        uiState = uiState,
        onNavigationIconClicked = onNavigationIconClicked,
        onNavigateToLearn = { viewModel.onNavigateToLearn(onNavigateToLearn) },
        onLearningSetsCountChange = viewModel::onLearningSetsCountChange
    )
}

@Composable
fun DetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: DetailsUiState,
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToLearn: () -> Unit = {},
    onLearningSetsCountChange: (Int) -> Unit = {}
) {
    DefaultScaffold(
        appBarTitle = when (uiState) {
            is DetailsUiState.Success -> uiState.appBarTitle
            is DetailsUiState.Loading -> "Loading..."
            is DetailsUiState.Error -> "Error"
        },
        onNavigationIconClicked = onNavigationIconClicked
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (uiState) {
                is DetailsUiState.Success -> {
                    if (uiState.isTest) {
                        Text(text = "Questions: ${uiState.testHiraganaList.size}")
                        Text(
                            uiState.testHiraganaList.map { it.hiragana }
                                .chunked(5)
                                .joinToString("\n") { it.joinToString(" ") }
                        )
                        Button(onClick = onNavigateToLearn) {
                            Text("Test All Learned")
                        }
                    } else {
                        Text(text = "Learning Sets: ${uiState.learningSetsCount} Sets")

                        Slider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            value = uiState.learningSetsCount.toFloat(),
                            onValueChange = { onLearningSetsCountChange(it.toInt()) },
                            steps = 3,
                            valueRange = 1f..5f
                        )

                        Button(onClick = onNavigateToLearn) {
                            Text("Learn")
                        }
                    }
                }
                is DetailsUiState.Loading -> {
                    Text("App is loading...")
                }
                is DetailsUiState.Error -> {
                    Text("An error occurred.")
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenSuccessPreview() {
    DetailsScreenContent(
        uiState = DetailsUiState.Success(
            appBarTitle = "ひみかせ HI MI KA SE",
            learningSetsCount = 3,
            isTest = false,
            testHiraganaList = listOf()
        )
    )
}

@Preview
@Composable
fun DetailsScreenSuccessTestPreview() {
    DetailsScreenContent(
        uiState = DetailsUiState.Success(
            appBarTitle = "Test All Learned",
            learningSetsCount = 3,
            isTest = true,
            testHiraganaList = Hiragana.categories.getLearnedHiraganaUpToId("2")
        )
    )
}

@Preview
@Composable
fun DetailsScreenLoadingPreview() {
    DetailsScreenContent(
        uiState = DetailsUiState.Loading
    )
}

@Preview
@Composable
fun DetailsScreenErrorPreview() {
    DetailsScreenContent(
        uiState = DetailsUiState.Error(Exception("Error"))
    )
}
