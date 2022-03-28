package xc.common.viewlib.view;

import androidx.annotation.NonNull;

public class PasswordCharSequence implements CharSequence{
    private CharSequence mSource;

    public PasswordCharSequence(CharSequence source) {
        mSource = source; // Store char sequence
    }

    @Override
    public int length() {
        return mSource.length();
    }

    @Override
    public char charAt(int i) {
//        return '●';
//        return '•●·';
        return '•';
    }

    @NonNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return mSource.subSequence(start, end);
    }
}
