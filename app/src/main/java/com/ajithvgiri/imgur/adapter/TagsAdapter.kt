package com.ajithvgiri.imgur.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajithvgiri.imgur.R
import com.ajithvgiri.imgur.api.model.Tags
import kotlinx.android.synthetic.main.layout_item_tag.view.*

class TagsAdapter(val tags: ArrayList<Tags>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
return TagsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_tag, parent, false))
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tag = tags[position]
        (holder as TagsViewHolder).bind(tag)
    }

    class TagsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(tag: Tags){
            itemView.textViewTag.text = tag.name
            itemView.textViewTag.setTextColor(Color.parseColor("#${tag.accent}"))
        }
    }
}