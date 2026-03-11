package com.example.hw1.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hw1.R
import com.example.hw1.presentation.viewmodels.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {

    val image by viewModel.image.collectAsStateWithLifecycle()
    Scaffold { innerPadding ->
        Column (
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Hello World!",
                modifier = modifier
                    .padding(innerPadding)
                    .padding(10.dp)
            )
            Box(
                modifier = Modifier.height(256.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val transition by animateFloatAsState(
                    targetValue = 1f
                )
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

