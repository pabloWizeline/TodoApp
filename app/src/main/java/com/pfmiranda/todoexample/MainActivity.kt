package com.pfmiranda.todoexample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pfmiranda.todoexample.databinding.ActivityMainBinding
import com.pfmiranda.todoexample.domain.GetTodosUseCase
import com.pfmiranda.todoexample.domain.Todo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject // Hilt proveerá la instancia de ApiService aquí
    lateinit var getTodosUseCase: GetTodosUseCase

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
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
                val todos: Result<List<Todo>> = getTodosUseCase()
                todos.fold(
                    onSuccess = { todosList ->
                        todoAdapter.updateTodos(todosList)
                        showEmptyState(todosList.isEmpty())
                        showError(false)
                    },
                    onFailure = { exception ->
                        showError(true, exception.message)
                    }
                )
            } catch (e: Exception) {
                showError(true, e.message)
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

    private fun showError(show: Boolean, exception: String? = null) {
        binding.errorLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
        exception?.let {
            Toast.makeText(
                this@MainActivity,
                "Error: $exception",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

