package com.example.demoexample.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "graph_item")
data class GraphEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int?,
        @ColumnInfo(name = "title") var title: String ?=null,
        @ColumnInfo(name = "score1") var score1: String ?=null,
        @ColumnInfo(name = "score2") var score2: String?=null
)
