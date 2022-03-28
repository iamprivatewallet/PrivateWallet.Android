package com.fanrong.frwallet

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Test
import org.spongycastle.jce.provider.BouncyCastleProvider
import java.security.AlgorithmParameters
import java.security.Security
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Test {
    class Abc {
//        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    }


    @Test
    fun test() {


    }


    fun isMatcher(s: String?): Boolean {
        val regEx = "^[\\u4e00-\\u9fa5a-zA-Z]+$"
        val pattern: Pattern = Pattern.compile(regEx)
        val m: Matcher = pattern.matcher(s)
        return m.find()
    }


    @Test
    fun a() {
        val json =
            "[{\"from\":\"0x2085A2ec89fFdBb162e45e32D136298FC072BcAb\",\"to\":\"0x62AAaFeedf192f1Cdd66754F9Ae93a511B0ac93d\",\"value\":\"0x29a2241af62c0000\",\"gasPrice\":\"0x09184e72a000\",\"gas\":\"0x2710\"}]"
        val type = object : TypeToken<List<Abc?>>() {}.type
        val fromJson = Gson().fromJson<Any>(json, object : TypeToken<List<Abc?>>() {}.type)
        println()


        val abc =
            "{\"cipher\":\"cbc\",\"cipherParams\":{\"iv\":\"93cf443c3b2c31c4f9b9b0cf7b7897d8\"},\"cipherText\":\"7a1180ed51ebeffab7c86324032c74a5862fa81bb1afd9dca17b94fada8233a352457d5f9e15f367af8f304de0573c5614a3d5a8d0a9bc665da12b9228ccd7a910e7063cb4aff57716f192893409527307d223bbf4e832fad10a437b6fc9be83515649613b4c30ecab0623c046705f08027142b4961454c5f7970782035b6eb0\",\"ksType\":\"aes\",\"params\":{\"c\":128,\"dklen\":256,\"l\":114,\"salt\":\"7c69d19ff5f33593\"},\"pwd\":\"30e36724313b7e3d9fff50e3772f769d361ad626fb228662385235a28499f761\"}"
//
//        val instance = KeyStore.getInstance("CBC")
//        val instance1 = Cipher.getInstance("")
        Security.addProvider(BouncyCastleProvider());

        val cipher = Cipher.getInstance("AES/CBC/ISO10126Padding")
        val sKeySpec = SecretKeySpec("1111111111111111".toByteArray(), "AES")
        val instance2 = AlgorithmParameters.getInstance("AES")
        instance2.init(IvParameterSpec("93cf443c3b2c31c4f9b9b0cf7b7897d8".toByteArray()))
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, instance2)
        val doFinal =
            cipher.doFinal("3253cff74ca86622f13df7ae97513715f1e221ef1fb56b8e0173e880052a365f402098cc97c8552ac26d6c0599bb1c9a1c0e00abe4ca94f071148dbb3243cb5173bfd6ffd97aa9c6ae29d0337f9d5f7e572f4334fa21dbca51fdb13a36a11285c5c397bcd5a1519d05ac7deaa7556d9b8ec6636a0baff0e8fac527d17388b482".toByteArray())


        var a =
            "{\"address\":\"e5a5a70af56a8971be1f86871edc528aa1cbd2f9\",\"id\":\"254420fb-237c-4449-9ecf-ed6bcd04b1b4\",\"version\":3,\"Crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"5437349426a4a68fdbf7a58320237a7a\"},\"ciphertext\":\"505ed3c7442dd57e48ac74125f70b60b8a5b089e3de81df7e40e36fec293d118\",\"kdf\":\"scrypt\",\"kdfparams\":{\"salt\":\"dbccf583a0e0336249d757f18f23c997a88adf024f2b6da07a0ae36098cbcff0\",\"n\":131072,\"dklen\":32,\"p\":1,\"r\":8},\"mac\":\"a98b6c3000d696b0bda5356c6861875df3e95d923753bee2555944d58528184f\"},\"x-ethers\":{\"client\":\"ethers.js\",\"gethFilename\":\"UTC--2021-08-19T09-50-26.0Z--e5a5a70af56a8971be1f86871edc528aa1cbd2f9\",\"mnemonicCounter\":\"bd1719fc98bb51037686f5140b25875b\",\"mnemonicCiphertext\":\"c28a99fdd8d74125fbf28d355f99cc2a\",\"path\":\"m/44'/60'/0'/0/0\",\"version\":\"0.1\"}}"
        println(a)

//        instance.load(ByteArrayInputStream(abc.toByteArray()),"11111111".toCharArray())

        println()
    }
}