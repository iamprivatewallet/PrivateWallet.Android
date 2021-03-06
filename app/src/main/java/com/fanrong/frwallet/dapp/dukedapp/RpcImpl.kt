package com.fanrong.frwallet.dapp.dukedapp

import android.app.Activity
import android.content.Context
import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.data.TransferDataBean
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dapp.DappTransferDialog
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.wallet.bsc.BscApi
import com.fanrong.frwallet.wallet.bsc.bscApi
import com.fanrong.frwallet.wallet.eth.eth.CallReq
import com.fanrong.frwallet.wallet.eth.eth.Eth_GetTransactionByHashReq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import me.duke.eth.browser.control.AbstractRpc
import me.duke.eth.browser.control.MetaMaskCallBack
import me.duke.eth.browser.dto.RpcReq
import me.duke.eth.browser.dto.RpcResp
import org.brewchain.core.crypto.cwv.util.BytesHelper
import org.json.JSONArray
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import xc.common.tool.CommonTool
import xc.common.viewlib.view.customview.FullScreenDialog
import java.io.IOException
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.concurrent.fixedRateTimer


class RpcImpl(context: Context,_curActivity:Activity, version:String, address:String, RpcUrl:String,privateKey:String,chainId:String,webUrl:String,addrInfo:WalletDao) : AbstractRpc {
    private val version:String = version;
    private val address:String = address;
    private val context:Context = context;
    private var web3j: Web3j = Web3j.build(HttpService(RpcUrl))
    private var RpcUrl = RpcUrl;
    private var chainId = chainId;
    private var webUrl = webUrl;
    private var addrInfo = addrInfo;
    private var _curActivity = _curActivity;


    lateinit var timer: Timer;
    private var hashReqMapList: TreeMap<String, RpcReq> = TreeMap()
    private var hashRespMapList: TreeMap<String, MetaMaskCallBack<RpcResp<*>>> = TreeMap()

    val privateKey = privateKey;

    init {
        startTimer()
    }

    override fun rpcHandler(request: RpcReq, callBack: MetaMaskCallBack<RpcResp<*>>) {
        when (request.method) {
            "eth_chainId" -> {
                callBack.callback(RpcResp(request.id?:0,request.jsonrpc?:"", chainId,null,""))
            }
            /**
             * ????????????????????????????????????(??????????????????)
             * ?????????????????????????????????????????????????????????????????????
             * ?????????????????? result??????????????????
             * ?????????????????????????????????error??????????????????
             * ?????????????????? ??????????????????????????????
             */
            "eth_accounts", "eth_requestAccounts" -> {
                Log.d("walletRpcImpl", "rpcHandler: eth_accounts")
                callBack.callback(
                    RpcResp<List<String>>(
                        request.id ?: 0,
                        request.jsonrpc ?: "",
                        arrayListOf(address),
                        null,
                        ""
                    )
                )
            }
            /**
             * ????????????????????????????????????(??????????????????)
             * ?????????????????????????????????????????????????????????????????????
             * ?????????????????? result??????null
             * ?????????????????????????????????error??????????????????
             */
            "eth_coinbase"->{
                Log.d("walletRpcImpl", "rpcHandler: eth_coinbase")
                callBack.callback(
                    RpcResp(
                        request.id ?: 0,
                        request.jsonrpc ?: "",
                        arrayListOf(address),
                        null,
                        ""
                    )
                )
            }
            /**
             * ????????????????????????????????????(??????????????????)
             * ?????????????????????????????????  ?????????0x1
             * ???????????????????????????????????????????????????Demo????????????
             */
            "net_version" -> {
                Log.d("walletRpcImpl", "rpcHandler: net_version")
                var jsonArray1 : JSONArray = JSONArray(request.params)
                callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", "0x80", null, ""))
            }
            /**
             * ????????????????????????????????????(??????????????????)
             * ?????????????????????????????????===>`MetaMask/${app_version}/Beta/Mobile`
             */
            "web3_clientVersion" -> {
                val web3ClientVersion = web3j.web3ClientVersion().send().web3ClientVersion
                Log.d("walletRpcImpl", "rpcHandler: web3_clientVersion----->>>>>>>>>"+web3ClientVersion)
                callBack.callback(
                    RpcResp(
                        request.id ?: 0,
                        request.jsonrpc ?: "",
                        web3ClientVersion,
                        null,
                        ""
                    )
                )
            }
            /**
             * ????????????????????????????????????(??????????????????)
             * ??????ETH?????????
             * ??????????????????????????????
             * ?????????????????????????????????error??????????????????
             * ???????????????????????????params["address","data"]
             */
            "eth_sign" -> {
                Log.d("walletRpcImpl", "rpcHandler: eth_sign")
//                val address = request.params[0].toString()
//                val data = request.params[1].toString()

                var jsonArray1 : JSONArray = JSONArray(request.params)

//                val sign = "**********"//????????????
//                callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", sign, null, ""))
            }
            /**
             * ????????????????????????????????????(??????????????????)
             * ??????ETH?????????
             * ??????????????????????????????
             * ?????????????????????????????????error??????????????????
             * ???????????????????????????params["address","data"]??????["data","address"]
             */
            "personal_sign"->{
                Log.d("walletRpcImpl", "rpcHandler: personal_sign")
                val fistIsAddress = isAddress(request.params[0].toString())
                val address = (if(fistIsAddress){ request.params[0]}else{ request.params[1]}).toString()
                val data = (if(fistIsAddress){ request.params[1]}else{ request.params[0]}).toString()
                var jsonArray1 : JSONArray = JSONArray()
                jsonArray1.put(address)
                jsonArray1.put(data)

                WalletUtils._CallToContract(request.id.toString(),request.jsonrpc,RpcUrl,request.method,jsonArray1, WalletUtils.IRPCRequestSuccess {
                    callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", it, null, ""))
                })
            }
            /**
             * ????????????????????????????????????(??????????????????)
             * ??????ETH?????????
             * data?????????????????????
             * {
             * type: 'object',
             * properties: {
             * types: {
             * type: 'object',
             * additionalProperties: {
             * type: 'array',
             * items: {
             * type: 'object',
             * properties: {
             * name: {type: 'string'},
             * type: {type: 'string'}
             * },
             * required: ['name', 'type']
             * }
             * }
             * },
             * primaryType: {type: 'string'},
             * domain: {type: 'object'},
             * message: {type: 'object'}
             * }
             * }
             * ????????????
             * {"types":{"EIP712Domain":[{"name":"name","type":"string"},{"name":"version","type":"string"},{"name":"chainId","type":"uint256"},{"name":"verifyingContract","type":"address"}],"Person":[{"name":"name","type":"string"},{"name":"wallet","type":"address"}],"Mail":[{"name":"from","type":"Person"},{"name":"to","type":"Person"},{"name":"contents","type":"string"}]},"primaryType":"Mail","domain":{"name":"Ether Mail","version":"1","chainId":1,"verifyingContract":"0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC"},"message":{"from":{"name":"Cow","wallet":"0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826"},"to":{"name":"Bob","wallet":"0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB"},"contents":"Hello, Bob!"}}
             * ??????????????????????????????
             * ?????????????????????????????????error??????????????????
             * ???????????????????????????params["address",{data}]
             */
            "eth_signTypedData"->{
                Log.d("walletRpcImpl", "eth_signTypedData: ------>>>>>>")
                val address = request.params[0].toString()
                val data = request.params[1] as JSONObject
                var jsonArray1 : JSONArray = JSONArray()
                jsonArray1.put(address)
                jsonArray1.put(data)

                WalletUtils._CallToContract(request.id.toString(),request.jsonrpc,RpcUrl,request.method,jsonArray1, WalletUtils.IRPCRequestSuccess {
                    callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", it, null, ""))
                })
            }
            /**
             * ????????????????????????????????????(??????????????????)
             * ??????ETH????????? V3???
             * data?????????????????????
             * {
             * type: 'object',
             * properties: {
             * types: {
             * type: 'object',
             * additionalProperties: {
             * type: 'array',
             * items: {
             * type: 'object',
             * properties: {
             * name: {type: 'string'},
             * type: {type: 'string'}
             * },
             * required: ['name', 'type']
             * }
             * }
             * },
             * primaryType: {type: 'string'},
             * domain: {type: 'object'},
             * message: {type: 'object'}
             * }
             * }
             * ????????????
             * {"types":{"EIP712Domain":[{"name":"name","type":"string"},{"name":"version","type":"string"},{"name":"chainId","type":"uint256"},{"name":"verifyingContract","type":"address"}],"Person":[{"name":"name","type":"string"},{"name":"wallet","type":"address"}],"Mail":[{"name":"from","type":"Person"},{"name":"to","type":"Person"},{"name":"contents","type":"string"}]},"primaryType":"Mail","domain":{"name":"Ether Mail","version":"1","chainId":1,"verifyingContract":"0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC"},"message":{"from":{"name":"Cow","wallet":"0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826"},"to":{"name":"Bob","wallet":"0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB"},"contents":"Hello, Bob!"}}
             * ??????????????????????????????
             * ?????????????????????????????????error??????????????????
             * ???????????????????????????params["address",{data}]
             */
            "eth_signTypedData_v3"->{
                Log.d("walletRpcImpl", "eth_signTypedData_v3: ------>>>>>>")
                val address = request.params[0].toString()
                val data = request.params[1] as JSONObject
                var jsonArray1 : JSONArray = JSONArray()
                jsonArray1.put(address)
                jsonArray1.put(data)

                WalletUtils._CallToContract(request.id.toString(),request.jsonrpc,RpcUrl,request.method,jsonArray1, WalletUtils.IRPCRequestSuccess {
                    callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", it, null, ""))
                })
            }
            /**
             * ????????????????????????????????????(??????????????????)
             * ??????ETH????????? V4???
             * data?????????????????????
             * {
             * type: 'object',
             * properties: {
             * types: {
             * type: 'object',
             * additionalProperties: {
             * type: 'array',
             * items: {
             * type: 'object',
             * properties: {
             * name: {type: 'string'},
             * type: {type: 'string'}
             * },
             * required: ['name', 'type']
             * }
             * }
             * },
             * primaryType: {type: 'string'},
             * domain: {type: 'object'},
             * message: {type: 'object'}
             * }
             * }
             * ????????????
             * {"types":{"EIP712Domain":[{"name":"name","type":"string"},{"name":"version","type":"string"},{"name":"chainId","type":"uint256"},{"name":"verifyingContract","type":"address"}],"Person":[{"name":"name","type":"string"},{"name":"wallet","type":"address"}],"Mail":[{"name":"from","type":"Person"},{"name":"to","type":"Person"},{"name":"contents","type":"string"}]},"primaryType":"Mail","domain":{"name":"Ether Mail","version":"1","chainId":1,"verifyingContract":"0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC"},"message":{"from":{"name":"Cow","wallet":"0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826"},"to":{"name":"Bob","wallet":"0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB"},"contents":"Hello, Bob!"}}
             * ??????????????????????????????
             * ?????????????????????????????????error??????????????????
             * ???????????????????????????params["address",{data}]
             */
            "eth_signTypedData_v4"->{
                Log.d("walletRpcImpl", "eth_signTypedData_v4: ------>>>>>>")
                val address = request.params[0].toString()
                val data = request.params[1].toString()
                Log.d("TAG", "rpcHandler: ffff")
                var jsonArray1 : JSONArray = JSONArray()
                jsonArray1.put(address)
                jsonArray1.put(data)


                CommonTool.mainHandler.post{
                    DappTransferDialog(context).apply {
                        this.dappUrl = webUrl
                        walletInfo = addrInfo
                        var _tokenInfo: CoinDao = CoinOperator.queryChainCoin(addrInfo!!)
                        this.tokenInfo = CoinOperator.queryChainCoin(addrInfo!!)

                        this.showType = 3
                        this.signMemo = data
                        this.isShowNode = false
                        this.receiptAddr = ""//transaction.to  ????????????
                        this.payAddr = address//addrInfo!!.address  ????????????
                        this.gasLimit = ""
                        this.gasPrice = ""//transaction.gasPrice.extHexToTen()

                        this.payAMount = ""//BytesHelper.hexStr2BigDecimal("0", 18, 4).toPlainString()

                        onConfrim = object : FullScreenDialog.OnConfirmListener {
                            override fun confirm(params: Any?) {
                                WalletUtils._CallToContract(request.id.toString(),request.jsonrpc,RpcUrl,request.method,jsonArray1, WalletUtils.IRPCRequestSuccess {
                                    callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", it, null, ""))
                                })
                            }
                        }

                        onCancel = object : FullScreenDialog.OnCancelListener {
                            override fun cancel() {
                                callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", "", null, ""))
                            }
                        }

                    }.show()
                }

            }
            //====================================
            //
            //???????????????????????????else?????????
            //
            //====================================

            /**
             * ??????????????????????????????????????????Eth rpc??????
             * ????????????Web3j ??????????????????
             * ?????????[address,blockNumber]
             * ?????????????????????eth?????? 16?????? ????????????
             */
            "eth_getBalance" -> {
                Log.d("walletRpcImpl", "eth_getBalance: ------>>>>>>")
                var jsonArray : JSONArray = JSONArray(request.params)
                var balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get().balance;
                callBack.callback(RpcResp(request.id?:0,request.jsonrpc?:"", balance,null,""))
            }
            /**
             * ??????????????????????????????????????????Eth rpc??????
             * ????????????Web3j ??????????????????
             * ????????????????????????
             */
            "eth_blockNumber" ->{
                Log.d("walletRpcImpl", "eth_blockNumber: ------>>>>>>")
                var callParams : JSONArray = JSONArray(request.params)
                WalletUtils._CallToContract(request.id.toString(),request.jsonrpc,RpcUrl,request.method,callParams, WalletUtils.IRPCRequestSuccess {
                    callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", it, null, ""))
                })
            }
            /**
             * ??????????????????????????????????????????Eth rpc??????
             * ????????????Web3j ??????????????????
             * ????????????????????????????????????/???????????????
             * result ??????????????????
             */
            "eth_call" ->{
                var callParams:JSONArray = JSONArray(request.params);
                WalletUtils._CallToContract(request.id.toString(),request.jsonrpc,RpcUrl,request.method,callParams, WalletUtils.IRPCRequestSuccess {
                    callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", it, null, ""))
                })
            }
            "eth_sendTransaction","eth_sendRawTransaction" ->{

                val transactionParams = request.params[0] as JSONObject
                val _from = transactionParams.getString("from")
                val _to = transactionParams.getString("to")
                val _data = transactionParams.getString("data")
                val _gasPrice = transactionParams.getString("gasPrice").extHexToTen()
                var _gas = transactionParams.getString("gas").extHexToTen()
                if (_gas.equals("0")){
                    val request_gas = web3j.ethEstimateGas(Transaction.createEthCallTransaction(_from,_to,_data)).send().amountUsed;
                    _gas = request_gas.toString()
                }

                var spenderAddress:String = ""
                var approveTokenAmount:String = ""
                var approveTokenAddress:String = ""
                if (_data.startsWith("0x095ea7b3")){
                    val substring = _data.substring(10).substring(0,64);
                    spenderAddress = org.web3j.abi.datatypes.Address(substring).toString();

                    val amount_str = _data.substring(74)

                    if (amount_str.equals("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")){
                        approveTokenAmount = context.getString(R.string.wxz)
                    }else{
                        approveTokenAmount = "0x"+amount_str.extHexToTen()
                    }

                    approveTokenAddress = _to
                }


                var _value = "0"
                if (transactionParams.getString("value") != null && transactionParams.getString("value")!= ""){
                    _value = BytesHelper.hexStr2BigDecimal(transactionParams.getString("value"), 18, 4).toPlainString()
                }

                CommonTool.mainHandler.post{
                    DappTransferDialog(context).apply {
                        this.dappUrl = webUrl
                        walletInfo = addrInfo
                        var _tokenInfo: CoinDao = CoinOperator.queryChainCoin(addrInfo!!)
                        this.tokenInfo = CoinOperator.queryChainCoin(addrInfo!!)

                        if (_data.startsWith("0x095ea7b3")){
                            this.showType = 2
                        }else{
                            this.showType = 1
                        }
                        this.approveTokenAmount = approveTokenAmount
                        this.approveFillData = _data
                        this.cur_activity = _curActivity
                        this.approveTokenAddress = approveTokenAddress

                        this.isShowNode = false
                        this.approveSpender = spenderAddress
                        this.receiptAddr = _to//transaction.to  ????????????
                        this.payAddr = _from//addrInfo!!.address  ????????????
                        this.gasLimit = _gas
                        this.gasPrice = _gasPrice//?????????0????????????10????????????

                        this.payAMount = _value//BytesHelper.hexStr2BigDecimal("0", 18, 4).toPlainString()

                        onConfrim = object : FullScreenDialog.OnConfirmListener {
                            override fun confirm(params: Any?) {
                                val transferDataBean = params as TransferDataBean
                                if (transferDataBean!=null){
                                    transactionParams.set("data",transferDataBean.transferData)

                                    val rpcReq = RpcReq()
                                    rpcReq.id = request.id
                                    rpcReq.isToNative = request.isToNative
                                    rpcReq.jsonrpc = request.jsonrpc
                                    rpcReq.method = request.method
                                    rpcReq.params = mutableListOf()
                                    rpcReq.params.add(transactionParams)

                                    signAndTrans(_tokenInfo,rpcReq,_value,callBack,_from,_to,transferDataBean.gasInfo!!.gasPrice,transferDataBean.gasInfo!!.gasLimit)
                                }else{
                                    signAndTrans(_tokenInfo,request,_value,callBack,_from,_to,_gasPrice,_gas)
                                }
                            }
                        }

                        onCancel = object : FullScreenDialog.OnCancelListener {
                            override fun cancel() {
                                callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", "", null, ""))
                            }
                        }

                    }.show()
                }

            }
            "eth_getTransactionReceipt" ->{
                val TransactionReceipt = web3j.ethGetTransactionReceipt(request.params[0].toString()).send()
                if (TransactionReceipt.result != null){
                    //??????,????????????
                    Log.d("walletRpcImpl2311", "eth_getTransactionReceipt result: ----->>>>>>>"+TransactionReceipt.result)
                    callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", TransactionReceipt.result, null, ""))
                }else{
                    //???????????????
                    hashReqMapList.put(request.params[0].toString(),request)
                    hashRespMapList.put(request.params[0].toString(),callBack)
                }

            }
            "eth_estimateGas" -> {
//                val jsonObject = request.params[0] as JSONObject
//                val _from = jsonObject.getString("from")
//                val _to = jsonObject.getString("to")
//                val _data = jsonObject.getString("data")
//                val result = web3j.ethEstimateGas(Transaction.createEthCallTransaction(_from,_to,_data)).send().result
//                callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", result, null, ""))
                var callParams:JSONArray = JSONArray(request.params);
                WalletUtils._CallToContract(request.id.toString(),request.jsonrpc,RpcUrl,request.method,callParams, WalletUtils.IRPCRequestSuccess {
                    callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", it, null, ""))
                })
            }
            else ->{
                Log.d("walletRpcImpl", "rpcHandler: else---->>>>>>>"+request.method)
                //TODO ????????????Method ??????????????????web3???rpc?????????????????????????????????????????????
                var callParams:JSONArray = JSONArray(request.params);
                WalletUtils._CallToContract(request.id.toString(),request.jsonrpc,RpcUrl,request.method,callParams, WalletUtils.IRPCRequestSuccess {
                    callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", it, null, ""))
                })
            }
        }
    }

    fun checkReceiptIsSuccess(){
        Log.d("walletRpcImpl231122", "hashMapList.size0: ---->>>>>>>>>"+hashReqMapList.size)
        for ((address,requ) in hashReqMapList){
            Log.d("walletRpcImpl231122", "checkReceiptIsSuccess: ----->>>>>>>>"+address)
            val TransactionReceipt = web3j.ethGetTransactionReceipt(address).send()
            if (TransactionReceipt!=null){
                Log.d("walletRpcImpl231122", "hashMapList.size1: ---->>>>>>>>>"+hashReqMapList.size)
                hashRespMapList.get(address)?.callback(RpcResp(requ.id ?: 0, requ.jsonrpc ?: "", TransactionReceipt.result, null, ""))
                hashReqMapList.remove(address)
                hashRespMapList.remove(address)
                Log.d("walletRpcImpl231122", "checkReceiptIsSuccess: ---->>>>>>>>>"+TransactionReceipt.result)
                Log.d("walletRpcImpl231122", "hashMapList.size2: ---->>>>>>>>>"+hashReqMapList.size)
            }
        }
    }

    fun  startTimer(){
        timer = fixedRateTimer("",false,0,3000){
            checkReceiptIsSuccess()
        }
    }

    fun stopTimer(){
        timer.cancel();
    }


    private fun balanceOf(owner: String,functionName:String): Function {
        return Function(
            functionName, listOf(Address(address)), listOf(object : TypeReference<Uint256?>() {}))
    }

    private fun getFunctionByName(owner: String,functionName:String): Function {
        return Function(
            functionName, listOf(Address(owner)), listOf(object : TypeReference<Uint256?>() {}))
    }

    @Throws(Exception::class)
    private fun callSmartContractFunction(function: Function, contractAddress: String, owner:String): String? {
        val encodedFunction = FunctionEncoder.encode(function)
        val response = web3j.ethCall(
            Transaction.createEthCallTransaction(owner, contractAddress, encodedFunction),
            DefaultBlockParameterName.LATEST)
            .sendAsync().get()
        return response.result
    }

    /**
     * ?????????????????????
     */
    private fun isAddress(data:String):Boolean{
        return data.length == 2 + 20 * 2
    }

    fun signAndTrans(tokenInfo:CoinDao,request: RpcReq,transAmount:String,callBack: MetaMaskCallBack<RpcResp<*>>,_from:String,_to:String,gasPrice:String,gasLimit:String){
        Thread({
            val nonce = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().transactionCount

            val credentials : Credentials = Credentials.create(privateKey)

            var gas_price:BigInteger
            if (gasPrice!="" && gasPrice.toDouble() != 0.0){
                val extGwei2Wei = gasPrice.extGwei2Wei().extKillPointAfterZero()
                gas_price = BigInteger(extGwei2Wei)
            }else{
                gas_price = web3j.ethGasPrice().send().gasPrice
            }
            var gas_limit:BigInteger
            if (gasLimit!=""&&gasLimit.toDouble() != 0.0){
                gas_limit = BigInteger(gasLimit)
            }else{
                gas_limit = DefaultGasProvider.GAS_LIMIT
            }

            val jsonObject = request.params[0] as JSONObject
            jsonObject.set("gas",gas_limit.toString().extTen2Hex())
            jsonObject.set("gasPrice",gas_price.toString().extTen2Hex())

            val rawTransaction = RawTransaction.createTransaction(nonce,gas_price, gas_limit, jsonObject.getString("to"),getAmountToWei(
                BigDecimal(transAmount),18), jsonObject.getString("data"))
            val chain_decimal = BytesHelper.hexStr2BigDecimal(chainId,0,0)
            val chain_long = chain_decimal.toLong()
            val signedMessage = TransactionEncoder.signMessage(rawTransaction,chain_long,credentials)
            val hexValue = Numeric.toHexString(signedMessage)
            val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send()

            LocalNoceOperator.save(nonce.toString(), WalletOperator.queryWallet(tokenInfo), ChainNodeOperator.queryCurrentNode(tokenInfo.chain_name!!))

            TransferOperator.save(
                addrInfo.chainType!!,
                _from,
                if (tokenInfo != null) tokenInfo.coin_name else "UNKNOW",
                _to,//?????????
                ethSendTransaction.result,
                nonce.toString(),
                BytesHelper.hexStr2BigDecimal("0", 18, 4).toPlainString(),//?????????hexvalue
                "",
                System.currentTimeMillis().toString()
            )

            CommonTool.mainHandler.post{
                callBack.callback(RpcResp(request.id ?: 0, request.jsonrpc ?: "", ethSendTransaction.result, null, ""))
            }
        }).start()
    }

    fun getAmountToWei(amount: BigDecimal?, factor: Int?): BigInteger? {
        val unit: Convert.Unit
        unit = when (factor) {
            3 -> Convert.Unit.KWEI
            6 -> Convert.Unit.MWEI
            9 -> Convert.Unit.GWEI
            12 -> Convert.Unit.SZABO
            15 -> Convert.Unit.FINNEY
            18 -> Convert.Unit.ETHER
            21 -> Convert.Unit.KETHER
            24 -> Convert.Unit.METHER
            27 -> Convert.Unit.GETHER
            else -> Convert.Unit.WEI
        }
        return Convert.toWei(amount, unit).toBigInteger()
    }
}