package com.pfmiranda.todoexample.domain

data class Todo(
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
)