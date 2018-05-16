package cn.ypz.com.killetomrxmateria.rxwidget.toast.anim;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 警告动态绘制图标
 */
public class RxToastWarningAnimation extends RxToastBasePathAnim {

    private int width = 0, height = 0;

    public RxToastWarningAnimation(Context context) {
        super(context);
    }

    public RxToastWarningAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxToastWarningAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    List<Path> getPaths() {
        if (width == 0 || height == 0) {
            width = getWidth();
            height = getHeight();
        }
        List<Path> paths = new ArrayList<>();
        Path circle = new Path();
        circle.addArc(new RectF(0 + width / 20f, 0 + width / 20f, width - width / 20f, height - width / 20f)
                , 0f, 360f);
        paths.add(circle);
        Path line = new Path();
        line.moveTo(width / 2f, height * 0.2f);
        line.lineTo(width / 2f, height * 0.6f);
        paths.add(line);
        Path spot = new Path();
        spot.moveTo(width / 2f, height * 0.7f);
        spot.lineTo(width / 2f, height * 0.8f);
        paths.add(spot);
        return paths;
    }

    @Override
    void setPaint(Paint paint) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getWidth() * 0.05f);
        paint.setColor(Color.parseColor("#FF8214"));
    }

}
