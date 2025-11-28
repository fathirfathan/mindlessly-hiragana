package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mindlessly Hiragana") }
            )
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        HomeContent(
            unlockedCategories = uiState.unlockedCategories,
            lockedCategories = uiState.lockedCategories,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun HomeContent(
    unlockedCategories: List<HiraganaCategory>,
    lockedCategories: List<HiraganaCategory>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 16.dp)
    ) {
        for (category in unlockedCategories) {
            CategoryItem(
                title = category.toHiraganaStringWithNakaguro(),
                isLocked = false
            )
        }

        CategoryItem(
            title = "Test All Learned",
            isLocked = false
        )

        for (category in lockedCategories) {
            CategoryItem(
                title = category.toHiraganaStringWithNakaguro(),
                isLocked = true
            )
        }
    }
}

@Composable
fun CategoryItem(
    title: String,
    isLocked: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(title)
        Spacer(modifier = Modifier.weight(1f))
        when(isLocked) {
            true -> Icon(
                painter = painterResource(R.drawable.lock_24px),
                contentDescription = "$title category locked"
            )
            false -> Icon(
                painter = painterResource(R.drawable.arrow_right_24px),
                contentDescription = "$title category unlocked"
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MindlesslyHiraganaTheme {
        Surface {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Mindlessly Hiragana") }
                    )
                }
            ) { paddingValues ->
                HomeContent(
                    unlockedCategories = Hiragana.getCategories().take(3),
                    lockedCategories = Hiragana.getCategories().drop(3),
                    modifier = Modifier.padding(paddingValues)
                )

            }
        }
    }
}