package com.ajithvgiri.imgur.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import android.util.TypedValue

fun Float.toPixel(mContext: Context): Int {
    val r = mContext.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        r.displayMetrics
    ).toInt()
}

// Get max available VM memory, exceeding this amount will throw an
// OutOfMemory exception. Stored in kilobytes as LruCache takes an
// int in its constructor.
val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

// Use 1/8th of the available memory for this memory cache.
val cacheSize = maxMemory / 8

var memoryCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(cacheSize) {
    override fun sizeOf(key: String, bitmap: Bitmap): Int {
        // The cache size will be measured in kilobytes rather than
        // number of items.
        return bitmap.byteCount / 1024
    }
}