package com.edu.uabc.appm.misnotas.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(TaskEntity::class), version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun TaskDao(): TaskDao
}