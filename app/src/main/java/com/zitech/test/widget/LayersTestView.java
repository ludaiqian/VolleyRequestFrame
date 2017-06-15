package com.zitech.test.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LayersTestView extends View {
    public static final String TAG = "LayersTestView";

    private Paint mPaint;

    public LayersTestView(Context context) {
        super(context);
        init();
    }

    public LayersTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LayersTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(75, 75, 75, mPaint);

        canvas.translate(25, 25);
        //LogUtil.d(TAG, "getCounet2 = " + canvas.getSaveCount());
        int count = canvas.saveLayerAlpha(0, 0, 200, 200, 0x88, Canvas.ALL_SAVE_FLAG);
        //LogUtil.d(TAG, "count = " + count + " , getCounet2 = " + canvas.getSaveCount());
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(125, 125, 75, mPaint);
        canvas.restore();
        canvas.drawCircle(30, 30, 30, mPaint);
        //LogUtil.d(TAG, "getCounet3 = " + canvas.getSaveCount());
    }
}