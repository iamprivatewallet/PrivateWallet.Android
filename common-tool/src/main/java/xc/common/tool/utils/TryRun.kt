package xc.common.tool.utils


fun <T> tryRunDefault(default: T, block: () -> T): T {
    try {
        return block.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        return default
    }
}

object TryRun {


}