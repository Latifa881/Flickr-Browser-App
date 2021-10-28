package com.example.flickrbrowserapp.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoritesPhotos")
data class favoritesPhotos (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id : Int = 0, // this is how can include id if needed
    @ColumnInfo(name = "Title") val title: String,
    @ColumnInfo(name = "Url") val Url: String
)