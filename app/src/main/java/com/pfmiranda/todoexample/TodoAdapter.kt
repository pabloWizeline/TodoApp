package com.pfmiranda.todoexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pfmiranda.todoexample.databinding.ItemTodoBinding


class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var todos = listOf<Todo>()

    fun updateTodos(newTodos: List<Todo>) {
        val diffCallback = TodoDiffCallback(todos, newTodos)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        todos = newTodos
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount() = todos.size

    inner class TodoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            binding.apply {
                todoTitle.text = todo.title
                todoId.text = "ID: ${todo.id}"
                todoUserId.text = "Usuario: ${todo.userId}"

                // Cambiar apariencia según el estado completado
                if (todo.completed) {
                    statusIcon.setImageResource(R.drawable.ic_check_circle)
                    statusIcon.setColorFilter(
                        ContextCompat.getColor(root.context, R.color.green)
                    )
                    statusText.text = "Completado"
                    statusText.setTextColor(
                        ContextCompat.getColor(root.context, R.color.green)
                    )
                    cardView.alpha = 0.7f
                } else {
                    statusIcon.setImageResource(R.drawable.ic_radio_button_unchecked)
                    statusIcon.setColorFilter(
                        ContextCompat.getColor(root.context, R.color.orange)
                    )
                    statusText.text = "Pendiente"
                    statusText.setTextColor(
                        ContextCompat.getColor(root.context, R.color.orange)
                    )
                    cardView.alpha = 1.0f
                }

                // Click listener opcional
                cardView.setOnClickListener {
                    // Aquí puedes agregar funcionalidad de click
                }
            }
        }
    }
}

// DiffUtil para optimizar las actualizaciones del RecyclerView
class TodoDiffCallback(
    private val oldList: List<Todo>,
    private val newList: List<Todo>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}