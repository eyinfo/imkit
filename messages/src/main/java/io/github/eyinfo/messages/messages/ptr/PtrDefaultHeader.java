package io.github.eyinfo.messages.messages.ptr;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import io.github.eyinfo.messages.messages.MaterialProgressDrawable;

/**
 * Default header is material style
 */
public class PtrDefaultHeader extends View implements PtrUIHandler {

    private MaterialProgressDrawable mDrawable;
    private float mScale = 1f;

    private Animation mScaleAnimation = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            mScale = 1f - interpolatedTime;
            mDrawable.setAlpha((int) (255 * mScale));
            invalidate();
        }
    };

    public PtrDefaultHeader(Context context) {
        super(context);
        initView();
    }

    public PtrDefaultHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PtrDefaultHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setPtrFrameLayout(PullToRefreshLayout layout) {

        final PtrUIHandlerHook mPtrUIHandlerHook = new PtrUIHandlerHook() {
            @Override
            public void run() {
                startAnimation(mScaleAnimation);
            }
        };

        mScaleAnimation.setDuration(200);
        mScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPtrUIHandlerHook.resume();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.setRefreshCompleteHook(mPtrUIHandlerHook);
    }

    private void initView() {
        mDrawable = new MaterialProgressDrawable(getContext(), this);
        mDrawable.setBackgroundColor(Color.WHITE);
        mDrawable.setCallback(this);
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        if (dr == mDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(dr);
        }
    }

    public void setColorSchemeColors(int[] colors) {
        mDrawable.setColorSchemeColors(colors);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mDrawable.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int size = mDrawable.getIntrinsicHeight();
        mDrawable.setBounds(0, 0, size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int saveCount = canvas.save();
        Rect rect = mDrawable.getBounds();
        int l = getPaddingLeft() + (getMeasuredWidth() - mDrawable.getIntrinsicWidth()) / 2;
        canvas.translate(l, getPaddingTop());
        canvas.scale(mScale, mScale, rect.exactCenterX(), rect.exactCenterY());
        mDrawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    /**
     * When the content view has reached top and refresh has been completed, view will be reset.
     *
     * @param frame
     */
    @Override
    public void onUIReset(PullToRefreshLayout frame) {
        mScale = 1f;
        mDrawable.stop();
    }

    /**
     * prepare for loading
     *
     * @param frame
     */
    @Override
    public void onUIRefreshPrepare(PullToRefreshLayout frame) {
    }

    /**
     * perform refreshing UI
     *
     * @param frame
     */
    @Override
    public void onUIRefreshBegin(PullToRefreshLayout frame) {
        mDrawable.setAlpha(255);
        mDrawable.start();
    }

    /**
     * perform UI after refresh
     *
     * @param frame
     */
    @Override
    public void onUIRefreshComplete(PullToRefreshLayout frame) {
        mDrawable.stop();
    }

    @Override
    public void onUIPositionChange(PullToRefreshLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        float percent = Math.min(1f, ptrIndicator.getCurrentPercent());

        if (status == PullToRefreshLayout.PTR_STATUS_PREPARE) {
            mDrawable.setAlpha((int) (255 * percent));
            mDrawable.showArrow(true);

            float strokeStart = ((percent) * .8f);
            mDrawable.setStartEndTrim(0f, Math.min(0.8f, strokeStart));
            mDrawable.setArrowScale(Math.min(1f, percent));

            // magic
            float rotation = (-0.25f + .4f * percent + percent * 2) * .5f;
            mDrawable.setProgressRotation(rotation);
            invalidate();
        }
    }
}
