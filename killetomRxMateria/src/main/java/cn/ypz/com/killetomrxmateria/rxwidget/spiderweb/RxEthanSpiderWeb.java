package cn.ypz.com.killetomrxmateria.rxwidget.spiderweb;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import cn.ypz.com.killetomrxmateria.R;
import cn.ypz.com.killetomrxmateria.rxwidget.RxDimenUtils;
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseView;

public class RxEthanSpiderWeb extends BaseView {
    /**
     * @value spiderWebPaint 蜘蛛网线条画笔
     * @value spiderBgPaint 蜘蛛网背景颜色画笔
     * @value scoreBgPaint 分数背景颜色画笔
     * @value scoreStrokePaint 分数线条画笔
     * @value path 图形
     * @value scores 用户每一个评分点的具体数值分数列表
     * @value angleCount 整个蛛网有几个角
     * @value hierarchyCount 整个蛛网分多少层（例如最大分数是10分，分5层，那么每层就代表2分）
     * @value maxScore 每一个分数顶点代表分数值
     * @value averageAngle 平均角度
     * @value offsetAngle 偏移角度
     * @value spiderColor 蜘蛛网背景颜色当设置为变色模式则为起始颜色
     * @value scoreColor 分数图形的颜色
     * @value spiderStrokeColor  蛛网线条的颜色
     * @value scoreStrokeColor 分数图形描边的颜色
     * @value spiderEndColor 蜘蛛网变色的结束颜色
     * @value spiderStrokeWidth 蛛网线条的宽度
     * @value scoreStrokeWidth  分数图形描边的宽度
     * @value isScoreStroke  是否分数图形的描边
     * @value isSpiderStroke 是否蜘蛛网状层次描边
     * @value isSpiderBg  是否设置蜘蛛网背景
     * @value isScoreBg 是否设置分数网背景
     * @value isComplexOffset 是否设置复数边形的角度偏移量保证复数边形不会以一定角度旋转偏移默认不设置效果会好一点
     * @value gradientSpiderColors 每一层变色后的颜色值
     * @value radius 整个蛛网图的半径
     * @value centerX 蛛网圆点的X轴坐标
     * @value centerY 蛛网圆点的Y轴坐标
     */
    private Paint spiderWebPaint;
    private Paint spiderBgPaint;
    private Paint scoreBgPaint;
    private Paint scoreStrokePaint;
    private Path path = new Path();
    private float[] scores;
    private int angleCount;
    private int hierarchyCount;
    private float maxScore;
    private float averageAngle;
    private float offsetAngle;
    private int spiderColor;
    private int scoreColor;
    private int spiderStrokeColor;
    private int scoreStrokeColor;
    private int spiderEndColor;
    private float spiderStrokeWidth;
    private float scoreStrokeWidth;
    private boolean isScoreStroke;
    private boolean isSpiderStroke;
    private boolean isSpiderBg;
    private boolean isScoreBg;
    private boolean isComplexOffset;
    private boolean isGradientSpider;
    private int gradientSpiderColors[];
    private float radius;
    private float centerX, centerY;

    public RxEthanSpiderWeb(Context context) {
        this(context, null);
    }

    public RxEthanSpiderWeb(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initAttr(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxEthanSpiderWeb);
            isComplexOffset = typedArray.getBoolean(R.styleable.RxEthanSpiderWeb_isComplexOffset, RxSpiderConstant.isComplexOffset);
            isSpiderBg = typedArray.getBoolean(R.styleable.RxEthanSpiderWeb_isSpiderBg, RxSpiderConstant.isSpiderBg);
            isSpiderStroke = typedArray.getBoolean(R.styleable.RxEthanSpiderWeb_isSpiderStroke, RxSpiderConstant.isSpiderStroke);
            isScoreBg = typedArray.getBoolean(R.styleable.RxEthanSpiderWeb_isScoreBg, RxSpiderConstant.isScoreBg);
            isScoreStroke = typedArray.getBoolean(R.styleable.RxEthanSpiderWeb_isScoreStroke, RxSpiderConstant.isScoreStroke);
            setAngleCount(typedArray.getInt(R.styleable.RxEthanSpiderWeb_angleCount, RxSpiderConstant.angleCount));
            setHierarchyCount(typedArray.getInt(R.styleable.RxEthanSpiderWeb_hierarchyCount, RxSpiderConstant.hierarchyCount));
            setMaxScore(typedArray.getFloat(R.styleable.RxEthanSpiderWeb_maxScore, maxScore));
            if (isSpiderBg) {
                spiderColor = typedArray.getResourceId(R.styleable.RxEthanSpiderWeb_spiderBg, RxSpiderConstant.spiderColor);
                setSpiderColor(spiderColor);
            }
            isGradientSpider = typedArray.getBoolean(R.styleable.RxEthanSpiderWeb_isGradientSpider, RxSpiderConstant.isGradientSpider);
            if (isGradientSpider) {
                spiderColor = typedArray.getResourceId(R.styleable.RxEthanSpiderWeb_spiderBg, RxSpiderConstant.spiderColor);
                spiderEndColor = typedArray.getResourceId(R.styleable.RxEthanSpiderWeb_spiderEndBg, RxSpiderConstant.spiderEndColor);
                resetGradientSpiderColors();
            }
            if (isSpiderStroke) {
                setSpiderStrokeWidth(typedArray.getDimension(R.styleable.RxEthanSpiderWeb_spiderStrokeWidth, RxDimenUtils.dpToPx(1, getResources())));
                setSpiderStrokeColor(typedArray.getResourceId(R.styleable.RxEthanSpiderWeb_spiderStokeBg, RxSpiderConstant.spiderStrokeColor));
            }
            if (isScoreBg)
                setScoreColor(typedArray.getResourceId(R.styleable.RxEthanSpiderWeb_scoreBg, RxSpiderConstant.scoreColor));
            if (isScoreStroke) {
                setScoreStrokeWidth(typedArray.getDimension(R.styleable.RxEthanSpiderWeb_spiderStrokeWidth, RxDimenUtils.dpToPx(1, getResources())));
                setScoreStrokeColor(typedArray.getResourceId(R.styleable.RxEthanSpiderWeb_scoreStokeBg, RxSpiderConstant.scoreStrokeColor));
            }
            typedArray.recycle();
        } else {
            resetScoreStrokePaint();
            resetSpiderStrokePaint();
            resetSpiderBgPaint();
            resetScoreBgPaint();
        }
    }

    private Paint resetPaint(@Nullable Paint paint, Paint.Style style) {
        if (paint == null) paint = new Paint();
        paint.setStyle(style);
        paint.setAntiAlias(true);
        return paint;
    }

    private void resetSpiderBgPaint() {
        spiderBgPaint = resetPaint(spiderBgPaint, Paint.Style.FILL);
        paintSetColor(spiderBgPaint, spiderColor);
    }

    private void resetSpiderStrokePaint() {
        spiderWebPaint = resetPaint(spiderWebPaint, Paint.Style.STROKE);
        paintSetColor(spiderWebPaint, spiderStrokeColor);
        paintSetStroke(spiderWebPaint, spiderStrokeWidth);
    }

    private void resetScoreStrokePaint() {
        scoreStrokePaint = resetPaint(scoreStrokePaint, Paint.Style.STROKE);
        paintSetColor(scoreStrokePaint, scoreStrokeColor);
        paintSetStroke(scoreStrokePaint, scoreStrokeWidth);
    }

    private void resetScoreBgPaint() {
        scoreBgPaint = resetPaint(scoreBgPaint, Paint.Style.FILL);
        paintSetColor(scoreBgPaint, scoreColor);
    }

    private void resetGradientSpiderColors() {
        try {
            gradientSpiderColors = new int[hierarchyCount];
            gradientSpiderColors[hierarchyCount - 1] = getResources().getColor(spiderEndColor);
            gradientSpiderColors[0] = getResources().getColor(spiderColor);
            int startRed = (gradientSpiderColors[0] & 0xff0000) >> 16;
            int startGreen = (gradientSpiderColors[0] & 0x00ff00) >> 8;
            int startBlue = (gradientSpiderColors[0] & 0x0000ff);
            int endRed = (gradientSpiderColors[hierarchyCount - 1] & 0xff0000) >> 16;
            int endGreen = (gradientSpiderColors[hierarchyCount - 1] & 0x00ff00) >> 8;
            int endBlue = (gradientSpiderColors[hierarchyCount - 1] & 0x0000ff);
            int nextRed = (endRed - startRed) / hierarchyCount;
            int nextGreen = (endGreen - startGreen) / hierarchyCount;
            int nextBlue = (endBlue - startBlue) / hierarchyCount;
            for (int i = 0; i < hierarchyCount; i++) {
                if (!(i == 0 || i == (hierarchyCount - 1)))
                    gradientSpiderColors[i] = Color.argb(255, (startRed + i * nextRed), (startGreen + i * nextGreen), (startBlue + i * nextBlue));
            }
        } catch (Resources.NotFoundException e) {
            Log.i("ypz", "error" + e.getMessage());
        }
       invalidate();
    }

    public void setScoreStroke(boolean scoreStroke) {
        isScoreStroke = scoreStroke;
        if (isScoreStroke) {
            if (scoreStrokePaint == null) {
                resetScoreStrokePaint();
            }
        }
        postInvalidate();
    }

    public void setSpiderStroke(boolean spiderStroke) {
        isSpiderStroke = spiderStroke;
        if (isSpiderStroke) {
            if (spiderWebPaint == null) {
                resetSpiderStrokePaint();
            }
        }
        postInvalidate();
    }

    public void setSpiderBg(boolean spiderBg) {
        isSpiderBg = spiderBg;
        if (isSpiderBg) {
            if (spiderBgPaint == null) resetSpiderBgPaint();
        }
        postInvalidate();
    }

    public void setScoreBg(boolean scoreBg) {
        isScoreBg = scoreBg;
        if (isScoreBg) {
            if (scoreBgPaint == null) resetScoreBgPaint();
        }
        postInvalidate();
    }

    public void setComplexOffset(boolean complexOffset) {
        isComplexOffset = complexOffset;
        if (isComplexOffset) offsetAngle = averageAngle / 2;
        else offsetAngle = 0;
        postInvalidate();
    }

    public void setGradientSpider(boolean gradientSpider) {
        isGradientSpider = gradientSpider;
        if (isGradientSpider)
            if (spiderEndColor == 0) setSpiderEndColor(RxSpiderConstant.spiderEndColor);
            else setSpiderEndColor(spiderEndColor);
    }

    public void setScores(float[] scores) {
        if (scores == null) throw new NullPointerException("分数集合不能为null");
        int length = scores.length;
        if (length > angleCount) {
            setAngleCount(scores.length);
            this.scores = scores;
        } else if (length < angleCount) {
            this.scores = new float[angleCount];
            for (int i = 0; i < this.scores.length; i++) {
                if (i < length) this.scores[i] = scores[i];
                else scores[i] = 0;
            }
        } else this.scores = scores;
        Log.i("ypzZZ","finish");
        invalidate();
    }

    /**
     * @param angleCount 设置多边形
     */
    public void setAngleCount(int angleCount) {
        if (angleCount <= 2) throw new IllegalArgumentException("最小的多边形为三角形所以角的总数不能小于2");
        this.angleCount = angleCount;
        averageAngle = 360 / angleCount;
        if (isComplexOffset) offsetAngle = averageAngle / 2;
        else offsetAngle = 0;
        invalidate();
    }

    public int getAngleCount() {
        return angleCount;
    }

    public void setHierarchyCount(int hierarchyCount) {
        if (hierarchyCount <= 0)
            throw new IllegalArgumentException("蜘蛛网状层必须大于0");
        this.hierarchyCount = hierarchyCount;
        if (isGradientSpider) resetGradientSpiderColors();
        postInvalidate();
    }

    public void setMaxScore(float maxScore) {
        if (maxScore <= 0) throw new IllegalArgumentException("分数最大值必须是大于等于0对于一些负数操作你可以转化为绝对数进行操作");
        this.maxScore = maxScore;
        postInvalidate();
    }

    public void setSpiderColor(int spiderColor) {
        this.spiderColor = spiderColor;
        setSpiderBg(true);
        paintSetColor(spiderBgPaint, spiderColor);
        if (isGradientSpider) resetGradientSpiderColors();
        postInvalidate();
    }

    public void setSpiderEndColor(int spiderEndColor) {
        this.spiderEndColor = spiderEndColor;
        isGradientSpider = true;
        resetGradientSpiderColors();
    }

    public void setScoreColor(int scoreColor) {
        this.scoreColor = scoreColor;
        setScoreBg(true);
        resetScoreBgPaint();
        paintSetColor(scoreBgPaint, scoreColor);
        postInvalidate();
    }

    public void setSpiderStrokeColor(int spiderStrokeColor) {
        this.spiderStrokeColor = spiderStrokeColor;
        setSpiderStroke(true);
        paintSetColor(spiderWebPaint, spiderStrokeColor);
        postInvalidate();
    }

    public void setScoreStrokeColor(int scoreStrokeColor) {
        this.scoreStrokeColor = scoreStrokeColor;
        setScoreStroke(true);
        paintSetColor(scoreStrokePaint, scoreStrokeColor);
        postInvalidate();
    }

    public void setSpiderStrokeWidth(float spiderStrokeWidth) {
        this.spiderStrokeWidth = spiderStrokeWidth;
        if (spiderWebPaint == null) resetSpiderStrokePaint();
        else spiderWebPaint.setStrokeWidth(spiderStrokeWidth);
        postInvalidate();
    }

    public void setScoreStrokeWidth(float scoreStrokeWidth) {
        this.scoreStrokeWidth = scoreStrokeWidth;
        if (scoreStrokePaint == null) resetScoreStrokePaint();
        else scoreStrokePaint.setStrokeWidth(scoreStrokeWidth);
        postInvalidate();
    }

    protected void paintSetColor(@Nullable Paint paint, int colorId) {
        if (paint != null) paint.setColor(colorId);
        else {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(colorId);
        }
    }

    protected void paintSetStroke(@Nullable Paint paint, float strokeWidth) {
        if (paint != null && strokeWidth > 0) {
            paint.setStrokeWidth(scoreStrokeWidth);
        }
    }

    private void reset() {
        if (angleCount >= 3 && hierarchyCount >= 1) {
            centerX = getWidth() / 2;
            centerY = getHeight() / 2;
            radius = Math.min(getWidth(), getHeight()) / 2 - RxDimenUtils.dpToPx(2, getResources());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isSetDimen(widthMeasureSpec)) {
            widthMeasureSpec = RxDimenUtils.dpToPx(100, getResources());
        }
        if (!isSetDimen(heightMeasureSpec)) {
            widthMeasureSpec = RxDimenUtils.dpToPx(100, getResources());
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        reset();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isSpiderBg && !isGradientSpider) drawSpiderBg(canvas);
        if (isGradientSpider) drawGradientSpider(canvas);
        if ((!isSpiderStroke) && (!isSpiderBg) || isSpiderStroke) drawSpiderStoke(canvas);
        drawScore(canvas);
    }

    /**
     * 绘制蜘蛛网中心点到最外层形状的的背景path
     */
    protected void drawSpiderBg(Canvas canvas) {
        canvas.drawPath(hierarchyByRadiusPath(radius), spiderBgPaint);
    }

    /**
     * 绘制渐变的蜘蛛网 注意是从最外层到最内层间的绘制避免画笔颜色会被覆盖
     */
    protected void drawGradientSpider(Canvas canvas) {
        float averageRadius = radius / hierarchyCount;
        for (int i = hierarchyCount - 1; i >= 0; i--) {
            try {
                paintSetColor(spiderBgPaint, gradientSpiderColors[i]);
                canvas.drawPath(hierarchyByRadiusPath(averageRadius * (i + 1)), spiderBgPaint);
            } catch (Exception e) {
                Log.i("ypzZZ", e.getMessage());
            }
        }
    }

    /**
     * 绘制描边蜘蛛网每一层的形状Path
     * 最后绘制每一个顶点到圆心的路径
     */
    protected void drawSpiderStoke(Canvas canvas) {
        float averageRadius = radius / hierarchyCount;
        for (int w = 1; w <= hierarchyCount; w++)
            canvas.drawPath(hierarchyByRadiusPath(averageRadius * w), spiderWebPaint);
        float nextAngle;
        float nextRadians;
        float nextPointX;
        float nextPointY;
        for (int position = 0; position < angleCount; position++) {
            nextAngle = offsetAngle + (position * averageAngle);
            nextRadians = (float) Math.toRadians(nextAngle);
            nextPointX = (float) (centerX + Math.sin(nextRadians) * radius);
            nextPointY = (float) (centerY - Math.cos(nextRadians) * radius);
            canvas.drawLine(centerX, centerY, nextPointX, nextPointY, spiderWebPaint);
        }
    }

    /**
     * 绘制分数图形
     */
    protected void drawScore(Canvas canvas) {
        if (scores == null || scores.length <= 0) return;
        path.reset();
        Log.i("ypzZZ","drawScore");
        float nextAngle;
        float nextRadians;
        float nextPointX;
        float nextPointY;
        float currentRadius;
        for (int position = 0; position < angleCount; position++) {
            currentRadius = (scores[position] / maxScore) * radius;
            nextAngle = offsetAngle + (position * averageAngle);
            nextRadians = (float) Math.toRadians(nextAngle);
            nextPointX = (float) (centerX + Math.sin(nextRadians) * currentRadius);
            nextPointY = (float) (centerY - Math.cos(nextRadians) * currentRadius);
            if (position == 0) path.moveTo(nextPointX, nextPointY);
            else path.lineTo(nextPointX, nextPointY);
        }
        path.close();
        canvas.drawPath(path, scoreBgPaint);
        // 绘制描边
        if (isScoreStroke) canvas.drawPath(path, scoreStrokePaint);
    }

    /**
     * 返回每一层蜘蛛网的path路径
     */
    private Path hierarchyByRadiusPath(float currentRadius) {
        path.reset();
        float nextAngle;
        float nextRadians;
        float nextPointX;
        float nextPointY;
        for (int position = 0; position < angleCount; position++) {
            nextAngle = offsetAngle + (position * averageAngle);
            nextRadians = (float) Math.toRadians(nextAngle);
            nextPointX = (float) (centerX + Math.sin(nextRadians) * currentRadius);
            nextPointY = (float) (centerY - Math.cos(nextRadians) * currentRadius);
            if (position == 0) path.moveTo(nextPointX, nextPointY);
            else path.lineTo(nextPointX, nextPointY);
        }
        path.close();
        return path;
    }


}