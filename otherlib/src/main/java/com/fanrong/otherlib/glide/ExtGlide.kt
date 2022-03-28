package com.fanrong.otherlib.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry
import com.bumptech.glide.request.RequestOptions

fun RequestManager.noCacheLoad(path:String?):RequestBuilder<Drawable>{
    return load(path).apply(
        RequestOptions()
        .skipMemoryCache(true))
}