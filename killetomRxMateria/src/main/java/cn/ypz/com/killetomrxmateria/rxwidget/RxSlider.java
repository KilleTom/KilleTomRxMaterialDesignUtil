package cn.ypz.com.killetomrxmateria.rxwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

@SuppressLint("AppCompatCustomView")
public class RxSlider extends SeekBar{

    private boolean isTuch;
    private boolean isShowToast;
    private int toastType;
    private
    RxSliderToast rxSliderToast;


    public RxSlider(Context context) {
        super(context);
        setThumbOffset(0);
    }

    public RxSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        setThumbOffset(0);
    }

    public RxSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setThumbOffset(0);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN&&event.getAction()==MotionEvent.ACTION_MOVE&&isShowToast){

        }
        return super.onTouchEvent(event);
    }

}
