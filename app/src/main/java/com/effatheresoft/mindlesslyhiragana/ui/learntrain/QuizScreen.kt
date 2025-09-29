package com.effatheresoft.mindlesslyhiragana.ui.learntrain

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.effatheresoft.mindlesslyhiragana.ui.common.DefaultScaffold

@Composable
fun QuizScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel(),
    onNavigationIconClicked: () -> Unit = {},
    onNavigateToResults: (quizResults: List<QuizResult>) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.initializeWithId(id)
    }
    val uiState = viewModel.uiState

    DefaultScaffold(
        appBarTitle = uiState.appBarTitle,
        onNavigationIconClicked = onNavigationIconClicked
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp, bottom = 48.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                uiState.currentQuestion,
                fontSize = 57.sp
            )
            Spacer(Modifier.weight(1f))
            Text(
                "Remaining: ${uiState.remainingQuestionsCount}",
                Modifier.padding(bottom = 16.dp)
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (answer in uiState.possibleAnswers) {
                    Button({ viewModel.onAnswerSelected(
                        selectedAnswer = answer,
                        onNavigateToResults = onNavigateToResults
                    ) }) {
                        Text(answer.romaji.uppercase())
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LearnTrainScreenPreview() {
    QuizScreen(
        id = "0"
    )
}

