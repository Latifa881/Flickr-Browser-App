package com.example.flickrbrowserapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_row.view.*
import java.net.URL

class RecyclerViewAdapter  ( val details:ArrayList<Details>,val image:ImageView,val rvMain:RecyclerView,val linearLayout:LinearLayout): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>(){
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

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

            Glide.with(context)
                .load(data.imageLink)
                .into(ivImage)
           ivImage.setOnClickListener{
               image.visibility=View.VISIBLE
               linearLayout.visibility=View.GONE
               rvMain.visibility=View.GONE
               Glide.with(context)
                   .load(data.imageLink)
                   .into(image)
           }
            image.setOnClickListener{
                image.visibility=View.GONE
                linearLayout.visibility=View.VISIBLE
                rvMain.visibility=View.VISIBLE
            }
            tvTitle.text=data.title
        }
    }

    override fun getItemCount()=details.size
}
