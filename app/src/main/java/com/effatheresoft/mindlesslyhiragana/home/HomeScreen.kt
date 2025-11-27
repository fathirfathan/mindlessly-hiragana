package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mindlessly Hiragana") }
            )
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val currentProgressCategoryIndex =
            remember(uiState) { hiraganaCategories.indexOfFirst { it.id == uiState.progress } }
        val unlockedCategory =
            remember(uiState) { hiraganaCategories.take(currentProgressCategoryIndex + 1) }
        val lockedCategory =
            remember(uiState) { hiraganaCategories.drop(currentProgressCategoryIndex + 1) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
        ) {
            for (category in unlockedCategory) {
                CategoryItem(
                    title = category.toHiraganaStringWithNakaguro(),
                    isLocked = false
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            CategoryItem(
                title = "Test All Learned",
                isLocked = false
            )
            Spacer(modifier = Modifier.height(8.dp))

            for (category in lockedCategory) {
                CategoryItem(
                    title = category.toHiraganaStringWithNakaguro(),
                    isLocked = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

val hiraganaCategories = listOf(
    HiraganaCategory("himikase",  listOf(Hiragana.HI,  Hiragana.MI, Hiragana.KA,  Hiragana.SE)),
    HiraganaCategory("fuwoya",    listOf(Hiragana.FU,  Hiragana.WO, Hiragana.YA)),
    HiraganaCategory("ao",        listOf(Hiragana.A,   Hiragana.O)),
    HiraganaCategory("tsuune",    listOf(Hiragana.TSU, Hiragana.U,  Hiragana.N,   Hiragana.E)),
    HiraganaCategory("kuherike",  listOf(Hiragana.KU,  Hiragana.HE, Hiragana.RI,  Hiragana.KE)),
    HiraganaCategory("konitana",  listOf(Hiragana.KO,  Hiragana.NI, Hiragana.TA,  Hiragana.NA)),
    HiraganaCategory("sumuroru",  listOf(Hiragana.SU,  Hiragana.MU, Hiragana.RO,  Hiragana.RU)),
    HiraganaCategory("shiimo",    listOf(Hiragana.SHI, Hiragana.I,  Hiragana.MO)),
    HiraganaCategory("toteso",    listOf(Hiragana.TO,  Hiragana.TE, Hiragana.SO)),
    HiraganaCategory("wanere",    listOf(Hiragana.WA,  Hiragana.NE, Hiragana.RE)),
    HiraganaCategory("noyumenu",  listOf(Hiragana.NO,  Hiragana.YU, Hiragana.ME,  Hiragana.NU)),
    HiraganaCategory("yohamaho",  listOf(Hiragana.YO,  Hiragana.HA, Hiragana.MA,  Hiragana.HO)),
    HiraganaCategory("sakichira", listOf(Hiragana.SA,  Hiragana.KI, Hiragana.CHI, Hiragana.RA)),
)

@Composable
fun CategoryItem(
    title: String,
    isLocked: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title)
        Spacer(modifier = Modifier.weight(1f))
        when(isLocked) {
            true -> Icon(
                painter = painterResource(R.drawable.lock_24px),
                contentDescription = "$title category locked"
            )
            false -> Icon(
                painter = painterResource(R.drawable.arrow_right_24px),
                contentDescription = "$title category unlocked"
            )

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MindlesslyHiraganaTheme {
        Surface {
            HomeScreen()
        }
    }
}