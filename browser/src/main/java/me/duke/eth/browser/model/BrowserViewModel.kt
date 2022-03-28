package me.duke.eth.browser.model

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BrowserViewModel:ViewModel() {

    /**
     * 图标
     */
    val result = MutableLiveData<BrowserViewModel>()



    /**
     * 标题
     */
    var title:String? = ""

    /**
     * 图标
     */
    var icon:Bitmap? = null

    /**
     * url
     */
    var url:String? = ""

}