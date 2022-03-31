package com.fanrong.frwallet.dao.database

import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty
import xc.common.tool.utils.extGet

object WalletOperator {
    fun queryIdentityWallet(): WalletDao {
        return LitePal.where("isMainWallet = ?", "1")
            .find(WalletDao::class.java)[0]
    }

    fun queryMainWallet(): List<WalletDao> {
        return LitePal.where("isMainWallet = ?", "1")
            .find(WalletDao::class.java)
    }
    fun queryAllWallet(): List<WalletDao> {
        return LitePal.findAll(WalletDao::class.java)
    }

    fun queryWallet(coinBeanModel: CoinDao): WalletDao {
        return LitePal.where("chainType = ? and address = ?", coinBeanModel.chain_name, coinBeanModel.sourceAddr)
            .find(WalletDao::class.java)[0]!!
    }

    fun queryWalletByChainNameAndAddress(chainName:String,address:String): WalletDao {
        return LitePal.where("chainType = ? and address = ?", chainName, address)
            .find(WalletDao::class.java)[0]!!
    }

    fun checkedWalletIsExist(wallet: WalletDao): Boolean {
        return LitePal.where("chainType = ? and address = ?", wallet.chainType, wallet.address)
            .find(WalletDao::class.java).checkNotEmpty()
    }

    fun queryWallet(wallet: WalletDao): WalletDao {
        return LitePal.find(WalletDao::class.java, wallet.id)
    }

    /**
     * 查找 是否存在重复钱包，有就返回，没有返回null
     */
    fun queryWallet1(wallet: WalletDao): WalletDao? {
        return LitePal.where("chainType = ? and address = ?", wallet.chainType, wallet.address)
            .find(WalletDao::class.java).extGet(0)
    }

    fun queryOtherWallet(): List<WalletDao> {
        return LitePal.where("isMainWallet != ?", "1")
            .find(WalletDao::class.java)
    }

    fun queryWalletWithChainType(type: String?): List<WalletDao> {
        return LitePal.where("chainType = ?", type)
            .find(WalletDao::class.java)
    }

    fun queryAddress(addr: String?): WalletDao? {
        val walletModels = LitePal.where("address like ?", addr).find(WalletDao::class.java)
        return if (walletModels.checkIsEmpty()) {
            null
        } else {
            walletModels[0]
        }
    }

    fun update(greWallet: WalletDao) {
        greWallet.update(greWallet.id)
    }

    /**
     * 插入钱包创建对应的主币信息
     */
    fun insert(greWallet: WalletDao,changeWallet:Boolean = true): Boolean {
        val where = LitePal.where("chainType=? and address=?", greWallet.chainType, greWallet.address)
            .find(WalletDao::class.java)
        if (where.checkNotEmpty()) {
            //钱包已存在 不重复添加
            return false
        }

        greWallet.isShowRmb = true
        var insert = greWallet.save()
        if (insert) {
            val maincoin = CoinDao(greWallet.chainType!!)
            maincoin.sourceWallet = "${greWallet.chainType}_${greWallet.address}"
            maincoin.sourceAddr = greWallet.address
            maincoin.save()
            if (changeWallet) {
                changeCurrentWallet(greWallet)
                EventBus.getDefault().post(CurrentWalletChange())
            }
        }
        return insert
    }

    fun reSortByChain(walletList: List<WalletDao?>){
        if (walletList != null && walletList.size > 0 && walletList.get(0) != null){
            val queryWalletWithChainType = queryWalletWithChainType(walletList.get(0)!!.chainType)

            for (item in queryWalletWithChainType){
                LitePal.delete(WalletDao::class.java,item.id)
            }

            for (wallet in walletList){
                if (wallet != null){
                    val cur_wallet = WalletDao(wallet!!.address).apply {
                        this.privateKey = wallet.privateKey
                        this.mnemonic = wallet.mnemonic
                        this.chainType = wallet.chainType
                        this.symbol = wallet.symbol
                        this.identityName = wallet.identityName
                        this.balance = "0.00"
                        this.address = wallet.address
                        this.id = wallet.id
                        this.pubKey = wallet.pubKey
                        this.walletName = wallet.walletName
                        this.isCurrentWallet = wallet.isCurrentWallet
                        this.isMainWallet = wallet.isMainWallet
                        this.isBackUp = wallet.isBackUp
                        this.password = wallet.password
                        this.passwordHint = wallet.passwordHint
                        this.isFinger = wallet.isFinger
                        this.amount = wallet.amount
                        this.keystore = keystore
                        this.sortType = wallet.sortType
                        this.isShowRmb = wallet.isShowRmb
                    }
                    cur_wallet.save()
                }
            }
        }
    }

    fun getChainMainCoin(chainType:String):String{
        if (chainType.equals("ETH")){
            return "ETH"
        }else if(chainType.equals("BSC")){
            return "BNB"
        }else if(chainType.equals("HECO")){
            return "HT"
        }else if(chainType.equals("CVN")){
            return "CVN"
        }else{
            return chainType
        }
    }


    val currentWallet: WalletDao?
        get() {
            val all = LitePal.where("isCurrentWallet = ?", "1")
                .find(WalletDao::class.java)
            return if (all.checkIsEmpty()) {
                null
            } else all[0]
        }

    fun changeCurrentWallet(itemData: WalletDao) {
        val all = LitePal.where("isCurrentWallet = ?", "1")
            .find(WalletDao::class.java)
        if (all.checkNotEmpty()) {
            for (greWalletModel in all) {
                greWalletModel!!.isCurrentWallet = "0"
                greWalletModel.save()
            }
        }
        itemData.isCurrentWallet = "1"
        itemData.save()
    }

    fun deleteWallet(wallet: WalletDao) {
        LitePal.delete(WalletDao::class.java, wallet.id)
        val queryContractAssetWithWallet = CoinOperator.queryContractAssetWithWallet(wallet)
        if (queryContractAssetWithWallet != null) {
            for (coins in queryContractAssetWithWallet) {
                CoinOperator.deleteCoin(wallet, coins)
            }
        }
    }

    fun updateBackUpTrue(wallet: WalletDao) {
        if ("1".equals(wallet.isMainWallet)) {
            val find = LitePal.where("isMainWallet = ?", "1").find(WalletDao::class.java)
            for (greWalletModel in find) {
                greWalletModel.isBackUp = "1"
                greWalletModel.save()
            }
        } else {
            // 导入钱包必须先备份
//            val find = LitePal.find(GreWalletModel::class.java, wallet.id)
//            find.isBackUp = "1"
//            find.save()
        }
    }

    fun updateIdiWalletName(name: String) {
        val find = LitePal.where("isMainWallet = ?", "1").find(WalletDao::class.java)
        for (greWalletModel in find) {
            greWalletModel.identityName = name
            greWalletModel.save()
        }
    }

}