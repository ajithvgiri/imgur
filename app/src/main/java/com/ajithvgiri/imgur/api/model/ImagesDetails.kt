package com.ajithvgiri.imgur.api.model

import java.io.Serializable

data class ImageDetails(
    var tags: List<Tags>,
    var images: Images,
    var galleryHash: String
):Serializable
