package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigateToDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    viewModel: HomeViewModel = viewModel(factory = DefaultViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    HomeDrawer(
        drawerState = drawerState,
        onRestartProgressConfirmed = {
            coroutineScope.launch { drawerState.close() }
            viewModel.restartProgress()
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                HomeTopAppBar(
                    onNavigationIconClicked = { coroutineScope.launch { drawerState.open() } }
                )
            }
        ) { innerPadding ->
            HomeScreenContent(
                uiState = uiState,
                onCategorySelected = onNavigateToDetails,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when(uiState) {
            is HomeUiState.Success -> {
                LazyColumn(Modifier.padding(horizontal = 16.dp)) {
                    items(items = Hiragana.categories) { item ->
                        if (item.id.toInt() <= uiState.highestCategoryId.toInt()) {
                            HomeListItem(
                                text = item.hiraganaList.joinToString(" ") { it.hiragana },
                                isUnlocked = true,
                                onClick = { onCategorySelected(item.id) }
                            )
                        }
                    }
                    item {
                        HomeListItem(
                            text = "Test All Learned",
                            onClick = { onCategorySelected("12") }
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
            }
            is HomeUiState.Loading -> {
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
            modifier = Modifier.height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, Modifier.weight(1f))

            Icon(
                painter = when(isUnlocked) {
                    true -> { painterResource(R.drawable.keyboard_arrow_right_24px) }
                    false -> { painterResource(R.drawable.lock_24px) }
                },
                contentDescription = when(isUnlocked) {
                    true -> { "Category is unlocked" }
                    false -> { "Category is locked" }
                },
            )
        }
    }
}

@Composable
fun HomeDrawer(
    drawerState: DrawerState,
    onRestartProgressConfirmed: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerSheet(
                drawerState = drawerState,
                onRestartProgressConfirmed = onRestartProgressConfirmed
            )
        }
    ) {
        content()
    }
}

@Composable
fun HomeDrawerSheet(
    drawerState: DrawerState,
    onRestartProgressConfirmed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRestartProgressDialogShown by rememberSaveable { mutableStateOf(false) }

    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Mindlessly Hiragana", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider()

            Text("Progress", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
            NavigationDrawerItem(
                label = { Text("Restart Progress") },
                selected = false,
                onClick = { isRestartProgressDialogShown = true }
            )
        }
    }

    if (isRestartProgressDialogShown) {
        RestartProgressDialog(
            onDismissDialog = { isRestartProgressDialogShown = false },
            onConfirmDialog = onRestartProgressConfirmed
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(onNavigationIconClicked: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text("Mindlessly Hiragana") },
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open drawer"
                )
            }
        }
    )
}

@Composable
fun RestartProgressDialog(
    onDismissDialog: () -> Unit,
    onConfirmDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissDialog()
                    onConfirmDialog()
                },
                content = { Text("Restart") }
            )
        },
        dismissButton = { TextButton(onClick = onDismissDialog) { Text("Cancel") } },
        text = { Text("Are you sure you want to restart your progress?") },
        title = { Text("Restart Progress") }
    )
}

@Preview(name = "Home Screen:Success", showBackground = true)
@Composable
fun HomeScreenSuccessPreview() {
    MindlesslyHiraganaTheme {
        Column {
            HomeTopAppBar(onNavigationIconClicked = {})
            HomeScreenContent(
                uiState = HomeUiState.Success("0"),
                onCategorySelected = {}
            )
        }
    }
}

@Preview(name = "Home Screen:Drawer", showBackground = true)
@Composable
fun HomeScreenDrawerPreview() {
    MindlesslyHiraganaTheme {
        HomeDrawer(
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            onRestartProgressConfirmed = {}
        ) {}
    }
}

@Preview(name = "Home Screen:Drawer:Restart Progress Dialog", showBackground = true)
@Composable
fun HomeScreenDialogPreview() {
    MindlesslyHiraganaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            RestartProgressDialog(
                onDismissDialog = {},
                onConfirmDialog = {}
            )
        }
    }
}

@Preview(name = "Home Screen:Loading", showBackground = true)
@Composable
fun HomeScreenLoadingPreview() {
    MindlesslyHiraganaTheme {
        Column {
            HomeTopAppBar(onNavigationIconClicked = {})
            HomeScreenContent(
                uiState = HomeUiState.Loading,
                onCategorySelected = {}
            )
        }
    }
}

@Preview(name = "Home Screen:Error", showBackground = true)
@Composable
fun HomeScreenErrorPreview() {
    MindlesslyHiraganaTheme {
        Column {
            HomeTopAppBar(onNavigationIconClicked = {})
            HomeScreenContent(
                uiState = HomeUiState.Error(Exception("Error")),
                onCategorySelected = {}
            )
        }
    }
}