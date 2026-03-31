package com.example.hw2.book.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hw2.R
import com.example.hw2.book.domain.Book
import com.example.hw2.book.presentation.book_list.components.BookList
import com.example.hw2.core.presentation.DarkBlue
import com.example.hw2.core.presentation.DesertWhite
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesScreenRoot(
    viewModel: FavoritesViewModel = koinViewModel(),
    onBookClick: (Book) -> Unit,
) {
    val favoriteBooks by viewModel.favoriteBooks.collectAsStateWithLifecycle()

    FavoritesScreen(
        favoriteBooks = favoriteBooks,
        onBookClick = onBookClick
    )
}

@Composable
fun FavoritesScreen(
    favoriteBooks: List<Book>,
    onBookClick: (Book) -> Unit,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.favorites),
            style = MaterialTheme.typography.headlineMedium,
            color = DesertWhite,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            )
        ) {
            if (favoriteBooks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_favorite_books),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            } else {
                BookList(
                    books = favoriteBooks,
                    onBookClick = onBookClick,
                    modifier = Modifier.fillMaxSize(),
                    scrollState = listState
                )
            }
        }
    }
}
