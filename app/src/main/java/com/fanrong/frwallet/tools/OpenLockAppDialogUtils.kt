package com.fanrong.frwallet.tools

import android.content.Context
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.dialog.PasswordDialog
import com.fanrong.frwallet.ui.login.LoginActivity
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.AppManager
import xc.common.viewlib.view.customview.FullScreenDialog

object OpenLockAppDialogUtils {
    var dialogStateIsOpen:Boolean = false
    var isNeedShow:Boolean = false
    public fun OpenDialog(context: Context){
        if (!dialogStateIsOpen && isNeedShow){
            LockAppDialog(context).apply {
                onConfrim = object :
                    FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        dialogStateIsOpen = false
                        isNeedShow = false
                    }
                }
                onCancel = object : FullScreenDialog.OnCancelListener {
                    override fun cancel() {
                        dialogStateIsOpen = false
                        isNeedShow = false
                    }
                }
            }.show()
            dialogStateIsOpen = true
        }
    }

}