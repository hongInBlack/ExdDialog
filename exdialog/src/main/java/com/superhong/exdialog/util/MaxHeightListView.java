package com.superhong.exdialog.util;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ListView;

/**
 * @author huangzhihong
 * @version 1.0
 * 先判断是否设定了mMaxHeight，如果设定了mMaxHeight，则直接使用mMaxHeight的值，
 * 如果没有设定mMaxHeight，则判断是否设定了mMaxRatio，如果设定了mMaxRatio的值 则使用此值与屏幕高度的乘积作为最高高度
 * @date 2018/8/11
 */
public class MaxHeightListView extends ListView {

    private static final float DEFAULT_MAX_RATIO = 0.6f;
    private static final int DEFAULT_MAX_HEIGHT = 0;

    private float mMaxRatio = DEFAULT_MAX_RATIO;// 优先级高
    private int mMaxHeight = DEFAULT_MAX_HEIGHT;// 优先级低

    public MaxHeightListView(Context context) {
        super(context);
        init();
    }

    public MaxHeightListView(Context context,AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public MaxHeightListView(Context context,AttributeSet attrs,int defStyle) {
        super(context,attrs,defStyle);
        init();
    }

    private void init() {
        if (mMaxHeight <= 0) {
            mMaxHeight = (int) (mMaxRatio * getScreenHeight(getContext()));
        } else {
            mMaxHeight = (int) Math.min(mMaxHeight,mMaxRatio * getScreenHeight(getContext()));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        if (getMeasuredHeight() > mMaxHeight) {
            int heightSize = mMaxHeight;
            int heightMode = MeasureSpec.EXACTLY;
            int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,heightMode);
            super.onMeasure(widthMeasureSpec,maxHeightMeasureSpec);
        }

    }

    /**
     * 获取屏幕高度
     * @param context
     */
    private int getScreenHeight(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            return size.y;
        }
        return 0;
    }

    public float getMaxRatio() {
        return mMaxRatio;
    }

    public void setMaxRatio(float maxRatio) {
        mMaxRatio = maxRatio;
    }

    public float getMaxHeight() {
        return mMaxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
    }
}