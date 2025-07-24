package com.pfmiranda.todoexample.domain

interface TodoRepository {
    suspend fun getTodos(): Result<List<Todo>>
}