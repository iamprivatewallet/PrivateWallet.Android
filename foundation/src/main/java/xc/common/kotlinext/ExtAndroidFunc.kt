package xc.common.kotlinext

import xc.common.tool.CommonTool


fun Any.delayCallAtMain(call: Runnable, delay: Long) {
    CommonTool.mainHandler.postDelayed(call, delay)

}