package com.fanrong.frwallet.tools

import org.apache.commons.lang.StringUtils
import java.net.URI

object getUrlHostUtils {
    fun getHost(url: String): String {
        var url = url
        if (!(StringUtils.startsWithIgnoreCase(url, "http://") || StringUtils
                .startsWithIgnoreCase(url, "https://"))
        ) {
            url = "http://$url"
        }
        var returnVal = StringUtils.EMPTY
        try {
            val uri = URI(url)
            returnVal = uri.getHost()
        } catch (e: Exception) {
        }
        if (StringUtils.endsWithIgnoreCase(returnVal, ".html") || StringUtils
                .endsWithIgnoreCase(returnVal, ".htm")
        ) {
            returnVal = StringUtils.EMPTY
        }
        return returnVal
    }
}