package com.example.flickrbrowserapp.Database

import androidx.room.*

//Data Access Object
@Dao
interface FavoritesPhotosDao {

    @Query("SELECT * FROM favoritesPhotos ")
    fun getAllFavoritesPhoto(): List<favoritesPhotos>
    @Insert
    fun insertFavPhoto(recipe: favoritesPhotos)
    @Update
    fun updateFavPhoto(recipe: favoritesPhotos)
    @Delete
    fun deleteFavPhoto(recipe: favoritesPhotos)

}