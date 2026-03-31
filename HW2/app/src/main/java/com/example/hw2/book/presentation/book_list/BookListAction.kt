package com.example.hw2.book.presentation.book_list

import com.example.hw2.book.domain.Book

sealed interface BookListAction {
    data class OnSearchQueryChange(val query: String): BookListAction
    data class OnBookClick(val book: Book): BookListAction
    data object OnToggleRecording: BookListAction
}