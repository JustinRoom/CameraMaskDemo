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
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
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

    public final static int CAMERA_LENS_SHAPE_SQUARE = 0;
    public final static int CAMERA_LENS_SHAPE_CIRCULAR = 1;
    public final static int BELOW_CAMERA_LENS = 0;
    public final static int ABOVE_CAMERA_LENS = 1;

    @IntDef({CAMERA_LENS_SHAPE_SQUARE, CAMERA_LENS_SHAPE_CIRCULAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraLensShape {
    }

    @IntDef({BELOW_CAMERA_LENS, ABOVE_CAMERA_LENS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextLocation {
    }

    protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private RectF rectF = new RectF();
    private Rect cameraLensRect = new Rect();
    private Matrix cameraLensMatrix = new Matrix();
    private Bitmap cameraLensBitmap;
    private int cameraLensShape;
    private int maskColor;//相机镜头遮罩颜色
    private int boxBorderColor;//扫描框边的颜色
    private int boxBorderWidth;//扫描框边的粗细
    private Path boxAnglePath;
    private int boxAngleColor;//扫描框四个角的颜色
    private int boxAngleBorderWidth;//扫描框四个角边的粗细
    private int boxAngleLength;//扫描框四个角边的长度
    private int cameraLensTopMargin;//相机镜头（或扫描框）与顶部的间距
    private float cameraLensSizeRatio;//相机镜头（或扫描框）大小占View宽度的百分比

    protected TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    protected StaticLayout textStaticLayout;
    private String text;//提示文字
    private boolean textMathParent;//提示文字是否填充父View的宽度。true与View等宽，false与相机镜头（或扫描框）等宽。
    private int textLocation;//提示文字位于相机镜头（或扫描框）上方（或下方）
    private int textVerticalMargin;//提示文字与相机镜头（或扫描框）的间距
    private int textLeftMargin;//提示文字与View（或相机镜头或扫描框）的左间距
    private int textRightMargin;//提示文字与View（或相机镜头或扫描框）的右间距
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            updateStaticLayout();
            invalidate();
        }
    };

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

        cameraLensShape = a.getInt(R.styleable.CameraLensView_clvCameraLensShape, CAMERA_LENS_SHAPE_SQUARE);
        boxBorderColor = a.getColor(R.styleable.CameraLensView_clvBoxBorderColor, 0x99FFFFFF);
        boxBorderWidth = a.getDimensionPixelSize(R.styleable.CameraLensView_clvBoxBorderWidth, 2);
        boxAngleColor = a.getColor(R.styleable.CameraLensView_clvBoxAngleColor, Color.YELLOW);
        int defaultScannerBoxAngleBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        boxAngleBorderWidth = a.getDimensionPixelSize(R.styleable.CameraLensView_clvBoxAngleBorderWidth, defaultScannerBoxAngleBorderWidth);
        int defaultScannerBoxAngleLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        boxAngleLength = a.getDimensionPixelSize(R.styleable.CameraLensView_clvBoxAngleLength, defaultScannerBoxAngleLength);
        maskColor = a.getColor(R.styleable.CameraLensView_clvMaskColor, 0x99000000);
        cameraLensSizeRatio = a.getFloat(R.styleable.CameraLensView_clvCameraLensSizeRatio, .6f);
        if (cameraLensSizeRatio < .3f)
            cameraLensSizeRatio = .3f;
        if (cameraLensSizeRatio > 1.0f)
            cameraLensSizeRatio = 1.0f;

        text = a.getString(R.styleable.CameraLensView_clvText);
        int textColor = a.getColor(R.styleable.CameraLensView_clvTextColor, Color.WHITE);
        int defaultTextSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()) + .5f);
        float textSize = a.getDimension(R.styleable.CameraLensView_clvTextSize, defaultTextSize);
        textMathParent = a.getBoolean(R.styleable.CameraLensView_clvTextMathParent, false);
        textLocation = a.getInt(R.styleable.CameraLensView_clvTextLocation, BELOW_CAMERA_LENS);
        textVerticalMargin = a.getDimensionPixelSize(R.styleable.CameraLensView_clvTextVerticalMargin, 0);
        textLeftMargin = a.getDimensionPixelSize(R.styleable.CameraLensView_clvTextLeftMargin, 0);
        textRightMargin = a.getDimensionPixelSize(R.styleable.CameraLensView_clvTextRightMargin, 0);
        a.recycle();

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initCameraLensSize(getMeasuredWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMask(canvas, cameraLensShape);

        float translateX = 0;
        float translateY = 0;
        if (cameraLensBitmap != null) {
            translateX = cameraLensRect.left;
            translateY = cameraLensTopMargin;
            float scale = cameraLensRect.width() * 1.0f / cameraLensBitmap.getWidth();
            cameraLensMatrix.setScale(scale, scale);
            canvas.save();
            canvas.translate(translateX, translateY);
            canvas.drawBitmap(cameraLensBitmap, cameraLensMatrix, null);
            canvas.translate(-translateX, -translateY);
            canvas.restore();
        } else {
            paint.setStyle(Paint.Style.STROKE);
            switch (cameraLensShape) {
                case CAMERA_LENS_SHAPE_SQUARE:
                    if (boxAnglePath == null) {
                        boxAnglePath = new Path();
                    }
                    paint.setStrokeWidth(boxBorderWidth);
                    paint.setColor(boxBorderColor);
                    canvas.drawRect(cameraLensRect, paint);

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
                    break;
                case CAMERA_LENS_SHAPE_CIRCULAR:
                    paint.setStrokeWidth(boxBorderWidth);
                    paint.setColor(boxBorderColor);
                    float cx = cameraLensRect.left + cameraLensRect.width() / 2.0f;
                    float cy = cameraLensRect.top + cameraLensRect.height() / 2.0f;
                    float radius = cameraLensRect.width() / 2.0f - boxBorderWidth / 2.0f;
                    canvas.drawCircle(cx, cy, radius, paint);

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
                    break;
            }

        }

        //提示文字
        if (textStaticLayout != null) {
            canvas.save();
            translateX = textMathParent ? 0 : cameraLensRect.left;
            translateX = translateX + textLeftMargin;
            translateY = textLocation == BELOW_CAMERA_LENS ? cameraLensRect.bottom + textVerticalMargin : cameraLensRect.top - textVerticalMargin - textStaticLayout.getHeight();
            canvas.translate(translateX, translateY);
            textStaticLayout.draw(canvas);
            canvas.translate(-translateX, -translateY);
            canvas.restore();
        }
    }

    private void initCameraLensSize(int width) {
        int cameraLensSize = (int) (width * cameraLensSizeRatio);
        int left = (width - cameraLensSize) / 2;
        cameraLensRect.set(left, cameraLensTopMargin, left + cameraLensSize, cameraLensTopMargin + cameraLensSize);
        updateStaticLayout();
    }

    /**
     * The second way to draw mask. In this way, there are two different shapes.
     * Square: {@link #CAMERA_LENS_SHAPE_SQUARE}、Circular: {@link #CAMERA_LENS_SHAPE_CIRCULAR}.
     *
     * @param canvas    canvas
     * @param maskShape mask shape. One of {@link #CAMERA_LENS_SHAPE_SQUARE}、{@link #CAMERA_LENS_SHAPE_CIRCULAR}.
     */
    private void drawMask(Canvas canvas, int maskShape) {
        //满屏幕bitmap
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bitmap);
        paint.setColor(maskColor);
        paint.setStyle(Paint.Style.FILL);
        mCanvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        paint.setXfermode(xfermode);
        switch (maskShape) {
            case CAMERA_LENS_SHAPE_SQUARE:
                mCanvas.drawRect(cameraLensRect, paint);
                break;
            case CAMERA_LENS_SHAPE_CIRCULAR:
                float radius = cameraLensRect.height() / 2.0f;
                mCanvas.drawCircle(getWidth() / 2.0f, cameraLensRect.top + radius, radius, paint);
                break;
        }
        paint.setXfermode(null);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void executeInvalidateDelay() {
        removeCallbacks(r);
        postDelayed(r, 10);
    }

    private void updateStaticLayout() {
        if (text == null || text.trim().length() == 0) {
            textStaticLayout = null;
            return;
        }
        int textWidth = textMathParent ? getWidth() : cameraLensRect.width();
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


    //getter and setter
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
        this.cameraLensTopMargin = cameraLensTopMargin;
        requestLayout();
    }

    public float getCameraLensSizeRatio() {
        return cameraLensSizeRatio;
    }

    public void setCameraLensSizeRatio(@FloatRange(from = 0.0, to = 1.0) float cameraLensSizeRatio) {
        this.cameraLensSizeRatio = cameraLensSizeRatio;
        requestLayout();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}
