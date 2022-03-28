package com.fanrong.frwallet.dapp.dappapi.error

class AccountNotFoundException : RuntimeException {
    constructor() : super("account not found")
}