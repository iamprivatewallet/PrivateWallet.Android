package com.fanrong.frwallet.dao.eventbus.walletconnect

import org.walletconnect.Session

class TransactionEvent(var call: Session.MethodCall.SendTransaction) {
}