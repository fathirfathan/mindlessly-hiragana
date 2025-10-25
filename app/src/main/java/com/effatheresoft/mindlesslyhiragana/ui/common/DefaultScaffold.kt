package com.effatheresoft.mindlesslyhiragana.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreenContent
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeUiState
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultScaffold(
    modifier: Modifier = Modifier,
    appBarTitle: String = "",
    onNavigationIconClicked: () -> Unit = {},
    content: @Composable ( (PaddingValues) -> Unit)
) {
    val isAtHome = appBarTitle == "Mindlessly Hiragana"

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(appBarTitle) },
                navigationIcon = {
                    DefaultNavigationIcon(
                        isAtHome,
                        onNavigationIconClicked
                    )
                }
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffoldWithDrawer(
    modifier: Modifier = Modifier,
    appBarTitle: String = "",
    onRestartProgressConfirmed: () -> Unit = {},
    content: @Composable ( () -> Unit)
) {
    var isRestartConfirmationDialogShown by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerState = drawerState
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("Mindlessly Hiragana", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                    HorizontalDivider()

                    Text("Progress", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Restart Progress") },
                        selected = false,
                        onClick = { isRestartConfirmationDialogShown = true }
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(appBarTitle) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply { if (isClosed) open() else close() }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Open menu"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content()
                if (isRestartConfirmationDialogShown) {
                    AlertDialog(
                        icon = {},
                        title = { Text("Restart Progress") },
                        text = { Text("Are you sure you want to restart your progress?") },
                        onDismissRequest = {
                            isRestartConfirmationDialogShown = false
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                isRestartConfirmationDialogShown = false
                                scope.launch {
                                    drawerState.apply { if (isClosed) open() else close() }
                                }
                                onRestartProgressConfirmed()
                            }) {
                                Text("Restart")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                isRestartConfirmationDialogShown = false
                            }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }


}

@Composable
fun DefaultNavigationIcon(
    isAtHome: Boolean = false,
    onNavigationIconClicked: () -> Unit = {}
) {
    IconButton(onClick = {
        onNavigationIconClicked()
    }) {
        if (isAtHome) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Open menu"
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back"
            )

        }
    }
}

@Preview(name = "Home Screen: Success", showBackground = true)
@Composable
fun HomeScreenSuccessPreview() {
    MindlesslyHiraganaTheme {
        HomeScreenContent(
            uiState = HomeUiState.Success("0")
        )
    }
}