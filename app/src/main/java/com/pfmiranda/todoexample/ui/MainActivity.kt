package com.pfmiranda.todoexample.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pfmiranda.todoexample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Inyección del ViewModel usando Hilt y Activity KTX
    private val viewModel: TodosViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeUiState()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState -> // o .collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            showLoading(true)
                        }
                        is UiState.Success -> {
                            showLoading(false)
                            todoAdapter.updateTodos(uiState.data)
                            showEmptyState(uiState.data.isEmpty())
                            showError(false)
                        }
                        is UiState.Error -> {
                            showLoading(false)
                            showError(true, uiState.message)
                        }
                    }
                }
            }
        }
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
        viewModel.loadTodos()
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

