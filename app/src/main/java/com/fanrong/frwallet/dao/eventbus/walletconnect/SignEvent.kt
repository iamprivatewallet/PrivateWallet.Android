package com.fanrong.frwallet.dao.eventbus.walletconnect

import org.walletconnect.Session

class SignEvent(var call: Session.MethodCall.SignMessage) {
}