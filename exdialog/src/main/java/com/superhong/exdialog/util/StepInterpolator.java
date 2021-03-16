package com.superhong.exdialog.util;

import android.view.animation.Interpolator;

/**
 * @author huangzhihong
 * @version 1.0
 * @description
 * @date 2021/3/16
 */
public class StepInterpolator implements Interpolator {

    private int mStep;

    public StepInterpolator(int step) {
        this.mStep = step;
    }

    @Override
    public float getInterpolation(float input) {
        float per = 1f / mStep;
        return input - (input % per);
    }
}
