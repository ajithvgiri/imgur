package com.ajithvgiri.imgur.api

import com.ajithvgiri.imgur.api.model.Comments
import com.ajithvgiri.imgur.api.model.GallerySearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("gallery/search/{page}")
    fun getImages(@Path("page") page: Int, @Query("q") query:String="Science and Tech"): Call<GallerySearch>

    @GET("gallery/{galleryHash}/comments")
    fun getComments(@Path("galleryHash") galleryHash: String): Call<Comments>

}