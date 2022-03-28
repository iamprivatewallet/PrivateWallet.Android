package xc.common.tool.utils

import java.util.*

class XcUrlParse(var url: String) {

    private var urlParamter = mutableMapOf<String, String>()
    private var urlPath: String

    init {
        var index = url.indexOf("?")
        var query = ""
        if (index > 0) {
            urlPath = url.substring(0, index)
            query = url.substring(index + 1)
        } else {
            urlPath = url
        }

        if (query.checkNotEmpty()) {
            val tokenizer = StringTokenizer(query, "&")
            while (tokenizer.hasMoreElements()) {
                val attributeValuePair = tokenizer.nextToken()
                if (attributeValuePair.length > 0) {
                    val assignmentIndex = attributeValuePair.indexOf('=')

                    if (assignmentIndex < 0) {
                        // No assignment found, treat as if empty value
                        urlParamter.put(attributeValuePair, "")
                    } else {
                        urlParamter.put(attributeValuePair.substring(0, assignmentIndex),
                                attributeValuePair.substring(assignmentIndex + 1))
                    }
                }
            }
        }

    }


    fun putParamter(key: String, value: String) {
        urlParamter.put(key, value)
    }

    fun toFullUrl(): String {
        if (urlParamter.checkIsEmpty()) {
            return urlPath
        } else {
            var fullUrl = urlPath + "?"

            for (mutableEntry in urlParamter) {
                fullUrl += "${mutableEntry.key}=${mutableEntry.value}&"
            }

            return fullUrl.trimEnd('&')
        }

    }


}