package com.pfmiranda.todoexample.data

import com.pfmiranda.todoexample.domain.Todo
import com.pfmiranda.todoexample.domain.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException // Para errores de red
import javax.inject.Inject


class TodoRepositoryImpl @Inject constructor(
    private val apiService: TodoApiService
) : TodoRepository {
    override suspend fun getTodos(): Result<List<Todo>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(apiService.getTodos().toDomainModel())
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}