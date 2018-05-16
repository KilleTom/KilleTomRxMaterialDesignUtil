package cn.ypz.com.killetomrxmateria.rxwidget.toast.anim;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 声明文本用来制作自定义Toast的文本信息
 * 可以与RxToastIcon进行组合
 */
public class RxToastText extends android.support.v7.widget.AppCompatTextView implements RxToastBaseAnimation {
    public RxToastText(Context context) {
        super(context);
    }

    public RxToastText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RxToastText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setDuration(int duration) {

    }

    @Override
    public void setColor(int color) {

    }
}
