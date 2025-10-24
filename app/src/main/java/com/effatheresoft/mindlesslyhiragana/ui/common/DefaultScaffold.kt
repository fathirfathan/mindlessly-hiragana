package com.effatheresoft.mindlesslyhiragana.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

@Composable
fun HomeDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    onRestartProgressClicked: () -> Unit = {},
    content: @Composable ((DrawerState) -> Unit)
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerState = drawerState
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(text = "Mindlessly Hiragana", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                    HorizontalDivider()

                    Text(text = "Progress", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Restart Progress") },
                        selected = false,
                        onClick = onRestartProgressClicked
                    )
                }
            }
        }
    ) {
        content(drawerState)
    }
}

@Composable
fun DefaultScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    content: @Composable (() -> Unit)
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "Mindlessly Hiragana",
    onNavigationIconClicked: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClicked) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open menu"
                )
            }
        }
    )
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