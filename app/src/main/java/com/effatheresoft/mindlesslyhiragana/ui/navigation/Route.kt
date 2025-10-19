package com.effatheresoft.mindlesslyhiragana.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object Home: Route()

    @Serializable
    data class Details(val id: String): Route()

    @Serializable
    data class Quiz(
        val id: String,
        val learningSetsCount: Int
    ): Route()

    @Serializable
    data class Results(
        val id: String,
        val quizResultsJson: String
    ): Route()
}