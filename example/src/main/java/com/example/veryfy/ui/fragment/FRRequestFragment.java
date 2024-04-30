package com.example.veryfy.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.veryfy.databinding.FragmentFRRequestBinding;
import com.ionnex.veryfy.ui.CameraActivityVeryfy;

import java.io.File;

public class FRRequestFragment extends Fragment {

    FragmentFRRequestBinding binding;
    private FRFragmentListener mListener;
    private File selfieFile;

    ActivityResultLauncher<Intent> mIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                selfieFile = (File) result.getData().getExtras().get("file");
                Glide.with(requireContext()).load(selfieFile).into(binding.captureSelfie);
                binding.captureSelfie.setVisibility(View.VISIBLE);
                binding.captureSelfieBg.setVisibility(View.GONE);
                mListener.onGetFRResult(selfieFile);
            });

    public FRRequestFragment() {
        // Required empty public constructor
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);
            } else {
                checkRequestSelected();
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 112);
            } else {
                checkRequestSelected();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            checkRequestSelected();
        } else {
            checkPermission();
        }
    }

    private void checkRequestSelected(){
        Intent newIntent = new Intent(requireActivity(), CameraActivityVeryfy.class);
        newIntent.putExtra("selfie", true);
        mIntent.launch(newIntent);
    }

    public static FRRequestFragment newInstance(Bundle bundle) {
        FRRequestFragment fragment = new FRRequestFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (FRFragmentListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFRRequestBinding.inflate(getLayoutInflater());

        binding.btnSelfie.setOnClickListener(v-> {
            checkPermission();
        });

        return binding.getRoot();
    }

    public interface FRFragmentListener {
        void onGetFRResult(File selfieImage);
    }
}