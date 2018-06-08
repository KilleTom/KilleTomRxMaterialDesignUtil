

package cn.ypz.com.killetomrxmateria.rxwidget.raisebutton;


import android.os.Parcelable;

interface RxRaisedDropView {

    Parcelable onSaveInstanceState(Parcelable state);

    Parcelable onRestoreInstanceState(Parcelable state);

    void setFlatEnabled(boolean enable);

    boolean isFlatEnabled();
}
