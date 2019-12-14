package com.superhong;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.superhong.exdialog.ExDialog;

import java.util.ArrayList;
import java.util.List;

import widget.sungrowpower.com.exdialog.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Hello World!
     */
    private Button mBt1;
    /**
     * Hello World!
     */
    private Button mBt2;
    /**
     * Hello World!
     */
    private Button mBt3;
    /**
     * Hello World!
     */
    private Button mBt4;
    /**
     * Hello World!
     */
    private Button mBt5;
    /**
     * Hello World!
     */
    private Button mBt6;

    private Button mBt7;

    private Button mBt8;

    private Button mBt9;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBt1 = findViewById(R.id.bt_1);
        mBt1.setOnClickListener(this);
        mBt2 = findViewById(R.id.bt_2);
        mBt2.setOnClickListener(this);
        mBt3 = findViewById(R.id.bt_3);
        mBt3.setOnClickListener(this);
        mBt4 = findViewById(R.id.bt_4);
        mBt4.setOnClickListener(this);
        mBt5 = findViewById(R.id.bt_5);
        mBt5.setOnClickListener(this);
        mBt6 = findViewById(R.id.bt_6);
        mBt6.setOnClickListener(this);
        mBt7 = findViewById(R.id.bt_7);
        mBt7.setOnClickListener(this);
        mBt8 = findViewById(R.id.bt_8);
        mBt8.setOnClickListener(this);
        mBt9 = findViewById(R.id.bt_9);
        mBt9.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.bt_1:

                // 标准提示
                new ExDialog.Builder(this)
                        .autoDismiss(true)
                        .title("温馨提示")
                        .content("明日天气：东风有雨,明日天气：东风有雨,明日天气：东风有雨,明日天气：东风有雨,明日天气：东风有雨。")
                        .onAction((dialog,isOk) -> showToast(isOk + "!"))
                        .build()
                        .show();

                break;
            case R.id.bt_2:
                new ExDialog.Builder(this)
                        .autoDismiss(true)
                        .title("温馨提示")
                        .onAction((dialog,isOk) -> showToast(isOk + "!"))
                        .build()
                        .show();
                break;
            case R.id.bt_3:
                ExDialog.Builder.newInstance(this)
                        .autoDismiss(true)
                        .title("温馨提示")
                        .content("明日天气：东风有雨")
                        .singleAction()
                        .onAction((dialog,isOk) -> showToast(isOk + "!"))
                        .build()
                        .show();
                break;
            case R.id.bt_4:
                ExDialog.Builder.newInstance(this)
                        .autoDismiss(true)
                        .title("温馨提示")
                        .content("明日天气：东风有雨")
                        .negativeText("淡定淡定淡定淡定 淡定淡定淡定淡定 淡定淡定淡定淡定 淡定淡定淡定 淡定淡定淡定淡定 淡定淡定淡定淡定")
                        .positiveText("打伞")
                        .contentColorRes(R.color.exd_sheet_cancel_text)
                        .negativeColor(getResources().getColor(R.color.colorPrimary))
                        .positiveColor(getResources().getColor(R.color.exd_black))
                        .onAction((dialog,isOk) -> showToast(isOk + "!"))
                        .build()
                        .show();
                break;

            case R.id.bt_5:
                ExDialog.Builder.newInstance(this)
                        .autoDismiss(true)
                        .title("成功")
                        .content("设备已经更新")
                        .iconRes(R.drawable.icon_success)
                        .singleAction()
                        .onAction((dialog,isOk) -> showToast(isOk + "!"))
                        .build()
                        .show();
                break;

            case R.id.bt_6:
                ExDialog.Builder.newInstance(this)
                        .autoDismiss(true)
                        .title("请输入密码")
                        .content("长度大于等于10位")
                        .inputTypeTextPassWord()
                        .input("请输入密码",(dialog,input) -> {
                            if (input.toString().trim().length() >= 10 || input.toString().trim().length() == 0) {
                                dialog.positiveView().setEnabled(true);
                            } else {
                                dialog.positiveView().setEnabled(false);
                            }

                        })
                        .onAction((dialog,isOk) -> {
                            if (isOk) {
                                showToast(dialog.positiveView().getText() + "!");
                            } else {
                                showToast("取消!");
                            }
                        })
                        .build()
                        .show();
                break;

            case R.id.bt_7:
                new ExDialog.Builder(this)
                        .autoDismiss(true)
                        .negativeBg(null)
                        .positiveBg(null)
                        .customView(R.layout.coustom,true)
                        .fullScreen()
                        .onAction((dialog,isOk) -> showToast(isOk + "!"))
                        .build()
                        .show();
                break;

            case R.id.bt_8:
                showList();
                break;
            case R.id.bt_9:
                showSheet();
                break;
        }
    }

    public void showList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list.add("苹果");
            list.add("菠萝");
            list.add("西瓜");
        }

        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            colors.add(Color.BLACK);
        }
        colors.set(0,Color.parseColor("#DB4437"));
        colors.set(2,Color.parseColor("#86B950"));

        new ExDialog.Builder(this)
                .list(list)
                .title("水果种类")
                .content("你好，欢迎你，请您选择您喜欢吃的水果，如果觉得满意，请给五星好评！")
                .itemColors(colors)
                //                .selectPosition(0)
                .listItemTextColorRes(R.color.exd_dialog_blue)
                .onItemClick((dialog,position) -> showToast(list.get(position)))
                .build()
                .show();
    }

    public void showSheet() {

        ArrayList<String> list = new ArrayList<>();
        list.add("苹果");
        list.add("菠萝");
        list.add("西瓜");
        list.add("鸭梨");

        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            colors.add(Color.BLACK);
        }
        colors.set(0,Color.parseColor("#DB4437"));
        colors.set(2,Color.parseColor("#86B950"));

        new ExDialog.Builder(this)
                .sheet(list)
                .itemColors(colors)
                .onItemClick((dialog,position) -> showToast(list.get(position)))
                .build()
                .show();
    }

    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.show();
    }
}
