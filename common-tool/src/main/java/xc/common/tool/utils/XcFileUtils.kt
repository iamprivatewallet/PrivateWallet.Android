package xc.common.tool.utils

import android.util.Base64
import java.io.File
import java.io.FileOutputStream


object XcFileUtils {

    fun base64ToFile(data: String, filePath: String) {
        try {
            File(filePath).apply {
                File(this.parent).run {
                    this.mkdirs()
                }
                if (this.exists()) {
                    this.delete()
                }

                File(filePath).createNewFile()
            }


            var realData: String = data
            if (data.contains(",")) {
                realData = realData.split(",")[1]
            }
            var buffer = Base64.decode(realData, Base64.DEFAULT);
            val outputStream = FileOutputStream(filePath)
            outputStream.write(buffer)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}