package io.github.eyinfo.messages.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import io.github.eyinfo.messages.R;

public class RoundTextView extends AppCompatTextView {

    private int mBgColor;
    private int mCornerRadius;
    private Paint mPaint;

    public RoundTextView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("Recycle")
    private void init(Context context, AttributeSet attributeSet) {
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RoundTextView);
            mBgColor = typedArray.getColor(R.styleable.RoundTextView_backgroundColor,
                    getResources().getColor(R.color.aurora_event_msg_bg_color));
            mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.RoundTextView_cornerRadius,
                    getResources().getDimensionPixelSize(R.dimen.aurora_event_bg_corner_radius));
            mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (this.getMeasuredWidth() <= 0 || this.getMeasuredHeight() <= 0) {
            return;
        }
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBgColor);

        RectF rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRoundRect(rectF, mCornerRadius, mCornerRadius, mPaint);
        super.onDraw(canvas);
    }

    public void setBgCornerRadius(int radius) {
        mCornerRadius = radius;
        invalidate();
    }

    public void setBgColor(int color) {
        mBgColor = color;
        invalidate();
    }
}
