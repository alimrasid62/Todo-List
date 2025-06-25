package com.alimrasid.simpletodolist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alimrasid.simpletodolist.databinding.ActivityMainBinding
import com.alimrasid.simpletodolist.ui.AddTaskActivity
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
            onCheckChanged = { task -> viewModel.update(task.copy(isDone = !task.isDone)) },
            onDeleteClicked = { task -> viewModel.delete(task) }
        )

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = adapter

        viewModel.allTasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        binding.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }
}