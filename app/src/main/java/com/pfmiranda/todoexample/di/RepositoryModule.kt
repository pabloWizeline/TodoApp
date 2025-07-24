package com.pfmiranda.todoexample.di

import com.pfmiranda.todoexample.data.TodoRepositoryImpl
import com.pfmiranda.todoexample.domain.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Generalmente los repositorios son singletons a nivel de aplicación
abstract class RepositoryModule {

    @Binds
    @Singleton // Asegura que Hilt provea la misma instancia del repositorio
    abstract fun bindTodoRepository(
        todoRepositoryImpl: TodoRepositoryImpl // Hilt sabe crear TodoRepositoryImpl por su @Inject constructor
    ): TodoRepository
}