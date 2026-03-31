@file:OptIn(FlowPreview::class)

package com.example.hw2.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw2.book.domain.Book
import com.example.hw2.book.domain.BookRepository
import com.example.hw2.core.data.AudioRecorderManager
import com.example.hw2.core.data.SttClient
import com.example.hw2.core.domain.onError
import com.example.hw2.core.domain.onSuccess
import com.example.hw2.core.presentation.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(
    private val bookRepository: BookRepository,
    private val audioRecorderManager: AudioRecorderManager,
    private val sttClient: SttClient
) : ViewModel() {

    private var cachedBooks = emptyList<Book>()
    private var searchJob: Job? = null

    private val _state = MutableStateFlow(BookListState())
    val state = _state
        .onStart {
            if(cachedBooks.isEmpty()) {
                observeSearchQuery()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: BookListAction) {
        when (action) {
            is BookListAction.OnBookClick -> {

            }

            is BookListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }

            is BookListAction.OnToggleRecording -> {
                toggleRecording()
            }
        }
    }

    private fun toggleRecording() {
        if (_state.value.isRecording) {
            // Stop recording and send to STT
            val audioPath = audioRecorderManager.stopRecording()
            _state.update { it.copy(isRecording = false) }

            if (audioPath != null) {
                viewModelScope.launch {
                    val result = sttClient.recognize(audioPath)
                    if (result != null) {
                        _state.update {
                            it.copy(searchQuery = result)
                        }
                    }
                }
            }
        } else {
            // Start recording
            try {
                audioRecorderManager.startRecording()
                _state.update { it.copy(isRecording = true) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResults = cachedBooks
                            )
                        }
                    }

                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        bookRepository
            .searchBooks(query)
            .onSuccess { searchResults ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        searchResults = searchResults
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        searchResults = emptyList(),
                        isLoading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }
    }

}