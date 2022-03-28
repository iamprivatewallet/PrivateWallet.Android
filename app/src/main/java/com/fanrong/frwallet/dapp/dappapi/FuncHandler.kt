package com.fanrong.frwallet.dapp.dappapi

interface FuncHandler {

    var method: String

    fun excute(params: Any?): Any

}