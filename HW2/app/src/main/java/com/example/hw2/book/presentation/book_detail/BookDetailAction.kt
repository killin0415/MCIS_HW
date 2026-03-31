package com.example.hw2.book.presentation.book_detail

import com.example.hw2.book.domain.Book

sealed interface BookDetailAction {
    data object OnBackClick: BookDetailAction
    data object OnFavoriteClick: BookDetailAction
    data class OnSelectedBookChange(val book: Book): BookDetailAction
    data object OnTtsClick: BookDetailAction
}