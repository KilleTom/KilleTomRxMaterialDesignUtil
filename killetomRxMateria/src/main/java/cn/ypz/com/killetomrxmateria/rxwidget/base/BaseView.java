package cn.ypz.com.killetomrxmateria.rxwidget.base;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public abstract class BaseView extends View{

    public BaseView(Context context) {
        this(context,null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context,attrs);
    }

    protected  abstract void initAttr(Context context,@Nullable AttributeSet attrs);

    protected boolean isSetDimen(int measureSpecSize) {
        return (MeasureSpec.getMode(measureSpecSize) != MeasureSpec.EXACTLY);
    }

    protected void paintSetColorId(Paint paint,int colorId){
        paint.setColor(getResources().getColor(colorId));
    }
}
