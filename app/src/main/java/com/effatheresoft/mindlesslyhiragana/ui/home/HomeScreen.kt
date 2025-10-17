package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.DefaultViewModelProvider
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = DefaultViewModelProvider.Factory),
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToDetails: (String) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    HomeScreenContent(
        modifier = modifier,
        uiState = uiState,
        onNavigationIconClicked = onNavigationIconClicked,
        onNavigateToDetails = onNavigateToDetails
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState.Loading,
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToDetails: (String) -> Unit = {}
) {
    DefaultScaffold(
        appBarTitle = "Mindlessly Hiragana",
        onNavigationIconClicked = onNavigationIconClicked
    ) { innerPadding ->
        Column(modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)) {
            when(uiState) {
                is HomeUiState.Loading -> {
                    Text(
                        text = "Loading",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                is HomeUiState.Success -> {
                    LazyColumn {
                        items(items = Hiragana.categories) { item ->
                            if (item.id.toInt() <= uiState.highestCategoryId.toInt()) {
                                HomeListItem(
                                    text = item.hiraganaList.joinToString(" ") { it.hiragana },
                                    isUnlocked = true,
                                    onClick = { onNavigateToDetails(item.id) }
                                )
                            }
                        }
                        item { HomeListItem(text = "Test All Learned") }
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
        val hiraganaCategories = listOf(
            HiraganaCategory("0", listOf(Hiragana.HI, Hiragana.MI, Hiragana.KA, Hiragana.SE)),
            HiraganaCategory("1", listOf(Hiragana.FU, Hiragana.WO, Hiragana.YA)),
            HiraganaCategory("2", listOf(Hiragana.A, Hiragana.O, Hiragana.E, Hiragana.N)),
            HiraganaCategory("3", listOf(Hiragana.U, Hiragana.TSU, Hiragana.KU, Hiragana.HE)),
            HiraganaCategory("4", listOf(Hiragana.KE, Hiragana.RI, Hiragana.KO, Hiragana.NI)),
            HiraganaCategory("5", listOf(Hiragana.SU, Hiragana.MU, Hiragana.NA, Hiragana.TA, Hiragana.RU, Hiragana.RO)),
            HiraganaCategory("6", listOf(Hiragana.SHI, Hiragana.I, Hiragana.MO)),
            HiraganaCategory("7", listOf(Hiragana.TO, Hiragana.TE, Hiragana.SO)),
            HiraganaCategory("8", listOf(Hiragana.NE, Hiragana.WA, Hiragana.RE)),
            HiraganaCategory("9", listOf(Hiragana.NU, Hiragana.ME, Hiragana.NO, Hiragana.YU)),
            HiraganaCategory("10", listOf(Hiragana.HA, Hiragana.HO, Hiragana.YO, Hiragana.MA)),
            HiraganaCategory("11", listOf(Hiragana.KI, Hiragana.SA, Hiragana.CHI, Hiragana.RA))
        )
        HomeScreenContent(
            uiState = HomeUiState.Success("0")
        )
    }
}

@Preview(name = "Home Screen: Success Empty", showBackground = true)
@Composable
fun HomeScreenSuccessEmptyPreview() {
    MindlesslyHiraganaTheme {
        HomeScreenContent(
            uiState = HomeUiState.Success("0")
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