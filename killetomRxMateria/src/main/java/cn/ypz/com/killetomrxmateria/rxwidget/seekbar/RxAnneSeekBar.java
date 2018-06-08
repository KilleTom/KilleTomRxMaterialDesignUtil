package cn.ypz.com.killetomrxmateria.rxwidget.seekbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cn.ypz.com.killetomrxmateria.R;
import cn.ypz.com.killetomrxmateria.rxwidget.RxDimenUtils;

/**
 * @author ChineseName 易庞宙 EnglishName  kingadmin  OR killeTom
 */
public class RxAnneSeekBar extends View {
    /**
     * 初始化各类画笔属性
     *
     * @value textPaint 文字画笔
     * @value thumbPaint 指示器画笔
     * @value averagePaint 进度点画笔
     * @value readyPaint 准备进度画笔
     * @value progressPaint 进度条画笔
     * @value shapePaint AnneToast中不管选择什么显示类型都只使用这个画笔绘制出来
     */
    private TextPaint textPaint = new TextPaint(Paint.LINEAR_TEXT_FLAG);
    private Paint thumbPaint = new Paint();
    /* private Paint averagePaint = new Paint();*/
    private Paint readyPaint = new Paint();
    private Paint progressPaint = new Paint();
    private Paint shapePaint = new Paint();
    /**
     * 尺寸大小
     *
     * @value dp10 将10dp转化为屏幕大小长度
     * @value anneToastWidth AnneToast的宽度
     * @value anneToastHeight AnneToast的高度
     * @value progressRadius 进度条圆角弧度
     * @value thumbRadius 指示器圆角弧度 假如选择圆点指示器那么该弧度则为圆点半径宽度上的一部分 如果选择的是圆角矩形那么则是该矩形的圆角弧度
     * @value halfDp5 averageIndicator 大小参数
     * @value top 进度条的顶部坐标
     * @value bottom 进度条底部坐标
     */
    private float dp10 = RxDimenUtils.dpToPx(10, getResources());
    private float anneToastWidth, anneToastHeight;
    private float toastRadius = RxDimenUtils.dpToPx(3, getResources());
    private float progressRadius = dp10 / 5;
    private float thumbRadius = dp10 / 2;
    private float halfThumbRadius = thumbRadius / 2;
    private float halfDp5 = thumbRadius / 1.25F;
    private float top, bottom;

    /**
     * Toast属性
     * 设置toast样式的字体大小 xml属性<attr name="anneToastTextSize" format="dimension"/>
     * 设置圆角大小 xml属性<attr name="anneToastRadius" format="integer"/>
     *
     * @value toastShowWay 选择toast显示位置的居中或者下部显示或者上部显示xml属性如下
     * <attr name="anneToastShowWay" format="integer">
     * <enum name="above" value="0"/>
     * <enum name="below" value="1"/>
     * <enum name="center" value="2"/>
     * </attr>
     * @value isAnneShow 判断toast是否显示
     * @value isShowToast 设置是否显示toast <attr name="anneIsShowToast" format="boolean"/>
     * @value toastShape toast样式 xml属性如下
     * <attr name="anneToastShape" format="integer">
     * <enum name="RoundedRectangleIndicator" value="0"/>
     * <enum name="CircleIndicator" value="1"/>
     * </attr>
     * @value toastTextTag toastText显示面前带字符串的标记 设置了带标记就会变成像这样 我是（tag）+进度信息 xml属性如下
     * <attr name="anneToastTextTag" format="string"/>
     * @value toastShapeColor toast样式的颜色 xml属性<attr name="anneToastShapeColor" format="color"/>
     * @value toastTextColor  toast字体的颜色 xml属性<attr name="anneToastTextColor" format="color"/>
     */
    private boolean isAnneShow = false;
    private boolean isShowToast = false;
    private int toastShowWay = 0;
    private int toastShape = 0;
    private String toastTextTag = "";
    private int toastShapeColor = R.color.peru;
    private int toastTextColor = R.color.palegoldenrod;
    private AnneToast anneToast;
    private WindowManager windowManager;
    private WindowManager.LayoutParams mLayoutParams;

    public void setShowToast(boolean showToast) {
        isShowToast = showToast;
    }

    public void setToastTextTag(String toastTextTag) {
        this.toastTextTag = toastTextTag;
    }

    public void setToastShapeColor(int toastShapeColor) {
        this.toastShapeColor = toastShapeColor;
        shapePaint.setColor(toastShapeColor);
        if (anneToast != null && isAnneShow && isShowToast) anneToast.invalidate();
    }

    public void setToastTextColor(int toastTextColor) {
        this.toastTextColor = toastTextColor;
        textPaint.setColor(toastTextColor);
        if (anneToast != null && isAnneShow && isShowToast) anneToast.invalidate();
    }

    /**
     * 进度属性
     *
     * @value readyColor 进度条没有进度的颜色 xml属性<attr name="anneReadyProgressColor" format="color"/>
     * @value progressColor 进度条的颜色 xml属性 <attr name="anneProgressColor" format="color"/>
     * @value average 进度条上的进度点 每个点与点之间的间距是一致的例如1/2 就设置为1 xml属性<attr name="anneAverage" format="integer" />
     * @value averageIndicator 进度点指示器样式 xml属性如下
     * <attr name="anneThumbIndicator" format="integer">
     * <enum name="RoundedRectangleIndicator" value="0"/><!--圆角矩形-->
     * <enum name="CircleIndicator" value="1"/><!--圆角矩形-->
     * </attr>
     * @value thumbIndicator 进度点指示器样式 xml属性如下
     * <attr name="anneThumbIndicator" format="integer">
     * <enum name="RoundedRectangleIndicator" value="0"/><!--圆角矩形-->
     * <enum name="CirlueIndicator" value="1"/><!--圆角矩形-->
     * </attr>
     * @value thumbIndicatorColor 进度点颜色 xml属性<attr name="anneThumbIndicatorColor" format="color"/>
     * 拖动指示器的坐标点只有X可变，当经过onLayout或者onMeasure后会变成一个具体不可变的值
     * @value thumbX  拖动指示器的横坐标点
     * @value thumbY  拖动指示器的纵坐标点 进过draw方法初始后不再变化
     * @value startX 进度条0%的X坐标
     * @value endX 进度条100%的X坐标
     * @value max 进度最大值
     * @value progress 进度值
     * @value averageIndicators averageIndicator集合
     * @value isSetProgress 是否代码控制设置进度
     */
    private int readyColor = R.color.palegoldenrod, progressColor = R.color.peru;
    private int average = 1, averageIndicator = 1;
    private int thumbIndicator = 1;
    private int thumbIndicatorColor = R.color.peru;
    private float thumbX = 0.0F, thumbY = 0.0F;
    private float startX = 0.0F, endX = 0.0F, XLength;
    private float max = 100;
    private float progress = 0;
    private boolean isInit = false;
    protected List<Float> averageIndicators = new ArrayList<>();
    private boolean isSetProgress = false;
    private float thumbIndicatorTop, thumbIndicatorBottom;

    public void setReadyColor(int readyColor) {
        this.readyColor = readyColor;
        readyPaint.setColor(readyColor);
        invalidate();
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        progressPaint.setColor(progressColor);
        invalidate();
    }

    public void setThumbIndicatorColor(int thumbIndicatorColor) {
        this.thumbIndicatorColor = thumbIndicatorColor;
        invalidate();
    }

    public void setProgress(float progress) {
        if (progress >= max) this.progress = max;
        else this.progress = progress;
        isSetProgress = true;
        setThumbXProgress();
        invalidate();
    }

    //调用这个方法可以一边代码控制进度并且显示Toast并且回调进度监听，如果需要显示完toast后再某一步隐藏掉Toast可以调用hideAnneToast()这个方法
    public void setProgressByToast(float progress, boolean isShowToast) {
        this.progress = progress;
        isSetProgress = !(isShowToast && this.isShowToast);
        setThumbXProgress();
        invalidate();
    }

    protected void setThumbXProgress() {
        if (isInit)
            thumbX = this.progress / getMax() * (XLength) + startX;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMax() {
        return max;
    }

    public float getProgress() {
        if (progress >= max) return max;
        return progress;
    }

    protected String getProgressMessage() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.setGroupingUsed(false);
        float number = progress * 100 / max;
        if (TextUtils.isEmpty(toastTextTag) || toastTextTag == null)
            return Float.parseFloat(numberFormat.format(number)) + "%";
        return toastTextTag + Float.parseFloat(numberFormat.format(number)) + "%";
    }

    public void setAverage(int average) {
        averageIndicators = new ArrayList<>();
        this.average = average;
        if (average > 0) {
            float averageSize = XLength / (average + 1);
            for (int index = 1; index <= average; index++) {
                float x = startX + index * averageSize;
                Log.i("ypzSS", x + "x");
                averageIndicators.add(x);
            }
        }
        Log.i("ypzSS", averageIndicators.size() + "");
        invalidate();
    }

    private AnneProgressChangeListener AnneProgressChangeListener;

    private ExpandProgressMessage expandProgressMessage;

    public void setAnneProgressChangeListener(AnneProgressChangeListener AnneProgressChangeListener) {
        this.AnneProgressChangeListener = AnneProgressChangeListener;
    }

    public void setExpandProgressMessage(ExpandProgressMessage expandProgressMessage) {
        this.expandProgressMessage = expandProgressMessage;
        if (!isShowToast) {
            isShowToast = true;
        }
    }

    protected void resetAnnerMeasure() {
        String toastMessage = "";
        if (TextUtils.isEmpty(toastTextTag)) toastTextTag = "";
        if (expandProgressMessage == null) toastMessage = toastTextTag + " " + 99.99F + "% ";
        else {
            toastMessage = toastTextTag + expandProgressMessage.getProgressMessage(9999F) + "0%0";
            Log.i("ypz", "toastMessage" + toastMessage);
        }
        float textWidth = RxDimenUtils.getTextWidth(textPaint, toastMessage);
        float textHeight = RxDimenUtils.getTextHeighet(textPaint, toastMessage);
        switch (toastShape) {
            case 0:
                anneToastWidth = textWidth + dp10;
                anneToastHeight = textHeight + dp10;
                break;
            case 1:
                if (textWidth >= textHeight)
                    anneToastHeight = anneToastWidth = (textWidth + dp10 * 1.56F);
                else
                    anneToastHeight = anneToastWidth = (textHeight + dp10 * 1.56F);
                break;
        }
    }

    protected float getToastX() {
        if ((thumbX - anneToastWidth / 2) <= startX)
            return startX;
        if ((thumbX + anneToastWidth / 2) >= endX)
            return endX - anneToastWidth;
        return thumbX - anneToastWidth / 2;
    }

    protected float getToastY() {
        int[] Y = new int[2];
        getLocationOnScreen(Y);
        float toastY = Y[1];
        Context context = getContext();
        boolean isMIUI = false;
        toastY -= RxDimenUtils.dpToPx(24, getResources());
        if (AnneUtils.isMIUI()) {
            isMIUI = true;
            toastY += RxDimenUtils.dpToPx(24, getResources());

        }
        if (context instanceof Activity || context instanceof AppCompatActivity) {
            Window window = ((Activity) context).getWindow();
            if (window != null) {
                int flags = window.getAttributes().flags;
                if ((flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0) {
                    Resources res = Resources.getSystem();
                    int id = res.getIdentifier("status_bar_height", "dimen", "android");
                    toastY += res.getDimensionPixelSize(id);
                }
            }
        }
        toastY += getHeight() / 2;
        if (toastShowWay == 2) {
            if (!isMIUI)
                return toastY - (getHeight() - anneToastHeight) / 2 - RxDimenUtils.dpToPx(12, getResources()) + RxDimenUtils.dpToPx(1, getResources());
            else return toastY - (getHeight() + anneToastHeight) / 2;
        }
        if (toastShowWay == 0) {
            if (((toastY - dp10 - anneToastHeight) > 0) && ((toastY - dp10) > 0)) {
                return toastY - dp10 - anneToastHeight;
            } else {
                toastShowWay = 1;
                return getToastY();
            }
        }
        if (toastShowWay == 1) {
            float maxY = getResources().getDisplayMetrics().heightPixels;
            if (((toastY + dp10 + anneToastHeight) < maxY) && ((toastY + dp10) < maxY))
                return toastY + dp10;
            else {
                toastShowWay = 2;
                return getToastY();
            }
        }
        return 0;
    }

    public RxAnneSeekBar(Context context) {
        this(context, null);
    }

    public RxAnneSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxAnneSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RxAnneSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxAnneSeekBar);
            if (typedArray.getBoolean(R.styleable.RxAnneSeekBar_anneIsShowToast, false)) {
                isShowToast = true;
                float toastTextSize = typedArray.getDimension(R.styleable.RxAnneSeekBar_anneToastTextSize, RxDimenUtils.spToPx(12, getResources()));
                toastShape = typedArray.getInteger(R.styleable.RxAnneSeekBar_anneToastShape, 0);
                toastShowWay = typedArray.getInteger(R.styleable.RxAnneSeekBar_anneToastShowWay, 0);
                toastShapeColor = typedArray.getResourceId(R.styleable.RxAnneSeekBar_anneToastShapeColor, R.color.peru);
                toastTextColor = typedArray.getResourceId(R.styleable.RxAnneSeekBar_anneToastTextColor, R.color.palegoldenrod);
                toastTextTag = typedArray.getString(R.styleable.RxAnneSeekBar_anneToastTextTag);
                if (toastTextTag == null) toastTextTag = "";
                toastRadius = typedArray.getDimension(R.styleable.RxAnneSeekBar_anneToastRadius, RxDimenUtils.dpToPx(2, getResources()));
                anneToast = new AnneToast(context, attrs);
                textPaint.setTextSize(toastTextSize);
                textPaint.setColor(getResources().getColor(toastTextColor));
                textPaint.setTextAlign(Paint.Align.CENTER);
                shapePaint.setStyle(Paint.Style.FILL_AND_STROKE);
                shapePaint.setColor(getResources().getColor(toastShapeColor));
                shapePaint.setAlpha((int) (255 * 0.896));
                resetAnnerMeasure();
                windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                mLayoutParams = new WindowManager.LayoutParams();
                mLayoutParams.gravity = Gravity.START | Gravity.TOP;
                mLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mLayoutParams.format = PixelFormat.TRANSLUCENT;
                mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
                // MIUI禁止了开发者使用TYPE_TOAST，Android 7.1.1 对TYPE_TOAST的使用更严格
                if (AnneUtils.isMIUI() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
                } else {
                    mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                }
            }
            setProgress(typedArray.getFloat(R.styleable.RxAnneSeekBar_anneProgress, 0F));
            setMax(typedArray.getFloat(R.styleable.RxAnneSeekBar_anneMax, 100F));
            readyColor = typedArray.getResourceId(R.styleable.RxAnneSeekBar_anneReadyProgressColor, R.color.palegoldenrod);
            progressColor = typedArray.getResourceId(R.styleable.RxAnneSeekBar_anneProgressColor, R.color.peru);
            average = typedArray.getInteger(R.styleable.RxAnneSeekBar_anneAverage, 0);
            averageIndicator = typedArray.getInteger(R.styleable.RxAnneSeekBar_anneAverageIndicator, 1);
            thumbIndicator = typedArray.getInteger(R.styleable.RxAnneSeekBar_anneThumbIndicator, 1);
            thumbIndicatorColor = typedArray.getResourceId(R.styleable.RxAnneSeekBar_anneThumbIndicatorColor, R.color.peru);
            typedArray.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        thumbY = getPivotY();
        int dp2 = RxDimenUtils.dpToPx(2, getResources());
        this.top = thumbY - dp2;
        this.bottom = this.top + dp2 * 2;
        startX = left + dp10;
        endX = right - dp10;
    }

    private boolean isSetDimen(int measureSpecSize) {
        return (MeasureSpec.getMode(measureSpecSize) == MeasureSpec.EXACTLY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isSetDimen(widthMeasureSpec)) {
            ViewGroup mViewGroup = (ViewGroup) getParent();
            widthMeasureSpec = mViewGroup.getWidth();
        }
        int dp20 = RxDimenUtils.dpToPx(20, getResources());
        if (isSetDimen(heightMeasureSpec)) {
            if (heightMeasureSpec < dp20) heightMeasureSpec = dp20;
        } else heightMeasureSpec = dp20;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            thumbX = event.getX();
            if (thumbX <= startX) {
                thumbX = startX;
                progress = 0.0F;
            } else if (thumbX >= (endX - halfThumbRadius)) {
                thumbX = endX - halfThumbRadius;
                progress = max;
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                if (isShowToast) showAnneToast();
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (isShowToast) updateAnneToast();
            }
            invalidate();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            hideAnneToast();
            invalidate();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            hideAnneToast();
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            hideAnneToast();
            invalidate();
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        XLength = endX - startX;
        if (!isInit) {
            //初始化各种画笔样式以及拖动的thumber初始坐标
            thumbX = startX;
            thumbPaint.setColor(getResources().getColor(thumbIndicatorColor));
            thumbPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            thumbPaint.setAntiAlias(true);//设置画笔的锯齿效果
            progressPaint.setColor(getResources().getColor(progressColor));
            progressPaint.setStyle(Paint.Style.FILL);//充满
            progressPaint.setAntiAlias(true);//设置画笔的锯齿效果
            readyPaint.setColor(getResources().getColor(readyColor));
            readyPaint.setStyle(Paint.Style.FILL);//充满
            readyPaint.setAntiAlias(true);//设置画笔的锯齿效果
            isInit = true;
            if (average > 0) {
                float averageSize = XLength / (average + 1);
                for (int index = 1; index <= average; index++) {
                    float x = startX + index * averageSize;
                    averageIndicators.add(x);
                }
            }
            thumbIndicatorTop = getPivotY() - thumbRadius;
            thumbIndicatorBottom = getPivotY() + thumbRadius;
            setThumbXProgress();
        } else {
            if (isSetProgress) {
                isSetProgress = false;
            } else {
                if (thumbX > startX || thumbX < (endX - halfThumbRadius)) {
                    progress = (thumbX - startX) / (XLength - halfDp5) * max;
                }
                if (AnneProgressChangeListener != null)
                    AnneProgressChangeListener.changeProgress(getProgress());
            }
        }
        //画第一次没有进度的进度条
        canvas.drawRoundRect(startX, top, endX, bottom, progressRadius, progressRadius, readyPaint);//第二个参数是x半径，第三个参数是y半径
        //画进度条上的进度点每个点于点之间的距离是相等的
        //画进度长度
        canvas.drawRoundRect(startX, top, thumbX, bottom, progressRadius, progressRadius, progressPaint);
        if (thumbIndicator == 0)
            if (!(toastShowWay == 2 && isAnneShow))
                canvas.drawRoundRect(thumbX - halfThumbRadius, thumbIndicatorTop,
                        thumbX + halfThumbRadius, thumbIndicatorBottom,
                        progressRadius, progressRadius, thumbPaint);
        if (thumbIndicator == 1)
            if (!(toastShowWay == 2 && isAnneShow))
                canvas.drawCircle(thumbX - halfThumbRadius, thumbY, thumbRadius, thumbPaint);
        if (averageIndicators.size() > 0) {
            if (averageIndicator == 0) {
                drawRoundedRectangleAverageIndicator(canvas);
            } else if (averageIndicator == 1) {
                drawCrecledRectangleAverageIndicator(canvas);
            }
        }
    }

    private void drawRoundedRectangleAverageIndicator(Canvas canvas) {
        for (float x : averageIndicators) {
            Log.i("ypzSS", x + "......");
            if (x < startX) continue;
            canvas.drawRoundRect(x - halfDp5, getPivotY() - halfDp5,
                    x + halfDp5, getPivotY() + halfDp5,
                    progressRadius, progressRadius, thumbPaint);
        }
    }

    private void drawCrecledRectangleAverageIndicator(Canvas canvas) {
        for (float x : averageIndicators) {
            canvas.drawCircle(x + halfDp5, thumbY, halfDp5, thumbPaint);
        }

    }

    protected void showAnneToast() {
        if (anneToast == null || anneToast.getParent() != null) {
            return;
        }
        mLayoutParams.x = (int) getToastX();
        mLayoutParams.y = (int) getToastY();
        anneToast.setAlpha(0);
        anneToast.setVisibility(VISIBLE);
        anneToast.animate().alpha(0.96f).setDuration(99)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        windowManager.addView(anneToast, mLayoutParams);
                    }
                }).start();
        isAnneShow = true;
    }

    protected void updateAnneToast() {
        if (anneToast != null && anneToast.getParent() != null) {
            resetAnnerMeasure();
            mLayoutParams.x = (int) getToastX();
            mLayoutParams.y = (int) getToastY();
            windowManager.updateViewLayout(anneToast, mLayoutParams);
            anneToast.invalidate();
        }
    }

    //考虑到增加了设置进度显示Toast的方法将这个方法公开让开发者在关键时候可以代码控制隐藏Toast
    public void hideAnneToast() {
        if (anneToast == null)
            return;
        anneToast.setVisibility(GONE); // 防闪烁
        if (anneToast.getParent() != null) {
            windowManager.removeViewImmediate(anneToast);
        }
        isAnneShow = false;
    }

    private class AnneToast extends View {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }

        private String message;
        private Paint bg = new Paint();

        public AnneToast(Context context) {
            this(context, null);
        }

        public AnneToast(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public AnneToast(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            this(context, attrs, defStyleAttr, 0);
        }

        public AnneToast(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            bg.setColor(getResources().getColor(toastShapeColor));
            bg.setStyle(Paint.Style.FILL_AND_STROKE);//充满
            bg.setAntiAlias(true);//设置画笔的锯齿效果
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension((int) anneToastWidth, (int) anneToastHeight);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (toastShape == 0)
                roundedRectangleIndicator(canvas);
            if (toastShape == 1) circleIndicator(canvas);
        }

        protected void roundedRectangleIndicator(Canvas canvas) {
            if (expandProgressMessage == null) message = getProgressMessage();
            else message = expandProgressMessage.getProgressMessage(getProgress());
            float textWidth = RxDimenUtils.getTextWidth(textPaint, message);
            float textHeight = RxDimenUtils.getTextHeighet(textPaint, message);
            float textCenterX = (this.getX() + dp10 + textWidth) / 2;

            canvas.drawRoundRect(this.getX(), this.getY(),
                    textWidth + dp10 + this.getX(), this.getY() + textHeight + dp10,
                    toastRadius, toastRadius, shapePaint);//第二个参数是x半径，第三个参数是y半径
            canvas.drawText(message, textCenterX, getY() + dp10 / 2 + textHeight, textPaint);

        }

        protected void circleIndicator(Canvas canvas) {
            if (expandProgressMessage == null) message = getProgressMessage();
            else message = expandProgressMessage.getProgressMessage(getProgress());
            float textHeight = RxDimenUtils.getTextHeighet(textPaint, message);
            float radius = getWidth() / 2;
            float textCenterY = textHeight / 2;
            float X = getPivotX();
            float Y = getPivotY();
            canvas.drawCircle(X, Y, radius, shapePaint);
            canvas.drawText(message, X, Y + textCenterY, textPaint);
        }

    }

    public interface ExpandProgressMessage {
        String getProgressMessage(float progressMessage);
    }

    public interface AnneProgressChangeListener {
        void changeProgress(float progress);
    }
}