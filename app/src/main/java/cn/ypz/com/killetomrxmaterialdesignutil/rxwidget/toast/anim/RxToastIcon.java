package cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 声明图标icon用来制作自定义Toast的图标
 */
public class RxToastIcon extends android.support.v7.widget.AppCompatImageView implements RxToastBaseAnimation {

    public RxToastIcon(Context context) {
        super(context);
    }

    public RxToastIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RxToastIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setDuration(int duration) {

    }

    @Override
    public void setColor(int color) {

    }
}
