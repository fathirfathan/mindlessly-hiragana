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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    categoryId: String,
    modifier: Modifier = Modifier,
) {
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
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        val learningSetsCount = remember { mutableStateOf(5) }

        LearnScreenContent(
            category = Hiragana.getCategories().first { it.id == categoryId }.toHiraganaStringWithNakaguro(),
            learningSetsCount = learningSetsCount.value,
            onLearningSetsCountChange = { learningSetsCount.value = it },
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

@Preview(showBackground = true)
@Composable
fun LearnScreenPreview() {
    MindlesslyHiraganaTheme {
        Surface {
            LearnScreen("himikase")
        }
    }
}