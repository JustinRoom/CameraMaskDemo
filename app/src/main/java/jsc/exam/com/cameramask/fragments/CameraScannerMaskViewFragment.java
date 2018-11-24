package jsc.exam.com.cameramask.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jsc.exam.com.cameramask.R;
import jsc.kit.cameramask.CameraScannerMaskView;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/CameraMaskDemo" target="_blank">https://github.com/JustinRoom/CameraMaskDemo</a>
 *
 * @author jiangshicheng
 */
public class CameraScannerMaskViewFragment extends Fragment {

    private CameraScannerMaskView cameraLensView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera_scanner_mask_view, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        cameraLensView = root.findViewById(R.id.camera_scanner_mask_view);
        root.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraLensView.start();
                getView().findViewById(R.id.btn_start).setEnabled(false);
                getView().findViewById(R.id.btn_pause).setEnabled(true);
                getView().findViewById(R.id.btn_resume).setEnabled(false);
                getView().findViewById(R.id.btn_stop).setEnabled(true);
            }
        });
        root.findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraLensView.pause();
                getView().findViewById(R.id.btn_start).setEnabled(false);
                getView().findViewById(R.id.btn_pause).setEnabled(false);
                getView().findViewById(R.id.btn_resume).setEnabled(true);
                getView().findViewById(R.id.btn_stop).setEnabled(true);
            }
        });
        root.findViewById(R.id.btn_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraLensView.resume();
                getView().findViewById(R.id.btn_start).setEnabled(false);
                getView().findViewById(R.id.btn_pause).setEnabled(true);
                getView().findViewById(R.id.btn_resume).setEnabled(false);
                getView().findViewById(R.id.btn_stop).setEnabled(true);
            }
        });
        root.findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraLensView.stop();
                getView().findViewById(R.id.btn_start).setEnabled(true);
                getView().findViewById(R.id.btn_pause).setEnabled(false);
                getView().findViewById(R.id.btn_resume).setEnabled(false);
                getView().findViewById(R.id.btn_stop).setEnabled(false);
            }
        });
    }
}
