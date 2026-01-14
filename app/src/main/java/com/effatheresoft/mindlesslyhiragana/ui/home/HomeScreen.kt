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
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.ui.component.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

data class HomeCategory(
    val id: String,
    val title: String,
    val isLocked: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToLearnOrTest: (categoryId: String) -> Unit,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val scope = rememberCoroutineScope()

    HomeDrawer(
        title = R.string.mindlessly_hiragana,
        drawerState = drawerState,
        onResetButtonClick = viewModel::onDrawerResetButtonClick
    ) {
        DefaultScaffold(
            topAppBar = {
                HomeTopAppBar(
                    title = R.string.mindlessly_hiragana,
                    onMenuIconClick = { viewModel.onMenuItemClick(drawerState, scope) }
                )
            },
            modifier = modifier
        ) {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            HomeContent(
                categories = uiState.categories,
                onNavigateToLearnOrTest = onNavigateToLearnOrTest
            )

            when {
                uiState.isResetDialogOpen -> HomeDialog(
                    onConfirm = viewModel::onResetDialogConfirm,
                    onConfirmLabel = R.string.reset,
                    onDismiss = viewModel::onResetDialogDismiss,
                    title = R.string.reset_progress,
                    text = R.string.reset_dialog_text,
                    icon = R.drawable.delete_24px
                )
            }
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
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
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
                initialValue = if(state == "drawer opened" || state == "dialog opened") DrawerValue.Open else DrawerValue.Closed
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
                val ids = listOf("himikase", "Test All Learned", "fuwoya", "ao", "tsuune", "kuherike", "konitana", "sumuroru", "shiimo", "toteso", "wanere", "noyumenu", "yohamaho", "sakichira")
                val titles = listOf("ひみかせ", "Test All Learned", "ふをや", "あお", "つう・んえ", "くへ・りけ", "こに・たな", "すむ・ろる", "しいも", "とてそ", "わねれ", "のゆめぬ", "よはまほ", "さきちら")
                val isLockedStates = listOf(false, false, true, true, true, true, true, true, true, true, true, true, true, true)
                val categories = ids.zip(titles).zip(isLockedStates) { (id, title), isLocked ->
                    HomeCategory(id = id, title = title, isLocked = isLocked)
                }

                HomeContent(
                    categories = categories,
                    onNavigateToLearnOrTest = {}
                )

                if (state == "dialog opened") {
                    HomeDialog(
                        onConfirm = {},
                        onConfirmLabel = R.string.reset,
                        onDismiss = {},
                        title = R.string.reset,
                        text = R.string.reset_dialog_text,
                        icon = R.drawable.delete_24px
                    )
                }
            }
        }
    }
}