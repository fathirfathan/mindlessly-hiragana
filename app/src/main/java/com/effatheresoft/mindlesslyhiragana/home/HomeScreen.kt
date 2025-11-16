package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mindlessly Hiragana") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 32.dp)
        ) {
            val progress = "himikase"
            for (category in hiraganaCategories) {
                when (category.id) {
                    progress -> {
                        Text(category.toHiraganaStringWithNakaguro())
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Test All Learned")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    else -> {
                        Text(category.toHiraganaStringWithNakaguro())
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MindlesslyHiraganaTheme {
        Surface {
            HomeScreen()
        }
    }
}