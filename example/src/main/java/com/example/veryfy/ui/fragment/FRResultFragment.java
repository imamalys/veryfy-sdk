package com.example.veryfy.ui.fragment;

import static com.ionnex.veryfy.constants.Constant.FR_MODEL;
import static com.ionnex.veryfy.constants.Constant.RETRY_COUNT;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.UriUtils;
import com.bumptech.glide.Glide;
import com.example.veryfy.databinding.FragmentFRResultBinding;
import com.ionnex.veryfy.model.FRModel;

import java.io.File;

public class FRResultFragment extends Fragment {

    private FragmentFRResultBinding binding;
    private FRModel frModel;
    int retryCount = 0;
    private File selfieImage;
    private FRResultFragmentListener mListener;

    public FRResultFragment() {
        // Required empty public constructor
    }

    public static FRResultFragment newInstance(Bundle bundle) {
        FRResultFragment fragment = new FRResultFragment();
        fragment.setArguments(bundle);
        return fragment;
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

        binding = FragmentFRResultBinding.inflate(getLayoutInflater());

        if (getArguments() != null) {
            frModel = getArguments().getParcelable(FR_MODEL);
            retryCount = getArguments().getInt(RETRY_COUNT);
            selfieImage = UriUtils.uri2File(Uri.parse(getArguments().getString( "SELFIE_IMAGE")));
        }

        if (retryCount == 2) {
            binding.btnSelfieResult.setEnabled(false);
        }
        if (frModel.getScore() != null) {
            if (Float.parseFloat(String.valueOf(frModel.getScore())) < 80) {
                binding.btnDone.setEnabled(false);
                binding.tvUnderScore.setVisibility(View.VISIBLE);
                binding.llFrScore.setVisibility(View.GONE);

            }
            if (retryCount == 2 && Float.parseFloat(String.valueOf(frModel.getScore())) < 80) {
                binding.tvUnderScore.setText("Verification Failed\nSelfie did not match!");
                binding.tvUnderScore.setVisibility(View.VISIBLE);
                binding.tvUnderScoreDetail.setVisibility(View.VISIBLE);
                binding.btnTryAgain.setVisibility(View.VISIBLE);
                binding.btnSelfieResult.setVisibility(View.GONE);
                binding.btnDone.setVisibility(View.GONE);
            }

            binding.score.setText(String.format("%s%%", frModel.getScore()));
        } else {
            if (Float.parseFloat(String.valueOf(frModel.getImageBestLiveness().getScore())) < 80) {
                binding.btnDone.setEnabled(false);
                binding.tvUnderScore.setVisibility(View.VISIBLE);
                binding.llFrScore.setVisibility(View.GONE);

            }
            if (retryCount == 2 && Float.parseFloat(String.valueOf(frModel.getImageBestLiveness().getScore())) < 80) {
                binding.tvUnderScore.setText("Verification Failed\nSelfie did not match!");
                binding.tvUnderScore.setVisibility(View.VISIBLE);
                binding.tvUnderScoreDetail.setVisibility(View.VISIBLE);
                binding.btnTryAgain.setVisibility(View.VISIBLE);
                binding.btnSelfieResult.setVisibility(View.GONE);
                binding.btnDone.setVisibility(View.GONE);
            }

            binding.score.setText(String.format("%s%%", frModel.getImageBestLiveness().getScore()));
        }

        Glide.with(requireContext()).load(selfieImage).into(binding.captureSelfieResult);

        binding.btnSelfieResult.setOnClickListener(v-> {
            mListener.onRetryFR();
        });

        binding.btnTryAgain.setOnClickListener(v-> {
            mListener.onTryAgainFR();
        });

        binding.btnDone.setOnClickListener(v-> {
            mListener.onDone();
        });
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (FRResultFragmentListener) context;
    }

    public interface FRResultFragmentListener {
        void onRetryFR();
        void onTryAgainFR();
        void onDone();
    }
}