package com.effatheresoft.mindlesslyhiragana.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route() {
    @Serializable
    data object Home: Route()

    @Serializable
    data class Details(val id: String): Route()
}