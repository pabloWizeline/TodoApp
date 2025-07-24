package com.pfmiranda.todoexample.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pfmiranda.todoexample.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Inyección del ViewModel usando Hilt y Activity KTX
    private val viewModel: TodosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppTheme {
                val uiState by viewModel.uiState.collectAsState()

                TodoScreen(
                    uiState = uiState,
                    onLoadTodos = { viewModel.loadTodos() },
                    onRefresh = { viewModel.loadTodos() }
                )
            }
        }
    }

}

