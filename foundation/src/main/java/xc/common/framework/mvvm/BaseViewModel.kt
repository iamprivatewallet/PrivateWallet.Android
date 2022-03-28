package xc.common.framework.mvvm

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import xc.common.tool.CommonTool
import kotlin.reflect.KFunction1

open abstract class BaseViewModel<T : BaseLiveData> : ViewModel() {

    val observeData: MutableLiveData<T> by lazy {
        MutableLiveData<T>()
    }

    abstract fun createDefautState(): T


    /**
     * T. 让方法体的 this 指向 T
     */
    @Synchronized
    fun newAndPostValue(initValue: T.() -> Unit) {
        CommonTool.mainHandler.post {
            observeData.value = createDefautState().apply {
                initValue()
            }
        }

    }

    fun postLoading(isShow: Boolean) {
        newAndPostValue {
            isShowLoading = isShow
        }
    }

    fun postErrorMsg(msg: String) {
        newAndPostValue {
            errorinfo = LiveDataErrorInfo(msg)
        }
    }

    /**
     * 让编译器更好的代码提示
     */
    fun observerDataChange(@NonNull owner: LifecycleOwner, @NonNull observer: KFunction1<T, Unit>) {
        observeData.observe(owner, observer)
    }
}