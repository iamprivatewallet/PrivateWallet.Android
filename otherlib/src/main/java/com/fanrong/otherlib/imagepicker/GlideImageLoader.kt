package com.fanrong.otherlib.imagepicker

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.fanrong.otherlib.glide.noCacheLoad
import com.lzy.imagepicker.loader.ImageLoader

class GlideImageLoader : ImageLoader {
    override fun clearMemoryCache() {
    }

    override fun displayImagePreview(
        activity: Activity?,
        path: String?,
        imageView: ImageView?,
        width: Int,
        height: Int
    ) {
        Glide.with(activity).noCacheLoad(path).into(imageView)

    }

    override fun displayImage(
        activity: Activity?,
        path: String?,
        imageView: ImageView?,
        width: Int,
        height: Int
    ) {
        Glide.with(activity).noCacheLoad(path).into(imageView)
    }
}