package com.example.hw2.book.presentation.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw2.book.domain.BookRepository
import com.example.hw2.core.data.AudioPlayerManager
import com.example.hw2.core.data.TtsClient
import com.example.hw2.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookRepository: BookRepository,
    private val bookId: String,
    private val ttsClient: TtsClient,
    private val audioPlayerManager: AudioPlayerManager
): ViewModel() {

    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .onStart {
            fetchBookDescription()
            observeFavoriteStatus()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: BookDetailAction) {
        when(action) {
            is BookDetailAction.OnSelectedBookChange -> {
                _state.update { it.copy(
                    book = action.book
                ) }
            }
            is BookDetailAction.OnFavoriteClick -> {
                viewModelScope.launch {
                    if(state.value.isFavorite) {
                        bookRepository.deleteFromFavorites(bookId)
                    } else {
                        state.value.book?.let { book ->
                            bookRepository.markAsFavorite(book)
                        }
                    }
                }
            }
            is BookDetailAction.OnTtsClick -> {
                handleTts()
            }
            else -> Unit
        }
    }

    private fun handleTts() {
        if (_state.value.isSpeaking) {
            audioPlayerManager.stop()
            _state.update { it.copy(isSpeaking = false) }
            return
        }

        val book = _state.value.book ?: return
        var textToSpeak = book.description?.takeIf { it.isNotBlank() } ?: book.title
        if (textToSpeak.length > 150) {
            textToSpeak = textToSpeak.take(150) + "..."
        }

        viewModelScope.launch {
            _state.update { it.copy(isSpeaking = true) }
            try {
                val audioPath = ttsClient.synthesize(textToSpeak)
                if (audioPath != null) {
                    audioPlayerManager.play(audioPath)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _state.update { it.copy(isSpeaking = false) }
            }
        }
    }

    private fun observeFavoriteStatus() {
        bookRepository
            .isBookFavorite(bookId)
            .onEach { isFavorite ->
                _state.update { it.copy(
                    isFavorite = isFavorite
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchBookDescription() {
        viewModelScope.launch {
            bookRepository
                .getBookDescription(bookId)
                .onSuccess { description ->
                    _state.update { it.copy(
                        book = it.book?.copy(
                            description = description
                        ),
                        isLoading = false
                    ) }
                }
        }
    }
}