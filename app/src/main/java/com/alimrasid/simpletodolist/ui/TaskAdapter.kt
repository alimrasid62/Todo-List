package com.alimrasid.simpletodolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alimrasid.simpletodolist.data.entity.Task
import com.alimrasid.simpletodolist.databinding.ItemTaskBinding

class TaskAdapter(
    private val isArchiveMode: Boolean = false,
    private val onCompleteClicked: (Task) -> Unit,
    private val onDeleteClicked: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvCategory.text = task.category
            binding.textViewDescription.text = task.description

            if (isArchiveMode) {
                binding.buttonComplete.visibility = View.GONE
            } else {
                binding.buttonComplete.visibility = View.VISIBLE
                binding.buttonComplete.setOnClickListener {
                    onCompleteClicked(task)
                }
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClicked(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(old: Task, new: Task): Boolean = old.id == new.id
    override fun areContentsTheSame(old: Task, new: Task): Boolean = old == new
}