package com.ajithvgiri.imgur.story

import android.view.View

interface StoryCallback{
    fun onFinish()
    fun onNextCalled(view: View,textView: View,story : Story,index: Int)
    fun onDetailView(index: Int)
}