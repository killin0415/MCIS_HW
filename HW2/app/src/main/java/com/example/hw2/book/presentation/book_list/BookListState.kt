package com.example.hw2.book.presentation.book_list

import com.example.hw2.book.domain.Book
import com.example.hw2.core.presentation.UiText

data class BookListState(
    val searchQuery: String = "potato",
    val searchResults: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val isRecording: Boolean = false
)