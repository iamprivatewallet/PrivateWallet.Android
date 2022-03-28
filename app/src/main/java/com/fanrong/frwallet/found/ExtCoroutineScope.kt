package com.fanrong.frwallet.found

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.tryLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit
): Job {
    return this.launch(context, start) {
        block.invoke(this)
//        try {
//            block.invoke(this)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            showToastAtMainThread(e.message ?: "未知错误")
//        }

    }

}