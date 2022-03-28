package com.fanrong.otherlib.eventbus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import org.greenrobot.eventbus.EventBus

fun EventBus.extRegisterAutoUnregister(lifecycleOwner: LifecycleOwner) {
    if (isRegistered(lifecycleOwner)) {
        return
    }
    register(lifecycleOwner)
    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyCall() {
            unregister(lifecycleOwner)
        }
    })
}