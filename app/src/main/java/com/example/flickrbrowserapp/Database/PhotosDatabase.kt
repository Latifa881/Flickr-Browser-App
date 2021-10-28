package com.example.flickrbrowserapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [favoritesPhotos::class],version = 1,exportSchema = false)
abstract class PhotosDatabase: RoomDatabase() {

    companion object{
        var instance:PhotosDatabase?=null;
        fun getInstance(ctx: Context):PhotosDatabase
        {
            if(instance!=null)
            {
                return  instance as PhotosDatabase;
            }
            instance= Room.databaseBuilder(ctx,PhotosDatabase::class.java,"favoritesPhotosDB").run { allowMainThreadQueries() }.build()
            return instance as PhotosDatabase;
        }
    }
    abstract fun favoritesPhotosDao():FavoritesPhotosDao
}