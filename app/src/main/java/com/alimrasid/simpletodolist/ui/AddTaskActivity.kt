package com.alimrasid.simpletodolist.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alimrasid.simpletodolist.R
import com.alimrasid.simpletodolist.data.entity.Task
import com.alimrasid.simpletodolist.databinding.ActivityAddTaskBinding
import com.alimrasid.simpletodolist.ui.viewmodel.TaskViewModel
import com.alimrasid.simpletodolist.ui.viewmodel.TaskViewModelFactory
import com.alimrasid.simpletodolist.utils.ReminderHelper

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val category = binding.etCategory.text.toString().trim()
            val description = binding.editTextDescription.text.toString()
            val dueDate = System.currentTimeMillis() + 60 * 60 * 1000 // 1 jam dari sekarang (sementara)

            if (title.isNotEmpty() && category.isNotEmpty()) {
                val task = Task(title = title, category = category, description = description, dueDate = dueDate)
                viewModel.insert(task)
                viewModel.allTasks.observe(this) { tasks ->
                    val lastTask = tasks.lastOrNull()
                    if (lastTask != null) {
                        ReminderHelper.setReminder(this, lastTask)
                    }
                }
                finish()
            } else {
                Toast.makeText(this, "Judul dan kategori wajib diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}