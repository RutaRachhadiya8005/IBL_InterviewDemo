package com.example.demoexample.roomdatabase

import androidx.room.*

@Dao
interface Graph_Item {
    @Query("SELECT * FROM graph_item")
    fun getAllgraph(): List<GraphEntity>

    @Insert
    fun insertgraph(vararg todo: GraphEntity)

    @Query("DELETE FROM graph_item WHERE id = :id")
    fun deletegraph(id: String)

    @Query("UPDATE graph_item SET title=:title , score1=:score1,score2=:score2 WHERE id=:id")
    fun updategraph(title: String,score1: String,score2: String, id: String)

}