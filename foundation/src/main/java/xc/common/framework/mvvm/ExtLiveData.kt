package xc.common.framework.mvvm

import androidx.lifecycle.LiveData

fun <T> LiveData<T>.notnullValue(): T {
    return value!!
}