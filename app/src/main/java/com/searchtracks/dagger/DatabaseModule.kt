package com.searchtracks.dagger

import androidx.room.Room
import com.searchtracks.MyApplication
import com.searchtracks.room.SearchDatabase
import com.searchtracks.room.dao.SearchDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(var application: MyApplication) {

    @DatabaseInfo
    private val mDBName = "search.db"

    @Singleton
    @Provides
    fun provideDatabase(): SearchDatabase {
        return Room.databaseBuilder(application, SearchDatabase::class.java, mDBName).fallbackToDestructiveMigration().build()
    }

    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String {
        return mDBName
    }

    @Singleton
    @Provides
    fun provideSearchDao(db: SearchDatabase): SearchDao {
        return db.SearchDao()
    }
}