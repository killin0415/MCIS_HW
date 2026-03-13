package com.example.hw1.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Main : Route, NavKey

    @Serializable
    data class Edit(val content: String) : Route, NavKey
}