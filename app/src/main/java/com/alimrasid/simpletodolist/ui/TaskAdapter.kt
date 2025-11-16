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
    private val onDeleteClicked: (Task) -> Unit,
    private val onItemDoubleClick: (Task) -> Unit // Tambahan
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        private var lastClickTime = 0L
        private val DOUBLE_CLICK_INTERVAL = 300L

        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvCategory.text = task.category
            binding.textViewDescription.text = task.description

            // Sembunyikan tombol saat awal
            binding.buttonComplete.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE

            // Deteksi klik ganda atau klik tunggal
            binding.root.setOnClickListener {
                val clickTime = System.currentTimeMillis()
                if (clickTime - lastClickTime < DOUBLE_CLICK_INTERVAL) {
                    // Klik ganda → buka detail
                    onItemDoubleClick(task)
                    binding.buttonComplete.visibility = View.GONE
                    binding.btnDelete.visibility = View.GONE
                } else {
                    // Klik pertama → tampilkan tombol jika bukan archive mode
                    if (!isArchiveMode) {
                        binding.buttonComplete.visibility = View.VISIBLE
                    }
                    binding.btnDelete.visibility = View.VISIBLE
                }
                lastClickTime = clickTime
            }

            // Aksi tombol
            binding.buttonComplete.setOnClickListener {
                onCompleteClicked(task)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClicked(task)
            }

            // Jika di mode arsip, tombol complete tetap disembunyikan
            if (isArchiveMode) {
                binding.buttonComplete.visibility = View.GONE
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