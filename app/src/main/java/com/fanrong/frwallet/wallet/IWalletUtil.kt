package com.fanrong.frwallet.wallet

import com.fanrong.frwallet.dao.database.WalletDao
import xc.common.framework.bean.ValueResult

interface IWalletUtil {

    fun createMainWallet(words: String)

    fun createWalletNoSave(
        walletName: String, password: String, passwordhint: String, callback: (result: ValueResult) -> Unit
    )

    fun createFromWords(
        words: String, callback: (result: ValueResult) -> Unit
    )

    fun createPrivateKey(privatekey: String, password: String, passwordhint: String, callback: (result: ValueResult) -> Unit)

    fun createFromKeystore(keystore: String, password: String, callback: (result: ValueResult) -> Unit)

    fun getWalletKeystore(wallet: WalletDao, password: String, callback: (result: ValueResult) -> Unit)

}