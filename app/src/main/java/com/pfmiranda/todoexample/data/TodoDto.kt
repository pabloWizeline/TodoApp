package com.pfmiranda.todoexample.data

import com.pfmiranda.todoexample.domain.Todo

data class TodoDto(
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
)

fun TodoDto.toDomainModel(): Todo {
    return Todo(
        userId = this.userId,
        id = this.id,
        title = this.title,
        completed = this.completed
    )
}

fun List<TodoDto>.toDomainModel(): List<Todo> {
    return this.map { it.toDomainModel() }
}