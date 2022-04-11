package com.fanrong.frwallet.wallet

import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.tools.bip44Utils
import com.fanrong.frwallet.wallet.cwv.CVNWalletUtils
import com.fanrong.frwallet.wallet.eth.EthWalletUtils
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.wallet.DeterministicSeed
import org.bitcoinj.wallet.Wallet
import org.brewchain.sdk.util.WalletUtil
import org.consenlabs.tokencore.wallet.model.BIP44Util.generatePath
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import org.web3j.utils.Numeric
import xc.common.framework.bean.ValueResult


object WalletHelper {

    private val cvnWalletUtils: CVNWalletUtils by lazy {
        CVNWalletUtils()
    }

    private val ethWalletUtils: EthWalletUtils by lazy {
        EthWalletUtils("ETH")
    }
    private val hecoWalletUtils: EthWalletUtils by lazy {
        EthWalletUtils("HECO")
    }
    private val bscWalletUtils: EthWalletUtils by lazy {
        EthWalletUtils("BSC")
    }

    private fun getWalletUtils(chainname: String): IWalletUtil {

        return if ("CVN".equals(chainname)) {
            cvnWalletUtils
        } else if ("ETH".equals(chainname)) {
            ethWalletUtils
        } else if ("HECO".equals(chainname)) {
            hecoWalletUtils
        } else if ("BSC".equals(chainname)) {
            bscWalletUtils
        } else {
            throw java.lang.RuntimeException("不支持链类型")
        }
    }


    /**
     * 添加身份钱包 下主链
     */
    fun createMainWallet(chainname: String, words: String) {
        if ("CVN".equals(chainname)) {
            cvnWalletUtils.createWallet(words, "CVN") {
                if (it == null) {

                } else {
                    it!!.isMainWallet = "1"
                    it!!.password = WalletOperator.queryIdentityWallet().password
                    it!!.passwordHint = WalletOperator.queryIdentityWallet().passwordHint
                    WalletOperator.queryWallet1(it)?.delete()
                    WalletOperator.insert(it)
                }
            }
        } else {
            throw RuntimeException("不支持 ${chainname} 类型创建")
        }
    }


    fun createWalletWithWordsNoSave(
        chainname: String, walletName: String, password: String, passwordhint: String, callback: (result: ValueResult) -> Unit
    ) {
        getWalletUtils(chainname).createWalletNoSave(walletName, password, passwordhint, callback)
    }

    fun createFromWords(
        chainname: String, words: String,path:String, password: String, passwordhint: String, callback: (result: ValueResult) -> Unit
    ) {
//        getWalletUtils(chainname).createFromWords(words) {
//
//            if (it.success) {
//                var wallet = it.result as WalletDao //result里包含 地址\类型\助记词\私钥
//                wallet.password = password
//                wallet.passwordHint = passwordhint
//                wallet.isMainWallet = "0"
//                if (WalletOperator.insert(wallet)) {
//                    callback.invoke(ValueResult.success(wallet))
//                } else {
//                    callback.invoke(ValueResult.error("钱包已存在"))
//                }
//            } else {
//                callback.invoke(it)
//            }
//        }


        val pathPrivateKey = bip44Utils.importAuxiliaries(words,path)
//        val split = words.split(' ')
//        val pathPrivateKey = getPathPrivateKey(split, "m/44'/60'/0'/0/0")

        val ecKeyPair = ECKeyPair.create(pathPrivateKey)
        val publicKey = Numeric.toHexStringWithPrefix(ecKeyPair.publicKey)
        val privateKey = Numeric.toHexStringWithPrefix(ecKeyPair.privateKey)
        val address = "0x" + Keys.getAddress(ecKeyPair);

        val wallet = WalletDao(address).apply {
            this.mnemonic = words
            this.privateKey = privateKey
            this.password
            this.chainType = chainname.toUpperCase()
            this.password = password
            this.passwordHint = passwordHint
            this.isMainWallet = "1"
        }

        if (WalletOperator.insert(wallet)) {
            callback.invoke(ValueResult.success(wallet))
        } else {
            callback.invoke(ValueResult.error("钱包已存在"))
        }
    }

    fun createPrivateKey(chainname: String, privatekey: String, password: String, passwordhint: String, callback: (result: ValueResult) -> Unit) {
        getWalletUtils(chainname).createPrivateKey(privatekey, password, passwordhint, callback)
    }

    fun createFromKeystore(chainname: String, keystore: String, password: String, callback: (result: ValueResult) -> Unit) {
        getWalletUtils(chainname).createFromKeystore(keystore, password) {
            if (it.success) {
                val walletDao = it.result as WalletDao
                if (WalletOperator.checkedWalletIsExist(walletDao)) {
                    callback(ValueResult.error("钱包已存在"))
                } else {
                    walletDao.keystore = keystore
                    WalletOperator.insert(walletDao)
                    callback(ValueResult.success(it.result))
                }
            } else {
                callback(ValueResult.error(it.error))
            }

        }
    }

    fun getWalletKeystore(wallet: WalletDao, password: String, callback: (result: ValueResult) -> Unit) {
        getWalletUtils(wallet.chainType!!).getWalletKeystore(wallet, password) {
            if (it.success) {
                callback(ValueResult.success(it.result))
            } else {
                callback(ValueResult.error(it.error))
            }

        }
    }

    fun initMainWallet(name: String, password: String, passwordHint: String, callback: (Boolean) -> Unit) {
        var mnemonic = WalletUtil.getMnemonic()
        initMainWallet(mnemonic, name, password, passwordHint, callback)
    }

    fun initMainWallet(mnemonic: String, name: String, password: String, passwordHint: String, callback: (Boolean) -> Unit) {

        ethWalletUtils.createFromWords(mnemonic) {

            if (it.success) {
                var wallet = it.result as WalletDao
                wallet.identityName = name
                wallet.chainType = "ETH"
                wallet.symbol = "ETH"
                wallet.password = password
                wallet.passwordHint = passwordHint
                wallet.isMainWallet = "1"
                wallet.isCurrentWallet = "1"
                WalletOperator.insert(wallet, false)

                WalletDao(wallet.address).apply {
                    privateKey = wallet.privateKey
                    isMainWallet = "1"
                    this.mnemonic = mnemonic
                    this.password = password
                    this.passwordHint = passwordHint
                    chainType = "HECO"
                    symbol = "HT"
                    identityName = name
                    balance = "0.00"
                    WalletOperator.insert(this, false)
                }
                WalletDao(wallet.address).apply {
                    privateKey = wallet.privateKey
                    isMainWallet = "1"
                    this.mnemonic = mnemonic
                    this.password = password
                    this.passwordHint = passwordHint
                    chainType = "BSC"
                    symbol = "BNB"
                    identityName = name
                    balance = "0.00"
                    WalletOperator.insert(this, false)
                }
                callback(true)
            } else {
                callback(false)
            }
        }
    }

}