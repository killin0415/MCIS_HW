package com.example.hw1.di


import com.example.hw1.presentation.viewmodels.EditProfileViewModel
import com.example.hw1.presentation.viewmodels.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::EditProfileViewModel)
}