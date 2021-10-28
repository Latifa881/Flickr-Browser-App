package com.example.flickrbrowserapp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickrbrowserapp.Database.FavoritesPhotosDao
import com.example.flickrbrowserapp.Database.PhotosDatabase
import com.example.flickrbrowserapp.Database.favoritesPhotos
import kotlinx.android.synthetic.main.activity_liked.view.*
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.android.synthetic.main.item_row.view.ivImage

class RecyclerViewAdapter  (val details:ArrayList<photoDetails>, val image:ImageView, val rvMain:RecyclerView, val constraintLayout:ConstraintLayout,val activity: Activity): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>(){
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
    val favoritesPhotosDB by lazy { PhotosDatabase.getInstance(activity).favoritesPhotosDao() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data=details[position]
        holder.itemView.apply {
//            if (activity is LikedActivity){
//                if(getItemCount()==0)
//                    lottieNoData.visibility=View.VISIBLE
//                else{ lottieNoData.visibility=View.GONE}
//            }
            var photoId=0
            var photoTitle=""
            var photoUrl=""
            var favPhotos=favoritesPhotosDB.getAllFavoritesPhoto()
            for(photo in favPhotos){
                if(photo.title==data.title&&photo.Url==data.imageLink){
                    photoId=photo.id
                    photoTitle=photo.title
                    photoUrl=photo.Url
                    ivFavorite.visibility=View.VISIBLE
                    ivFavorite_border.visibility=View.GONE
                }
            }

            ivFavorite.setOnClickListener {
                if(photoTitle.isNotEmpty()&&photoUrl.isNotEmpty())
                { favoritesPhotosDB.deleteFavPhoto(favoritesPhotos(photoId,photoTitle,photoUrl))
                    if (activity is LikedActivity)
                    { activity.readFromDB()
//                        if(getItemCount()==0)
//                            lottieNoData.visibility=View.VISIBLE
//                        else{ lottieNoData.visibility=View.GONE}
                                       }
                }
                ivFavorite.visibility=View.GONE
                ivFavorite_border.visibility=View.VISIBLE
            }
            ivFavorite_border.setOnClickListener {
                ivFavorite.visibility=View.VISIBLE
                ivFavorite_border.visibility=View.GONE
                favoritesPhotosDB.insertFavPhoto(favoritesPhotos(0,data.title,data.imageLink))
            }

            Glide.with(context)
                .load(data.imageLink)
                .into(ivImage)
           ivImage.setOnClickListener{
               image.visibility=View.VISIBLE
               constraintLayout.visibility=View.GONE
               rvMain.visibility=View.GONE
               Glide.with(context)
                   .load(data.imageLink)
                   .into(image)
           }
            image.setOnClickListener{
                image.visibility=View.GONE
                constraintLayout.visibility=View.VISIBLE
                rvMain.visibility=View.VISIBLE
            }
            tvTitle.text=data.title
        }
    }

    override fun getItemCount()=details.size

}
