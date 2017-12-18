package com.xtagwgj.base.view.loadingdialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xtagwgj.base.R;
import com.xtagwgj.base.utils.ToastUtil;
import com.xtagwgj.base.view.loadingdialog.progressing.SpinKitView;


/**
 * 加载框
 * Created by Administrator on 2017/3/16.
 */

public class RxDialogLoading extends RxDialog {

    private SpinKitView mLoadingView;
    private View mDialogContentView;
    private TextView mTextView;

    public RxDialogLoading(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public RxDialogLoading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public RxDialogLoading(Context context) {
        super(context);
        initView(context);
    }

    public RxDialogLoading(Activity context) {
        super(context);
        initView(context);
    }

    public RxDialogLoading(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView(context);
    }

    private void initView(Context context) {
        mDialogContentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading_spinkit, null);
        mLoadingView = (SpinKitView) mDialogContentView.findViewById(R.id.spin_kit);
        mTextView = (TextView) mDialogContentView.findViewById(R.id.name);
        setContentView(mDialogContentView);
    }

    public void setLoadingText(CharSequence charSequence) {
        mTextView.setText(charSequence);
    }

    public void setLoadingColor(int color) {
        mLoadingView.setColor(color);
    }

    public void cancel(String str) {
        cancel();
        ToastUtil.INSTANCE.showToast(getContext(), str);
    }

    public SpinKitView getLoadingView() {
        return mLoadingView;
    }

    public View getDialogContentView() {
        return mDialogContentView;
    }

    public TextView getTextView() {
        return mTextView;
    }
}
