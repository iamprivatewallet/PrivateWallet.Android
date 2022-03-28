package com.fanrong.frwallet.dapp.dappapi.error

class TokenNotFoundException : RuntimeException {
    constructor() : super("token not found")
}