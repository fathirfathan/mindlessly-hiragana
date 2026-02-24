package com.kaishijak.mindlesslyhiragana.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultScaffold(
    topAppBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
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