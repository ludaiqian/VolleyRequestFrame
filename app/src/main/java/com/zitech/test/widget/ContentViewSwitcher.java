package com.zitech.test.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.zitech.test.R;

/**
 * 提高性能可用ViewStub优化之
 */
public class ContentViewSwitcher extends ViewAnimator {

    public static final int LOADING = 0;
    public static final int RETRY = 1;
    public static final int NO_DATA = 2;
    public static final int CONTENT = 3;
    protected Button retry;
    protected TextView noData;
    protected TextView errorPromptView;
    protected ProgressBar progressBar;
    private static final int CHILD_SIZE = 4;

    public ContentViewSwitcher(Context context) {
        super(context);
        initView();
    }

    //    private ViewCo
    public ContentViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    protected void initView() {
        View v = inflate(getContext(), R.layout.content_view_holder, this);
        retry = (Button) v.findViewById(R.id.retry_btn);
        noData = (TextView) v.findViewById(R.id.no_data);
        errorPromptView = (TextView) findViewById(R.id.error_prompt_view);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
    }

    public void setContent(View content) {
        if (getChildCount() == CHILD_SIZE) {
            removeViewAt(CONTENT);
        }
        ViewGroup parent = (ViewGroup) content.getParent();
        ViewGroup.LayoutParams contentLayoutParams = content.getLayoutParams();
        int index = parent.indexOfChild(content);
        parent.removeView(content);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(content, CONTENT, params);
        parent.addView(this, index, contentLayoutParams);
    }

    protected View inflateLayout() {
        return inflate(getContext(), R.layout.content_view_holder, this);
    }

    public void showContent() {
        setDisplayedChild(CONTENT);
    }

    public void showLoading() {
        setDisplayedChild(LOADING);
    }

    public void showRetry() {
        setDisplayedChild(RETRY);
    }

    public void showEmpty() {
        setDisplayedChild(NO_DATA);
    }


    public void setRetryListener(final OnClickListener listener) {
        retry.setOnClickListener(listener);
    }


    public void setDefaultEmptyImage(int resId) {
        noData.setBackgroundResource(resId);
    }

    public void setNoDataText(String noDataText) {
        noData.setText(noDataText);
    }

    public void setErrorPrompt(int resId) {
        errorPromptView.setText(resId);
    }
}
