package cn.ypz.com.killetomrxmateria.rxwidget.toast.anim;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 *错误动画
 */
public class RxToastErrorAnimation extends RxToastBasePathAnim {

    private int width = 0, height = 0;

    public RxToastErrorAnimation(Context context) {
        super(context);
    }

    public RxToastErrorAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxToastErrorAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    List<Path> getPaths() {
        if (width == 0 || height == 0) {
            width = getWidth();
            height = getHeight();
        }
        Path path = new Path();
        path.moveTo(width / 6, height / 6);
        path.lineTo(width / 6 * 5, height / 6 * 5);
        Path path1 = new Path();
        path1.moveTo(width / 6 * 5, height / 6);
        path1.lineTo(width / 6, height / 6 * 5);
        List<Path> paths = new ArrayList<>();
        paths.add(path);
        paths.add(path1);
        return paths;
    }

    @Override
    void setPaint(Paint paint) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getWidth() / 18f);
        paint.setColor(Color.parseColor("#5CE660"));
    }

}
