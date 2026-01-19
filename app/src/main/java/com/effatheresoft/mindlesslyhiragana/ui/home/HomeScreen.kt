package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.Constants.TEST_ALL_LEARNED_CATEGORY_ID
import com.effatheresoft.mindlesslyhiragana.Constants.TEST_ALL_LEARNED_CATEGORY_TITLE
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.component.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import kotlinx.coroutines.launch

data class HomeCategory(
    val id: String,
    val title: String,
    val isLocked: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToLearnOrTest: (categoryId: String) -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val scope = rememberCoroutineScope()

    HomeDrawer(
        title = R.string.mindlessly_hiragana,
        drawerState = drawerState,
        onResetButtonClick = viewModel::onResetDialogOpen
    ) {
        DefaultScaffold(
            topAppBar = {
                HomeTopAppBar(
                    title = R.string.mindlessly_hiragana,
                    onMenuIconClick = { viewModel.onDrawerToggled(drawerState, scope) }
                )
            },
            modifier = modifier
        ) {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.isResetDialogOpen) {
                HomeDialog(
                    icon = R.drawable.delete_24px,
                    title = R.string.reset_progress,
                    text = R.string.reset_dialog_text,
                    onConfirmLabel = R.string.reset,
                    onConfirm = {
                        viewModel.onResetDialogConfirm()
                        scope.launch { drawerState.close() }
                    },
                    onDismiss = viewModel::onResetDialogDismiss
                )
            }

            HomeContent(
                categories = uiState.categories,
                onNavigateToLearnOrTest = onNavigateToLearnOrTest
            )
        }
    }
}

@Composable
fun HomeContent(
    categories: List<HomeCategory>,
    onNavigateToLearnOrTest: (categoryId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState)
    ) {
        for (category in categories) {
            HomeCategoryItem(
                title = category.title,
                isLocked = category.isLocked,
                onClick = { if (!category.isLocked) onNavigateToLearnOrTest(category.id) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenPreviewBase("main")
}

@Preview(showBackground = true)
@Composable
fun HomeScreenDrawerPreview() {
    HomeScreenPreviewBase("drawer opened")
}

@Preview(showBackground = true)
@Composable
fun HomeScreenDialogPreview() {
    HomeScreenPreviewBase("dialog opened")
}

@Composable
fun HomeScreenPreviewBase(state: String) {
    MindlesslyHiraganaTheme {
        HomeDrawer(
            title = R.string.mindlessly_hiragana,
            drawerState = rememberDrawerState(
                initialValue = when(state) {
                    "drawer opened", "dialog opened" -> DrawerValue.Open
                    else -> DrawerValue.Closed
                }
            ),
            onResetButtonClick = {}
        ) {
            DefaultScaffold(
                topAppBar = {
                    HomeTopAppBar(
                        title = R.string.mindlessly_hiragana,
                        onMenuIconClick = {}
                    )
                }
            ) {
                val ids = HiraganaCategory.entries.map { it.id }.toMutableList().apply {
                    add(1,TEST_ALL_LEARNED_CATEGORY_ID )
                }.toList()
                val titles = HiraganaCategory.entries.last().complementedHiraganaCategory
                    .map { it.kanaWithNakaguro }.toMutableList().apply {
                        add(1, TEST_ALL_LEARNED_CATEGORY_TITLE)
                    }.toList()
                val isLockedStates = HiraganaCategory.entries.map { true }.toMutableList().apply {
                    add(0, false)
                    add(1, false)
                }
                val categories = ids.zip(titles).zip(isLockedStates) {
                    (id, title), isLocked ->
                    HomeCategory(id = id, title = title, isLocked = isLocked)
                }

                HomeContent(
                    categories = categories,
                    onNavigateToLearnOrTest = {}
                )

                if (state == "dialog opened") {
                    HomeDialog(
                        icon = R.drawable.delete_24px,
                        title = R.string.reset,
                        text = R.string.reset_dialog_text,
                        onConfirmLabel = R.string.reset,
                        onConfirm = {},
                        onDismiss = {}
                    )
                }
            }
        }
    }
}