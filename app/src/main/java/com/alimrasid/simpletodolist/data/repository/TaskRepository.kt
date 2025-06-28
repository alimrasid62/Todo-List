package com.alimrasid.simpletodolist.data.repository

import androidx.lifecycle.LiveData
import com.alimrasid.simpletodolist.data.dao.TaskDao
import com.alimrasid.simpletodolist.data.entity.Task

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()
    val pendingTasks: LiveData<List<Task>> = taskDao.getPendingTasks()
    val completedTasks: LiveData<List<Task>> = taskDao.getCompletedTasks()

    fun getActiveTasks(): LiveData<List<Task>> = taskDao.getActiveTasks()

    fun getArchivedTasks(): LiveData<List<Task>> = taskDao.getArchivedTasks()


    fun getTasksByCategory(category: String): LiveData<List<Task>> {
        return taskDao.getTasksByCategory(category)
    }

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }
}