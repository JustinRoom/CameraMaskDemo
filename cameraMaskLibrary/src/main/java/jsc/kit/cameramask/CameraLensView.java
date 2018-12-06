package jsc.kit.cameramask;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Camera Lens.
 * <p>
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/CameraMaskDemo" target="_blank">https://github.com/JustinRoom/CameraMaskDemo</a>
 *
 * @author jiangshicheng
 */
public class CameraLensView extends View {

    private final String TAG = "CameraLensView";
    public static final int TOP = 0;
    public static final int CENTER = 1;
    public static final int BOTTOM = 2;
    public final static int RECTANGLE = 0;
    public final static int CIRCULAR = 1;
    public final static int BELOW_CAMERA_LENS = 0;
    public final static int ABOVE_CAMERA_LENS = 1;

    @IntDef({TOP, CENTER, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraLensGravity {
    }

    @IntDef({RECTANGLE, CIRCULAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraLensShape {
    }

    @IntDef({BELOW_CAMERA_LENS, ABOVE_CAMERA_LENS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextLocation {
    }

    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();
    private Rect cameraLensRect = new Rect();
    private Matrix cameraLensMatrix = new Matrix();
    private Bitmap cameraLensBitmap;
    private int cameraLensShape;
    private int maskColor;//相机镜头遮罩颜色
    private int boxBorderColor;//扫描框边的颜色
    private int boxBorderWidth;//扫描框边的粗细
    private Path boxAnglePath;
    private boolean showBoxAngle;
    private int boxAngleColor;//扫描框四个角的颜色
    private int boxAngleBorderWidth;//扫描框四个角边的粗细
    private int boxAngleLength;//扫描框四个角边的长度
    private int cameraLensTopMargin;//相机镜头在Y轴上的偏移量
    private float cameraLensWidthRatio = 0.0f;//相机镜宽度百分比
    private float cameraLensHeightRatio = 0.0f;//相机镜高度百分比
    private int cameraLensWidth = 0;
    private int cameraLensHeight = 0;
    private int cameraLensGravity;

    protected TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    protected StaticLayout textStaticLayout;
    private String text;//提示文字
    private boolean textMathParent;//提示文字是否填充父View的宽度。true与View等宽，false与相机镜头等宽。
    private int textLocation;//提示文字位于相机镜头上方（或下方）
    private int textVerticalMargin;//提示文字与相机镜头的间距
    private int textLeftMargin;//提示文字与View（或相机镜头或扫描框）的左间距
    private int textRightMargin;//提示文字与View（或相机镜头或扫描框）的右间距

    private OnInitCameraLensCallBack initCameraLensCallBack = null;

    public CameraLensView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public CameraLensView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CameraLensView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CameraLensView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    public void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CameraLensView, defStyleAttr, 0);
        cameraLensTopMargin = a.getDimensionPixelSize(R.styleable.CameraLensView_clvCameraLensTopMargin, 0);

        if (a.hasValue(R.styleable.CameraLensView_clvCameraLens)) {
            int resId = a.getResourceId(R.styleable.CameraLensView_clvCameraLens, -1);
            if (resId != -1)
                cameraLensBitmap = BitmapFactory.decodeResource(getResources(), resId);
        }

        if (a.hasValue(R.styleable.CameraLensView_clvCameraLensSizeRatio)) {
            float cameraLensSizeRatio = a.getFloat(R.styleable.CameraLensView_clvCameraLensSizeRatio, 0);
            if (cameraLensSizeRatio <= 0 || cameraLensSizeRatio >= 1)
                throw new IllegalArgumentException("The value of clvCameraLensSizeRatio should be (0.0, 1.0).");
            cameraLensWidthRatio = cameraLensSizeRatio;
            cameraLensHeightRatio = cameraLensSizeRatio;
        }

        cameraLensGravity = a.getInt(R.styleable.CameraLensView_clvCameraLensGravity, TOP);
        if (a.hasValue(R.styleable.CameraLensView_clvCameraLensWidthWeight)) {
            String weightStr = a.getString(R.styleable.CameraLensView_clvCameraLensWidthWeight);
            cameraLensWidthRatio = explainRatio(weightStr);
        } else {
            cameraLensWidth = a.getDimensionPixelSize(R.styleable.CameraLensView_clvCameraLensWidth, 0);
        }
        if (a.hasValue(R.styleable.CameraLensView_clvCameraLensHeightWeight)) {
            String weightStr = a.getString(R.styleable.CameraLensView_clvCameraLensHeightWeight);
            cameraLensHeightRatio = explainRatio(weightStr);
        } else {
            cameraLensHeight = a.getDimensionPixelSize(R.styleable.CameraLensView_clvCameraLensHeight, 0);
        }

        cameraLensShape = a.getInt(R.styleable.CameraLensView_clvCameraLensShape, RECTANGLE);
        boxBorderColor = a.getColor(R.styleable.CameraLensView_clvBoxBorderColor, 0x99FFFFFF);
        boxBorderWidth = a.getDimensionPixelSize(R.styleable.CameraLensView_clvBoxBorderWidth, 2);
        showBoxAngle = a.getBoolean(R.styleable.CameraLensView_clvShowBoxAngle, true);
        boxAngleColor = a.getColor(R.styleable.CameraLensView_clvBoxAngleColor, Color.YELLOW);
        int defaultScannerBoxAngleBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        boxAngleBorderWidth = a.getDimensionPixelSize(R.styleable.CameraLensView_clvBoxAngleBorderWidth, defaultScannerBoxAngleBorderWidth);
        int defaultScannerBoxAngleLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        boxAngleLength = a.getDimensionPixelSize(R.styleable.CameraLensView_clvBoxAngleLength, defaultScannerBoxAngleLength);
        maskColor = a.getColor(R.styleable.CameraLensView_clvMaskColor, 0x99000000);

        text = a.getString(R.styleable.CameraLensView_clvText);
        int textColor = a.getColor(R.styleable.CameraLensView_clvTextColor, Color.WHITE);
        int defaultTextSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()) + .5f);
        float textSize = a.getDimension(R.styleable.CameraLensView_clvTextSize, defaultTextSize);
        textMathParent = a.getBoolean(R.styleable.CameraLensView_clvTextMathParent, true);
        textLocation = a.getInt(R.styleable.CameraLensView_clvTextLocation, BELOW_CAMERA_LENS);
        textVerticalMargin = a.getDimensionPixelSize(R.styleable.CameraLensView_clvTextVerticalMargin, 0);
        textLeftMargin = a.getDimensionPixelSize(R.styleable.CameraLensView_clvTextLeftMargin, 0);
        textRightMargin = a.getDimensionPixelSize(R.styleable.CameraLensView_clvTextRightMargin, 0);
        a.recycle();

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    private float explainRatio(String floatString) {
        floatString = floatString == null ? "" : floatString.trim();
        floatString = floatString.replace("{", "");
        floatString = floatString.replace("}", "");
        String[] weightSplits = floatString.split(",");
        if (weightSplits.length != 2) {
            throw new IllegalArgumentException("The clvCameraLensWidthWight's value should like this:{5.0, 3.0}.");
        }
        try {
            float value1 = Float.parseFloat(weightSplits[0]);
            float value2 = Float.parseFloat(weightSplits[1]);
            if (value1 * value2 == 0)
                throw new IllegalArgumentException("The clvCameraLensWidthWight's values must be more chan zero.");
            return value1 < value2 ? value1 / value2 : value2 / value1;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid values.");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        textStaticLayout = null;
        initCameraLensSize(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ");
        drawMask(canvas);
        switch (cameraLensShape) {
            case RECTANGLE:
                if (drawCameraLensBitmap(canvas))
                    drawRectangle(canvas);
                break;
            case CIRCULAR:
                if (drawCameraLensBitmap(canvas))
                    drawCircular(canvas);
                break;
        }

        //提示文字
        if (textStaticLayout != null) {
            canvas.save();
            float translateX = textMathParent ? 0 : cameraLensRect.left;
            translateX = translateX + textLeftMargin;
            float translateY = textLocation == BELOW_CAMERA_LENS ? cameraLensRect.bottom + textVerticalMargin : cameraLensRect.top - textVerticalMargin - textStaticLayout.getHeight();
            canvas.translate(translateX, translateY);
            textStaticLayout.draw(canvas);
            canvas.translate(-translateX, -translateY);
            canvas.restore();
        }
    }

    private boolean drawCameraLensBitmap(Canvas canvas) {
        if (cameraLensBitmap == null)
            return true;

        float translateX = cameraLensRect.left;
        float translateY = cameraLensRect.top;
        float scaleX = cameraLensRect.width() * 1.0f / cameraLensBitmap.getWidth();
        float scaleY = cameraLensRect.height() * 1.0f / cameraLensBitmap.getHeight();
        cameraLensMatrix.setScale(scaleX, scaleY);
        canvas.save();
        canvas.translate(translateX, translateY);
        canvas.drawBitmap(cameraLensBitmap, cameraLensMatrix, null);
        canvas.translate(-translateX, -translateY);
        canvas.restore();
        return false;
    }

    private void drawRectangle(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        if (boxAnglePath == null) {
            boxAnglePath = new Path();
        }
        paint.setStrokeWidth(boxBorderWidth);
        paint.setColor(boxBorderColor);
        canvas.drawRect(cameraLensRect, paint);

        if (!showBoxAngle)
            return;
        paint.setStrokeWidth(boxAngleBorderWidth);
        paint.setColor(boxAngleColor);
        //左上角
        boxAnglePath.reset();
        boxAnglePath.moveTo(cameraLensRect.left, cameraLensRect.top + boxAngleLength);
        boxAnglePath.lineTo(cameraLensRect.left, cameraLensRect.top);
        boxAnglePath.lineTo(cameraLensRect.left + boxAngleLength, cameraLensRect.top);
        canvas.drawPath(boxAnglePath, paint);
        //右上角
        boxAnglePath.reset();
        boxAnglePath.moveTo(cameraLensRect.right - boxAngleLength, cameraLensRect.top);
        boxAnglePath.lineTo(cameraLensRect.right, cameraLensRect.top);
        boxAnglePath.lineTo(cameraLensRect.right, cameraLensRect.top + boxAngleLength);
        canvas.drawPath(boxAnglePath, paint);
        //右下角
        boxAnglePath.reset();
        boxAnglePath.moveTo(cameraLensRect.right, cameraLensRect.bottom - boxAngleLength);
        boxAnglePath.lineTo(cameraLensRect.right, cameraLensRect.bottom);
        boxAnglePath.lineTo(cameraLensRect.right - boxAngleLength, cameraLensRect.bottom);
        canvas.drawPath(boxAnglePath, paint);
        //左下角
        boxAnglePath.reset();
        boxAnglePath.moveTo(cameraLensRect.left + boxAngleLength, cameraLensRect.bottom);
        boxAnglePath.lineTo(cameraLensRect.left, cameraLensRect.bottom);
        boxAnglePath.lineTo(cameraLensRect.left, cameraLensRect.bottom - boxAngleLength);
        canvas.drawPath(boxAnglePath, paint);
    }

    private void drawCircular(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(boxBorderWidth);
        paint.setColor(boxBorderColor);
        float cx = cameraLensRect.exactCenterX();
        float cy = cameraLensRect.exactCenterY();
        float radius = (cameraLensRect.width() - boxBorderWidth) / 2.0f;
        canvas.drawCircle(cx, cy, radius, paint);

        if (!showBoxAngle)
            return;
        paint.setStrokeWidth(boxAngleBorderWidth);
        paint.setColor(boxAngleColor);
        float halfBoxAngleBorderWidth = boxAngleBorderWidth / 16.0f;
        rectF.set(
                cx - radius - halfBoxAngleBorderWidth,
                cy - radius - halfBoxAngleBorderWidth,
                cx + radius + halfBoxAngleBorderWidth,
                cy + radius + halfBoxAngleBorderWidth
        );
        float angle = (float) (boxAngleLength * 180 / (Math.PI * radius));
        float startAngle;
        //左上角
        startAngle = 225 - angle / 2;
        canvas.drawArc(rectF, startAngle, angle, false, paint);
        //右上角
        startAngle = 315 - angle / 2;
        canvas.drawArc(rectF, startAngle, angle, false, paint);
        //右下角
        startAngle = 45 - angle / 2;
        canvas.drawArc(rectF, startAngle, angle, false, paint);
        //左下角
        startAngle = 135 - angle / 2;
        canvas.drawArc(rectF, startAngle, angle, false, paint);
    }

    private void initCameraLensSize(int w, int h) {
        if (cameraLensWidthRatio > 0) {
            cameraLensWidth = (int) (w * cameraLensWidthRatio);
        }
        if (cameraLensHeightRatio > 0) {
            cameraLensHeight = (int) (h * cameraLensHeightRatio);
        }
        if (cameraLensWidth <= 0) {
            cameraLensWidth = cameraLensHeight > 0 ? cameraLensHeight : w / 2;
        }
        if (cameraLensHeight <= 0) {
            cameraLensHeight = cameraLensWidth > 0 ? cameraLensWidth : w / 2;
        }

        switch (cameraLensGravity) {
            case TOP:
                cameraLensRect.left = (w - cameraLensWidth) / 2;
                cameraLensRect.top = cameraLensTopMargin;
                break;
            case CENTER:
                cameraLensRect.left = (w - cameraLensWidth) / 2;
                cameraLensRect.top = (h - cameraLensHeight) / 2 + cameraLensTopMargin;
                break;
            case BOTTOM:
                cameraLensRect.left = (w - cameraLensWidth) / 2;
                cameraLensRect.top = h - cameraLensHeight + cameraLensTopMargin;
                break;
        }
        cameraLensRect.right = cameraLensRect.left + cameraLensWidth;
        cameraLensRect.bottom = cameraLensRect.top + cameraLensHeight;
        switch (cameraLensShape) {
            case RECTANGLE:
                break;
            case CIRCULAR:
                Rect temp = new Rect(cameraLensRect);
                int centerX = temp.centerX();
                int centerY = temp.centerY();
                int max = Math.min(temp.width(), temp.height());
                int left = centerX - max / 2;
                int top = centerY - max / 2;
                cameraLensRect.set(left, top, left + max, top + max);
                break;
        }
        updateStaticLayout(w);

        if (initCameraLensCallBack != null)
            initCameraLensCallBack.onFinishInitialize(cameraLensRect);
    }

    /**
     * The second way to draw mask. In this way, there are two different shapes.
     * Square: {@link #RECTANGLE}、Circular: {@link #CIRCULAR}.
     *
     * @param canvas canvas
     */
    private void drawMask(Canvas canvas) {
        //满屏幕bitmap
        Bitmap maskBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(maskBitmap);
        paint.setColor(maskColor);
        paint.setStyle(Paint.Style.FILL);
        mCanvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        paint.setXfermode(xfermode);
        switch (cameraLensShape) {
            case RECTANGLE:
                mCanvas.drawRect(cameraLensRect, paint);
                break;
            case CIRCULAR:
                float centerX = cameraLensRect.exactCenterX();
                float centerY = cameraLensRect.exactCenterY();
                float radius = cameraLensRect.width() / 2.0f;
                mCanvas.drawCircle(centerX, centerY, radius, paint);
                break;
        }
        paint.setXfermode(null);
        canvas.drawBitmap(maskBitmap, 0, 0, null);
    }

    private void executeInvalidateDelay() {
        updateStaticLayout(getMeasuredWidth());
        invalidate();
    }

    private void updateStaticLayout(int w) {
        if (text == null || text.trim().length() == 0) {
            textStaticLayout = null;
            return;
        }
        if (textStaticLayout == null) {
            int textWidth = textMathParent ? w : cameraLensRect.width();
            textWidth = textWidth - textLeftMargin - textRightMargin;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textStaticLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, textWidth)
                        .setAlignment(StaticLayout.Alignment.ALIGN_CENTER)
                        .setLineSpacing(0, 1.0f)
                        .build();
            } else {
                textStaticLayout = new StaticLayout(text, textPaint, textWidth, StaticLayout.Alignment.ALIGN_CENTER, 1.0f, 0, true);
            }
        }
    }

    /**
     * @param src src
     * @return the bitmap of camera lens area
     */
    public Bitmap cropCameraLensRectBitmap(Bitmap src, boolean withRatio) {
        if (src == null)
            return null;
        int sw = src.getWidth();
        int sh = src.getHeight();
        if (!withRatio) {
            return Bitmap.createBitmap(src, cameraLensRect.left, cameraLensRect.top, cameraLensRect.width(), cameraLensRect.height());
        }
        int l = sw * cameraLensRect.left / getWidth();
        int t = sh * cameraLensRect.top / getHeight();
        int r = sw * cameraLensRect.right / getWidth();
        int b = sh * cameraLensRect.bottom / getHeight();
        return Bitmap.createBitmap(src, l, t, r - l, b - t);
    }


    //getter and setter
    public void setInitCameraLensCallBack(OnInitCameraLensCallBack initCameraLensCallBack) {
        this.initCameraLensCallBack = initCameraLensCallBack;
    }

    @NonNull
    public Rect getCameraLensRect() {
        return cameraLensRect;
    }

    public Bitmap getCameraLensBitmap() {
        return cameraLensBitmap;
    }

    public void setCameraLensBitmap(Bitmap cameraLensBitmap) {
        this.cameraLensBitmap = cameraLensBitmap;
        executeInvalidateDelay();
    }

    public int getMaskColor() {
        return maskColor;
    }

    public void setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
        executeInvalidateDelay();
    }

    public int getBoxBorderColor() {
        return boxBorderColor;
    }

    public void setBoxBorderColor(@ColorInt int boxBorderColor) {
        this.boxBorderColor = boxBorderColor;
        executeInvalidateDelay();
    }

    public int getBoxBorderWidth() {
        return boxBorderWidth;
    }

    public void setBoxBorderWidth(int boxBorderWidth) {
        this.boxBorderWidth = boxBorderWidth;
        executeInvalidateDelay();
    }

    public int getBoxAngleColor() {
        return boxAngleColor;
    }

    public void setBoxAngleColor(@ColorInt int boxAngleColor) {
        this.boxAngleColor = boxAngleColor;
        executeInvalidateDelay();
    }

    public int getBoxAngleBorderWidth() {
        return boxAngleBorderWidth;
    }

    public void setBoxAngleBorderWidth(int boxAngleBorderWidth) {
        this.boxAngleBorderWidth = boxAngleBorderWidth;
        executeInvalidateDelay();
    }

    public int getBoxAngleLength() {
        return boxAngleLength;
    }

    public void setBoxAngleLength(int boxAngleLength) {
        this.boxAngleLength = boxAngleLength;
        executeInvalidateDelay();
    }

    public int getCameraLensTopMargin() {
        return cameraLensTopMargin;
    }

    public void setCameraLensTopMargin(int cameraLensTopMargin) {
        int moveY = cameraLensTopMargin - this.cameraLensTopMargin;
        this.cameraLensTopMargin = cameraLensTopMargin;
        cameraLensRect.offset(0, moveY);
        executeInvalidateDelay();
    }

    @Deprecated
    public float getCameraLensSizeRatio() {
        return 0.0f;
    }

    public void setCameraLensSizeRatio(float cameraLensSizeRatio) {
        if (cameraLensSizeRatio <= 0 || cameraLensSizeRatio >= 1)
            throw new IllegalArgumentException("The value of cameraLensSizeRatio should be (0.0, 1.0).");
        cameraLensWidthRatio = cameraLensSizeRatio;
        cameraLensHeightRatio = cameraLensSizeRatio;
        cameraLensWidth = 0;
        cameraLensHeight = 0;
        requestLayout();
    }

    public void setCameraLensSize(int width, int height) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("width and height must be more chan zero.");
        cameraLensWidthRatio = 0;
        cameraLensHeightRatio = 0;
        cameraLensWidth = width;
        cameraLensHeight = height;
        requestLayout();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        textStaticLayout = null;
        executeInvalidateDelay();
    }

    public boolean isTextMathParent() {
        return textMathParent;
    }

    public void setTextMathParent(boolean textMathParent) {
        this.textMathParent = textMathParent;
        executeInvalidateDelay();
    }

    public int getTextLocation() {
        return textLocation;
    }

    public void setTextLocation(@TextLocation int textLocation) {
        this.textLocation = textLocation;
        executeInvalidateDelay();
    }

    public int getTextVerticalMargin() {
        return textVerticalMargin;
    }

    public void setTextVerticalMargin(int textVerticalMargin) {
        this.textVerticalMargin = textVerticalMargin;
        executeInvalidateDelay();
    }

    public int getTextLeftMargin() {
        return textLeftMargin;
    }

    public void setTextLeftMargin(int textLeftMargin) {
        this.textLeftMargin = textLeftMargin;
        executeInvalidateDelay();
    }

    public int getTextRightMargin() {
        return textRightMargin;
    }

    public void setTextRightMargin(int textRightMargin) {
        this.textRightMargin = textRightMargin;
        executeInvalidateDelay();
    }

    public int getCameraLensShape() {
        return cameraLensShape;
    }

    public void setCameraLensShape(@CameraLensShape int cameraLensShape) {
        this.cameraLensShape = cameraLensShape;
        executeInvalidateDelay();
    }

    public interface OnInitCameraLensCallBack {

        /**
         * Call back when camera lens location was initialized.
         *
         * @param rect camera lens rect
         */
        void onFinishInitialize(@NonNull Rect rect);
    }
}
