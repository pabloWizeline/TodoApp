package com.pfmiranda.todoexample.data

import com.pfmiranda.todoexample.Todo
import retrofit2.http.GET

interface TodoApiService {
    @GET("todos")
    suspend fun getTodos(): List<Todo>
}