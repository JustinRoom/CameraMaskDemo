package jsc.exam.com.cameramask.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import jsc.exam.com.cameramask.R;
import jsc.exam.com.cameramask.widgets.dialog.BottomShowDialog;
import jsc.kit.cameramask.CameraLensView;
import jsc.kit.cameramask.ScannerBarView;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/CameraMaskDemo" target="_blank">https://github.com/JustinRoom/CameraMaskDemo</a>
 *
 * @author jiangshicheng
 */
public class CameraLensViewFragment extends Fragment {

    private ImageView ivBackground;
    private CameraLensView cameraLensView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera_lens_view, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        ivBackground = root.findViewById(R.id.iv_background);
        cameraLensView = root.findViewById(R.id.camera_lens_view);
        RadioGroup typeRadioGroup = root.findViewById(R.id.radio_group_type);
        typeRadioGroup.check(R.id.radio_type_picture);
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_type_picture:
                        cameraLensView.setCameraLensBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_camera_lens));
                        break;
                    case R.id.radio_type_normal:
                        cameraLensView.setCameraLensBitmap(null);
                        break;
                }
            }
        });

        RadioGroup shapeRadioGroup = root.findViewById(R.id.radio_group_shape);
        shapeRadioGroup.check(R.id.radio_shape_circle);
        shapeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_shape_circle:
                        cameraLensView.setCameraLensShape(CameraLensView.CAMERA_LENS_SHAPE_CIRCULAR);
                        break;
                    case R.id.radio_shape_square:
                        cameraLensView.setCameraLensShape(CameraLensView.CAMERA_LENS_SHAPE_SQUARE);
                        break;
                }
            }
        });
        RadioGroup locationRadioGroup = root.findViewById(R.id.radio_group_location);
        locationRadioGroup.check(R.id.radio_location_below);
        locationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_location_below:
                        cameraLensView.setTextLocation(CameraLensView.BELOW_CAMERA_LENS);
                        break;
                    case R.id.radio_location_above:
                        cameraLensView.setTextLocation(CameraLensView.ABOVE_CAMERA_LENS);
                        break;
                }
            }
        });

        SeekBar topMarginSeekBar = root.findViewById(R.id.seek_bar_top_margin);
        topMarginSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cameraLensView.setCameraLensTopMargin(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar textVerticalMarginSeekBar = root.findViewById(R.id.seek_bar_vertical_margin);
        textVerticalMarginSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cameraLensView.setTextVerticalMargin(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        root.findViewById(R.id.show_camera_lens_rect_bitmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCameraLensRectBitmap();
            }
        });
    }

    private void showCameraLensRectBitmap(){
        ivBackground.setDrawingCacheEnabled(true);
        Bitmap bitmap = ivBackground.getDrawingCache(true);
        bitmap = cameraLensView.cropCameraLensRectBitmap(bitmap, false);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bitmap);
        BottomShowDialog dialog = new BottomShowDialog(getContext());
        dialog.setTitle("ShowBitmapInCameraLensRect");
        dialog.setBitmap(bitmap);
        dialog.show();
    }
}
