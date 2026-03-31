package com.example.hw2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val icon: ImageVector,
    val title: String,
)

val TOP_LEVEL_DESTINATIONS = mapOf(
    Route.BookList to BottomNavItem(
        icon = Icons.Outlined.Search,
        title = "Search"
    ),
    Route.Favorites to BottomNavItem(
        icon = Icons.Outlined.FavoriteBorder,
        title = "Favorites"
    ),
)