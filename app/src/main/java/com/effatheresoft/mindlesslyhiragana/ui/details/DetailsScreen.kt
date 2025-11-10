package com.effatheresoft.mindlesslyhiragana.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold

@Composable
fun DetailsScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = viewModel(),
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToLearn: () -> Unit = {}
) {
    LaunchedEffect(Unit){
        viewModel.initializeWithId(id)
    }
    val uiState = viewModel.uiState

    DefaultScaffold(
        appBarTitle = uiState.appBarTitle,
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
            Button(onClick = onNavigateToLearn) {
                Text("Learn")
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    DetailsScreen(
        id = "0"
    )
}
