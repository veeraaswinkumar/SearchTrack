package com.searchtracks.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.searchtracks.room.entity.SearchEnt

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(results: SearchEnt)

    @Query("SELECT * FROM cart_list WHERE status LIkE:status")
    fun mGetallSavedData(status: String): List<SearchEnt>

    @Query("DELETE FROM cart_list WHERE artistName LIkE :artistName")
    fun mRemoveCart(artistName: String)
}