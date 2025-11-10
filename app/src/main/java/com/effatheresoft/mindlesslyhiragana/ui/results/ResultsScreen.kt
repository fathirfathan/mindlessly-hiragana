package com.effatheresoft.mindlesslyhiragana.ui.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.QuizResult
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.getCorrectAnswersCount
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.getIncorrectAnswersCount

@Composable
fun ResultsScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit = {},
    viewModel: ResultsViewModel = viewModel(),
    quizResults: List<QuizResult>
) {
    DefaultScaffold(
        onNavigationIconClicked = onNavigationIconClicked
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Correct answers: ${quizResults.getCorrectAnswersCount()}")
            Text("Incorrect answers: ${quizResults.getIncorrectAnswersCount()}")
        }
    }
}