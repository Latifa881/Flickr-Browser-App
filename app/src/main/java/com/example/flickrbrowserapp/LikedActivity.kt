package com.example.flickrbrowserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flickrbrowserapp.Database.PhotosDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class LikedActivity : AppCompatActivity() {
    lateinit var rvMain: RecyclerView
    val detailsArray = ArrayList<photoDetails>()
    val favoritesPhotosDB by lazy { PhotosDatabase.getInstance(this).favoritesPhotosDao() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked)
        bottomNavigationView()
        rvMain = findViewById(R.id.rvMain)
        rvMain.adapter = RecyclerViewAdapter(detailsArray, findViewById(R.id.ivImage), rvMain, findViewById(R.id.constraintLayout),this)
        rvMain.layoutManager = LinearLayoutManager(this)
        readFromDB()

    }
    fun bottomNavigationView(){
        var bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.search -> startActivity(Intent(this, MainActivity::class.java))
                R.id.liked->startActivity(Intent(this,LikedActivity::class.java))
            }
            true
        }
    }
    fun readFromDB(){
        detailsArray.clear()
        val favPhotos= favoritesPhotosDB.getAllFavoritesPhoto()
        for(photo in favPhotos)
        {
            detailsArray.add(photoDetails(photo.Url,photo.title))
            rvMain.adapter!!.notifyDataSetChanged()
        }
    }
}