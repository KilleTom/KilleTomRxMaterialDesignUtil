package cn.ypz.com.killetomrxmateria.rxwidget.toast.anim;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 成功用例动画
 */
public class RxToastSuccessAnimation extends RxToastBasePathAnim {

    private int width = 0, height = 0;

    public RxToastSuccessAnimation(Context context) {
        super(context);
    }

    public RxToastSuccessAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxToastSuccessAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    List<Path> getPaths() {
        if (width == 0 || height == 0) {
            width = getWidth();
            height = getHeight();
        }
        float x1 = width * 0.25f, y1 = height * 0.50f;
        float x2 = width * 0.40f, y2 = height * 0.80f;
        float x3 = width * 0.90f, y3 = height * 0.40f;
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        List<Path> paths = new ArrayList<>();
        paths.add(path);
        return paths;
    }

    @Override
    void setPaint(Paint paint) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getWidth() * 0.05f);
        paint.setColor(Color.parseColor("#388E3C"));
    }
}
