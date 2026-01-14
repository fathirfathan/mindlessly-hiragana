package com.effatheresoft.mindlesslyhiragana.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultScaffold(
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