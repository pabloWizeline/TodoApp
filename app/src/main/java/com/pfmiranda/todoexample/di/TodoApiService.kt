package com.pfmiranda.todoexample.di

import com.pfmiranda.todoexample.Todo
import retrofit2.http.GET

interface TodoApiService {
    @GET("todos")
    suspend fun getTodos(): List<Todo>
}