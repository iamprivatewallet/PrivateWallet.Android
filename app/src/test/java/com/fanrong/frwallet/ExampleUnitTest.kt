package com.fanrong.frwallet

import org.brewchain.sdk.util.WalletUtil
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        println(BytesHelper.hexStr2BigDecimal("0x000000000000000000000000000000000000000000a56fa5b99019a5c8000000",0,0))

//        assert("11111".isHexStr())


        println(WalletUtil.getKeyPair("elite mention army apology belt acquire flip topic ensure abstract film doctor"))
    }

}