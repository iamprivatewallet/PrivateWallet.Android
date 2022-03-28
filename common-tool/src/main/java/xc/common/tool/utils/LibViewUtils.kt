package xc.common.tool.utils

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.TypedValue
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import xc.common.tool.listener.TextWatcherAfter


object LibViewUtils {

    fun getScreenInfo(context: Context): Pair<Int, Int> {
        val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width: Int = wm.getDefaultDisplay().getWidth()
        val height: Int = wm.getDefaultDisplay().getHeight()
        return Pair(width, height)
    }

    fun handlerEditTextTwoDecimal(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcherAfter() {

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().checkNotEmpty()
                    && s.toString().contains(".")
                    && s.toString().indexOf(".") + 3 < s!!.length
                ) {
                    editText.setText(s.toString().substring(0, s.toString().indexOf(".") + 3))
                    editText.setSelection(editText.text.length)
                }
            }
        })
    }


    /**
     * 自动更新 edittext  清空文本图标
     */
    fun updateEditTextClearIcon(editText: EditText, icon: ImageView) {
        icon.setOnClickListener {
            editText.setText("")
        }
        editText.addTextChangedListener(object : TextWatcherAfter() {

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().checkNotEmpty()) {
                    icon.visibility = View.VISIBLE
                } else {
                    icon.visibility = View.GONE
                }
            }
        })

    }

    /**
     * 监听所有输入框，当所有输入框都不为空的情况下 button 激活，editText 可以传多个
     */
    fun updateBtnStatus(button: Button, vararg editTexts: EditText) {
        if (editTexts == null) {
            return
        }

        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcherAfter() {
                override fun afterTextChanged(s: Editable?) {
                    button.isEnabled = notEmptyContent(*editTexts)
                }
            })
        }
    }

    /**
     * 判断输入框是否为空，可以传多个
     */
    fun notEmptyContent(vararg editTexts: EditText): Boolean {
        if (editTexts.size == 0) {
            return false
        }
        for (editText in editTexts) {
            if (editText.text.toString().checkIsEmpty()) {
                return false
            }
        }
        return true
    }


    fun changeLineBg(editText: EditText, view: View) {
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                view.setBackgroundColor(Color.parseColor("#FFD6302D"))
            } else {
                view.setBackgroundColor(Color.parseColor("#FFEDEDED"))
            }
        }
    }

    fun setViewVisibleOrGone(view: View, isVisible: Boolean) {
        if (isVisible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    fun setViewVisibleOrInvisible(view: View, isVisible: Boolean) {
        if (isVisible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }


    fun setTextHintAndInputSize(textView: TextView, hintSize: Int, textSize: Int) {
        textView.addTextChangedListener(object : TextWatcherAfter() {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().checkIsEmpty()) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, hintSize.toFloat())
                } else {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
                }
            }
        })
    }


    fun setEditChange(vararg editTexts: EditText, onChange: () -> Unit) {
        if (editTexts == null) {
            return
        }

        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcherAfter() {
                override fun afterTextChanged(s: Editable?) {
                    onChange?.invoke()
                }
            })
        }
    }

    fun disableInput(vararg editTexts: EditText) {
        if (editTexts == null) {
            return
        }
        for (editText in editTexts) {
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
        }
    }

    fun enbleInput(vararg editTexts: EditText) {
        if (editTexts == null) {
            return
        }
        for (editText in editTexts) {
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
        }
    }

    /**
     * 禁止输入框复制粘贴菜单
     */
    fun disableCopyAndPaste(editText: EditText?) {
        try {
            if (editText == null) {
                return
            }
            //处理长按事件
            editText.setOnLongClickListener(View.OnLongClickListener { true })
            //禁用长按
            editText.isLongClickable = false
            editText.setTextIsSelectable(false)
            //禁用ActonMode弹窗
            editText.customSelectionActionModeCallback = object :
                ActionMode.Callback {
                override fun onCreateActionMode(
                    mode: ActionMode?,
                    menu: Menu?
                ): Boolean {
                    return false
                }

                override fun onPrepareActionMode(
                    mode: ActionMode?,
                    menu: Menu?
                ): Boolean {
                    return false
                }

                override fun onActionItemClicked(
                    mode: ActionMode?,
                    item: MenuItem?
                ): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode) {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun editLenghtByte(editText: EditText, length: Int) {
        editText.addTextChangedListener(object : TextWatcherAfter() {
            override fun tryAfterTextChanged(s: Editable?) {
                super.tryAfterTextChanged(s)
                if (XcStringUtils.getBytesLenght(s.toString()) > length * 2) {
                    var str = s.toString().removeRange(s.toString().length - 1, s.toString().length)
                    editText.setText(str)
                    editText.setSelection(editText.text.length)
                }
            }
        })

    }

    fun setViewTwoState(
        view: View, onChangeState1: (v: View) -> Unit, onChangeState2: (v: View) -> Unit
    ) {
        if (view.tag != null) {
            throw RuntimeException("view 不应该自己设置tag ， 这样是不支持的")
        }

        view.tag = "1"
        view.setOnClickListener {
            view.tag = if ("1".equals(view.tag)) "2" else "1"

            if ("1".equals(view.tag)) {
                onChangeState1(view)
            } else {
                onChangeState2(view)
            }
        }


    }


}