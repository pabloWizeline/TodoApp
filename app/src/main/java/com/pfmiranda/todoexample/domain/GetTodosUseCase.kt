package com.pfmiranda.todoexample.domain

import javax.inject.Inject

class GetTodosUseCase @Inject constructor(private val todoRepository: TodoRepository){
    suspend operator fun invoke() : Result<List<Todo>> {
        val resultFromRepository = todoRepository.getTodos()

        return resultFromRepository.map { todosList -> // Usamos el .map de kotlin.Result
            // Si es Success, transforma la lista
            todosList.map { todo ->
                todo.copy(title = todo.title.uppercase()) // Transforma el título a mayúsculas
            }
        }
    }
}