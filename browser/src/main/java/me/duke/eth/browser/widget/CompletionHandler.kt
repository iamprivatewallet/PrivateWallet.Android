package me.duke.eth.browser.widget

interface CompletionHandler<T> {
    fun complete(retValue: T?)
    fun complete()
    fun setProgressData(value: T?)
}