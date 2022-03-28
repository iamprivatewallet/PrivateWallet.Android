package xc.common.tool.comptent

import android.os.Environment
import xc.common.tool.utils.XcTimeUtils
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class CatchExceptionHandler : Thread.UncaughtExceptionHandler {

    companion object {
        var appName = "xc"
        private var default: Thread.UncaughtExceptionHandler? = null

        fun init() {
            default = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(CatchExceptionHandler())
        }
    }

    var rootdir = Environment.getExternalStorageDirectory().absolutePath

    override fun uncaughtException(t: Thread?, e: Throwable?) {

        writeSdCard(e)
        e?.printStackTrace()

        default?.uncaughtException(t, e)
    }

    private fun writeSdCard(e: Throwable?) {
        try {

            var fileName =
                "crash-log${XcTimeUtils.get_ymd(System.currentTimeMillis().toString())}.txt"

            var dir =
                File(rootdir + "/${appName}")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            var crashFile = File(dir, fileName)
            if (!crashFile.exists()) {
                crashFile.createNewFile()
            }

            val fileWriter = FileWriter(crashFile, true)
            var printWriter = PrintWriter(fileWriter)
            printWriter.print("${XcTimeUtils.get_yMd_hms(System.currentTimeMillis().toString())}  \\r\\n")
            printWriter.println()
            e?.printStackTrace(printWriter)
            printWriter.println()
            printWriter.println()
            printWriter.println()

            printWriter.close()
            fileWriter.close()
        } catch (e: Exception) {
            println(e)
        }
    }
}