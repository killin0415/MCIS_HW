package com.example.hw2.book.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw2.book.domain.Book
import com.example.hw2.book.domain.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(
    bookRepository: BookRepository
) : ViewModel() {

    val favoriteBooks: StateFlow<List<Book>> = bookRepository
        .getFavoriteBooks()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )
}
