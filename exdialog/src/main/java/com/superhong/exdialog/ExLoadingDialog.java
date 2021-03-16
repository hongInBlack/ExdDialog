package com.superhong.exdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.superhong.exdialog.util.StepInterpolator;

/**
 * 自定义 LoadingDialog
 *
 * @author huangzhihong
 * @date 2021/3/16
 */
public class ExLoadingDialog extends Dialog {

    private String tip = "loading...";

    public ExLoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        final LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout mRootView = (LinearLayout) inflater.inflate(R.layout.exd_dialog_loading, null);
        setContentView(mRootView);
        AppCompatImageView icon = findViewById(R.id.img_loading);
        TextView textMsg = findViewById(R.id.text);
        if (TextUtils.isEmpty(tip)) {
            textMsg.setVisibility(View.GONE);
        } else {
            textMsg.setVisibility(View.VISIBLE);
            textMsg.setText(tip);
        }
        RotateAnimation rotate = new RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        StepInterpolator interpolator = new StepInterpolator(12);
        rotate.setInterpolator(interpolator);
        rotate.setDuration(1000);
        rotate.setRepeatCount(-1);
        icon.setAnimation(rotate);
        initWindow();
    }


    private void initWindow() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}