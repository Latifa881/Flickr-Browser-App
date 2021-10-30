package com.example.flickrbrowserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flickrbrowserapp.Database.PhotosDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class LikedActivity : AppCompatActivity() {
    lateinit var rvLiked: RecyclerView
    lateinit var likedConstraintLayout:ConstraintLayout
    lateinit var ivLikedImage:ImageView
    val detailsArray = ArrayList<photoDetails>()
    val favoritesPhotosDB by lazy { PhotosDatabase.getInstance(this).favoritesPhotosDao() }
    private val myAdapter by lazy{RecyclerViewAdapter(detailsArray, ivLikedImage, rvLiked, likedConstraintLayout,this)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked)
        bottomNavigationView()
        ivLikedImage=findViewById(R.id.ivLikedImage)
        likedConstraintLayout=findViewById(R.id.likedConstraintLayout)
        rvLiked = findViewById(R.id.rvLiked)
        rvLiked.adapter = myAdapter
        rvLiked.layoutManager = LinearLayoutManager(this)
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
          //  rvLiked.adapter!!.notifyDataSetChanged()
            myAdapter.updateRVData(detailsArray)
        }
    }
}