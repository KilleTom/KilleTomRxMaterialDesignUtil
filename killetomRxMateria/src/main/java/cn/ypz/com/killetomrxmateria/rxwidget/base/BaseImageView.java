package cn.ypz.com.killetomrxmateria.rxwidget.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

public abstract class BaseImageView extends ImageView {

    protected boolean isSquera;

    public BaseImageView(Context context) {
        this(context,null);
    }

    public BaseImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public BaseImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
    }

    protected  void initAttr(Context context,@Nullable AttributeSet attrs){
        if (attrs!=null) hastAttr(context, attrs);
        else notAttr();
    }

    protected abstract void hastAttr(Context context,AttributeSet attrs);

    protected abstract void notAttr();

    protected boolean isSetDimen(int measureSpecSize) {
        return (MeasureSpec.getMode(measureSpecSize) == MeasureSpec.EXACTLY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isSetDimen(widthMeasureSpec)) widthMeasureSpec = resetWSize(widthMeasureSpec);
        if (!isSetDimen(heightMeasureSpec)) heightMeasureSpec = resetHSize(heightMeasureSpec);
        if (isSquera){
            int size = Math.min(widthMeasureSpec,heightMeasureSpec);
            setMeasuredDimension(size,size);
        }else
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    protected abstract int resetWSize(int size);

    protected abstract int resetHSize(int size);

}
