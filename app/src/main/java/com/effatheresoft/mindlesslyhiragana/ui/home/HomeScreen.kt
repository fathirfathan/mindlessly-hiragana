package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToLearn: (categoryId: String) -> Unit,
    onNavigateToTest: (categoryId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val scope = rememberCoroutineScope()

    DefaultDrawer(
        title = R.string.mindlessly_hiragana,
        drawerState = drawerState,
        onResetButtonClick = viewModel::onDrawerResetButtonClick
    ) {
        HomeScaffold(
            topAppBar = {
                HomeTopAppBar(
                    title = R.string.mindlessly_hiragana,
                    onMenuIconClick = { scope.launch {
                        when (drawerState.currentValue) {
                            DrawerValue.Closed -> drawerState.open()
                            DrawerValue.Open -> drawerState.close()
                        }
                    } }
                )
            },
            modifier = modifier
        ) {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            HomeContent(
                unlockedCategories = uiState.unlockedCategories,
                lockedCategories = uiState.lockedCategories,
                onNavigateToLearn = onNavigateToLearn,
                onNavigateToTest = { onNavigateToTest(uiState.progress) }
            )

            when {
                uiState.isResetDialogOpen -> DefaultDialog(
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
fun DefaultDialog(
    onConfirm: () -> Unit,
    @StringRes onConfirmLabel: Int,
    onDismiss: () -> Unit,
    @StringRes title: Int,
    @StringRes text: Int,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { Button(onConfirm) { Text(stringResource(onConfirmLabel)) } },
        dismissButton = { Button(onConfirm) { Text(stringResource(R.string.cancel)) } },
        title = { Text(stringResource(title)) },
        text = { Text(stringResource(text)) },
        icon = { Icon(painterResource(icon), null) },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultDialogPreview() {
    MindlesslyHiraganaTheme {
        Surface {
            DefaultDialog(
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
fun DefaultDrawer(
    @StringRes title: Int,
    drawerState: DrawerState,
    onResetButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.reset_progress)) },
                    selected = false,
                    onClick = onResetButtonClick,
                    icon = { Icon(
                        painter = painterResource(R.drawable.delete_24px),
                        contentDescription = null
                    ) }
                )
            }
        },
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun HomeScaffold(
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { topAppBar() },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->

        Surface(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    @StringRes title: Int,
    onMenuIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = onMenuIconClick) {
                Icon(
                    painter = painterResource(R.drawable.menu_24px),
                    contentDescription = stringResource(R.string.open_menu)
                )
            }
        },
        modifier = modifier
    )
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
            DefaultDrawer(
                title = R.string.mindlessly_hiragana,
                drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                onResetButtonClick = {}
            ) {
                HomeScaffold(
                    topAppBar = {
                        HomeTopAppBar(
                            title = R.string.mindlessly_hiragana,
                            onMenuIconClick = {}
                        )
                    }
                ) {
                    HomeContent(
                        unlockedCategories = HiraganaCategory.entries.take(3),
                        lockedCategories = HiraganaCategory.entries.drop(3),
                        onNavigateToLearn = {},
                        onNavigateToTest = {}
                    )
                }
            }
        }
    }
}