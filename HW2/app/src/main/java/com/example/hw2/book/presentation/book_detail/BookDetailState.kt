package com.example.hw2.book.presentation.book_detail

import com.example.hw2.book.domain.Book

data class BookDetailState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null,
    val isSpeaking: Boolean = false
)
