package com.fanrong.frwallet

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.brewchain.sdk.util.WalletUtil
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.fanrong.frwallet", appContext.packageName)
//        WalletUtils
        println(WalletUtil.getKeyPair("elite mention army apology belt acquire flip topic ensure abstract film doctor"))

    }
}