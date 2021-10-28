package com.example.flickrbrowserapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET
    fun getPhotos(@Url url: String): Call<Flickr>?
}