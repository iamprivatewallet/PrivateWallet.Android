package xc.common.tool.listener

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

open class SimpleLifecycleObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    final fun onCreate() {
        onCreateCall()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    final fun onStart() {
        onStartCall()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    final fun onResume() {
        onResumeCall()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    final fun onPause() {
        onPauseCall()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    final fun onStop() {
        onStopCall()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    final fun onDestroy() {
        onDestroyCall()
    }

    open fun onCreateCall() {}
    open fun onStartCall() {}
    open fun onResumeCall() {}
    open fun onPauseCall() {}
    open fun onStopCall() {}
    open fun onDestroyCall() {}

}