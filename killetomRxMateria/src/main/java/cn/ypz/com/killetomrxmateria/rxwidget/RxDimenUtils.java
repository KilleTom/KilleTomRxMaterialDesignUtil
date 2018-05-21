package cn.ypz.com.killetomrxmateria.rxwidget;

import android.content.res.Resources;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

public class RxDimenUtils {


    /**
     * dpתPx
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static float getTextWidth(TextPaint paint,String str) {
        return paint.measureText(str);//文字宽

    }
    public static int getTextHeighet(TextPaint paint,String str) {
        Rect rect = new Rect();
       // Log.i("ypz", String.valueOf(str==null)+" ppp"+paint);
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();//文字高;//文字宽
    }

    public static int getRelativeTop(View myView) {
//	    if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content)
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    public static int getRelativeLeft(View myView) {
//	    if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content)
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

}
