package com.example.hw2.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object BookList : Route, NavKey

    @Serializable
    data object Favorites : Route, NavKey

    @Serializable
    data class BookDetail(val id: String) : Route, NavKey
}