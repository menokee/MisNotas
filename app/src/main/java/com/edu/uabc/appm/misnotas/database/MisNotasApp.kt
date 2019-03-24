package com.edu.uabc.appm.misnotas.database

import android.app.Application
import android.arch.persistence.room.Room

class MisNotasApp: Application(){
    companion object {
        lateinit var database: TaskDatabase
    }
    override fun onCreate(){
        super.onCreate()
        MisNotasApp.database = Room.databaseBuilder(this, TaskDatabase::class.java, "task-db").build()
    }
}