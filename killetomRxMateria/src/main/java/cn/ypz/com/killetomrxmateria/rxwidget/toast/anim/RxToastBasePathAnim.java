package cn.ypz.com.killetomrxmateria.rxwidget.toast.anim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;

import java.util.Iterator;
import java.util.List;

/**
 * 通过绘制path制作动画图标
 */
public abstract class RxToastBasePathAnim extends RxToastIcon {

    private Path rightDstPath;
    private PathMeasure mPathMeasure;
    private float percentage = 0;
    private boolean isStart = false;
    private int duration = 850;
    private Paint paint;
    private int color = -1000;

    abstract List<Path> getPaths();

    abstract void setPaint(Paint paint);

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    public RxToastBasePathAnim(Context context) {
        super(context);
        init();
    }

    public RxToastBasePathAnim(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxToastBasePathAnim(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        paint = new Paint();
        rightDstPath = new Path();
        mPathMeasure = new PathMeasure();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isStart) {
            setPaint(paint);
            if (color != -1000) {
                paint.setColor(color);
            }
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(animation -> {
                percentage = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            valueAnimator.setDuration(duration).start();
            isStart = true;
        }
        Iterator<Path> iterator =  getPaths().iterator();
        while (iterator.hasNext()){
            mPathMeasure.setPath(iterator.next(), false);
            mPathMeasure.getSegment(0, mPathMeasure.getLength() * percentage, rightDstPath, true);
            canvas.drawPath(rightDstPath, paint);
        }
    }

}
