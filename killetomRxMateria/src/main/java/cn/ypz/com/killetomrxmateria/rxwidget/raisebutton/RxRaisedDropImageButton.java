

package cn.ypz.com.killetomrxmateria.rxwidget.raisebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import cn.ypz.com.killetomrxmateria.R;

/**
 * An ImageButton that lowers down to 0dp of elevation when it's pressed
 */
public class RxRaisedDropImageButton extends AppCompatImageButton {

    private RxRaisedDropDelegate mDelegate;

    public RxRaisedDropImageButton(Context context) {
        this(context, null);
    }

    public RxRaisedDropImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RxRaisedDropImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDelegate = new RxRaisedDropDelegate(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxRaisedDropButton);
            mDelegate.setup(typedArray.getBoolean(R.styleable.RxRaisedDropButton_flatten, true));
            typedArray.recycle();
        }
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
