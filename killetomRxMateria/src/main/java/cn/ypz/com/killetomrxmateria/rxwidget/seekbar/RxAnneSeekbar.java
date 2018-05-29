package cn.ypz.com.killetomrxmateria.rxwidget.seekbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.text.NumberFormat;

import cn.ypz.com.killetomrxmateria.R;
import cn.ypz.com.killetomrxmateria.rxwidget.RxDimenUtils;

public class RxAnneSeekbar extends View {
    private TextPaint textPaint = new TextPaint(Paint.LINEAR_TEXT_FLAG);
    private Paint thumberPaint = new Paint();
    private Paint readyPaint = new Paint();
    private Paint progessPaint = new Paint();
    private Paint shapePaint = new Paint();
    private float dp10 = RxDimenUtils.dpToPx(10, getResources());
    private float anneToastWidth, anneToastHeight;
    private String toastMessage;
    /**
     * Toast属性
     * <attr name="anneToastTextSize" format="dimension"/>
     * <attr name="anneToastRadius" format="integer"/>
     * <attr name="anneToastShowWay" format="integer">
     * <enum name="above" value="0"/>
     * <enum name="below" value="1"/>
     * <enum name="center" value="2"/>
     * </attr>
     * <attr name="anneIsShowToast" format="boolean"/>
     * <attr name="anneToastShape" format="integer">
     * <enum name="RoundedRectangleIndicator" value="0"/>
     * <enum name="CirlueIndicator" value="1"/>
     * </attr>
     * <attr name="anneToastTextTag" format="string"/>
     * <attr name="anneToastTextColor" format="color"/>
     * <attr name="anneToastShapeColor" format="color"/>
     */
    private float toastTextSize = RxDimenUtils.spToPx(12, getResources());
    private boolean isShowToast = false;
    private int toastShowWay = 0;
    private int toastShape = 0;
    private float toastRadius = RxDimenUtils.dpToPx(3, getResources());
    private String toastTextTag = "";
    private int toastShapeColor = R.color.peru, toastTextColor = R.color.palegoldenrod;
    /**
     * 进度属性
     * <attr name="anneReadyProgressColor" format="color"/>
     * <attr name="anneProgressColor" format="color"/>
     * <attr name="anneAverage" format="integer" />
     * <attr name="anneAverageIndicator" format="integer">
     * <enum name="RoundedRectangleIndicator" value="0"/>
     * </attr>
     * <attr name="anneThumbIndicator" format="integer">
     * <enum name="RoundedRectangleIndicator" value="0"/>
     * <enum name="CirlueIndicator" value="1"/>
     * </attr>
     * <attr name="anneThumbIndicatorColor" format="color"/>
     */
    private int radyColor = R.color.palegoldenrod, progressColor = R.color.peru;
    private int average = 1, averageIndicator = 1;
    private int thumbIndicator = 1;
    private int thumbIndicatorColor = R.color.peru;
    /**
     * 拖动指示器的坐标点只有X可变，当经过onLayout或者onMeasure后会变成一个具体不可变的值
     */
    private float thumbX = 0.0F, thumbY = 0.0F;
    /**
     * 进度起点及完成进度的节点
     */
    private float startX = 0.0F, endX = 0.0F, XLength;

    private float progressRadius = dp10 / 5;
    private float thumberRadius = dp10 / 2;
    private float top, bottom;
    /**
     * @parm max 进度最大值
     * @parm progress 进度值
     */
    private float max = 100;
    private float progress = 0;

    private ProgressChanggeLisnter progressChanggeLisnter;

    private ExpandProgressMessage expandProgressMessage;

    public void setProgressChanggeLisnter(ProgressChanggeLisnter progressChanggeLisnter) {
        this.progressChanggeLisnter = progressChanggeLisnter;
    }

    public void setExpandProgressMessage(ExpandProgressMessage expandProgressMessage) {
        this.expandProgressMessage = expandProgressMessage;
    }

    private AnneToast anneToast;
    private WindowManager windowManager;
    private WindowManager.LayoutParams mLayoutParams;

    public void setProgress(float progress) {
        this.progress = progress;
        isSetProgress = true;
        thumbX = progress / max * (XLength) + getX();
        invalidate();
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMax() {
        return max;
    }

    public float getProgress() {
        return progress;
    }

    protected String getProgressMessage() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.setGroupingUsed(false);
        return Float.parseFloat(numberFormat.format(progress * 100)) + "%";
    }

    protected void resetAnnerMersue() {
        if (expandProgressMessage == null) toastMessage = getProgress() + "00";
        else toastMessage = expandProgressMessage.getProgressMessage(0.123456789F) + "0%0";
        float textWidth = RxDimenUtils.getTextWidth(textPaint, toastMessage);
        float textHeight = RxDimenUtils.getTextHeighet(textPaint, toastMessage);
        switch (toastShape) {
            case 0:
                anneToastWidth = textWidth + dp10 * 2;
                anneToastHeight = textHeight + dp10 + dp10 / 2;
                break;
            case 1:
                if (textWidth >= textHeight) {
                    anneToastWidth = (textWidth + dp10) / 2;
                    anneToastHeight = (textWidth + dp10) / 2;
                } else {
                    anneToastWidth = (textHeight + dp10) / 2;
                    anneToastHeight = (textHeight + dp10) / 2;
                }
                break;
        }
    }

    protected float getToastX() {
        if (toastShowWay == 2) return thumbX;
        if ((thumbX - anneToastWidth / 2) <= startX)
            return startX;
        if ((thumbX + anneToastWidth / 2) >= endX)
            return endX - anneToastWidth;
        return thumbX - anneToastWidth / 2;
    }

    protected float getToastY() {
        Log.i("ypz", getY() + "ToastY" + toastShowWay);
        float Y = getY();
        Y += getHeight() / 2;
        if (toastShowWay == 2) return (Y + getHeight() / 2 - anneToastHeight / 2);
        if (toastShowWay == 0) {
            if (((Y - dp10 - anneToastHeight) > 0) && ((Y - dp10) > 0)) {
                return Y - dp10 - anneToastHeight;
            } else {
                toastShowWay = 1;
                return getToastY();
            }
        }
        if (toastShowWay == 1) {
            float maxY = getResources().getDisplayMetrics().heightPixels;
            if (((Y + dp10 + anneToastHeight) < maxY) && ((Y + dp10) < maxY))
                return Y + dp10 + anneToastHeight;
            else {
                toastShowWay = 2;
                return getToastY();
            }
        }
        return 0;
    }

    private boolean isSetProgress = false;

    public RxAnneSeekbar(Context context) {
        this(context, null);
    }

    public RxAnneSeekbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxAnneSeekbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RxAnneSeekbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxAnneSeekbar);
            if (typedArray.getBoolean(R.styleable.RxAnneSeekbar_anneIsShowToast, false)) {
                isShowToast = true;
                toastTextSize = typedArray.getDimension(R.styleable.RxAnneSeekbar_anneToastTextSize, RxDimenUtils.spToPx(12, getResources()));
                toastShape = typedArray.getInteger(R.styleable.RxAnneSeekbar_anneToastShape, 0);
                toastShowWay = typedArray.getInteger(R.styleable.RxAnneSeekbar_anneToastShowWay, 0);
                toastShapeColor = typedArray.getResourceId(R.styleable.RxAnneSeekbar_anneToastShapeColor, R.color.peru);
                toastTextColor = typedArray.getResourceId(R.styleable.RxAnneSeekbar_anneToastTextColor, R.color.palegoldenrod);
                toastTextTag = typedArray.getString(R.styleable.RxAnneSeekbar_anneToastTextTag) + "";
                toastRadius = typedArray.getDimension(R.styleable.RxAnneSeekbar_anneToastRadius, RxDimenUtils.dpToPx(2, getResources()));
                anneToast = new AnneToast(context, attrs);
                textPaint.setTextSize(toastTextSize);
                textPaint.setColor(getResources().getColor(toastTextColor));
                textPaint.setTextAlign(Paint.Align.CENTER);
                shapePaint.setStyle(Paint.Style.FILL_AND_STROKE);
                shapePaint.setColor(getResources().getColor(toastShapeColor));
                shapePaint.setAlpha((int) (255*0.896));
                resetAnnerMersue();
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
            radyColor = typedArray.getResourceId(R.styleable.RxAnneSeekbar_anneReadyProgressColor, R.color.palegoldenrod);
            progressColor = typedArray.getResourceId(R.styleable.RxAnneSeekbar_anneProgressColor, R.color.peru);
            average = typedArray.getInteger(R.styleable.RxAnneSeekbar_anneAverage, 1);
            averageIndicator = typedArray.getInteger(R.styleable.RxAnneSeekbar_anneAverageIndicator, 1);
            thumbIndicator = typedArray.getInteger(R.styleable.RxAnneSeekbar_anneThumbIndicator, 1);
            thumbIndicatorColor = typedArray.getResourceId(R.styleable.RxAnneSeekbar_anneThumbIndicatorColor, R.color.peru);
            typedArray.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        thumbY = getPivotY();
        int dp2 = RxDimenUtils.dpToPx(2, getResources());
        this.top = thumbY - dp2;
        this.bottom = this.top + dp2 * 2;
        startX = getX() + dp10;
        endX = getX() + getWidth() - dp10;
        Log.i("ypz", thumbX + "thumbX");
    }

    private boolean isSetDimession(int measureSpecSize) {
        return (MeasureSpec.getMode(measureSpecSize) == MeasureSpec.EXACTLY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isSetDimession(widthMeasureSpec)) {
            widthMeasureSpec = getResources().getDisplayMetrics().widthPixels;
        }
        int dp20 = RxDimenUtils.dpToPx(20, getResources());
        if (isSetDimession(heightMeasureSpec)) {
            if (heightMeasureSpec < dp20) heightMeasureSpec = dp20;
        } else heightMeasureSpec = dp20;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        Log.i("ypz", widthMeasureSpec + "........" + heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            thumbX = event.getX();
            if (thumbX <= startX) {
                thumbX = startX;
                progress = 0.0F;
            } else if (thumbX >= (endX - thumberRadius)) {
                thumbX = endX - thumberRadius;
                progress = max;
            }
            invalidate();
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                if (isShowToast) showAnneToast();
            if (event.getAction() == MotionEvent.ACTION_MOVE)
                if (isShowToast) updateAnneToast();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            hideAnneToast();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (thumbX == 0) {
            //初始化各种画笔样式以及拖动的thumber初始坐标
            thumbX = startX;
            thumberPaint.setColor(getResources().getColor(thumbIndicatorColor));
            thumberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            thumberPaint.setAntiAlias(true);//设置画笔的锯齿效果
            progessPaint.setColor(getResources().getColor(progressColor));
            progessPaint.setStyle(Paint.Style.FILL);//充满
            progessPaint.setAntiAlias(true);//设置画笔的锯齿效果
            readyPaint.setColor(getResources().getColor(radyColor));
            readyPaint.setStyle(Paint.Style.FILL);//充满
            readyPaint.setAntiAlias(true);//设置画笔的锯齿效果
            setProgress(0.0F);
        }
        XLength = endX - startX;
        canvas.drawRoundRect(startX, top, endX, bottom, progressRadius, progressRadius, readyPaint);//第二个参数是x半径，第三个参数是y半径
        if (isSetProgress) {
            isSetProgress = false;
            thumbX = (XLength) * getProgress() + startX;
        } else {
            if (thumbX > startX || thumbX < (endX - thumberRadius)) {
                progress = (thumbX - startX) / (XLength - thumberRadius);
            }
            if (progressChanggeLisnter != null)
                progressChanggeLisnter.changeProgress(getProgress());

        }
        canvas.drawRoundRect(startX, top, thumbX, bottom, progressRadius, progressRadius, progessPaint);
        canvas.drawCircle(thumbX + thumberRadius, thumbY, thumberRadius, thumberPaint);
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
    }

    protected void updateAnneToast() {
        if (anneToast != null && anneToast.getParent() != null) {
            mLayoutParams.x = (int) getToastX();
            windowManager.updateViewLayout(anneToast, mLayoutParams);
            anneToast.invalidate();
        }
    }

    protected void hideAnneToast() {
        if (anneToast == null)
            return;
        anneToast.setVisibility(GONE); // 防闪烁
        if (anneToast.getParent() != null) {
            windowManager.removeViewImmediate(anneToast);
        }
    }

    private class AnneToast extends View {
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
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) anneToastWidth, (int) anneToastHeight);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (toastShape == 0)
                drawRoundedRectangleIndicator(canvas);
            if (toastShape == 1) drawCricleIndicator(canvas);
        }

        protected void drawRoundedRectangleIndicator(Canvas canvas) {
            Log.i("ypz", "drwas" + this.getX() + "   " + this.getHeight() + "  " + this.getY());
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

        protected void drawCricleIndicator(Canvas canvas) {
            if (expandProgressMessage == null) message = getProgressMessage();
            else message = expandProgressMessage.getProgressMessage(getProgress());
            float textWidth = RxDimenUtils.getTextWidth(textPaint, message);
            float textHeight = RxDimenUtils.getTextHeighet(textPaint, message);
            float radius = getWidth() /2;
            float textCenterX = (this.getX() + dp10 + textWidth) / 2;
            float textCenterY = (this.getY() + dp10 + textHeight) /2;
            canvas.drawCircle(textCenterX, textCenterY,radius,shapePaint);
            canvas.drawText(message, textCenterX, getY() + dp10 / 2 + textHeight, textPaint);
        }

    }


    public interface ExpandProgressMessage {
        String getProgressMessage(float progressMessage);
    }

    public interface ProgressChanggeLisnter {
        void changeProgress(float progress);
    }
}
