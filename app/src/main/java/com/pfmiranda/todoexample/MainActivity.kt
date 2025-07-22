package com.pfmiranda.todoexample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pfmiranda.todoexample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var apiService: TodoApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRetrofit()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(TodoApiService::class.java)
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fetchButton.setOnClickListener {
            fetchTodos()
        }

        binding.refreshButton.setOnClickListener {
            fetchTodos()
        }
    }

    private fun fetchTodos() {
        lifecycleScope.launch {
            try {
                showLoading(true)
                val todos = apiService.getTodos()
                todoAdapter.updateTodos(todos)
                showEmptyState(todos.isEmpty())
                showError(false)
            } catch (e: Exception) {
                showError(true)
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.fetchButton.isEnabled = !show
        binding.refreshButton.isEnabled = !show
    }

    private fun showEmptyState(show: Boolean) {
        binding.emptyStateLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(show: Boolean) {
        binding.errorLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }
}

// Interfaz para Retrofit
interface TodoApiService {
    @GET("todos")
    suspend fun getTodos(): List<Todo>
}

// Modelo de datos
data class Todo(
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
)