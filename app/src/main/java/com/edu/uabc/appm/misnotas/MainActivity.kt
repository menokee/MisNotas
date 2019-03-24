package com.edu.uabc.appm.misnotas

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager
import com.edu.uabc.appm.misnotas.database.MisNotasApp
import com.edu.uabc.appm.misnotas.database.TaskEntity
import com.edu.uabc.appm.misnotas.database.TasksAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TasksAdapter
    lateinit var tasks: MutableList<TaskEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tasks = ArrayList()
        getTask()

        btnAddTask.setOnClickListener {
            addTask(TaskEntity(name = etTask.text.toString()))
        }
    }

    fun getTask() {
        doAsync {
            tasks =
                MisNotasApp.database.TaskDao().getAllTasks()
            uiThread {
                setUpRecyclerView(tasks)
            }
        }
    }

    fun clearFocus() {
        etTask.setText("")
    }

    fun Context.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    fun addTask(task: TaskEntity) {
        doAsync {
            val id = MisNotasApp.database.TaskDao().addTask(task)
            val recoveryTask = MisNotasApp.database.TaskDao().getTaskById(id)
            uiThread {
                tasks.add(recoveryTask)
                adapter.notifyItemInserted(tasks.size)
                clearFocus()
                hideKeyboard()
            }
        }
    }

    fun updateTask(task: TaskEntity) {
        doAsync {
            task.isDone = !task.isDone
            MisNotasApp.database.TaskDao().updateTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        doAsync {
            val position = tasks.indexOf(task)
            MisNotasApp.database.TaskDao().deleteTask(task)
            tasks.remove(task)
            uiThread {
                //                toast("delete ${tasks[position].name}")
                adapter.notifyItemRemoved(position)
            }
        }
    }

    fun setUpRecyclerView(tasks: List<TaskEntity>) {
        adapter = TasksAdapter(tasks, { updateTask(it) }, { deleteTask(it) })
        recyclerView = findViewById(R.id.rvTask)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}