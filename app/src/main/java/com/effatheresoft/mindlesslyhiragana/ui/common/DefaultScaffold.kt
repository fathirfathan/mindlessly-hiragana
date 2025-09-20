package com.effatheresoft.mindlesslyhiragana.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultScaffold(
    modifier: Modifier = Modifier,
    appBarTitle: String,
    onNavigationIconClicked: () -> Unit,
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