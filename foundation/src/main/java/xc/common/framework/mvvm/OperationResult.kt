package xc.common.framework.mvvm

class OperationResult<T> {
    var resultData: T? = null
    var isExcuteSuccess = false

    constructor(resultData: T?, result: Boolean) {
        this.resultData = resultData
        this.isExcuteSuccess = result
    }

    constructor(result: Boolean) {
        this.isExcuteSuccess = result
    }


}