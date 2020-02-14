package com.ajithvgiri.imgur.story

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.ajithvgiri.imgur.R
import com.ajithvgiri.imgur.utils.toPixel

class StoryProgressBar(
    context: Context,
    private var index: Int,
    var durationInSeconds: Int,
    private val timeWatcher: ProgressTimeWatcher
) : ProgressBar(context,null, android.R.attr.progressBarStyleHorizontal) {
    private var objectAnimator = ObjectAnimator.ofInt(this, "progress", this.progress, 100)
    private var hasStarted: Boolean = false


    init {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        params.marginEnd = 5f.toPixel(context)
        this.max = 100
        this.progress = 0
        this.layoutParams = params
        this.progressDrawable = ContextCompat.getDrawable(context, R.drawable.drawable_progress_bar)
    }

    fun startProgress() {
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                timeWatcher.onEnd(index)
            }

            override fun onAnimationCancel(animation: Animator?) {
                animation?.apply { removeAllListeners() }
            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })
        objectAnimator.apply {
            duration = (durationInSeconds * 1000).toLong()
            start()
        }

        hasStarted = true
    }

    fun cancelProgress() {
        objectAnimator.apply {
            cancel()
        }
    }

    fun pauseProgress() {
        objectAnimator.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pause()
            } else {
                end()
            }
        }
    }

    fun resumeProgress() {
        if (hasStarted) {
            objectAnimator.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    resume()
                } else {
                    start()
                }
            }
        }
    }

    fun editDurationAndResume(newDurationInSeconds: Int) {
        this.durationInSeconds = newDurationInSeconds
        cancelProgress()
        startProgress()
    }
}