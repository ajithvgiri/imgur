package com.ajithvgiri.imgur.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class GallerySearch(
    @Json(name = "success")
    val success: Boolean,

    @Json(name = "status")
    val status: Int,

    @Json(name = "data")
    var data: List<Post>? = ArrayList()
):Serializable

@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "id")
    var id: String = "",

    @Json(name = "title")
    var title: String? = null,

    @Json(name = "description")
    var description: String? = null,

    @Json(name = "nsfw")
    var nsfw: Boolean? = null,

    @Json(name = "comment_count")
    var comment_count: Int = 0,

    @Json(name = "tags")
    var tags: List<Tags>? = ArrayList(),

    @Json(name = "images")
    var images: List<Images>? = ArrayList()
):Serializable

@JsonClass(generateAdapter = true)
data class Images(
    @Json(name = "id")
    var id:String = "",

    @Json(name = "title")
    var title:String? = null,

    @Json(name = "description")
    var description: String? = null,

    @Json(name = "size")
    var size: Long = 0L,

    @Json(name = "type")
    var type:String = "",

    @Json(name = "nsfw")
    var nsfw:Boolean? = null,

    @Json(name = "link")
    var link:String? = null
):Serializable



@JsonClass(generateAdapter = true)
data class Tags(
    @Json(name = "name")
    var name:String = "",

    @Json(name = "background_hash")
    var background_hash:String? = null,

    @Json(name = "accent")
    var accent: String? = null

):Serializable

