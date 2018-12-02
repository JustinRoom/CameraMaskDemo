package jsc.exam.com.cameramask.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jsc.exam.com.cameramask.R;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/CameraMaskDemo" target="_blank">https://github.com/JustinRoom/CameraMaskDemo</a>
 *
 * @author jiangshicheng
 */
public class BottomShowDialog extends Dialog {

    CharSequence title;
    Bitmap bitmap;

    public BottomShowDialog( @NonNull Context context) {
        this(context, R.style.Theme_AppCompat_Dialog);
    }

    public BottomShowDialog( @NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        //
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setText(title);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setTextColor(0xFF333333);
        textView.setPadding(0, 16, 0, 16);
        layout.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 24;
        params.bottomMargin = 24;
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bitmap);
        layout.addView(imageView, params);
        setContentView(layout);

        if (getWindow() != null) {
            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(null);
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
