package xc.common.framework

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Observable

import org.junit.Test
import org.junit.runner.RunWith

import retrofit2.http.POST

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    class DataResp {
        var err_code: String? = null
        var msg: String? = null
    }

    interface NetInter {

        @POST("http://114.115.166.19:38081/bro/app/pbtok.do")
        fun login(): Observable<DataResp>

    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("xc.common.frameworklib.test", appContext.packageName)




    }
}
