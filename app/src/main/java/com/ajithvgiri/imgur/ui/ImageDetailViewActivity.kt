package com.ajithvgiri.imgur.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajithvgiri.imgur.R
import com.ajithvgiri.imgur.adapter.TagsAdapter
import com.ajithvgiri.imgur.api.model.ImageDetails
import com.ajithvgiri.imgur.api.model.Images
import com.ajithvgiri.imgur.api.model.Tags
import com.ajithvgiri.imgur.utils.memoryCache
import kotlinx.android.synthetic.main.activity_image_detail_view.*

class ImageDetailViewActivity : AppCompatActivity() {

    private var image: Images? = null
    private var tags: ArrayList<Tags>? = null
    private var details: ImageDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail_view)



        details = intent?.getSerializableExtra("image_details") as ImageDetails

        image = details?.images
        tags = details?.tags as ArrayList<Tags>


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        image?.title?.let { imageTitle ->
            supportActionBar?.title = imageTitle
        }

        details?.images?.link?.let { imageUrl ->
            imageView.setImageBitmap(memoryCache.get(imageUrl))

        }

        tags?.let { listOfTags ->
            val tagAdapter = TagsAdapter(listOfTags)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = tagAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_image_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        if (item.itemId == R.id.menu_comments) {
            details?.galleryHash?.let {
                val commentsFragment = CommentsFragment().newInstance(it)
                commentsFragment.show(supportFragmentManager, "comments_dialog_fragment")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
