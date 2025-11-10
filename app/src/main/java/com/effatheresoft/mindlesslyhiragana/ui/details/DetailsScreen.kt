package com.effatheresoft.mindlesslyhiragana.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
    onNavigateToLearn: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    DetailsScreenContent(
        modifier = modifier,
        uiState = uiState,
        onNavigationIconClicked = onNavigationIconClicked,
        onNavigateToLearn = onNavigateToLearn
    )
}

@Composable
fun DetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: DetailsUiState,
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToLearn: () -> Unit = {}
) {
    DefaultScaffold(
        appBarTitle = when (uiState) {
            is DetailsUiState.Success -> uiState.appBarTitle
            is DetailsUiState.Loading -> "Loading"
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onNavigateToLearn,
                enabled = when (uiState) {
                    is DetailsUiState.Success -> true
                    is DetailsUiState.Loading -> false
                    is DetailsUiState.Error -> false
                }
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
        uiState = DetailsUiState.Success("ひみかせ HI MI KA SE")
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
