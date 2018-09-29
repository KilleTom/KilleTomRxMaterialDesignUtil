package cn.ypz.com.killetomrxmateria.rxwidget.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import cn.ypz.com.killetomrxmateria.R;
import cn.ypz.com.killetomrxmateria.rxwidget.RxDimenUtils;
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseView;

public class RxEasyArcLoading extends BaseView {
    private Paint arc0FP = new Paint();
    private Paint arc1FP = new Paint();
    private Paint arc2FP = new Paint();
    private Paint arc3FP = new Paint();
    private int dp50 = RxDimenUtils.dpToPx(50, getResources());
    private int dp10 = RxDimenUtils.dpToPx(10, getResources());
    private float multiple;
    private int arc0_f, arc1_f, arc2_f, arc3_f;
    private int arc0_s, arc1_s, arc2_s, arc3_s;
    private RectF oval;
    private int w;          //View宽高
    private int h;
    private float r;
    private int mini;

    private ValueAnimator arc0Animator = ValueAnimator.ofInt(0, 360);
    private ValueAnimator arc1Animator = ValueAnimator.ofInt(0, 360);
    private ValueAnimator arc2Animator = ValueAnimator.ofInt(0, 360);
    private ValueAnimator arc3Animator = ValueAnimator.ofInt(0, 360);

    public RxEasyArcLoading(Context context) {
        super(context);
    }

    public RxEasyArcLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paintSetColorId(arc0FP, R.color.deepskyblue);
        paintSetColorId(arc1FP, R.color.goldenrod);
        paintSetColorId(arc2FP, R.color.crimson);
        paintSetColorId(arc3FP, R.color.violet);
        reset();
        arc1Animator.addUpdateListener(animation -> {
            arc1_f = (int) animation.getAnimatedValue() + 31;
            arc2_s = (int) animation.getAnimatedValue() - 127;
        });
        arc2Animator.addUpdateListener(animation -> {
            arc2_f = (int) animation.getAnimatedValue() - 127;
            arc3_s = (int) animation.getAnimatedValue() + 100;
        });
        arc3Animator.addUpdateListener(animation -> {
            arc3_f = (int) animation.getAnimatedValue() + 100;
            arc0_s = (int) animation.getAnimatedValue() - 26;
        });
        arc0Animator.addUpdateListener(animation -> {
            arc0_f = (int) animation.getAnimatedValue() - 26;
            arc1_s = (int) animation.getAnimatedValue() + 31;
            postInvalidate();
        });
        arc0Animator.setDuration(3200);
        arc0Animator.setRepeatCount(-1);
        arc0Animator.setRepeatMode(ValueAnimator.RESTART);
        arc0Animator.setInterpolator(new LinearInterpolator());
        arc1Animator.setDuration(2850);
        arc2Animator.setDuration(2300);
        arc3Animator.setDuration(1950);
        arc1Animator.setRepeatCount(-1);
        arc2Animator.setRepeatCount(-1);
        arc3Animator.setRepeatCount(-1);
        arc1Animator.setInterpolator(new LinearInterpolator());
        arc2Animator.setInterpolator(new LinearInterpolator());
        arc3Animator.setInterpolator(new LinearInterpolator());
        oval = new RectF();
        setPaint(arc0FP);
        setPaint(arc1FP);
        setPaint(arc2FP);
        setPaint(arc3FP);
    }

    private void setPaint(Paint paint) {
        paint.setAntiAlias(true);//使用抗锯齿功能
        paint.setStyle(Paint.Style.STROKE);//
        if (multiple > 1) paint.setStrokeWidth(dp10 * multiple / 5);
        else paint.setStrokeWidth(dp10 / 4);
        r = paint.getStrokeWidth();
    }

    protected void reset() {
        arc0_s = arc0_f = -26;
        arc1_s = arc1_f = 31;
        arc2_s = arc2_f = -127;
        arc3_s = arc3_f = 100;
    }

    protected void stop() {
        if (arc0Animator.isRunning()) arc0Animator.pause();
        if (arc1Animator.isRunning()) arc1Animator.pause();
        if (arc2Animator.isRunning()) arc2Animator.pause();
        if (arc3Animator.isRunning()) arc3Animator.pause();
    }

    protected void reStart() {
        if (arc0Animator.isPaused()) arc0Animator.resume();
        if (arc1Animator.isPaused()) arc0Animator.resume();
        if (arc2Animator.isPaused()) arc0Animator.resume();
        if (arc3Animator.isPaused()) arc0Animator.resume();
    }

    public void cancle() {
        if (arc0Animator.isStarted()) arc0Animator.cancel();
        if (arc1Animator.isStarted()) arc1Animator.cancel();
        if (arc2Animator.isStarted()) arc2Animator.cancel();
        if (arc3Animator.isStarted()) arc3Animator.cancel();
    }

    public void start() {
        if (!arc0Animator.isStarted()) arc0Animator.start();
        if (!arc1Animator.isStarted()) arc1Animator.start();
        if (!arc2Animator.isStarted()) arc2Animator.start();
        if (!arc3Animator.isStarted()) arc3Animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("ypzLoading", widthMeasureSpec + "\n" + dp50);
        if (isSetDimen(widthMeasureSpec) || widthMeasureSpec < dp50) {
            widthMeasureSpec = dp50;
        }
        if (isSetDimen(heightMeasureSpec) || heightMeasureSpec < dp50) {
            heightMeasureSpec = dp50;
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        mini = Math.min(w,h);
        multiple = Math.min(w, h) / dp50;
        setPaint(arc0FP);
        setPaint(arc1FP);
        setPaint(arc2FP);
        setPaint(arc3FP);

    }

    @Override
    protected void initAttr(Context context, @Nullable AttributeSet attrs) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(w / 2, h / 2);//绘画点移动至中心点
        int i = 1;
        for (int nextR=mini/2-dp10;nextR>0; nextR-=r){
            oval.set(-nextR, -nextR, nextR, nextR);
            if (i%2==0){
                canvas.drawArc(oval, arc0_s, 52, false, arc0FP);
                canvas.drawArc(oval, arc1_s, 64, false, arc1FP);
                canvas.drawArc(oval, arc2_s, 96, false, arc2FP);
                canvas.drawArc(oval, arc3_s, 128, false, arc3FP);
            }else {
                canvas.drawArc(oval, arc0_f, 52, false, arc0FP);
                canvas.drawArc(oval, arc1_f, 64, false, arc1FP);
                canvas.drawArc(oval, arc2_f, 96, false, arc2FP);
                canvas.drawArc(oval, arc3_f, 128, false, arc3FP);
            }
            i+=1;
        }
    }
}
