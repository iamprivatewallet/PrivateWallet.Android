package com.fanrong.frwallet.dao.data

import org.web3j.abi.datatypes.primitive.Int
import java.io.Serializable

class GasInfoBean : Serializable {
    var gasLimit: String
    var gasPrice: String
    var currentType = -2


    constructor(gasLimit: String, gasPrice: String) {
        this.gasLimit = gasLimit
        this.gasPrice = gasPrice
    }
}