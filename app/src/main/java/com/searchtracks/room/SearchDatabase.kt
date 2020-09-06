package com.searchtracks.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.searchtracks.room.dao.SearchDao
import com.searchtracks.room.entity.SearchEnt

@Database(entities = [SearchEnt::class], version = 1)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun SearchDao(): SearchDao
}