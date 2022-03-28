package me.duke.eth.browser.control

import me.duke.eth.browser.dto.Event
import me.duke.eth.browser.dto.PublicConfig
import me.duke.eth.browser.utlis.JsonObject
import me.duke.eth.browser.widget.Web3View
import org.json.JSONObject

class MetaMaskControl(
    var mWeb3View: Web3View?,
    var url: String,
    private val isFrame: Boolean
) : OnConfigStateChangeListener {

    fun postMessage(obj: Event) {
        val json = JSONObject()
        val data = JSONObject(JsonObject.toJSONString(obj))
        json.put("target", "inpage")
        json.put("data", data)
        if (isFrame) {
            mWeb3View?.callHandler(
                "emitIFrames",
                arrayOf(json, url)
            )
        } else {
            mWeb3View?.callHandler(
                "emit",
                arrayOf(json, url)
            )
        }
    }

    override fun onChange() {
        postMessage(PublicConfig(AdapterWallet.getInstance().configStore))
    }

}