package cn.ypz.com.killetomrxmateria.rxwidget.spiderweb;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.ypz.com.killetomrxmateria.R;
import cn.ypz.com.killetomrxmateria.rxwidget.RxDimenUtils;
import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseViewGroup;

import static cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderConstant.LOCATION_EAST;
import static cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderConstant.LOCATION_EAST_NORTH;
import static cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderConstant.LOCATION_EAST_SOUTH;
import static cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderConstant.LOCATION_NORTH;
import static cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderConstant.LOCATION_SOUTH;
import static cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderConstant.LOCATION_WEST;
import static cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderConstant.LOCATION_WEST_NORTH;
import static cn.ypz.com.killetomrxmateria.rxwidget.spiderweb.RxSpiderConstant.LOCATION_WEST_SOUTH;

public class RxSpiderWebLayout extends BaseViewGroup {

    private RxEthanSpiderWeb spiderWeb;
    private boolean isBuildIn;
    private float spiderWebMargin;
    private boolean isComplexOffset;
    private float centerX;    // 中心点X坐标
    private float centerY;    // 中心点Y坐标
    private float radius;   // 半径
    private float measureSpec;
    private int childCount;
    private float spacing = RxDimenUtils.dpToPx(8, getResources());

    public RxSpiderWebLayout(Context context) {
        super(context);
    }

    public RxSpiderWebLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initAttr(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RxSpiderWebLayout);
            isBuildIn = typedArray.getBoolean(R.styleable.RxSpiderWebLayout_isBuildInSpider, false);
            isComplexOffset = typedArray.getBoolean(R.styleable.RxSpiderWebLayout_isComplexOffset, RxSpiderConstant.isComplexOffset);
            //判断是否需要自带蜘蛛网生成
            if (isBuildIn) {
                if (spiderWeb == null) spiderWeb = new RxEthanSpiderWeb(context);
                Log.i("ypz", typedArray.getInt(R.styleable.RxSpiderWebLayout_angleCount, RxSpiderConstant.angleCount) + "");
                spiderWeb.setAngleCount(typedArray.getInt(R.styleable.RxSpiderWebLayout_angleCount, RxSpiderConstant.angleCount));
                spiderWeb.setHierarchyCount(typedArray.getInt(R.styleable.RxSpiderWebLayout_hierarchyCount, RxSpiderConstant.hierarchyCount));
                spiderWeb.setMaxScore(typedArray.getFloat(R.styleable.RxSpiderWebLayout_maxScore, RxSpiderConstant.maxScore));
                spiderWeb.setComplexOffset(isComplexOffset);
                spiderWebMargin = typedArray.getDimension(R.styleable.RxSpiderWebLayout_spiderMargin, RxDimenUtils.dpToPx(25, getResources()));
                //判读是否存在设置蛛网背景 默认存在
                if (typedArray.getBoolean(R.styleable.RxSpiderWebLayout_isSpiderBg, RxSpiderConstant.isSpiderBg)) {
                    spiderWeb.setSpiderBg(true);
                    spiderWeb.setSpiderColor(typedArray.getResourceId(R.styleable.RxSpiderWebLayout_spiderBg, RxSpiderConstant.spiderColor));
                } else spiderWeb.setSpiderBg(false);
                //判断是否存在蛛网层次绘画 默认不存在
                if (typedArray.getBoolean(R.styleable.RxSpiderWebLayout_isSpiderStroke, RxSpiderConstant.isSpiderStroke)) {
                    spiderWeb.setSpiderStroke(true);
                    spiderWeb.setSpiderStrokeWidth(typedArray.getDimension(R.styleable.RxSpiderWebLayout_spiderStrokeWidth, RxDimenUtils.dpToPx(1, getResources())));
                    spiderWeb.setSpiderStrokeColor(typedArray.getResourceId(R.styleable.RxSpiderWebLayout_spiderStokeBg, RxSpiderConstant.spiderStrokeColor));
                } else spiderWeb.setSpiderStroke(false);
                //判断是否存在分数绘画背景 默认存在
                if (typedArray.getBoolean(R.styleable.RxSpiderWebLayout_isScoreBg, RxSpiderConstant.isScoreBg)) {
                    spiderWeb.setScoreBg(true);
                    spiderWeb.setScoreColor(typedArray.getResourceId(R.styleable.RxSpiderWebLayout_scoreBg, R.color.steelblue));
                } else spiderWeb.setScoreBg(false);
                //判断是否存在分数绘画描线 默认不存在
                if (typedArray.getBoolean(R.styleable.RxSpiderWebLayout_isScoreStroke, RxSpiderConstant.isScoreStroke)) {
                    spiderWeb.setScoreStroke(true);
                    spiderWeb.setScoreStrokeWidth(typedArray.getDimension(R.styleable.RxSpiderWebLayout_spiderStrokeWidth, RxDimenUtils.dpToPx(1, getResources())));
                    spiderWeb.setScoreStrokeColor(typedArray.getResourceId(R.styleable.RxSpiderWebLayout_scoreStokeBg, RxSpiderConstant.spiderStrokeColor));
                } else spiderWeb.setScoreStroke(false);
                if (typedArray.getBoolean(R.styleable.RxSpiderWebLayout_isGradientSpider, RxSpiderConstant.isGradientSpider)) {
                    spiderWeb.setGradientSpider(true);
                    spiderWeb.setSpiderEndColor(typedArray.getResourceId(R.styleable.RxSpiderWebLayout_spiderEndBg, RxSpiderConstant.spiderEndColor));
                }
                addView(spiderWeb);
                reset();
            }
            typedArray.recycle();
        } else Log.i("ypz", "!!!!!!layout");
    }

    private void reset() {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        centerX = viewWidth / 2;
        centerY = viewHeight / 2;
        radius = Math.min(viewWidth, viewHeight) / 2;
        if (isBuildIn) {
            measureSpec = (int) radius * 2;
            measureSpec -= (spiderWebMargin * 2);
            spiderWeb.measure((int) measureSpec, (int) measureSpec);
        }
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
        childCount = getChildCount();
        reset();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (childCount == 0) return;
        // 循环处理每个位置的子View，先计算出子View在圆上所处的位置，然后按照其子View的宽高偏移一定的距离，保证子View全部在圆圈之外，并且子View的中心点和其圆上的点连同圆心在一条直线上
        View childView;
        float nextAngle;
        float nextRadians;
        float nextPointX;
        float nextPointY;
        int childViewMeasuredWidth;
        int childViewMeasuredHeight;
        float childViewLeft;
        float childViewTop;
        float averageAngle;
        if (isBuildIn && spiderWeb != null)
            averageAngle = childCount > 1 ? 360 / (childCount - 1) : 0;
        else averageAngle = childCount > 0 ? 360 / childCount : 0;
        float offsetAngle = 0;
        if (isComplexOffset) offsetAngle = averageAngle / 2;
        for (int position = 0; position < childCount; position++) {
            childView = getChildAt(position);
            childViewMeasuredWidth = childView.getMeasuredWidth();
            childViewMeasuredHeight = childView.getMeasuredHeight();
            if (childView == spiderWeb) {
                childViewLeft = centerX - measureSpec / 2;
                childViewTop = centerY - measureSpec / 2;
                childView.layout((int) childViewLeft, (int) childViewTop, (int) (childViewLeft + measureSpec), (int) (childViewTop + measureSpec));
            } else {
                nextAngle = offsetAngle + (position * averageAngle);
                nextRadians = (float) Math.toRadians(nextAngle);
                if (isBuildIn){
                    float builderRadius = radius-spiderWebMargin/8*7;
                    nextPointX = (float) (centerX + Math.sin(nextRadians) * builderRadius);
                    nextPointY = (float) (centerY - Math.cos(nextRadians) * builderRadius);
                    spacing /= 8;
                }else {
                    nextPointX = (float) (centerX + Math.sin(nextRadians) * radius);
                    nextPointY = (float) (centerY - Math.cos(nextRadians) * radius);
                }
                childViewLeft = nextPointX;
                childViewTop = nextPointY;
                switch (calculateLocationByAngle(nextAngle)) {
                    case LOCATION_NORTH :
                        childViewLeft -= childViewMeasuredWidth / 2;
                        childViewTop -= childViewMeasuredHeight;
                        childViewTop -= spacing;
                        break;
                    case LOCATION_EAST_NORTH :
                        childViewTop -= childViewMeasuredHeight / 2;
                        childViewLeft += spacing;
                        break;
                    case LOCATION_EAST :
                        childViewTop -= childViewMeasuredHeight / 2;
                        childViewLeft += spacing;
                        break;
                    case LOCATION_EAST_SOUTH :
                        childViewLeft += spacing;
                        childViewTop += spacing;
                        break;
                    case LOCATION_SOUTH :
                        childViewLeft -= childViewMeasuredWidth / 2;
                        childViewTop += spacing;
                        break;
                    case LOCATION_WEST_SOUTH :
                        childViewLeft -= childViewMeasuredWidth;
                        childViewLeft -= spacing;
                        childViewTop += spacing;
                        break;
                    case LOCATION_WEST :
                        childViewLeft -= childViewMeasuredWidth;
                        childViewTop -= childViewMeasuredHeight / 2;
                        childViewLeft -= spacing;
                        break;
                    case LOCATION_WEST_NORTH :
                        childViewLeft -= childViewMeasuredWidth;
                        childViewTop -= childViewMeasuredHeight / 2;
                        childViewLeft -= spacing;
                        childViewTop -= spacing;
                        break;
                }
                childView.layout((int) childViewLeft, (int) childViewTop, (int) (childViewLeft + childViewMeasuredWidth), (int) (childViewTop + childViewMeasuredHeight));
            }
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        reset();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isSetDimen(widthMeasureSpec)) {
            widthMeasureSpec = RxDimenUtils.dpToPx(150, getResources());
        }
        if (!isSetDimen(heightMeasureSpec)) {
            heightMeasureSpec = RxDimenUtils.dpToPx(150, getResources());
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setBuildInScores(float[] scores) {
        Log.i("ypzZZ", "scores");
        if (scores != null && scores.length > 0 && isBuildIn && spiderWeb != null) {
            Log.i("ypzZZ", "setScores");
            spiderWeb.setScores(scores);
        }

    }

    public boolean isBuildIn() {
        return isBuildIn;
    }

    /**
     * 根据角度判断所处的方位，一个圆分成了8个方位（东、南、西、北、西北、东北、西南、东南），不同的方位有不同的偏移方式
     *
     * @param angle 角度
     * @return 方位
     */
    private int calculateLocationByAngle(float angle) {
        if ((angle >= 337.5f && angle <= 360f) || (angle >= 0f && angle <= 22.5f)) {
            return LOCATION_NORTH;
        } else if (angle >= 22.5f && angle <= 67.5f) {
            return LOCATION_EAST_NORTH;
        } else if (angle >= 67.5f && angle <= 112.5f) {
            return LOCATION_EAST;
        } else if (angle >= 112.5f && angle <= 157.5) {
            return LOCATION_EAST_SOUTH;
        } else if (angle >= 157.5 && angle <= 202.5) {
            return LOCATION_SOUTH;
        } else if (angle >= 202.5 && angle <= 247.5) {
            return LOCATION_WEST_SOUTH;
        } else if (angle >= 247.5 && angle <= 292.5) {
            return LOCATION_WEST;
        } else if (angle >= 292.5 && angle <= 337.5) {
            return LOCATION_WEST_NORTH;
        } else {
            throw new IllegalArgumentException("error angle " + angle);
        }
    }
}