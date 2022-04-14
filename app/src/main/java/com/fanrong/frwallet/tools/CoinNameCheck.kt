package com.fanrong.frwallet.tools

import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dapp.dappapi.error.AccountNotFoundException

object CoinNameCheck {
    fun getNameByName(name:String?):String{
        if (name.equals("BSC")){
            return "BNB"
        }else if(name.equals("HECO")){
            return "HT"
        }
        else if(name.equals("CVN")){
            return "CVNT"
        }
        else{
            return name.toString()
        }
    }
    fun getCoinImgUrl(contractAddress:String?):String{
        var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包
        if (contractAddress!=null&&contractAddress!="")
            return "https://privatewallet.s3.ap-southeast-1.amazonaws.com/icon/"+walletInfo.chainType!!.toLowerCase()+"/"+getMainCoinAddress(contractAddress,walletInfo.chainType!!)+".png"
        else
            return getCoinImgUrl2(walletInfo.chainType!!)
    }
    fun getCoinImgUrl2(name:String):String{
        return "https://privatewallet.s3.ap-southeast-1.amazonaws.com/icon/"+name.toLowerCase()+"/"+getMainCoinAddress("",name)+".png"
    }
    fun getMainCoinAddress(address:String,chainType:String):String{
        if (address.equals("")){
            if (chainType.equals("ETH")){
                return chainType
            }else if(chainType.equals("BSC")){
                return "BNB"
            }else if(chainType.equals("HECO")){
                return "HT"
            }else if(chainType.startsWith("CVN")){
                return "CVNT"
            }
        }

        return address
    }

    fun getMainCoinName():String{
        var addrInfo = WalletOperator.currentWallet!!
        if (addrInfo == null) {
            throw AccountNotFoundException()
        }

        if (addrInfo.chainType!!.equals("BSC")){
            return "BNB"
        }else if(addrInfo.chainType!!.equals("HECO")){
            return "HT"
        }
        else if (addrInfo.chainType!!.equals("CVN")){
            return "CVNT"
        }
        else{
            return addrInfo.chainType!!
        }

    }
    fun getCurrentNetID():String{
        var addrInfo = WalletOperator.currentWallet!!
        if (addrInfo == null) {
            return "1"
        }

        if (addrInfo.chainType!!.equals("BSC")){
            return "56"
        }else if(addrInfo.chainType!!.equals("HECO")){
            return "128"
        }else if(addrInfo.chainType!!.equals("CVN")){
            return "168"
        }else{
            return "1"
        }
    }
    fun getNetWorkNameByID(workId:String):String{
        if (workId.equals("1")){
            return "ETH"
        }else if(workId.equals("56")){
            return "BSC"
        }else if(workId.equals("128")){
            return "HECO"
        }else if(workId.equals("168")){
            return "CVN"
        }
        else{
            return ""
        }
    }

    fun getChainIdByName(type:String):String{
        if (type.toUpperCase().equals("ETH")){
            return "1"
        }else if (type.toUpperCase().equals("BSC")){
            return "56"
        }else if (type.toUpperCase().equals("HECO")){
            return "128"
        }else if (type.toUpperCase().equals("CVN")){
            return "168"
        }else{
            return "1"
        }
    }
}