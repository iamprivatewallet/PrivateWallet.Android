package xc.common.viewlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class PatchedTextView extends TextView {
    public PatchedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public PatchedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PatchedTextView(Context context) {
        super(context);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }catch (ArrayIndexOutOfBoundsException e){
            setText(getText().toString());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
    @Override
    public void setGravity(int gravity){
        try{
            super.setGravity(gravity);
        }catch (ArrayIndexOutOfBoundsException e){
            setText(getText().toString());
            super.setGravity(gravity);
        }
    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        try{
            super.setText(text, type);
        }catch (ArrayIndexOutOfBoundsException e){
            setText(text.toString());
        }
    }
}