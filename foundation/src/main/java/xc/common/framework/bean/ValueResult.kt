package xc.common.framework.bean

class ValueResult() {

    companion object {
        fun error(error: Any?): ValueResult {
            return ValueResult().apply {
                this.error = error
                this.success = false
            }
        }

        fun success(result: Any?): ValueResult {
            return ValueResult().apply {
                this.result = result
                this.success = true
            }
        }
    }

    var success = false
    var error: Any? = null
    var result: Any? = null

}