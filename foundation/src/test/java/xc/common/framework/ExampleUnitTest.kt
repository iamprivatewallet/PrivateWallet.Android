package xc.common.framework

import com.basiclib.utils.LibAppUtils
import xc.common.framework.utils.EncryptionUtils
import xc.common.framework.utils.XcStringUtils
import io.reactivex.Observable
import org.junit.Test
import retrofit2.http.POST


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    class DataResp {
        var err_code: String? = null
        var msg: String? = null
    }

    interface NetInter {

        @POST("http://114.115.166.19:38081/bro/app/pbtok.do")
        fun login(): Observable<DataResp>

    }

    @Test
    fun testVersion() {
        println(LibAppUtils.hasNewVersion("1.0.2", "1.0.2"))
        println(LibAppUtils.hasNewVersion("1.0.3", "1.0.2"))
        println(LibAppUtils.hasNewVersion("1.0.3", "1.0.4"))
    }

    @Test
    fun useAppContext() {
        println("0.002".toString().indexOf(".")+2 < "0.002"!!.length-1)

    }
}
