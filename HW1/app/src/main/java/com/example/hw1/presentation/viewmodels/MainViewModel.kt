package com.example.hw1.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hw1.R
import com.example.hw1.domain.Profile
import com.example.hw1.utils.COPY_TEXT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.OnEditClick -> {
                _state.update {
                    it.copy(
                        profile = it.profile.copy(
                            content = action.content
                        )
                    )
                }
            }
        }
    }

}

data class MainState(
    val profile: Profile = Profile(
        R.drawable.vivian,
        content = COPY_TEXT,
        null
    )
)

sealed interface MainAction {
    data class OnEditClick(val content: String) : MainAction
}