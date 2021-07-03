package com.example.demoexample.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.demoexample.roomdatabase.GraphEntity
import com.example.demoexample.roomdatabase.Graph_Item


@Database(entities = [(GraphEntity::class)],
    version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun graphdao(): Graph_Item

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "demo_example.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}