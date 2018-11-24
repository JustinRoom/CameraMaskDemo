package jsc.kit.cameramask;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * ScannerBarView
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/CameraMaskDemo" target="_blank">https://github.com/JustinRoom/CameraMaskDemo</a>
 *
 * @author jiangshicheng
 */
public class ScannerBarView extends ViewGroup {

    private ImageView scannerBar;
    private ObjectAnimator animator = null;

    public ScannerBarView(Context context) {
        this(context, null);
    }

    public ScannerBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScannerBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        scannerBar = new ImageView(context);
        scannerBar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScannerBarView, defStyleAttr, 0);
        scannerBar.setImageResource(a.getResourceId(R.styleable.ScannerBarView_sbvSrc, R.drawable.camera_mask_scanner_bar));
        a.recycle();
        addView(scannerBar);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.layout(0, 0 - child.getMeasuredHeight(), getMeasuredWidth(), 0);
            }
        }
    }

    public void setScannerBarImageResource(@DrawableRes int drawable) {
        scannerBar.setImageResource(drawable);
        executeRequestLayout();
    }

    public void setScannerBarImageBitmap(Bitmap bitmap) {
        scannerBar.setImageBitmap(bitmap);
        executeRequestLayout();
    }

    public void start() {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(scannerBar, View.TRANSLATION_Y, 0, getHeight() + scannerBar.getHeight());
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(2_000);
            animator.setRepeatCount(Animation.INFINITE);
            animator.start();
        }
    }

    public void pause() {
        if (animator != null && animator.isRunning())
            animator.pause();
    }

    public void resume() {
        if (animator != null && animator.isPaused())
            animator.resume();
    }

    public void stop() {
        scannerBar.setTranslationY(0);
        if (animator != null && animator.isStarted()) {
            animator.cancel();
            animator = null;
        }
    }

    public boolean isRunning() {
        return animator != null && animator.isRunning();
    }

    private void executeRequestLayout() {
        boolean needRestart = isRunning();
        stop();
        requestLayout();
        if (needRestart)
            start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        executeRequestLayout();
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }
}
