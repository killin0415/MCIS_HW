package com.example.hw1.di


import com.example.hw1.presentation.viewmodels.MainViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


val sharedModule = module {
    viewModelOf(::MainViewModel)

}