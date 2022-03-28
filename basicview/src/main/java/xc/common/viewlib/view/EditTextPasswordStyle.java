package xc.common.viewlib.view;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * 修改edittext密码样式
 */
public class EditTextPasswordStyle extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }
}
