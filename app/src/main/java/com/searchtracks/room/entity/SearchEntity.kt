package com.searchtracks.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cart_list")
class SearchEnt {
    @PrimaryKey
    var artistname = ""
    var trackname = ""
    var releaseDate = ""
    var collectionPrice = ""
    var collectionName = ""
    var artworkUrl100 = ""
    var status = ""
}