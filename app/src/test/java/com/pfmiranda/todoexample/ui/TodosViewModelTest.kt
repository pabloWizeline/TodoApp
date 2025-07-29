package com.pfmiranda.todoexample.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pfmiranda.todoexample.domain.GetTodosUseCase
import com.pfmiranda.todoexample.domain.Todo
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// Datos de prueba (puedes moverlos a un archivo/objeto separado si lo prefieres)
val testTodoItems = listOf(
 Todo(1, 101, "Sample Item 1", false),
 Todo(2, 101, "Sample Item 2", true),
 Todo(3, 102, "Sample Item 3", false)
)

val testException = Exception("Network error occurred")
val testUseCaseException = Exception("UseCase failed with Result.failure")
val testViewModelCatchException = Exception("An unexpected error occurred in ViewModel")



@ExperimentalCoroutinesApi
 class TodosViewModelTest {

 @get:Rule
 val instantTaskExecutorRule = InstantTaskExecutorRule()

 private val testDispatcher = StandardTestDispatcher()

 // Mock para el UseCase
 private lateinit var mockGetItemsUseCase: GetTodosUseCase

 // ViewModel a testear
 private lateinit var viewModel: TodosViewModel


@Before
 fun setUp() {

 MockKAnnotations.init(this)
 Dispatchers.setMain(testDispatcher)
 mockGetItemsUseCase = mockk()

 }

@After
 fun tearDown() {
 clearAllMocks()

 Dispatchers.resetMain()

 }

 // 1. Estado inicial
 @Test
 fun `initial state after init is Loading then Success (due to init call)`() = runTest(testDispatcher) {

  // Given
  coEvery { mockGetItemsUseCase() } coAnswers {
   delay(300) // Simula operación de red
   Result.success(testTodoItems)
  }

  // Crear ViewModel sin llamar init automáticamente
  viewModel = TodosViewModel(mockGetItemsUseCase)

  val collectedStates = mutableListOf<UiState<List<Todo>>>()
  val collectionJob = launch {
   viewModel.uiState.collect { state ->
    collectedStates.add(state)
   }
  }

  // When - llamar explícitamente
  viewModel.loadTodos()
  testDispatcher.scheduler.advanceUntilIdle()

  // Then
  assertTrue(collectedStates.size >= 2)
  assertTrue(collectedStates[0] is UiState.Loading )
  assertTrue(collectedStates[1] is UiState.Success )
  assertEquals(testTodoItems, (collectedStates[1] as UiState.Success).data)


  collectionJob.cancel()
 }

 // 1. Estado inicial Vacio
 @Test
 fun `initial Empty state after init is Loading then Success (due to init call)`() = runTest(testDispatcher) {

  // Given
  coEvery { mockGetItemsUseCase() } coAnswers {
   delay(300) // Simula operación de red
   Result.success(emptyList())
  }

  // Crear ViewModel sin llamar init automáticamente
  viewModel = TodosViewModel(mockGetItemsUseCase)

  val collectedStates = mutableListOf<UiState<List<Todo>>>()
  val collectionJob = launch {
   viewModel.uiState.collect { state ->
    collectedStates.add(state)
   }
  }

  // When - llamar explícitamente
  viewModel.loadTodos()
  testDispatcher.scheduler.advanceUntilIdle()

  // Then
  assertTrue(collectedStates.size >= 2)
  assertTrue(collectedStates[0] is UiState.Loading )
  assertTrue(collectedStates[1] is UiState.Success )
  assertEquals(emptyList<Todo>(), (collectedStates[1] as UiState.Success).data)


  collectionJob.cancel()
 }

 // 1. Estado Error
 @Test
 fun `loadItems use case throws exception - emits Loading then Error with ViewModel's catch message`() = runTest(testDispatcher) {

  // Given
  coEvery { mockGetItemsUseCase() } coAnswers {
   delay(300) // Simula operación de red
   Result.failure(testException)
  }

  // Crear ViewModel sin llamar init automáticamente
  viewModel = TodosViewModel(mockGetItemsUseCase)

  val collectedStates = mutableListOf<UiState<List<Todo>>>()
  val collectionJob = launch {
   viewModel.uiState.collect { state ->
    collectedStates.add(state)
   }
  }

  // When - llamar explícitamente
  viewModel.loadTodos()
  testDispatcher.scheduler.advanceUntilIdle()

  // Then
  assertTrue(collectedStates.size >= 2)
  assertTrue(collectedStates[0] is UiState.Loading )
  assertTrue(collectedStates[1] is UiState.Error )
  assertEquals(testException.message, (collectedStates[1] as UiState.Error).message)
  collectionJob.cancel()
 }

}