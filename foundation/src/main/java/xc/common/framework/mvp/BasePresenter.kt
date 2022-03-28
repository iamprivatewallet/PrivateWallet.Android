package xc.common.framework.mvp

import androidx.lifecycle.LifecycleOwner

abstract class BasePresenter<T : IBaseView> {
    protected val view: T
    protected val lifecycleOwner: LifecycleOwner

    constructor(lifecycleOwner: LifecycleOwner, iBaseView: T) {
        this.lifecycleOwner = lifecycleOwner
        this.view = iBaseView
    }
}