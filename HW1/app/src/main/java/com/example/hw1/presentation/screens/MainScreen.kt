package com.example.hw1.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hw1.core.ui.theme.Pink40
import com.example.hw1.core.ui.theme.Purple80
import com.example.hw1.navigation.ResultStore
import com.example.hw1.presentation.viewmodels.MainAction
import com.example.hw1.presentation.viewmodels.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    resultStore: ResultStore,
    viewModel: MainViewModel = koinViewModel(),
    onEditClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val content = resultStore.getResult<String>("edit_content")
    content?.let {
        viewModel.onAction(action = MainAction.OnEditClick(content))
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Purple80.copy(0.8f),
                contentColor = Pink40,
                onClick = { onEditClick(state.profile.content) }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "",
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .height(256.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(state.profile.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            Text(
                text = state.profile.content,
                modifier = Modifier
            )
        }
    }
}

