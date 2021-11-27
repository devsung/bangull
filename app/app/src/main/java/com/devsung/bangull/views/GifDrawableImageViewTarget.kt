package com.devsung.bangull.views

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.ImageViewTarget

class GifDrawableImageViewTarget(
    view: ImageView,
    private val loopCount: Int
) : ImageViewTarget<Drawable>(view) {

    override fun setResource(resource: Drawable?) {
        if (resource is GifDrawable)
            resource.setLoopCount(loopCount)
        view.setImageDrawable(resource)
    }
}