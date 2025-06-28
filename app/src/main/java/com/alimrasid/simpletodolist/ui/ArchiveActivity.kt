package com.alimrasid.simpletodolist.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alimrasid.simpletodolist.R
import com.alimrasid.simpletodolist.data.entity.Task
import com.alimrasid.simpletodolist.databinding.ActivityArchiveBinding
import com.alimrasid.simpletodolist.ui.viewmodel.TaskViewModel
import com.alimrasid.simpletodolist.ui.viewmodel.TaskViewModelFactory

class ArchiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchiveBinding
    private lateinit var adapter: TaskAdapter

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter(
            isArchiveMode = true,
            onCompleteClicked = {},
            onDeleteClicked = { task -> showDeleteConfirmation(task) }
        )

        binding.recyclerViewArchived.adapter = adapter
        binding.recyclerViewArchived.layoutManager = LinearLayoutManager(this)

        viewModel.archivedTasks.observe(this) { tasks ->
            if (tasks.isEmpty()) {
                binding.emptyText.visibility = View.VISIBLE
            } else {
                binding.emptyText.visibility = View.GONE
            }
            adapter.submitList(tasks)
        }
    }

    private fun showDeleteConfirmation(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Hapus tugas \"${task.title}\" dari arsip?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.delete(task)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
