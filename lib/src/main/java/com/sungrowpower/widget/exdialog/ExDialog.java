package com.sungrowpower.widget.exdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AndroidException;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sungrowpower.widget.R;
import com.sungrowpower.widget.exdialog.util.DialogUtils;
import com.sungrowpower.widget.exdialog.util.MaxHeightLayout;
import com.sungrowpower.widget.exdialog.util.MaxHeightListView;

import java.util.List;

/**
 * 可扩展的自定义 Dialog
 * @author huangzhihong
 * @date 2018/8/8
 */
public class ExDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Builder mBuilder;

    private TextView mExdTitle;
    private ImageView mExdIcon;
    private TextView mExdMsg;
    private TextView mExdCancel;
    private TextView mExdConfirm;
    private EditText mExdInput;
    private View mExdTitleLine;
    private ListView mListView;
    private TextView mTvSheetCancel;
    private LinearLayout mLlSheetRoot;
    private View mExdLineVertical;
    private FrameLayout mRootView;

    public ExDialog(Builder builder) {
        super(builder.getContext(),builder.mThemeResId);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView();
        findView();
        switch (mBuilder.mTypeEnum) {
            case LIST:
                bindListView();
                break;
            case SHEET:
                bindSheetView();
                break;
            case DIALOG:
            default:
                setDialogView();
                break;
        }
        initWindow();
        initDialogData();
    }

    private void setContentView() {

        final LayoutInflater inflater = LayoutInflater.from(getContext());

        switch (mBuilder.mTypeEnum) {
            case LIST:
                mRootView = (MaxHeightLayout) inflater.inflate(R.layout.exd_dialog_list,null);
                break;
            case SHEET:
                mRootView = (FrameLayout) inflater.inflate(R.layout.exd_dialog_sheet,null);
                break;
            case DIALOG:
            default:
                if (mBuilder.mCustomView != null) {

                    if (mBuilder.mWithFootView) {
                        mRootView = (MaxHeightLayout) inflater.inflate(R.layout.exd_dialog_custom_with_foot,null);
                        FrameLayout customContainer = (FrameLayout) mRootView.findViewById(R.id.custom_container);
                        customContainer.addView(mBuilder.mCustomView);
                    } else {
                        mRootView = (MaxHeightLayout) inflater.inflate(R.layout.exd_dialog_custom,null);
                        mRootView.addView(mBuilder.mCustomView);
                    }

                } else {
                    mRootView = (MaxHeightLayout) inflater.inflate(R.layout.exd_dialog_common,null);
                }
                break;
        }
        setContentView(mRootView);
    }

    private void findView() {

        this.mExdTitle = (TextView) findViewById(R.id.exd_title);
        this.mExdIcon = (ImageView) findViewById(R.id.exd_icon);
        this.mExdMsg = (TextView) findViewById(R.id.exd_msg);
        this.mExdCancel = (TextView) findViewById(R.id.exd_cancel);
        this.mExdLineVertical = (View) findViewById(R.id.exd_line_vertical);
        this.mExdConfirm = (TextView) findViewById(R.id.exd_confirm);
        this.mExdInput = (EditText) findViewById(R.id.exd_input);
        this.mExdTitleLine = findViewById(R.id.exd_title_line);
        this.mListView = (ListView) findViewById(R.id.exd_list);
        this.mTvSheetCancel = (TextView) findViewById(R.id.exd_sheet_cancel);
        this.mLlSheetRoot = (LinearLayout) findViewById(R.id.sheet_root);
    }

    private void bindDialogView() {
        // 标题
        if (TextUtils.isEmpty(mBuilder.title)) {
            mExdTitle.setVisibility(View.GONE);
        } else {
            mExdTitle.setText(mBuilder.title);
        }
        if (mBuilder.mTitleColor != -1) {
            mExdTitle.setTextColor(mBuilder.mTitleColor);
        }

        // 内容
        if (TextUtils.isEmpty(mBuilder.content)) {
            mExdMsg.setVisibility(View.GONE);
        } else {
            mExdMsg.setText(mBuilder.content);
        }
        if (mBuilder.mContentColor != -1) {
            mExdMsg.setTextColor(mBuilder.mContentColor);
        }
        // icon
        if (mBuilder.icon == null) {
            mExdIcon.setVisibility(View.GONE);
        } else {
            mExdIcon.setImageDrawable(mBuilder.icon);
        }

        // input
        if (mBuilder.mInputSet) {
            mExdInput.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(mBuilder.mInputHint)) {
                mExdInput.setHint(mBuilder.mInputHint);
            }
            if (mBuilder.mInputHintColor != -1) {
                mExdInput.setHintTextColor(mBuilder.mInputHintColor);
            }
            if (mBuilder.mInputColor != -1) {
                mExdInput.setTextColor(mBuilder.mInputColor);
            }
            if (mBuilder.mInputBg != null) {
                mExdInput.setBackground(mBuilder.mInputBg);
            }
            if (mBuilder.mInputFilters != null) {
                mExdInput.setFilters(mBuilder.mInputFilters);
            }
            mExdInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s,int start,int count,int after) {

                }

                @Override
                public void onTextChanged(CharSequence s,int start,int before,int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mBuilder.mInputCallback != null) {
                        mBuilder.mInputCallback.onInput(ExDialog.this,s);
                    }
                }
            });

        } else {
            mExdInput.setVisibility(View.GONE);
        }
    }

    private void initFootView() {
        // 取消
        if (mBuilder.mSingleAction) {
            mExdCancel.setVisibility(View.GONE);
            mExdLineVertical.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(mBuilder.mNegativeText)) {
                mExdCancel.setText(mBuilder.mNegativeText);
            }
        }

        if (mBuilder.mNegativeTextColor != null) {
            mExdCancel.setTextColor(mBuilder.mNegativeTextColor);
        }

        if (mBuilder.mNegativeBg != null) {
            mExdCancel.setBackground(mBuilder.mNegativeBg);
        }

        //  确定
        if (!TextUtils.isEmpty(mBuilder.mPositiveText)) {
            mExdConfirm.setText(mBuilder.mPositiveText);
        }
        if (mBuilder.mPositiveTextColor != null) {
            mExdConfirm.setTextColor(mBuilder.mPositiveTextColor);
        }
        if (mBuilder.mPositiveBg != null) {
            mExdConfirm.setBackground(mBuilder.mPositiveBg);
        }

        mExdCancel.setOnClickListener(this);
        mExdConfirm.setOnClickListener(this);
    }

    private void initLayout() {

        // 内容文本单行居中，多行左对齐
        mExdConfirm.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        if (mExdMsg != null) {
                            if (mExdMsg.getLineCount() > 1 || mBuilder.mInputSet) {
                                mExdMsg.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                            } else {
                                mExdMsg.setGravity(Gravity.CENTER);
                            }
                        }

                        int max = Math.max(mExdCancel.getHeight(),mExdConfirm.getHeight());
                        mExdCancel.setHeight(max);
                        mExdConfirm.setHeight(max);

                        mExdConfirm.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
        );

    }

    private void setDialogView() {
        if (mBuilder.mCustomView == null) {
            bindDialogView();
        }
        if (mBuilder.mCustomView == null || mBuilder.mWithFootView) {
            initFootView();
            initLayout();
        }
    }

    private void bindListView() {
        if (TextUtils.isEmpty(mBuilder.title)) {
            mExdTitle.setVisibility(View.GONE);
            mExdTitleLine.setVisibility(View.GONE);
        } else {
            mExdTitle.setText(mBuilder.title);
        }
        if (mBuilder.mTitleColor != -1) {
            mExdTitle.setTextColor(mBuilder.mTitleColor);
        }
        mListView.setAdapter(new ListAdapter(mBuilder));
        if (mBuilder.mListDividerColor != -1) {
            mListView.setDivider(new ColorDrawable(mBuilder.mListDividerColor));
        }
        mListView.setOnItemClickListener(this);
    }

    private void bindSheetView() {

        mListView.setAdapter(new ListAdapter(mBuilder));
        if (mBuilder.mListDividerColor != -1) {
            mListView.setDivider(new ColorDrawable(mBuilder.mListDividerColor));
        }
        mListView.setOnItemClickListener(this);

        if (!TextUtils.isEmpty(mBuilder.mSheetCancelText)) {
            mTvSheetCancel.setText(mBuilder.mSheetCancelText);
        }
        if (mBuilder.mSheetCancelColor != -1) {
            mTvSheetCancel.setTextColor(mBuilder.mSheetCancelColor);
        }
        mLlSheetRoot.setOnClickListener(this);
        mTvSheetCancel.setOnClickListener(this);

        // 解决虚拟按键遮挡弹框问题
        mTvSheetCancel.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mTvSheetCancel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int[] outLocation1 = new int[2];
                        mRootView.getLocationOnScreen(outLocation1);

                        int[] outLocation2 = new int[2];
                        View rootView = ((Activity) (mBuilder.mContext)).findViewById(android.R.id.content);
                        rootView.getLocationOnScreen(outLocation2);

                        int bottom = rootView.getHeight() + outLocation2[1];

                        if (mRootView.getHeight() + outLocation1[1] > bottom) {
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTvSheetCancel
                                    .getLayoutParams();

                            int bottomMargin = layoutParams.bottomMargin + mRootView.getHeight()
                                    + outLocation1[1] - bottom;
                            layoutParams.setMargins(layoutParams.leftMargin,
                                    layoutParams.topMargin,
                                    layoutParams.rightMargin,
                                    bottomMargin);

                            mTvSheetCancel.setLayoutParams(layoutParams);
                        }
                    }
                });
    }

    private void initWindow() {

        Window window = getWindow();
        if (window == null) {
            return;
        }
        if (mBuilder.background != null) {
            window.setBackgroundDrawable(mBuilder.background);
        } else {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        if (mBuilder.mTypeEnum == TypeEnum.SHEET) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        WindowManager wm = window.getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int windowWidth = size.x;
        final int windowHeight = size.y;

        final int windowVerticalPadding =
                mBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.exd_dialog_vertical_margin);
        final int windowHorizontalPadding =
                mBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.exd_dialog_horizontal_margin);
        final int maxWidth =
                mBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.exd_dialog_max_width);
        final int calculatedWidth = windowWidth - (windowHorizontalPadding * 2);
        final int maxHeight = windowHeight - windowVerticalPadding * 2;
        final int maxListViewHeight = maxHeight
                - mBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.exd_dialog_sheet_cancel_height);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        if (mBuilder.mTypeEnum == TypeEnum.SHEET) {
            ((MaxHeightListView) mListView).setMaxHeight(maxListViewHeight);
            lp.width = windowWidth;
            lp.height = windowHeight;
        } else {
            ((MaxHeightLayout) mRootView).setMaxHeight(maxHeight);
            lp.width = Math.min(maxWidth,calculatedWidth);
        }
        getWindow().setAttributes(lp);
    }

    private void initDialogData() {

        setCancelable(mBuilder.mCancelable);
        setCanceledOnTouchOutside(mBuilder.mCanceledOnTouchOutside);
        setOnDismissListener(mBuilder.mDismissListener);
        setOnCancelListener(mBuilder.mCancelListener);
        setOnKeyListener(mBuilder.mKeyListener);
        setOnShowListener(mBuilder.mShowListener);
    }

    @Override
    public void onClick(View v) {
        if (mBuilder.mAutoDismiss || mBuilder.mTypeEnum != TypeEnum.DIALOG) {
            dismiss();
        }
        if (mBuilder.mActionButtonCallback == null) {
            return;
        }
        if (v.equals(mExdCancel)) {
            mBuilder.mActionButtonCallback.onClick(this,false);
            return;
        }
        if (v.equals(mExdConfirm)) {
            mBuilder.mActionButtonCallback.onClick(this,true);
        }

    }

    @Override
    public void show() {
        super.show();

        if (mBuilder.mTypeEnum != TypeEnum.SHEET) {
            return;
        }
        int anim = R.anim.exd_slide_in_bottom;
        Animation animation = AnimationUtils.loadAnimation(mBuilder.mContext,anim);
        mRootView.startAnimation(animation);
    }

    @Override
    public void dismiss() {
        if (mBuilder.mTypeEnum != TypeEnum.SHEET) {
            super.dismiss();
            return;
        }

        int anim = R.anim.exd_slide_out_bottom;
        Animation animation = AnimationUtils.loadAnimation(mBuilder.mContext,anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ExDialog.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mRootView.startAnimation(animation);

    }

    public TextView titleView() {
        return mExdTitle;
    }

    public ImageView iconView() {
        return mExdIcon;
    }

    public TextView contentView() {
        return mExdMsg;
    }

    public TextView negativeView() {
        return mExdCancel;
    }

    public TextView positiveView() {
        return mExdConfirm;
    }

    public EditText inputView() {
        return mExdInput;
    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
        dismiss();
        if (mBuilder.mOnItemClick != null) {
            mBuilder.mOnItemClick.onItemClick(ExDialog.this,position);
        }
    }

    public interface ActionButtonCallback {

        void onClick(ExDialog dialog,Boolean isPositive);
    }


    public interface OnItemClick {

        void onItemClick(ExDialog dialog,int position);
    }


    public interface InputCallback {

        void onInput(ExDialog dialog,CharSequence input);
    }


    public static class Builder {

        protected Context mContext;
        protected int mThemeResId;
        protected View mCustomView;

        protected CharSequence title;
        protected CharSequence content;
        protected CharSequence mPositiveText;
        protected CharSequence mNegativeText;
        protected CharSequence mInputHint;
        protected CharSequence mListTitle;
        protected CharSequence mSheetCancelText;

        protected Drawable background;
        protected Drawable icon;

        protected int mTitleColor;
        protected int mContentColor;
        protected ColorStateList mPositiveTextColor;
        protected ColorStateList mNegativeTextColor;

        protected ColorStateList mItemSelector;
        protected int mListItemTextColor = -1;
        protected int mSheetItemTextColor = -1;
        protected int mListDividerColor = -1;
        protected int mSheetCancelColor = -1;

        protected Drawable mPositiveBg;
        protected Drawable mNegativeBg;

        @DrawableRes
        protected int mBgSelectorPositive;
        @DrawableRes
        protected int mBgSelectorNegative;

        protected int mInputColor = -1;
        protected int mInputHintColor = -1;
        protected Drawable mInputBg;

        protected boolean mCancelable = true;
        protected boolean mCanceledOnTouchOutside = false;
        protected boolean mAutoDismiss = true;
        protected OnDismissListener mDismissListener;
        protected OnCancelListener mCancelListener;
        protected OnKeyListener mKeyListener;
        protected OnShowListener mShowListener;
        protected ActionButtonCallback mActionButtonCallback;
        protected OnItemClick mOnItemClick;
        protected InputCallback mInputCallback;
        protected InputFilter[] mInputFilters;

        protected TypeEnum mTypeEnum = TypeEnum.DIALOG;
        protected List<String> mList;
        protected boolean mInputSet = false;
        protected boolean mSingleAction = false;
        protected boolean mWithFootView = false;

        public Builder(Context context) {
            init(context);
        }

        private void init(Context context) {

            this.mContext = context;
            TypedArray a = context.getTheme().obtainStyledAttributes(null,
                    R.styleable.ExDialog,R.attr.exDialogStyle,0);

            int themeId = a.getResourceId(R.styleable.ExDialog_exdTheme,0);
            this.mThemeResId = themeId != 0 ? themeId : R.style.ExdStyle;
            this.background = a.getDrawable(R.styleable.ExDialog_exdBackground);
            if (background == null) {
                background = context.getResources().getDrawable(R.drawable.shape_radius_12);
            }
            this.mTitleColor = a.getColor(R.styleable.ExDialog_exdTitleColor,-1);
            this.mContentColor = a.getColor(R.styleable.ExDialog_exdContentColor,-1);
            this.mPositiveText = a.getString(R.styleable.ExDialog_exdPositiveText);
            this.mNegativeText = a.getString(R.styleable.ExDialog_exdNegativeText);
            this.mSheetCancelText = a.getString(R.styleable.ExDialog_exdSheetCancelText);

            this.mPositiveTextColor = a.getColorStateList(R.styleable.ExDialog_exdPositiveTextSelector);
            this.mNegativeTextColor = a.getColorStateList(R.styleable.ExDialog_exdNegativeTextSelector);
            this.mPositiveBg = a.getDrawable(R.styleable.ExDialog_exdPositiveBgSelector);
            this.mNegativeBg = a.getDrawable(R.styleable.ExDialog_exdNegativeBgSelector);

            this.mInputBg = a.getDrawable(R.styleable.ExDialog_exdInputBg);
            this.mInputColor = a.getColor(R.styleable.ExDialog_exdInputColor,-1);
            this.mInputHintColor = a.getColor(R.styleable.ExDialog_exdInputHintColor,-1);

            this.mListDividerColor = a.getColor(R.styleable.ExDialog_exdDividerColor,-1);
            this.mListItemTextColor = a.getColor(R.styleable.ExDialog_exdListItemColor,-1);
            this.mSheetItemTextColor = a.getColor(R.styleable.ExDialog_exdSheetItemColor,-1);
            this.mItemSelector = a.getColorStateList(R.styleable.ExDialog_exdListSelector);
            this.mSheetCancelColor = a.getColor(R.styleable.ExDialog_exdSheetCancelColor,-1);

            a.recycle();
        }

        public final Context getContext() {
            return mContext;
        }

        public Builder themeResId(@StyleRes int id) {
            this.mThemeResId = id;
            return this;
        }

        public Builder list(List<String> list) {
            this.mTypeEnum = TypeEnum.LIST;
            this.mList = list;
            return this;
        }

        public Builder sheet(List<String> list) {
            this.mTypeEnum = TypeEnum.SHEET;
            this.mList = list;
            return this;
        }

        public Builder onItemClick(OnItemClick itemClick) {
            this.mOnItemClick = itemClick;
            return this;
        }

        public Builder sheetCancelText(@StringRes int titleRes) {
            sheetCancelText(this.mContext.getText(titleRes));
            return this;
        }

        public Builder sheetCancelText(CharSequence title) {
            this.mSheetCancelText = title;
            return this;
        }

        public Builder listTitle(@StringRes int titleRes) {
            listTitle(this.mContext.getText(titleRes));
            return this;
        }

        public Builder listTitle(CharSequence title) {
            this.mListTitle = title;
            return this;
        }

        public Builder title(@StringRes int titleRes) {
            title(this.mContext.getText(titleRes));
            return this;
        }

        public Builder title(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder titleColor(@ColorInt int color) {
            this.mTitleColor = color;
            return this;
        }

        public Builder titleColorRes(@ColorRes int colorRes) {
            return titleColor(DialogUtils.getColor(this.mContext,colorRes));
        }

        public Builder titleColorAttr(@AttrRes int colorAttr) {
            return titleColor(DialogUtils.resolveColor(this.mContext,colorAttr));
        }

        public Builder icon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder iconRes(@DrawableRes int icon) {
            this.icon = ResourcesCompat.getDrawable(mContext.getResources(),icon,null);
            return this;
        }

        public Builder iconAttr(@AttrRes int iconAttr) {
            this.icon = DialogUtils.resolveDrawable(mContext,iconAttr);
            return this;
        }

        public Builder content(@StringRes int contentRes) {
            return content(contentRes,false);
        }

        public Builder content(@StringRes int contentRes,boolean html) {
            CharSequence text = this.mContext.getText(contentRes);
            if (html) {
                text = Html.fromHtml(text.toString().replace("\n","<br/>"));
            }
            return content(text);
        }

        public Builder content(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder contentColor(@ColorInt int color) {
            this.mContentColor = color;
            return this;
        }

        public Builder contentColorRes(@ColorRes int colorRes) {
            contentColor(DialogUtils.getColor(this.mContext,colorRes));
            return this;
        }

        public Builder contentColorAttr(@AttrRes int colorAttr) {
            contentColor(DialogUtils.resolveColor(this.mContext,colorAttr));
            return this;
        }

        public Builder input(
                @StringRes int hint,
                InputCallback callback) {
            return input(
                    hint == 0 ? null : mContext.getText(hint),
                    callback);
        }

        public Builder input(
                @Nullable CharSequence hint,
                InputCallback callback) {

            this.mInputSet = true;
            this.mInputCallback = callback;
            this.mInputHint = hint;
            return this;
        }

        public Builder inputFilters(@Nullable InputFilter... filters) {
            this.mInputFilters = filters;
            return this;
        }

        public Builder inputColor(@ColorInt int color) {
            this.mInputColor = color;
            return this;
        }

        public Builder inputColorRes(@ColorRes int colorRes) {
            inputColor(DialogUtils.getColor(this.mContext,colorRes));
            return this;
        }

        public Builder inputColorAttr(@AttrRes int colorAttr) {
            inputColor(DialogUtils.resolveColor(this.mContext,colorAttr));
            return this;
        }

        public Builder inputHintColor(@ColorInt int color) {
            this.mInputHintColor = color;
            return this;
        }

        public Builder inputHintColorRes(@ColorRes int colorRes) {
            inputHintColor(DialogUtils.getColor(this.mContext,colorRes));
            return this;
        }

        public Builder inputHintColorAttr(@AttrRes int colorAttr) {
            inputHintColor(DialogUtils.resolveColor(this.mContext,colorAttr));
            return this;
        }

        public Builder inputBgRes(@DrawableRes int colorRes) {
            return inputBgRes(mContext.getResources().getDrawable(colorRes));
        }

        public Builder inputBgResAttr(@AttrRes int colorAttr) {
            return inputBgRes(DialogUtils.resolveDrawable(this.mContext,colorAttr,null));
        }

        public Builder inputBgRes(Drawable drawable) {
            this.mInputBg = drawable;
            return this;
        }

        public Builder positiveText(@StringRes int positiveRes) {
            if (positiveRes == 0) {
                return this;
            }
            positiveText(this.mContext.getText(positiveRes));
            return this;
        }

        /**
         * 只有确定按钮
         * @return
         */
        public Builder singleAction() {
            this.mSingleAction = true;
            return this;
        }

        public Builder positiveText(CharSequence message) {
            this.mPositiveText = message;
            return this;
        }

        public Builder positiveColor(@ColorInt int color) {
            return positiveColor(DialogUtils.getActionTextStateList(mContext,color));
        }

        public Builder positiveColorRes(@ColorRes int colorRes) {
            return positiveColor(DialogUtils.getActionTextColorStateList(this.mContext,colorRes));
        }

        public Builder positiveColorAttr(@AttrRes int colorAttr) {
            return positiveColor(
                    DialogUtils.resolveActionTextColorStateList(this.mContext,colorAttr,null));
        }

        public Builder positiveColor(ColorStateList colorStateList) {
            this.mPositiveTextColor = colorStateList;
            return this;
        }

        public Builder positiveBgRes(@DrawableRes int colorRes) {
            return positiveBg(mContext.getResources().getDrawable(colorRes));
        }

        public Builder positiveBgAttr(@AttrRes int colorAttr) {
            return positiveBg(
                    DialogUtils.resolveDrawable(this.mContext,colorAttr,null));
        }

        public Builder positiveBg(Drawable drawable) {
            this.mPositiveBg = drawable;
            return this;
        }

        public Builder negativeColor(@ColorInt int color) {
            return negativeColor(DialogUtils.getActionTextStateList(mContext,color));
        }

        public Builder negativeColorRes(@ColorRes int colorRes) {
            return negativeColor(DialogUtils.getActionTextColorStateList(this.mContext,colorRes));
        }

        public Builder negativeColorAttr(@AttrRes int colorAttr) {
            return negativeColor(
                    DialogUtils.resolveActionTextColorStateList(this.mContext,colorAttr,null));
        }

        public Builder negativeColor(ColorStateList colorStateList) {
            this.mNegativeTextColor = colorStateList;
            return this;
        }

        public Builder negativeBgRes(@DrawableRes int colorRes) {
            return negativeBg(mContext.getResources().getDrawable(colorRes));
        }

        public Builder negativeBgAttr(@AttrRes int colorAttr) {
            return negativeBg(
                    DialogUtils.resolveDrawable(this.mContext,colorAttr,null));
        }

        public Builder negativeBg(Drawable drawable) {
            this.mNegativeBg = drawable;
            return this;
        }

        public Builder negativeText(@StringRes int negativeRes) {
            if (negativeRes == 0) {
                return this;
            }
            return negativeText(this.mContext.getText(negativeRes));
        }

        public Builder negativeText(CharSequence message) {
            this.mNegativeText = message;
            return this;
        }

        public Builder btnSelector(@DrawableRes int selectorRes) {
            this.mBgSelectorPositive = selectorRes;
            this.mBgSelectorNegative = selectorRes;
            return this;
        }

        public Builder btnSelector(@DrawableRes int selectorRes,DialogAction which) {
            switch (which) {
                default:
                    this.mBgSelectorPositive = selectorRes;
                    break;
                case NEGATIVE:
                    this.mBgSelectorNegative = selectorRes;
                    break;
            }
            return this;
        }

        public Builder listDividerColor(@ColorInt int color) {
            this.mListDividerColor = color;
            return this;
        }

        public Builder listDividerColorRes(@ColorRes int colorRes) {
            this.mListDividerColor = ContextCompat.getColor(mContext,colorRes);
            return this;
        }

        public Builder listItemTextColor(@ColorInt int color) {
            this.mListItemTextColor = color;
            return this;
        }

        public Builder listItemTextColorRes(@ColorRes int colorRes) {
            this.mListItemTextColor = ContextCompat.getColor(mContext,colorRes);
            return this;
        }

        public Builder sheetItemTextColor(@ColorInt int color) {
            this.mSheetItemTextColor = color;
            return this;
        }

        public Builder sheetItemTextColorRes(@ColorRes int colorRes) {
            this.mSheetItemTextColor = ContextCompat.getColor(mContext,colorRes);
            return this;
        }

        public Builder sheetCancelColor(@ColorInt int color) {
            this.mSheetCancelColor = color;
            return this;
        }

        public Builder sheetCancelColorRes(@ColorRes int colorRes) {
            this.mSheetCancelColor = ContextCompat.getColor(mContext,colorRes);
            return this;
        }

        public Builder itemSelector(@ColorInt int color) {
            return itemSelector(DialogUtils.getActionTextStateList(mContext,color));
        }

        public Builder itemSelectorRes(@ColorRes int colorRes) {
            return itemSelector(DialogUtils.getActionTextColorStateList(this.mContext,colorRes));
        }

        public Builder itemSelectorAttr(@AttrRes int colorAttr) {
            return itemSelector(DialogUtils.resolveActionTextColorStateList(this.mContext,colorAttr,null));
        }

        public Builder itemSelector(ColorStateList colorStateList) {
            this.mItemSelector = colorStateList;
            return this;
        }

        public Builder customView(@LayoutRes int layoutRes,boolean withFootView) {
            LayoutInflater li = LayoutInflater.from(this.mContext);
            return customView(li.inflate(layoutRes,null),withFootView);
        }

        public Builder customView(View view,boolean withFootView) {
            this.mCustomView = view;
            this.mWithFootView = withFootView;
            return this;
        }

        public Builder backgroundRes(@DrawableRes int res) {
            background(mContext.getResources().getDrawable(res));
            return this;
        }

        public Builder background(Drawable res) {
            this.background = res;
            return this;
        }

        public Builder backgroundAttr(@AttrRes int drawableAttr) {
            return background(DialogUtils.resolveDrawable(this.mContext,drawableAttr));
        }

        public Builder onAction(ActionButtonCallback callback) {
            this.mActionButtonCallback = callback;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            this.mCanceledOnTouchOutside = cancelable;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder autoDismiss(boolean dismiss) {
            this.mAutoDismiss = dismiss;
            return this;
        }

        public Builder showListener(OnShowListener listener) {
            this.mShowListener = listener;
            return this;
        }

        public Builder dismissListener(OnDismissListener listener) {
            this.mDismissListener = listener;
            return this;
        }

        public Builder cancelListener(OnCancelListener listener) {
            this.mCancelListener = listener;
            return this;
        }

        public Builder keyListener(OnKeyListener listener) {
            this.mKeyListener = listener;
            return this;
        }

        @UiThread
        public ExDialog build() {
            return new ExDialog(this);
        }

        @UiThread
        public ExDialog show() {
            ExDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }


}