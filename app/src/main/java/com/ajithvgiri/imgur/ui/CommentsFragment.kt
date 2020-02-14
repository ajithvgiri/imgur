package com.ajithvgiri.imgur.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajithvgiri.imgur.R
import com.ajithvgiri.imgur.adapter.CommentsAdapter
import com.ajithvgiri.imgur.api.ApiInterface
import com.ajithvgiri.imgur.api.RetrofitService
import com.ajithvgiri.imgur.api.model.Comment
import com.ajithvgiri.imgur.api.model.Comments
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_comments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentsFragment : BottomSheetDialogFragment(){

    val api: ApiInterface = RetrofitService.retrofit.create(ApiInterface::class.java)

    lateinit var commentsAdapter: CommentsAdapter

    fun newInstance(imageHash: String): CommentsFragment {
        val contactBottomSheetFragment = CommentsFragment()
        val args = Bundle()
        args.putString("imageHash", imageHash)
        contactBottomSheetFragment.arguments = args
        return contactBottomSheetFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BaseBottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageHash = arguments?.getString("imageHash")

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        println("galleryHash $imageHash")
        imageHash?.let { loadComments(it) }
    }

    private fun loadComments(imageHash:String){
        api.getComments(imageHash).enqueue(object : Callback<Comments> {
            override fun onFailure(call: Call<Comments>, t: Throwable) {
                Log.e("CommentsFragment", "error ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<Comments>, response: Response<Comments>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {listOfComments->
                        progressBar.visibility = View.GONE
                        if (listOfComments.isNotEmpty()){
                            textViewNoComments.visibility = View.GONE
                            commentsAdapter = CommentsAdapter(listOfComments as ArrayList<Comment>)
                            recyclerView.adapter = commentsAdapter

                            listOfComments.forEach {comments->
                                println("response for comment ${comments.comment}.")
                            }
                        }else{
                            textViewNoComments.visibility = View.VISIBLE
                        }
                    }
                }else{
                    Log.e("CommentsFragment", "error ${response.body()}")
                }
            }
        })
    }
}