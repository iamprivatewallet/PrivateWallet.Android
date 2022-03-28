package xc.common.tool.utils

fun <E> List<E>?.extGet(index: Int): E? {
    try {
        return this!!.get(index)!!
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}