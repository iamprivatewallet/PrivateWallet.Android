package com.fanrong.frwallet.ui.receipt

import android.content.Context
import android.graphics.Bitmap
import com.fanrong.frwallet.R
import com.fanrong.frwallet.tools.ShareUtils
import com.yzq.zxinglibrary.EncodingUtils
import kotlinx.android.synthetic.main.share_receipt_qr_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class ShareReceiptQrDialog(context: Context) : FullScreenDialog(context) {

    lateinit var bitmap: Bitmap
    var receiptAddress:String? = ""
    var coinName:String? = ""

    override fun getContentView(): Int {
        return R.layout.share_receipt_qr_dialog

    }

    override fun initView() {
//        iv_image.setImageBitmap(bitmap)
        tv_cancel.setOnClickListener {
            dismiss()
        }
//        tv_canfirm.setOnClickListener {
//
////            val saveBitmap = LibAppUtils.saveBitmap(
////                LibAppUtils.inviteViewToBitmap(ll_share_content),
////                Environment.getExternalStorageDirectory().path + "/frwallet", "receiptqr.png"
////            )
////            LibAppUtils.shareImg(saveBitmap, ownerActivity!!)
//            ll_share_content.setDrawingCacheEnabled(true)
//            ll_share_content.buildDrawingCache()
//            val bitmap: Bitmap = Bitmap.createBitmap(ll_share_content.getDrawingCache())
//            ShareUtils.shareImage(bitmap,"分享",ownerActivity!!)
////            dismiss()
//        }

        iv_downloadqrcode.setImageBitmap(EncodingUtils.createQRCodeWithoutWhite("https://www.baidu.com", 200, 200, null))

        //iv_qrcode   转账二维码
        //iv_downloadqrcode  App下载
        //tv_url    Appp下载网站

        tv_qbdz.setText(context.resources.getString(R.string.qbdz,coinName))
        val qrcodeImg = EncodingUtils.createQRCodeWithoutWhite(receiptAddress, 400, 400, null)
        iv_qrcode.setImageBitmap(qrcodeImg)
        tv_address.setText(receiptAddress)
    }
}