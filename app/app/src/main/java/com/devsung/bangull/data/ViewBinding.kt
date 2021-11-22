package com.devsung.bangull.data

import android.view.View
import android.view.animation.*
import androidx.databinding.BindingAdapter
import com.devsung.bangull.adapter.AnimationAdapter

class ViewBinding {

    companion object {

        private var ready = 0
        private var leftRight = false

        @JvmStatic
        @BindingAdapter("animation")
        fun setAnimation(view: View, bool: Boolean) {
            if (ready < 2) {
                ready++
                return
            }
            view.isClickable = false
            view.startAnimation(getAnimation(bool).apply {
                setAnimationListener(object: AnimationAdapter() {
                    override fun onAnimationEnd(animation: Animation?) {
                        view.isClickable = true
                    }
                })
            })
            leftRight = !leftRight
        }

        private fun getAnimation(bool: Boolean) = AnimationSet(true).apply {
            addAnimation(AlphaAnimation(if (bool) 0.0f else 1.0f, if (bool) 1.0f else 0.0f))
            addAnimation(RotateAnimation(
                if (!bool && !leftRight || bool && leftRight) 0f else 180f,
                if (!bool && leftRight || bool && !leftRight) 0f else 180f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f))
            duration = 300
            fillAfter = true
        }
    }
}