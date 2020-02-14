package com.ajithvgiri.imgur.story

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.ajithvgiri.imgur.R
import kotlinx.android.synthetic.main.layout_story_view.view.*
import kotlin.math.max

class Story(
    context: Context,
    private var listOfStoryView: List<StoryView>,
    passedInContainerView: ViewGroup,
    private var storyCallback: StoryCallback
) : ConstraintLayout(context) {

    private var currentlyShownIndex = 0
    private lateinit var currentView: View

    private var listOfProgressBar = ArrayList<StoryProgressBar>()
    private var view: View = View.inflate(context, R.layout.layout_story_view, this)
    private var pausedState : Boolean = false

    init {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        val gestureDetector = GestureDetector(context, SingleTapConfirm())

        val touchListener = object  : OnTouchListener{
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (gestureDetector.onTouchEvent(event)) {
                    // single tap
                    when (v.id) {
                        view.rightLayout.id -> {
                            next()
                        }
                        view.leftLayout.id -> {
                            previous()
                        }
                        view.centerLayout.id -> {
                            fullScreen()
                        }
                    }

                    return true
                } else {
                    // your code for move and drag
                    return when(event.action){
                        MotionEvent.ACTION_DOWN -> {
                            callPause(true)
                            true
                        }

                        MotionEvent.ACTION_UP -> {
                            callPause(false)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        view.leftLayout.setOnTouchListener(touchListener)
        view.rightLayout.setOnTouchListener(touchListener)
        view.centerLayout.setOnTouchListener(touchListener)

        this.layoutParams = params
        passedInContainerView.addView(this)

        listOfStoryView.forEachIndexed { index, sliderView ->
            val myProgressBar = StoryProgressBar(context, index, sliderView.durationInSeconds, object : ProgressTimeWatcher {
                    override fun onEnd(indexFinished: Int) {
                        currentlyShownIndex = indexFinished + 1
                        next()
                    }
                })
            listOfProgressBar.add(myProgressBar)
            view.linearProgressIndicatorLay.addView(myProgressBar)
        }
    }



    fun show() {
        view.progressBar.visibility = View.GONE
        if (currentlyShownIndex != 0) {
            for (i in 0..max(0, currentlyShownIndex - 1)) {
                listOfProgressBar[i].progress = 100
                listOfProgressBar[i].cancelProgress()
            }
        }

        if (currentlyShownIndex != listOfProgressBar.size - 1) {
            for (i in currentlyShownIndex + 1 until listOfProgressBar.size) {
                listOfProgressBar[i].progress = 0
                listOfProgressBar[i].cancelProgress()
            }
        }

        currentView = listOfStoryView[currentlyShownIndex].view

        listOfProgressBar[currentlyShownIndex].startProgress()

        storyCallback.onNextCalled(currentView,textViewTitle, this, currentlyShownIndex)

        view.currentlyDisplayedView.removeAllViews()
        view.currentlyDisplayedView.addView(currentView)
        val params = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT, 1f
        )
        if(currentView is ImageView) {
            (currentView as ImageView).scaleType = ImageView.ScaleType.FIT_CENTER
            (currentView as ImageView).adjustViewBounds = true
        }
        currentView.layoutParams = params
    }

    fun previous() {
        try {
            if (currentView == listOfStoryView[currentlyShownIndex].view) {
                currentlyShownIndex--
                if (0 > currentlyShownIndex) {
                    currentlyShownIndex = 0
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            currentlyShownIndex -= 2
        }finally {
            show()
        }
    }

    fun next() {
        try {
            if (currentView == listOfStoryView[currentlyShownIndex].view) {
                currentlyShownIndex++
                if (listOfStoryView.size <= currentlyShownIndex) {
                    finish()
                    return
                }
            }
            show()
        } catch (e: IndexOutOfBoundsException) {
            finish()
        }
    }

    fun fullScreen(){
        if (pausedState){
            if (listOfStoryView.size > 1){
                storyCallback.onDetailView(currentlyShownIndex)
            }else{
                storyCallback.onDetailView(0)
            }
        }
    }

    private fun finish() {
        storyCallback.onFinish()
        for (progressBar in listOfProgressBar) {
            progressBar.cancelProgress()
            progressBar.progress = 100
        }
    }

    fun callPause(pause : Boolean){
        try {
            if(pause){
                if(!pausedState){
                    this.pausedState = !pausedState
                    onPause(false)
                }
            } else {
                if(pausedState){
                    this.pausedState = !pausedState
                    onResume()
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    fun onPause(withLoader : Boolean) {
        if(withLoader){
            view.progressBar.visibility = View.VISIBLE
        }
        listOfProgressBar[currentlyShownIndex].pauseProgress()
    }

    fun onResume() {
        view.progressBar.visibility = View.GONE
        listOfProgressBar[currentlyShownIndex].resumeProgress()
    }

    private inner class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return true
        }
    }

}