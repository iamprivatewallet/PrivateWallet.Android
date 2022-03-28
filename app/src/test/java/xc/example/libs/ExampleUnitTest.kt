package xc.example.libs

import org.brewchain.core.crypto.cwv.util.BytesHelper
import org.json.JSONException
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

fun JSONObject?.toMap(non:String): HashMap<String, Any>? {
    if (this == null) {
        return HashMap()
    }
    println(non)
    try {
        val keyIter: Iterator<String> = this.keys()
        var key: String
        var value: Any
        var valueMap = HashMap<String, Any>()
        while (keyIter.hasNext()) {
            key = keyIter.next()
            value = this[key] as Any
            valueMap[key] = value
        }
        return valueMap
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    return null
}

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        println(BytesHelper.hexStr2BigDecimal("0x3b9aca00",0,0))
        
        assertEquals(4, 2 + 2)
    }
}