package com.effatheresoft.mindlesslyhiragana.learn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    categoryId: String,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LearnViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Learn") },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_24px),
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LearnScreenContent(
            category = Hiragana.getCategories().first { it.id == categoryId }.toHiraganaStringWithNakaguro(),
            learningSetsCount = uiState.learningSetsCount,
            onLearningSetsCountChange = { viewModel.updateLearningSetsCount(it) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun LearnScreenContent(
    category: String,
    learningSetsCount: Int,
    onLearningSetsCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(category)
        Text("Learning Sets: $learningSetsCount Sets")
        Slider(
            value = learningSetsCount.toFloat(),
            valueRange = 1f..10f,
            steps = 8,
            onValueChange = { onLearningSetsCountChange(it.toInt()) }
        )
        Button(onClick = {}) { Text("Learn") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LearnScreenPreview() {
    MindlesslyHiraganaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Learn") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.arrow_back_24px),
                                contentDescription = "Navigate back"
                            )
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) { paddingValues ->
            LearnScreenContent(
                category = "himikase",
                learningSetsCount = 5,
                onLearningSetsCountChange = {},
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}