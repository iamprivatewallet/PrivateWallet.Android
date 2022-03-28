package xc.common.tool.utils

import java.awt.font.NumericShaper

object FormatStr {
    fun formatPhone(phone: String?): String {
        if (phone == null) {
            return ""
        }

        if (phone!!.length < 8) {
            return phone + "****"
        }
        return phone!!.replaceRange(3..6, "****")
    }
}