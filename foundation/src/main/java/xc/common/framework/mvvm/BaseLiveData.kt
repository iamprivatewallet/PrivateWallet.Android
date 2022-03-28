package xc.common.framework.mvvm

open abstract class BaseLiveData {

    var errorinfo: LiveDataErrorInfo? = null
    var isShowLoading = false

    open fun clearInfo(){
        errorinfo = null
        isShowLoading = false
    }
}