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
    var appBarTitle: String
    var learningSetsCount = 3
    var learningSetsSliderLabel: String
    var isStateSuccess: Boolean

    when (uiState) {
        is DetailsUiState.Success -> {
            learningSetsCount = uiState.learningSetsCount
            learningSetsSliderLabel = "Learning Sets: $learningSetsCount Sets"
            appBarTitle = uiState.appBarTitle
            isStateSuccess = true
        }
        is DetailsUiState.Loading -> {
            learningSetsSliderLabel = "Learning Sets: Loading..."
            appBarTitle = "Loading..."
            isStateSuccess = false
        }
        is DetailsUiState.Error -> {
            learningSetsSliderLabel = "Learning Sets: "
            appBarTitle = "Error"
            isStateSuccess = false
        }
    }

    DefaultScaffold(
        appBarTitle = appBarTitle,
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
            Text(learningSetsSliderLabel)

            Slider(
                modifier = Modifier.padding(horizontal = 16.dp),
                enabled = isStateSuccess,
                value = learningSetsCount.toFloat(),
                onValueChange = { onLearningSetsCountChange(it.toInt()) },
                steps = 3,
                valueRange = 1f..5f
            )

            Button(
                onClick = onNavigateToLearn,
                enabled = isStateSuccess
            ) {
                Text("Learn")
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenSuccessPreview() {
    DetailsScreenContent(
        uiState = DetailsUiState.Success("ひみかせ HI MI KA SE", 3)
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
