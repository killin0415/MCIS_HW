package com.example.hw2.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val icon: ImageVector,
    val title: String,
)

val TOP_LEVEL_DESTINATIONS = mapOf(
    Route.Main to BottomNavItem(
        icon = Icons.Outlined.Checklist,
        title = "Todos"
    ),

)