package com.example.hw2.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.hw2.book.presentation.SelectedBookViewModel
import com.example.hw2.book.presentation.book_detail.BookDetailAction
import com.example.hw2.book.presentation.book_detail.BookDetailScreenRoot
import com.example.hw2.book.presentation.book_detail.BookDetailViewModel
import com.example.hw2.book.presentation.book_list.BookListScreenRoot
import com.example.hw2.book.presentation.favorites.FavoritesScreenRoot
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val navigationState = rememberNavigationState(
        startRoute = Route.BookList as NavKey,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys.toSet() as Set<NavKey>
    )
    val navigator = remember {
        Navigator(navigationState)
    }

    // Shared SelectedBookViewModel at the activity scope (outside NavDisplay entries)
    val selectedBookViewModel = koinViewModel<SelectedBookViewModel>()

    Scaffold(
        bottomBar = {
            NavigationBar {
                TOP_LEVEL_DESTINATIONS.forEach { (route, item) ->
                    NavigationBarItem(
                        selected = navigationState.topLevelRoute == route,
                        onClick = { navigator.navigate(route) },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = modifier.padding(innerPadding),
            onBack = navigator::goBack,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            popTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            predictivePopTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.BookList> {
                        BookListScreenRoot(
                            onBookClick = { book ->
                                selectedBookViewModel.onSelectBook(book)
                                navigator.navigate(Route.BookDetail(book.id))
                            }
                        )
                    }
                    entry<Route.Favorites> {
                        FavoritesScreenRoot(
                            onBookClick = { book ->
                                selectedBookViewModel.onSelectBook(book)
                                navigator.navigate(Route.BookDetail(book.id))
                            }
                        )
                    }
                    entry<Route.BookDetail> { route ->
                        val viewModel = koinViewModel<BookDetailViewModel> { parametersOf(route.id) }
                        val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()

                        LaunchedEffect(selectedBook) {
                            selectedBook?.let { book ->
                                viewModel.onAction(BookDetailAction.OnSelectedBookChange(book))
                            }
                        }

                        BookDetailScreenRoot(
                            viewModel = viewModel,
                            onBackClick = { navigator.goBack() }
                        )
                    }
                }
            )
        )
    }
}