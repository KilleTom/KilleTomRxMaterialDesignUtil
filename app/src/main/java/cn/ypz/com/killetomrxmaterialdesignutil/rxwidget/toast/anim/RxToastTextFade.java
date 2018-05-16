package cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;

import com.zia.toastex.RxToast;

/**
 * 文字颜色渐变toast文本
 */
public class RxToastTextFade extends RxToastText {

    private int duration;
    private boolean isStart = false;
    private int color = 0;
    private ValueAnimator colorValueAnimator;

    public RxToastTextFade(Context context) {
        super(context);
    }

    @Override
    public void setDuration(int duration) {
        super.setDuration(duration);
        this.duration = duration;
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        this.color = color;
        setFabAnimationColor(RxToast.NORMAL_COLOR, color);
    }

    public void setFabAnimationColor(int startColor, int endColor) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            colorValueAnimator = ValueAnimator.ofArgb(RxToast.NORMAL_COLOR, color);
        else colorValueAnimator = ValueAnimator.ofInt(RxToast.NORMAL_COLOR, color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isStart) {
            isStart = true;
            if (colorValueAnimator == null) setColor(color);
            colorValueAnimator.addUpdateListener(animation -> {
                        RxToastTextFade.this.color = (int) animation.getAnimatedValue();
                        postInvalidate();
                    }
            );
            colorValueAnimator.setDuration(duration);
            colorValueAnimator.start();
        }
        setTextColor(color);
    }
}
