package com.effatheresoft.mindlesslyhiragana.ui.testquiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana

@Composable
fun HiraganaKeyboard(
    onButtonClick: (Hiragana) -> Unit,
    selectedAnswers: Set<Hiragana>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyGridState()
    LazyHorizontalGrid(
        state = listState,
        rows = GridCells.Fixed(5),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {

        Hiragana.entries.forEach {
            when (it) {
                Hiragana.YU -> {
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                    item(it) {
                        HiraganaKeyboardButton(
                            hiragana = it,
                            isEnabled = !selectedAnswers.contains(it),
                            onButtonClick = { onButtonClick(it) }
                        )
                    }
                    item { Spacer(modifier = Modifier.width(72.dp)) }
                }

                Hiragana.WA -> {
                    item(it) {
                        HiraganaKeyboardButton(
                            hiragana = it,
                            isEnabled = !selectedAnswers.contains(it),
                            onButtonClick = { onButtonClick(it) }
                        )
                    }
                    repeat(3) { item { Spacer(modifier = Modifier.width(72.dp)) } }
                }

                Hiragana.N -> {
                    item(it) {
                        HiraganaKeyboardButton(
                            hiragana = it,
                            isEnabled = !selectedAnswers.contains(it),
                            onButtonClick = { onButtonClick(it) }
                        )
                    }
                    repeat(4) { item { Spacer(modifier = Modifier.width(72.dp)) } }
                }

                else -> item(it) {
                    HiraganaKeyboardButton(
                        hiragana = it,
                        isEnabled = !selectedAnswers.contains(it),
                        onButtonClick = { onButtonClick(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun HiraganaKeyboardButton(
    hiragana: Hiragana,
    isEnabled: Boolean,
    onButtonClick: () -> Unit
) {
    Button(
        onClick = onButtonClick,
        enabled = isEnabled,
        modifier = Modifier.width(76.dp)
    ) {
        Text(hiragana.name)
    }
}