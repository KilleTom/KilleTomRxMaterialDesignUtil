
package cn.ypz.com.killetomrxmateria.rxwidget.raisebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import cn.ypz.com.killetomrxmateria.R;


/**
 * 设置RaiseButton点击时Z轴坐标变为0
 */
public class RxRaisedDropButton extends AppCompatButton {

    private RxRaisedDropDelegate mDelegate;

    public RxRaisedDropButton(Context context) {
        this(context, null);
    }

    public RxRaisedDropButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RxRaisedDropButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDelegate = new RxRaisedDropDelegate(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.RxRaisedDropButton);
            mDelegate.setup(typedArray.getBoolean(R.styleable.RxRaisedDropButton_flatten, true));
            typedArray.recycle();

        }
    }

    public void setHeightLightEvetion(int dimenId){
        mDelegate.setViewHeightLightElevation(dimenId);
    }

    public void setFlatEnabled(boolean flatten) {
        mDelegate.setFlatEnabled(flatten);
    }

    public boolean isFlatEnabled() {
        return mDelegate.isFlatEnabled();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return mDelegate.onSaveInstanceState(super.onSaveInstanceState());
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(mDelegate.onRestoreInstanceState(state));
    }
}
