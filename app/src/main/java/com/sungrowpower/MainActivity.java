package com.sungrowpower;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sungrowpower.widget.exdialog.ExDialog;

import java.util.ArrayList;

import widget.sungrowpower.com.exdialog.R;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

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
        mBt1 = (Button) findViewById(R.id.bt_1);
        mBt1.setOnClickListener(this);
        mBt2 = (Button) findViewById(R.id.bt_2);
        mBt2.setOnClickListener(this);
        mBt3 = (Button) findViewById(R.id.bt_3);
        mBt3.setOnClickListener(this);
        mBt4 = (Button) findViewById(R.id.bt_4);
        mBt4.setOnClickListener(this);
        mBt5 = (Button) findViewById(R.id.bt_5);
        mBt5.setOnClickListener(this);
        mBt6 = (Button) findViewById(R.id.bt_6);
        mBt6.setOnClickListener(this);
        mBt7 = (Button) findViewById(R.id.bt_7);
        mBt7.setOnClickListener(this);
        mBt8 = (Button) findViewById(R.id.bt_8);
        mBt8.setOnClickListener(this);
        mBt9 = (Button) findViewById(R.id.bt_9);
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
                        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
                        .build()
                        .show();

                break;
            case R.id.bt_2:
                new ExDialog.Builder(this)
                        .autoDismiss(true)
                        .title("温馨提示")
                        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
                        .build()
                        .show();
                break;
            case R.id.bt_3:
                new ExDialog.Builder(this)
                        .autoDismiss(true)
                        .title("温馨提示")
                        .content("明日天气：东风有雨")
                        .singleAction()
                        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
                        .build()
                        .show();
                break;
            case R.id.bt_4:
                new ExDialog.Builder(this)
                        .autoDismiss(true)
                        .title("温馨提示")
                        .content("明日天气：东风有雨")
                        .negativeText("淡定")
                        .positiveText("打伞")
                        .contentColorRes(R.color.exd_sheet_cancel_text)
                        .negativeColor(getResources().getColor(R.color.colorPrimary))
                        .positiveColor(getResources().getColor(R.color.exd_black))
                        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
                        .build()
                        .show();
                break;

            case R.id.bt_5:
                new ExDialog.Builder(this)
                        .autoDismiss(true)
                        .title("成功")
                        .content("设备已经更新")
                        .iconRes(R.drawable.icon_success)
                        .singleAction()
                        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
                        .build()
                        .show();
                break;

            case R.id.bt_6:
                new ExDialog.Builder(this)
                        .autoDismiss(true)
                        .title("请输入密码")
                        .content("长度大于等于10位")
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
                        .customView(R.layout.coustom,true)
                        .onAction((dialog,isOk) -> showToast(String.valueOf(isOk) + "!"))
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
        for (int i = 0; i < 6; i++) {
            list.add("苹果");
            list.add("菠萝");
            list.add("西瓜");
            list.add("鸭梨");
        }

        new ExDialog.Builder(this)
                .list(list)
                .title("水果种类")
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

        new ExDialog.Builder(this)
                .sheet(list)
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
