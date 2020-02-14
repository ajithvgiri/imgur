package com.ajithvgiri.imgur.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Comments(
    @Json(name = "success")
    val success: Boolean,

    @Json(name = "status")
    val status: Int,

    @Json(name = "data")
    var data: List<Comment>? = ArrayList()
): Serializable


@JsonClass(generateAdapter = true)
data class Comment(
    @Json(name = "id")
    val id: String,

    @Json(name = "comment")
    val comment: String,

    @Json(name = "author")
    var author: String,

    @Json(name = "deleted")
    var deleted: Boolean
): Serializable

