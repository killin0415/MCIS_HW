package com.example.hw1.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hw1.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow



class MainViewModel : ViewModel() {
    private val _image = MutableStateFlow(R.drawable.vivian)
    val image = _image.asStateFlow()
}