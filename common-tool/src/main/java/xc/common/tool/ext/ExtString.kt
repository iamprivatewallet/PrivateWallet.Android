package xc.common.tool.ext

fun String.extIsNetUrl(): Boolean {
    return this.startsWith("http", true)
}

fun String?.extHasDefault(default: String): String {
    if (this == null || this.length == 0) {
        return default
    }
    return this
}