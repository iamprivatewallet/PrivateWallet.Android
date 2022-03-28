package xc.common.tool.utils

import java.io.File

object FileOperatorUtils {

    /**
     * 深度删除文件
     */
    fun delete(file: File) {
        if (!file.exists()) {
            return
        }

        if (file.isDirectory) {
            for (listFile in file.listFiles()) {
                delete(listFile)
            }
        } else {
            file.delete()
        }
    }
}