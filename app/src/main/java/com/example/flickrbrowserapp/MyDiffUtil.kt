package com.example.flickrbrowserapp

import androidx.recyclerview.widget.DiffUtil

class MyDiffUtil(
    private val oldList:ArrayList<photoDetails>,
    private val newList:ArrayList<photoDetails>

):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
    return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        //compare the pk , here the image link is unique
     return oldList[oldItemPosition].imageLink==newList[newItemPosition].imageLink
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return when{
           oldList[oldItemPosition].imageLink!=newList[newItemPosition].imageLink->false
           oldList[oldItemPosition].title!=newList[newItemPosition].title->false
           else->true
       }
    }
}