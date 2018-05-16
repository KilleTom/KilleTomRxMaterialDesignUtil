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
 *信息提示框动画
 */
public class RxToastInfoAnimation extends RxToastBasePathAnim {
    private int width = 0, height = 0;

    public RxToastInfoAnimation(Context context) {
        super(context);
    }

    public RxToastInfoAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxToastInfoAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Path longLine = new Path();
        longLine.moveTo(width / 2f, height * 0.4f);
        longLine.lineTo(width / 2f, height * 0.8f);
        paths.add(longLine);
        Path shortLine = new Path();
        shortLine.moveTo(width / 2f, height * 0.2f);
        shortLine.lineTo(width / 2f, height * 0.3f);
        paths.add(shortLine);
        return paths;
    }

    @Override
    void setPaint(Paint paint) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getWidth() * 0.05f);
        paint.setColor(Color.parseColor("#FFA900"));
    }
}
