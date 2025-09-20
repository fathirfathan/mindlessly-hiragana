package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToDetails: (String) -> Unit = {}
) {
    DefaultScaffold(
        appBarTitle = "Mindlessly Hiragana",
        onNavigationIconClicked = onNavigationIconClicked
    ) { innerPadding ->
        val hiraganaCategories by viewModel.hiraganaCategories.collectAsStateWithLifecycle()
        Column(modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            LazyColumn {
                items(items = hiraganaCategories) { item ->
                    HomeListItem(
                        item.hiragana,
                        { onNavigateToDetails(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeListItem(
    text: String,
    onClick: () -> Unit = {}
) {
    TextButton(
        modifier = Modifier.padding(horizontal = 4.dp),
        onClick = onClick
    ) {
        Row(
            Modifier.height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = text
            )
        }
    }
}