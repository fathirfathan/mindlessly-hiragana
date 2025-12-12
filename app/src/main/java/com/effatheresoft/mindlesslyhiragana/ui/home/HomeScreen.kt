package com.effatheresoft.mindlesslyhiragana.ui.home

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToLearn: (categoryId: String) -> Unit,
    onNavigateToTest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.mindlessly_hiragana)) }
            )
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        HomeContent(
            unlockedCategories = uiState.unlockedCategories,
            lockedCategories = uiState.lockedCategories,
            onNavigateToLearn = onNavigateToLearn,
            onNavigateToTest = onNavigateToTest,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun HomeContent(
    unlockedCategories: List<HiraganaCategory>,
    lockedCategories: List<HiraganaCategory>,
    onNavigateToLearn: (categoryId: String) -> Unit,
    onNavigateToTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        for (category in unlockedCategories) {
            CategoryItem(
                title = category.toHiraganaStringWithNakaguro(),
                isLocked = false,
                onClick = { onNavigateToLearn(category.id) }
            )
        }

        CategoryItem(
            title = stringResource(R.string.test_all_learned),
            isLocked = false,
            onClick = onNavigateToTest
        )

        for (category in lockedCategories) {
            CategoryItem(
                title = category.toHiraganaStringWithNakaguro(),
                isLocked = true,
                onClick = {}
            )
        }
    }
}

@Composable
fun CategoryItem(
    title: String,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            Text(title)
            Spacer(modifier = Modifier.weight(1f))
            when(isLocked) {
                true -> Icon(
                    painter = painterResource(R.drawable.lock_24px),
                    contentDescription = stringResource(R.string.x_category_locked, title)
                )
                false -> Icon(
                    painter = painterResource(R.drawable.arrow_right_24px),
                    contentDescription = stringResource(R.string.x_category_unlocked, title)
                )

            }
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
                        title = { Text(stringResource(R.string.mindlessly_hiragana)) }
                    )
                }
            ) { paddingValues ->
                HomeContent(
                    unlockedCategories = HiraganaCategory.entries.take(3),
                    lockedCategories = HiraganaCategory.entries.drop(3),
                    onNavigateToLearn = {},
                    onNavigateToTest = {},
                    modifier = Modifier.padding(paddingValues)
                )

            }
        }
    }
}