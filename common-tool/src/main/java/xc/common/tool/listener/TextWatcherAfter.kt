package xc.common.tool.listener

import android.text.Editable
import android.text.TextWatcher

open abstract class TextWatcherAfter : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        try {
            tryAfterTextChanged(s)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun tryAfterTextChanged(s: Editable?) {

    }
}