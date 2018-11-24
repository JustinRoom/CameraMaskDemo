package jsc.exam.com.cameramask.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jsc.exam.com.cameramask.R;
import jsc.kit.cameramask.ScannerBarView;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * <br><a href="https://github.com/JustinRoom/CameraMaskDemo" target="_blank">https://github.com/JustinRoom/CameraMaskDemo</a>
 *
 * @author jiangshicheng
 */
public class ScannerBarViewFragment extends Fragment {

    private static final String TAG = "ScannerBarViewFragment";
    ScannerBarView scannerBarView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View root = inflater.inflate(R.layout.fragment_scanner_bar_view, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        scannerBarView = root.findViewById(R.id.scanner_view);
        root.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerBarView.start();
            }
        });
        root.findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerBarView.pause();
            }
        });
        root.findViewById(R.id.btn_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerBarView.resume();
            }
        });
        root.findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerBarView.stop();
            }
        });
        root.findViewById(R.id.btn_change_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = scannerBarView.getLayoutParams();
                params.width = getResources().getDimensionPixelSize(R.dimen.space_256);
                scannerBarView.setLayoutParams(params);
            }
        });
        root.findViewById(R.id.btn_change_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerBarView.setScannerBarImageResource(R.drawable.camera_mask_scanner_line);
            }
        });
    }
}
