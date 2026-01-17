package com.effatheresoft.mindlesslyhiragana.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    object Home: Route

    @Serializable
    data class Learn(val categoryId: String): Route

    @Serializable
    data class Quiz(val categoryId: String): Route

    @Serializable
    data class Result(val categoryId: String): Route

    @Serializable
    object Test: Route

    @Serializable
    object TestQuiz: Route

    @Serializable
    object TestResult: Route
}