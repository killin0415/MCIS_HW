package com.example.hw1.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun LikesButton(icon: ImageVector, text: String) {
    Button(
        onClick = {},
        colors = ButtonColors(
            containerColor = Color(0xFFD0BCFF).copy(0.8f),
            contentColor = Color(0xFF7D5260),
            disabledContainerColor = Color(0xFF6650a4).copy(0.8f),
            disabledContentColor = Color(0xFFEFB8C8)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = "",
                tint = Color(0xFF7D5260)
            )
            Text(text)
        }

    }
}