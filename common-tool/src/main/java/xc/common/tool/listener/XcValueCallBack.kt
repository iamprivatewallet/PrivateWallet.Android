package xc.common.tool.listener

interface XcValueCallBack<T> {
    fun valueBack(t: T)
    fun error(){}
}
