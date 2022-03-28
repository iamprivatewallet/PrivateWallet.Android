package com.fanrong.frwallet.tools

import android.graphics.Bitmap
import android.graphics.Matrix

object BitmapUtils {
    fun bigBitMap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(3f,3f)
        val resizeBmp: Bitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

//        val resizeBmp: Bitmap =
//            Bitmap.createScaledBitmap(bitmap, DensityUtil.dp2px(20), DensityUtil.dp2px(20), true)
        return resizeBmp
    }
}