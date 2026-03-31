package com.example.hw2.core.presentation

import androidx.compose.runtime.Composable
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.example.hw2.R


sealed interface UiText {
    data class DynamicString(val value: String): UiText
    class IntId(
        val id: Int,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is IntId -> stringResource(id, *args)
        }
    }
}