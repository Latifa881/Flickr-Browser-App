package com.example.flickrbrowserapp

import com.google.gson.annotations.SerializedName

class Photo {
    @SerializedName("title")
    var title: String? =null

    @SerializedName("url_s")
    var url_s: String? =null
}