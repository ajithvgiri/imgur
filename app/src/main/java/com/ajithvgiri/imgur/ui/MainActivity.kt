package com.ajithvgiri.imgur.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ajithvgiri.imgur.R
import com.ajithvgiri.imgur.api.ApiInterface
import com.ajithvgiri.imgur.api.RetrofitService
import com.ajithvgiri.imgur.api.model.GallerySearch
import com.ajithvgiri.imgur.api.model.ImageDetails
import com.ajithvgiri.imgur.story.Story
import com.ajithvgiri.imgur.story.StoryCallback
import com.ajithvgiri.imgur.story.StoryView
import com.ajithvgiri.imgur.utils.memoryCache
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity(), StoryCallback {

    val maxImageSize = 1 * 1024 * 1024 // 1 MB for demo purpose
    val api: ApiInterface = RetrofitService.retrofit.create(ApiInterface::class.java)
    private val okHttpClient: OkHttpClient = RetrofitService.okHttpClient
    var listOfDetailImage = ArrayList<ImageDetails>()
    var listOfViews = ArrayList<StoryView>()

    private var mStory: Story? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getImages()
    }

    private fun getImages() {
        api.getImages(0).enqueue(object : Callback<GallerySearch> {
            override fun onFailure(call: Call<GallerySearch>, t: Throwable) {
                Log.e("MainActivity", "error ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<GallerySearch>, response: Response<GallerySearch>) {
                if (response.isSuccessful) {

                    response.body()?.data?.let { listOfPosts ->
                        listOfPosts.forEach { post ->
                            post.images?.let { listOfImages ->
                                listOfImages.forEach { image ->
                                    if ((image.type.equals("image/jpeg") || image.type.equals("image/png"))
                                        && image.size <= maxImageSize
                                    ) {
                                        val tag = if (post.tags?.isNotEmpty() == true) {
                                            post.tags!!
                                        } else {
                                            ArrayList()
                                        }
                                        val detailModel = ImageDetails(tag, image, post.id)
                                        listOfDetailImage.add(detailModel)
                                    }
                                }
                            }
                        }
                    }

                    listOfDetailImage.forEach { _ ->
                        //image to be loaded from the internet
                        val imageView = ImageView(this@MainActivity)
                        listOfViews.add(StoryView(imageView))
                    }

                    mStory = Story(this@MainActivity, listOfViews, container, this@MainActivity)
                    mStory?.show()
                }
            }
        })
    }


    override fun onNextCalled(view: View, textView: View, story: Story, index: Int) {
        listOfDetailImage[index].images.link?.let { imageUrl ->
            loadBitmap(listOfDetailImage[index], view as ImageView, textView as TextView, story)
        }
    }

    override fun onFinish() {
        Toast.makeText(this@MainActivity, "Finished!", Toast.LENGTH_LONG).show()
    }

    override fun onDetailView(index: Int) {
        val imageUrl = listOfDetailImage[index].images.link
        memoryCache[imageUrl].let {
            val intent = Intent(this@MainActivity, ImageDetailViewActivity::class.java)
            intent.putExtra("image_details",listOfDetailImage[index] )
            startActivity(intent)
        }
    }

    fun loadBitmap(
        imageDetails: ImageDetails,
        imageView: ImageView,
        textView: TextView,
        story: Story
    ) {
        memoryCache.get(imageDetails.images.link)?.also {
            story.onResume()
            imageView.setImageBitmap(it)
            imageDetails.images.title?.let { title ->
                textView.text = title
            }
        } ?: run {
            story.onPause(true)
            imageDetails.images.link?.let { imageUrl ->
                val request = Request.Builder().url(imageUrl).build()
                okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.e(this@MainActivity.localClassName, "Oops ${e.localizedMessage}")
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        if (response.isSuccessful) {
                            val originalBitmap =
                                BitmapFactory.decodeStream(response.body()?.byteStream()) // Read the data from the stream
                            val out = ByteArrayOutputStream()
                            originalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                            val compressedBitmap =
                                BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
                            memoryCache.put(imageDetails.images.link, compressedBitmap)
                            runOnUiThread {
                                loadBitmap(imageDetails, imageView, textView, story)
                            }
                        }
                    }
                })
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mStory?.onPause(false)
    }

    override fun onResume() {
        super.onResume()
        mStory?.onResume()
    }

}
