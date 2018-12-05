package jsc.kit.cameramask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/CameraMaskDemo" target="_blank">https://github.com/JustinRoom/CameraMaskDemo</a>
 *
 * @author jiangshicheng
 */
public class CameraScannerMaskView extends FrameLayout implements CameraLensView.OnInitCameraLensCallBack {

    private final String TAG = "CameraScannerMas";
    private ScannerBarView scannerBarView;
    private CameraLensView cameraLensView;

    public CameraScannerMaskView(@NonNull Context context) {
        this(context, null);
    }

    public CameraScannerMaskView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraScannerMaskView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i(TAG, "CameraScannerMaskView: ");
        cameraLensView = new CameraLensView(context, attrs, defStyleAttr);
        scannerBarView = new ScannerBarView(context, attrs, defStyleAttr);
        addView(cameraLensView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(scannerBarView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        cameraLensView.setInitCameraLensCallBack(this);
    }

    @Override
    public void onFinishInitialize(@NonNull Rect rect) {
        reLocationScannerBarView(rect);
    }

    private void reLocationScannerBarView(@NonNull Rect rect){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) scannerBarView.getLayoutParams();
        params.width = rect.width();
        params.height = rect.height();
        params.leftMargin = rect.left;
        params.topMargin = rect.top;
        scannerBarView.setLayoutParams(params);
    }

    public void start() {
        scannerBarView.start();
    }

    public void pause() {
        scannerBarView.pause();
    }

    public void resume() {
        scannerBarView.resume();
    }

    public void stop() {
        scannerBarView.stop();
    }
}
