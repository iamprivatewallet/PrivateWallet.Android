package com.fanrong.frwallet.wallet.eth

import android.webkit.ValueCallback
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.tools.CallJsCodeUtils
import com.fanrong.frwallet.tools.Web3JsUtils
import com.fanrong.frwallet.wallet.IWalletUtil
import org.brewchain.sdk.util.WalletUtil
import xc.common.framework.bean.ValueResult
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.XcSingleten
import xc.common.tool.utils.checkNotEmpty
import xc.common.tool.utils.isJsonObj

class EthWalletUtils : IWalletUtil {
    val chainType: String

    constructor(chainType: String) {
        this.chainType = chainType
    }

    override fun createMainWallet(words: String) {
//        val mnemonic = WalletUtil.getMnemonic()
//        createMainWallet(name, words, password, passwordHint, callback)
    }


    override fun createWalletNoSave(walletName: String, password: String, passwordhint: String, callback: (result: ValueResult) -> Unit) {

        val mnemonic = WalletUtil.getMnemonic()
        val ethModel = WalletDao("")
        ethModel.mnemonic = mnemonic
        fun getEthPrivateKey(mainKey: String) {
            CallJsCodeUtils.getPrivateKey(mainKey, object : ValueCallback<String> {
                override fun onReceiveValue(ethPrivateKey: String?) {
                    var realEthPrivateKey = CallJsCodeUtils.readStringJsValue(ethPrivateKey)
                    ethModel.privateKey = realEthPrivateKey

                    val wallet = ethModel
                    wallet.password = password
                    wallet.chainType = this@EthWalletUtils.chainType
                    wallet.passwordHint = passwordhint
                    wallet.walletName = walletName

                    callback(ValueResult.success(wallet))
                }
            })
        }

        fun getEthAddr(mainKey: String) {
            CallJsCodeUtils.getAddress(mainKey, object : ValueCallback<String> {
                override fun onReceiveValue(ethAddress: String) {
                    var realEthAddress = CallJsCodeUtils.readStringJsValue(ethAddress)
                    ethModel.address = realEthAddress
                    getEthPrivateKey(mainKey)
                }
            })
        }

        CallJsCodeUtils.mnemonicToHDPrivateKey(mnemonic, object : ValueCallback<String> {
            override fun onReceiveValue(mainKey: String?) {
                getEthAddr(CallJsCodeUtils.readStringJsValue(mainKey))
            }
        })

    }


    //获取公钥(address)和私钥
    override fun createFromWords(words: String, callback: (result: ValueResult) -> Unit) {
        var addr = ""
        fun getEthPrivateKey(mainKey: String) {
            CallJsCodeUtils.getPrivateKey(mainKey, object : ValueCallback<String> {
                override fun onReceiveValue(ethPrivateKey: String?) {
                    var realEthPrivateKey = CallJsCodeUtils.readStringJsValue(ethPrivateKey)

                    val wallet = WalletDao("0x" + addr.removePrefix("0x")).apply {
                        this.mnemonic = words
                        privateKey = realEthPrivateKey
                        this.chainType = this@EthWalletUtils.chainType
                    }
                    callback.invoke(ValueResult.success(wallet))
                }
            })
        }

        fun getEthAddr(mainKey: String) {
            CallJsCodeUtils.getAddress(mainKey, object : ValueCallback<String> {
                override fun onReceiveValue(ethAddress: String) {
                    var realEthAddress = CallJsCodeUtils.readStringJsValue(ethAddress)
                    addr = realEthAddress
                    getEthPrivateKey(mainKey)
                }
            })
        }

        CallJsCodeUtils.mnemonicToHDPrivateKey(words, object : ValueCallback<String> {
            override fun onReceiveValue(mainKey: String?) {
                if (mainKey!=null&&mainKey!="null"){
                    getEthAddr(CallJsCodeUtils.readStringJsValue(mainKey))
                }else{
                    callback.invoke(ValueResult.error("助记词不正确"))
                }

            }
        })

    }

    override fun createPrivateKey(privatekey: String, password: String, passwordhint: String, callback: (result: ValueResult) -> Unit) {

        CallJsCodeUtils.privateToAddress(privatekey, object : ValueCallback<String> {
            override fun onReceiveValue(value: String?) {
                val jsValue = CallJsCodeUtils.readStringJsValue(value ?: "")
                var b = jsValue.checkNotEmpty()
                var s = "0x" + jsValue

                if (b) {
                    var wallet = WalletDao(s).apply {
                        privateKey = privatekey
                        this.password = password
                        this.passwordHint = passwordhint
                        this.isMainWallet = "0"
                        this.chainType = this@EthWalletUtils.chainType
                    }

                    var insert = WalletOperator.insert(wallet)

                    if (insert) {
                        callback.invoke(ValueResult.success(wallet))
                    } else {
                        callback.invoke(ValueResult.error("地址已存在"))
                    }
                } else {
                    callback.invoke(ValueResult.error("生成地址失败"))
                }
            }
        })
    }

    override fun createFromKeystore(keystore: String, password: String, callback: (result: ValueResult) -> Unit) {
        Web3JsUtils.eth_decrypt(keystore, password, object : ValueCallback<String> {
            override fun onReceiveValue(value: String?) {
                var result = CallJsCodeUtils.readStringJsValue(value)
                val wallet = WalletDao()
                if (!result.isJsonObj()) {
                    callback(ValueResult.error("keystore格式不对 或 密码不匹配"))
                } else {
                    val toJson = XcSingleten.gson.fromJson(result, Map::class.java)
                    wallet.chainType =this@EthWalletUtils.chainType
                    wallet.address = toJson.get("address").toString()
                    wallet.privateKey = toJson.get("privateKey").toString()
                    wallet.password = password

                    callback(ValueResult.success(wallet))

                }
            }
        })
    }

    override fun getWalletKeystore(wallet: WalletDao, password: String, callback: (result: ValueResult) -> Unit) {

        Web3JsUtils.eth_encrypt(wallet.privateKey, password, object : ValueCallback<String> {
            override fun onReceiveValue(value: String?) {
                SWLog.e(value)
                callback.invoke(ValueResult.success(CallJsCodeUtils.readStringJsValue(value)))
            }
        })
    }


    fun createMainWallet(
        name: String,
        mnemonic: String,
        password: String,
        passwordHint: String,
        callback: (success: Boolean, wallet: WalletDao) -> Unit
    ) {
        val ethModel = WalletDao("")

        fun getEthPrivateKey(mainKey: String) {
            CallJsCodeUtils.getPrivateKey(mainKey, object : ValueCallback<String> {
                override fun onReceiveValue(ethPrivateKey: String?) {
                    var realEthPrivateKey = CallJsCodeUtils.readStringJsValue(ethPrivateKey)
                    ethModel.privateKey = realEthPrivateKey
                    ethModel.isMainWallet = "1"
                    ethModel.mnemonic = mnemonic
                    ethModel.password = password
                    ethModel.passwordHint = passwordHint
                    ethModel.chainType = "ETH"
                    ethModel.balance = "0.00"
                    ethModel.isCurrentWallet = "1"
                    ethModel.identityName = name
                    ethModel.save()

                    CoinDao(ethModel.chainType!!).apply {
                        sourceAddr = ethModel.address
                        sourceWallet = ethModel.chainType + "_" + ethModel.address
                    }.save()

                    val hecoModel = WalletDao("")
                    hecoModel.address = ethModel.address
                    hecoModel.privateKey = realEthPrivateKey
                    hecoModel.isMainWallet = "1"
                    hecoModel.mnemonic = mnemonic
                    hecoModel.password = password
                    hecoModel.passwordHint = passwordHint
                    hecoModel.chainType = "HECO"
                    hecoModel.identityName = name
                    hecoModel.balance = "0.00"
                    hecoModel.save()
                    CoinDao(hecoModel.chainType!!).apply {
                        sourceAddr = ethModel.address
                        sourceWallet = hecoModel.chainType + "_" + ethModel.address
                    }.save()


                    val bscModel = WalletDao("")
                    bscModel.address = ethModel.address
                    bscModel.privateKey = realEthPrivateKey
                    bscModel.isMainWallet = "1"
                    bscModel.mnemonic = mnemonic
                    bscModel.password = password
                    bscModel.passwordHint = passwordHint
                    bscModel.chainType = "BSC"
                    bscModel.identityName = name
                    bscModel.balance = "0.00"
                    bscModel.save()

                    CoinDao(bscModel.chainType!!).apply {
                        sourceAddr = ethModel.address
                        sourceWallet = bscModel.chainType + "_" + ethModel.address
                    }.save()


                    callback.invoke(true, ethModel)
                }
            })
        }

        fun getEthAddr(mainKey: String) {
            CallJsCodeUtils.getAddress(mainKey, object : ValueCallback<String> {
                override fun onReceiveValue(ethAddress: String) {
                    var realEthAddress = CallJsCodeUtils.readStringJsValue(ethAddress)
                    ethModel.address = "0x" + realEthAddress.removePrefix("0x")
                    getEthPrivateKey(mainKey)
                }
            })
        }

        CallJsCodeUtils.mnemonicToHDPrivateKey(mnemonic, object : ValueCallback<String> {
            override fun onReceiveValue(mainKey: String?) {
                getEthAddr(CallJsCodeUtils.readStringJsValue(mainKey))
            }
        })
    }


}