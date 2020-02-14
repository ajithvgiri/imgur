package com.ajithvgiri.imgur.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajithvgiri.imgur.R
import com.ajithvgiri.imgur.api.model.Comment
import kotlinx.android.synthetic.main.layout_item_comments.view.*

class CommentsAdapter(private val comments: ArrayList<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
return CommentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_comments, parent, false))
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val comment = comments[position]
        (holder as CommentsViewHolder).bind(comment)
    }

    class CommentsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(comment: Comment){
            itemView.textViewAuthor.text = comment.author
            itemView.textViewComment.text = comment.comment
        }
    }
}