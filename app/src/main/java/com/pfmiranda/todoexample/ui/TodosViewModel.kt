package com.pfmiranda.todoexample.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.pfmiranda.todoexample.domain.GetTodosUseCase
import com.pfmiranda.todoexample.domain.Todo

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase // Hilt inyectará el UseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Todo>>>(UiState.Loading) // Estado inicial
    val uiState: StateFlow<UiState<List<Todo>>> = _uiState.asStateFlow()

    init {
        loadTodos()
    }

    fun loadTodos() {
        _uiState.value = UiState.Loading // Emitir estado de carga al inicio de la función
        viewModelScope.launch {
            try {
                // Llama al UseCase. Asumimos que GetTodosUseCase devuelve kotlin.Result<List<Todo>>
                val resultFromUseCase = getTodosUseCase() // Esto es una suspend fun

                resultFromUseCase.fold(
                    onSuccess = { todosList ->
                        _uiState.value = UiState.Success(todosList)
                    },
                    onFailure = { exception ->
                        _uiState.value =
                            UiState.Error(exception.localizedMessage ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _uiState.value =
                    UiState.Error(e.localizedMessage ?: "An unexpected error occurred in ViewModel")
            }
        }
    }
}