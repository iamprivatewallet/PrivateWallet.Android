package com.fanrong.frwallet.wallet.cwv

import android.webkit.ValueCallback
//import com.brewchain.sdk.crypto.KeyPairs
import com.fanrong.frwallet.dao.data.NodeList
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.tools.BrewChainJsUtils
import com.fanrong.frwallet.tools.CallJsCodeUtils
import com.fanrong.frwallet.wallet.IWalletUtil
import org.brewchain.core.crypto.model.KeyPairs
import org.brewchain.sdk.Config
import org.brewchain.sdk.HiChain
import org.brewchain.sdk.util.WalletUtil
import xc.common.framework.bean.ValueResult
import xc.common.tool.utils.XcSingleten
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.isJsonObj
import kotlin.concurrent.thread

class CVNWalletUtils : IWalletUtil {


    val chainName = "CVN"


    var isInited = false

    constructor() {
        init()
    }

    fun init() {
        thread {
            try {
                Config.changeHost(NodeList.CURRENT_CVN)
                if (!isInited) {
                    isInited = true
                    HiChain.init()
                    Config.changeHost(NodeList.CURRENT_CVN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    //助记词导入
    fun createWalletWithWords(): WalletDao {
        var words = WalletUtil.getMnemonic()
        val kp = WalletUtil.getKeyPair(words)

        val walletModel = WalletDao("")
        walletModel.address = kp.address
        walletModel.privateKey = kp.prikey
        walletModel.pubKey = kp.pubkey
        walletModel.mnemonic = words

        return walletModel
    }


    //通过私钥导入钱包
    fun importPriKey(
        walletName: String,
        priKey: String,
        result: (cwvWallet: WalletDao?, msg: String?) -> Unit
    ) {

        var kp: KeyPairs? = null
        try {
            kp = WalletUtil.getKeyPairFromPk(priKey)

            if (kp == null) {
                result(null, "导入失败,请检查私钥是否正确")
                return
            }

            val cvnwallet = WalletDao("")
            cvnwallet.chainType = chainName
            cvnwallet.address = kp.address
            cvnwallet.privateKey = kp.prikey
            cvnwallet.pubKey = kp.pubkey
            cvnwallet.identityName = walletName
            result(cvnwallet, "导入成功")
        } catch (e: Exception) {
            e.printStackTrace()
            result(null, "导入失败，请检查私钥")
        }
    }

    //通过创建钱包
    fun createWallet(
        words: String, walletName: String,
        result: (cwvWallet: WalletDao?) -> Unit
    ) {
        //助记词生成公私钥地址对
        var kp: KeyPairs? = null
        try {
            kp = WalletUtil.getKeyPair(words)
        } catch (ex: Exception) {
            ex.printStackTrace()
            result(null)
            return
        }
        if (kp == null) {
            result(null)
            return
        }

        val cwvWalletModel = WalletDao("")
        cwvWalletModel.address = kp.address
        cwvWalletModel.privateKey = kp.prikey
        cwvWalletModel.pubKey = kp.pubkey

        if (cwvWalletModel != null) {
            cwvWalletModel.chainType = chainName
            cwvWalletModel.mnemonic = words
        }
        result(cwvWalletModel)
    }

    override fun createMainWallet(words: String) {
        createWallet(words, chainName) {
            if (it != null) {
                it.password = WalletOperator.queryMainWallet()[0].password
                it.passwordHint = WalletOperator.queryMainWallet()[0].passwordHint
                it.isMainWallet = "1"
                it!!.save()

                CoinDao(it.chainType!!).apply {
                    sourceAddr = it.address
                    sourceWallet = it.chainType + "_" + it.address
                }.save()

            }
        }
    }

    override fun createWalletNoSave(
        walletName: String,
        password: String,
        passwordhint: String,
        callback: (result: ValueResult) -> Unit
    ) {
        val wallet = createWalletWithWords()
        wallet.password = password
        wallet.chainType = chainName
        wallet.passwordHint = passwordhint
        wallet.walletName = walletName
        callback(ValueResult.success(wallet))
    }

    override fun createFromWords(words: String, callback: (result: ValueResult) -> Unit) {
        createWallet(words, chainName) {
            if (it != null) {
                callback.invoke(ValueResult.success(it))
            } else {
                callback.invoke(ValueResult.error("创建出错"))
            }
        }
    }

    override fun createPrivateKey(
        privatekey: String,
        password: String,
        passwordhint: String,
        callback: (result: ValueResult) -> Unit
    ) {
        importPriKey(chainName, privatekey) { wallet: WalletDao?, msg: String? ->
            if (wallet != null) {
                wallet.password = password
                wallet.passwordHint = passwordhint
                wallet.isMainWallet = "0"
                wallet.chainType = chainName

                if (WalletOperator.insert(wallet)) {
                    callback.invoke(ValueResult.success(wallet))
                } else {
                    callback.invoke(ValueResult.error("钱包已存在"))
                }

            }
        }
    }

    override fun createFromKeystore(keystore: String, password: String, callback: (result: ValueResult) -> Unit) {
        BrewChainJsUtils.importKeystore(keystore,password,object :ValueCallback<String>{
            override fun onReceiveValue(value: String?) {
                var walletInfo = CallJsCodeUtils.readStringJsValue(value)
                if (walletInfo.isJsonObj()) {
                    val toJson = XcSingleten.gson.fromJson(walletInfo, Map::class.java)
                    var walletDao = WalletDao("").apply {
                        this.chainType = this@CVNWalletUtils.chainName
                        this.address = toJson.get("address").toString()
                        this.privateKey = toJson.get("privateKey").toString()
                        this.password = password
                    }
                    callback.invoke(ValueResult.success(walletDao))
                } else {
                    callback.invoke(ValueResult.error("keystore格式不对 或 密码不匹配"))
                }
            }

        })

    }

    override fun getWalletKeystore(wallet: WalletDao, password: String, callback: (result: ValueResult) -> Unit) {
        BrewChainJsUtils.exportKeystore(wallet.privateKey!!, password, object : ValueCallback<String> {
            override fun onReceiveValue(value: String?) {
                var keystore = CallJsCodeUtils.readStringJsValue(value)
                if (keystore.checkIsEmpty()) {
                    callback.invoke(ValueResult.error("导出keystore错误"))
                } else {
                    callback.invoke(ValueResult.success(keystore))
                }
            }
        })

    }

}