package com.alimrasid.simpletodolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alimrasid.simpletodolist.data.entity.Task
import com.alimrasid.simpletodolist.databinding.ActivityMainBinding
import com.alimrasid.simpletodolist.ui.AddTaskActivity
import com.alimrasid.simpletodolist.ui.ArchiveActivity
import com.alimrasid.simpletodolist.ui.TaskAdapter
import com.alimrasid.simpletodolist.ui.viewmodel.TaskViewModel
import com.alimrasid.simpletodolist.ui.viewmodel.TaskViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(application)
    }
    private lateinit var adapter: TaskAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter(
            isArchiveMode = false,
            onCompleteClicked = { task -> showCompleteConfirmation(task) },
            onDeleteClicked = { task -> showDeleteCOnfirm(task) }
        )

        viewModel.archivedTasks.observe(this) { tasks ->
            if (tasks.isEmpty()) {
                binding.emptyText.visibility = View.VISIBLE
            } else {
                binding.emptyText.visibility = View.GONE
            }
            adapter.submitList(tasks)
        }


        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = adapter

        viewModel.activeTasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        binding.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        binding.buttonGoToArchive.setOnClickListener {
            val intent = Intent(this, ArchiveActivity::class.java)
            startActivity(intent)
        }

        viewModel.activeTasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

    }

    private fun showDeleteCOnfirm(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus tugas ini?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.delete(task)
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun showCompleteConfirmation(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Tugas Selesai")
            .setMessage("Apakah kamu telah menyelesaikan tugas \"${task.title}\"?")
            .setPositiveButton("Ya") { _, _ ->
                val updatedTask = task.copy(isArchived = true)
                viewModel.update(updatedTask)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}