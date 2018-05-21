package cn.ypz.com.killetomrxmateria.rxwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import cn.ypz.com.killetomrxmateria.R;

public class RxSliderToast extends View {
    private int textColor = android.R.color.holo_red_dark, shapeColor = android.R.color.holo_orange_dark;
    private float x, y, endx, endy;
    private float textWidth, textHeight;
    private int marginStart ,magrinEnd ,magrinTop,maginBottom;
    private String text = "12:25:30";
    private int radius = 2;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RxSliderToast(Context context) {
        this(context, null);
    }

    public RxSliderToast(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxSlider);
            text = typedArray.getString(R.styleable.RxSlider_rxSliderToastText);
            radius = typedArray.getInteger(R.styleable.RxSlider_rxSliderToastRadius, 2);
            typedArray.recycle();
        }
    }

    public RxSliderToast(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        TextPaint textPaint = new TextPaint(Paint.LINEAR_TEXT_FLAG);
        textPaint.setTextSize(38f);
        if (text == null) text = "12:25:30";
        textWidth = textPaint.measureText(text);
        textHeight = RxDimenUtils.getTextHeighet(textPaint, text);
        if (isSetDimession(widthMeasureSpec)) {
            if (textWidth > widthMeasureSpec) widthMeasureSpec = (int) (60 + textWidth);
        } else widthMeasureSpec = (int) (60 + textWidth);
        if (isSetDimession(heightMeasureSpec)) {
            if (textHeight > heightMeasureSpec) heightMeasureSpec = (int) (70 + textHeight);
        } else heightMeasureSpec = (int) (70 + textHeight);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean isSetDimession(int measureSpecSize) {
        return (MeasureSpec.getMode(measureSpecSize) == MeasureSpec.EXACTLY);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int[] XY = new int[2];
        getLocationOnScreen(XY);
        x = XY[0];
        y = XY[1];
        int statusBarHeight = -1;
//获取status_bar_height资源的ID  
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            y -= statusBarHeight;
            endx = right;
            endy = bottom;
        }
        if (endy==0){
            endx = getWidth()+x;
            endy = y+getHeight()-63;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginStart = lp.leftMargin;
        magrinEnd = lp.rightMargin;
        magrinTop = lp.topMargin;
        maginBottom = lp.bottomMargin;

        Log.i("ypzkilletom", left + "\ny:" + y + "\nendy:" + endy + "\nendx" + endx);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (text == null) text = "12:25:30";
        if (textColor == 0) textColor = android.R.color.holo_red_light;
        if (shapeColor == 0) shapeColor = android.R.color.holo_orange_dark;
        TextPaint textPaint = new TextPaint(Paint.LINEAR_TEXT_FLAG);
        textPaint.setTextSize(30f);
        textPaint.setColor(getResources().getColor(textColor));
        textPaint.setTextAlign(Paint.Align.CENTER);
        //三角形起点坐标点
        float lineS_X = (endx-magrinEnd)/2-20 , lineS_Y = endy-20-maginBottom;
        Paint bg = new Paint();
        bg.setColor(getResources().getColor(shapeColor));
        bg.setStyle(Paint.Style.FILL_AND_STROKE);//充满
        bg.setAntiAlias(true);//设置画笔的锯齿效果
        canvas.drawRoundRect(x-marginStart, y-magrinTop,
                endx-magrinEnd, lineS_Y, 30, 30, bg);//第二个参数是x半径，第三个参数是y半径
        Log.i("ypzkilletom", endx + "\n" + endy);
        Path path = new Path();
        path.moveTo(lineS_X, lineS_Y);
        //画三角形
        path.lineTo(lineS_X + 20, lineS_Y + 20);
        path.lineTo(lineS_X + 40, lineS_Y);
        path.close();
        canvas.drawPath(path, bg);
        Log.i("ypzkilletom", "TX" + (endx) / 2 + "\nTY" + y+30);
        canvas.drawText(text, lineS_X+20, (lineS_Y)/2+10, textPaint);
    }
}
