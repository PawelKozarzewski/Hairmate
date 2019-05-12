package com.example.patryk.hairfire;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CustomDayLinearLayout extends LinearLayout {
    public CustomDayLinearLayout(Context context) {
        super(context);
    }

    public CustomDayLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDayLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}