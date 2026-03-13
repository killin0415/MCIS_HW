package com.example.hw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.hw1.core.ui.theme.HW1Theme
import com.example.hw1.navigation.NavigationRoot
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HW1Theme {
                NavigationRoot()
            }
        }
    }
}
