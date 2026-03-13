package com.example.hw1.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditProfileViewModel(
    content: String
) : ViewModel() {
    private val _state = MutableStateFlow(
        EditState(
            data = content
        )
    )
    val state = _state.asStateFlow()

    fun onAction(action: EditAction) {
        when (action) {
            is EditAction.ContentChanged -> {
                _state.value = _state.value.copy(data = action.content)
            }
        }
    }
}

sealed interface EditAction {
    data class ContentChanged(val content: String) : EditAction
}

data class EditState(
    val data: String = ""
)