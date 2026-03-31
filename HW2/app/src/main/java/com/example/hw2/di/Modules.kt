package com.example.hw2.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.hw2.book.data.database.DatabaseFactory
import com.example.hw2.book.data.database.FavoriteBookDatabase
import com.example.hw2.book.data.network.KtorRemoteBookDataSource
import com.example.hw2.book.data.network.RemoteBookDataSource
import com.example.hw2.book.data.repository.DefaultBookRepository
import com.example.hw2.book.domain.BookRepository
import com.example.hw2.book.presentation.SelectedBookViewModel
import com.example.hw2.book.presentation.book_detail.BookDetailViewModel
import com.example.hw2.book.presentation.book_list.BookListViewModel
import com.example.hw2.book.presentation.favorites.FavoritesViewModel
import com.example.hw2.core.data.AudioPlayerManager
import com.example.hw2.core.data.AudioRecorderManager
import com.example.hw2.core.data.HttpClientFactory
import com.example.hw2.core.data.SttClient
import com.example.hw2.core.data.TtsClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()

    single { DatabaseFactory(androidApplication()) }
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDatabase>().favoriteBookDao }

    // Audio services
    single { AudioRecorderManager(androidApplication()) }
    single { SttClient(get()) }
    single { TtsClient() }
    single { AudioPlayerManager() }

    // ViewModels
    viewModel { BookListViewModel(get(), get(), get()) }
    viewModel { params -> BookDetailViewModel(get(), params.get(), get(), get()) }
    viewModelOf(::SelectedBookViewModel)
    viewModelOf(::FavoritesViewModel)
}