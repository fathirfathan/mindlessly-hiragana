package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.DefaultViewModelProvider
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.common.HomeDrawer
import com.effatheresoft.mindlesslyhiragana.ui.common.HomeTopAppBar
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = DefaultViewModelProvider.Factory),
    onNavigateToDetails: (String) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    HomeScreenContent(
        uiState = uiState,
        onRestartProgressConfirmed = viewModel::restartProgress,
        onCategoryClicked = { viewModel.onCategoryClicked(it, onNavigateToDetails) },
        onRestartDialogToggled = viewModel::toggleRestartDialog,
        onDrawerToggled = viewModel::toggleDrawer
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState.Loading,
    onRestartProgressConfirmed: () -> Unit = {},
    onCategoryClicked: (String) -> Unit = {},
    onRestartDialogToggled: () -> Unit = {},
    onDrawerToggled: () -> Unit = {}
) {
    HomeDrawer(
        drawerState = rememberDrawerState(initialValue = if (uiState is HomeUiState.Success) {
            if (uiState.isDrawerOpen) DrawerValue.Open else DrawerValue.Closed
        } else {
            DrawerValue.Closed
        }),
        onRestartProgressClicked = onRestartDialogToggled
    ) { drawerState ->
        DefaultScaffold(topBar = {
            HomeTopAppBar(onNavigationIconClicked = onDrawerToggled)
        }) {
            Column {
                when(uiState) {
                    is HomeUiState.Success -> {
                        LaunchedEffect(uiState.isDrawerOpen) {
                            if (uiState.isDrawerOpen) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                        LaunchedEffect(drawerState.isClosed) {
                            if (drawerState.isClosed && uiState.isDrawerOpen) onDrawerToggled()
                            if (drawerState.isOpen && !uiState.isDrawerOpen) onDrawerToggled()
                        }

                        LazyColumn(modifier.padding(horizontal = 16.dp)) {
                            items(items = Hiragana.categories) { item ->
                                if (item.id.toInt() <= uiState.highestCategoryId.toInt()) {
                                    HomeListItem(
                                        text = item.hiraganaList.joinToString(" ") { it.hiragana },
                                        isUnlocked = true,
                                        onClick = { onCategoryClicked(item.id) }
                                    )
                                }
                            }
                            item {
                                HomeListItem(
                                    text = "Test All Learned",
                                    onClick = { onCategoryClicked("12") }
                                )
                            }
                            items(items = Hiragana.categories) { item ->
                                if (item.id.toInt() > uiState.highestCategoryId.toInt()) {
                                    HomeListItem(
                                        text = item.hiraganaList.joinToString(" ") { it.hiragana },
                                        isUnlocked = false
                                    )
                                }
                            }
                        }

                        if (uiState.isRestartDialogShown) {
                            AlertDialog(
                                icon = {},
                                title = { Text("Restart Progress") },
                                text = { Text("Are you sure you want to restart your progress?") },
                                onDismissRequest = onRestartDialogToggled,
                                confirmButton = {
                                    TextButton(onClick = {
                                        onRestartDialogToggled()
                                        onDrawerToggled()
                                        onRestartProgressConfirmed()
                                    }) {
                                        Text("Restart")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = onRestartDialogToggled) {
                                        Text("Cancel")
                                    }
                                }
                            )
                        }
                    }

                    is HomeUiState.Loading -> {
                        LaunchedEffect(drawerState.isOpen) {
                            if (drawerState.isOpen) drawerState.close()
                        }

                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    is HomeUiState.Error -> {
                        Text(
                            text = "Error",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeListItem(
    text: String,
    modifier: Modifier = Modifier,
    isUnlocked: Boolean = true,
    onClick: () -> Unit = {}
) {
    TextButton(
        modifier = modifier.padding(horizontal = 4.dp),
        onClick = onClick
    ) {
        Row(
            Modifier.height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, Modifier.weight(1f))

            Icon(
                painter = when(isUnlocked) {
                    true -> { painterResource(R.drawable.keyboard_arrow_right_24px) }
                    false -> { painterResource(R.drawable.lock_24px) }
                },
                contentDescription = "Choose this category"
            )
        }
    }
}

@Preview(name = "Home Screen: Success", showBackground = true)
@Composable
fun HomeScreenSuccessPreview() {
    MindlesslyHiraganaTheme {
        HomeScreenContent(
            uiState = HomeUiState.Success(
                highestCategoryId = "0",
                isDrawerOpen = false,
                isRestartDialogShown = false
            )
        )
    }
}

@Preview(name = "Home Screen: Success: Drawer", showBackground = true)
@Composable
fun HomeScreenSuccessDrawerPreview() {
    MindlesslyHiraganaTheme {
        HomeScreenContent(
            uiState = HomeUiState.Success(
                highestCategoryId = "0",
                isDrawerOpen = true,
                isRestartDialogShown = false
            )
        )
    }
}

@Preview(name = "Home Screen: Success: Dialog", showBackground = true)
@Composable
fun HomeScreenSuccessDialogPreview() {
    MindlesslyHiraganaTheme {
        HomeScreenContent(
            uiState = HomeUiState.Success(
                highestCategoryId = "0",
                isDrawerOpen = true,
                isRestartDialogShown = true
            )
        )
    }
}

@Preview(name = "Home Screen: Loading", showBackground = true)
@Composable
fun HomeScreenLoadingPreview() {
    MindlesslyHiraganaTheme {
        HomeScreenContent(uiState = HomeUiState.Loading)
    }
}

@Preview(name = "Home Screen: Error", showBackground = true)
@Composable
fun HomeScreenErrorPreview() {
    MindlesslyHiraganaTheme {
        HomeScreenContent(uiState = HomeUiState.Error(Exception("Error")))
    }
}