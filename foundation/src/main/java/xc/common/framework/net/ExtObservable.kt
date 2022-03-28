package xc.common.framework.net

import androidx.lifecycle.LifecycleOwner
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import xc.common.tool.listener.SimpleLifecycleObserver
import xc.common.tool.utils.SWLog
import kotlin.reflect.KProperty


public fun <T> Observable<T>.netSchduler(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}


public fun Disposable.bindLifeCycle(owner: LifecycleOwner) {
    owner.lifecycle.addObserver(object : SimpleLifecycleObserver() {

        override fun onDestroyCall() {
            dispose()
        }
    })
}


interface NetCallBack<T> {
    fun onSuccess(t: T)
    fun onFailed(error: Throwable)

}


public fun <T> Observable<T>.subscribeObj(netCallBack: NetCallBack<T>): Disposable {
    return subscribe(Consumer {
        netCallBack.onSuccess(it)
    }, Consumer {
        it.printStackTrace()
        netCallBack.onFailed(it)

    })
}



/**
 *
 * 属性委托 不支持 getValue 返回可空对象，通过wrap 对象包一层提供一个默认值
 *
 *
 */
class LifecycleOwnerWrap() {
    var lifeCycleOwner: LifecycleOwner? = null

    constructor(lifeCycleOwner: LifecycleOwner) : this() {
        this.lifeCycleOwner = lifeCycleOwner
    }

    fun hasObj(): Boolean {
        return lifeCycleOwner != null
    }

}

class lifecycleOwnerWrapDelegate<T> {
    var name: LifecycleOwnerWrap? =
        LifecycleOwnerWrap()
    operator fun setValue(
        thisRef: Observable<T>,
        property: KProperty<*>,
        value: LifecycleOwnerWrap
    ) {
        SWLog.i("lifecycleOwnerWrapDelegate----setValue")
        this.name = value
    }

    operator fun getValue(thisRef: Observable<T>, property: KProperty<*>): LifecycleOwnerWrap {
        SWLog.i("lifecycleOwnerWrapDelegate----getValue")
        return this.name!!
    }


}

var <T> Observable<T>.lifecycleOwnerWrap: LifecycleOwnerWrap by lifecycleOwnerWrapDelegate()


fun <T> Observable<T>.bindLifeCycle(owner: LifecycleOwner): Observable<T> {
    this.lifecycleOwnerWrap =
        LifecycleOwnerWrap(owner)
    return this
}


fun <T> Observable<T>.selfSubsribe(): Disposable? {
    if (!this.lifecycleOwnerWrap.hasObj()) {
        return subscribe()
    }

    val subscribe = subscribe()
    this.lifecycleOwnerWrap.lifeCycleOwner!!.lifecycle.addObserver(object : SimpleLifecycleObserver() {

        override fun onDestroyCall() {
            subscribe.dispose()
        }
    })
    return subscribe
}

